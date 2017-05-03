package com.cardpay.pccredit.intopieces.model;

import java.util.Date;

public class CustomerSqInfo {
	private String id;
	private String name;//申请人
	private String cardId;//申请人证件
	private String sqje;//申请金额
	private String dkyt;//贷款用途
	private String sqqx;//申请期限
	private String jyje;//建议金额
	private String jyqx;//建议期限
	private String jycp;//建议产品
	private String jylv;//建议利率
	private String jydbr;//建议担保人
	private String gx;//与担保人关系
	private String jydyw;//建议抵押物
	private String wqr;//抵押物物权人
	private String cid;
	private String pid;
	private Date time;
	private Integer icount;
	public Integer getIcount() {
		return icount;
	}
	public void setIcount(Integer icount) {
		this.icount = icount;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
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
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getSqje() {
		return sqje;
	}
	public void setSqje(String sqje) {
		this.sqje = sqje;
	}
	public String getDkyt() {
		return dkyt;
	}
	public void setDkyt(String dkyt) {
		this.dkyt = dkyt;
	}
	public String getSqqx() {
		return sqqx;
	}
	public void setSqqx(String sqqx) {
		this.sqqx = sqqx;
	}
	public String getJyje() {
		return jyje;
	}
	public void setJyje(String jyje) {
		this.jyje = jyje;
	}
	public String getJyqx() {
		return jyqx;
	}
	public void setJyqx(String jyqx) {
		this.jyqx = jyqx;
	}
	public String getJycp() {
		return jycp;
	}
	public void setJycp(String jycp) {
		this.jycp = jycp;
	}
	public String getJylv() {
		return jylv;
	}
	public void setJylv(String jylv) {
		this.jylv = jylv;
	}
	public String getJydbr() {
		return jydbr;
	}
	public void setJydbr(String jydbr) {
		this.jydbr = jydbr;
	}
	public String getGx() {
		return gx;
	}
	public void setGx(String gx) {
		this.gx = gx;
	}
	public String getJydyw() {
		return jydyw;
	}
	public void setJydyw(String jydyw) {
		this.jydyw = jydyw;
	}
	public String getWqr() {
		return wqr;
	}
	public void setWqr(String wqr) {
		this.wqr = wqr;
	}
	

}
