package com.thinkive.market.service.works.task;

import com.thinkive.market.service.cache.HQStateCache;

import org.apache.log4j.Logger;

/**
 * @描述: 排序
 * @版权: Copyright (c) 2016
 * @公司: Thinkive
 * @作者: 王嵊俊
 * @创建日期: 2016年8月23日 上午9:57:59
 */
public class ActiveSortTask extends BaseSortTask {
    private static Logger logger = Logger.getLogger("ActiveSortTask");

    @Override
    public void update() throws Exception {
        if (!HQStateCache.isNeedUpdate()) {
            return;
        }
        sortList();
    }
}
