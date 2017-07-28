package com.cardpay.pccredit.manager.model;

import com.wicresoft.jrad.base.database.model.BusinessModel;
import com.wicresoft.jrad.base.database.model.ModelParam;

/**
 * Created by Johnny on 2017/7/18 0018.
 * ʵ�ʷ��ż�¼
 */
@ModelParam(table="salary_payment")
public class SalaryPayment extends BusinessModel {
    /**
     * ʵ�ʼ�Ч���
     */
    private double amount;
    /**
     * �û�ID
     */
    private String userId;
    /**
     * ����
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
