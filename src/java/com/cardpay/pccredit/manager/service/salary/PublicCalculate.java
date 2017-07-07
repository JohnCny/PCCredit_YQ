package com.cardpay.pccredit.manager.service.salary;

import com.cardpay.pccredit.manager.dao.comdao.SalaryRecordDao;
import com.cardpay.pccredit.manager.model.SalaryRecord;

import java.util.List;

/**
 * Created by Johnny on 2017/7/7 0007.
 * 计算绩效共用类
 */
public class PublicCalculate {

    /**
     * 获得全部收益总和
     * @param userId
     * @param YM
     * @return
     */
    static int getSumSalary(String userId,String YM){
        SalaryRecordDao salaryRecordDao=new SalaryRecordDao();

        List<SalaryRecord> salary = salaryRecordDao.getSalary(userId, YM);

        if(salary.isEmpty())
            return 0;
        else
            //返回新增贷款所得+月末贷款余额所得+有效管户所得+当月辅调所得-逾期扣款
            return salary.get(0).getAddedLoan()+salary.get(0).getLoanBalance()+
                    salary.get(0).getManagement()+salary.get(0).getAssistLoan()-salary.get(0).getOverdueLoan();
    }

    /**
     * 获得团队逾期率
     * @param userId
     * @param YM
     * @return
     */
    static float calculateTeamOverdueRate(String userId,String YM){
        //获得团队逾期金额
        int overdueAmount=0;
        //获得团队维护贷款余额
        int totalAmount=getTeamLoanBalance(userId, YM);

        return Math.round((overdueAmount/totalAmount*100)/100);//两位小数
    }

    /**
     * 获得团队任务完成率
     * @param userId
     * @param YM
     * @return
     */
    static float calculateTeamTaskCompletionRate(String userId,String YM){
        //获得团队维护贷款余额
        int totalAmout=getTeamLoanBalance(userId,YM);
        //获得团队计划余额
        int planAmout=100;

        return Math.round((totalAmout/planAmout*100)/100);//两位小数
    }

    /**
     * 获得团队贷款余额
     * @param userId
     * @param YM
     * @return
     */
    static int getTeamLoanBalance(String userId,String YM){
        return 1;
    }

    /**
     * 获得跨月逾期扣款
     * @param userId
     * @param YM
     * @param D 每笔逾期扣款对应金额
     * @return
     */
    static int calculateTeamOverdueFine(String userId,String YM,int D){
        //获得团队跨月逾期笔数
        int overdueNum=1;

        return overdueNum*D;
    }

    /**
     * 获得团队平均绩效
     * @param YM
     * @return
     */
    static float calculateTeamAverageSalary(String userId,String YM){

        //获得团队客户经理平均绩效
        int averageSalary=0;

        return averageSalary;
    }
}
