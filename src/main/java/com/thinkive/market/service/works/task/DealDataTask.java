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
 * @描述: 更新个股期权成交明细
 * @版权: Copyright (c) 2012
 * @公司: 思迪科技
 * @作者: 熊攀
 * @版本: 1.0
 * @创建日期: 2015-1-17
 * @创建时间: 下午12:05:10
 */
public class DealDataTask extends BaseConvTask {
    public static final String DEAL_DATA = "dealdata";
    private static Logger logger = Logger.getLogger("DealDataTask");

    //测试从转码机拉取的成交明细数据
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
                logger.warn("个股期权stockOptionArray为空！！！！");
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
		// 如果为空则需要初始化
		ThinkConvDao convDao = null;
		
		String mode = Configuration.getString("receiver.mode");
		if ( "none".equalsIgnoreCase(mode) )
		{
			//如果接收服务器没启动 ，则zhudong拉取更新数据
			try
			{
				convDao = new ThinkConvDao(false);
				
				if ( dealData != null )
				{
					if ( HQStateCache.isNeedUpdate() )
					{
						logger.info("个股期权成交明细开始更新-------");
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
									// 获取增量
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
								logger.warn("个股期权stockOptionArray为空！！！！");
								
							}
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						
						HQDataCache.setData(DEAL_DATA, dealData);
						logger.info("成交明细更新完成，耗时：" + (System.currentTimeMillis() - t1));
						
					}
				}
				else
				{
					logger.warn("成交明细数据为空，未做更新");
				}
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				logger.warn("成交明细更新出现异常！");
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
