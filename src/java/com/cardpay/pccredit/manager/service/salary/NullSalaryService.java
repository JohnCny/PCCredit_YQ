package com.cardpay.pccredit.manager.service.salary;

import org.springframework.stereotype.Service;

/**
 * Created by Johnny on 2017/7/7 0007.
 * 空工资类
 */

public class NullSalaryService extends EmployeeSalaryService {

    NullSalaryService(String userID){
        setUserID(userID);
    }
    public NullSalaryService(){}
    @Override
    double calculateSalary(String YM) {
        return 0;
    }

    @Override
    double calculateMargin(String YM) {
        return 0;
    }
}
