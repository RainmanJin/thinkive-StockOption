/*
 * Copyright (c) 2009 Your Corporation. All Rights Reserved.
 */

package com.thinkive.market.bean;

/**
 * ����:������Ȩ�������� 
 * ��Ȩ:	 Copyright (c) 2007
 * ��˾:	 ˼�ϿƼ�
 * ����:	 ����
 * �汾:	 1.0
 * ��������: 2015-1-15
 * ����ʱ��: 17:14:07
 */
public class BaseInfo
{
     private String RFStreamID;//�ο��������ͱ�ʶ����ȡֵR0301��ʾ��Ȩ������Ϣ  [6]
	 private String SecurityID;//��Ȩ��Լ�ĺ�Լ����        [9]
	 private String ContractID;//��Լ���״���          [20]    =======��Լ���״���=======601318P1502M07500
	 private String ContractSymbol;//��Ȩ��Լ���         [21]
	 
	 private String UnderlyingSecurityID;//���֤ȯ����             [7]
	 private String UnderlyingSymbol;//���֤ȯ����               [9]
	 
	 private String UnderlyingType;//���֤ȯ����//EBS �C ETF�� ASH �C A��                      [4]
	 private String OptionType;//��Ϊŷʽ��Ȩ�����ֶ�Ϊ��E������Ϊ��ʽ��Ȩ�����ֶ�Ϊ��A��    [2]
	 
	 private String CallOrPut;//�Ϲ������ֶ�Ϊ��C������Ϊ�Ϲ������ֶ�Ϊ��P��          [2]
	 private long ContractMultiplierUnit;//��Լ��λ  ��һ����Ȩ��Լ�������Ĺ�����
	 private double ExercisePrice;//��Ȩ��Ȩ��,������Ȩ��Ϣ���������Ȩ��Ȩ�ۣ���ȷ��0.1��
	 private String StartDate;//�׸�������               [9]
	 
	 private String EndDate;//�������              [9]
	 private String ExerciseDate;//��Ȩ��Ȩ��          [9]
	 private String DeliveryDate;//��Ȩ������          [9]
	 private String ExpireDate;//��Ȩ������               [9]   ---20150128
	 
	 private String UpdateVersion;//��Լ�汾��         [2]
	 private long  TotalLongPosition;//��ǰ��Լδƽ���� ��λ�� ����)       ��ֲ���
	 private double SecurityClosePx;//�������̼ۣ��Ҷ��룬��λ��Ԫ����ȷ��0.1�壩
	 private double SettlPrice;//��Լǰ�����,���ս���ۣ�������Ȩ��Ϣ��Ϊ������Ľ���ۣ���Լ����������д�ο��ۣ����Ҷ��룬��λ��Ԫ����ȷ��0.1�壩
	
	
	private double UnderlyingClosePx;//���֤ȯǰ����,��Ȩ���֤ȯ��Ȩ��Ϣ�������ǰ���̼۸��Ҷ��룬��λ��Ԫ����ȷ��0.1��
	 private String PriceLimitType;//�ǵ�����������,��N�����ǵ�����������            [2]
	private double DailyPriceUpLimit;//�Ƿ����޼۸�
	private double DailyPriceDownLimit;//�������޼۸�
	
	private double MarginUnit;//��λ��֤��,���ճ���һ�ź�Լ����Ҫ�ı�֤����������ȷ����
	private float MarginRatioParam1;//��֤������������һ ��λ��%
	private float MarginRatioParam2;//��֤�������������� ��λ��%
	private long RoundLot;//������ һ�ֶ�Ӧ�ĺ�Լ��
	
	private long LmtOrdMinFloor;//�����޼��걨����
	private long LmtOrdMaxFloor;//�����޼��걨����
	private long MktOrdMinFloor;//�����м��걨����
	private long MktOrdMaxFloor;//�����м��걨����
	
	
	private double TickSize;//��С���۵�λ,��λ��Ԫ����ȷ��0.1��
	 private String SecurityStatusFlag;//��Ȩ��Լ״̬��Ϣ��ǩ      9
	 
	 
	 
	 
	 ////////
	 
	 private String ListID;//[5]1501
	 private int  LeaveDay;//���뵽����ʣ������
	 private int  iPC;//1�Ϲ�                                 0�Ϲ�
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
