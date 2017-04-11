package com.cardpay.pccredit.system.filter;

import com.wicresoft.jrad.base.web.filter.BaseQueryFilter;

public class SysLoginLogFilter extends BaseQueryFilter{
	
	
	private String login;
	private String action;
	private String actionTime;
	private String startDate;
	private String endDate;
	
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getActionTime() {
		return actionTime;
	}
	public void setActionTime(String actionTime) {
		this.actionTime = actionTime;
	}
	
   
}
