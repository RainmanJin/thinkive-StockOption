package com.thinkive.market.bean;

/**
 * 描述: 个股期权分时行情数据
 * 版权: Copyright (c) 2007 
 * 公司: 思迪科技 
 * 作者: 熊攀
 * 版本: 1.0 
 * 创建日期: 2015-1-17
 * 创建时间: 15:56:01
 */
public class MinuteHQ {

	//private int date;// 日期 五日分时需要这个字段 xiongpan 214-10-07

	private short minute; // 分钟数
	private float now; // 现价
	private int thedeal; // 成交量
	private float average; // 均价
	
	private float   yesterday;
	private long  TotalLongPosition;//当前持仓量 
	
	
	private float lead;

	public long getTotalLongPosition() {
		return TotalLongPosition;
	}

	public void setTotalLongPosition(long totalLongPosition) {
		TotalLongPosition = totalLongPosition;
	}



	public float getYesterday() {
		return yesterday;
	}

	public void setYesterday(float yesterday) {
		this.yesterday = yesterday;
	}

	/**
	 * 获得分钟数
	 * 
	 * @return
	 */
	public short getMinute() {
		return minute;
	}

	/**
	 * 设置分钟数
	 * 
	 * @param minute
	 */
	public void setMinute(short minute) {
		this.minute = minute;
	}

	/**
	 * 获得现价
	 * 
	 * @return
	 */
	public float getNow() {
		return now;
	}

	/**
	 * 设置现价
	 * 
	 * @param now
	 */
	public void setNow(float now) {
		this.now = now;
	}

	/**
	 * 获得成交量
	 * 
	 * @return
	 */
	public int getThedeal() {
		return thedeal;
	}

	/**
	 * 设置成交量
	 * 
	 * @param thedeal
	 */
	public void setThedeal(int thedeal) {
		this.thedeal = thedeal;
	}

	/**
	 * 获得均价
	 * 
	 * @return
	 */
	public float getAverage() {
		return average;
	}

	/**
	 * 设置均价
	 * 
	 * @param average
	 */
	public void setAverage(float average) {
		this.average = average;
	}

	public float getLead() {
		return lead;
	}

	public void setLead(float lead) {
		this.lead = lead;
	}

}
