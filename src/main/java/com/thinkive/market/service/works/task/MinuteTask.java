package com.thinkive.market.service.works.task;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.thinkive.base.config.Configuration;
import com.thinkive.market.bean.StockOption;
import com.thinkive.market.service.cache.HQDataCache;
import com.thinkive.market.service.cache.HQStateCache;
import com.thinkive.market.service.dao.ThinkConvDao;

/**
 * @����: ������Ȩ��ʱ���ݴ���
 * @��Ȩ: Copyright (c) 2012 
 * @��˾: ˼�ϿƼ� 
 * @����: ����
 * @�汾: 1.0 
 * @��������: 2015-1-17
 * @����ʱ��: ����11:35:39
 */
public class MinuteTask extends BaseConvTask
{
	private static Logger		logger		= Logger.getLogger("MinuteTask");
											
	public static final String	MINUTE_DATA	= "minutedata";
											
	//���Է�ʱ����   ��c������ȡ
	public static void main(String[] args)
	{
		ThinkConvDao convDao = null;
		convDao = new ThinkConvDao(false);
		String market = "0";
		String stockCode = "11000001";
		try
		{
			//List minuteHq = 
			convDao.queryMinuteData(stockCode, market, 0);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if ( convDao != null )
			{
				convDao.close();
			}
		}
	}
	
	@Override
	public void init(String param)
	{
		//���Ϊ������Ҫ��ʼ����ʱͼ
		ThinkConvDao convDao = null;
		try
		{	
			convDao = new ThinkConvDao(false);
			Map minuteMap = (Map) HQDataCache.getData(MINUTE_DATA);
			minuteMap = new ConcurrentHashMap();
			logger.info("������Ȩ��ʱͼ���ݿ�ʼ��ʼ��-------");
			StockOption[] stockOptionArray = HQDataCache.getStockOptionArray();
			if ( null != stockOptionArray && stockOptionArray.length > 0 )
			{
				for (int i = 0; i < stockOptionArray.length; i++)
				{
					String stockCode = stockOptionArray[i].getSecurityID();
					String market = stockOptionArray[i].getMarket();
					
					List minuteHq = convDao.queryMinuteData(stockCode, market, 0);
					minuteMap.put(market + stockCode, minuteHq);
				}
			}
			else
			{
				logger.warn("������Ȩ�б�stockOptionArrayΪ�գ�����");
			}
			
			HQDataCache.setData(MINUTE_DATA, minuteMap);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			logger.warn("ThinkConvDaoת��������쳣������");
		}
		finally
		{
			if ( convDao != null )
			{
				convDao.close();
			}
		}
		
	}
	
	@Override
	public void update() throws Exception
	{
		/*
		if ( !HQStateCache.isNeedUpdate() )
		{
			return;
		}
		
		ThinkConvDao convDao = null;
		try
		{
			convDao = new ThinkConvDao(false);
			
			Map minuteMap = (Map) HQDataCache.getData(MINUTE_DATA);
			//��ʱ���������Ϊ�ղ��ҽ���ģʽΪnone �� ���·�ʱ����
			if ( null != minuteMap )
			{
				logger.info("������Ȩ��ʱ���ݿ�ʼ����-------");
				// long t1 = System.currentTimeMillis();
				//��ʱ���ݲ�������
				String mode = Configuration.getString("receiver.mode");
				//if ("none".equalsIgnoreCase(mode) ) {
				//none��ʾ������շ�����û���� ������ȡ���·�ʱ����     ������ȡ   ֻ��������
				//��ǰ��java����û�����ͷ�ʱ����  �����ǲ���none   ��Ҫ����
				StockOption[] stockOptionArray = HQDataCache.getStockOptionArray();
				for (int i = 0; i < stockOptionArray.length; i++)
				{
					String stockCode = stockOptionArray[i].getSecurityID();
					String market = stockOptionArray[i].getMarket();
					List minuteHq = (List) minuteMap.get(market + stockCode);
					//��ȡ����
					List minuteHqInc = convDao.queryMinuteData(stockCode, market, minuteHq.size());
					minuteHq.addAll(minuteHqInc);
					minuteMap.put(market + stockCode, minuteHq);
				}
				//}
				HQDataCache.setData(MINUTE_DATA, minuteMap);
				// logger.info("��ʱͼ������ɣ���ʱ��" + (System.currentTimeMillis() - t1));
			}
			//��ʱ�������Ϊ������Ҫ��ʼ����ʱͼ
			else
			{
				minuteMap = new ConcurrentHashMap();
				StockOption[] stockOptionArray = HQDataCache.getStockOptionArray();
				if ( null != stockOptionArray && stockOptionArray.length > 0 )
				{
					for (int i = 0; i < stockOptionArray.length; i++)
					{
						String stockCode = stockOptionArray[i].getSecurityID();
						String market = stockOptionArray[i].getMarket();
						List minuteHq = convDao.queryMinuteData(stockCode, market, 0);
						minuteMap.put(market + stockCode, minuteHq);
					}
				}
				else
				{
					logger.warn("������Ȩ�б�stockOptionArrayΪ�գ�����");
				}
				HQDataCache.setData(MINUTE_DATA, minuteMap);
			}
			
		}
		finally
		{
			if ( convDao != null )
			{
				convDao.close();
			}
		}
		
		*/}
		
	@Override
	public void clear()
	{
		HQDataCache.setData(MINUTE_DATA, null);
	}
	
}
