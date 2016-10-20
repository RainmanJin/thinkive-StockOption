package com.thinkive.server;

import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import java.io.IOException;

/**
 * ����:  ������ͻ��˷�������
 * ��Ȩ:	 Copyright (c) 2005
 * ��˾:	 ˼�ϿƼ�
 * ����:	 �����
 * �汾:	 1.0
 * ��������: 2007-3-28
 * ����ʱ��: 10:32:56
 */
public class SocketResponse
{
    private SocketChannel client;

    /**
     * ������Ӧ����
     *
     * @param client
     */
    public SocketResponse(SocketChannel client)
    {
        this.client = client;
    }

    /**
     * ��ͻ���д����
     *
     * @param data byte[]������Ӧ����
     */
    public void write(byte[] data) throws IOException
    {
        /*
        ByteBuffer buffer = ByteBuffer.allocate(data.length);
        buffer.put(data, 0, data.length);
        buffer.flip();
        */
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.flip();
        //����һ�β��ܷ����꣬����Ҫ����ѭ����ֱ�����ݷ������
        while (buffer.hasRemaining())
        {
            client.write(buffer);
        }
        //buffer.clear();
    }

    /**
     * ��ͻ���д����
     *
     * @param buffer ���ݻ�����
     * @throws IOException
     */
    public void write(ByteBuffer buffer) throws IOException
    {
        synchronized (client)
        {
            //����һ�β��ܷ����꣬����Ҫ����ѭ����ֱ�����ݷ������
            while (buffer.hasRemaining())
            {
                client.write(buffer);
            }
        }

    }
}
