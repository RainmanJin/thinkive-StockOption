package com.thinkive.server.event;

import com.thinkive.server.SocketRequest;
import com.thinkive.server.SocketResponse;


/**
 * ����:  �������¼�������
 * ��Ȩ:	 Copyright (c) 2005
 * ��˾:	 ˼�ϿƼ�
 * ����:	 �����
 * �汾:	 1.0
 * ��������: 2007-3-28
 * ����ʱ��: 10:32:56
 */
public interface ServerListener
{

    /**
     * �������˴����������ʱ�������¼�
     *
     * @param error ������Ϣ
     */
    public void onError(String error);

    /**
     * ���пͻ��˷�������ʱ�������¼�
     */
    public void onAccept();

    /**
     * ������˽��ܿͻ�������󴥷����¼�
     *
     * @param request �ͻ�������
     */
    public void onAccepted(SocketRequest request);

    /**
     * ���ͻ����Ѿ����ӳɹ�ʱ���������¼�
     *
     * @param request
     */
    public void onConnected(SocketRequest request);

    /**
     * ���ͻ��˷������ݣ����ѱ������������߳���ȷ��ȡʱ���������¼�
     *
     * @param request �ͻ�������
     */
    public void onDataArrival(SocketRequest request, SocketResponse response);

    /**
     * ���ͻ�����������������Ӻ󴥷����¼�
     *
     * @param request �ͻ�������
     */
    public void onClosed(SocketRequest request);
}