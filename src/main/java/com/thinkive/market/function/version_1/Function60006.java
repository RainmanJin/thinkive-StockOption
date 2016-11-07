package com.thinkive.market.function.version_1;

import com.thinkive.market.bean.MinuteHQ;
import com.thinkive.market.common.impl.RequestImpl;
import com.thinkive.market.common.impl.ResponseImpl;
import com.thinkive.market.service.cache.HQDataCache;
import com.thinkive.market.service.cache.HQStateCache;
import com.thinkive.market.service.works.task.MinuteTask;
import com.thinkive.server.Request;
import com.thinkive.server.Response;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @描述: 个股期权分时图数据
 * @版权: Copyright (c) 2012
 * @公司: 思迪科技
 * @作者: 熊攀
 * @版本: 1.0
 * @创建日期: 2015-1-18
 * @创建时间: 上午9:29:31
 */
public class Function60006 extends BaseFunction {
    private static Logger logger = Logger.getLogger(Function60006.class);

    @Override
    public void service(Request req, Response resp) {
        RequestImpl request = (RequestImpl) req;
        ResponseImpl response = (ResponseImpl) resp;
        String SecurityID = getStrParameter(request, "SecurityID"); //期权编码   唯一标识
        String market = getMarketParam(request);
        int start = getIntParameter(request, "start", 0); //数据点起始位置

        Map result = new HashMap();
        try {
            List list = new ArrayList();

            Map minuteMap;
            minuteMap = HQDataCache.getData(MinuteTask.MINUTE_DATA);

            List minHQArray = null;
            if (null != minuteMap && minuteMap.size() > 0) {

                minHQArray = (List) (minuteMap.get(market + SecurityID));
            }
            if (minHQArray != null && minHQArray.size() > 0) {
                for (int i = start; i < minHQArray.size(); i++) {
                    MinuteHQ minHQ = (MinuteHQ) minHQArray.get(i);
                    Object item[] = new Object[6];
                    if (minHQ != null) {
                        item[0] = minHQ.getMinute();
                        item[1] = minHQ.getNow();
                        item[2] = minHQ.getAverage();
                        item[3] = minHQ.getThedeal();
                        item[4] = minHQ.getYesterday();
                        item[5] = minHQ.getTotalLongPosition();
                        list.add(item);
                    }
                }
            }
            result.put("results", list);
            result.put("errorNo", 0);
            result.put("errorInfo", "");
            result.put("date", HQStateCache.SH.getInitDate());
        } catch (Exception e) {
            result.put("errorNo", -600061);
            result.put("errorInfo", e.getMessage());
            logger.info("", e);
        }
        response.write(result);
    }

}
