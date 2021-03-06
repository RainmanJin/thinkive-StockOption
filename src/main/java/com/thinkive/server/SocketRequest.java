package com.thinkive.server;

import java.net.InetAddress;
import java.nio.channels.SocketChannel;

/**
 * 描述:  用于封装客户端的请求
 * 版权:	 Copyright (c) 2005
 * 公司:	 思迪科技
 * 作者:	 易庆锋
 * 版本:	 1.0
 * 创建日期: 2007-3-28
 * 创建时间: 10:32:56
 */
public class SocketRequest {
    private SocketChannel client = null;
    private byte[] data = new byte[0];
    private byte[] buffer = new byte[0];
    private Object attachObj = null;

    /**
     * 构造客户端的请求
     */
    public SocketRequest(SocketChannel client) {
        this.client = client;
    }

    /**
     * 返回客户数据存储缓冲区
     */
    public byte[] getBuffer() {
        return buffer;
    }

    /**
     * 设置客户数据缓冲区
     */
    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    /**
     * 清除缓冲区
     */
    public void clearBuffer() {
        this.buffer = null;
        this.data = null;
        this.attachObj = null;
    }

    /**
     * 返回客户端通道
     */
    public SocketChannel getSocketChannel() {
        return client;
    }

    /**
     * 获得客户端的地址
     */
    public InetAddress getAddress() {
        return client.socket().getInetAddress();
    }

    /**
     * 获得客户端的端口
     */
    public int getPort() {
        return client.socket().getPort();
    }

    /**
     * 判断客户是否已经连接
     */
    public boolean isConnected() {
        return client.isConnected();
    }

    /**
     * 判断客户通道是否已经关闭
     */
    public boolean isClosed() {
        return client.socket().isClosed();
    }

    /**
     * 在Request上附加其它对象
     */
    public void attach(Object attachObj) {
        this.attachObj = attachObj;
    }

    /**
     * 返回附加的对象
     */
    public Object attachment() {
        return attachObj;
    }

    /**
     * 返回获得的客户端的数据
     */
    public byte[] getData() {
        return data;
    }

    /**
     * 设置请求上得到的数据
     */
    public void setData(byte[] data) {
        this.data = data;
    }
}
