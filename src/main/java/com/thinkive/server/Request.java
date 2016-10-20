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
public interface Request
{
    /**
     * ��ȡ���ܺ�
     *
     * @return
     */
    public int getFuncNo();

    /**
     * ��ȡ��֧��ID
     *
     * @return
     */
    public int getBranchNo();

    /**
     * ��ȡ��ˮ��
     *
     * @return
     */
    public int getFlowNo();

    /**
     * ��ȡ�汾��
     *
     * @return
     */
    public int getVersionNo();

    /**
     * ��ȡ��������
     *
     * @return
     */
    public byte[] getData();


}
