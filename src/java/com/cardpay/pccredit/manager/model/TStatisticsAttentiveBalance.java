/**
 * 
 */
package com.cardpay.pccredit.manager.model;

import com.wicresoft.jrad.base.database.id.IDType;
import com.wicresoft.jrad.base.database.model.BusinessModel;
import com.wicresoft.jrad.base.database.model.ModelParam;

/**
 * @author songchen
 */
@ModelParam(table="T_STATISTICS_ATTENTIVE_BALANCE",generator=IDType.uuid32)
public class TStatisticsAttentiveBalance extends BusinessModel {

	private static final long serialVersionUID = 1L;
	
	private String  year;                          
	private String  month;                         
	private String  monthDayAverageCustLoanamt;
	private String  customerManagerId;
	private String  customerId;      
	private String instcode;
	
	
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getInstcode() {
		return instcode;
	}
	public void setInstcode(String instcode) {
		this.instcode = instcode;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getMonthDayAverageCustLoanamt() {
		return monthDayAverageCustLoanamt;
	}
	public void setMonthDayAverageCustLoanamt(String monthDayAverageCustLoanamt) {
		this.monthDayAverageCustLoanamt = monthDayAverageCustLoanamt;
	}
	
	public String getCustomerManagerId() {
		return customerManagerId;
	}
	public void setCustomerManagerId(String customerManagerId) {
		this.customerManagerId = customerManagerId;
	}
	
	
	
}
