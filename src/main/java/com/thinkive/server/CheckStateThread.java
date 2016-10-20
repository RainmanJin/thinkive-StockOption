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
 * ����:
 * ��Ȩ:   Copyright (c) 2009
 * ��˾:   ˼�ϿƼ�
 * ����:   �����
 * �汾:   1.0
 * ��������: 2011-9-9
 * ����ʱ��: 11:49:36
 */
public class CheckStateThread extends Thread
{
    private Logger logger = Logger.getLogger(CheckStateThread.class);
    private static String SOH = new String("\1");
    private static String STX = new String("\2");
    private final int REQUEST_HEAD_LENGTH = 18;     //�����ͷ�ĳ���
    private final int RESPONSE_HEAD_LENGTH = 18;     //���ذ�ͷ����

    private long lastCheckServerTime = 0;     //����������ʱ��
    private long lastCheckClientTime = 0;     //�����ͻ�ʱ��
    private long lastServerConnectedTime = 0;  //���ķ����������ͨʱ��

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
                if (System.currentTimeMillis() - lastCheckServerTime > 1000)  //1���ּ��һ�·�����
                {
                    checkServerState();
                    lastCheckServerTime = System.currentTimeMillis();
                }

                if (System.currentTimeMillis() - lastCheckClientTime > 60 * 1000) //1���Ӽ��һ�¿ͻ�
                {
                    if (StateManager.isCenterServerRuning()) //���ķ�����������ʱ����
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
     * �����ķ��������ͱ�����������������ķ�������Ϣ�˿�
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
            logger.error("���ͽ�������������˿��쳣",e);
        }
    }
    
    /**
     * ������ķ������Ĵ��״̬
     */
    private void checkServerState()
    {
        String ip = Configuration.getString("centerServer.ip");
        int port = Configuration.getInt("centerServer.hPort");
        int serverTimeout = Configuration.getInt("server.serverTimeout");


        if (sendAndReciveHeartMessage(ip, port)) //���ͺͽ��ճɹ�
        {
            lastServerConnectedTime = System.currentTimeMillis();
            StateManager.setCenterServerRuning(true);
        }

        if (System.currentTimeMillis() - lastServerConnectedTime > serverTimeout * 1000) //���ķ������Ѿ���ʱ
        {
            if (Configuration.getBoolean("general.isDebug"))
            {
                Console.println("���ķ�����[" + ip + ":" + port + "]�Ѿ���ʱ�޷�����[" + DateHelper.formatDate(new Date()) + "]");
            }

            StateManager.setCenterServerRuning(false);
            //�����ر����е�ǰ����������ϵĿͻ�
            Object[] clientArray = StateManager.getClientChannelMap().values().toArray();
            if (clientArray != null && clientArray.length > 0)
            {
                if (Configuration.getBoolean("general.isDebug"))
                {
                    Console.println("����������ر������Ѿ����ӿͻ�");
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
     * ���ͺͽ����������������ͺͽ��ճɹ����򷵻�true�����򷵻�false
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
            if (msgType == 2) //����ȷ��������Ӧ��
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
     * ��ȡ�̶����ȵ����ݵ���������
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
            if (count == -1) //�Ѿ�����ĩβ
            {
                if (readLength != bufLength) //��ʵ�ʶ�ȡ�����ݺ���Ҫ��ȡ�����ݲ�ƥ�䣬�򱨴�
                {
                    throw new Exception("read data wrong");
                }
            }

            readLength += count;

            if (readLength == bufLength) //�Ѿ���ȡ�꣬�򷵻�
            {
                return;
            }

            remainLength = bufLength - readLength;
        }
        while (true);
    }

    /**
     * �ر�ͨ��
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
     * ���ͻ������ӵ�״̬
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
                if (System.currentTimeMillis() - client.getLastAccessTime() > clientTimeout * 60 * 1000)  //��ǰ�ͻ��Ѿ��Ͽ�
                {
                    if (StateManager.isCenterServerRuning())  //�˴���Ҫ�жϣ���Ϊ�������ڴ�ѭ���Ĺ��������ķ������޷�����
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
     * ����ͨ���Ͽ���Ϣ�����ķ�����
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
                        Console.println("ͨ��[" + client.getUniqueKey() + "]��ʱ�رգ�������Ϣ�����ķ�������");
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
