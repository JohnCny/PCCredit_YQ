package com.cardpay.pccredit.manager.service.salary;

import com.cardpay.pccredit.manager.dao.comdao.SalaryRecordDao;
import com.cardpay.pccredit.manager.model.SalaryRecord;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Johnny on 2017/7/6 0006.
 */
public class ManagerSalary extends EmployeeSalary {
    private String userId;
    @Override
    int calculateSalary(String YM) {
        SalaryRecordDao salaryRecordDao=new SalaryRecordDao();

        List<SalaryRecord> salary = salaryRecordDao.getSalary(this.userId, YM);

        if(salary.isEmpty())
            return 0;
        else
            //返回新增贷款所得+月末贷款余额所得+有效管户所得+当月辅调所得-逾期扣款
            return salary.get(0).getAddedLoan()+salary.get(0).getLoanBalance()+
                    salary.get(0).getManagement()+salary.get(0).getAssistLoan()-salary.get(0).getOverdueLoan();
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
    int calculateMargin(String YM) {
        int salary=calculateSalary(YM);
        //数字*100倍计算
        if(salary>10000*100) return (salary-10000*100)*40+3050;
        else if (salary>5000*100) return (salary-5000*100)*35+1300;
        else if (salary>2000*100) return (salary-2000*100)*30+400;
        else return salary*20;
    }
}
