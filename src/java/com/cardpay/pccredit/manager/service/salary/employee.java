package com.cardpay.pccredit.manager.service.salary;

/**
 * Created by Johnny on 2017/7/6 0006.
 */


public abstract class employee {

    /*
    获得罚款金额
     */
    int getFineByUser(String userId){
        return 0;
    }

    /*
    获得风险保证金返还金额
     */
    int getReturnedMargin(String userId){
        return 0;
    }

    /*
    计算用户当月绩效
     */
    abstract int calculateSalary(String userId);

    /*
    计算保证金金额
     */
    abstract int calculateMargin(String userId);

    /*
    计算用户实际应得绩效
     */
    int calculateRealSalary(String userId){
        return calculateSalary(userId)-getFineByUser(userId)+getReturnedMargin(userId)-calculateMargin(userId);
    }
}
