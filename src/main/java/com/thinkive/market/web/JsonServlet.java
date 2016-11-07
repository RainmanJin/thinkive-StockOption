package com.thinkive.market.web;

import com.thinkive.base.util.StringHelper;
import com.thinkive.market.Library;
import com.thinkive.market.common.ServiceInvoke;
import com.thinkive.market.common.impl.RequestImpl;
import com.thinkive.market.common.impl.ResponseImpl;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.ajax.JSON;

import java.io.IOException;
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

/**
 * @描述: 用于处理HTTP协议请求
 * @版权: Copyright (c) 2012
 * @公司: 思迪科技
 * @作者: 岳知之
 * @版本: 1.0
 * @创建日期: 2012-4-21
 * @创建时间: 下午2:56:11
 */
public class JsonServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(JsonServlet.class);

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestImpl req = new RequestImpl();
        ResponseImpl resp = new ResponseImpl();

        try {
            initRequest(req, processRequest(request, response));
            ServiceInvoke.Invoke(req, resp);
            sendSuccessHttpResponse(response, resp.getMap());
        } catch (Exception ex) {
            Map back = new HashMap();
            back.put("errorNo", -999997);
            back.put("errorInfo", ex.getMessage());
            sendSuccessHttpResponse(response, back);
            logger.error("", ex);
        } finally {
            req.clear();
            resp.clear();
        }

    }

    private Map processRequest(HttpServletRequest request, HttpServletResponse response) {
        Map map = new HashMap();
        Enumeration e = request.getParameterNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();

            String paramValues = "";
            try {
                paramValues = java.net.URLDecoder.decode(request.getParameter(key), "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            map.put(key, paramValues);
        }
        return map;
    }

    /**
     * 解析请求参数，得到一个Map
     */
    private Map parseRequestParam(String paramStr) throws UnsupportedEncodingException {
        Map params = new HashMap();

        String[] itemArray = StringHelper.split(paramStr, "&");
        if (itemArray != null && itemArray.length > 0) {
            for (int i = 0; i < itemArray.length; i++) {
                String item = itemArray[i];
                String[] tempArray = StringHelper.split(item, "=");
                if (tempArray != null && tempArray.length > 0) {
                    String name = "";
                    String value = "";

                    name = URLDecoder.decode(tempArray[0], "GBK");
                    if (tempArray.length == 2) {
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
     */
    private void initRequest(RequestImpl request, String paramStr) throws UnsupportedEncodingException {
        Map params = parseRequestParam(paramStr);

        int funcNo = Integer.parseInt((String) params.get("funcno"));

        request.setFuncNo(funcNo);

        for (Iterator iter = params.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            request.addParameter(name, (String) params.get(name));
        }
    }

    /**
     * @描述：初始化请求 根据MAP
     * @作者：岳知之
     * @时间：2013-1-16 下午5:23:28
     */
    private void initRequest(RequestImpl request, Map params) throws UnsupportedEncodingException {
        /*xiongpan*/
        int funcNo = 0;
        String funcnoString = (String) params.get("funcno");
        if (null != funcnoString && funcnoString.trim().length() > 0) {
            funcNo = Integer.parseInt(funcnoString);
        }
        int versionNo = 0;
        String versionNoString = (String) params.get("version");
        if (null != versionNoString && versionNoString.trim().length() > 0) {
            versionNo = Integer.parseInt(versionNoString);
        }
        //int funcNo = Integer.parseInt(funcnoString);
        //int versionNo = Integer.parseInt((String) params.get("version"));

        request.setFuncNo(funcNo);
        request.setVersionNo(versionNo);
        for (Iterator iter = params.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            request.addParameter(name, (String) params.get(name));
        }
    }

    /**
     * 发送成功的Http响应
     */
    private void sendSuccessHttpResponse(HttpServletResponse httpResponse, Map response) {
        try {
            httpResponse.setCharacterEncoding(Library.getEncoding());
            httpResponse.setContentType("application/json;charset=UTF-8");
            //httpResponse.setContentType("text/html;charset=UTF-8");
            PrintWriter writer = httpResponse.getWriter();
            String json = JSON.toString(response);
            writer.print(json);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            logger.error("", e);
        }
    }

}
