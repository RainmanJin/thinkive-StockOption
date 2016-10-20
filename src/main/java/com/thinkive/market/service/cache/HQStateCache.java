package com.thinkive.market.service.cache;

public enum HQStateCache
{
	SH, SZ;
	
	/**
	 * DBFʱ��
	 */
	private long			dbftime	= 0;
									
	private String			initDate;
							
	/**
	 * �Ƿ��ڳ�ʼ����
	 */
	private boolean			isInit;
							
	/**
	 * ת�������ʱ���ʼ�����ʱ��
	 */
	private static String	convDate;
							
	/**
	 * �����߳��Ƿ���Ҫ����
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
	 * @������ת������ط���ʱ����ڵ�ǰ���������ʱ���ʾstocklist�������ĳ�ʼ����ɡ�
	 * @���ߣ���֪֮
	 * @ʱ�䣺2012-4-20 ����7:36:19
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
