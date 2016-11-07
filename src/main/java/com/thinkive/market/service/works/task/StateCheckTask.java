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
 * @描述: 状态检测线程
 * @版权: Copyright (c) 2013
 * @公司: 思迪科技
 * @作者: 熊攀
 * @版本: 1.0
 * @创建日期: 2015-1-14
 * @创建时间: 下午3:13:33
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
            //获取当前服务器dbf文件的时间
            InetSocketAddress curAddress = ConnAddressManager.getCurrentAddress();
            if (curAddress == null) {
                curAddress = addressList.get(0);
                curDbfTime = getDbfTimeByAddress(curAddress);
                if (curDbfTime > 0) {
                    ConnAddressManager.setCurrentAddress(curAddress);
                    logger.warn(curAddress + "为当前连接服务器(转码机地址)!");
                }
            } else {
                curDbfTime = getDbfTimeByAddress(curAddress);
            }
            //轮询找到dbf更新的中心服务器，延迟相差大于1分钟的
            for (Iterator iterator = addressList.iterator(); iterator.hasNext(); ) {
                InetSocketAddress inetSocketAddress = (InetSocketAddress) iterator.next();
                if (inetSocketAddress.equals(curAddress)) {
                    continue;
                }

                long dbfTime = getDbfTimeByAddress(inetSocketAddress);
                if (dbfTime - curDbfTime > 60 * 1000) {
                    ConnAddressManager.setCurrentAddress(inetSocketAddress);
                    logger.warn("转码机服务器切换到" + curAddress + "!");
                    break;
                }
            }
        }

        if (ConnAddressManager.getCurrentAddress() == null) {
            logger.error("没有可连接的转码机服务器，请检查configuration.xml的中心服务器配置[thinkconv.host]");
        }

    }

    /**
     * @描述： 根据网络地址获取dbf文件的时间
     * @作者：岳知之
     * @时间：2012-4-6 下午3:28:48
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
            logger.error(socketAddress + "连接发生异常:", e);
        } finally {
            if (service != null) {
                service.close();
            }
        }
        return dbfTime;
    }

    /**
     * @描述：根据配置的时间获取秒数
     * @作者：岳知之
     * @时间：2012-5-5 上午11:06:31
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
     * 熊攀
     *  检测转码机推送地址和端口
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
            //获取当前服务器dbf文件的时间
            InetSocketAddress curAddress = ConnAddressManager.getPush_currentAddress();
            if (curAddress == null) {
                curAddress = addressList.get(0);
                ConnAddressManager.setPush_currentAddress(curAddress);
                logger.warn(curAddress + "为当前连接服务器(转码机地址及推送端口)!");
            }
        }

        if (ConnAddressManager.getPush_currentAddress() == null) {
            logger.error("没有可连接的转码机推送地址服务，请检查configuration.xml的中心服务器配置[thinkconv.pushhost]");
        }

    }

    @Override
    public void init(String param) {
        /*检测转码机地址拉取端口*/
        checkTCPServer();
        /*检测转码机推送地址和端口*/
        chekPushServer();
    }

    @Override
    public void update() {
		/*检测转码机地址拉取端口*/
        checkTCPServer();
		/*检测转码机推送地址和端口*/
        chekPushServer();
    }

    @Override
    public void clear() {
    }
}
