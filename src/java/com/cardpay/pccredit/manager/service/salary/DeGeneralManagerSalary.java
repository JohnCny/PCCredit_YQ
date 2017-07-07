package com.cardpay.pccredit.manager.service.salary;

/**
 * Created by Johnny on 2017/7/7 0007.
 * 副总经理绩效
 */
public class DeGeneralManagerSalary extends EmployeeSalary{

    DeGeneralManagerSalary(String userID){
        setUserID(userID);
    }

    @Override
    float calculateSalary(String YM) {
        return 85/100*((PublicCalculate.calculateTeamAverageSalary(getUserID(),YM)*2+
                PublicCalculate.getTeamLoanBalance(getUserID(),YM)*15/1000
                -PublicCalculate.calculateTeamOverdueFine(getUserID(),YM,100))*(1-calculateG3(YM)));
    }

    @Override
    float calculateMargin(String YM) {
        return 0;
    }

    //计算G3
    float calculateG3(String YM){
        float overdueRate=PublicCalculate.calculateTeamOverdueRate(getUserID(),YM);

        if(overdueRate>0.08) return 1;
        else if (overdueRate>0.06) return 5/10;
        else if (overdueRate>0.04) return 3/10;
        else return 0;
    }
}
