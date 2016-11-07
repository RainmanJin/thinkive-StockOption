package com.thinkive.market.function.version_1;

import com.thinkive.base.util.ConvertHelper;
import com.thinkive.market.bean.StockOption;
import com.thinkive.market.common.impl.RequestImpl;
import com.thinkive.market.common.impl.ResponseImpl;
import com.thinkive.market.service.cache.HQDataCache;
import com.thinkive.server.Request;
import com.thinkive.server.Response;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @描述: 查询个股期权五档盘口数据详情
 * @版权: Copyright (c) 2012
 * @公司: 思迪科技
 * @作者: 熊攀
 * @版本: 1.0
 * @创建日期: 2015-1-16
 * @创建时间: 下午2:14:16
 */
public class Function60005 extends BaseFunction {
    private static Logger logger = Logger.getLogger(Function60005.class);

    @Override
    public void service(Request req, Response resp) {
        RequestImpl request = (RequestImpl) req;
        ResponseImpl response = (ResponseImpl) resp;
        String SecurityID = getStrParameter(request, "SecurityID"); // 合约编码   8位数   唯一标识
        String market = getMarketParam(request);

        String field = getStrParameter(request, "field",
                "34:51:52:53:54:55:56:57:58:59:60:61:62:63:64:65:66:67:68:69:70:12");//出参
        int[] fieldArray = ConvertHelper.strArrayToIntArray(field.split(":"));

        StockOption stockOption = getStockOption(market + SecurityID);

        Map result = new HashMap();
        try {
            List list = new ArrayList();

            if (stockOption != null) {
                Object item[] = new Object[fieldArray.length];
                for (int i = 0; i < fieldArray.length; i++) {
                    item[i] = stockOption.getDataBySort(fieldArray[i]);
                }
                list.add(item);
            } else {
                logger.warn("   --  @五档接口   --  找不到股票：SecurityID:" + SecurityID + ",market:" + market);
            }

            result.put("errorNo", 0);
            result.put("errorInfo", "");
            result.put("results", list);
        } catch (Exception e) {
            result.put("errorNo", -600051);
            result.put("errorInfo", e.getMessage());
            logger.info("", e);
        }

        response.write(result);
    }

    private StockOption getStockOption(String key) {
        Map stockOptionKeyMap = HQDataCache.getStockOptionMap();

        if (stockOptionKeyMap != null) {
            StockOption stockOption = (StockOption) stockOptionKeyMap.get(key);
            return stockOption;
        }
        return null;
    }

}
