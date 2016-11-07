package com.thinkive.market.common;


import java.util.HashMap;
import java.util.Map;

/**
 * ����:
 * ��Ȩ:	 Copyright (c) 2007
 * ��˾:	 ˼�ϿƼ�
 * ����:	 �����
 * �汾:	 1.0
 * ��������: 2009-1-9
 * ����ʱ��: 15:03:48
 */
public class Context {
    //��ˮ��
    private int flowNo = 0;

    //���ܺ���
    private int funcNo = 0;

    //����
    private Map params = new HashMap();

    //�洢���ݻ�����
    private byte[] resultData = new byte[0];

    public Context() {
    }

    /**
     * �����ˮ����
     */
    public int getFlowNo() {
        return flowNo;
    }

    /**
     * ������ˮ����
     */
    public void setFlowNo(int flowNo) {
        this.flowNo = flowNo;
    }

    /**
     * ��ù��ܺ���
     */
    public int getFuncNo() {
        return funcNo;
    }

    /**
     * ���ù��ܺ���
     */
    public void setFuncNo(int funcNo) {
        this.funcNo = funcNo;
    }

    /**
     * ����������
     */
    public void addParameter(String name, String value) {
        params.put(name, value);
    }

    /**
     * ��÷�������
     */
    public byte[] getResultData() {
        return resultData;
    }

    /**
     * ���÷�������
     */
    public void setResultData(byte[] data) {
        this.resultData = data;
    }

    /**
     * ����������
     */
    public String getStrParameter(String name) {
        String value = (String) params.get(name);
        return (value == null) ? "" : value;
    }

    /**
     * ����������
     */
    public String getStrParameter(String name, String defaultValue) {
        String value = getStrParameter(name);
        return (value.length() == 0) ? defaultValue : value;
    }

    /**
     * ������Ͳ���
     */
    public int getIntParameter(String name) {
        String value = getStrParameter(name);
        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * ������Ͳ���
     */
    public int getIntParameter(String name, int defaultValue) {
        String value = (String) params.get(name);
        if (value == null) //������
        {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            return 0;
        }
    }

}
