package com.thinkive.market.service.conn.push;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;

import org.apache.log4j.Logger;

import com.thinkive.market.bean.BaseInfo;
import com.thinkive.market.bean.StockOption;
import com.thinkive.market.service.cache.HQDataCache;
import com.thinkive.market.service.util.ByteStrHelper;
import com.thinkive.market.service.util.MarketUtil;
import com.thinkive.market.service.works.task.BaseInfoTask;

/**
 * @描述: 个股期权实时数据更新 从转码机接收数据 需要配置接收模式tcp
 * @版权: Copyright (c) 2013
 * @公司: 思迪科技
 * @作者: 熊攀
 * @版本: 1.0
 * @创建日期: 2015-1-7
 * @创建时间: 上午11:15:27
 */
public class Updater10000 extends Updater
{
	private static Logger	logger	= Logger.getLogger(Updater10000.class);
									
	private static Updater	updater;
							
	private Updater10000()
	{
	}
	
	public static Updater getInstance()
	{
		if ( updater == null )
		{
			updater = new Updater10000();
			updater.setName("Updater10000");
			updater.start();
		}
		return updater;
	}
	
	/**
	 * @描述：返回 ：证券代码:现价
	 * @作者：熊攀
	 * @时间：2012-5-4 下午5:34:52
	 * @param data
	 */
	@Override
	public void update(byte[] b)
	{
		ByteBuffer dataBuffer = ByteBuffer.wrap(b);
		dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
		String market = ByteStrHelper.getString(dataBuffer, 2);
		String codeTotal = ByteStrHelper.getString(dataBuffer, 8);// 8个字节 期权编码8位
		
		Map<String, StockOption> stockOptionMap = HQDataCache.getStockOptionMap();
		String key = market + codeTotal;
		StockOption stockOption = stockOptionMap.get(key);
		if ( null != stockOption )
		{
			String MDStreamID = ByteStrHelper.getString(dataBuffer, 6);// 行情数据类型
																		// [6]
			String SecurityID = ByteStrHelper.getString(dataBuffer, 9);// 合约编码
																		// [9]
			long TotalLongPosition = dataBuffer.getLong();// 当前合约未平仓数 持仓 今持仓量
			long TradeVolume = dataBuffer.getLong();// 成交数量
			
			double TotalValueTraded = dataBuffer.getDouble();// 成交金额 分
			double PreSettlPrice = dataBuffer.getDouble();// 昨日结算价
			double OpenPrice = dataBuffer.getDouble();// 今日开盘价
			double AuctionPrice = dataBuffer.getDouble();// 动态参考价格
			
			long AuctionQty = dataBuffer.getLong();// 虚拟匹配数量
			double HighPrice = dataBuffer.getDouble();// 最高价
			double LowPrice = dataBuffer.getDouble();// 最低价
			double TradePrice = dataBuffer.getDouble();// 最新价
			
			double BuyPrice1 = dataBuffer.getDouble();// 申买价一
			long BuyVolume1 = dataBuffer.getLong();// 申买量一
			double SellPrice1 = dataBuffer.getDouble();//
			long SellVolume1 = dataBuffer.getLong();
			
			double BuyPrice2 = dataBuffer.getDouble();
			long BuyVolume2 = dataBuffer.getLong();
			double SellPrice2 = dataBuffer.getDouble();
			long SellVolume2 = dataBuffer.getLong();
			
			double BuyPrice3 = dataBuffer.getDouble();
			long BuyVolume3 = dataBuffer.getLong();
			double SellPrice3 = dataBuffer.getDouble();
			long SellVolume3 = dataBuffer.getLong();
			
			double BuyPrice4 = dataBuffer.getDouble();
			long BuyVolume4 = dataBuffer.getLong();
			double SellPrice4 = dataBuffer.getDouble();
			long SellVolume4 = dataBuffer.getLong();
			
			double BuyPrice5 = dataBuffer.getDouble();
			long BuyVolume5 = dataBuffer.getLong();
			double SellPrice5 = dataBuffer.getDouble();
			long SellVolume5 = dataBuffer.getLong();
			
			double SettlPrice = dataBuffer.getDouble();// 今日结算价
			
			/*
			 * 该字段为4位字符串，左起每位表示特定的含义，无定义则填空格。
			 * 第1位：‘S’表示启动（开市前）时段，‘C’表示集合竞价时段，‘T’表示连续交易时段
			 * ，‘B’表示休市时段，‘E’表示闭市时段，‘V’表示波动性中断，‘P’表示临时停牌。
			 * 第2位：‘0’表示未连续停牌，‘1’表示连续停牌
			 */
			String TradingPhaseCode = ByteStrHelper.getString(dataBuffer, 5);// 产品实时阶段及标志
																				// [5]
			String Timestamp = ByteStrHelper.getString(dataBuffer, 13);// 时间戳
																		// [13]
			long thedeal = dataBuffer.getLong(); // 现量
			
			long inside = dataBuffer.getLong(); // 总卖盘
			long outside = dataBuffer.getLong(); // 总买盘
			int inoutflag = dataBuffer.getInt(); // 内外盘标志
			float thechange = dataBuffer.getFloat(); // 笔升跌
			
			int dealno = dataBuffer.getInt();// 成交明细数量
			int serno = dataBuffer.getInt();// 序号
			long iRoundLot = dataBuffer.getLong();// 每张合约的股数
			
			stockOption.setMDStreamID(MDStreamID);
			stockOption.setSecurityID(SecurityID);
			stockOption.setTotalLongPosition(TotalLongPosition);
			stockOption.setTradeVolume(TradeVolume);
			
			stockOption.setTotalValueTraded(TotalValueTraded);
			stockOption.setPreSettlPrice(PreSettlPrice);
			stockOption.setOpenPrice(OpenPrice);
			stockOption.setAuctionPrice(AuctionPrice);
			
			stockOption.setAuctionQty(AuctionQty);
			stockOption.setHighPrice(HighPrice);
			stockOption.setLowPrice(LowPrice);
			stockOption.setTradePrice(TradePrice);
			
			stockOption.setBuyPrice1(BuyPrice1);
			stockOption.setBuyVolume1(BuyVolume1);
			stockOption.setSellPrice1(SellPrice1);
			stockOption.setSellVolume1(SellVolume1);
			stockOption.setBuyPrice2(BuyPrice2);
			stockOption.setBuyVolume2(BuyVolume2);
			stockOption.setSellPrice2(SellPrice2);
			stockOption.setSellVolume2(SellVolume2);
			stockOption.setBuyPrice3(BuyPrice3);
			stockOption.setBuyVolume3(BuyVolume3);
			stockOption.setSellPrice3(SellPrice3);
			stockOption.setSellVolume3(SellVolume3);
			
			stockOption.setBuyPrice4(BuyPrice4);
			stockOption.setBuyVolume4(BuyVolume4);
			stockOption.setSellPrice4(SellPrice4);
			stockOption.setSellVolume4(SellVolume4);
			stockOption.setBuyPrice5(BuyPrice5);
			stockOption.setBuyVolume5(BuyVolume5);
			stockOption.setSellPrice5(SellPrice5);
			stockOption.setSellVolume5(SellVolume5);
			
			stockOption.setSettlPrice(SettlPrice);
			stockOption.setTradingPhaseCode(TradingPhaseCode);
			stockOption.setTimestamp(Timestamp);
			stockOption.setThedeal(thedeal);
			
			stockOption.setInside(inside);
			stockOption.setOutside(outside);
			stockOption.setInoutflag(inoutflag);
			stockOption.setThechange(thechange);
			stockOption.setDealno(dealno);
			stockOption.setSerno(serno);
			stockOption.setiRoundLot(iRoundLot);
			
			Map baseInfoMap = (Map) HQDataCache.getData(BaseInfoTask.BASE_INFO);
			// 更新基础数据
			if ( baseInfoMap != null )
			{
				BaseInfo baseInfo = (BaseInfo) baseInfoMap.get(key);
				stockOption = MarketUtil.updateStockData(stockOption, baseInfo);
			}
			stockOptionMap.put(key, stockOption);
		}
		else
		{
			logger.info("行情服务器未能找到该期权编码，key:" + key);
		}
		logger.debug("Updater10000处理包成功，key:" + key);
		
	}
}
