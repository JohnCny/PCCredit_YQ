package com.cardpay.pccredit.manager.service.salary;

import org.springframework.stereotype.Service;

/**
 * Created by Johnny on 2017/7/7 0007.
 * 风险经理绩效
 */

public class RiskManagerSalaryService extends EmployeeSalaryService {

    RiskManagerSalaryService(String userID){
        setUserID(userID);
    }

    @Override
    public double calculateSalary(String YM) {

        return calculateAverageSalary(YM)+getSumSalary(YM);
    }

    @Override
    public double calculateMargin(String YM) {
        return 0;
    }

    int calculateAverageSalary(String YM){

        //获得客户经理平均绩效
        int averageSalary=0;

        return averageSalary;
    }
}
