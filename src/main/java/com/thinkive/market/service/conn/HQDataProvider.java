package com.thinkive.market.service.conn;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.thinkive.market.Library;
import com.thinkive.market.service.cache.ConnAddressManager;

public class HQDataProvider
{
	private static Logger	logger	= Logger.getLogger(HQDataProvider.class);
									
	private static String	SOH		= new String("\1");
									
	private static String	STX		= new String("\2");
									
	private SocketConnector	client	= null;
									
	private boolean			isClose;
							
	public HQDataProvider(boolean isClose)
	{
		this.isClose = isClose;
	}
	
	public void setSocketAddress(InetSocketAddress socketAddress) throws IOException
	{
		if ( socketAddress != null )
		{
			client = new SocketConnector(socketAddress);
			client.connect();
		}
	}
	
	//����ת����ĵ�ַ���˿�
	public PackageData sendRequest(Map param) throws Exception
	{
		if ( client == null )
		{
			InetSocketAddress curAddress = ConnAddressManager.getCurrentAddress();
			if ( curAddress == null )
			{
				return null;
			}
			this.setSocketAddress(curAddress);
		}
		PackageData result = null;
		try
		{
			byte[] data = packArray(param);
			result = client.request(data);
		}
		finally
		{
			if ( isClose )
			{
				close();
			}
		}
		return result;
	}
	
	/**
	 * @�������ر�socket����
	 * @���ߣ���֪֮
	 * @ʱ�䣺2012-4-24 ����3:44:26
	 */
	public void close()
	{
		if ( client != null )
		{
			client.close();
		}
	}
	
	protected static byte[] packArray(Map map)
	{
		try
		{
			/*map.remove ������� Դ������д���� �������� key ��ô�÷��� put��ȥ��value  û�оͷ���null*/
			Integer msgType = (Integer) map.remove("funcno");//10003
			
			Integer version = (Integer) map.remove("version");
			if ( msgType == null )
			{
				msgType = 0;
				logger.error("funcno ������nullֵ�����顣param:" + map);
			}
			if ( version == null )
			{
				version = 0;
			}
			
			StringBuffer buffer = new StringBuffer();
			Set keySet = map.keySet();
			for (Iterator iterator = keySet.iterator(); iterator.hasNext();)
			{
				String key = (String) iterator.next();
				buffer.append(key);
				if ( iterator.hasNext() )
				{
					buffer.append(SOH);
				}
			}
			buffer.append(STX);
			for (Iterator iterator = keySet.iterator(); iterator.hasNext();)
			{
				String key = (String) iterator.next();
				buffer.append(String.valueOf(map.get(key)));
				if ( iterator.hasNext() )
				{
					buffer.append(SOH);
				}
			}
			byte[] bodyData;
			bodyData = buffer.toString().getBytes(Library.getEncoding());
			
			ByteBuffer byteBuffer = ByteBuffer.allocate(18 + bodyData.length);
			byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
			byteBuffer.put((byte) 'T').put((byte) 'K');
			byteBuffer.putInt(msgType);
			byteBuffer.putInt(version);
			byteBuffer.putInt(0);
			byteBuffer.putInt(bodyData.length);
			byteBuffer.put(bodyData);
			return byteBuffer.array();
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
