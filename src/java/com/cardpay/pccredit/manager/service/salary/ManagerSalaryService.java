package com.cardpay.pccredit.manager.service.salary;

/**
 * Created by Johnny on 2017/7/6 0006.
 * 客户经理绩效
 */

public class ManagerSalaryService extends EmployeeSalaryService {

    ManagerSalaryService(String userID){
        setUserID(userID);
    }

    @Override
    double calculateSalary(String YM) {
        return getSumSalary(YM);
    }



    @Override
    double calculateMargin(String YM) {
        double salary=calculateSalary(YM);
        double margin=0.0;

        if(salary>10000) margin=(salary-10000)*40/100+3050;
        else if (salary>5000) margin= (salary-5000)*35/100+1300;
        else if (salary>2000) margin= (salary-2000)*30/100+400;
        else margin= salary*20/100;

        super.setMargin(margin);

        return margin;
    }


}
