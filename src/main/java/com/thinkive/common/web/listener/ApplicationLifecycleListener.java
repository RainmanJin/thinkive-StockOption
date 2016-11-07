/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.thinkive.common.web.listener;

import com.thinkive.AppServer;
import com.thinkive.common.system.Application;

import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * ����:
 * ��Ȩ:	 Copyright (c) 2005
 * ��˾:	 ˼�ϿƼ�
 * ����:	 �����
 * �汾:	 1.0
 * ��������: 2006-10-11
 * ����ʱ��: 11:13:50
 */
public class ApplicationLifecycleListener implements ServletContextListener {
    private static Logger logger = Logger.getLogger(ApplicationLifecycleListener.class);
    private ServletContext context = null;

    /**
     * ��ϵͳ����ʱ����
     *
     * @param event a ServletContextEvent instance
     */
    public void contextInitialized(ServletContextEvent event) {
        if (logger.isInfoEnabled())
            logger.info("Starting application......");

        context = event.getServletContext();
        init();
    }

    public void contextDestroyed(javax.servlet.ServletContextEvent servletContextEvent) {

    }

    /**
     * ϵͳ����ʱ��ʼ����Ӧ������
     */
    private void init() {
        //��ʼӦ�ó����Ŀ¼·��
        Application.setRootPath(context.getRealPath("/"));

        //OLDAppServer.start();
        AppServer.start();

    }
}
