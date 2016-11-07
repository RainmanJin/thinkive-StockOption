package com.thinkive.market.function.version_1;

import com.thinkive.base.config.Configuration;
import com.thinkive.base.util.ConvertHelper;
import com.thinkive.market.common.impl.RequestImpl;
import com.thinkive.server.Function;

import org.apache.log4j.Logger;

import java.io.File;

/**
 * ����: ��Ȩ: Copyright (c) 2007 ��˾: ˼�ϿƼ� ����: ����� �汾: 1.0 ��������: 2009-1-13 ����ʱ��:
 * 14:30:48
 */
public abstract class BaseFunction implements Function {
    protected static String SOH = new String(new byte[]{1});
    protected static String STX = new String(new byte[]{2});
    private static Logger logger = Logger.getLogger(BaseFunction.class);

    /**
     * @��������ʽ��������,����1000000���ΪM ����1000���ΪK, ����1λС��
     * @���ߣ���֪֮
     * @ʱ�䣺2012-5-7 ����7:59:06
     */
    public static String formatBigNum(String str) {
        String numStr = str;
        float num = ConvertHelper.strToFloat(str);
        if (num >= 1000000) {
            numStr = (int) (((num / 1000000.0f) + 0.05f) * 10) / 10f + "M";
        } else if (num >= 1000) {
            numStr = (int) (((num / 1000.0f) + 0.05f) * 10) / 10f + "K";
        }
        return numStr;
    }

    /**
     * ����������
     */
    public String getStrParameter(RequestImpl request, String name) {
        String value = (String) request.getParameter(name);
        return (value == null) ? "" : value;
    }

    /**
     * ����������
     */
    public String getStrParameter(RequestImpl request, String name, String defaultValue) {
        String value = getStrParameter(request, name);
        return (value.length() == 0) ? defaultValue : value;
    }

    /**
     * ������Ͳ���
     */
    public int getIntParameter(RequestImpl request, String name) {
        String value = getStrParameter(request, name);
        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * ������Ͳ���
     */
    public int getIntParameter(RequestImpl request, String name, int defaultValue) {
        String value = (String) request.getParameter(name);
        if (value == null) // ������
        {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * ���length���ȵ��ֽ����飬��ͷ��ʼ��str������䣬����Ĳ�0
     */
    protected byte[] getBytes(String str, int length) {
        byte[] strBytes = str.getBytes();
        if (strBytes.length >= length) {
            byte[] temp = new byte[length];
            System.arraycopy(strBytes, 0, temp, 0, length);
            return temp;
        }

        byte[] temp = new byte[length];
        for (int i = 0; i < length; i++) {
            temp[i] = 0;
        }

        for (int i = 0; i < strBytes.length; i++) {
            temp[i] = strBytes[i];
        }

        return temp;
    }

    /**
     * ���ؿ��õ��ļ�·�� xiongpan 2014-10-11
     */
    protected String getEnableFile(String key, String filePath) {
        String path = filePath;
        String[] paths = Configuration.getString(key).split("\\|");
        for (int i = 0; i < paths.length; i++) {
            path = paths[i] + filePath;
            if (new File(path).exists()) {
                break;
            } else {
                logger.error("·��Ϊ'" + path + "'���ļ�������.");
            }

        }
        return path;
    }

    protected String getMarketParam(RequestImpl request) {
        String market = getStrParameter(request, "market");
        if (market == null || market.isEmpty()) {
            market = "SH";
        }
        market = market.toUpperCase();

        return market;
    }
}
