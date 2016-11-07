/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.thinkive.service;

import com.thinkive.base.jdbc.DataRow;
import com.thinkive.base.jdbc.session.Session;
import com.thinkive.base.jdbc.session.SessionFactory;

import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * 描述:
 * 版权:	 Copyright (c) 2005
 * 公司:	 思迪科技
 * 作者:	 易庆锋
 * 版本:	 1.0
 * 创建日期: 2006-11-28
 * 创建时间: 13:58:41
 */
public class SequenceGenerator {
    private static SequenceGenerator instance = new SequenceGenerator();
    private static Logger logger = Logger.getLogger(SequenceGenerator.class);
    //private static String SEQSTARTS = "GSS";


    private SequenceGenerator() {
    }


    public static SequenceGenerator getInstance() {
        return instance;
    }

    public String getSeqValue(String seqName) {
        if (seqName == null || seqName.equals(""))
            return "";

        Session session = null;
        try {
            session = SessionFactory.getSession(Constants.SERVICE_DB_CONN_ID);
            session.beginTrans();
            seqName = seqName.toUpperCase();
            ArrayList argList = new ArrayList();
            argList.add(seqName);
            session.update("update T_SEQUENCE set CURRENT_VALUE=CURRENT_VALUE+1 where NAME=?", argList.toArray());
            DataRow dataRow = session.queryMap("select * from T_SEQUENCE where NAME=?", argList.toArray());
            if (dataRow == null)
                return "";

            long max = dataRow.getLong("max_value");
            long min = dataRow.getLong("start_value");
            long value = dataRow.getLong("current_value");

            if (value >= max) {
                argList.clear();
                argList.add(new Long(min));
                argList.add(seqName);
                session.update("update T_SEQUENCE set CURRENT_VALUE=? where NAME=?", argList.toArray());
            }

            session.commitTrans();
            //return SEQSTARTS + value;
            return "" + value;
        } catch (Exception e) {
            if (session != null) {
                session.rollbackTrans();
            }
        } finally {
            if (session != null) {
                session.close();
                session = null;
            }
        }
        return "";
    }


}
