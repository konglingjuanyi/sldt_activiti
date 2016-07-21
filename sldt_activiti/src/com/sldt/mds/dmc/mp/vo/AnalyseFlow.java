/**
 * 
 */
package com.sldt.mds.dmc.mp.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分析结果中的数据流向描述
 * 
 * @author fbchen
 * @version 2.2 2010-12-23
 */
public class AnalyseFlow implements Serializable {
	private static final long serialVersionUID = 1L;

	protected AnalyseFlow pAnalyseFlow;
	
	protected List<AnalyseFlow> childAnalyseFlow = new ArrayList<AnalyseFlow>();
	
	protected String id;
	
	/**
	 * 上游元数据ID
	 */
	protected String upstreamId;
	
	/**
	 * 下游元数据ID
	 */
	protected String downstreamId;
	/**
	 * 上游元数据模型ID
	 */
	protected String upstreamClassifier;
	
	/**
	 * 下游元数据模型ID
	 */
	protected String downUpstreamClassifier;
	
	/**
	 * 用于区分启示线条为ETL模型还是字典模型，0:字典，1：ETL
	 */
	protected String upstreamModelType;

	/**
	 * 用于区分启示线条为ETL模型还是字典模型，0:字典，1：ETL
	 */
	protected String downUpstreamModelType;
	
	/**
	 * 上游元数据CODE
	 */
	protected String upstreamCode;
	
	/**
	 * 下游元数据CODE
	 */
	protected String downUpstreamCode;
	
	private String upnamespace;
	
	private String downupnamespace;
	
	public String getUpnamespace() {
		return upnamespace;
	}

	public void setUpnamespace(String upnamespace) {
		this.upnamespace = upnamespace;
	}

	public String getDownupnamespace() {
		return downupnamespace;
	}

	public void setDownupnamespace(String downupnamespace) {
		this.downupnamespace = downupnamespace;
	}

	public String getUpstreamId() {
		return upstreamId;
	}

	public void setUpstreamId(String upstreamId) {
		this.upstreamId = upstreamId;
	}

	public String getDownstreamId() {
		return downstreamId;
	}

	public void setDownstreamId(String downstreamId) {
		this.downstreamId = downstreamId;
	}

	public String getUpstreamClassifier() {
		return upstreamClassifier;
	}

	public void setUpstreamClassifier(String upstreamClassifier) {
		this.upstreamClassifier = upstreamClassifier;
	}

	public String getDownUpstreamClassifier() {
		return downUpstreamClassifier;
	}

	public void setDownUpstreamClassifier(String downUpstreamClassifier) {
		this.downUpstreamClassifier = downUpstreamClassifier;
	}

	public String getUpstreamModelType() {
		return upstreamModelType;
	}

	public void setUpstreamModelType(String upstreamModelType) {
		this.upstreamModelType = upstreamModelType;
	}

	public String getDownUpstreamModelType() {
		return downUpstreamModelType;
	}

	public void setDownUpstreamModelType(String downUpstreamModelType) {
		this.downUpstreamModelType = downUpstreamModelType;
	}

	public String getUpstreamCode() {
		return upstreamCode;
	}

	public void setUpstreamCode(String upstreamCode) {
		this.upstreamCode = upstreamCode;
	}

	public String getDownUpstreamCode() {
		return downUpstreamCode;
	}

	public void setDownUpstreamCode(String downUpstreamCode) {
		this.downUpstreamCode = downUpstreamCode;
	}

	public AnalyseFlow getpAnalyseFlow() {
		return pAnalyseFlow;
	}

	public void setpAnalyseFlow(AnalyseFlow pAnalyseFlow) {
		this.pAnalyseFlow = pAnalyseFlow;
	}

	public List<AnalyseFlow> getChildAnalyseFlow() {
		return childAnalyseFlow;
	}

	public void addChildAnalyseFlow(AnalyseFlow childAnalyseFlow) {
		this.childAnalyseFlow.add(childAnalyseFlow);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
