package com.cardpay.pccredit.manager.service.salary;

import org.springframework.stereotype.Service;

/**
 * Created by Johnny on 2017/7/7 0007.
 * 业务主管绩效
 */

public class ChargeManagerSalaryServiceService extends EmployeeSalaryService {

    ChargeManagerSalaryServiceService(String userID){
        setUserID(userID);
    }

    @Override
    double calculateSalary(String YM) {

        double averageSalary=calculateTeamAverageSalary(YM);//客户经理平均绩效
        double G2=calculateG2(YM);//G2
        double F1=calculateTeamTaskCompletionRate(YM);//F1，任务完成率
        double overdueFine=calculateTeamOverdueFine(YM,100);//逾期罚款

        return averageSalary*15/10*(1-G2)*F1-overdueFine;
    }

    @Override
    double calculateMargin(String YM) {
        return 0;
    }
    //计算G2
    double calculateG2(String YM){
        double overdueRate=calculateTeamOverdueRate(YM);

        if(overdueRate>0.08) return 1;
        else if (overdueRate>0.06) return 5/10;
        else if (overdueRate>0.04) return 3/10;
        else return 0;
    }

}
