package com.cardpay.pccredit.dateplan.model;

import com.wicresoft.jrad.base.database.dao.business.BusinessFilter;

public class JBUser extends BusinessFilter{
	private String id;
	private String name;
	private String qname;
	private String parentId;
	private String childId;
	private String userId;
	private String orgId;
	private String displayName;
	private String zt;
	private Integer rezl;
	private Integer wcrezl;
	private String rebfl;
	
	public String getRebfl() {
		return rebfl;
	}
	public void setRebfl(String rebfl) {
		this.rebfl = rebfl;
	}
	public Integer getRezl() {
		return rezl;
	}
	public void setRezl(Integer rezl) {
		this.rezl = rezl;
	}
	public Integer getWcrezl() {
		return wcrezl;
	}
	public void setWcrezl(Integer wcrezl) {
		this.wcrezl = wcrezl;
	}
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
	public String getQname() {
		return qname;
	}
	public void setQname(String qname) {
		this.qname = qname;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getChildId() {
		return childId;
	}
	public void setChildId(String childId) {
		this.childId = childId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


}
