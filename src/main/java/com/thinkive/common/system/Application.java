package com.thinkive.common.system;

/**
 * 描述:
 * 版权:	 Copyright (c) 2005
 * 公司:	 思迪科技
 * 作者:	 易庆锋
 * 版本:	 1.0
 * 创建日期: 2006-11-18
 * 创建时间: 17:04:22
 */
public class Application {
    private static String rootPath = "";

    /**
     * 获得当前应用程序的根目录的路径
     */
    public static String getRootPath() {
        return rootPath;
    }

    public static void setRootPath(String rootPath) {
        Application.rootPath = rootPath;
    }
}
