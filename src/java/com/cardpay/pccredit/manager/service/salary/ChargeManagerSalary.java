package com.cardpay.pccredit.manager.service.salary;

/**
 * Created by Johnny on 2017/7/7 0007.
 * 业务主管绩效
 */
public class ChargeManagerSalary extends EmployeeSalary{

    ChargeManagerSalary(String userID){
        setUserID(userID);
    }

    @Override
    float calculateSalary(String YM) {
        float averageSalary=PublicCalculate.calculateTeamAverageSalary(getUserID(), YM);
        float G2=calculateG2(YM);
        float F1=PublicCalculate.calculateTeamTaskCompletionRate(getUserID(),YM);
        float overdueFine=PublicCalculate.calculateTeamOverdueFine(getUserID(),YM,100);

        return averageSalary*15/10*(1-G2)*F1-overdueFine;
    }

    @Override
    float calculateMargin(String YM) {
        return 0;
    }
    //计算G2
    float calculateG2(String YM){
        float overdueRate=PublicCalculate.calculateTeamOverdueRate(getUserID(),YM);

        if(overdueRate>0.08) return 1;
        else if (overdueRate>0.06) return 5/10;
        else if (overdueRate>0.04) return 3/10;
        else return 0;
    }

}
