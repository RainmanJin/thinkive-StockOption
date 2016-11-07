package com.thinkive.market.service.works.task;

import com.thinkive.base.config.Configuration;
import com.thinkive.base.util.ConvertHelper;
import com.thinkive.market.bean.MCState;
import com.thinkive.market.service.cache.ConnAddressManager;
import com.thinkive.market.service.dao.ThinkConvDao;
import com.thinkive.market.service.works.ITask;

import org.apache.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @����: ״̬����߳�
 * @��Ȩ: Copyright (c) 2013
 * @��˾: ˼�ϿƼ�
 * @����: ����
 * @�汾: 1.0
 * @��������: 2015-1-14
 * @����ʱ��: ����3:13:33
 */
public class StateCheckTask implements ITask {
    private static Logger logger = Logger.getLogger(StateCheckTask.class);

    private void checkTCPServer() {
        List<InetSocketAddress> addressList = ConnAddressManager.getAddressList();

        if (addressList == null || addressList.size() == 0) {
            String[] addressStrs = Configuration.getString("thinkconv.host").split("\\|");
            if (addressStrs != null) {
                addressList = new ArrayList<InetSocketAddress>();
                for (int i = 0; i < addressStrs.length; i++) {
                    String[] addressPort = addressStrs[i].split(":");
                    if (addressPort.length == 2) {
                        String address = addressPort[0];
                        int port = ConvertHelper.strToInt(addressPort[1]);
                        addressList.add(new InetSocketAddress(address, port));
                    }
                }
                ConnAddressManager.setAddressList(addressList);
            }
        }
        if (addressList != null && addressList.size() > 0) {
            long curDbfTime = 0;
            //��ȡ��ǰ������dbf�ļ���ʱ��
            InetSocketAddress curAddress = ConnAddressManager.getCurrentAddress();
            if (curAddress == null) {
                curAddress = addressList.get(0);
                curDbfTime = getDbfTimeByAddress(curAddress);
                if (curDbfTime > 0) {
                    ConnAddressManager.setCurrentAddress(curAddress);
                    logger.warn(curAddress + "Ϊ��ǰ���ӷ�����(ת�����ַ)!");
                }
            } else {
                curDbfTime = getDbfTimeByAddress(curAddress);
            }
            //��ѯ�ҵ�dbf���µ����ķ��������ӳ�������1���ӵ�
            for (Iterator iterator = addressList.iterator(); iterator.hasNext(); ) {
                InetSocketAddress inetSocketAddress = (InetSocketAddress) iterator.next();
                if (inetSocketAddress.equals(curAddress)) {
                    continue;
                }

                long dbfTime = getDbfTimeByAddress(inetSocketAddress);
                if (dbfTime - curDbfTime > 60 * 1000) {
                    ConnAddressManager.setCurrentAddress(inetSocketAddress);
                    logger.warn("ת����������л���" + curAddress + "!");
                    break;
                }
            }
        }

        if (ConnAddressManager.getCurrentAddress() == null) {
            logger.error("û�п����ӵ�ת���������������configuration.xml�����ķ���������[thinkconv.host]");
        }

    }

    /**
     * @������ ���������ַ��ȡdbf�ļ���ʱ��
     * @���ߣ���֪֮
     * @ʱ�䣺2012-4-6 ����3:28:48
     */
    private long getDbfTimeByAddress(InetSocketAddress socketAddress) {
        MCState state = null;
        long dbfTime = 0;
        ThinkConvDao service = null;
        try {
            //ThinkConvDao
            service = new ThinkConvDao();
            state = service.queryMCState(socketAddress);
            if (state != null) {
                dbfTime = state.getDbfTime_sh();
            }
        } catch (Exception e) {
            logger.error(socketAddress + "���ӷ����쳣:", e);
        } finally {
            if (service != null) {
                service.close();
            }
        }
        return dbfTime;
    }

    /**
     * @�������������õ�ʱ���ȡ����
     * @���ߣ���֪֮
     * @ʱ�䣺2012-5-5 ����11:06:31
     */
    private int getSecoundsByStr(String time) {
        int seconds = 0;
        if (time != null) {
            String[] timeParts = time.split(":");
            if (timeParts.length == 2) {
                int hours = Integer.parseInt(timeParts[0]);
                int minute = Integer.parseInt(timeParts[1]);
                seconds = hours * 60 * 60 + minute * 60;
            } else if (timeParts.length == 3) {
                int hours = Integer.parseInt(timeParts[0]);
                int minute = Integer.parseInt(timeParts[1]);
                int second = Integer.parseInt(timeParts[2]);
                seconds = hours * 60 * 60 + minute * 60 + second;
            }
        }
        return seconds;
    }

    /*
     * ����
     *  ���ת������͵�ַ�Ͷ˿�
     * */
    private void chekPushServer() {
        List<InetSocketAddress> addressList = ConnAddressManager.getPush_addressList();

        if (addressList == null || addressList.size() == 0) {
            String[] addressStrs = Configuration.getString("thinkconv.pushhost").split("\\|");
            if (addressStrs != null) {
                addressList = new ArrayList<InetSocketAddress>();
                for (int i = 0; i < addressStrs.length; i++) {
                    String[] addressPort = addressStrs[i].split(":");
                    if (addressPort.length == 2) {
                        String address = addressPort[0];
                        int port = ConvertHelper.strToInt(addressPort[1]);
                        addressList.add(new InetSocketAddress(address, port));
                    }
                }
                ConnAddressManager.setPush_addressList(addressList);
            }
        }

        if (addressList != null && addressList.size() > 0) {
            long curDbfTime = 0;
            //��ȡ��ǰ������dbf�ļ���ʱ��
            InetSocketAddress curAddress = ConnAddressManager.getPush_currentAddress();
            if (curAddress == null) {
                curAddress = addressList.get(0);
                ConnAddressManager.setPush_currentAddress(curAddress);
                logger.warn(curAddress + "Ϊ��ǰ���ӷ�����(ת�����ַ�����Ͷ˿�)!");
            }
        }

        if (ConnAddressManager.getPush_currentAddress() == null) {
            logger.error("û�п����ӵ�ת������͵�ַ��������configuration.xml�����ķ���������[thinkconv.pushhost]");
        }

    }

    @Override
    public void init(String param) {
        /*���ת�����ַ��ȡ�˿�*/
        checkTCPServer();
        /*���ת������͵�ַ�Ͷ˿�*/
        chekPushServer();
    }

    @Override
    public void update() {
		/*���ת�����ַ��ȡ�˿�*/
        checkTCPServer();
		/*���ת������͵�ַ�Ͷ˿�*/
        chekPushServer();
    }

    @Override
    public void clear() {
    }
}
