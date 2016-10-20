package com.thinkive.server.database;

import org.apache.log4j.Logger;

import com.thinkive.base.jdbc.JdbcTemplate;
import com.thinkive.base.jdbc.session.Session;
import com.thinkive.base.jdbc.session.SessionFactory;
import com.thinkive.base.service.BaseService;

/**
 * 描述:  
 * 版权:	 Copyright (c) 2009
 * 公司:	 思迪科技
 * 作者:	 李炜
 * 版本:	 1.0
 * 创建日期: 2011-10-24
 * 创建时间: 下午05:51:52
 */
public class ProjectBaseService extends BaseService
{
	
	private static Logger logger = Logger.getLogger(ProjectBaseService.class);
	
	/**
	 * 返回根据datasource.xml文件中配置数据源ID，得到的会话对象
	 *
	 * @param id 数据源ID
	 * @return
	 */
	public Session getSession()
	{
		return SessionFactory.getSession();
	}
	
	/**
	 * 
	 * @描述：返回缺省数据源的数据操作对象
	 * @作者：李炜
	 * @时间：2011-10-26 下午06:40:55
	 * @return
	 */
	public JdbcTemplate getJdbcTemplate()
	{
		return new JdbcTemplate();
	}
}