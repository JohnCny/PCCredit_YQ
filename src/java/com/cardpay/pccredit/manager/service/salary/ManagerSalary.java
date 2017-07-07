package com.cardpay.pccredit.manager.service.salary;

/**
 * Created by Johnny on 2017/7/6 0006.
 * 客户经理绩效
 */
public class ManagerSalary extends EmployeeSalary {

    ManagerSalary(String userID){
        setUserID(userID);
    }

    @Override
    float calculateSalary(String YM) {
        return PublicCalculate.getSumSalary(getUserID(),YM);
    }


    /**
     * 获得上个月 todo:可提炼到工具类
     * @return 返回上个月的yyyyMM
     *
    private String getLastMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        return sdf.format(getLastDate());
    }

    private static Date getLastDate(){
        Calendar cal = Calendar.getInstance();
        Date date=new Date();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }*/
    @Override
    float calculateMargin(String YM) {
        float salary=calculateSalary(YM);
        if(salary>10000) return (salary-10000)*40/100+3050;
        else if (salary>5000) return (salary-5000)*35/100+1300;
        else if (salary>2000) return (salary-2000)*30/100+400;
        else return salary*20/100;
    }
}
