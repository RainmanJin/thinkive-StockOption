/*
 * Copyright (c) 2011 Your Corporation. All Rights Reserved.
 */

package com.thinkive.server;

import com.thinkive.base.config.Configuration;
import com.thinkive.base.util.Console;
import com.thinkive.base.util.DateHelper;

import java.util.Date;

/**
 * 描述:
 * 版权:	 Copyright (c) 2009
 * 公司:	 思迪科技
 * 作者:	 易庆锋
 * 版本:	 1.0
 * 创建日期: 2011-9-16
 * 创建时间: 11:18:45
 */
public class StateLogger
{
    public static void WriteLog(String msg)
    {
        if (Configuration.getBoolean("general.isDebug"))
        {
            Console.println("[" + DateHelper.formatDate(new Date()) + "] " + msg);
        }
    }
}
