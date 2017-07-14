package com.cardpay.pccredit.manager.model;

import com.wicresoft.jrad.base.database.model.BusinessModel;
import com.wicresoft.jrad.base.database.model.ModelParam;

import java.util.Date;

/**
 * Created by Johnny on 2017/7/14 0014.
 */
@ModelParam(table="salary_fine")
public class SalaryFine extends BusinessModel {
    /**
     * 惩罚金额
     */
    private double fineAmount;
    /**
     * 惩罚类型
     */
    private double fineType;
    /**
     * 惩罚原因-描述
     */
    private double fineDescribe;

    private String userId;

    /**
     * 建立时间
     */
    private String ym;
    /**
     * 建立人
     */
    private String createUser;


    public double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public double getFineType() {
        return fineType;
    }

    public void setFineType(double fineType) {
        this.fineType = fineType;
    }

    public double getFineDescribe() {
        return fineDescribe;
    }

    public void setFineDescribe(double fineDescribe) {
        this.fineDescribe = fineDescribe;
    }


    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
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
