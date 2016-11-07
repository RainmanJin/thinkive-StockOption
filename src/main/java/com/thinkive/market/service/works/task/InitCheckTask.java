package com.thinkive.market.service.works.task;

import com.thinkive.market.bean.MCState;
import com.thinkive.market.service.cache.HQStateCache;
import com.thinkive.market.service.dao.ThinkConvDao;
import com.thinkive.market.service.works.DayWorker;
import com.thinkive.market.service.works.WorkerManager;

import org.apache.log4j.Logger;

import java.util.List;

/**
 * @����: �����Ҫ��ʼ���ļ��϶��󣬲��ѳ�ʼ��ʱ�����ԣ������ǰ����й�Ʊ�Ļ���������ӵ��ڴ��ṩ�������������ʹ�á�
 * @��Ȩ: Copyright (c) 2012
 * @��˾: ˼�ϿƼ�
 * @����: ����
 * @�汾: 1.0
 * @��������: 2015-1-14
 * @����ʱ��: ����1:25:50
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

            //��ǰ����Ϊ��˵��������Ȩ��������һ����������ʼ��ϵͳ
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
            logger.warn("	--	@ϵͳ��ʼ��	-- ��ʼ��ʼ��������Ȩ��������" + market + "���г�����ʼ������Ϊ" + initDate);

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
            logger.warn("	--	@ϵͳ��ʼ��	-- ��ɳ�ʼ��������Ȩ��������" + market + "���г�����ʼ������Ϊ" + initDate);

            stateCache.setInit(true);
        }
    }
}
