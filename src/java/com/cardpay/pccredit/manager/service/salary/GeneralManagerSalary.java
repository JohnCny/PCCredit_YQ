package com.cardpay.pccredit.manager.service.salary;

/**
 * Created by Johnny on 2017/7/7 0007.
 * 总经理绩效
 */
public class GeneralManagerSalary extends EmployeeSalary{

    GeneralManagerSalary(String userID){
        setUserID(userID);
    }

    @Override
    float calculateSalary(String YM) {

        return (PublicCalculate.calculateTeamAverageSalary(getUserID(),YM)*2+
                PublicCalculate.getTeamLoanBalance(getUserID(),YM)*15/1000
                -PublicCalculate.calculateTeamOverdueFine(getUserID(),YM,100))*(1-calculateG3(YM));
    }

    @Override
    float calculateMargin(String YM) {
        float salary=calculateSalary(YM);
        if(salary>15000) return (salary-15000)*40/100+4250;
        else if (salary>10000) return (salary-10000)*35/100+2500;
        else if (salary>5000) return (salary-5000)*30/100+1000;
        else return salary*20/100;
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
