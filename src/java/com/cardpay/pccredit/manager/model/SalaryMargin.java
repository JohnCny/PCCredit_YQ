package com.cardpay.pccredit.manager.model;

import com.wicresoft.jrad.base.database.model.BusinessModel;
import com.wicresoft.jrad.base.database.model.ModelParam;

/**
 * Created by Johnny on 2017/7/14 0014.
 */
@ModelParam(table="salary_margin")
public class SalaryMargin extends BusinessModel {
    /**
     * 缴纳保证金金额
      */
    private double amount;
    /**
     * 缴纳年月
     */
    private String ym;
    /**
     * 用户ID
     */
    private String userId;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getYm() {
        return ym;
    }

    public void setYm(String ym) {
        this.ym = ym;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
