package com.thinkive.market.bean;

/**
 * @描述: 行情中心服务器状态对象
 * @版权: Copyright (c) 2012 
 * @公司: 思迪科技 
 * @作者: 熊攀
 * @版本: 1.0 
 * @创建日期: 2015-1-18
 * @创建时间: 下午2:25:32
 */
public class MCState
{
	
	private String	curHqDate;		//转码机部署的机器上当前时间
					
	private boolean	isNeedUpdate;
					
	private String	initDate_sh;	//转码机部署的机器上初始化时间
	private long	dbfTime_sh;		//dbf文件的时间
	private String	initDate_sz;	//转码机部署的机器上初始化时间
	private long	dbfTime_sz;		//dbf文件的时间
					
	public String getCurHqDate()
	{
		return curHqDate;
	}
	
	public boolean isNeedUpdate()
	{
		return isNeedUpdate;
	}
	
	public String getInitDate_sh()
	{
		return initDate_sh;
	}
	
	public long getDbfTime_sh()
	{
		return dbfTime_sh;
	}
	
	public String getInitDate_sz()
	{
		return initDate_sz;
	}
	
	public long getDbfTime_sz()
	{
		return dbfTime_sz;
	}
	
	public void setCurHqDate(String curHqDate)
	{
		this.curHqDate = curHqDate;
	}
	
	public void setNeedUpdate(boolean isNeedUpdate)
	{
		this.isNeedUpdate = isNeedUpdate;
	}
	
	public void setInitDate_sh(String initDate_sh)
	{
		this.initDate_sh = initDate_sh;
	}
	
	public void setDbfTime_sh(long dbfTime_sh)
	{
		this.dbfTime_sh = dbfTime_sh;
	}
	
	public void setInitDate_sz(String initDate_sz)
	{
		this.initDate_sz = initDate_sz;
	}
	
	public void setDbfTime_sz(long dbfTime_sz)
	{
		this.dbfTime_sz = dbfTime_sz;
	}
	
}
