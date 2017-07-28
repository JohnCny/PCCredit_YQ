package com.cardpay.pccredit.manager.service.salary;

import org.springframework.stereotype.Service;

/**
 * Created by Johnny on 2017/7/7 0007.
 * 审贷委绩效
 */

public class ReviewMemberManagerSalaryService extends EmployeeSalaryService {

    ReviewMemberManagerSalaryService(String userID){
        setUserID(userID);
    }

    @Override
    double calculateSalary(String YM) {

        return getSumSalary(YM);
    }

    @Override
    double calculateMargin(String YM) {
        return 0;
    }
}
