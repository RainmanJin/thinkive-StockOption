package com.thinkive.market.service.works.task;

import com.thinkive.market.bean.BaseInfo;
import com.thinkive.market.bean.StockOption;
import com.thinkive.market.service.cache.HQDataCache;
import com.thinkive.market.service.cache.HQStateCache;
import com.thinkive.market.service.dao.ThinkConvDao;
import com.thinkive.market.service.util.MarketUtil;

import org.apache.log4j.Logger;

import java.util.Map;

/**
 * @����: ���¸�����Ȩʵʱ������Ϣ
 * @��Ȩ: Copyright (c) 2012
 * @��˾: ˼�ϿƼ�
 * @����: ����
 * @�汾: 1.0
 * @��������: 2015-1-14
 * @����ʱ��: ����11:37:35
 */
public class SSHQTask extends BaseConvTask {
    private static Logger logger = Logger.getLogger(SSHQTask.class);


    @Override
    public void init(String param) throws Exception {
        String market = HQStateCache.getInitMarket();
        if (market == null) {
            return;
        }

        ThinkConvDao convDao = null;
        StockOption[] stockOptionArray;
        try {
            convDao = new ThinkConvDao(false);
            stockOptionArray = convDao.queryAllStockList(market);
            if (stockOptionArray != null && stockOptionArray.length > 0) {
                logger.warn("	--	@ϵͳ��ʼ��	--	��" + market + "���г�����ȡʵʱ���������" + stockOptionArray.length);
                if ("SH".equals(market)) {
                    HQDataCache.setStockOptionArray_sh(stockOptionArray);
                } else {
                    HQDataCache.setStockOptionArray_sz(stockOptionArray);
                }

                StockOption shStockOptions[] = HQDataCache.getStockOptionArray_sh();
                StockOption szStockOptions[] = HQDataCache.getStockOptionArray_sz();
                StockOption allStockOptions[] = new StockOption[shStockOptions.length + szStockOptions.length];

                System.arraycopy(shStockOptions, 0, allStockOptions, 0, shStockOptions.length);
                System.arraycopy(szStockOptions, 0, allStockOptions, shStockOptions.length, szStockOptions.length);

                HQDataCache.setStockOptionArray(allStockOptions);

                Map<String, StockOption> stockOptionMap = HQDataCache.getStockOptionMap();
                for (int i = 0; i < stockOptionArray.length; i++) {
                    Map<String, BaseInfo> baseInfoMap = HQDataCache.getData(BaseInfoTask.BASE_INFO);

                    String key = stockOptionArray[i].getMarket() + stockOptionArray[i].getSecurityID();//��Լ����
                    //���»�������
                    if (baseInfoMap != null) {
                        BaseInfo baseInfo = (BaseInfo) baseInfoMap.get(key);
                        stockOptionArray[i].setBaseInfo(baseInfo);
                        MarketUtil.updateStockData(stockOptionArray[i], baseInfo);
                    }
                    stockOptionMap.put(key, stockOptionArray[i]);
                }
                HQDataCache.setUpdateTime(System.currentTimeMillis());
            } else {
                logger.warn("	--	@ϵͳ��ʼ��	--	��" + market + "���г�����ȡʵʱ���������0");
            }
        } finally {
            if (convDao != null) {
                convDao.close();
            }
        }
    }

    @Override
    public void update() throws Exception {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clear() {
        MarketUtil.cleanMapAtInit(HQDataCache.getStockOptionMap());

        String initMarket = HQStateCache.getInitMarket();

        if ("SH".equals(initMarket)) {
            HQDataCache.setStockOptionArray(HQDataCache.getStockOptionArray_sz());
        } else if ("SZ".equals(initMarket)) {
            HQDataCache.setStockOptionArray(HQDataCache.getStockOptionArray_sh());
        }
    }
}
