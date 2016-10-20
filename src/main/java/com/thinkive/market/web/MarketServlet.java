package com.thinkive.market.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.thinkive.base.util.StringHelper;
import com.thinkive.market.common.InvokeException;
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
public class MarketServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(MarketServlet.class);

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		RequestImpl req = new RequestImpl();
		ResponseImpl resp = new ResponseImpl();

		try {
			String paramStr = request.getQueryString();

			initRequest(req, paramStr);
			ServiceInvoke.Invoke(req, resp);
			sendSuccessHttpResponse(response, req, resp);

		} catch (InvokeException ex) {
			logger.error("", ex);
			sendFailHttpResponse(response, req, resp);
		} catch (Exception ex) {
			logger.error("", ex);
			sendFailHttpResponse(response, req, resp);
		} finally {
			req.clear();
			resp.clear();
		}

	}

	/**
	 * 解析请求参数，得到一个Map
	 * 
	 * @param paramStr
	 * @return
	 */
	private Map parseRequestParam(String paramStr)
			throws UnsupportedEncodingException {
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
	 * 
	 * @param request
	 * @param paramStr
	 * @throws java.io.UnsupportedEncodingException
	 */
	private void initRequest(RequestImpl request, String paramStr)
			throws UnsupportedEncodingException {
		Map params = parseRequestParam(paramStr);

		int funcNo = Integer.parseInt((String) params.get("funcno"));
		int flowNo = Integer.parseInt((String) params.get("flowno"));

		request.setFuncNo(funcNo);
		request.setFlowNo(flowNo);

		for (Iterator iter = params.keySet().iterator(); iter.hasNext();) {
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
	private void sendSuccessHttpResponse(HttpServletResponse httpResponse,
			RequestImpl request, ResponseImpl response) {
		try {
		    httpResponse.setContentType("application/octet-stream");
			int flowNo = request.getFlowNo();
			byte[] data = response.getData();

			// 构造响应包头
			ByteBuffer buffer = ByteBuffer.allocate(16);
			buffer.order(ByteOrder.LITTLE_ENDIAN);
			buffer.putInt(flowNo);
			buffer.putInt(0); // 成功
			buffer.putInt(0); // 0：正常数据 1：ZLIB方式压缩后的数据(WEB版本不需要压缩)
			buffer.putInt(data.length);
			httpResponse.setContentLength(data.length + 16);
			httpResponse.getOutputStream().write(buffer.array());
			if (data.length > 0) {
				httpResponse.getOutputStream().write(data);
			}
			httpResponse.getOutputStream().flush();
			buffer.clear();
		} catch (Exception ex) {
			logger.error("", ex);
		}
	}

	/**
	 * @param session
	 * @param request
	 * @param response
	 */
	/**
	 * 发送失败的Http响应
	 * 
	 * @param httpResponse
	 * @param request
	 * @param response
	 */
	private void sendFailHttpResponse(HttpServletResponse httpResponse,
			RequestImpl request, ResponseImpl response) {
		try {
		    httpResponse.setContentType("application/octet-stream");
			int flowNo = request.getFlowNo();

			// 构造响应包头
			ByteBuffer buffer = ByteBuffer.allocate(16);
			buffer.order(ByteOrder.LITTLE_ENDIAN);
			buffer.putInt(flowNo);
			buffer.putInt(1); // 失败
			buffer.putInt(0); // 0:正常数据
			buffer.putInt(0);

			httpResponse.setContentLength(16);
			httpResponse.getOutputStream().write(buffer.array());
			httpResponse.getOutputStream().flush();
			buffer.clear();
		} catch (Exception ex) {
			logger.error("", ex);
		}
	}

}
