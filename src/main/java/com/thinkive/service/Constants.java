/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.thinkive.service;

/**
 * 描述:
 * 版权:	 Copyright (c) 2005
 * 公司:	 思迪科技
 * 作者:	 易庆锋
 * 版本:	 1.0
 * 创建日期: 2006-11-28
 * 创建时间: 10:48:25
 */
public class Constants {

    /**
     * 深圳A
     */
    public final static int SZ_A_STK = (0);
    /**
     * 深圳B
     */
    public final static int SZ_B_STK = (1);
    /**
     * 中小板
     */
    public final static int SZ_ZXB_STK = (2);
    /**
     * SZ开放式基金
     */
    public final static int SZ_KJ_STK = (3);
    /**
     * SZ封闭式基金
     */
    public final static int SZ_FJ_STK = (4);
    /**
     * SZ权证
     */
    public final static int SZ_QZ_STK = (5);
    /**
     * SZ债券
     */
    public final static int SZ_ZQ_STK = (6);
    /**
     * SZ指数
     */
    public final static int SZ_ZS_STK = (7);
    public final static int SZ_OTHER_STK = (8);
    /**
     * 上海A
     */
    public final static int SH_A_STK = (9);
    /**
     * 上海B
     */
    public final static int SH_B_STK = (10);
    /**
     * 上海开放式基金
     */
    public final static int SH_KJ_STK = (11);
    /**
     * 上海封闭式基金
     */
    public final static int SH_FJ_STK = (12);
    /**
     * 上海权证
     */
    public final static int SH_QZ_STK = (13);
    /**
     * 上海债券
     */
    public final static int SH_ZQ_STK = (14);
    /**
     * 上海指数
     */
    public final static int SH_ZS_STK = (15);
    /**
     * 上海其他
     */
    public final static int SH_OTHER_STK = (16);
    /**
     * 三板
     */
    public final static int SANBAN_STK = (17);
    /**
     * 行情DB连接ID
     */
    public static String SERVICE_DB_CONN_ID = "web";

    /**
     * 判断是否是股票
     */
    public final static boolean isStock(int type) {
        if (type == SH_A_STK || type == SH_B_STK || type == SZ_A_STK || type == SZ_B_STK || type == SZ_ZXB_STK) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否是基金
     */
    public final static boolean isFund(int type) {
        if (type == SH_KJ_STK || type == SH_FJ_STK || type == SZ_KJ_STK || type == SZ_FJ_STK) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否是债券
     */
    public final static boolean isZQ(int type) {
        if (type == SH_ZQ_STK || type == SZ_ZQ_STK) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否是权证
     */
    public final static boolean isQZ(int type) {
        if (type == SH_QZ_STK || type == SZ_QZ_STK) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否是指数
     */
    public final static boolean isExponent(int type) {
        if (type == SH_ZS_STK || type == SZ_ZS_STK) {
            return true;
        } else {
            return false;
        }
    }
}   
    