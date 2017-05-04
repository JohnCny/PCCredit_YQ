package com.cardpay.pccredit.intopieces.model;

import com.wicresoft.jrad.base.database.dao.business.BusinessFilter;

public class ImageMore extends BusinessFilter{
	private String cid;
	private String pid;
	private String uri;
	private Integer count;
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}

	

}
