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
                parseData(data);// ���������������� update����
            } catch (Exception e) {
                logger.warn("�����������ݳ����쳣", e);
            }
        }
    }

    private void parseData(byte[] b) {
        try {
            byte[] data = new byte[b.length];
            byte[] data1 = new byte[b.length - 16];

            ByteBuffer dataBuffer = ByteBuffer.wrap(b);// ����,ͨ����װ�ķ��������Ļ����������˱���װ�����ڱ��������.
            dataBuffer.order(ByteOrder.LITTLE_ENDIAN);

            int length = dataBuffer.getInt();// ����
            int funcno = dataBuffer.getInt();// ���ܺ�

            if (length != data.length) {
                logger.info("�����ȳ���,length:" + length + "�����ճ���:" + data.length);
                return;
            }
            dataBuffer.position(16);// ����8���ֽڳ��� ������
            dataBuffer.get(data1);

            //��Ʊ��Ȩ    ֻ��Ҫ����ת�����ʵʱ����ͳɽ���ϸ����������
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
