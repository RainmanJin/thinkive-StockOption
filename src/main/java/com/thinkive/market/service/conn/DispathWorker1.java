package com.thinkive.market.service.conn;

import com.thinkive.market.service.conn.push.Updater;

import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DispathWorker1 extends Thread {
    private static Logger logger = Logger.getLogger(DispathWorker1.class);

    public DispathWorker1() {
        setName("ReceiveWorker");
    }

    @Override
    public void run() {
        while (true) {
            try {
                final byte[] data = Receiver.getData();
                parseData(data);// 解析推送来的数据 update更新
            } catch (Exception e) {
                logger.warn("解析推送数据出现异常", e);
            }
        }
    }

    private void parseData(byte[] b) {
        try {
            byte[] data = new byte[b.length];
            byte[] data1 = new byte[b.length - 16];

            ByteBuffer dataBuffer = ByteBuffer.wrap(b);// 如下,通过包装的方法创建的缓冲区保留了被包装数组内保存的数据.
            dataBuffer.order(ByteOrder.LITTLE_ENDIAN);

            int length = dataBuffer.getInt();// 长度
            int funcno = dataBuffer.getInt();// 功能号

            if (length != data.length) {
                logger.info("包长度出错,length:" + length + "，接收长度:" + data.length);
                return;
            }
            dataBuffer.position(16);// 跳过8个字节长度 保留字
            dataBuffer.get(data1);

            //股票期权    只需要接收转码机的实时行情和成交明细的推送数据
            if (funcno == 10000 || funcno == 10001 || funcno == 10003) {
                String className = "com.thinkive.market.service.conn.push.Updater" + funcno;
                Class c = Class.forName(className);
                Method m = c.getMethod("getInstance");
                Updater updater = (Updater) m.invoke(c);

                updater.add(data1);
                dataBuffer.clear();
            }
        } catch (IllegalAccessException e) {
            logger.warn(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            logger.warn(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            logger.warn(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            logger.warn(e.getMessage(), e);
        } catch (SecurityException e) {
            logger.warn(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            logger.warn(e.getMessage(), e);
        }
    }
}
