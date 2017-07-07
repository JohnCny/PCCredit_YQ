package com.cardpay.pccredit.manager.service.salary;

/**
 * Created by Johnny on 2017/7/7 0007.
 * 职工工资工厂类
 */
public class EmployeeSalaryFactory {

    EmployeeSalary createSalaryByUser(int userType,String userId){
        switch (userType){
            case 1:return new ManagerSalary(userId);//客户经理
            case 2:return new OperateManagerSalary(userId);//运营岗
            case 3:return new RiskManagerSalary(userId);//风险经理
            case 4:return new ChargeManagerSalary(userId);//业务主管
            case 5:return new ReviewMemberManagerSalary(userId);//审贷委成员
            case 6:return new GeneralManagerSalary(userId);//总经理
            case 7:return new DeGeneralManagerSalary(userId);//副总经理
            default:return new NullSalary(userId);
        }
    }
}
