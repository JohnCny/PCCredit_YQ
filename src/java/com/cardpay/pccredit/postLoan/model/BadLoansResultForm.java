package com.cardpay.pccredit.postLoan.model;

import com.wicresoft.jrad.base.web.form.BaseForm;

public class BadLoansResultForm extends BaseForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String deal_result;
	private String deal_date;
	private String dealby;
	
	public String getDeal_result() {
		return deal_result;
	}
	public void setDeal_result(String deal_result) {
		this.deal_result = deal_result;
	}
	public String getDeal_date() {
		return deal_date;
	}
	public void setDeal_date(String deal_date) {
		this.deal_date = deal_date;
	}
	public String getDealby() {
		return dealby;
	}
	public void setDealby(String dealby) {
		this.dealby = dealby;
	}
	
}
