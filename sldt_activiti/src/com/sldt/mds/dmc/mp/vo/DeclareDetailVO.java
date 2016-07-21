package com.sldt.mds.dmc.mp.vo;

import java.io.Serializable;

public class DeclareDetailVO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String declareId;
	
	private String altId;

	public String getDeclareId() {
		return declareId;
	}

	public void setDeclareId(String declareId) {
		this.declareId = declareId;
	}

	public String getAltId() {
		return altId;
	}

	public void setAltId(String altId) {
		this.altId = altId;
	}

}
