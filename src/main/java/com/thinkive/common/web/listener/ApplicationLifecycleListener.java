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
 * 描述:
 * 版权:	 Copyright (c) 2005
 * 公司:	 思迪科技
 * 作者:	 易庆锋
 * 版本:	 1.0
 * 创建日期: 2006-10-11
 * 创建时间: 11:13:50
 */
public class ApplicationLifecycleListener implements ServletContextListener {
    private static Logger logger = Logger.getLogger(ApplicationLifecycleListener.class);
    private ServletContext context = null;

    /**
     * 在系统启动时调用
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
     * 系统启动时初始化相应的数据
     */
    private void init() {
        //初始应用程序根目录路径
        Application.setRootPath(context.getRealPath("/"));

        //OLDAppServer.start();
        AppServer.start();

    }
}
