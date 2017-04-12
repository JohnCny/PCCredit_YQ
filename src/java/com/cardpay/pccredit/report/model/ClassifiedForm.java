package com.cardpay.pccredit.report.model;

import com.wicresoft.jrad.base.web.form.BaseForm;

public class ClassifiedForm extends BaseForm{

	/**
	 * 分类统计
	 */
	private static final long serialVersionUID = 1L;
	private String chineseName;
	private String cardId;
	private String productName;
	private String reqlmt;//授信金额
	private String money;//发放金额
	private String balamt;//发放金额
	private String interest;//利率
	private String accountstate;
	private String loandate;
	private String managerName;
	
	public String getBalamt() {
		return balamt;
	}
	public void setBalamt(String balamt) {
		this.balamt = balamt;
	}
	
	public String getChineseName() {
		return chineseName;
	}
	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getReqlmt() {
		return reqlmt;
	}
	public void setReqlmt(String reqlmt) {
		this.reqlmt = reqlmt;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getAccountstate() {
		return accountstate;
	}
	public void setAccountstate(String accountstate) {
		this.accountstate = accountstate;
	}
	public String getLoandate() {
		return loandate;
	}
	public void setLoandate(String loandate) {
		this.loandate = loandate;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	

}
