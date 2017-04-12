package com.cardpay.pccredit.customer.filter;

import java.math.BigDecimal;

/*
 * 放款排名查询
 * */
public class FkRankingFilter {
	private String rankingnum;//排名
	private String customermanager;//客户经理
	private BigDecimal fkmoney;//放款金额
	private String organization;//机构
	public String getCustomermanager() {
		return customermanager;
	}
	public void setCustomermanager(String customermanager) {
		this.customermanager = customermanager;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getRankingnum() {
		return rankingnum;
	}
	public void setRankingnum(String rankingnum) {
		this.rankingnum = rankingnum;
	}
	public BigDecimal getFkmoney() {
		return fkmoney;
	}
	public void setFkmoney(BigDecimal fkmoney) {
		this.fkmoney = fkmoney;
	}
	
}
