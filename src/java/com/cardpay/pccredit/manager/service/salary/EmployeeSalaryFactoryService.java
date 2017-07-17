package com.cardpay.pccredit.manager.service.salary;

import org.springframework.stereotype.Service;

/**
 * Created by Johnny on 2017/7/7 0007.
 * 职工工资工厂类
 */
@Service
public class EmployeeSalaryFactoryService {

     public EmployeeSalaryService createSalaryByUser(int userType,String userId){
        switch (userType){
            case 1:return new ManagerSalaryService(userId);//客户经理
            case 2:return new OperateManagerSalaryService(userId);//运营岗
            case 3:return new RiskManagerSalaryService(userId);//风险经理
            case 4:return new ChargeManagerSalaryServiceService(userId);//业务主管
            case 5:return new ReviewMemberManagerSalaryService(userId);//审贷委成员
            case 6:return new GeneralManagerSalaryService(userId);//总经理
            case 7:return new DeGeneralManagerSalaryServiceService(userId);//副总经理
            default:return new NullSalaryService(userId);
        }
    }
}
