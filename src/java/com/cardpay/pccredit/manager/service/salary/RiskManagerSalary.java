package com.cardpay.pccredit.manager.service.salary;

/**
 * Created by Johnny on 2017/7/7 0007.
 * 风险经理绩效
 */
public class RiskManagerSalary extends EmployeeSalary{

    RiskManagerSalary(String userID){
        setUserID(userID);
    }

    @Override
    float calculateSalary(String YM) {

        return calculateAverageSalary(YM)+PublicCalculate.getSumSalary(getUserID(),YM);
    }

    @Override
    float calculateMargin(String YM) {
        return 0;
    }

    int calculateAverageSalary(String YM){

        //获得客户经理平均绩效
        int averageSalary=0;

        return averageSalary;
    }
}
