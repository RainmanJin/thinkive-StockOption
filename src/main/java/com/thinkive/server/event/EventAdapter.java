package com.thinkive.server.event;

import com.thinkive.server.SocketRequest;
import com.thinkive.server.SocketResponse;


/**
 * ����:  �¼�������
 * ��Ȩ:	 Copyright (c) 2005
 * ��˾:	 ˼�ϿƼ�
 * ����:	 �����
 * �汾:	 1.0
 * ��������: 2007-3-28
 * ����ʱ��: 10:32:56
 */
public class EventAdapter implements ServerListener
{
    public EventAdapter()
    {
    }

    public void onError(String error)
    {
    }

    public void onAccept()
    {
    }

    public void onAccepted(SocketRequest request)
    {
    }

    public void onConnected(SocketRequest request)
    {

    }

    public void onDataArrival(SocketRequest request, SocketResponse response)
    {

    }

    public void onClosed(SocketRequest request)
    {
    }
}