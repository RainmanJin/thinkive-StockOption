package com.thinkive.server;

/**
 * 描述:
 * 版权:	 Copyright (c) 2006-2012
 * 公司:	 思迪科技
 * 作者:	 易庆锋
 * 版本:	 2.0
 * 创建日期: 2012-10-29
 * 创建时间: 15:30:10
 */
public interface Response
{
    /**
     * 写入一个字节数组
     *
     * @param src
     */
    public boolean write(byte[] src);

    /**
     * 写入一个字节数组
     *
     * @param src
     * @param off
     * @param len
     */
    public boolean write(byte[] src, int off, int len);

    /**
     * 写入一个字节
     *
     * @param value
     */
    public boolean writeByte(byte value);

    /**
     * 写入一个字符
     *
     * @param value
     */
    public boolean writeChar(char value);

    /**
     * 写入一个整数值
     *
     * @param value
     */
    public boolean writeInt(int value);

    /**
     * 写入一个short值
     *
     * @param value
     */
    public boolean writeShort(short value);

    /**
     * 写入一个long值
     *
     * @param value
     */
    public boolean writeLong(long value);

    /**
     * 写入一个float值
     *
     * @param value
     */
    public boolean writeFloat(float value);

    /**
     * 写入一个double值
     *
     * @param value
     */
    public boolean writeDouble(double value);

    /**
     * 清空已经输出的数据
     */
    public void clearData();

    /**
     * 设置调用错误编号(0:成功 负数为错误，缺省为0)
     *
     * @param errNo
     */
    public void setErrorNo(int errNo);
}
