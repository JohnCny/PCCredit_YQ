package com.cardpay.pccredit.postLoan.model;

import java.math.BigDecimal;

import com.wicresoft.jrad.base.database.model.BusinessModel;
/**
 * 合同Form
 * @author sc
 */
public class ContractForm extends BusinessModel {
    
    private String keycode;
    private String chineseName;
    private BigDecimal money;
	private String deptCode;
	private String displayName;
	private String startDate;
	public String getKeycode() {
		return keycode;
	}
	public void setKeycode(String keycode) {
		this.keycode = keycode;
	}
	public String getChineseName() {
		return chineseName;
	}
	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	
}
