package com.cardpay.pccredit.manager.model;
import com.wicresoft.jrad.base.database.id.IDType;
import com.wicresoft.jrad.base.database.model.BusinessModel;
import com.wicresoft.jrad.base.database.model.ModelParam;

/**
 * Created by Johnny on 2017/7/6 0006.
 * 薪资记录表
 */
@ModelParam(table="SALARY_RECORD",generator=IDType.uuid32)
public class SalaryRecord extends BusinessModel {

    /**
     *当月新增贷款收入
     */
    private float  addedLoan;

    /**
     *当月贷款余额收入
     */
    private float  loanBalance;

    /**
     *当月有效管户收入
     */
    private float  management;

    /**
     *当月辅调收入
     */
    private float  assistLoan;

    /**
     *当月逾期扣款
     */
    private float  overdueLoan;

    /**
     *当月贷款审查
     */
    private float  reviewLoan;

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

    public float getAddedLoan() {
        return addedLoan;
    }

    public void setAddedLoan(float addedLoan) {
        this.addedLoan = addedLoan;
    }

    public float getLoanBalance() {
        return loanBalance;
    }

    public void setLoanBalance(float loanBalance) {
        this.loanBalance = loanBalance;
    }

    public float getManagement() {
        return management;
    }

    public void setManagement(float management) {
        this.management = management;
    }

    public float getAssistLoan() {
        return assistLoan;
    }

    public void setAssistLoan(float assistLoan) {
        this.assistLoan = assistLoan;
    }

    public float getOverdueLoan() {
        return overdueLoan;
    }

    public void setOverdueLoan(float overdueLoan) {
        this.overdueLoan = overdueLoan;
    }

    public float getReviewLoan() {
        return reviewLoan;
    }

    public void setReviewLoan(float reviewLoan) {
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
