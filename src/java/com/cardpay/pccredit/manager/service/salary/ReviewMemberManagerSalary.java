package com.cardpay.pccredit.manager.service.salary;

/**
 * Created by Johnny on 2017/7/7 0007.
 * 审贷委绩效
 */
public class ReviewMemberManagerSalary extends EmployeeSalary{

    ReviewMemberManagerSalary(String userID){
        setUserID(userID);
    }

    @Override
    float calculateSalary(String YM) {

        return PublicCalculate.getSumSalary(getUserID(),YM);
    }

    @Override
    float calculateMargin(String YM) {
        return 0;
    }
}
