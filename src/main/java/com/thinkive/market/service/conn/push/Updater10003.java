package com.thinkive.market.service.conn.push;

import com.thinkive.market.bean.MinuteHQ;
import com.thinkive.market.service.cache.HQDataCache;
import com.thinkive.market.service.util.ByteStrHelper;
import com.thinkive.market.service.works.task.MinuteTask;

import org.apache.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @描述: 分时图数据 从转码机接收数据
 * @版权: Copyright (c) 2013
 * @公司: 思迪科技
 * @作者: 熊攀
 * @版本: 1.0
 * @创建日期: 2014-9-7
 * @创建时间: 上午11:15:27
 */
public class Updater10003 extends Updater {
    private static Logger logger = Logger.getLogger(Updater10003.class);

    private static Updater updater;

    private Updater10003() {
    }

    public static Updater getInstance() {
        if (updater == null) {
            updater = new Updater10003();
            updater.setName("Updater10003");
            updater.start();
        }
        return updater;
    }

    /**
     * @描述：个股期权分时图数据更新
     * @作者：熊攀
     * @时间：2015-1-22 下午7:12:20
     */
    @Override
    public void update(byte[] b) {
        ByteBuffer dataBuffer = ByteBuffer.wrap(b);
        dataBuffer.order(ByteOrder.LITTLE_ENDIAN);

        String market = ByteStrHelper.getString(dataBuffer, 2);
        String codeTotal = ByteStrHelper.getString(dataBuffer, 8);// 8个字节

        Map minuteMap = HQDataCache.getData(MinuteTask.MINUTE_DATA);
        if (minuteMap == null) {
            minuteMap = new ConcurrentHashMap();
        }

        String key = market + codeTotal;
        List minHQArray = (List) (minuteMap.get(key));
        if (minHQArray == null) {
            minHQArray = new ArrayList();
        }

        List minuteInc = new ArrayList();
        int count = (b.length - 8) / 30;
        for (int i = 0; i < count; i++) {
            short min = dataBuffer.getShort();
            float now = dataBuffer.getFloat();
            int thedeal = dataBuffer.getInt();
            float avg = dataBuffer.getFloat();
            float lead = dataBuffer.getFloat();

            float yesterday = dataBuffer.getFloat();
            long TotalLongPosition = dataBuffer.getLong();// 当前持仓量
            MinuteHQ minHq = new MinuteHQ();
            minuteInc.add(minHq);
            minHq.setMinute(min); // 分钟数
            minHq.setNow(now); // 现价
            minHq.setAverage(avg); // 均价
            minHq.setThedeal(thedeal); // 成交量
            minHq.setLead(lead);// 叫卖
            minHq.setYesterday(yesterday);
            minHq.setTotalLongPosition(TotalLongPosition);
        }
        minHQArray.addAll(minuteInc);
        minuteMap.put(key, minHQArray);

        HQDataCache.setData(MinuteTask.MINUTE_DATA, minuteMap);
        logger.debug("Updater10003处理包成功，key:" + key + ",size:" + minHQArray.size());
    }
}
