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
 * ����:
 * ��Ȩ:   Copyright (c) 2007
 * ��˾:   ˼�ϿƼ�
 * ����:   �����
 * �汾:   1.0
 * ��������: 2007-11-20
 * ����ʱ��: 17:16:58
 */
public class ServerHandler extends EventAdapter
{
    private static Logger logger               = Logger.getLogger(ServerHandler.class);
    
    private static String SOH                  = new String("\1");
    
    private static String STX                  = new String("\2");
    
    private final int     REQUEST_HEAD_LENGTH  = 35;                                   //�����ͷ�ĳ���
                                                                                        
    private final int     RESPONSE_HEAD_LENGTH = 35;                                   //���ذ�ͷ����
                                                                                        
    private int           DEFAULT_ERRORNO      = -1;                                   //ȱʡ�����
                                                                                        
    private int           DEFAULT_SUCCESSNO    = 0;
 
    
    
    /**
     * ����������ʱ����
     *
     * @param request
     * @param response
     */
    public void onDataArrival(SocketRequest request, SocketResponse response)
    {
        byte[] data = request.getData(); //������ͷ�Ͱ�����һ�������
        ByteBuffer dataBuffer = ByteBuffer.wrap(data, 2, data.length-2);
        dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
        // ͷ�������Ѿ��յ����������´���
        int msgVersionNo = dataBuffer.getInt(); // ��Ϣ�汾���
        char msgType = (char)dataBuffer.get(); // ��Ϣ�汾���
        int bodyLength = dataBuffer.getInt(); // ���峤��
        int origbodyLength = dataBuffer.getInt(); // ����ԭʼ����
        short branchID = dataBuffer.getShort(); // ��֧��
        short commandID = dataBuffer.getShort(); //���ܺ�
        int flowNo = dataBuffer.getInt(); // ��ˮ��
        int errorNo = dataBuffer.getInt(); // error��
        dataBuffer.get(new byte[8]);//Reserved    Byte(8) �����ֶ�

        System.out.println("���ܰ��ܳ���:"+data.length+",commandID="+commandID);
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
            //���ܺ�Ϊ0��Ϊ������
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
     * ���ͻ�������Ӧ��
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
            dataBuffer.putInt(bagLen);//BagLen  UINT    ���䳤��
            dataBuffer.putInt(0);//OrigLen Int ����ԭʼ����
            dataBuffer.putShort((short)0);//BranchID    Short   0
            dataBuffer.putShort((short)request.getFuncNo());//CommandID   Short   1002
            dataBuffer.putInt(request.getFlowNo());//FlowNo  Int ��ˮ��
            dataBuffer.putInt(1);//error��
            dataBuffer.put(new byte[8]);//Reserved    Byte(8) �����ֶ�
            
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
     * �����ڲ������Ӧ��
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
            dataBuffer.putInt(bagLen);//BagLen  UINT    ���䳤��
            dataBuffer.putInt(0);//OrigLen Int ����ԭʼ����
            dataBuffer.putShort((short)request.getBranchNo());//BranchID    Short   1
            dataBuffer.putShort((short)request.getFuncNo());//CommandID   Short   1002
            dataBuffer.putInt(request.getFlowNo());//FlowNo  Int ��ˮ��
            dataBuffer.putInt(-1);//error��
            dataBuffer.put(new byte[8]);//Reserved    Byte(8) �����ֶ�
            
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
            dataBuffer.putInt(bagLen);//BagLen  UINT    ���䳤��
            dataBuffer.putInt(0);//OrigLen Int ����ԭʼ����
            dataBuffer.putShort((short)request.getBranchNo());//BranchID    Short   1
            dataBuffer.putShort((short)request.getFuncNo());//CommandID   Short   1002
            dataBuffer.putInt(request.getFlowNo());//FlowNo  Int ��ˮ��
            dataBuffer.putInt(0);//error��
            dataBuffer.put(new byte[8]);//Reserved    Byte(8) �����ֶ�
            dataBuffer.put(body);
            dataBuffer.flip();
            response.write(dataBuffer);
            System.out.println("������ܳ���:"+bagLen+",commandID="+request.getFuncNo());
            return true;
        }
        catch (Exception ex)
        {
            logger.info("", ex);
        }
        return false;
    }
    
    /**
     * �ͻ��˹ر�ʱ����
     *
     * @param request
     */
    public void onClosed(SocketRequest request)
    {
        
    }
    
    /**
     * ���
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
