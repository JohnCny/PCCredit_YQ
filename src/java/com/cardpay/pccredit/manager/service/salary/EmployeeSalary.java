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
    abstract int calculateSalary(String YM);

    /**
    计算保证金金额
     */
    abstract int calculateMargin(String YM);

    /*
    计算用户实际应得绩效
     */
    int calculateRealSalary(String YM){
        return calculateSalary(YM)-getFine(YM)+getReturnedMargin(YM)-calculateMargin(YM);
    }
}
