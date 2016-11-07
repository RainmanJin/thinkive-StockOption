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
     * @��������ȡ��ǰϵͳʱ��
     * @���ߣ���֪֮
     * @ʱ�䣺2012-3-29 ����2:00:56
     */
    public static String getSystemDate() {
        Calendar c = Calendar.getInstance();
        String sysDate = DateHelper.formatDate(new Date(c.getTimeInMillis()), "yyyyMMdd");
        return sysDate;
    }

    //

    /**
     * �������ж��ǲ���ͬһ����
     * ���ߣ�yuezz
     * ʱ�䣺2009-10-21 ����01:09:15
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
     * @�������������������Ƿ���ͬһ��/��/��
     * @���ߣ���֪֮
     * @ʱ�䣺2014-1-9 ����3:49:11
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
     * ������Ȩʵʱ���ݵ���ؼ������ݣ�ʵʱ���ݣ�
     */
    public static StockOption updateStockData(StockOption stockOption, BaseInfo baseInfo) {
        if (null != baseInfo) {
            //���������0����ȡ���¼���Ϊ����
            double SettlPrice = stockOption.getSettlPrice();
            if (SettlPrice == 0) {
                stockOption.setSettlPrice(stockOption.getTradePrice());
            }

            stockOption.setUnderlyingSecurityID(baseInfo.getUnderlyingSecurityID());//֤ȯ����
            stockOption.setUnderlyingSymbol(baseInfo.getUnderlyingSymbol());//֤ȯ����

            if (("EBS").equalsIgnoreCase(baseInfo.getUnderlyingType())) {
                //etf
                stockOption.setStockOptionType(60);
            } else if (("ASH").equalsIgnoreCase(baseInfo.getUnderlyingType())) {
                //A��
                stockOption.setStockOptionType(61);
            } else if ("102".equals(baseInfo.getUnderlyingType())) {
                stockOption.setStockOptionType(62);
            }

            stockOption.setListID(baseInfo.getListID());
            stockOption.setLeaveDay(baseInfo.getLeaveDay());
            stockOption.setiPC(baseInfo.getiPC());
            stockOption.setExercisePrice(baseInfo.getExercisePrice());//��Ȩ��
            stockOption.setUp(stockOption.getTradePrice() - stockOption.getPreSettlPrice());//�ǵ�

            if (stockOption.getPreSettlPrice() != 0) {
                stockOption.setUppercent((stockOption.getTradePrice() - stockOption.getPreSettlPrice())
                        / stockOption.getPreSettlPrice());
            }
            stockOption.setExpireDate(baseInfo.getExpireDate());
            stockOption.setContractSymbol(baseInfo.getContractSymbol());//��Ȩ��Լ����

            //����
            if (stockOption.getTradeVolume() > 0) {
                stockOption.setAverage(stockOption.getTotalValueTraded()
                        / (stockOption.getTradeVolume() * stockOption.getiRoundLot()));
            } else {
                stockOption.setAverage(stockOption.getPreSettlPrice());//�ɽ���Ϊ0 ��ȡ����Ϊ���ս����
            }

            stockOption.setCangcha(stockOption.getTotalLongPosition() - baseInfo.getTotalLongPosition());
            stockOption.setContractMultiplierUnit(baseInfo.getContractMultiplierUnit());//��λ

            String TradingPhaseCode = stockOption.getTradingPhaseCode();
            if (!TradingPhaseCode.isEmpty()) {

                String preCode = TradingPhaseCode.substring(0, 1);
                //logger.warn("--------"+preCode);
                if (null != preCode) {
                    if ("S".equalsIgnoreCase(preCode)) {
                        stockOption.setZhuangtai("����ǰ");
                    } else if ("C".equalsIgnoreCase(preCode)) {
                        stockOption.setZhuangtai("���Ͼ���ʱ��");
                    } else if ("T".equalsIgnoreCase(preCode)) {
                        stockOption.setZhuangtai("��������ʱ��");
                    } else if ("B".equalsIgnoreCase(preCode)) {
                        stockOption.setZhuangtai("����ʱ��");
                    } else if ("E".equalsIgnoreCase(preCode)) {
                        stockOption.setZhuangtai("����ʱ��");
                    } else if ("V".equalsIgnoreCase(preCode)) {
                        stockOption.setZhuangtai("�������ж�");
                    } else if ("P".equalsIgnoreCase(preCode)) {
                        stockOption.setZhuangtai("��ʱͣ��");
                    } else {
                        stockOption.setZhuangtai("δ֪״̬");
                    }
                } else {
                    stockOption.setZhuangtai("δ֪״̬");
                }
            }
            String SecurityStatusFlag = baseInfo.getSecurityStatusFlag();
            if (!SecurityStatusFlag.isEmpty()) {
                String preCode1 = SecurityStatusFlag.substring(3, 4);//substring ��ǰ������
                //logger.warn("preCode1---------"+preCode1);
                if (null != preCode1) {
                    if ("0".equalsIgnoreCase(preCode1)) {
                        stockOption.setLeixing("δ����");
                    } else if ("1".equalsIgnoreCase(preCode1)) {
                        stockOption.setLeixing("�ѵ���");
                    } else {
                        stockOption.setLeixing("δ֪����");
                    }
                } else {
                    stockOption.setZhuangtai("δ֪����");
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
     * @����: ���Map��ָ���г���ֵ
     * @����: ���ӿ�
     * @��������: 2016��8��23�� ����2:48:47
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
     * @param tempList  ����õĽ��
     * @param typeArray stktype����
     * @param order     ������  Ĭ��Ϊ1  ����
     * @���������ݷ�ҳȡ������Ȩ
     * @���ߣ�����
     * @ʱ�䣺2013-6-7 ����3:52:37
     */
    public static List<StockOption> getStockPageList(StockOption[] tempList, int[] typeArray, int order, int rowOfPage,
                                                     int curPage, boolean isUnderlying) {
        //�õ�������Ҫ������
        List<StockOption> dataList = new ArrayList<StockOption>();
        //�ų���list
        List<StockOption> otherList = new ArrayList<StockOption>();
        List<String> stockOptionList = new ArrayList<String>();
        if (tempList != null) {
            for (int i = 0; i < tempList.length; i++) {
                StockOption stockOption = tempList[i];
                int stkType = stockOption.getStockOptionType();
                for (int j = 0; j < typeArray.length; j++) {
                    if (stkType == typeArray[j]) {
                        if (isUnderlying) {
                            // -- ��ѯ����б�
                            String a = stockOption.getMarket() + stockOption.getUnderlyingSecurityID();
                            if (!stockOptionList.contains(a)) {
                                stockOptionList.add(a);
                                dataList.add(stockOption);
                            }
                        } else {
                            // -- ��ѯ������Ȩ�б�
                            dataList.add(stockOption);
                        }
                        break;
                    }
                }
            }
            if (order == 0) //������Ҫ��ת
            {
                Collections.reverse(dataList);
            }
            dataList.addAll(otherList);//otherList�����
        }

        List<StockOption> resultList = new ArrayList<StockOption>();

        if (dataList.size() > 0) {
            int counts = dataList.size();

            int totalPages = 0;//��ҳ��
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
     * @param tempList  ����õĽ��
     * @param typeArray stktype����
     * @param order     ������  Ĭ��Ϊ1  ����
     * @param rowOfPage
     * @param curPage
     * @param sort
     * @return
     * @���������ݷ�ҳȡ֤ȯ����
     * @���ߣ�����
     * @ʱ�䣺2013-6-7 ����3:52:37
     */
    /* public static List getStocksPageList(Stock[] tempList, int[] typeArray, int order, int rowOfPage, int curPage,
                                        int sort)
	{
	    //�õ�������Ҫ������
	    List dataList = new ArrayList();
	    //�ų���list
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
	                	//System.out.println("��ծ��ծ������");
	                    //���۸�����Ľ��۸�Ϊ0���߳ɽ���Ϊ0��ֵ�������,�������� ����
	                	//���۸�����Ľ��۸�Ϊ0��ֵ�������,�������� ����
	                    //2 now  4 buyprice1 5 sellprice1 9 open 10 high 11 low 17 ����
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
	
	        if (order == 0) //������Ҫ��ת
	        {
	            Collections.reverse(dataList);
	        }
	        dataList.addAll(otherList);//otherList�����
	    }
	
	    List resultList = new ArrayList();
	
	    if (dataList.size() > 0)
	    {
	        int counts = dataList.size();
	        
	        int totalPages = 0 ;//��ҳ��
	        if( rowOfPage > 0){
	        	totalPages = ((counts % rowOfPage) == 0) ? (counts / rowOfPage) : ((counts / rowOfPage) + 1);
	        }else{
	        	���ÿҳ����������Ϊ0��,Ĭ��Ϊ8
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
     * ����������������������������
     * ���ߣ�yuezz
     * ʱ�䣺2009-10-21 ����01:09:29
     */
    public static KLineData[] getWeekHQDataByDayHQ(KLineData[] dayHQ) {
        if (dayHQ == null || dayHQ.length == 0) {
            return null;
        }

        List weekDays = new ArrayList();
        List oneWeekDays = new ArrayList();

        for (int i = 0; i < dayHQ.length - 1; i++) {
            if (!isSameWeek(dayHQ[i].getDate(), dayHQ[i + 1].getDate())) {//�ж��ǲ�������һ������
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
            weekHQ.setDate(((KLineData) list.get(list.size() - 1)).getDate()); //����
            weekHQ.setOpen(((KLineData) list.get(0)).getOpen());
            weekHQ.setClose(((KLineData) list.get(list.size() - 1)).getClose());
            if (i > 0) {
                weekHQ.setYesClose(weekHQs[i - 1].getClose());
            }
            for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
                KLineData dayHq = (KLineData) iterator.next();
                if (weekHQ.getHigh() < dayHq.getHigh()) {
                    weekHQ.setHigh(dayHq.getHigh());//��߼�
                }
                if (weekHQ.getLow() == 0 || weekHQ.getLow() > dayHq.getLow()) {
                    weekHQ.setLow(dayHq.getLow()); //��ͼ�
                }
                weekHQ.setMoney(weekHQ.getMoney() + dayHq.getMoney()); //�ɽ����
                weekHQ.setVolume(weekHQ.getVolume() + dayHq.getVolume());
            }

            weekHQs[i] = weekHQ;
        }

        return weekHQs;
    }

    /**
     * ������������������������
     * ���ߣ�yuezz
     * ʱ�䣺2009-10-21 ����01:09:48
     */
    public static KLineData[] getMonthHQDataByDayHQ(KLineData[] dayHQ) {
        if (dayHQ == null || dayHQ.length == 0) {
            return null;
        }

        List monthDays = new ArrayList();
        List oneMonthDays = new ArrayList();

        for (int i = 0; i < dayHQ.length - 1; i++) {
            if (!isSameMonth(dayHQ[i].getDate(), dayHQ[i + 1].getDate())) {//�ж��ǲ�������һ������
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
            monthHQ.setDate(((KLineData) list.get(list.size() - 1)).getDate()); //����
            monthHQ.setOpen(((KLineData) list.get(0)).getOpen());
            monthHQ.setClose(((KLineData) list.get(list.size() - 1)).getClose());
            if (i > 0) {
                monthHQ.setYesClose(monthHQs[i - 1].getClose());
            }
            for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
                KLineData dayHq = (KLineData) iterator.next();
                if (monthHQ.getHigh() < dayHq.getHigh()) {
                    monthHQ.setHigh(dayHq.getHigh());//��߼�
                }
                if (monthHQ.getLow() == 0 || monthHQ.getLow() > dayHq.getLow()) {
                    monthHQ.setLow(dayHq.getLow()); //��ͼ�
                }
                monthHQ.setMoney(monthHQ.getMoney() + dayHq.getMoney()); //�ɽ����
                monthHQ.setVolume(monthHQ.getVolume() + dayHq.getVolume());
            }

            monthHQs[i] = monthHQ;
        }

        return monthHQs;
    }

}
