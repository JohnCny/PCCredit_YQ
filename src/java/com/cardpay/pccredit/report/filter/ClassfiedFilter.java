package com.cardpay.pccredit.report.filter;

import com.wicresoft.jrad.base.web.filter.BaseQueryFilter;

public class ClassfiedFilter extends BaseQueryFilter{

	private String assureMeans;
	private String managerName;
	private String startDate;
	private String endDate;
	
	public String getAssureMeans() {
		return assureMeans;
	}
	public void setAssureMeans(String assureMeans) {
		this.assureMeans = assureMeans;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
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
	
	
}
