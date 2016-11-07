package com.thinkive.server.inner;

/**
 * 描述:  从中心服务器发送来的消息包
 * 版权:	 Copyright (c) 2011
 * 公司:	 思迪科技
 * 作者:	 欧阳大炯
 * 版本:	 1.0
 * 创建日期: 2011-6-21
 * 创建时间: 9:51:17
 */
public class Message {
    //数据包内容
    private byte[] buffer = new byte[0];


    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public void clear() {
        buffer = null;
    }

}
