package com.thinkive.market.service.works.task;

import com.thinkive.base.util.ConvertHelper;
import com.thinkive.base.util.StringHelper;
import com.thinkive.market.bean.StockOption;
import com.thinkive.market.service.cache.HQDataCache;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @����: ���ڹ�Ʊ����
 * @��Ȩ: Copyright (c) 2013
 * @��˾: ˼�ϿƼ�
 * @����: ��֪֮
 * @�汾: 1.0
 * @��������: 2013-11-6
 * @����ʱ��: ����10:36:36
 */
public abstract class BaseSortTask extends BaseConvTask {
    private static Map<String, Map<String, String>> items = new HashMap<String, Map<String, String>>();

    private void readSortConfig() throws DocumentException {
        InputStream is = this.getClass().getResourceAsStream("/sort_stockOption.xml");
        SAXReader reader = new SAXReader();

        Document document = reader.read(is);
        Element systemElement = document.getRootElement();
        List catList = systemElement.elements("data");

        for (Iterator catIter = catList.iterator(); catIter.hasNext(); ) {
            Element catElement = (Element) catIter.next();
            String catName = catElement.attributeValue("type");
            if (StringHelper.isEmpty(catName)) {
                continue;
            }
            List itemList = catElement.elements("param");
            Map<String, String> param = new HashMap<String, String>();
            for (Iterator itemIter = itemList.iterator(); itemIter.hasNext(); ) {
                Element itemElement = (Element) itemIter.next();
                String sort = itemElement.attributeValue("sort");// Y N
                String value = itemElement.attributeValue("value");
                String itemName = itemElement.attributeValue("name");
                if ("Y".equals(sort) && StringHelper.isNotEmpty(value)) {
                    param.put(value, itemName.toLowerCase());// (2,now)
                }
            }
            items.put(catName.toLowerCase(), param);// (sort ,(value,name))
        }
    }

    /**
     * @����������
     * @���ߣ���֪֮
     * @ʱ�䣺2013-4-9 ����6:14:26
     */
    protected void sortList() throws Exception {
        Map<String, String> sortMap = items.get("sort");
        if (sortMap != null) {
            StockOption[] stockOptionArraySort = null;
            for (String key : sortMap.keySet()) {
                stockOptionArraySort = HQDataCache.getData(key);
                if (stockOptionArraySort != null) {
                    int sort = ConvertHelper.strToInt(key);
                    Arrays.sort(stockOptionArraySort, new StockComparator(sort));
                    HQDataCache.setData(key, stockOptionArraySort);
                }
            }
        }
    }

    protected void initSortCache() throws Exception {
        StockOption[] stockOptionArray = HQDataCache.getStockOptionArray();
        if (items.isEmpty()) {
            readSortConfig();
        }
        Map<String, String> sortMap = items.get("sort");
        if (sortMap != null) {
            if (null != stockOptionArray && stockOptionArray.length > 0) {
                StockOption[] stockOptionArraySort = null;
                for (String key : sortMap.keySet()) {
                    stockOptionArraySort = new StockOption[stockOptionArray.length];
                    System.arraycopy(stockOptionArray, 0, stockOptionArraySort, 0, stockOptionArray.length);
                    int sort = ConvertHelper.strToInt(key);
                    Arrays.sort(stockOptionArraySort, new StockComparator(sort));
                    HQDataCache.setData(key, stockOptionArraySort);
                }
            }
        }
    }

    @Override
    public void init(String param) throws Exception {
        initSortCache();
    }

    @Override
    public void clear() {
        Map sortMap = items.get("sort");
        if (sortMap != null) {
            Set keySet = sortMap.keySet();
            for (Iterator iterator = keySet.iterator(); iterator.hasNext(); ) {
                String key = (String) iterator.next();
                HQDataCache.setData(key, null);
            }
        }
    }

    class StockComparator implements Comparator<StockOption> {

        private int sort;

        public StockComparator(int sort) {
            this.sort = sort;
        }

        @Override
        public int compare(StockOption stock1, StockOption stock2) {
            return stock1.getDataBySort(sort).compareTo(stock2.getDataBySort(sort));
        }

    }
}
