package com.thinkive.server.database;

import org.apache.log4j.Logger;

import com.thinkive.base.jdbc.JdbcTemplate;
import com.thinkive.base.jdbc.session.Session;
import com.thinkive.base.jdbc.session.SessionFactory;
import com.thinkive.base.service.BaseService;

/**
 * ����:  
 * ��Ȩ:	 Copyright (c) 2009
 * ��˾:	 ˼�ϿƼ�
 * ����:	 ���
 * �汾:	 1.0
 * ��������: 2011-10-24
 * ����ʱ��: ����05:51:52
 */
public class ProjectBaseService extends BaseService
{
	
	private static Logger logger = Logger.getLogger(ProjectBaseService.class);
	
	/**
	 * ���ظ���datasource.xml�ļ�����������ԴID���õ��ĻỰ����
	 *
	 * @param id ����ԴID
	 * @return
	 */
	public Session getSession()
	{
		return SessionFactory.getSession();
	}
	
	/**
	 * 
	 * @����������ȱʡ����Դ�����ݲ�������
	 * @���ߣ����
	 * @ʱ�䣺2011-10-26 ����06:40:55
	 * @return
	 */
	public JdbcTemplate getJdbcTemplate()
	{
		return new JdbcTemplate();
	}
}