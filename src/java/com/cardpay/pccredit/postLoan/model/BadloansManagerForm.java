package com.cardpay.pccredit.postLoan.model;

import java.util.Date;

import com.wicresoft.jrad.base.web.form.BaseForm;

public class BadloansManagerForm extends BaseForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String chinese_name;
	private String custidno;
	private String accountstatus;
	private String contractmoney;
	private String grantamount;
	private String duedate;
	private String debtinterest;
	private String restoredamount;
	private String description;
	private String display_name;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getChinese_name() {
		return chinese_name;
	}
	public void setChinese_name(String chinese_name) {
		this.chinese_name = chinese_name;
	}
	public String getCustidno() {
		return custidno;
	}
	public void setCustidno(String custidno) {
		this.custidno = custidno;
	}
	public String getAccountstatus() {
		return accountstatus;
	}
	public void setAccountstatus(String accountstatus) {
		this.accountstatus = accountstatus;
	}
	public String getContractmoney() {
		return contractmoney;
	}
	public void setContractmoney(String contractmoney) {
		this.contractmoney = contractmoney;
	}
	public String getGrantamount() {
		return grantamount;
	}
	public void setGrantamount(String grantamount) {
		this.grantamount = grantamount;
	}
	public String getDuedate() {
		return duedate;
	}
	public void setDuedate(String duedate) {
		this.duedate = duedate;
	}
	public String getDebtinterest() {
		return debtinterest;
	}
	public void setDebtinterest(String debtinterest) {
		this.debtinterest = debtinterest;
	}
	public String getRestoredamount() {
		return restoredamount;
	}
	public void setRestoredamount(String restoredamount) {
		this.restoredamount = restoredamount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDisplay_name() {
		return display_name;
	}
	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}
	
	

}
