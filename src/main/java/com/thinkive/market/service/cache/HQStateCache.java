package com.thinkive.market.service.cache;

public enum HQStateCache
{
	SH, SZ;
	
	/**
	 * DBF时间
	 */
	private long			dbftime	= 0;
									
	private String			initDate;
							
	/**
	 * 是否在初始化中
	 */
	private boolean			isInit;
							
	/**
	 * 转码机网关时间初始化完成时间
	 */
	private static String	convDate;
							
	/**
	 * 行情线程是否需要更新
	 */
	private boolean			isNeedUpdate;
							
	public boolean isInit()
	{
		return isInit;
	}
	
	public void setInit(boolean isInit)
	{
		this.isInit = isInit;
	}
	
	public String getInitDate()
	{
		return initDate;
	}
	
	public void setInitDate(String currentDate)
	{
		this.initDate = currentDate;
	}
	
	public static boolean isNeedUpdate()
	{
		return SH.isNeedUpdate || SZ.isNeedUpdate;
	}
	
	public static boolean isNeedUpdate(String market)
	{
		return HQStateCache.valueOf(market.toUpperCase()).isNeedUpdate;
	}
	
	public void setNeedUpdate(boolean isNeedUpdate)
	{
		this.isNeedUpdate = isNeedUpdate;
	}
	
	public static String getConvDate()
	{
		return convDate;
	}
	
	public static void setConvDate(String convDate)
	{
		HQStateCache.convDate = convDate;
	}
	
	/**
	 * @描述：转码机网关服务时间等于当前行情服务器时间表示stocklist是完整的初始化完成。
	 * @作者：岳知之
	 * @时间：2012-4-20 下午7:36:19
	 * @return
	 */
	public boolean isInitCompleted()
	{
		return convDate != null && convDate.equals(initDate);
	}
	
	public long getDbftime()
	{
		return dbftime;
	}
	
	public void setDbftime(long dbftime)
	{
		this.dbftime = dbftime;
	}
	
	public static String getInitMarket()
	{
		if ( SH.isInit )
		{
			return SH.name();
		}
		
		if ( SZ.isInit )
		{
			return SZ.name();
		}
		
		return null;
	}
}
