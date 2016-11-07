package com.thinkive.market.service.util;

import com.thinkive.base.util.DateHelper;
import com.thinkive.market.bean.BaseInfo;
import com.thinkive.market.bean.KLineData;
import com.thinkive.market.bean.StockOption;
import com.thinkive.market.service.cache.HQStateCache;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MarketUtil {
    private static Logger logger = Logger.getLogger(MarketUtil.class);

    /**
     * @描述：获取当前系统时间
     * @作者：岳知之
     * @时间：2012-3-29 下午2:00:56
     */
    public static String getSystemDate() {
        Calendar c = Calendar.getInstance();
        String sysDate = DateHelper.formatDate(new Date(c.getTimeInMillis()), "yyyyMMdd");
        return sysDate;
    }

    //

    /**
     * 描述：判断是不是同一个周
     * 作者：yuezz
     * 时间：2009-10-21 下午01:09:15
     */
    public static boolean isSameWeek(int date1, int date2) {
        int y1 = date1 / 10000;
        int m1 = date1 / 100 % 100 - 1;
        int d1 = date1 % 100;
        int y2 = date2 / 10000;
        int m2 = date2 / 100 % 100 - 1;
        int d2 = date2 % 100;

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.set(y1, m1, d1);
        cal2.set(y2, m2, d2);
        return cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR);
    }

    public static boolean isSameMonth(int date1, int date2) {
        return date1 / 100 == date2 / 100;
    }

    public static boolean isSameDay(int date1, int date2) {
        return date1 == date2;
    }

    /**
     * @param type day,week,month
     * @描述：返回两个日期是否是同一天/周/月
     * @作者：岳知之
     * @时间：2014-1-9 下午3:49:11
     */
    public static boolean isSame(int date1, int date2, String type) {
        boolean isSame = false;
        if ("day".equals(type)) {
            isSame = MarketUtil.isSameDay(date1, date2);

        } else if ("week".equals(type)) {
            isSame = MarketUtil.isSameWeek(date1, date2);

        } else if ("month".equals(type)) {
            isSame = MarketUtil.isSameMonth(date1, date2);
        }
        return isSame;
    }

    /**
     * 更新期权实时数据的相关计算数据（实时数据）
     */
    public static StockOption updateStockData(StockOption stockOption, BaseInfo baseInfo) {
        if (null != baseInfo) {
            //结算如果是0，就取最新价作为结算
            double SettlPrice = stockOption.getSettlPrice();
            if (SettlPrice == 0) {
                stockOption.setSettlPrice(stockOption.getTradePrice());
            }

            stockOption.setUnderlyingSecurityID(baseInfo.getUnderlyingSecurityID());//证券代码
            stockOption.setUnderlyingSymbol(baseInfo.getUnderlyingSymbol());//证券名称

            if (("EBS").equalsIgnoreCase(baseInfo.getUnderlyingType())) {
                //etf
                stockOption.setStockOptionType(60);
            } else if (("ASH").equalsIgnoreCase(baseInfo.getUnderlyingType())) {
                //A股
                stockOption.setStockOptionType(61);
            } else if ("102".equals(baseInfo.getUnderlyingType())) {
                stockOption.setStockOptionType(62);
            }

            stockOption.setListID(baseInfo.getListID());
            stockOption.setLeaveDay(baseInfo.getLeaveDay());
            stockOption.setiPC(baseInfo.getiPC());
            stockOption.setExercisePrice(baseInfo.getExercisePrice());//行权价
            stockOption.setUp(stockOption.getTradePrice() - stockOption.getPreSettlPrice());//涨跌

            if (stockOption.getPreSettlPrice() != 0) {
                stockOption.setUppercent((stockOption.getTradePrice() - stockOption.getPreSettlPrice())
                        / stockOption.getPreSettlPrice());
            }
            stockOption.setExpireDate(baseInfo.getExpireDate());
            stockOption.setContractSymbol(baseInfo.getContractSymbol());//期权合约名称

            //均价
            if (stockOption.getTradeVolume() > 0) {
                stockOption.setAverage(stockOption.getTotalValueTraded()
                        / (stockOption.getTradeVolume() * stockOption.getiRoundLot()));
            } else {
                stockOption.setAverage(stockOption.getPreSettlPrice());//成交量为0 就取均价为昨日结算价
            }

            stockOption.setCangcha(stockOption.getTotalLongPosition() - baseInfo.getTotalLongPosition());
            stockOption.setContractMultiplierUnit(baseInfo.getContractMultiplierUnit());//单位

            String TradingPhaseCode = stockOption.getTradingPhaseCode();
            if (!TradingPhaseCode.isEmpty()) {

                String preCode = TradingPhaseCode.substring(0, 1);
                //logger.warn("--------"+preCode);
                if (null != preCode) {
                    if ("S".equalsIgnoreCase(preCode)) {
                        stockOption.setZhuangtai("开市前");
                    } else if ("C".equalsIgnoreCase(preCode)) {
                        stockOption.setZhuangtai("集合竞价时段");
                    } else if ("T".equalsIgnoreCase(preCode)) {
                        stockOption.setZhuangtai("连续交易时段");
                    } else if ("B".equalsIgnoreCase(preCode)) {
                        stockOption.setZhuangtai("休市时段");
                    } else if ("E".equalsIgnoreCase(preCode)) {
                        stockOption.setZhuangtai("闭市时段");
                    } else if ("V".equalsIgnoreCase(preCode)) {
                        stockOption.setZhuangtai("波动性中断");
                    } else if ("P".equalsIgnoreCase(preCode)) {
                        stockOption.setZhuangtai("临时停牌");
                    } else {
                        stockOption.setZhuangtai("未知状态");
                    }
                } else {
                    stockOption.setZhuangtai("未知状态");
                }
            }
            String SecurityStatusFlag = baseInfo.getSecurityStatusFlag();
            if (!SecurityStatusFlag.isEmpty()) {
                String preCode1 = SecurityStatusFlag.substring(3, 4);//substring 包前不包后
                //logger.warn("preCode1---------"+preCode1);
                if (null != preCode1) {
                    if ("0".equalsIgnoreCase(preCode1)) {
                        stockOption.setLeixing("未调整");
                    } else if ("1".equalsIgnoreCase(preCode1)) {
                        stockOption.setLeixing("已调整");
                    } else {
                        stockOption.setLeixing("未知类型");
                    }
                } else {
                    stockOption.setZhuangtai("未知类型");
                }
            }
        }
        return stockOption;
    }

    public static void cleanMapAtInit(Map<String, ?> map) {
        if (!HQStateCache.SH.isInit()) {
            cleanMapByMarket(map, "SH");
        }

        if (!HQStateCache.SZ.isInit()) {
            cleanMapByMarket(map, "SZ");
        }
    }

    /**
     * @描述: 清空Map中指定市场的值
     * @作者: 王嵊俊
     * @创建日期: 2016年8月23日 下午2:48:47
     */
    public static void cleanMapByMarket(Map<String, ?> map, String market) {
        if (map == null) {
            return;
        }

        Iterator<String> iterator = map.keySet().iterator();
        for (; iterator.hasNext(); ) {
            String key = iterator.next();
            if (key.startsWith(market)) {
                iterator.remove();
            }
        }
    }

    /**
     * @param tempList  排序好的结果
     * @param typeArray stktype集合
     * @param order     正反序  默认为1  升序
     * @描述：根据分页取个股期权
     * @作者：熊攀
     * @时间：2013-6-7 下午3:52:37
     */
    public static List<StockOption> getStockPageList(StockOption[] tempList, int[] typeArray, int order, int rowOfPage,
                                                     int curPage, boolean isUnderlying) {
        //得到具体需要的数据
        List<StockOption> dataList = new ArrayList<StockOption>();
        //排除的list
        List<StockOption> otherList = new ArrayList<StockOption>();
        List<String> stockOptionList = new ArrayList<String>();
        if (tempList != null) {
            for (int i = 0; i < tempList.length; i++) {
                StockOption stockOption = tempList[i];
                int stkType = stockOption.getStockOptionType();
                for (int j = 0; j < typeArray.length; j++) {
                    if (stkType == typeArray[j]) {
                        if (isUnderlying) {
                            // -- 查询标的列表
                            String a = stockOption.getMarket() + stockOption.getUnderlyingSecurityID();
                            if (!stockOptionList.contains(a)) {
                                stockOptionList.add(a);
                                dataList.add(stockOption);
                            }
                        } else {
                            // -- 查询个股期权列表
                            dataList.add(stockOption);
                        }
                        break;
                    }
                }
            }
            if (order == 0) //降序，需要反转
            {
                Collections.reverse(dataList);
            }
            dataList.addAll(otherList);//otherList放最后
        }

        List<StockOption> resultList = new ArrayList<StockOption>();

        if (dataList.size() > 0) {
            int counts = dataList.size();

            int totalPages = 0;//总页数
            if (rowOfPage > 0) {
                totalPages = ((counts % rowOfPage) == 0) ? (counts / rowOfPage) : ((counts / rowOfPage) + 1);
            } else {
                rowOfPage = 8;
                totalPages = ((counts % rowOfPage) == 0) ? (counts / rowOfPage) : ((counts / rowOfPage) + 1);
            }
            curPage = (curPage < 1) ? 1 : curPage;
            curPage = (curPage > totalPages) ? totalPages : curPage;

            int pos = rowOfPage * (curPage - 1);

            for (int i = 0; i < rowOfPage; i++) {
                int curPos = pos + i;
                if (curPos < dataList.size()) {
                    resultList.add(dataList.get(curPos));
                }
            }
        } else {
            return dataList;
        }
        dataList.clear();
        return resultList;
    }

    /**
     * @param tempList  排序好的结果
     * @param typeArray stktype集合
     * @param order     正反序  默认为1  升序
     * @param rowOfPage
     * @param curPage
     * @param sort
     * @return
     * @描述：根据分页取证券代码
     * @作者：熊攀
     * @时间：2013-6-7 下午3:52:37
     */
    /* public static List getStocksPageList(Stock[] tempList, int[] typeArray, int order, int rowOfPage, int curPage,
                                        int sort)
	{
	    //得到具体需要的数据
	    List dataList = new ArrayList();
	    //排除的list
	    List otherList = new ArrayList();
	    if (tempList != null)
	    {
	        for (int i = 0; i < tempList.length; i++)
	        {
	            Stock stock = tempList[i];
	            int stkType = stock.getStktype();
	           // System.out.println(stkType);
	            Comparable c = stock.getDataBySort(sort);
	            for (int j = 0; j < typeArray.length; j++)
	            {
	            	
	            	if(typeArray[j] ==21){
	            		if(stkType == )
	            	}
	            	
	            	
	                if (stkType == typeArray[j])
	                	
	                {
	                	//System.out.println("国债国债！！！");
	                    //按价格排序的将价格为0或者成交量为0的值放在最后,不管正序 倒序
	                	//按价格排序的将价格为0的值放在最后,不管正序 倒序
	                    //2 now  4 buyprice1 5 sellprice1 9 open 10 high 11 low 17 均价
	                    if (sort != 2 && sort != 4 && sort != 5 && sort != 6 && sort != 9 && sort != 10 && sort != 11
	                            && sort != 17 && sort != 14)
	                    {
	                        dataList.add(stock);
	                    }
	                    else
	                    {
	                        if (c.compareTo(0.0F) == 0)
	                        {
	                            otherList.add(stock);
	                        }
	                        else
	                        {
	                            dataList.add(stock);
	                        }
	                    }
	                }
	            }
	        }
	
	        if (order == 0) //降序，需要反转
	        {
	            Collections.reverse(dataList);
	        }
	        dataList.addAll(otherList);//otherList放最后
	    }
	
	    List resultList = new ArrayList();
	
	    if (dataList.size() > 0)
	    {
	        int counts = dataList.size();
	        
	        int totalPages = 0 ;//总页数
	        if( rowOfPage > 0){
	        	totalPages = ((counts % rowOfPage) == 0) ? (counts / rowOfPage) : ((counts / rowOfPage) + 1);
	        }else{
	        	如果每页条数不传（为0）,默认为8
	        	rowOfPage = 8;
	        	totalPages = ((counts % rowOfPage) == 0) ? (counts / rowOfPage) : ((counts / rowOfPage) + 1);
	        }
	       // int totalPages = ((counts % rowOfPage) == 0) ? (counts / rowOfPage) : ((counts / rowOfPage) + 1);
	        curPage = (curPage < 1) ? 1 : curPage;
	        curPage = (curPage > totalPages) ? totalPages : curPage;
	
	        int pos = rowOfPage * (curPage - 1);
	
	        for (int i = 0; i < rowOfPage; i++)
	        {
	            int curPos = pos + i;
	            if (curPos < dataList.size())
	            {
	                resultList.add(dataList.get(curPos));
	            }
	        }
	    }else{
	    	return dataList;
	    }
	    dataList.clear();
	    return resultList;
	}
	
	
	
	*/

    /**
     * 描述：根据日线数据算周线数据
     * 作者：yuezz
     * 时间：2009-10-21 下午01:09:29
     */
    public static KLineData[] getWeekHQDataByDayHQ(KLineData[] dayHQ) {
        if (dayHQ == null || dayHQ.length == 0) {
            return null;
        }

        List weekDays = new ArrayList();
        List oneWeekDays = new ArrayList();

        for (int i = 0; i < dayHQ.length - 1; i++) {
            if (!isSameWeek(dayHQ[i].getDate(), dayHQ[i + 1].getDate())) {//判断是不是属于一个周内
                oneWeekDays.add(dayHQ[i]);
                weekDays.add(oneWeekDays);
                oneWeekDays = new ArrayList();
            } else {
                oneWeekDays.add(dayHQ[i]);
            }
        }
        oneWeekDays.add(dayHQ[dayHQ.length - 1]);
        weekDays.add(oneWeekDays);

        KLineData[] weekHQs = new KLineData[weekDays.size()];
        for (int i = 0; i < weekDays.size(); i++) {
            List list = (List) weekDays.get(i);
            if (list.size() == 0) {
                continue;
            }

            KLineData weekHQ = new KLineData();
            weekHQ.setDate(((KLineData) list.get(list.size() - 1)).getDate()); //日期
            weekHQ.setOpen(((KLineData) list.get(0)).getOpen());
            weekHQ.setClose(((KLineData) list.get(list.size() - 1)).getClose());
            if (i > 0) {
                weekHQ.setYesClose(weekHQs[i - 1].getClose());
            }
            for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
                KLineData dayHq = (KLineData) iterator.next();
                if (weekHQ.getHigh() < dayHq.getHigh()) {
                    weekHQ.setHigh(dayHq.getHigh());//最高价
                }
                if (weekHQ.getLow() == 0 || weekHQ.getLow() > dayHq.getLow()) {
                    weekHQ.setLow(dayHq.getLow()); //最低价
                }
                weekHQ.setMoney(weekHQ.getMoney() + dayHq.getMoney()); //成交金额
                weekHQ.setVolume(weekHQ.getVolume() + dayHq.getVolume());
            }

            weekHQs[i] = weekHQ;
        }

        return weekHQs;
    }

    /**
     * 描述：根据日线数据算月线
     * 作者：yuezz
     * 时间：2009-10-21 下午01:09:48
     */
    public static KLineData[] getMonthHQDataByDayHQ(KLineData[] dayHQ) {
        if (dayHQ == null || dayHQ.length == 0) {
            return null;
        }

        List monthDays = new ArrayList();
        List oneMonthDays = new ArrayList();

        for (int i = 0; i < dayHQ.length - 1; i++) {
            if (!isSameMonth(dayHQ[i].getDate(), dayHQ[i + 1].getDate())) {//判断是不是属于一个月内
                oneMonthDays.add(dayHQ[i]);
                monthDays.add(oneMonthDays);
                oneMonthDays = new ArrayList();
            } else {
                oneMonthDays.add(dayHQ[i]);
            }
        }
        oneMonthDays.add(dayHQ[dayHQ.length - 1]);
        monthDays.add(oneMonthDays);

        KLineData[] monthHQs = new KLineData[monthDays.size()];
        for (int i = 0; i < monthDays.size(); i++) {
            List list = (List) monthDays.get(i);
            if (list.size() == 0) {
                continue;
            }

            KLineData monthHQ = new KLineData();
            monthHQ.setDate(((KLineData) list.get(list.size() - 1)).getDate()); //日期
            monthHQ.setOpen(((KLineData) list.get(0)).getOpen());
            monthHQ.setClose(((KLineData) list.get(list.size() - 1)).getClose());
            if (i > 0) {
                monthHQ.setYesClose(monthHQs[i - 1].getClose());
            }
            for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
                KLineData dayHq = (KLineData) iterator.next();
                if (monthHQ.getHigh() < dayHq.getHigh()) {
                    monthHQ.setHigh(dayHq.getHigh());//最高价
                }
                if (monthHQ.getLow() == 0 || monthHQ.getLow() > dayHq.getLow()) {
                    monthHQ.setLow(dayHq.getLow()); //最低价
                }
                monthHQ.setMoney(monthHQ.getMoney() + dayHq.getMoney()); //成交金额
                monthHQ.setVolume(monthHQ.getVolume() + dayHq.getVolume());
            }

            monthHQs[i] = monthHQ;
        }

        return monthHQs;
    }

}
