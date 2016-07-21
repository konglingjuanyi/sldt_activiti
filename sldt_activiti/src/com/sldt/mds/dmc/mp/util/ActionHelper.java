
package com.sldt.mds.dmc.mp.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;


/**
 * Command Action类辅助，例如帮助输出、获取输入等
 * @author fbchen
 * @version 2009-02-04
 */
public class ActionHelper {
	static Logger log = Logger.getLogger(ActionHelper.class);
	public static final String XML_CONTENTTYPE  = "text/xml; charset=UTF-8";
	public static final String HTML_CONTENTTYPE = "text/html; charset=UTF-8";

	public ActionHelper() {
		super();
	}

	
	/**
	 * 输出内容到response。可以自行指定输出的CONTENT-TYPE。
	 * @param response HttpServletResponse
	 * @param content 内容，如Json,XML数据
	 * @throws IOException 网络异常
	 */
	public static void output(HttpServletResponse response, String content) throws IOException {
		response.getWriter().print(content);
		response.getWriter().flush();
	}
	
	/**
	 * 输出Json内容到response
	 * @param response HttpServletResponse
	 * @param json JSON格式数据
	 * @throws IOException 网络异常
	 */
	public static void outputJsonObject(HttpServletResponse response,
			JSONObject json) throws IOException {
		response.setContentType(HTML_CONTENTTYPE);
		output(response, json.toString());
	}
	
	/**
	 * 输出Json内容到response
	 * @param response HttpServletResponse
	 * @param json JSON格式数据
	 * @throws IOException 网络异常
	 */
	public static void outputJsonArray(HttpServletResponse response,
			JSONArray json) throws IOException {
		response.setContentType(HTML_CONTENTTYPE);
		output(response, json.toString());
	}
	
	
	/**
	 * 直接取所有的request参数
	 * @param request HTTP请求
	 */
	public static Page buildPage(HttpServletRequest request) {
		Page pag = page(request);
		Enumeration<?> en = request.getParameterNames();
		while (en.hasMoreElements()) {
			String name = (String) en.nextElement();
			pag.setParameter(name, request.getParameter(name));
		}
		return pag;
	}

	/**
	 * 获取request参数中的分页参数。<br>
	 * limit-每页的最大记录数；start-开始的记录位置
	 * @param request HTTP请求
	 * @return Page 包含分页参数
	 */
	public static Page page(HttpServletRequest request) {
		Page pag = new Page();
		pag.setStartIndex(NumberUtils.toInt(request.getParameter("start"), 0));
		pag.setPageSize(NumberUtils.toInt(request.getParameter("limit"), Page.getConfigPageSize()));
		return pag;
	}


	/**
	 * 将request中的参数获取到一个Map中并返回。<br>
	 * @param request HTTP请求
	 * @return 所有参数
	 */
	public static Map<String,Object> bindmap(HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		Enumeration<?> names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			map.put(name, request.getParameter(name));
		}
		return map;
	}
	
		
	/**
	 * 下载数据/文件
	 * @param response HTTP输出
	 * @param file 文件
	 * @param contentType ContentType in HTTP Header
	 * @throws IOException IO异常
	 */
	public static void download(HttpServletResponse response, File file,
			String contentType) throws IOException {
		FileInputStream in = new FileInputStream(file);
		download(response, in, contentType,
				new String(file.getName().getBytes("GBK"),"ISO8859_1"));
	}
	
	/**
	 * 下载数据/文件
	 * @param response HTTP输出
	 * @param inputStream 文件流
	 * @param contentType ContentType in HTTP Header
	 * @param fileName 文件名
	 * @throws IOException IO异常
	 */
	public static void download(HttpServletResponse response, InputStream inputStream,
			String contentType, String fileName) throws IOException {
		response.setContentType(StringUtils.isEmpty(contentType) ?
				"application/octet-stream" : contentType);
		response.setHeader("Content-Disposition", "attachment;filename="+fileName);
		response.setStatus(HttpServletResponse.SC_OK); //避免超时
		BufferedInputStream reader = null;
		try {
			reader = new BufferedInputStream(inputStream);
			OutputStream output = response.getOutputStream();
			int b = -1;
			int buff = 0;
			while ((b=reader.read()) != -1) {
				output.write(b);
				buff++;
				if (buff > 10240) {
					buff = 0;
					output.flush();
				}
			}
			output.flush();
		} finally {
			if (reader != null) { reader.close();}
			if (inputStream != null) { inputStream.close();}
		}
	}
	
}
