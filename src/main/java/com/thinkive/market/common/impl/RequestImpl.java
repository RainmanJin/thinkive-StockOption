package com.thinkive.market.common.impl;


import com.thinkive.server.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * @����: �������ķ���������汾��Ϣ
 * @��Ȩ: Copyright (c) 2012
 * @��˾: ˼�ϿƼ�
 * @����: ��֪֮
 * @�汾: 1.0
 * @��������: 2012-3-14
 * @����ʱ��: ����4:33:52
 */
public class RequestImpl implements Request {
    //����汾��
    private int versionNo = 0;

    //���ܺ���
    private int funcNo = 0;


    /**
     * ��������
     */
    private byte[] data;

    /**
     * ��ˮ��
     */
    private int flowNo = 0;

    /**
     * ��֧��
     */
    private int branchNo = 0;
    //�������
    private Map params = new HashMap();

    public int getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(int flowNo) {
        this.flowNo = flowNo;
    }

    public void clear() {
        params.clear();
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

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * ����������
     */
    public String getParameter(String name) {
        return (String) params.get(name);
    }

    public int getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(int versionNo) {
        this.versionNo = versionNo;
    }

    /**
     * ����������
     */
    public void addParameter(String name, String value) {
        params.put(name, value);
    }

    public int getBranchNo() {
        return branchNo;
    }

    public void setBranchNo(int branchNo) {
        this.branchNo = branchNo;
    }


}
