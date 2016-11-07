/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.thinkive.service;

import com.thinkive.base.jdbc.JdbcTemplate;
import com.thinkive.base.jdbc.session.Session;
import com.thinkive.base.jdbc.session.SessionFactory;

/**
 * 描述:
 * 版权:	 Copyright (c) 2005
 * 公司:	 思迪科技
 * 作者:	 易庆锋
 * 版本:	 1.0
 * 创建日期: 2006-11-28
 * 创建时间: 15:01:02
 */
public class BaseService {
    /**
     * 返回根据datasource.xml文件中配置数据源ID，得到的会话对象
     *
     * @param id 数据源ID
     */
    public Session getSession(String id) {
        return SessionFactory.getSession(id);
    }

    /**
     * 返回根据datasource.xml文件中配置的数据源ID，构造的数据操作对象
     *
     * @param id 数据源的ID
     */
    public JdbcTemplate getJdbcTemplate(String id) {
        return new JdbcTemplate(id);
    }

    /**
     * 返回缺省数据源操作对象
     */
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate();
    }
}