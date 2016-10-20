/*
 * Copyright (c) 2009 Your Corporation. All Rights Reserved.
 */

package com.thinkive.market.bean;

/**
 * 描述:个股期权基础数据 
 * 版权:	 Copyright (c) 2007
 * 公司:	 思迪科技
 * 作者:	 熊攀
 * 版本:	 1.0
 * 创建日期: 2015-1-15
 * 创建时间: 17:14:07
 */
public class BaseInfo
{
     private String RFStreamID;//参考数据类型标识符，取值R0301表示期权基础信息  [6]
	 private String SecurityID;//期权合约的合约编码        [9]
	 private String ContractID;//合约交易代码          [20]    =======合约交易代码=======601318P1502M07500
	 private String ContractSymbol;//期权合约简称         [21]
	 
	 private String UnderlyingSecurityID;//标的证券代码             [7]
	 private String UnderlyingSymbol;//标的证券名称               [9]
	 
	 private String UnderlyingType;//标的证券类型//EBS – ETF， ASH – A股                      [4]
	 private String OptionType;//若为欧式期权，则本字段为“E”；若为美式期权，则本字段为“A”    [2]
	 
	 private String CallOrPut;//认购，则本字段为“C”；若为认沽，则本字段为“P”          [2]
	 private long ContractMultiplierUnit;//合约单位  （一张期权合约所包含的股数）
	 private double ExercisePrice;//期权行权价,经过除权除息调整后的期权行权价，精确到0.1厘
	 private String StartDate;//首个交易日               [9]
	 
	 private String EndDate;//最后交易日              [9]
	 private String ExerciseDate;//期权行权日          [9]
	 private String DeliveryDate;//行权交割日          [9]
	 private String ExpireDate;//期权到期日               [9]   ---20150128
	 
	 private String UpdateVersion;//合约版本号         [2]
	 private long  TotalLongPosition;//当前合约未平仓数 单位是 （张)       昨持仓量
	 private double SecurityClosePx;//昨日收盘价，右对齐，单位：元（精确到0.1厘）
	 private double SettlPrice;//合约前结算价,昨日结算价，如遇除权除息则为调整后的结算价（合约上市首日填写参考价），右对齐，单位：元（精确到0.1厘）
	
	
	private double UnderlyingClosePx;//标的证券前收盘,期权标的证券除权除息调整后的前收盘价格，右对齐，单位：元（精确到0.1厘
	 private String PriceLimitType;//涨跌幅限制类型,‘N’有涨跌幅限制类型            [2]
	private double DailyPriceUpLimit;//涨幅上限价格
	private double DailyPriceDownLimit;//跌幅下限价格
	
	private double MarginUnit;//单位保证金,当日持有一张合约所需要的保证金数量，精确到分
	private float MarginRatioParam1;//保证金计算比例参数一 单位：%
	private float MarginRatioParam2;//保证金计算比例参数二 单位：%
	private long RoundLot;//整手数 一手对应的合约数
	
	private long LmtOrdMinFloor;//单笔限价申报下限
	private long LmtOrdMaxFloor;//单笔限价申报上限
	private long MktOrdMinFloor;//单笔市价申报下限
	private long MktOrdMaxFloor;//单笔市价申报上限
	
	
	private double TickSize;//最小报价单位,单位：元，精确到0.1厘
	 private String SecurityStatusFlag;//期权合约状态信息标签      9
	 
	 
	 
	 
	 ////////
	 
	 private String ListID;//[5]1501
	 private int  LeaveDay;//距离到期日剩余天数
	 private int  iPC;//1认购                                 0认沽
///////////
	 
	 
	 
	public String getRFStreamID() {
		return RFStreamID;
	}
	public void setRFStreamID(String rFStreamID) {
		RFStreamID = rFStreamID;
	}
	public String getSecurityID() {
		return SecurityID;
	}
	public void setSecurityID(String securityID) {
		SecurityID = securityID;
	}
	public String getContractID() {
		return ContractID;
	}
	public void setContractID(String contractID) {
		ContractID = contractID;
	}
	public String getContractSymbol() {
		return ContractSymbol;
	}
	public void setContractSymbol(String contractSymbol) {
		ContractSymbol = contractSymbol;
	}
	public String getUnderlyingSecurityID() {
		return UnderlyingSecurityID;
	}
	public void setUnderlyingSecurityID(String underlyingSecurityID) {
		UnderlyingSecurityID = underlyingSecurityID;
	}
	public String getUnderlyingSymbol() {
		return UnderlyingSymbol;
	}
	public void setUnderlyingSymbol(String underlyingSymbol) {
		UnderlyingSymbol = underlyingSymbol;
	}
	public String getUnderlyingType() {
		return UnderlyingType;
	}
	public void setUnderlyingType(String underlyingType) {
		UnderlyingType = underlyingType;
	}
	public String getOptionType() {
		return OptionType;
	}
	public void setOptionType(String optionType) {
		OptionType = optionType;
	}
	public String getCallOrPut() {
		return CallOrPut;
	}
	public void setCallOrPut(String callOrPut) {
		CallOrPut = callOrPut;
	}
	public long getContractMultiplierUnit() {
		return ContractMultiplierUnit;
	}
	public void setContractMultiplierUnit(long contractMultiplierUnit) {
		ContractMultiplierUnit = contractMultiplierUnit;
	}
	public double getExercisePrice() {
		return ExercisePrice;
	}
	public void setExercisePrice(double exercisePrice) {
		ExercisePrice = exercisePrice;
	}
	public String getStartDate() {
		return StartDate;
	}
	public void setStartDate(String startDate) {
		StartDate = startDate;
	}
	public String getEndDate() {
		return EndDate;
	}
	public void setEndDate(String endDate) {
		EndDate = endDate;
	}
	public String getExerciseDate() {
		return ExerciseDate;
	}
	public void setExerciseDate(String exerciseDate) {
		ExerciseDate = exerciseDate;
	}
	public String getDeliveryDate() {
		return DeliveryDate;
	}
	public void setDeliveryDate(String deliveryDate) {
		DeliveryDate = deliveryDate;
	}
	public String getExpireDate() {
		return ExpireDate;
	}
	public void setExpireDate(String expireDate) {
		ExpireDate = expireDate;
	}
	public String getUpdateVersion() {
		return UpdateVersion;
	}
	public void setUpdateVersion(String updateVersion) {
		UpdateVersion = updateVersion;
	}
	public long getTotalLongPosition() {
		return TotalLongPosition;
	}
	public void setTotalLongPosition(long totalLongPosition) {
		TotalLongPosition = totalLongPosition;
	}
	public double getSecurityClosePx() {
		return SecurityClosePx;
	}
	public void setSecurityClosePx(double securityClosePx) {
		SecurityClosePx = securityClosePx;
	}
	public double getSettlPrice() {
		return SettlPrice;
	}
	public void setSettlPrice(double settlPrice) {
		SettlPrice = settlPrice;
	}
	public double getUnderlyingClosePx() {
		return UnderlyingClosePx;
	}
	public void setUnderlyingClosePx(double underlyingClosePx) {
		UnderlyingClosePx = underlyingClosePx;
	}
	public String getPriceLimitType() {
		return PriceLimitType;
	}
	public void setPriceLimitType(String priceLimitType) {
		PriceLimitType = priceLimitType;
	}
	public double getDailyPriceUpLimit() {
		return DailyPriceUpLimit;
	}
	public void setDailyPriceUpLimit(double dailyPriceUpLimit) {
		DailyPriceUpLimit = dailyPriceUpLimit;
	}
	public double getDailyPriceDownLimit() {
		return DailyPriceDownLimit;
	}
	public void setDailyPriceDownLimit(double dailyPriceDownLimit) {
		DailyPriceDownLimit = dailyPriceDownLimit;
	}
	public double getMarginUnit() {
		return MarginUnit;
	}
	public void setMarginUnit(double marginUnit) {
		MarginUnit = marginUnit;
	}
	public float getMarginRatioParam1() {
		return MarginRatioParam1;
	}
	public void setMarginRatioParam1(float marginRatioParam1) {
		MarginRatioParam1 = marginRatioParam1;
	}
	public float getMarginRatioParam2() {
		return MarginRatioParam2;
	}
	public void setMarginRatioParam2(float marginRatioParam2) {
		MarginRatioParam2 = marginRatioParam2;
	}
	public long getRoundLot() {
		return RoundLot;
	}
	public void setRoundLot(long roundLot) {
		RoundLot = roundLot;
	}
	public long getLmtOrdMinFloor() {
		return LmtOrdMinFloor;
	}
	public void setLmtOrdMinFloor(long lmtOrdMinFloor) {
		LmtOrdMinFloor = lmtOrdMinFloor;
	}
	public long getLmtOrdMaxFloor() {
		return LmtOrdMaxFloor;
	}
	public void setLmtOrdMaxFloor(long lmtOrdMaxFloor) {
		LmtOrdMaxFloor = lmtOrdMaxFloor;
	}
	public long getMktOrdMinFloor() {
		return MktOrdMinFloor;
	}
	public void setMktOrdMinFloor(long mktOrdMinFloor) {
		MktOrdMinFloor = mktOrdMinFloor;
	}
	public long getMktOrdMaxFloor() {
		return MktOrdMaxFloor;
	}
	public void setMktOrdMaxFloor(long mktOrdMaxFloor) {
		MktOrdMaxFloor = mktOrdMaxFloor;
	}
	public double getTickSize() {
		return TickSize;
	}
	public void setTickSize(double tickSize) {
		TickSize = tickSize;
	}
	public String getSecurityStatusFlag() {
		return SecurityStatusFlag;
	}
	public void setSecurityStatusFlag(String securityStatusFlag) {
		SecurityStatusFlag = securityStatusFlag;
	}
	public String getListID() {
		return ListID;
	}
	public void setListID(String listID) {
		ListID = listID;
	}
	public int getLeaveDay() {
		return LeaveDay;
	}
	public void setLeaveDay(int leaveDay) {
		LeaveDay = leaveDay;
	}
	public int getiPC() {
		return iPC;
	}
	public void setiPC(int iPC) {
		this.iPC = iPC;
	}
	 
	

}
