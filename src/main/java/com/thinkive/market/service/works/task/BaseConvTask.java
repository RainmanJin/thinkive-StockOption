package com.thinkive.market.service.works.task;

import com.thinkive.market.service.works.ITask;

public abstract class BaseConvTask implements ITask
{
	protected static String	SOH	= new String(new byte[] { 1 });
								
	protected static String	STX	= new String(new byte[] { 2 });
								
}
