package com.thinkive.market.service.works.task;

import com.thinkive.market.bean.DealData;
import com.thinkive.market.bean.StockOption;
import com.thinkive.market.service.cache.HQDataCache;
import com.thinkive.market.service.dao.ThinkConvDao;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @����: ���¸�����Ȩ�ɽ���ϸ
 * @��Ȩ: Copyright (c) 2012
 * @��˾: ˼�ϿƼ�
 * @����: ����
 * @�汾: 1.0
 * @��������: 2015-1-17
 * @����ʱ��: ����12:05:10
 */
public class DealDataTask extends BaseConvTask {
    public static final String DEAL_DATA = "dealdata";
    private static Logger logger = Logger.getLogger("DealDataTask");

    //���Դ�ת�����ȡ�ĳɽ���ϸ����
    public static void main(String[] args) {
        ThinkConvDao convDao = null;
        convDao = new ThinkConvDao(false);
        String market = "0";
        String stockCode = "11000001";
        try {
            convDao.queryDealData(stockCode, market, 0);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (convDao != null) {
                convDao.close();
            }
        }
    }

    @Override
    public void init(String param) throws Exception {
        Map<String, List<DealData>> dealData = HQDataCache.getData(DEAL_DATA);

        if (dealData == null) {
            dealData = new ConcurrentHashMap<String, List<DealData>>();
            HQDataCache.setData(DEAL_DATA, dealData);
        }
        StockOption[] stockOptionArray = HQDataCache.getStockOptionArray();
        ThinkConvDao convDao = null;
        try {
            convDao = new ThinkConvDao(false);
            if (null != stockOptionArray && stockOptionArray.length > 0) {
                for (int i = 0; i < stockOptionArray.length; i++) {
                    String stockCode = stockOptionArray[i].getSecurityID();
                    String market = stockOptionArray[i].getMarket();
                    List<DealData> deal = convDao.queryDealData(stockCode, market, 0);
                    dealData.put(market + stockCode, deal);
                }
            } else {
                logger.warn("������ȨstockOptionArrayΪ�գ�������");
            }
        } finally {
            if (convDao != null) {
                convDao.close();
            }
        }
        HQDataCache.setData(DEAL_DATA, dealData);
    }

    @Override
    public void update() {
        /*

		if ( !HQStateCache.isNeedUpdate() )
		{
			return;
		}
		
		Map dealData = (Map) HQDataCache.getData(DEAL_DATA);
		// ���Ϊ������Ҫ��ʼ��
		ThinkConvDao convDao = null;
		
		String mode = Configuration.getString("receiver.mode");
		if ( "none".equalsIgnoreCase(mode) )
		{
			//������շ�����û���� ����zhudong��ȡ��������
			try
			{
				convDao = new ThinkConvDao(false);
				
				if ( dealData != null )
				{
					if ( HQStateCache.isNeedUpdate() )
					{
						logger.info("������Ȩ�ɽ���ϸ��ʼ����-------");
						long t1 = System.currentTimeMillis();
						
						StockOption[] stockOptionArray = HQDataCache.getStockOptionArray();
						
						try
						{
							if ( null != stockOptionArray && stockOptionArray.length > 0 )
							{
								for (int i = 0; i < stockOptionArray.length; i++)
								{
									String stockCode = stockOptionArray[i].getSecurityID();
									String market = "0";
									List deal = (List) dealData.get(stockCode);
									// ��ȡ����
									if ( deal != null )
									{
										List dealInc = convDao.queryDealData(stockCode, market, deal.size());
										deal.addAll(dealInc);
										dealData.put(stockCode, deal);
									}
								}
							}
							else
							{
								logger.warn("������ȨstockOptionArrayΪ�գ�������");
								
							}
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						
						HQDataCache.setData(DEAL_DATA, dealData);
						logger.info("�ɽ���ϸ������ɣ���ʱ��" + (System.currentTimeMillis() - t1));
						
					}
				}
				else
				{
					logger.warn("�ɽ���ϸ����Ϊ�գ�δ������");
				}
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				logger.warn("�ɽ���ϸ���³����쳣��");
			}
			finally
			{
				if ( convDao != null )
				{
					convDao.close();
				}
			}
		}
		*/
    }

    @Override
    public void clear() {
        HQDataCache.cleanData(DEAL_DATA);
    }

}
