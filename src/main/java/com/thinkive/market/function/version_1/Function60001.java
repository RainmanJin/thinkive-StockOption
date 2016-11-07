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
 * @����: ��ȡ������Ȩ����б����ơ��г������룩���ɷ�ҳ
 * @��Ȩ: Copyright (c) 2012
 * @��˾: ˼�ϿƼ�
 * @����: ����
 * @�汾: 1.0
 * @��������: 2015-1-14
 * @����ʱ��: ����2:14:16
 */
public class Function60001 extends BaseFunction {
    private static Logger logger = Logger.getLogger(Function60001.class);

    @Override
    public void service(Request req, Response resp) {
        RequestImpl request = (RequestImpl) req;
        ResponseImpl response = (ResponseImpl) resp;

        int sort = getIntParameter(request, "sort"); // �����ֶ�
        int order = getIntParameter(request, "order"); // ����˳�� 0:����1������
        int rowOfPage = getIntParameter(request, "rowOfPage"); // һҳ�ж�������¼
        int curPage = getIntParameter(request, "curPage"); // ��ǰ�ǵڼ�ҳ,��1��ʼ
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
