package com.thinkive.market.bean;

/**
 * ����:������Ȩ�ɽ���ϸʵ����
 * ��Ȩ:	 Copyright (c) 2007
 * ��˾:	 ˼�ϿƼ�
 * ����:	 ����
 * �汾:	 1.0
 * ��������: 2015-1-16
 * ����ʱ��: 17:00:51
 */
public class DealData
{
    //private int stktype;     //��Ʊ����
    //private String market;     //�г�����
    //private String code;     //��Ʊ����
   // private String name;     //��Ʊ����

    private short minute;    //������
    private float now;       //�ּ�
    private int thedeal;     //�ɽ���
    private int flag;        //������־
private float yesterday;

   /* public int getStktype()
    {
        return stktype;
    }

    public void setStktype(int stktype)
    {
        this.stktype = stktype;
    }*/

   /* public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }*/

   /* public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
*/

    public short getMinute()
    {
        return minute;
    }

    public void setMinute(short minute)
    {
        this.minute = minute;
    }

    public float getNow()
    {
        return now;
    }

    public void setNow(float now)
    {
        this.now = now;
    }

    public int getThedeal()
    {
        return thedeal;
    }

    public void setThedeal(int thedeal)
    {
        this.thedeal = thedeal;
    }

    public int getFlag()
    {
        return flag;
    }

    public void setFlag(int flag)
    {
        this.flag = flag;
    }

	public float getYesterday() {
		return yesterday;
	}

	public void setYesterday(float yesterday) {
		this.yesterday = yesterday;
	}


	

	
	/*public String getMarket()
	{
		return market;
	}

	
	public void setMarket(String market)
	{
		this.market = market;
	}
*/
}
