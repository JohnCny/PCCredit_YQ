package com.cardpay.pccredit.manager.model;

import com.wicresoft.jrad.base.database.model.BusinessModel;
import com.wicresoft.jrad.base.database.model.ModelParam;

import java.math.BigDecimal;

/**
 * Created by Johnny on 2017/7/6 0006.
 * 薪资记录表
 */
@ModelParam(table="salary_record")
public class SalaryRecord extends BusinessModel {

    /**
     *当月新增贷款收入
     */
    private Double addedLoan;

    /**
     *当月贷款余额收入
     */
    private Double  loanBalance;

    /**
     *当月有效管户收入
     */
    private Double  management;

    /**
     *当月辅调收入
     */
    private Double  assistLoan;

    /**
     *当月逾期扣款
     */
    private Double  overdueLoan;

    /**
     *当月贷款审查
     */
    private Double  reviewLoan;

    /**
     *当前年月
     */
    private String  ym;

    /**
     *贷款ID
     */
    private String  loanId;

    /**
     *用户ID
     */
    private String   userId;

    public Double getAddedLoan() {
        return addedLoan;
    }

    public void setAddedLoan(Double addedLoan) {
        this.addedLoan = addedLoan;
    }

    public Double getLoanBalance() {
        return loanBalance;
    }

    public void setLoanBalance(Double loanBalance) {
        this.loanBalance = loanBalance;
    }

    public Double getManagement() {
        return management;
    }

    public void setManagement(Double management) {
        this.management = management;
    }

    public Double getAssistLoan() {
        return assistLoan;
    }

    public void setAssistLoan(Double assistLoan) {
        this.assistLoan = assistLoan;
    }

    public Double getOverdueLoan() {
        return overdueLoan;
    }

    public void setOverdueLoan(Double overdueLoan) {
        this.overdueLoan = overdueLoan;
    }

    public Double getReviewLoan() {
        return reviewLoan;
    }

    public void setReviewLoan(Double reviewLoan) {
        this.reviewLoan = reviewLoan;
    }

    public String getYm() {
        return ym;
    }

    public void setYm(String ym) {
        this.ym = ym;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
