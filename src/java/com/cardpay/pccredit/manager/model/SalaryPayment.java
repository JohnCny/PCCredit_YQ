package com.cardpay.pccredit.manager.model;

import com.wicresoft.jrad.base.database.model.BusinessModel;
import com.wicresoft.jrad.base.database.model.ModelParam;

/**
 * Created by Johnny on 2017/7/18 0018.
 * 实际发放记录
 */
@ModelParam(table="salary_payment")
public class SalaryPayment extends BusinessModel {
    /**
     * 实际绩效金额
     */
    private double amount;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 年月
     */
    private String ym;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getYm() {
        return ym;
    }

    public void setYm(String ym) {
        this.ym = ym;
    }
}
