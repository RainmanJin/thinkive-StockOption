package com.thinkive.server;

import java.nio.channels.SocketChannel;
import java.net.InetAddress;

/**
 * ����:  ���ڷ�װ�ͻ��˵�����
 * ��Ȩ:	 Copyright (c) 2005
 * ��˾:	 ˼�ϿƼ�
 * ����:	 �����
 * �汾:	 1.0
 * ��������: 2007-3-28
 * ����ʱ��: 10:32:56
 */
public class SocketRequest
{
    private SocketChannel client = null;
    private byte[] data = new byte[0];
    private byte[] buffer = new byte[0];
    private Object attachObj = null;

    /**
     * ����ͻ��˵�����
     *
     * @param client
     */
    public SocketRequest(SocketChannel client)
    {
        this.client = client;
    }

    /**
     * ���ؿͻ����ݴ洢������
     *
     * @return
     */
    public byte[] getBuffer()
    {
        return buffer;
    }

    /**
     * ���ÿͻ����ݻ�����
     *
     * @param buffer
     */
    public void setBuffer(byte[] buffer)
    {
        this.buffer = buffer;
    }

    /**
     * ���������
     */
    public void clearBuffer()
    {
        this.buffer = null;
        this.data = null;
        this.attachObj = null;
    }

    /**
     * ���ؿͻ���ͨ��
     *
     * @return
     */
    public SocketChannel getSocketChannel()
    {
        return client;
    }

    /**
     * ��ÿͻ��˵ĵ�ַ
     *
     * @return
     */
    public InetAddress getAddress()
    {
        return client.socket().getInetAddress();
    }

    /**
     * ��ÿͻ��˵Ķ˿�
     *
     * @return
     */
    public int getPort()
    {
        return client.socket().getPort();
    }

    /**
     * �жϿͻ��Ƿ��Ѿ�����
     *
     * @return
     */
    public boolean isConnected()
    {
        return client.isConnected();
    }

    /**
     * �жϿͻ�ͨ���Ƿ��Ѿ��ر�
     *
     * @return
     */
    public boolean isClosed()
    {
        return client.socket().isClosed();
    }

    /**
     * ��Request�ϸ�����������
     *
     * @param attachObj
     */
    public void attach(Object attachObj)
    {
        this.attachObj = attachObj;
    }

    /**
     * ���ظ��ӵĶ���
     *
     * @return
     */
    public Object attachment()
    {
        return attachObj;
    }

    /**
     * ���ػ�õĿͻ��˵�����
     *
     * @return
     */
    public byte[] getData()
    {
        return data;
    }

    /**
     * ���������ϵõ�������
     *
     * @param data
     */
    public void setData(byte[] data)
    {
        this.data = data;
    }
}
