package com.thinkive.market.service.works.task;

import com.thinkive.market.bean.KLineData;

public class TechUtil
{
    
    /**
     * @描述：
     * @作者：岳知之
     * @时间：2013-11-9 下午5:36:41
     * @param klineData
     * @param n 均线周期
     * @param k KlineData ma[]中的索引
     */
    public static void MA(KLineData[] klineData, int n, int k)
    {
        
        if ( klineData == null )
        {
            return;
        }
        for (int i = n - 1; i < klineData.length; i++)
        {
            int sumn = 0;
            for (int p = 0; p < n; p++)
            {
                sumn += klineData[i - p].getClose();
            }
            int ma = Math.round(sumn * 1.0f / n);
            klineData[i].getMa()[k] = ma;
        }
    }
}
