/*
 * Copyright (c) 2011 Your Corporation. All Rights Reserved.
 */

package com.thinkive.server;

import com.thinkive.base.config.Configuration;
import com.thinkive.base.util.Console;
import com.thinkive.base.util.DateHelper;
import com.thinkive.base.util.StringHelper;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;
import java.util.Date;

/**
 * 描述:
 * 版权:   Copyright (c) 2009
 * 公司:   思迪科技
 * 作者:   易庆锋
 * 版本:   1.0
 * 创建日期: 2011-9-9
 * 创建时间: 11:49:36
 */
public class CheckStateThread extends Thread
{
    private Logger logger = Logger.getLogger(CheckStateThread.class);
    private static String SOH = new String("\1");
    private static String STX = new String("\2");
    private final int REQUEST_HEAD_LENGTH = 18;     //请求包头的长度
    private final int RESPONSE_HEAD_LENGTH = 18;     //返回包头长度

    private long lastCheckServerTime = 0;     //最后检测服务器时间
    private long lastCheckClientTime = 0;     //最后检测客户时间
    private long lastServerConnectedTime = 0;  //中心服务器最后连通时间

    public void run()
    {
        lastCheckServerTime = System.currentTimeMillis();
        lastCheckClientTime = System.currentTimeMillis();
        lastServerConnectedTime = System.currentTimeMillis();
        sendPortInfo();
        while (true)
        {
            try
            {
                if (System.currentTimeMillis() - lastCheckServerTime > 1000)  //1秒种检测一下服务器
                {
                    checkServerState();
                    lastCheckServerTime = System.currentTimeMillis();
                }

                if (System.currentTimeMillis() - lastCheckClientTime > 60 * 1000) //1分钟检测一下客户
                {
                    if (StateManager.isCenterServerRuning()) //中心服务器正常的时候处理
                    {
                        checkClientState();
                    }
                    lastCheckClientTime = System.currentTimeMillis();
                }
                Thread.sleep(50);
            }
            catch (Exception ex)
            {
            }
        }
    }

    /**
     * 向中心服务器发送本接入服务器接收中心服务器消息端口
     * @param ip
     * @param port
     */
    private void sendPortInfo()
    {
        try
        {
            String ip = Configuration.getString("centerServer.ip");
            int port = Configuration.getInt("centerServer.hPort");
            //String val[] = new String[]{};
            //byte date[] = packArray(4,val);
            
            ByteBuffer dataBuffer = ByteBuffer.allocate(REQUEST_HEAD_LENGTH);
            dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
            dataBuffer.put((byte) 'T').put((byte) 'K');
            dataBuffer.putInt(4);
            dataBuffer.putInt(0);
            dataBuffer.putInt(Configuration.getInt("innerServer.port"));
            dataBuffer.putInt(0);
            dataBuffer.flip();
            
            Socket socket = new Socket(ip, port);
            OutputStream outStream = socket.getOutputStream();
            InputStream inStream = socket.getInputStream();
            outStream.write(dataBuffer.array());
            outStream.flush();
        
        }
        catch(Exception e)
        {
            logger.error("发送接入服务器监听端口异常",e);
        }
    }
    
    /**
     * 检测中心服务器的存活状态
     */
    private void checkServerState()
    {
        String ip = Configuration.getString("centerServer.ip");
        int port = Configuration.getInt("centerServer.hPort");
        int serverTimeout = Configuration.getInt("server.serverTimeout");


        if (sendAndReciveHeartMessage(ip, port)) //发送和接收成功
        {
            lastServerConnectedTime = System.currentTimeMillis();
            StateManager.setCenterServerRuning(true);
        }

        if (System.currentTimeMillis() - lastServerConnectedTime > serverTimeout * 1000) //中心服务器已经超时
        {
            if (Configuration.getBoolean("general.isDebug"))
            {
                Console.println("中心服务器[" + ip + ":" + port + "]已经超时无法连接[" + DateHelper.formatDate(new Date()) + "]");
            }

            StateManager.setCenterServerRuning(false);
            //主动关闭所有当前接入服务器上的客户
            Object[] clientArray = StateManager.getClientChannelMap().values().toArray();
            if (clientArray != null && clientArray.length > 0)
            {
                if (Configuration.getBoolean("general.isDebug"))
                {
                    Console.println("接入服务器关闭所有已经连接客户");
                }
                for (int i = 0; i < clientArray.length; i++)
                {
                    SocketClient client = (SocketClient) clientArray[i];

                    SocketChannel channel = client.getSocketChannel();
                    StateManager.getClientChannelMap().remove(channel);
                    StateManager.getClientKeyMap().remove(client.getUniqueKey());
                    closeChannel(channel);
                }
            }
        }
    }


    /**
     * 发送和接收心跳包，若发送和接收成功，则返回true，否则返回false
     *
     * @return
     */
    private boolean sendAndReciveHeartMessage(String ip, int port)
    {
        Socket socket = null;
        OutputStream outStream = null;
        InputStream inStream = null;

        try
        {
            ByteBuffer dataBuffer = ByteBuffer.allocate(REQUEST_HEAD_LENGTH);
            dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
            dataBuffer.put((byte) 'T').put((byte) 'K');
            dataBuffer.putInt(1);
            dataBuffer.putInt(0);
            dataBuffer.putInt(0);
            dataBuffer.putInt(0);
            dataBuffer.flip();

            socket = new Socket(ip, port);
            outStream = socket.getOutputStream();
            inStream = socket.getInputStream();
            outStream.write(dataBuffer.array());
            outStream.flush();

            byte[] buffer = new byte[RESPONSE_HEAD_LENGTH];
            readFixedLenToBuffer(inStream, buffer);
            dataBuffer = ByteBuffer.wrap(buffer);
            dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
            dataBuffer.position(2);
            int msgType = dataBuffer.getInt();
            if (msgType == 2) //是正确的心跳回应包
            {
                return true;
            }
        }
        catch (Exception ex)
        {
        }
        finally
        {
            try
            {
                if (inStream != null)
                {
                    inStream.close();
                }
                if (outStream != null)
                {
                    outStream.close();
                }
                if (socket != null)
                {
                    socket.close();
                }
            }
            catch (Exception ex)
            {
            }
        }
        return false;
    }

    /**
     * 读取固定长度的数据到患冲区中
     *
     * @param inStream
     * @param buffer
     * @throws Exception
     */
    private void readFixedLenToBuffer(InputStream inStream, byte[] buffer) throws Exception
    {
        int count = 0;
        int remainLength = buffer.length;
        int bufLength = buffer.length;
        int readLength = 0;
        do
        {
            count = inStream.read(buffer, readLength, remainLength);
            if (count == -1) //已经到达末尾
            {
                if (readLength != bufLength) //若实际读取的数据和需要读取的数据不匹配，则报错
                {
                    throw new Exception("read data wrong");
                }
            }

            readLength += count;

            if (readLength == bufLength) //已经读取完，则返回
            {
                return;
            }

            remainLength = bufLength - readLength;
        }
        while (true);
    }

    /**
     * 关闭通道
     *
     * @param channel
     */
    private void closeChannel(SocketChannel channel)
    {
        try
        {
            if (channel != null)
            {
                channel.close();
            }
        }
        catch (Exception ex)
        {
        }
    }

    /**
     * 检测客户的连接的状态
     */
    private void checkClientState()
    {
        int clientTimeout = Configuration.getInt("server.clientTimeout", 1);

        Object[] clientArray = StateManager.getClientChannelMap().values().toArray();
        if (clientArray != null && clientArray.length > 0)
        {
            for (int i = 0; i < clientArray.length; i++)
            {
                SocketClient client = (SocketClient) clientArray[i];
                if (System.currentTimeMillis() - client.getLastAccessTime() > clientTimeout * 60 * 1000)  //当前客户已经断开
                {
                    if (StateManager.isCenterServerRuning())  //此处需要判断，因为可能正在此循环的过程中中心服务器无法连接
                    {
                        sendOffineMessageToServer(client);
                    }

                    SocketChannel channel = client.getSocketChannel();
                    StateManager.getClientChannelMap().remove(channel);
                    StateManager.getClientKeyMap().remove(client.getUniqueKey());
                    closeChannel(channel);
                }
            }
        }
    }

    /**
     * 发送通道断开消息到中心服务器
     *
     * @param client
     */
    private void sendOffineMessageToServer(SocketClient client)
    {
        String ip = Configuration.getString("centerServer.ip");
        int port = Configuration.getInt("centerServer.port");

        try
        {
            if (StringHelper.isNotEmpty(client.getUniqueKey()))
            {
                try
                {
                    if (Configuration.getBoolean("general.isDebug"))
                    {
                        Console.println("通道[" + client.getUniqueKey() + "]超时关闭，发送消息到中心服务器。");
                    }

                    Socket socket = new Socket(ip, port);

                    String uniqueKey = client.getUniqueKey();
                    ByteBuffer dataBuffer = ByteBuffer.allocate(REQUEST_HEAD_LENGTH + uniqueKey.length());
                    dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
                    dataBuffer.put((byte) 'T').put((byte) 'K');
                    dataBuffer.putInt(3);
                    dataBuffer.putInt(0);
                    dataBuffer.putInt(0);
                    dataBuffer.putInt(0);
                    dataBuffer.put(uniqueKey.getBytes());
                    dataBuffer.flip();

                    OutputStream outStream = socket.getOutputStream();
                    outStream.write(dataBuffer.array());
                    outStream.flush();
                    outStream.close();

                    socket.close();

                }
                catch (Exception e)
                {
                    logger.info("", e);
                }
            }
        }
        catch (Exception e)
        {
            logger.info("", e);
        }
    }
}
