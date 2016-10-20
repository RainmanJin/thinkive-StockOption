package com.thinkive.market.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.ajax.JSON;

import com.thinkive.base.util.StringHelper;
import com.thinkive.market.Library;
import com.thinkive.market.common.ServiceInvoke;
import com.thinkive.market.common.impl.RequestImpl;
import com.thinkive.market.common.impl.ResponseImpl;

/**
 * @描述: 用于处理HTTP协议请求
 * @版权: Copyright (c) 2012
 * @公司: 思迪科技
 * @作者: 岳知之
 * @版本: 1.0
 * @创建日期: 2012-4-21
 * @创建时间: 下午2:56:11
 */
public class Json2Servlet extends HttpServlet
{
    private static Logger logger = Logger.getLogger(Json2Servlet.class);
    
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        RequestImpl req = new RequestImpl();
        ResponseImpl resp = new ResponseImpl();
        
        try
        {
            initRequest(req, processRequest(request, response));
            ServiceInvoke.Invoke(req, resp);
            sendSuccessHttpResponse(response, resp.getMap());
        }
        
        catch (Exception ex)
        {
            Map back = new HashMap();
            back.put("errorNo", -999997);
            back.put("errorInfo", ex.getMessage());
            sendSuccessHttpResponse(response, back);
            logger.error("", ex);
        }
        finally
        {
            req.clear();
            resp.clear();
        }
        
    }
    
    private Map processRequest(HttpServletRequest request, HttpServletResponse response)
    {
        Map map = null;
        String data = getRequestData(request);
        if ( StringHelper.isNotEmpty(data) )
        {
            map = (Map) JSON.parse(data);
        }
        else
        {
            map = new HashMap();
            Enumeration e = request.getParameterNames();
            while (e.hasMoreElements())
            {
                String key = (String) e.nextElement();
                
                String paramValues = "";
                try
                {
                    paramValues = java.net.URLDecoder.decode(request.getParameter(key), "UTF-8");
                }
                catch (UnsupportedEncodingException e1)
                {
                    e1.printStackTrace();
                }
                map.put(key, paramValues);
            }
            
        }
        
        return map;
    }
    
    /**
     * @描述：获取request的data数据
     * @作者：岳知之
     * @时间：2013-6-27 下午4:34:03
     * @param request
     * @return
     */
    private String getRequestData(HttpServletRequest request)
    {
        String data = "";
        InputStream in;
        try
        {
            byte[] b = new byte[2048];
            in = request.getInputStream();
            int i = 0;
            while ((b[i] = (byte) in.read()) != -1)
            {
                i++;
            }
            byte[] tmp = new byte[i];
            System.arraycopy(b, 0, tmp, 0, tmp.length);
            data = new String(tmp).trim();
        }
        catch (IOException e2)
        {
            e2.printStackTrace();
        }
        return data;
    }
    
    /**
     * 解析请求参数，得到一个Map
     * 
     * @param paramStr
     * @return
     */
    private Map parseRequestParam(String paramStr) throws UnsupportedEncodingException
    {
        Map params = new HashMap();
        
        String[] itemArray = StringHelper.split(paramStr, "&");
        if ( itemArray != null && itemArray.length > 0 )
        {
            for (int i = 0; i < itemArray.length; i++)
            {
                String item = itemArray[i];
                String[] tempArray = StringHelper.split(item, "=");
                if ( tempArray != null && tempArray.length > 0 )
                {
                    String name = "";
                    String value = "";
                    
                    name = URLDecoder.decode(tempArray[0], "GBK");
                    if ( tempArray.length == 2 )
                    {
                        value = URLDecoder.decode(tempArray[1], "GBK");
                    }
                    params.put(name, value);
                }
            }
        }
        
        return params;
    }
    
    /**
     * 初始化请求
     * 
     * @param request
     * @param paramStr
     * @throws java.io.UnsupportedEncodingException
     */
    private void initRequest(RequestImpl request, String paramStr) throws UnsupportedEncodingException
    {
        Map params = parseRequestParam(paramStr);
        
        int funcNo = Integer.parseInt((String) params.get("funcno"));
        
        request.setFuncNo(funcNo);
        
        for (Iterator iter = params.keySet().iterator(); iter.hasNext();)
        {
            String name = (String) iter.next();
            request.addParameter(name, (String) params.get(name));
        }
    }
    
    /**
     * @描述：初始化请求 根据MAP
     * @作者：岳知之
     * @时间：2013-1-16 下午5:23:28
     * @param request
     * @param params
     * @throws UnsupportedEncodingException
     */
    private void initRequest(RequestImpl request, Map params) throws UnsupportedEncodingException
    {
        
        int funcNo = Integer.parseInt((String) params.get("funcno"));
        int versionNo = Integer.parseInt((String) params.get("version"));
        
        request.setFuncNo(funcNo);
        request.setVersionNo(versionNo);
        for (Iterator iter = params.keySet().iterator(); iter.hasNext();)
        {
            String name = (String) iter.next();
            request.addParameter(name, (String) params.get(name));
        }
    }
    
    /**
     * 发送成功的Http响应
     * 
     * @param httpResponse
     * @param request
     * @param response
     */
    private void sendSuccessHttpResponse(HttpServletResponse httpResponse, Map response)
    {
        try
        {
            httpResponse.setCharacterEncoding(Library.getEncoding());
            PrintWriter writer = httpResponse.getWriter();
            String json = JSON.toString(response);
            writer.print(json);
            writer.flush();
            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
}
