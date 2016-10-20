/*
 * Copyright (c) 2011 Your Corporation. All Rights Reserved.
 */

package com.thinkive.server;

import com.thinkive.base.config.Configuration;
import com.thinkive.base.util.Console;
import com.thinkive.base.util.DateHelper;

import java.util.Date;

/**
 * ����:
 * ��Ȩ:	 Copyright (c) 2009
 * ��˾:	 ˼�ϿƼ�
 * ����:	 �����
 * �汾:	 1.0
 * ��������: 2011-9-16
 * ����ʱ��: 11:18:45
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
