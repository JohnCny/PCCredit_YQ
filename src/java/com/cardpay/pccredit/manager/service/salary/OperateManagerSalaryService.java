package com.cardpay.pccredit.manager.service.salary;

import org.springframework.stereotype.Service;

/**
 * Created by Johnny on 2017/7/7 0007.
 * 运营岗绩效
 */

public class OperateManagerSalaryService extends EmployeeSalaryService {

    OperateManagerSalaryService(String userID){
        setUserID(userID);
    }

    @Override
    double calculateSalary(String YM) {
        //返回绩效额
        return calculateAverageSalary(YM);
    }

    @Override
    double calculateMargin(String YM) {
        return 0;
    }

    int calculateAverageSalary(String YM){

        //获得所支撑客户经理清单
        int supportNum=0;
        //获得客户经理平均绩效
        int averageSalary=0;
        //计算参数
        int param=(supportNum>12)?(100+(supportNum-12)*25)*80:8000;


        return averageSalary*param;
    }
}
