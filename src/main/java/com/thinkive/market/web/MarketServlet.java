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
 * @����: ���ڴ���HTTPЭ������
 * @��Ȩ: Copyright (c) 2012
 * @��˾: ˼�ϿƼ�
 * @����: ��֪֮
 * @�汾: 1.0
 * @��������: 2012-4-21
 * @����ʱ��: ����2:56:11
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
	 * ��������������õ�һ��Map
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
	 * ��ʼ������
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
	 * ���ͳɹ���Http��Ӧ
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

			// ������Ӧ��ͷ
			ByteBuffer buffer = ByteBuffer.allocate(16);
			buffer.order(ByteOrder.LITTLE_ENDIAN);
			buffer.putInt(flowNo);
			buffer.putInt(0); // �ɹ�
			buffer.putInt(0); // 0���������� 1��ZLIB��ʽѹ���������(WEB�汾����Ҫѹ��)
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
	 * ����ʧ�ܵ�Http��Ӧ
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

			// ������Ӧ��ͷ
			ByteBuffer buffer = ByteBuffer.allocate(16);
			buffer.order(ByteOrder.LITTLE_ENDIAN);
			buffer.putInt(flowNo);
			buffer.putInt(1); // ʧ��
			buffer.putInt(0); // 0:��������
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
