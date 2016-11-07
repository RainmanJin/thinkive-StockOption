/*
 * Copyright (c) 2007 Your Corporation. All Rights Reserved.
 */

package com.thinkive.server;

import java.net.InetAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

/**
 * ����:
 * ��Ȩ:	 Copyright (c) 2007
 * ��˾:	 ˼�ϿƼ�
 * ����:	 �����
 * �汾:	 1.0
 * ��������: 2007-11-24
 * ����ʱ��: 15:10:32
 */
public class SocketClient {
    //�ͻ�ͨ��
    private SocketChannel channel = null;
    //�ͻ����ķ���ʱ��
    private long lastAccessTime = 0;
    //�ͻ����ӵ�ʱ��
    private long connTime = 0;
    //���ڵ��ӷ�Ӧ����
    private SubReactor subReactor = null;
    //ͨ����ʶ
    private String uniqueKey = "";


    /**
     * �ͻ�socket��װ����
     */
    public SocketClient() {
    }

    /**
     * ���ؿͻ���ͨ��
     */
    public SocketChannel getSocketChannel() {
        return channel;
    }

    public void setSocketChannel(SocketChannel channel) {
        this.channel = channel;
    }

    /**
     * �жϿͻ��Ƿ��Ѿ��ر�
     */
    public boolean isClosed() {
        if (channel.socket() != null) {
            return channel.socket().isClosed();
        }
        return true;
    }

    /**
     * ���ؿͻ���IP��ַ
     */
    public String getIP() {
        Socket socket = channel.socket();
        if (socket != null) {
            InetAddress address = socket.getInetAddress();
            if (address != null) {
                return address.getHostAddress();
            }
        }
        return "";
    }


    public int getPort() {
        Socket socket = channel.socket();
        if (socket != null) {
            return socket.getPort();
        }
        return 0;
    }

    /**
     * ��ÿͻ�������ʱ��
     */
    public long getLastAccessTime() {
        return lastAccessTime;
    }

    /**
     * ���ÿͻ�������ʱ��
     */
    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    /**
     * ��ÿͻ�����ʱ��
     */
    public long getConnTime() {
        return connTime;
    }

    /**
     * ���ÿͻ�����ʱ��
     */
    public void setConnTime(long connTime) {
        this.connTime = connTime;
    }

    /**
     * ��ȡ�͵�ǰ�ͻ���������ӷ�Ӧ��
     */
    public SubReactor getSubReactor() {
        return subReactor;
    }

    /**
     * ���ú͵�ǰ�ͻ���������ӷ�Ӧ��
     */
    public void setSubReactor(SubReactor subReactor) {
        this.subReactor = subReactor;
    }

    /**
     * ��ȡ�ͻ�Ψһ��KEY
     */
    public String getUniqueKey() {
        return uniqueKey;
    }

    /**
     * ���ÿͻ�Ψһ��KEY
     */
    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }


}