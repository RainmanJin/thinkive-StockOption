package com.thinkive.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 描述:  用于向客户端发送数据
 * 版权:	 Copyright (c) 2005
 * 公司:	 思迪科技
 * 作者:	 易庆锋
 * 版本:	 1.0
 * 创建日期: 2007-3-28
 * 创建时间: 10:32:56
 */
public class SocketResponse {
    private SocketChannel client;

    /**
     * 构造响应对象
     */
    public SocketResponse(SocketChannel client) {
        this.client = client;
    }

    /**
     * 向客户端写数据
     *
     * @param data byte[]　待回应数据
     */
    public void write(byte[] data) throws IOException {
        /*
        ByteBuffer buffer = ByteBuffer.allocate(data.length);
        buffer.put(data, 0, data.length);
        buffer.flip();
        */
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.flip();
        //可能一次不能发送完，所以要进行循环，直到数据发送完成
        while (buffer.hasRemaining()) {
            client.write(buffer);
        }
        //buffer.clear();
    }

    /**
     * 向客户端写数据
     *
     * @param buffer 数据缓冲区
     */
    public void write(ByteBuffer buffer) throws IOException {
        synchronized (client) {
            //可能一次不能发送完，所以要进行循环，直到数据发送完成
            while (buffer.hasRemaining()) {
                client.write(buffer);
            }
        }

    }
}
