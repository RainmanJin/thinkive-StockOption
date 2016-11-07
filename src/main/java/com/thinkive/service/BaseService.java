/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.thinkive.service;

import com.thinkive.base.jdbc.JdbcTemplate;
import com.thinkive.base.jdbc.session.Session;
import com.thinkive.base.jdbc.session.SessionFactory;

/**
 * ����:
 * ��Ȩ:	 Copyright (c) 2005
 * ��˾:	 ˼�ϿƼ�
 * ����:	 �����
 * �汾:	 1.0
 * ��������: 2006-11-28
 * ����ʱ��: 15:01:02
 */
public class BaseService {
    /**
     * ���ظ���datasource.xml�ļ�����������ԴID���õ��ĻỰ����
     *
     * @param id ����ԴID
     */
    public Session getSession(String id) {
        return SessionFactory.getSession(id);
    }

    /**
     * ���ظ���datasource.xml�ļ������õ�����ԴID����������ݲ�������
     *
     * @param id ����Դ��ID
     */
    public JdbcTemplate getJdbcTemplate(String id) {
        return new JdbcTemplate(id);
    }

    /**
     * ����ȱʡ����Դ��������
     */
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate();
    }
}