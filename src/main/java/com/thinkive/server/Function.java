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
public interface Function {
    /**
     * 服务接口方法
     */
    public void service(Request request, Response response);
}
