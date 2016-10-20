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
	//System.out.println("dispathworker1 构造方法");
		setName("ReceiveWorker");
	}

	@Override
	public void run() {
		//System.out.println("dispathworker run");
		while (true) {
			//System.out.println("true true true true true true true ");
			try {
			//System.out.println("下来Receiver!!! ");
				final byte[] data = Receiver.getData();
				/*System.out.println("Receiver的getdata:" + data);
				System.out.println("data.getClass():" + data.getClass());
				System.out.println("data.length:" + data.length);
				System.out.println("data.toString():" + data.toString());*/
				// System.out.println("data:"+data);
				parseData(data);// 解析推送来的数据 update更新
			} catch (Exception e) {
				logger.warn("解析推送数据出现异常", e);
			}
		}
	}

	private void parseData(byte[] b) {
		try {/*
			 * buff = ByteBuffer.wrap("askjfasjkf".getBytes())
			 * 注意：wrap方法是静态函数且只能接收byte类型的数据，任何其他类型的数据想通过这种方式传递，需要进行类型的转换。 b)
			 * buff.put();可以根据数据类型做相应调整，如buff.putChar(chars),buff.putDouble(double)等
			 */
			byte[] data = new byte[b.length];
			// System.out.println("b.length=="+b.length);
			byte[] data1 = new byte[b.length - 16];

			ByteBuffer dataBuffer = ByteBuffer.wrap(b);// 如下,通过包装的方法创建的缓冲区保留了被包装数组内保存的数据.
			dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
			
			int length = dataBuffer.getInt();// 长度
		//System.out.println("长度：" + length);
			int funcno = dataBuffer.getInt();// 功能号
		//System.out.println("功能好：" + funcno);
			/*
			 * 在字节流里读取4个Byte，构成一个Int返回给你咯。从字节流里怎么知道你要的数据类型是什么，
			 * 只能按照你的意愿去解释字节流，你要int，
			 * 那就读4个字节解释成int给你，你要char，那就从字节流里读取2个字节解释成char返回给你，
			 * 你要Byte，你就只读取一个字节返回给你。
			 */
			if (length != data.length) {
				logger.info("包长度出错,length:" + length + "，接收长度:" + data.length);
				return;
			}
			dataBuffer.position(16);// 跳过8个字节长度 保留字
			// System.out.println("dataBuffer.get(data1)::"+dataBuffer.get(data1));//数据区域
			dataBuffer.get(data1);
			/*
			 * if(funcno == 10001){ String code =
			 * ByteStrHelper.getString(dataBuffer, 8);//8个字节 short minute =
			 * dataBuffer.getShort(); float now = dataBuffer.getFloat(); int
			 * thedeal = dataBuffer.getInt(); short bsFlag =
			 * dataBuffer.getShort(); System.out.println("股票代码code="+code);
			 * System.out.println("分钟数minute="+minute);
			 * System.out.println("现价now="+now);
			 * System.out.println("成交量thedeal="+thedeal);
			 * System.out.println("买卖标志bsFlag="+bsFlag); }
			 */
			//股票期权    只需要接收转码机的实时行情和成交明细的推送数据
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
