
package com.sldt.mds.dmc.mp.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Page信息类
 */
public class Page implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 全部显示时每页最多显示的记录数
	 */
	public static int DEFAULT_MAX_RESULT = 2000;

	protected int totalRecs;   // 总记录数
	
	protected int totalPages;	// 总页数

	protected int pageSize;	// 每页记录数

	protected int startIndex; //开始记录Index，从0开始

	protected int currPage;	// 当前页数，从1开始
	
	protected int currRecords;	// 当前页记录数

	protected List<Integer> pageSizeList; // 每页记录数的下拉
	
	protected Map<String,Object> parameters =  new HashMap<String,Object>(); //查询参数
	
	protected List<?> queryResult; //查询结果
	
	//constructor
	public Page() {
		this.pageSize = 15;//getConfigPageSize();
	}
	
	//constructor
	public Page(int currPage) {
		this();
		this.currPage = currPage;
	}
	
	//constructor
	public Page(int currPage, int pageSize) {
		this.currPage = currPage;
		this.pageSize = pageSize;
	}
	
	//constructor
	public Page(int currPage, boolean selectAll) {
		this.currPage = currPage;
		if (selectAll) {
			this.pageSize = DEFAULT_MAX_RESULT;
		}
	}

	/**
	 * 刷新总记录数、当前页数
	 */
	public Page refreshPageNum() {
		int pages = ((totalRecs%pageSize)==0 ? totalRecs/pageSize : (totalRecs/pageSize)+1);
		totalPages = pages;
		if(totalRecs>0)
			currPage = (startIndex <= 1) ? 1 : ((startIndex+pageSize)/pageSize);
		return this;
	}
	
	/**
	 * 重置为第一页参数
	 */
	public Page reset() {
		this.startIndex = 0;
		this.currPage = 1;
		this.totalRecs = this.totalPages = 0;
		return this;
	}
	
	/**
	 * 重置为下页参数
	 */
	public Page next() {
		this.startIndex = this.startIndex + this.pageSize;
		this.currPage = this.currPage + 1;
		return this;
	}
	
	/**
	 * 获取配置的每页记录数。默认为15
	 * @return int
	 */
	public static int getConfigPageSize() {
		//return CodeParamManager.getIntValue("System-Option-Value","PAGE_SIZE", 15);
		return 15;
	}
	
	
	public int getCurrPage() {
		return currPage;
	}

	public Page setCurrPage(int currPage) {
		this.currPage = currPage;
		return this;
	}

	public int getPageSize() {
		return pageSize;
	}

	public Page setPageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	public List<Integer> getPageSizeList() {
		return pageSizeList;
	}

	public Page setPageSizeList(List<Integer> pageSizeList) {
		this.pageSizeList = pageSizeList;
		return this;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public Page setTotalPages(int totalPages) {
		this.totalPages = totalPages;
		return this;
	}

	public int getTotalRecs() {
		return totalRecs;
	}

	public Page setTotalRecs(int totalRecs) {
		this.totalRecs = totalRecs;
		return this;
	}

	public int getCurrRecords() {
		return currRecords;
	}

	public Page setCurrRecords(int currRecords) {
		this.currRecords = currRecords;
		return this;
	}
	
	public int getStartIndex() {
		return startIndex;
	}

	public Page setStartIndex(int startIndex) {
		this.startIndex = startIndex;
		return this;
	}

	public Map<String,Object> getParameters() {
		return parameters;
	}
	
	public Page setParameters(Map<String,Object> parameters) {
		this.parameters = parameters;
		return this;
	}

	public Object getParameter(String key) {
		return parameters.get(key);
	}

	public Page setParameter(String key, Object param) {
		parameters.put(key, param);
		return this;
	}

	public Page setParameters(Object[] parameters) {
		this.getParameters().clear();
		for (int i = 0; i < parameters.length; i++) {
			this.setParameter(String.valueOf(i), parameters[i]);
		}
		return this;
	}
	
	public Object removeParameter(String key) {
		return parameters.remove(key);
	}
	
	/**
	 * get parameter as String。<br>
	 * 若参数不是String类型，会将其转换成String。
	 * @param key 参数名
	 * @return 参数值
	 */
	public String getParamStr(String key) {
		Object val = parameters.get(key);
		if (val == null) return null;
		if (val instanceof String) {
			return (String) val;
		}
		return String.valueOf(val);
	}
	
	/**
	 * get parameter as Integer。<br>
	 * 若参数不是Integer类型，会将其转换成Integer，转换不成功则抛出异常。
	 * @param key 参数名
	 * @return 参数值。转换成Integer，转换不成功则抛出异常
	 */
	public Integer getParamInt(String key) {
		Object val = parameters.get(key);
		if (val == null) return null;
		if (val instanceof Integer) {
			return (Integer) val;
		}
		return new Integer(val.toString());
	}
	
	/**
	 * get parameter as Long。<br>
	 * 若参数不是Long类型，会将其转换成Long，转换不成功则抛出异常。
	 * @param key 参数名
	 * @return 参数值。转换成Long，转换不成功则抛出异常
	 */
	public Long getParamLong(String key) {
		Object val = parameters.get(key);
		if (val == null) return null;
		if (val instanceof Long) {
			return (Long) val;
		}
		return new Long(val.toString());
	}
	
	/**
	 * 返回查询结果
	 * @return 查询结果
	 */
	public List<?> getQueryResult() {
		return queryResult;
	}

	/**
	 * 设置查询结果
	 * @param queryResult 查询结果
	 * @return 查询结果
	 */
	public Page setQueryResult(List<?> queryResult) {
		this.queryResult = queryResult;
		return this;
	}
	
	/**
	 * 对所有的String类型的参数值执行trim操作去除多余的空白字符
	 * @return this
	 */
	public Page trimParameter() {
		for (Iterator<String> key = parameters.keySet().iterator(); key.hasNext();) {
			String k = key.next();
			if (parameters.get(k) != null && parameters.get(k) instanceof String) {
				parameters.put(k, ((String)parameters.get(k)).trim());
			}
		}
		return this;
	}
}
