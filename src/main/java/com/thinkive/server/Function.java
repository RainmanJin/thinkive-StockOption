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
public interface Function {
    /**
     * ����ӿڷ���
     */
    public void service(Request request, Response response);
}
