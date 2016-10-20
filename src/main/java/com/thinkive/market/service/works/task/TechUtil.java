package com.thinkive.market.service.works.task;

import com.thinkive.market.bean.KLineData;

public class TechUtil
{
    
    /**
     * @������
     * @���ߣ���֪֮
     * @ʱ�䣺2013-11-9 ����5:36:41
     * @param klineData
     * @param n ��������
     * @param k KlineData ma[]�е�����
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
