package com.thinkive.market.service.works.task;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.ajax.JSON;

import com.thinkive.market.bean.KLineData;
import com.thinkive.market.bean.StockOption;
import com.thinkive.market.service.cache.HQDataCache;
import com.thinkive.market.service.dao.ThinkConvDao;
import com.thinkive.market.service.util.MarketUtil;

/**
 * @����: ��ʼ��������ȨK�����ݣ��������ʼ�����ʼ��һ��
 * @��Ȩ: Copyright (c) 2012
 * @��˾: ˼�ϿƼ�
 * @����: ����
 * @�汾: 1.0
 * @��������: 2015-1-17
 * @����ʱ��: ����4:36:31
 */
public class KLineTask extends BaseConvTask
{
	private static Logger		logger		= Logger.getLogger("KLineTask");
											
	public static final String	KLINE_DAY	= "kline_day";
	public static final String	KLINE_WEEK	= "kline_week";
	public static final String	KLINE_MONTH	= "kline_month";
											
	public static void main(String[] args)
	{
		ThinkConvDao convDao = null;
		convDao = new ThinkConvDao(false);
		String market = "0";
		String stockCode = "20000062";
		try
		{
			convDao.queryKLineData(stockCode, market, 0);
		}
		catch (Exception e)
		{
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
	public void init(String param) throws Exception
	{
		ThinkConvDao convDao = null;
		try
		{
			convDao = new ThinkConvDao(false);
			long t1 = System.currentTimeMillis();
			
			StockOption[] stockOptionArray = HQDataCache.getStockOptionArray();
			Map dayMap = new ConcurrentHashMap();
			if ( null != stockOptionArray && stockOptionArray.length > 0 )
			{
				for (int i = 0; i < stockOptionArray.length; i++)
				{
					String stockCode = stockOptionArray[i].getSecurityID();
					String market = stockOptionArray[i].getMarket();
					KLineData[] klineData = convDao.queryKLineData(stockCode, market, 0);
					if ( null != klineData && klineData.length > 0 )
					{
						dayMap.put(market + stockCode, klineData);
					}
					else
					{
						// ����ת��� �����еĸ�����Ȩû������
						klineData = new KLineData[0];
						dayMap.put(market + stockCode, klineData);
					}
				}
			}
			
			HQDataCache.setData(KLINE_DAY, dayMap);
			logger.warn("��ʼ�������ļ����");
			Map weekMap = findPeriodKDataByDay(dayMap, "week");
			HQDataCache.setData(KLINE_WEEK, weekMap);
			logger.warn("��ʼ�������ļ����");
			Map monthMap = findPeriodKDataByDay(dayMap, "month");
			HQDataCache.setData(KLINE_MONTH, monthMap);
			logger.warn("��ʼ�������ļ����");
			logger.warn("����ͼ��ʼ����ɣ���ʱ��" + (System.currentTimeMillis() - t1));
			calcTech(param, dayMap);
			calcTech(param, weekMap);
			calcTech(param, monthMap);
			
		}
		finally
		{
			if ( convDao != null )
			{
				convDao.close();
			}
		}
		
	}
	
	private void calcTech(String param, Map dayMap)
	{
		if ( param != null && dayMap != null )
		{
			Map paramMap = (Map) JSON.parse(param);
			if ( paramMap != null )
			{
				Set dayKeySet = dayMap.keySet();
				for (Iterator iter = dayKeySet.iterator(); iter.hasNext();)
				{
					Object stockKey = (Object) iter.next();
					KLineData[] klineDataTmp = (KLineData[]) dayMap.get(stockKey);
					KLineData[] klineData = new KLineData[klineDataTmp.length + 1];
					System.arraycopy(klineDataTmp, 0, klineData, 0, klineDataTmp.length);
					klineData[klineData.length - 1] = new KLineData();
					if ( klineData == null || klineData.length == 0 )
					{
						continue;
					}
					Set keySet = paramMap.keySet();
					for (Iterator iterator = keySet.iterator(); iterator.hasNext();)
					{
						String key = (String) iterator.next();
						if ( "MA".equals(key) )
						{
							Object techParam[] = (Object[]) paramMap.get(key);
							for (int i = 0; i < techParam.length; i++)
							{
								TechUtil.MA(klineData, (int) (long) (Long) techParam[i], i);
							}
						}
					}
					klineDataTmp = null;
					dayMap.put(stockKey, klineData);
				}
			}
		}
	}
	
	@Override
	public void update()
	{
	
	}
	
	@Override
	public void clear()
	{
		HQDataCache.setData(KLINE_DAY, null);
		HQDataCache.setData(KLINE_WEEK, null);
		HQDataCache.setData(KLINE_MONTH, null);
	}
	
	/**
	 * @�������������߻�ȡ���߻�������
	 * @���ߣ���֪֮
	 * @ʱ�䣺2012-4-27 ����4:57:08
	 * @param dayMap
	 * @param type
	 *            week|month
	 * @return
	 */
	public Map findPeriodKDataByDay(Map dayMap, String type)
	{
		StockOption[] stockOptionArray = HQDataCache.getStockOptionArray();
		Map klineMap = new ConcurrentHashMap();
		for (int i = 0; i < stockOptionArray.length; i++)
		{
			String stockCode = stockOptionArray[i].getSecurityID();
			KLineData[] dayArray = (KLineData[]) dayMap.get(stockCode);
			if ( dayArray != null && dayArray.length > 0 )
			{
				if ( "week".equalsIgnoreCase(type) )
				{
					KLineData[] klineWeek = MarketUtil.getWeekHQDataByDayHQ(dayArray);
					klineMap.put(stockCode, klineWeek);
				}
				else if ( "month".equalsIgnoreCase(type) )
				{
					KLineData[] klineMonth = MarketUtil.getMonthHQDataByDayHQ(dayArray);
					klineMap.put(stockCode, klineMonth);
				}
			}
		}
		return klineMap;
	}
	
}
