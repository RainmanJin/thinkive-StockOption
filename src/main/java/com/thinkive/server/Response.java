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
public interface Response
{
    /**
     * д��һ���ֽ�����
     *
     * @param src
     */
    public boolean write(byte[] src);

    /**
     * д��һ���ֽ�����
     *
     * @param src
     * @param off
     * @param len
     */
    public boolean write(byte[] src, int off, int len);

    /**
     * д��һ���ֽ�
     *
     * @param value
     */
    public boolean writeByte(byte value);

    /**
     * д��һ���ַ�
     *
     * @param value
     */
    public boolean writeChar(char value);

    /**
     * д��һ������ֵ
     *
     * @param value
     */
    public boolean writeInt(int value);

    /**
     * д��һ��shortֵ
     *
     * @param value
     */
    public boolean writeShort(short value);

    /**
     * д��һ��longֵ
     *
     * @param value
     */
    public boolean writeLong(long value);

    /**
     * д��һ��floatֵ
     *
     * @param value
     */
    public boolean writeFloat(float value);

    /**
     * д��һ��doubleֵ
     *
     * @param value
     */
    public boolean writeDouble(double value);

    /**
     * ����Ѿ����������
     */
    public void clearData();

    /**
     * ���õ��ô�����(0:�ɹ� ����Ϊ����ȱʡΪ0)
     *
     * @param errNo
     */
    public void setErrorNo(int errNo);
}
