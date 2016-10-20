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
 * @����: ������Ȩʵʱ���ݸ��� ��ת����������� ��Ҫ���ý���ģʽtcp
 * @��Ȩ: Copyright (c) 2013
 * @��˾: ˼�ϿƼ�
 * @����: ����
 * @�汾: 1.0
 * @��������: 2015-1-7
 * @����ʱ��: ����11:15:27
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
	 * @���������� ��֤ȯ����:�ּ�
	 * @���ߣ�����
	 * @ʱ�䣺2012-5-4 ����5:34:52
	 * @param data
	 */
	@Override
	public void update(byte[] b)
	{
		ByteBuffer dataBuffer = ByteBuffer.wrap(b);
		dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
		String market = ByteStrHelper.getString(dataBuffer, 2);
		String codeTotal = ByteStrHelper.getString(dataBuffer, 8);// 8���ֽ� ��Ȩ����8λ
		
		Map<String, StockOption> stockOptionMap = HQDataCache.getStockOptionMap();
		String key = market + codeTotal;
		StockOption stockOption = stockOptionMap.get(key);
		if ( null != stockOption )
		{
			String MDStreamID = ByteStrHelper.getString(dataBuffer, 6);// ������������
																		// [6]
			String SecurityID = ByteStrHelper.getString(dataBuffer, 9);// ��Լ����
																		// [9]
			long TotalLongPosition = dataBuffer.getLong();// ��ǰ��Լδƽ���� �ֲ� ��ֲ���
			long TradeVolume = dataBuffer.getLong();// �ɽ�����
			
			double TotalValueTraded = dataBuffer.getDouble();// �ɽ���� ��
			double PreSettlPrice = dataBuffer.getDouble();// ���ս����
			double OpenPrice = dataBuffer.getDouble();// ���տ��̼�
			double AuctionPrice = dataBuffer.getDouble();// ��̬�ο��۸�
			
			long AuctionQty = dataBuffer.getLong();// ����ƥ������
			double HighPrice = dataBuffer.getDouble();// ��߼�
			double LowPrice = dataBuffer.getDouble();// ��ͼ�
			double TradePrice = dataBuffer.getDouble();// ���¼�
			
			double BuyPrice1 = dataBuffer.getDouble();// �����һ
			long BuyVolume1 = dataBuffer.getLong();// ������һ
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
			
			double SettlPrice = dataBuffer.getDouble();// ���ս����
			
			/*
			 * ���ֶ�Ϊ4λ�ַ���������ÿλ��ʾ�ض��ĺ��壬�޶�������ո�
			 * ��1λ����S����ʾ����������ǰ��ʱ�Σ���C����ʾ���Ͼ���ʱ�Σ���T����ʾ��������ʱ��
			 * ����B����ʾ����ʱ�Σ���E����ʾ����ʱ�Σ���V����ʾ�������жϣ���P����ʾ��ʱͣ�ơ�
			 * ��2λ����0����ʾδ����ͣ�ƣ���1����ʾ����ͣ��
			 */
			String TradingPhaseCode = ByteStrHelper.getString(dataBuffer, 5);// ��Ʒʵʱ�׶μ���־
																				// [5]
			String Timestamp = ByteStrHelper.getString(dataBuffer, 13);// ʱ���
																		// [13]
			long thedeal = dataBuffer.getLong(); // ����
			
			long inside = dataBuffer.getLong(); // ������
			long outside = dataBuffer.getLong(); // ������
			int inoutflag = dataBuffer.getInt(); // �����̱�־
			float thechange = dataBuffer.getFloat(); // ������
			
			int dealno = dataBuffer.getInt();// �ɽ���ϸ����
			int serno = dataBuffer.getInt();// ���
			long iRoundLot = dataBuffer.getLong();// ÿ�ź�Լ�Ĺ���
			
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
			// ���»�������
			if ( baseInfoMap != null )
			{
				BaseInfo baseInfo = (BaseInfo) baseInfoMap.get(key);
				stockOption = MarketUtil.updateStockData(stockOption, baseInfo);
			}
			stockOptionMap.put(key, stockOption);
		}
		else
		{
			logger.info("���������δ���ҵ�����Ȩ���룬key:" + key);
		}
		logger.debug("Updater10000������ɹ���key:" + key);
		
	}
}
