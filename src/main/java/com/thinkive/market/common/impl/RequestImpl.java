package com.thinkive.market.common.impl;

 
import java.util.HashMap;
import java.util.Map;

import com.thinkive.server.Request;

/**
 * @描述: 行情中心服务器请求版本信息
 * @版权: Copyright (c) 2012 
 * @公司: 思迪科技 
 * @作者: 岳知之
 * @版本: 1.0 
 * @创建日期: 2012-3-14 
 * @创建时间: 下午4:33:52
 */
public class RequestImpl implements Request
{
    //请求版本号
    private int versionNo = 0;
    
    //功能号码
    private int funcNo    = 0;
    
    
    /**
     * 传输数据
     */
    private byte[] data;

    /**
     * 流水号
     */
    private int flowNo    = 0;
    
    /**
     * 分支号
     */
    private int branchNo    = 0;
    
    
  
 

    public int getFlowNo()
    {
        return flowNo;
    }
    
    public void setFlowNo(int flowNo)
    {
        this.flowNo = flowNo;
    }
    
    //请求参数
    private Map params = new HashMap();
    
    public void clear()
    {
        params.clear();
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
    
    public byte[] getData()
    {
        return data;
    }
    
    public void setData(byte[] data)
    {
        this.data = data;
    }
    /**
     * 获得请求参数
     *
     * @param name
     * @return
     */
    public String getParameter(String name)
    {
        return (String) params.get(name);
    }
    
 
    public int getVersionNo()
    {
        return versionNo;
    }

    public void setVersionNo(int versionNo)
    {
        this.versionNo = versionNo;
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
     * 添加请求参数
     *
     * @param name
     * @param value
     */
    public void addParameter(String name, String value)
    {
        params.put(name, value);
    }

    public int getBranchNo()
    {
        return branchNo;
    }

    public void setBranchNo(int branchNo)
    {
        this.branchNo = branchNo;
    }

 
 
}
