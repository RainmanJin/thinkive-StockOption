package com.thinkive.common.system;

/**
 * ����:
 * ��Ȩ:	 Copyright (c) 2005
 * ��˾:	 ˼�ϿƼ�
 * ����:	 �����
 * �汾:	 1.0
 * ��������: 2006-11-18
 * ����ʱ��: 17:04:22
 */
public class Application {
    private static String rootPath = "";

    /**
     * ��õ�ǰӦ�ó���ĸ�Ŀ¼��·��
     */
    public static String getRootPath() {
        return rootPath;
    }

    public static void setRootPath(String rootPath) {
        Application.rootPath = rootPath;
    }
}
