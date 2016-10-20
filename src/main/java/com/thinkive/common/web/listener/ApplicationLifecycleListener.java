/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.thinkive.common.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;




import com.thinkive.AppServer;
import com.thinkive.common.system.Application;

/**
 * ����:
 * ��Ȩ:	 Copyright (c) 2005
 * ��˾:	 ˼�ϿƼ�
 * ����:	 �����
 * �汾:	 1.0
 * ��������: 2006-10-11
 * ����ʱ��: 11:13:50
 */
public class ApplicationLifecycleListener implements ServletContextListener
{
    private ServletContext context = null;
    
    private static Logger  logger  = Logger.getLogger(ApplicationLifecycleListener.class);
    
    /**
     * ��ϵͳ����ʱ����
     *
     * @param event a ServletContextEvent instance
     */
    public void contextInitialized(ServletContextEvent event)
    {
        if ( logger.isInfoEnabled() )
            logger.info("Starting application......");
        
        context = event.getServletContext();
        init();
    }
    
    public void contextDestroyed(javax.servlet.ServletContextEvent servletContextEvent)
    {
        
    }
    
    /**
     * ϵͳ����ʱ��ʼ����Ӧ������
     */
    private void init()
    {
        //��ʼӦ�ó����Ŀ¼·��
        Application.setRootPath(context.getRealPath("/"));
        
        //OLDAppServer.start();
        AppServer.start();
        
    }
}