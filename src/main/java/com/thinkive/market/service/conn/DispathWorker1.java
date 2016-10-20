package com.thinkive.market.service.conn;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.log4j.Logger;

import com.thinkive.market.service.conn.push.Updater;

public class DispathWorker1 extends Thread {
	private static Logger logger = Logger.getLogger(DispathWorker1.class);

	public DispathWorker1() {
	//System.out.println("dispathworker1 ���췽��");
		setName("ReceiveWorker");
	}

	@Override
	public void run() {
		//System.out.println("dispathworker run");
		while (true) {
			//System.out.println("true true true true true true true ");
			try {
			//System.out.println("����Receiver!!! ");
				final byte[] data = Receiver.getData();
				/*System.out.println("Receiver��getdata:" + data);
				System.out.println("data.getClass():" + data.getClass());
				System.out.println("data.length:" + data.length);
				System.out.println("data.toString():" + data.toString());*/
				// System.out.println("data:"+data);
				parseData(data);// ���������������� update����
			} catch (Exception e) {
				logger.warn("�����������ݳ����쳣", e);
			}
		}
	}

	private void parseData(byte[] b) {
		try {/*
			 * buff = ByteBuffer.wrap("askjfasjkf".getBytes())
			 * ע�⣺wrap�����Ǿ�̬������ֻ�ܽ���byte���͵����ݣ��κ��������͵�������ͨ�����ַ�ʽ���ݣ���Ҫ�������͵�ת���� b)
			 * buff.put();���Ը���������������Ӧ��������buff.putChar(chars),buff.putDouble(double)��
			 */
			byte[] data = new byte[b.length];
			// System.out.println("b.length=="+b.length);
			byte[] data1 = new byte[b.length - 16];

			ByteBuffer dataBuffer = ByteBuffer.wrap(b);// ����,ͨ����װ�ķ��������Ļ����������˱���װ�����ڱ��������.
			dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
			
			int length = dataBuffer.getInt();// ����
		//System.out.println("���ȣ�" + length);
			int funcno = dataBuffer.getInt();// ���ܺ�
		//System.out.println("���ܺã�" + funcno);
			/*
			 * ���ֽ������ȡ4��Byte������һ��Int���ظ��㿩�����ֽ�������ô֪����Ҫ������������ʲô��
			 * ֻ�ܰ��������Ըȥ�����ֽ�������Ҫint��
			 * �ǾͶ�4���ֽڽ��ͳ�int���㣬��Ҫchar���Ǿʹ��ֽ������ȡ2���ֽڽ��ͳ�char���ظ��㣬
			 * ��ҪByte�����ֻ��ȡһ���ֽڷ��ظ��㡣
			 */
			if (length != data.length) {
				logger.info("�����ȳ���,length:" + length + "�����ճ���:" + data.length);
				return;
			}
			dataBuffer.position(16);// ����8���ֽڳ��� ������
			// System.out.println("dataBuffer.get(data1)::"+dataBuffer.get(data1));//��������
			dataBuffer.get(data1);
			/*
			 * if(funcno == 10001){ String code =
			 * ByteStrHelper.getString(dataBuffer, 8);//8���ֽ� short minute =
			 * dataBuffer.getShort(); float now = dataBuffer.getFloat(); int
			 * thedeal = dataBuffer.getInt(); short bsFlag =
			 * dataBuffer.getShort(); System.out.println("��Ʊ����code="+code);
			 * System.out.println("������minute="+minute);
			 * System.out.println("�ּ�now="+now);
			 * System.out.println("�ɽ���thedeal="+thedeal);
			 * System.out.println("������־bsFlag="+bsFlag); }
			 */
			//��Ʊ��Ȩ    ֻ��Ҫ����ת�����ʵʱ����ͳɽ���ϸ����������
			if(funcno == 10000 || funcno == 10001){
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
