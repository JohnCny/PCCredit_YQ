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
    private int  addedLoan;

    /**
     *当月贷款余额收入
     */
    private int  loanBalance;

    /**
     *当月有效管户收入
     */
    private int  management;

    /**
     *当月辅调收入
     */
    private int  assistLoan;

    /**
     *当月逾期扣款
     */
    private int  overdueLoan;

    public int getOverdueLoan() {
        return overdueLoan;
    }

    public void setOverdueLoan(int overdueLoan) {
        this.overdueLoan = overdueLoan;
    }

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
    private int  userId;

    public int getAddedLoan() {
        return addedLoan;
    }

    public void setAddedLoan(int addedLoan) {
        this.addedLoan = addedLoan;
    }

    public int getLoanBalance() {
        return loanBalance;
    }

    public void setLoanBalance(int loanBalance) {
        this.loanBalance = loanBalance;
    }

    public int getManagement() {
        return management;
    }

    public void setManagement(int management) {
        this.management = management;
    }

    public int getAssistLoan() {
        return assistLoan;
    }

    public void setAssistLoan(int assistLoan) {
        this.assistLoan = assistLoan;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
