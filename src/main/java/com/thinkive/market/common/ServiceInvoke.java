package com.thinkive.market.common;

import com.thinkive.server.Function;
import com.thinkive.server.Request;
import com.thinkive.server.Response;

/**
 * ����:
 * ��Ȩ:	 Copyright (c) 2007
 * ��˾:	 ˼�ϿƼ�
 * ����:	 �����
 * �汾:	 1.0
 * ��������: 2009-1-9
 * ����ʱ��: 17:29:53
 */
public class ServiceInvoke
{
    public static void Invoke(Request request, Response response) throws InvokeException
    {
        try
        {
            if ( request.getFuncNo() != 0 )
            {
                String className = "";
                if ( request.getVersionNo() > 0 )
                {
                    String versionDir = Integer.toHexString(request.getVersionNo());
                    className = "com.thinkive.market.function.version_" + versionDir + ".Function"
                            + request.getFuncNo();
                }
                else
                {
                    className = "com.thinkive.market.function.Function" + request.getFuncNo();
                }
                Function functionObj = (Function) (Class.forName(className).newInstance());
                functionObj.service(request, response);
            }
        }
        catch (Exception ex)
        {
            throw new InvokeException("Invoke Function Failed[funcno=" + request.getFuncNo() + "]", ex);
        }
    }
}
