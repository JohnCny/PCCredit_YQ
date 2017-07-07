package com.cardpay.pccredit.manager.service.salary;

/**
 * Created by Johnny on 2017/7/6 0006.
 */


public abstract class EmployeeSalary {

    private String userID;

    /**
    获得罚款金额
     */
    int getFine(String YM){
        return 0;
    }

    /**
    获得风险保证金返还金额
     */
    int getReturnedMargin(String YM){
        return 0;
    }


    /**
    按月计算用户绩效
    */
    abstract float calculateSalary(String YM);

    /**
    计算保证金金额
     */
    abstract float calculateMargin(String YM);

    /**
    计算用户实际应得绩效
     计算细节在子类中实现，这里只处理罚款（不含逾期）
     */
    float calculateRealSalary(String YM){
        return calculateSalary(YM)-getFine(YM)+getReturnedMargin(YM)-calculateMargin(YM);
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
