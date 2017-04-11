package com.cardpay.pccredit.manager.model;

import java.math.BigDecimal;

import com.wicresoft.jrad.base.database.model.BusinessModel;
import com.wicresoft.jrad.base.database.model.ModelParam;

/**
 * @Describe 收息历史表
 * @author   songchen
 * @Time     2017年1月12日 16:49:09
 */
@ModelParam(table = "T_INCOME_STATEMENT_HISTORY")
public class InComeStateMentHistory extends BusinessModel {
	private String busicode;//业务编号
	private String custid;//客户编号
	private String cname;//客户名称
	private String custidno;//客户证件号（核心）
	private String instcode;//所属机构
	private String deptcode;//所属部门
	private BigDecimal contractmoney;//合同金额
	private BigDecimal reqlmt;//授信金额
	private String busimanager;//主办客户经理
	private String assistbusimanager;//协办客户经理
	private String loandate;//发放日期（核心）
	private String startdate;//起始日期
	private String enddate;//现到期日/展期到期日（核心）
	private BigDecimal limit;//贷款期限(月)（核心）
	private BigDecimal interest;//利率
	private BigDecimal money;//发放金额（核心）
	private BigDecimal balamt;//贷款余额（核心）
	private String accountstate;//账户状态（核心）
	private BigDecimal paydebt;//已收利息
	private String operdatetime;//操作时间
	public String getBusicode() {
		return busicode;
	}
	public void setBusicode(String busicode) {
		this.busicode = busicode;
	}
	public String getCustid() {
		return custid;
	}
	public void setCustid(String custid) {
		this.custid = custid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getCustidno() {
		return custidno;
	}
	public void setCustidno(String custidno) {
		this.custidno = custidno;
	}
	public String getInstcode() {
		return instcode;
	}
	public void setInstcode(String instcode) {
		this.instcode = instcode;
	}
	public String getDeptcode() {
		return deptcode;
	}
	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}
	public BigDecimal getContractmoney() {
		return contractmoney;
	}
	public void setContractmoney(BigDecimal contractmoney) {
		this.contractmoney = contractmoney;
	}
	public BigDecimal getReqlmt() {
		return reqlmt;
	}
	public void setReqlmt(BigDecimal reqlmt) {
		this.reqlmt = reqlmt;
	}
	public String getBusimanager() {
		return busimanager;
	}
	public void setBusimanager(String busimanager) {
		this.busimanager = busimanager;
	}
	public String getAssistbusimanager() {
		return assistbusimanager;
	}
	public void setAssistbusimanager(String assistbusimanager) {
		this.assistbusimanager = assistbusimanager;
	}
	public String getLoandate() {
		return loandate;
	}
	public void setLoandate(String loandate) {
		this.loandate = loandate;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public BigDecimal getLimit() {
		return limit;
	}
	public void setLimit(BigDecimal limit) {
		this.limit = limit;
	}
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public BigDecimal getBalamt() {
		return balamt;
	}
	public void setBalamt(BigDecimal balamt) {
		this.balamt = balamt;
	}
	public String getAccountstate() {
		return accountstate;
	}
	public void setAccountstate(String accountstate) {
		this.accountstate = accountstate;
	}
	public BigDecimal getPaydebt() {
		return paydebt;
	}
	public void setPaydebt(BigDecimal paydebt) {
		this.paydebt = paydebt;
	}
	public String getOperdatetime() {
		return operdatetime;
	}
	public void setOperdatetime(String operdatetime) {
		this.operdatetime = operdatetime;
	}
	
	
	
	
}
