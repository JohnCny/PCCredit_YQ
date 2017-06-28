package com.cardpay.pccredit.jxlx.model;

import com.wicresoft.jrad.base.database.dao.business.BusinessFilter;

public class COEFFICIENT  extends BusinessFilter{
	private String id;
	private String userid;
	private double code;
	private String team;
	private String orgteam;
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public double getCode() {
		return code;
	}
	public void setCode(double code) {
		this.code = code;
	}
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public String getOrgteam() {
		return orgteam;
	}
	public void setOrgteam(String orgteam) {
		this.orgteam = orgteam;
	}
}
