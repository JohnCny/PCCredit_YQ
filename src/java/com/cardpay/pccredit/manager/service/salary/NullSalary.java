package com.cardpay.pccredit.manager.service.salary;

/**
 * Created by Johnny on 2017/7/7 0007.
 * 空工资类
 */
public class NullSalary extends EmployeeSalary{

    NullSalary(String userID){
        setUserID(userID);
    }

    @Override
    float calculateSalary(String YM) {
        return 0;
    }

    @Override
    float calculateMargin(String YM) {
        return 0;
    }
}
