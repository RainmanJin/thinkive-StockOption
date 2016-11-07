package com.thinkive.market.service.works.task;

import com.thinkive.market.bean.MCState;
import com.thinkive.market.service.cache.HQStateCache;
import com.thinkive.market.service.dao.ThinkConvDao;
import com.thinkive.market.service.works.DayWorker;
import com.thinkive.market.service.works.WorkerManager;

import org.apache.log4j.Logger;

import java.util.List;

/**
 * @描述: 清空需要初始化的集合对象，并把初始化时间做对，最后就是把所有股票的基础数据添加到内存提供给其他插件服务使用。
 * @版权: Copyright (c) 2012
 * @公司: 思迪科技
 * @作者: 熊攀
 * @版本: 1.0
 * @创建日期: 2015-1-14
 * @创建时间: 下午1:25:50
 */
public class InitCheckTask extends BaseConvTask {
    private static Logger logger = Logger.getLogger(InitCheckTask.class);

    public static void main(String[] args) {
        ThinkConvDao service = null;
        try {
            service = new ThinkConvDao();
            MCState state = service.queryMCState();
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    @Override
    public void init(String param) {

    }

    @Override
    public void update() {
        ThinkConvDao service = null;
        try {
            service = new ThinkConvDao();
            MCState state = service.queryMCState();

            //当前日期为空说明个股期权服务器第一次启动，初始化系统
            if (state != null) {
                String convDate = state.getCurHqDate();
                String initDate_sh = state.getInitDate_sh();
                String initDate_sz = state.getInitDate_sz();

                long dbfTime_sh = state.getDbfTime_sh();
                long dbfTime_sz = state.getDbfTime_sz();

                initSystem("SH", initDate_sh);
                initSystem("SZ", initDate_sz);

                HQStateCache.setConvDate(convDate);
                HQStateCache.SH.setDbftime(dbfTime_sh);
                HQStateCache.SZ.setDbftime(dbfTime_sz);
            }
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (service != null) {
                service.close();
            }
        }
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub

    }

    private void initSystem(String market, String initDate) throws Exception {
        if (initDate == null) {
            return;
        }

        HQStateCache stateCache = HQStateCache.valueOf(market);
        if (stateCache.getInitDate() == null || !stateCache.getInitDate().equals(initDate)) {
            stateCache.setInit(false);
            logger.warn("	--	@系统初始化	-- 开始初始化个股期权服务器【" + market + "】市场，初始化日期为" + initDate);

            List<DayWorker> workers = WorkerManager.getInstance().getAllWorkerList();
            for (DayWorker worker : workers) {
                worker.clear();
            }

            for (final DayWorker dayWorker : workers) {
                if (dayWorker.getType() == 1) {
                    dayWorker.init();
                } else if (dayWorker.getType() == 2) {
                    new Thread("Worker-init-" + dayWorker.getId()) {
                        public void run() {
                            dayWorker.init();
                        }
                    }.start();
                }
            }
            stateCache.setInitDate(initDate);
            logger.warn("	--	@系统初始化	-- 完成初始化个股期权服务器【" + market + "】市场，初始化日期为" + initDate);

            stateCache.setInit(true);
        }
    }
}
