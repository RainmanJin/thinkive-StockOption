package com.thinkive.market.service.conn;

/**
 * @����: �������ݰ�
 * @��Ȩ: Copyright (c) 2012
 * @��˾: ˼�ϿƼ�
 * @����: ��֪֮
 * @�汾: 1.0
 * @��������: 2012-4-6
 * @����ʱ��: ����10:23:32
 */
public class PackageData {
    private byte[] data;        //������

    private int msgTypeNo;   // ��Ϣ���ͱ��

    private int msgVersionNo; // ��Ϣ�汾���

    private int bodyLength;  //  �����ݳ���
    private int keepField; // �����ֶ�

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
