package com.thinkive.market.service.cache;

import java.net.InetSocketAddress;
import java.util.List;

public class ConnAddressManager
{
	/**
	 * 中心服务器地址列表（转码机连接地址及端口）
	 */
	private static List<InetSocketAddress>	addressList;
											
	/**
	 * 当前连接的服务器地址（转码机连接地址及端口）
	 */
	private static InetSocketAddress		currentAddress;
											
	/**
	 * 中心服务器地址列表（转码机推送地址和端口）
	 */
	private static List<InetSocketAddress>	push_addressList;
											
	/**
	 * 当前连接的服务器地址（转码机推送地址和端口）
	 */
	private static InetSocketAddress		push_currentAddress;
											
	public static List<InetSocketAddress> getAddressList()
	{
		return addressList;
	}
	
	public static InetSocketAddress getCurrentAddress()
	{
		return currentAddress;
	}
	
	public static List<InetSocketAddress> getPush_addressList()
	{
		return push_addressList;
	}
	
	public static InetSocketAddress getPush_currentAddress()
	{
		return push_currentAddress;
	}
	
	public static void setAddressList(List<InetSocketAddress> addressList)
	{
		ConnAddressManager.addressList = addressList;
	}
	
	public static void setCurrentAddress(InetSocketAddress currentAddress)
	{
		ConnAddressManager.currentAddress = currentAddress;
	}
	
	public static void setPush_addressList(List<InetSocketAddress> push_addressList)
	{
		ConnAddressManager.push_addressList = push_addressList;
	}
	
	public static void setPush_currentAddress(InetSocketAddress push_currentAddress)
	{
		ConnAddressManager.push_currentAddress = push_currentAddress;
	}
}
