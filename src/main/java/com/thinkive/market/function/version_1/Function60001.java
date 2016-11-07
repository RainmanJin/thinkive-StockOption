package com.thinkive.market.function.version_1;

import com.thinkive.base.util.ConvertHelper;
import com.thinkive.market.bean.StockOption;
import com.thinkive.market.common.impl.RequestImpl;
import com.thinkive.market.common.impl.ResponseImpl;
import com.thinkive.market.service.cache.HQDataCache;
import com.thinkive.market.service.util.MarketUtil;
import com.thinkive.server.Request;
import com.thinkive.server.Response;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @描述: 获取个股期权标的列表（名称、市场、代码），可分页
 * @版权: Copyright (c) 2012
 * @公司: 思迪科技
 * @作者: 熊攀
 * @版本: 1.0
 * @创建日期: 2015-1-14
 * @创建时间: 下午2:14:16
 */
public class Function60001 extends BaseFunction {
    private static Logger logger = Logger.getLogger(Function60001.class);

    @Override
    public void service(Request req, Response resp) {
        RequestImpl request = (RequestImpl) req;
        ResponseImpl response = (ResponseImpl) resp;

        int sort = getIntParameter(request, "sort"); // 排序字段
        int order = getIntParameter(request, "order"); // 排序顺序 0:降序，1：升序
        int rowOfPage = getIntParameter(request, "rowOfPage"); // 一页有多少条记录
        int curPage = getIntParameter(request, "curPage"); // 当前是第几页,从1开始
        String type = getStrParameter(request, "type");
        String field = getStrParameter(request, "field", "22:24:2:10:11:9:12:14:6:23:21:3:1");
        boolean isUnderlying = getIntParameter(request, "isUnderlying", 0) != 0;

        int[] typeArray = ConvertHelper.strArrayToIntArray(type.split(":"));
        int[] fieldArray = ConvertHelper.strArrayToIntArray(field.split(":"));

        String sortStr = String.valueOf(sort);
        StockOption[] tempList = HQDataCache.getData(sortStr);

        List<StockOption> stockList = MarketUtil.getStockPageList(tempList, typeArray, order, rowOfPage, curPage, isUnderlying);

        Map<String, Object> result = new HashMap<String, Object>();
        try {
            List<Object[]> list = new ArrayList<Object[]>();

            for (StockOption stock : stockList) {
                if (stock != null) {
                    Object item[] = new Object[fieldArray.length];
                    for (int i = 0; i < fieldArray.length; i++) {
                        item[i] = stock.getDataBySort(fieldArray[i]);
                    }
                    list.add(item);
                }
            }
            result.put("errorNo", 0);
            result.put("errorInfo", "");
            result.put("results", list);
        } catch (Exception e) {
            result.put("errorNo", -600011);
            result.put("errorInfo", e.getMessage());
            logger.info("", e);
        }
        response.write(result);
    }
}
