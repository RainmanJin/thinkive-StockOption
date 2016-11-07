package com.thinkive.server;

import java.net.InetAddress;
import java.nio.channels.SocketChannel;

/**
 * ����:  ���ڷ�װ�ͻ��˵�����
 * ��Ȩ:	 Copyright (c) 2005
 * ��˾:	 ˼�ϿƼ�
 * ����:	 �����
 * �汾:	 1.0
 * ��������: 2007-3-28
 * ����ʱ��: 10:32:56
 */
public class SocketRequest {
    private SocketChannel client = null;
    private byte[] data = new byte[0];
    private byte[] buffer = new byte[0];
    private Object attachObj = null;

    /**
     * ����ͻ��˵�����
     */
    public SocketRequest(SocketChannel client) {
        this.client = client;
    }

    /**
     * ���ؿͻ����ݴ洢������
     */
    public byte[] getBuffer() {
        return buffer;
    }

    /**
     * ���ÿͻ����ݻ�����
     */
    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    /**
     * ���������
     */
    public void clearBuffer() {
        this.buffer = null;
        this.data = null;
        this.attachObj = null;
    }

    /**
     * ���ؿͻ���ͨ��
     */
    public SocketChannel getSocketChannel() {
        return client;
    }

    /**
     * ��ÿͻ��˵ĵ�ַ
     */
    public InetAddress getAddress() {
        return client.socket().getInetAddress();
    }

    /**
     * ��ÿͻ��˵Ķ˿�
     */
    public int getPort() {
        return client.socket().getPort();
    }

    /**
     * �жϿͻ��Ƿ��Ѿ�����
     */
    public boolean isConnected() {
        return client.isConnected();
    }

    /**
     * �жϿͻ�ͨ���Ƿ��Ѿ��ر�
     */
    public boolean isClosed() {
        return client.socket().isClosed();
    }

    /**
     * ��Request�ϸ�����������
     */
    public void attach(Object attachObj) {
        this.attachObj = attachObj;
    }

    /**
     * ���ظ��ӵĶ���
     */
    public Object attachment() {
        return attachObj;
    }

    /**
     * ���ػ�õĿͻ��˵�����
     */
    public byte[] getData() {
        return data;
    }

    /**
     * ���������ϵõ�������
     */
    public void setData(byte[] data) {
        this.data = data;
    }
}
