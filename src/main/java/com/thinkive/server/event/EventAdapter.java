package com.thinkive.server.event;

import com.thinkive.server.SocketRequest;
import com.thinkive.server.SocketResponse;


/**
 * 描述:  事件适配器
 * 版权:	 Copyright (c) 2005
 * 公司:	 思迪科技
 * 作者:	 易庆锋
 * 版本:	 1.0
 * 创建日期: 2007-3-28
 * 创建时间: 10:32:56
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