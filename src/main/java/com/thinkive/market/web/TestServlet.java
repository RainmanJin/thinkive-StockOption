package com.thinkive.market.web;

import com.thinkive.base.util.BeanHelper;
import com.thinkive.base.util.StringHelper;
import com.thinkive.market.Library;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import com.thinkive.monitor.base.util.StateMap;

/**
 * @描述: 用于处理HTTP协议请求
 * @版权: Copyright (c) 2012
 * @公司: 思迪科技
 * @作者: 岳知之
 * @版本: 1.0
 * @创建日期: 2012-4-21
 * @创建时间: 下午2:56:11
 */
public class TestServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(TestServlet.class);

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            response.setCharacterEncoding(Library.getEncoding());
            String cache = request.getParameter("cache");
            String methodName = request.getParameter("method");
            String type = request.getParameter("type");
            String key = request.getParameter("key");
            Class c;
            c = Class.forName("com.thinkive.market.service.cache." + cache);
            Map data = null;
            if ("Data".equals(methodName)) {
                if (StringHelper.isEmpty(key)) {
                    Method m = c.getMethod("get" + methodName, new Class[]{String.class});
                    Object[] temp = (Object[]) m.invoke(c, new Object[]{type});
                    Object[] dest = new Object[11];
                    key = "sort";
                    data = new HashMap();
                    System.arraycopy(temp, 0, dest, 0, dest.length / 2);
                    System.arraycopy(temp, temp.length - dest.length / 2, dest, dest.length / 2 + 1, dest.length / 2);
                    data.put(key, dest);
                } else {
                    Method m = c.getMethod("get" + methodName, new Class[]{String.class});
                    data = (Map) m.invoke(c, new Object[]{type});
                }
            } else {
                Method m = c.getMethod("get" + methodName);
                data = (Map) m.invoke(c);

            }

            if (data != null) {
                List resultList = new ArrayList();

                Object result = data.get(key);

                if (result instanceof Object[]) {
                    Object[] objectArr = (Object[]) result;

                    for (int i = 0; i < objectArr.length; i++) {
                        Map map = new HashMap();
                        if (objectArr[i] != null) {
                            BeanHelper.beanToMap(objectArr[i], map);
                        }
                        resultList.add(map);
                    }
                } else if (result instanceof List) {
                    List objectArr = (List) result;

                    for (int i = 0; i < objectArr.size(); i++) {
                        Map map = new HashMap();
                        if (objectArr.get(i) != null) {
                            BeanHelper.beanToMap(objectArr.get(i), map);
                        }
                        resultList.add(map);
                    }
                } else if (result instanceof Map) {
                    resultList.add(result);
                } else if (result instanceof Number) {
                    Map map = new HashMap();
                    map.put("Number", result);
                    resultList.add(map);
                } else if (result instanceof String) {
                    Map map = new HashMap();
                    map.put("String", result);
                    resultList.add(map);
                } else {
                    Map map = new HashMap();
                    BeanHelper.beanToMap(result, map);
                    resultList.add(map);

                }
                PrintWriter writer = response.getWriter();
                writer.print(prepare(resultList));
                writer.flush();
                writer.close();
            }
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private String prepare(List resultList) {
        if (resultList == null || resultList.size() == 0) {
            return "null";
        }
        Map header = null;
        for (Iterator iterator = resultList.iterator(); iterator.hasNext(); ) {
            header = (Map) iterator.next();
            if (header.keySet().size() > 0) {
                break;
            }
        }
        Set keyset = header.keySet();
        String buffer = "<HTML><HEAD><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"> </HEAD> <BODY><TABLE cellspacing=\"1\" bgcolor=\"#bbbbbb\" border=\"0\"  >";
        buffer += "<TR><TD bgcolor=\"#DDDDDD\">索引</TD>";
        for (Iterator iterator = keyset.iterator(); iterator.hasNext(); ) {
            String key = (String) iterator.next();
            buffer += "<TD bgcolor=\"#DDDDDD\">" + key + "</TD>";
        }
        buffer += "</TR>";
        for (int i = 0; i < resultList.size(); i++) {
            Map item = (Map) resultList.get(i);
            buffer += "<TR>  <TD bgcolor=\"#FFFFFF\">" + i + "</TD>";
            for (Iterator iterator = keyset.iterator(); iterator.hasNext(); ) {
                String key = (String) iterator.next();
                buffer += "<TD bgcolor=\"#FFFFFF\">" + item.get(key) + "</TD>";
            }
            buffer += "</TR>";
        }
        buffer += "</TABLE> </BODY></HTML>";
        return buffer;
    }


}
