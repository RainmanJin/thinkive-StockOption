package com.thinkive.server;

/**
 * ����:
 * ��Ȩ:	 Copyright (c) 2006-2012
 * ��˾:	 ˼�ϿƼ�
 * ����:	 �����
 * �汾:	 2.0
 * ��������: 2012-10-29
 * ����ʱ��: 15:30:10
 */
public interface Response {
    /**
     * д��һ���ֽ�����
     */
    public boolean write(byte[] src);

    /**
     * д��һ���ֽ�����
     */
    public boolean write(byte[] src, int off, int len);

    /**
     * д��һ���ֽ�
     */
    public boolean writeByte(byte value);

    /**
     * д��һ���ַ�
     */
    public boolean writeChar(char value);

    /**
     * д��һ������ֵ
     */
    public boolean writeInt(int value);

    /**
     * д��һ��shortֵ
     */
    public boolean writeShort(short value);

    /**
     * д��һ��longֵ
     */
    public boolean writeLong(long value);

    /**
     * д��һ��floatֵ
     */
    public boolean writeFloat(float value);

    /**
     * д��һ��doubleֵ
     */
    public boolean writeDouble(double value);

    /**
     * ����Ѿ����������
     */
    public void clearData();

    /**
     * ���õ��ô�����(0:�ɹ� ����Ϊ����ȱʡΪ0)
     */
    public void setErrorNo(int errNo);
}
