package com.thinkive.server.inner;

/**
 * ����:  �����ķ���������������Ϣ��
 * ��Ȩ:	 Copyright (c) 2011
 * ��˾:	 ˼�ϿƼ�
 * ����:	 ŷ����
 * �汾:	 1.0
 * ��������: 2011-6-21
 * ����ʱ��: 9:51:17
 */
public class Message {
    //���ݰ�����
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
