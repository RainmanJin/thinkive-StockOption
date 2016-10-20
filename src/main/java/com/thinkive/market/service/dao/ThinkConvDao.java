package com.thinkive.market.service.dao;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.thinkive.market.bean.BaseInfo;
import com.thinkive.market.bean.DealData;
import com.thinkive.market.bean.KLineData;
import com.thinkive.market.bean.MCState;
import com.thinkive.market.bean.MinuteHQ;
import com.thinkive.market.bean.StockOption;
import com.thinkive.market.service.conn.HQDataProvider;
import com.thinkive.market.service.conn.PackageData;
import com.thinkive.market.service.util.ByteStrHelper;

/**
 * @描述: 用于提供从转码机获取数据的各种方法
 * @版权: Copyright (c) 2013 
 * @公司: 思迪科技 
 * @作者: 岳知之
 * @版本: 1.0 
 * @创建日期: 2013-11-5 
 * @创建时间: 下午2:11:48
 */
public class ThinkConvDao
{
	private HQDataProvider	provider;
							
	protected static String	SOH	= new String(new byte[] { 1 });
								
	protected static String	STX	= new String(new byte[] { 2 });
								
	public ThinkConvDao()
	{
		this.provider = new HQDataProvider(true);
	}
	
	/**
	 * 描述： 构造方法
	 * @param isClose 为提高效率在批量处理的时候不关闭连接，而改为手动关闭，需要写finally中的close方法
	 */
	public ThinkConvDao(boolean isClose)
	{
		this.provider = new HQDataProvider(isClose);
	}
	
	private static Logger logger = Logger.getLogger(ThinkConvDao.class);
	
	/**
	 * @描述：获取服务器状态信息
	 * @作者：岳知之
	 * @时间：2012-4-6 下午1:39:33
	 * @return
	 * @throws Exception
	 */
	public MCState queryMCState(InetSocketAddress socketAddress) throws Exception
	{
		MCState state = null;
		
		Map param = new HashMap();
		param.put("funcno", 90001);
		provider.setSocketAddress(socketAddress);
		PackageData pkgData = provider.sendRequest(param);
		
		byte[] data = null;
		if ( pkgData != null )
		{
			data = pkgData.getData();
		}
		if ( data != null )
		{
			ByteBuffer dataBuffer = ByteBuffer.wrap(data);
			dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
			int currentDate = dataBuffer.getInt();
			
			int initDate_sh = dataBuffer.getInt();
			long dbfTime_sh = dataBuffer.getLong();
			
			int initDate_sz = dataBuffer.getInt();
			long dbfTime_sz = dataBuffer.getLong();
			
			state = new MCState();
			
			if ( currentDate != 0 )
			{
				state.setCurHqDate(String.valueOf(currentDate));
			}
			if ( initDate_sh != 0 )
			{
				state.setInitDate_sh(String.valueOf(initDate_sh));
			}
			if ( initDate_sz != 0 )
			{
				state.setInitDate_sz(String.valueOf(initDate_sz));
			}
			
			state.setDbfTime_sh(dbfTime_sh);
			state.setDbfTime_sz(dbfTime_sz);
		}
		return state;
	}
	
	/**
	 * @描述：获取服务器状态信息
	 * @作者：岳知之
	 * @时间：2012-4-6 下午1:39:33
	 * @return
	 * @throws Exception
	 */
	public MCState queryMCState() throws Exception
	{
		MCState state = null;
		Map param = new HashMap();
		param.put("funcno", 90001);
		PackageData pkgData = provider.sendRequest(param);
		byte[] data = null;
		if ( pkgData != null )
		{
			data = pkgData.getData();
		}
		if ( data != null )
		{
			ByteBuffer dataBuffer = ByteBuffer.wrap(data);
			dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
			int currentDate = dataBuffer.getInt();
			
			int initDate_sh = dataBuffer.getInt();
			long dbfTime_sh = dataBuffer.getLong();
			
			int initDate_sz = dataBuffer.getInt();
			long dbfTime_sz = dataBuffer.getLong();
			
			state = new MCState();
			
			if ( currentDate != 0 )
			{
				state.setCurHqDate(String.valueOf(currentDate));
			}
			if ( initDate_sh != 0 )
			{
				state.setInitDate_sh(String.valueOf(initDate_sh));
			}
			if ( initDate_sz != 0 )
			{
				state.setInitDate_sz(String.valueOf(initDate_sz));
			}
			
			state.setDbfTime_sh(dbfTime_sh);
			state.setDbfTime_sz(dbfTime_sz);
		}
		
		return state;
	}
	
	/**
	 * @描述：查询获取所有个股期权数据   实时数据
	 * @作者：熊攀
	 * @时间：2015-1-15 下午3:00:38
	 * @return
	 * @throws Exception
	 */
	public StockOption[] queryAllStockList() throws Exception
	{
		StockOption[] stockOPtionList = null;
		Map param = new HashMap();
		param.put("funcno", 10004);
		PackageData pkgData = provider.sendRequest(param);
		byte[] data = null;
		if ( pkgData != null )
		{
			data = pkgData.getData();
		}
		if ( data != null )
		{
			int STRUCT_LENGTH = 329;
			ByteBuffer dataBuffer = ByteBuffer.wrap(data);
			dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
			int size = data.length / STRUCT_LENGTH;
			stockOPtionList = new StockOption[size];
			for (int i = 0; i < size; i++)
			{
				StockOption stockOption = new StockOption();
				
				String MDStreamID = getString(dataBuffer, 6);
				String SecurityID = getString(dataBuffer, 9);
				long TotalLongPosition = dataBuffer.getLong();// 当前合约未平仓数
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
				double SellPrice1 = dataBuffer.getDouble();
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
				
				double SettlPrice = dataBuffer.getDouble();//今日结算价
				String TradingPhaseCode = getString(dataBuffer, 5);//产品实时阶段及标志
				String Timestamp = getString(dataBuffer, 13); //时间戳    
				long thedeal = dataBuffer.getLong();//现手
				
				long inside = dataBuffer.getLong();//总卖盘
				long outside = dataBuffer.getLong();//总买盘
				int inoutflag = dataBuffer.getInt(); // 内外盘标志
				float thechange = dataBuffer.getFloat();//笔升跌
				
				int dealno = dataBuffer.getInt(); //成交明细数量
				int serno = dataBuffer.getInt(); //序号
				long iRoundLot = dataBuffer.getLong();
				
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
				stockOPtionList[i] = stockOption;
			}
		}
		return stockOPtionList;
	}
	
	public StockOption[] queryAllStockList(String market) throws Exception
	{
		StockOption[] stockOPtionList = null;
		Map<String, Object> param = new HashMap();
		param.put("funcno", 10005);
		param.put("market", market);
		PackageData pkgData = provider.sendRequest(param);
		byte[] data = null;
		if ( pkgData != null )
		{
			data = pkgData.getData();
		}
		if ( data != null )
		{
			int STRUCT_LENGTH = 329;
			ByteBuffer dataBuffer = ByteBuffer.wrap(data);
			dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
			int size = data.length / STRUCT_LENGTH;
			stockOPtionList = new StockOption[size];
			for (int i = 0; i < size; i++)
			{
				StockOption stockOption = new StockOption();
				
				String MDStreamID = getString(dataBuffer, 6);
				String SecurityID = getString(dataBuffer, 9);
				long TotalLongPosition = dataBuffer.getLong();// 当前合约未平仓数
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
				double SellPrice1 = dataBuffer.getDouble();
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
				
				double SettlPrice = dataBuffer.getDouble();//今日结算价
				String TradingPhaseCode = getString(dataBuffer, 5);//产品实时阶段及标志
				String Timestamp = getString(dataBuffer, 13); //时间戳    
				long thedeal = dataBuffer.getLong();//现手
				
				long inside = dataBuffer.getLong();//总卖盘
				long outside = dataBuffer.getLong();//总买盘
				int inoutflag = dataBuffer.getInt(); // 内外盘标志
				float thechange = dataBuffer.getFloat();//笔升跌
				
				int dealno = dataBuffer.getInt(); //成交明细数量
				int serno = dataBuffer.getInt(); //序号
				long iRoundLot = dataBuffer.getLong();
				
				stockOption.setMarket(market);
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
				
				stockOPtionList[i] = stockOption;
			}
		}
		return stockOPtionList;
	}
	
	/**
	 * @描述：查询获取所有個股期權基础数据
	 * @作者：熊攀
	 * @时间：2015-1-15 下午3:00:38
	 * @return
	 * @throws Exception
	 */
	public Map queryAllBaseInfo() throws Exception
	{
		Map result = new HashMap();
		
		Map param = new HashMap();
		param.put("funcno", 10006);
		PackageData pkgData = provider.sendRequest(param);
		byte[] data = null;
		if ( pkgData != null )
		{
			data = pkgData.getData();
		}
		if ( data != null )
		{
			int STRUCT_LENGTH = 279;
			ByteBuffer dataBuffer = ByteBuffer.wrap(data);
			dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
			int size = data.length / STRUCT_LENGTH;
			for (int i = 0; i < size; i++)
			{
				BaseInfo baseInfo = new BaseInfo();
				baseInfo.setRFStreamID(ByteStrHelper.getString(dataBuffer, 6));
				baseInfo.setSecurityID(ByteStrHelper.getString(dataBuffer, 9));
				baseInfo.setContractID(ByteStrHelper.getString(dataBuffer, 20));
				baseInfo.setContractSymbol(ByteStrHelper.getString(dataBuffer, 21));
				
				baseInfo.setUnderlyingSecurityID(ByteStrHelper.getString(dataBuffer, 7));
				baseInfo.setUnderlyingSymbol(ByteStrHelper.getString(dataBuffer, 9));
				baseInfo.setUnderlyingType(ByteStrHelper.getString(dataBuffer, 4));
				baseInfo.setOptionType(ByteStrHelper.getString(dataBuffer, 2));
				
				baseInfo.setCallOrPut(ByteStrHelper.getString(dataBuffer, 2));
				baseInfo.setContractMultiplierUnit(dataBuffer.getLong());
				baseInfo.setExercisePrice(dataBuffer.getDouble());
				baseInfo.setStartDate(ByteStrHelper.getString(dataBuffer, 9));
				
				baseInfo.setEndDate(ByteStrHelper.getString(dataBuffer, 9));
				baseInfo.setExerciseDate(ByteStrHelper.getString(dataBuffer, 9));
				baseInfo.setDeliveryDate(ByteStrHelper.getString(dataBuffer, 9));
				baseInfo.setExpireDate(ByteStrHelper.getString(dataBuffer, 9));
				
				baseInfo.setUpdateVersion(ByteStrHelper.getString(dataBuffer, 2));
				baseInfo.setTotalLongPosition(dataBuffer.getLong());
				baseInfo.setSecurityClosePx(dataBuffer.getDouble());
				baseInfo.setSettlPrice(dataBuffer.getDouble());
				
				baseInfo.setUnderlyingClosePx(dataBuffer.getDouble());
				baseInfo.setPriceLimitType(ByteStrHelper.getString(dataBuffer, 2));
				baseInfo.setDailyPriceUpLimit(dataBuffer.getDouble());
				baseInfo.setDailyPriceDownLimit(dataBuffer.getDouble());
				
				baseInfo.setMarginUnit(dataBuffer.getDouble());
				baseInfo.setMarginRatioParam1(dataBuffer.getFloat());
				baseInfo.setMarginRatioParam2(dataBuffer.getFloat());
				baseInfo.setRoundLot(dataBuffer.getLong());
				
				baseInfo.setLmtOrdMinFloor(dataBuffer.getLong());
				baseInfo.setLmtOrdMaxFloor(dataBuffer.getLong());
				baseInfo.setMktOrdMinFloor(dataBuffer.getLong());
				baseInfo.setMktOrdMaxFloor(dataBuffer.getLong());
				
				baseInfo.setTickSize(dataBuffer.getDouble());
				baseInfo.setSecurityStatusFlag(ByteStrHelper.getString(dataBuffer, 9));
				
				baseInfo.setListID(ByteStrHelper.getString(dataBuffer, 5));
				baseInfo.setLeaveDay(dataBuffer.getInt());
				baseInfo.setiPC(dataBuffer.getInt());
				result.put(baseInfo.getSecurityID(), baseInfo);// <"09102",hkBaseInfo>
			}
			
		}
		return result;
	}
	
	public Map<String, BaseInfo> queryAllBaseInfo(String market) throws Exception
	{
		Map<String, BaseInfo> result = new HashMap<String, BaseInfo>();
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("funcno", 10007);
		param.put("market", market);
		PackageData pkgData = provider.sendRequest(param);
		byte[] data = null;
		if ( pkgData != null )
		{
			data = pkgData.getData();
		}
		if ( data != null )
		{
			int STRUCT_LENGTH = 319;
			ByteBuffer dataBuffer = ByteBuffer.wrap(data);
			dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
			int size = data.length / STRUCT_LENGTH;
			for (int i = 0; i < size; i++)
			{
				BaseInfo baseInfo = new BaseInfo();
				baseInfo.setRFStreamID(ByteStrHelper.getString(dataBuffer, 6));
				baseInfo.setSecurityID(ByteStrHelper.getString(dataBuffer, 9));
				baseInfo.setContractID(ByteStrHelper.getString(dataBuffer, 40));
				baseInfo.setContractSymbol(ByteStrHelper.getString(dataBuffer, 40));
				
				baseInfo.setUnderlyingSecurityID(ByteStrHelper.getString(dataBuffer, 8));
				baseInfo.setUnderlyingSymbol(ByteStrHelper.getString(dataBuffer, 9));
				baseInfo.setUnderlyingType(ByteStrHelper.getString(dataBuffer, 4));
				baseInfo.setOptionType(ByteStrHelper.getString(dataBuffer, 2));
				
				baseInfo.setCallOrPut(ByteStrHelper.getString(dataBuffer, 2));
				baseInfo.setContractMultiplierUnit(dataBuffer.getLong());
				baseInfo.setExercisePrice(dataBuffer.getDouble());
				baseInfo.setStartDate(ByteStrHelper.getString(dataBuffer, 9));
				
				baseInfo.setEndDate(ByteStrHelper.getString(dataBuffer, 9));
				baseInfo.setExerciseDate(ByteStrHelper.getString(dataBuffer, 9));
				baseInfo.setDeliveryDate(ByteStrHelper.getString(dataBuffer, 9));
				baseInfo.setExpireDate(ByteStrHelper.getString(dataBuffer, 9));
				
				baseInfo.setUpdateVersion(ByteStrHelper.getString(dataBuffer, 2));
				baseInfo.setTotalLongPosition(dataBuffer.getLong());
				baseInfo.setSecurityClosePx(dataBuffer.getDouble());
				baseInfo.setSettlPrice(dataBuffer.getDouble());
				
				baseInfo.setUnderlyingClosePx(dataBuffer.getDouble());
				baseInfo.setPriceLimitType(ByteStrHelper.getString(dataBuffer, 2));
				baseInfo.setDailyPriceUpLimit(dataBuffer.getDouble());
				baseInfo.setDailyPriceDownLimit(dataBuffer.getDouble());
				
				baseInfo.setMarginUnit(dataBuffer.getDouble());
				baseInfo.setMarginRatioParam1(dataBuffer.getFloat());
				baseInfo.setMarginRatioParam2(dataBuffer.getFloat());
				baseInfo.setRoundLot(dataBuffer.getLong());
				
				baseInfo.setLmtOrdMinFloor(dataBuffer.getLong());
				baseInfo.setLmtOrdMaxFloor(dataBuffer.getLong());
				baseInfo.setMktOrdMinFloor(dataBuffer.getLong());
				baseInfo.setMktOrdMaxFloor(dataBuffer.getLong());
				
				baseInfo.setTickSize(dataBuffer.getDouble());
				baseInfo.setSecurityStatusFlag(ByteStrHelper.getString(dataBuffer, 9));
				
				baseInfo.setListID(ByteStrHelper.getString(dataBuffer, 5));
				baseInfo.setLeaveDay(dataBuffer.getInt());
				baseInfo.setiPC(dataBuffer.getInt());
				
				result.put(market + baseInfo.getSecurityID(), baseInfo);
			}
		}
		return result;
	}
	
	/**
	 * @描述：获取个股期权分时图数据
	 * @作者：熊攀
	 * @时间：2015-1-17  下午11:13:46
	 * @param stockCode
	 * @param market
	 * @param start
	 * @return
	 * @throws Exception
	 */
	public List queryMinuteData(String stockCode, String market, int start) throws Exception
	{
		List minuteHQ = new ArrayList();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("funcno", 10000);
		param.put("stock_code", stockCode);
		param.put("market", market);
		param.put("start", start);
		PackageData pkgData = provider.sendRequest(param);
		byte[] data = null;
		if ( pkgData != null )
		{
			data = pkgData.getData();
		}
		
		if ( data != null )
		{
			int STRUCT_LENGTH = 28;
			ByteBuffer dataBuffer = ByteBuffer.wrap(data);
			dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
			int size = data.length / STRUCT_LENGTH;
			for (int i = 0; i < size; i++)
			{
				MinuteHQ minHq = new MinuteHQ();
				minHq.setMinute((short) dataBuffer.getInt()); //分钟数
				minHq.setNow(dataBuffer.getFloat()); //现价
				minHq.setThedeal(dataBuffer.getInt()); //成交量
				minHq.setAverage(dataBuffer.getFloat()); //均价
				
				minHq.setYesterday(dataBuffer.getFloat());//昨收
				minHq.setTotalLongPosition(dataBuffer.getLong());////当前持仓量 
				minuteHQ.add(minHq);
			}
		}
		return minuteHQ;
	}
	
	/**
	 * @描述：获取个股期权成交明细
	 * @作者：熊攀
	 * @时间：2015-1-19 下午4:06:19
	 * @param stockCode
	 * @param market
	 * @param start
	 * @return
	 * @throws Exception
	 */
	public List<DealData> queryDealData(String stockCode, String market, int start) throws Exception
	{
		List<DealData> dealData = new ArrayList<DealData>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("funcno", 10003);
		param.put("stock_code", stockCode);
		param.put("market", market);
		param.put("start", start);
		PackageData pkgData = provider.sendRequest(param);
		byte[] data = null;
		if ( pkgData != null )
		{
			data = pkgData.getData();
		}
		if ( data != null )
		{
			int STRUCT_LENGTH = 20;
			ByteBuffer dataBuffer = ByteBuffer.wrap(data);
			dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
			int size = data.length / STRUCT_LENGTH;
			for (int i = 0; i < size; i++)
			{
				DealData deal = new DealData();
				deal.setMinute((short) dataBuffer.getInt());
				deal.setNow(dataBuffer.getFloat());
				deal.setThedeal(dataBuffer.getInt());
				deal.setFlag(dataBuffer.getInt());
				deal.setYesterday(dataBuffer.getFloat());
				if ( deal.getThedeal() > 0 )
				{
					dealData.add(deal);
				}
			}
		}
		return dealData;
	}
	
	/**
	 * @描述：获取个股期权日线数据
	 * @作者：熊攀
	 * @时间：2015-1-17 下午10:33:38
	 * @param stockCode
	 * @param market
	 * @param count
	 * @return
	 * @throws Exception 
	 */
	public KLineData[] queryKLineData(String stockCode, String market, int count) throws Exception
	{
		KLineData[] klineData = null;
		Map param = new HashMap();
		param.put("funcno", 10001);
		param.put("stock_code", stockCode);
		param.put("market", market);
		param.put("count", count);
		PackageData pkgData = provider.sendRequest(param);
		byte[] data = null;
		if ( pkgData != null )
		{
			data = pkgData.getData();
		}
		if ( data != null )
		{
			int STRUCT_LENGTH = 40;
			ByteBuffer dataBuffer = ByteBuffer.wrap(data);
			dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
			int size = data.length / STRUCT_LENGTH;
			klineData = new KLineData[size];
			// int stockType=StockHelper.getStockType(market, stockCode);
			for (int i = 0; i < size; i++)
			{
				KLineData kline = new KLineData();
				kline.setDate(dataBuffer.getInt()); //日期
				kline.setOpen(dataBuffer.getFloat()); //开盘价
				kline.setHigh(dataBuffer.getFloat()); //最高价 
				kline.setLow(dataBuffer.getFloat()); //最低价
				kline.setClose(dataBuffer.getFloat()); //收盘价
				kline.setMoney(dataBuffer.getDouble()); //成交金额
				kline.setVolume(dataBuffer.getLong()); //成交量
				kline.setYesClose(dataBuffer.getFloat()); //昨日收盘价
				
				klineData[i] = kline;
			}
		}
		return klineData;
	}
	
	protected String getString(ByteBuffer dataBuffer, int length)
	{
		String str = null;
		try
		{
			byte[] codeBuffer = new byte[length];
			dataBuffer.get(codeBuffer);
			str = new String(codeBuffer, 0, length - 1, "GBK").trim();
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	 * 获得length长度的字节数组，从头开始用str进行填充，不足的补0
	 *
	 * @param str
	 * @param length
	 * @return
	 */
	protected byte[] getBytes(String str, int length)
	{
		byte[] strBytes = str.getBytes();
		if ( strBytes.length >= length )
		{
			byte[] temp = new byte[length];
			System.arraycopy(strBytes, 0, temp, 0, length);
			return temp;
		}
		
		byte[] temp = new byte[length];
		for (int i = 0; i < length; i++)
		{
			temp[i] = 0;
		}
		
		for (int i = 0; i < strBytes.length; i++)
		{
			temp[i] = strBytes[i];
		}
		
		return temp;
	}
	
	public void close()
	{
		provider.close();
	}
}
