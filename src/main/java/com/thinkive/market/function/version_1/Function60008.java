package com.thinkive.market.function.version_1;

import com.thinkive.base.util.ConvertHelper;
import com.thinkive.market.bean.KLineData;
import com.thinkive.market.common.impl.RequestImpl;
import com.thinkive.market.common.impl.ResponseImpl;
import com.thinkive.market.service.cache.HQDataCache;
import com.thinkive.market.service.cache.HQStateCache;
import com.thinkive.market.service.util.MarketUtil;
import com.thinkive.server.Request;
import com.thinkive.server.Response;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @描述: 个股期权历史数据带MA 并附加计算的数据   日线周线月线数据
 * @版权: Copyright (c) 2012
 * @公司: 思迪科技
 * @作者: 熊攀
 * @版本: 1.0
 * @创建日期: 2015-1-18
 * @创建时间: 上午9:29:31
 */
public class Function60008 extends BaseFunction {
    private static Logger logger = Logger.getLogger(Function60008.class);

    @Override
    public void service(Request req, Response resp) {
        RequestImpl request = (RequestImpl) req;
        ResponseImpl response = (ResponseImpl) resp;
        String SecurityID = getStrParameter(request, "SecurityID"); //期权编码   唯一标识
        String market = getMarketParam(request);
        String type = getStrParameter(request, "type", "day"); //k线图类型
        int count = getIntParameter(request, "count"); //获取数量

        Map result = new HashMap();
        try {
            Map<String, KLineData[]> klineMap;
            klineMap = HQDataCache.getData("kline_" + type);
            List list = new ArrayList();
            KLineData[] klineData = null;
            if (null != klineMap && klineMap.size() > 0) {
                klineData = (klineMap.get(market + SecurityID));
            }
            boolean same = false;
            if (klineData != null && klineData.length > 0) {
                if (count == 0 || count > klineData.length) {
                    count = klineData.length - 1;
                }
                //当count<=0说明没有有返回数量， 直接返回头
                if (count > 0) {
                    for (int i = 0; i < count; i++) {
                        KLineData klineItem = klineData[klineData.length - count - 1 + i];
                        if (klineItem != null) {
                            Object item[] = new Object[11];
                            item[0] = klineItem.getDate();
                            item[1] = klineItem.getOpen();
                            item[2] = klineItem.getHigh();
                            item[3] = klineItem.getClose();
                            item[4] = klineItem.getLow();
                            item[5] = klineItem.getVolume();
                            item[6] = klineItem.getMoney();
                            item[7] = klineItem.getMa()[0];
                            item[8] = klineItem.getMa()[1];
                            item[9] = klineItem.getMa()[2];
                            item[10] = klineItem.getMa()[4];

                            list.add(item);
                        }
                        if (i == count - 2) {
                            same = MarketUtil.isSame(klineItem.getDate(),
                                    ConvertHelper.strToInt(HQStateCache.SH.getInitDate()), type);
                        }
                    }
                }
            }
            result.put("errorNo", 0);
            result.put("errorInfo", "");
            result.put("results", list);
            result.put("same", same);
        } catch (Exception e) {
            result.put("errorNo", -600081);
            result.put("errorInfo", e.getMessage());
            logger.info("", e);
        }
        response.write(result);
    }

}
