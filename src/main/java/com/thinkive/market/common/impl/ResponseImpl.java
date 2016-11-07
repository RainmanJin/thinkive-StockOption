package com.thinkive.market.common.impl;

import com.thinkive.server.Response;

import org.eclipse.jetty.util.ajax.JSON;

import java.util.Map;

/**
 * @描述: 用于向客户端发送数据
 * @版权: Copyright (c) 2012
 * @公司: 思迪科技
 * @作者: 岳知之
 * @版本: 1.0
 * @创建日期: 2012-3-14
 * @创建时间: 下午3:05:56
 */
public class ResponseImpl implements Response {
    private byte[] data = new byte[0];
    private Map map;

    public ResponseImpl() {

    }

    @Override
    public boolean write(byte[] data) {
        this.data = data;
        return true;
    }

    public byte[] getData() {
        return data;
    }

    public void clear() {
        data = null;
        map = null;
    }

    public Map getMap() {
        return map;
    }

    public void write(Map map) {
        this.map = map;

        data = JSON.toString(map).getBytes();
    }

    @Override
    public boolean write(byte[] src, int off, int len) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean writeByte(byte value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean writeChar(char value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean writeInt(int value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean writeShort(short value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean writeLong(long value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean writeFloat(float value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean writeDouble(double value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void clearData() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setErrorNo(int errNo) {
        // TODO Auto-generated method stub

    }

}
