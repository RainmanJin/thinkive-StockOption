package com.thinkive.server.event;

import com.thinkive.server.SocketRequest;
import com.thinkive.server.SocketResponse;


/**
 * 描述:  服务器事件监听器
 * 版权:	 Copyright (c) 2005
 * 公司:	 思迪科技
 * 作者:	 易庆锋
 * 版本:	 1.0
 * 创建日期: 2007-3-28
 * 创建时间: 10:32:56
 */
public interface ServerListener
{

    /**
     * 服务器端处理产生错误时触发本事件
     *
     * @param error 错误信息
     */
    public void onError(String error);

    /**
     * 当有客户端发来请求时触发本事件
     */
    public void onAccept();

    /**
     * 当服务端接受客户端请求后触发本事件
     *
     * @param request 客户端请求
     */
    public void onAccepted(SocketRequest request);

    /**
     * 当客户端已经连接成功时，触发此事件
     *
     * @param request
     */
    public void onConnected(SocketRequest request);

    /**
     * 当客户端发来数据，并已被服务器控制线程正确读取时，触发该事件
     *
     * @param request 客户端请求
     */
    public void onDataArrival(SocketRequest request, SocketResponse response);

    /**
     * 当客户端与服务器结束连接后触发本事件
     *
     * @param request 客户端请求
     */
    public void onClosed(SocketRequest request);
}