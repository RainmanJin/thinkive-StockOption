/*
 * Copyright (c) 2007 Your Corporation. All Rights Reserved.
 */

package com.thinkive.server;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.thinkive.base.util.StringHelper;
import com.thinkive.market.Library;
import com.thinkive.market.common.InvokeException;
import com.thinkive.market.common.ServiceInvoke;
import com.thinkive.market.common.impl.RequestImpl;
import com.thinkive.market.common.impl.ResponseImpl;
import com.thinkive.server.event.EventAdapter;

/**
 * 描述:
 * 版权:   Copyright (c) 2007
 * 公司:   思迪科技
 * 作者:   易庆锋
 * 版本:   1.0
 * 创建日期: 2007-11-20
 * 创建时间: 17:16:58
 */
public class ServerHandler extends EventAdapter
{
    private static Logger logger               = Logger.getLogger(ServerHandler.class);
    
    private static String SOH                  = new String("\1");
    
    private static String STX                  = new String("\2");
    
    private final int     REQUEST_HEAD_LENGTH  = 35;                                   //请求包头的长度
                                                                                        
    private final int     RESPONSE_HEAD_LENGTH = 35;                                   //返回包头长度
                                                                                        
    private int           DEFAULT_ERRORNO      = -1;                                   //缺省错误号
                                                                                        
    private int           DEFAULT_SUCCESSNO    = 0;
 
    
    
    /**
     * 包正常到达时调用
     *
     * @param request
     * @param response
     */
    public void onDataArrival(SocketRequest request, SocketResponse response)
    {
        byte[] data = request.getData(); //包含包头和包体在一起的数据
        ByteBuffer dataBuffer = ByteBuffer.wrap(data, 2, data.length-2);
        dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
        // 头部数据已经收到，继续往下处理
        int msgVersionNo = dataBuffer.getInt(); // 消息版本编号
        char msgType = (char)dataBuffer.get(); // 消息版本编号
        int bodyLength = dataBuffer.getInt(); // 包体长度
        int origbodyLength = dataBuffer.getInt(); // 包体原始长度
        short branchID = dataBuffer.getShort(); // 分支号
        short commandID = dataBuffer.getShort(); //功能号
        int flowNo = dataBuffer.getInt(); // 流水号
        int errorNo = dataBuffer.getInt(); // error号
        dataBuffer.get(new byte[8]);//Reserved    Byte(8) 保留字段

        System.out.println("接受包总长度:"+data.length+",commandID="+commandID);
        SocketClient client = StateManager.getClientChannelMap().get(request.getSocketChannel());
        if ( client != null )
        {
            RequestImpl req = new RequestImpl();
            ResponseImpl resp = new ResponseImpl();
            byte[] body = new byte[bodyLength];
            dataBuffer.get(body, 0, bodyLength);
            req.setFuncNo(commandID);
            req.setVersionNo(msgVersionNo);
            req.setBranchNo(branchID);
            req.setFlowNo(flowNo);
            req.setData(body);
//            initRequest(req, body);
            //功能号为0则为心跳包
            if(req.getFuncNo()==0){
                writeHeartResponse(response, req);
            }else{
                try
                {
                    ServiceInvoke.Invoke(req, resp);
                    writeSuccessResponse(response,req, resp);
                }
                catch (InvokeException e)
                {
                    e.printStackTrace();
                    writeErrorResponse(response,req);
                }
            }
        }
    }
    
    /**
     * 发送回心跳响应包
     *
     * @param response
     */
    private boolean writeHeartResponse(SocketResponse response,Request request)
    {
        try
        {
            ByteBuffer dataBuffer = ByteBuffer.allocate(RESPONSE_HEAD_LENGTH);
            dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
            int bagLen=0  ;
            dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
            dataBuffer.put((byte) 'T').put((byte) 'K');
            dataBuffer.putInt((int)request.getVersionNo());
            dataBuffer.put((byte)0);
            dataBuffer.putInt(bagLen);//BagLen  UINT    传输长度
            dataBuffer.putInt(0);//OrigLen Int 包体原始长度
            dataBuffer.putShort((short)0);//BranchID    Short   0
            dataBuffer.putShort((short)request.getFuncNo());//CommandID   Short   1002
            dataBuffer.putInt(request.getFlowNo());//FlowNo  Int 流水号
            dataBuffer.putInt(1);//error号
            dataBuffer.put(new byte[8]);//Reserved    Byte(8) 保留字段
            
            dataBuffer.flip();
            response.write(dataBuffer);
            return true;
        }
        catch (Exception ex)
        {
            logger.info("", ex);
        }
        return false;
    }
    
    /**
     * 发送内部错误回应包
     *
     * @param response
     */
    private boolean writeErrorResponse(SocketResponse response,Request request)
    {
        try
        {
            ByteBuffer dataBuffer = ByteBuffer.allocate(RESPONSE_HEAD_LENGTH);
            int bagLen=0  ;
            dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
            dataBuffer.put((byte) 'T').put((byte) 'K');
            dataBuffer.putInt((int)request.getVersionNo());
            dataBuffer.put((byte)0);
            dataBuffer.putInt(bagLen);//BagLen  UINT    传输长度
            dataBuffer.putInt(0);//OrigLen Int 包体原始长度
            dataBuffer.putShort((short)request.getBranchNo());//BranchID    Short   1
            dataBuffer.putShort((short)request.getFuncNo());//CommandID   Short   1002
            dataBuffer.putInt(request.getFlowNo());//FlowNo  Int 流水号
            dataBuffer.putInt(-1);//error号
            dataBuffer.put(new byte[8]);//Reserved    Byte(8) 保留字段
            
            dataBuffer.flip();
            response.write(dataBuffer);
            return true;
        }
        catch (Exception ex)
        {
            logger.info("", ex);
        }
        return false;
    }
    
    private boolean writeSuccessResponse(SocketResponse response, Request request,ResponseImpl resp)
    {
        try
        {
            byte[] body = resp.getData();
            if ( body == null )
            {
                writeErrorResponse(response,request);
                return false;
            }
            
            int bagLen= body.length;
            ByteBuffer dataBuffer = ByteBuffer.allocate(bagLen+RESPONSE_HEAD_LENGTH);
            dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
            dataBuffer.put((byte) 'T').put((byte) 'K');
            dataBuffer.putInt((int)request.getVersionNo());
            dataBuffer.put((byte)0);
            dataBuffer.putInt(bagLen);//BagLen  UINT    传输长度
            dataBuffer.putInt(0);//OrigLen Int 包体原始长度
            dataBuffer.putShort((short)request.getBranchNo());//BranchID    Short   1
            dataBuffer.putShort((short)request.getFuncNo());//CommandID   Short   1002
            dataBuffer.putInt(request.getFlowNo());//FlowNo  Int 流水号
            dataBuffer.putInt(0);//error号
            dataBuffer.put(new byte[8]);//Reserved    Byte(8) 保留字段
            dataBuffer.put(body);
            dataBuffer.flip();
            response.write(dataBuffer);
            System.out.println("输出包总长度:"+bagLen+",commandID="+request.getFuncNo());
            return true;
        }
        catch (Exception ex)
        {
            logger.info("", ex);
        }
        return false;
    }
    
    /**
     * 客户端关闭时调用
     *
     * @param request
     */
    public void onClosed(SocketRequest request)
    {
        
    }
    
    /**
     * 解包
     *
     * @param data
     * @return
     * @throws
     */
    public Map initRequest(RequestImpl request, byte[] data)
    {
        Map packMap = null;
        try
        {
            String body = new String(data, Library.getEncoding());
            String[] valueArray = StringHelper.split(body, STX);
            if ( valueArray != null && valueArray.length == 2 )
            {
                String[] keys = StringHelper.split(valueArray[0], SOH);
                String[] values = StringHelper.split(valueArray[1], SOH);
                if ( keys.length == values.length )
                {
                    packMap = new HashMap();
                    for (int i = 0; i < values.length; i++)
                    {
                        request.addParameter(keys[i], values[i]);
                    }
                    return packMap;
                }
            }
        }
        catch (Exception ex)
        {
            logger.error("", ex);
        }
        return null;
    }
    
}
