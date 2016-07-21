package com.sldt.activiti.process.common;

import java.util.List;

public class PageResult {
	
	/**
	 * 一页数据
	 */
	private List result;
	
	/**
	 * 总行数
	 */
	private Long rowCount;
	
	public PageResult() {
	}

	public PageResult(List result, Long rowCount) {
		super();
		this.result = result;
		this.rowCount = rowCount;
	}

	public List getResult() {
		return result;
	}

	public void setResult(List result) {
		this.result = result;
	}

	public Long getRowCount() {
		return rowCount;
	}

	public void setRowCount(Long rowCount) {
		this.rowCount = rowCount;
	}

}
