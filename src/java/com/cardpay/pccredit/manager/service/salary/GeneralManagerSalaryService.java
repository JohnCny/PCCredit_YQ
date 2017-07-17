package com.cardpay.pccredit.manager.service.salary;

import org.springframework.stereotype.Service;

/**
 * Created by Johnny on 2017/7/7 0007.
 * 总经理绩效
 */

public class GeneralManagerSalaryService extends EmployeeSalaryService {

    GeneralManagerSalaryService(String userID){
        setUserID(userID);
    }

    @Override
    double calculateSalary(String YM) {

        return (calculateTeamAverageSalary(YM)*2+ getTeamLoanBalance(YM)*15/1000
                -calculateTeamOverdueFine(YM,100))*(1-calculateG3(YM));
    }

    @Override
    double calculateMargin(String YM) {
        double salary=calculateSalary(YM);
        double margin=0.0;

        if(salary>15000) margin= (salary-15000)*40/100+4250;
        else if (salary>10000) margin= (salary-10000)*35/100+2500;
        else if (salary>5000) margin=(salary-5000)*30/100+1000;
        else margin=salary*20/100;

        super.setMargin(margin);

        return margin;
    }

    //计算G3
    double calculateG3(String YM){
        double overdueRate=calculateTeamOverdueRate(YM);

        if(overdueRate>0.08) return 1;
        else if (overdueRate>0.06) return 5/10;
        else if (overdueRate>0.04) return 3/10;
        else return 0;
    }
}
