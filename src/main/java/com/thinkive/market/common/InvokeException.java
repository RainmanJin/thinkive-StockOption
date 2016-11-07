package com.thinkive.market.common;

/**
 * 描述:
 * 版权:	 Copyright (c) 2007
 * 公司:	 思迪科技
 * 作者:	 易庆锋
 * 版本:	 1.0
 * 创建日期: 2009-1-9
 * 创建时间: 17:29:18
 */
public class InvokeException extends Exception {
    public InvokeException() {
        super();
    }


    public InvokeException(String message) {
        super(message);
    }


    public InvokeException(String message, Throwable cause) {
        super(message, cause);
    }


    public InvokeException(Throwable cause) {
        super(cause);
    }
}

