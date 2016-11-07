package com.thinkive.market.service.conn;

/**
 * @描述: 返回数据包
 * @版权: Copyright (c) 2012
 * @公司: 思迪科技
 * @作者: 岳知之
 * @版本: 1.0
 * @创建日期: 2012-4-6
 * @创建时间: 上午10:23:32
 */
public class PackageData {
    private byte[] data;        //包数据

    private int msgTypeNo;   // 消息类型编号

    private int msgVersionNo; // 消息版本编号

    private int bodyLength;  //  包数据长度
    private int keepField; // 保留字段

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getMsgTypeNo() {
        return msgTypeNo;
    }

    public void setMsgTypeNo(int msgTypeNo) {
        this.msgTypeNo = msgTypeNo;
    }

    public int getMsgVersionNo() {
        return msgVersionNo;
    }

    public void setMsgVersionNo(int msgVersionNo) {
        this.msgVersionNo = msgVersionNo;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public int getKeepField() {
        return keepField;
    }

    public void setKeepField(int keepField) {
        this.keepField = keepField;
    }
}
