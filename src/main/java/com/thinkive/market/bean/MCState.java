package com.thinkive.market.bean;

/**
 * @����: �������ķ�����״̬����
 * @��Ȩ: Copyright (c) 2012 
 * @��˾: ˼�ϿƼ� 
 * @����: ����
 * @�汾: 1.0 
 * @��������: 2015-1-18
 * @����ʱ��: ����2:25:32
 */
public class MCState
{
	
	private String	curHqDate;		//ת�������Ļ����ϵ�ǰʱ��
					
	private boolean	isNeedUpdate;
					
	private String	initDate_sh;	//ת�������Ļ����ϳ�ʼ��ʱ��
	private long	dbfTime_sh;		//dbf�ļ���ʱ��
	private String	initDate_sz;	//ת�������Ļ����ϳ�ʼ��ʱ��
	private long	dbfTime_sz;		//dbf�ļ���ʱ��
					
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
