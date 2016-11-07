package com.thinkive.market.service.cache;

import com.thinkive.market.bean.StockOption;
import com.thinkive.market.service.util.MarketUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HQDataCache {
    /**
     * 个股列表
     */
    private static StockOption[] StockOptionArray = new StockOption[0];

    private static StockOption[] StockOptionArray_sh = new StockOption[0];

    private static StockOption[] StockOptionArray_sz = new StockOption[0];

    /**
     * 实时行情更新时间戳
     */
    private static long updateTime = 0;
    /**
     * 个股
     */
    private static Map<String, StockOption> stockOptionMap = new ConcurrentHashMap<String, StockOption>();
    private static Map<String, Object> hqdata = new HashMap<String, Object>();

    public static long getUpdateTime() {
        return updateTime;
    }

    public static void setUpdateTime(long updateTime) {
        HQDataCache.updateTime = updateTime;
    }

    public static StockOption[] getStockOptionArray() {
        return StockOptionArray;
    }

    public static void setStockOptionArray(StockOption[] StockOptionArray) {
        HQDataCache.StockOptionArray = StockOptionArray;
    }

    public static Map<String, StockOption> getStockOptionMap() {
        return stockOptionMap;
    }

    public static void setStockOptionMap(Map<String, StockOption> stockOptionMap) {
        HQDataCache.stockOptionMap = stockOptionMap;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getData(String key) {
        return (T) hqdata.get(key);
    }

    public static void setData(String key, Object data) {
        hqdata.put(key, data);
    }

    public static void clear() {
        StockOptionArray = new StockOption[0];
        stockOptionMap.clear();
        hqdata.clear();
    }

    public static StockOption[] getStockOptionArray_sh() {
        return StockOptionArray_sh;
    }

    public static void setStockOptionArray_sh(StockOption[] stockOptionArray_sh) {
        StockOptionArray_sh = stockOptionArray_sh;
    }

    public static StockOption[] getStockOptionArray_sz() {
        return StockOptionArray_sz;
    }

    public static void setStockOptionArray_sz(StockOption[] stockOptionArray_sz) {
        StockOptionArray_sz = stockOptionArray_sz;
    }

    public static void cleanData(String key) {
        Map<String, ?> map = getData(key);
        MarketUtil.cleanMapAtInit(map);
    }

}
