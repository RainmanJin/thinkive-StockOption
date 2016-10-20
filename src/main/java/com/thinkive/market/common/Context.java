package com.thinkive.market.common;


import java.util.Map;
import java.util.HashMap;

/**
 * 描述:
 * 版权:	 Copyright (c) 2007
 * 公司:	 思迪科技
 * 作者:	 易庆锋
 * 版本:	 1.0
 * 创建日期: 2009-1-9
 * 创建时间: 15:03:48
 */
public class Context
{
    //流水号
    private int flowNo = 0;

    //功能号码
    private int funcNo = 0;

    //参数
    private Map params = new HashMap();

    //存储数据缓冲区
    private byte[] resultData = new byte[0];

    public Context()
    {
    }

    /**
     * 设置流水号码
     *
     * @param flowNo
     */
    public void setFlowNo(int flowNo)
    {
        this.flowNo = flowNo;
    }

    /**
     * 获得流水号码
     *
     * @return
     */
    public int getFlowNo()
    {
        return flowNo;
    }

    /**
     * 设置功能号码
     *
     * @param funcNo
     */
    public void setFuncNo(int funcNo)
    {
        this.funcNo = funcNo;
    }

    /**
     * 获得功能号码
     *
     * @return
     */
    public int getFuncNo()
    {
        return funcNo;
    }

    /**
     * 添加请求参数
     *
     * @param name
     * @param value
     */
    public void addParameter(String name, String value)
    {
        params.put(name, value);
    }

    /**
     * 设置返回数据
     *
     * @param data
     */
    public void setResultData(byte[] data)
    {
        this.resultData = data;
    }

    /**
     * 获得返回数据
     */
    public byte[] getResultData()
    {
        return resultData;
    }

    /**
     * 获得请求参数
     *
     * @param name
     * @return
     */
    public String getStrParameter(String name)
    {
        String value = (String) params.get(name);
        return (value == null) ? "" : value;
    }

    /**
     * 获得请求参数
     *
     * @param name
     * @param defaultValue
     * @return
     */
    public String getStrParameter(String name, String defaultValue)
    {
        String value = getStrParameter(name);
        return (value.length() == 0) ? defaultValue : value;
    }

    /**
     * 获得整型参数
     *
     * @param name
     * @return
     */
    public int getIntParameter(String name)
    {
        String value = getStrParameter(name);
        try
        {
            return Integer.parseInt(value);
        }
        catch (Exception ex)
        {
            return 0;
        }
    }

    /**
     * 获得整型参数
     *
     * @param name
     * @param defaultValue
     * @return
     */
    public int getIntParameter(String name, int defaultValue)
    {
        String value = (String) params.get(name);
        if (value == null) //不存在
        {
            return defaultValue;
        }

        try
        {
            return Integer.parseInt(value);
        }
        catch (Exception ex)
        {
            return 0;
        }
    }

}
