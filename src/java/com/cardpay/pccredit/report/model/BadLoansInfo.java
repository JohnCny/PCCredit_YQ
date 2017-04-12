package com.cardpay.pccredit.report.model;

import com.wicresoft.jrad.base.web.form.BaseForm;

public class BadLoansInfo extends BaseForm{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String chinesename;
	private String cardid;
	private String managername;
	private String recorddate;
	private String customerId;
	private String subject;
	private String baddescription;
	private String opertor;
	private String instcityCode;
	private String operDateTime;
	private String Referaomunt;
	
	
	
	public String getBaddescription() {
		return baddescription;
	}
	public void setBaddescription(String baddescription) {
		this.baddescription = baddescription;
	}
	public String getChinesename() {
		return chinesename;
	}
	public void setChinesename(String chinesename) {
		this.chinesename = chinesename;
	}
	public String getCardid() {
		return cardid;
	}
	public void setCardid(String cardid) {
		this.cardid = cardid;
	}
	public String getManagername() {
		return managername;
	}
	public void setManagername(String managername) {
		this.managername = managername;
	}
	public String getRecorddate() {
		return recorddate;
	}
	public void setRecorddate(String recorddate) {
		this.recorddate = recorddate;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getOpertor() {
		return opertor;
	}
	public void setOpertor(String opertor) {
		this.opertor = opertor;
	}
	public String getInstcityCode() {
		return instcityCode;
	}
	public void setInstcityCode(String instcityCode) {
		this.instcityCode = instcityCode;
	}
	public String getOperDateTime() {
		return operDateTime;
	}
	public void setOperDateTime(String operDateTime) {
		this.operDateTime = operDateTime;
	}
	public String getReferaomunt() {
		return Referaomunt;
	}
	public void setReferaomunt(String referaomunt) {
		Referaomunt = referaomunt;
	}
	

	
}
