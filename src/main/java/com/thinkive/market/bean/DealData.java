package com.thinkive.market.bean;

/**
 * 描述:个股期权成交明细实体类
 * 版权:	 Copyright (c) 2007
 * 公司:	 思迪科技
 * 作者:	 熊攀
 * 版本:	 1.0
 * 创建日期: 2015-1-16
 * 创建时间: 17:00:51
 */
public class DealData
{
    //private int stktype;     //股票类型
    //private String market;     //市场代码
    //private String code;     //股票代码
   // private String name;     //股票名称

    private short minute;    //分钟数
    private float now;       //现价
    private int thedeal;     //成交量
    private int flag;        //买卖标志
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
