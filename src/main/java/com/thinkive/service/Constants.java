/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.thinkive.service;

/**
 * ����:
 * ��Ȩ:	 Copyright (c) 2005
 * ��˾:	 ˼�ϿƼ�
 * ����:	 �����
 * �汾:	 1.0
 * ��������: 2006-11-28
 * ����ʱ��: 10:48:25
 */
public class Constants {

    /**
     * ����A
     */
    public final static int SZ_A_STK = (0);
    /**
     * ����B
     */
    public final static int SZ_B_STK = (1);
    /**
     * ��С��
     */
    public final static int SZ_ZXB_STK = (2);
    /**
     * SZ����ʽ����
     */
    public final static int SZ_KJ_STK = (3);
    /**
     * SZ���ʽ����
     */
    public final static int SZ_FJ_STK = (4);
    /**
     * SZȨ֤
     */
    public final static int SZ_QZ_STK = (5);
    /**
     * SZծȯ
     */
    public final static int SZ_ZQ_STK = (6);
    /**
     * SZָ��
     */
    public final static int SZ_ZS_STK = (7);
    public final static int SZ_OTHER_STK = (8);
    /**
     * �Ϻ�A
     */
    public final static int SH_A_STK = (9);
    /**
     * �Ϻ�B
     */
    public final static int SH_B_STK = (10);
    /**
     * �Ϻ�����ʽ����
     */
    public final static int SH_KJ_STK = (11);
    /**
     * �Ϻ����ʽ����
     */
    public final static int SH_FJ_STK = (12);
    /**
     * �Ϻ�Ȩ֤
     */
    public final static int SH_QZ_STK = (13);
    /**
     * �Ϻ�ծȯ
     */
    public final static int SH_ZQ_STK = (14);
    /**
     * �Ϻ�ָ��
     */
    public final static int SH_ZS_STK = (15);
    /**
     * �Ϻ�����
     */
    public final static int SH_OTHER_STK = (16);
    /**
     * ����
     */
    public final static int SANBAN_STK = (17);
    /**
     * ����DB����ID
     */
    public static String SERVICE_DB_CONN_ID = "web";

    /**
     * �ж��Ƿ��ǹ�Ʊ
     */
    public final static boolean isStock(int type) {
        if (type == SH_A_STK || type == SH_B_STK || type == SZ_A_STK || type == SZ_B_STK || type == SZ_ZXB_STK) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * �ж��Ƿ��ǻ���
     */
    public final static boolean isFund(int type) {
        if (type == SH_KJ_STK || type == SH_FJ_STK || type == SZ_KJ_STK || type == SZ_FJ_STK) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * �ж��Ƿ���ծȯ
     */
    public final static boolean isZQ(int type) {
        if (type == SH_ZQ_STK || type == SZ_ZQ_STK) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * �ж��Ƿ���Ȩ֤
     */
    public final static boolean isQZ(int type) {
        if (type == SH_QZ_STK || type == SZ_QZ_STK) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * �ж��Ƿ���ָ��
     */
    public final static boolean isExponent(int type) {
        if (type == SH_ZS_STK || type == SZ_ZS_STK) {
            return true;
        } else {
            return false;
        }
    }
}   
    