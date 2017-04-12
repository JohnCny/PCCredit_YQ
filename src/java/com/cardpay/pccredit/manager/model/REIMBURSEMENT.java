package com.cardpay.pccredit.manager.model;

import com.wicresoft.jrad.base.database.model.BusinessModel;
import com.wicresoft.jrad.base.database.model.ModelParam;
/**
 * @author songchen
 * @table 还款计划提醒表
 */
@ModelParam(table = "T_REIMBURSEMENT")
public class REIMBURSEMENT extends BusinessModel {
	
	private String  id;                    
	private String  customerId;           
	private String  customerName;         
	private String  customerManagerId;   
	private String  customerManagerName; 
	private String  loandate;              
	private String  money;                 
	private String  lv;                    
	private String  repayTime;            
	private String  repayBj;              
	private String  repayLx;
	private String  repayMethod;
	private String  repayMzee;
	private String  busiCode;
	private String  hasTell;
	
	
	
	public String getHasTell() {
		return hasTell;
	}
	public void setHasTell(String hasTell) {
		this.hasTell = hasTell;
	}
	public String getBusiCode() {
		return busiCode;
	}
	public void setBusiCode(String busiCode) {
		this.busiCode = busiCode;
	}
	public String getRepayMzee() {
		return repayMzee;
	}
	public void setRepayMzee(String repayMzee) {
		this.repayMzee = repayMzee;
	}
	
	public String getRepayMethod() {
		return repayMethod;
	}
	public void setRepayMethod(String repayMethod) {
		this.repayMethod = repayMethod;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerManagerId() {
		return customerManagerId;
	}
	public void setCustomerManagerId(String customerManagerId) {
		this.customerManagerId = customerManagerId;
	}
	public String getCustomerManagerName() {
		return customerManagerName;
	}
	public void setCustomerManagerName(String customerManagerName) {
		this.customerManagerName = customerManagerName;
	}
	public String getLoandate() {
		return loandate;
	}
	public void setLoandate(String loandate) {
		this.loandate = loandate;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getLv() {
		return lv;
	}
	public void setLv(String lv) {
		this.lv = lv;
	}
	public String getRepayTime() {
		return repayTime;
	}
	public void setRepayTime(String repayTime) {
		this.repayTime = repayTime;
	}
	public String getRepayBj() {
		return repayBj;
	}
	public void setRepayBj(String repayBj) {
		this.repayBj = repayBj;
	}
	public String getRepayLx() {
		return repayLx;
	}
	public void setRepayLx(String repayLx) {
		this.repayLx = repayLx;
	}            
	
	   
}
