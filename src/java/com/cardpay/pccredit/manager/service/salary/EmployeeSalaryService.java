package com.cardpay.pccredit.manager.service.salary;

import com.cardpay.pccredit.manager.dao.SalaryFineDao;
import com.cardpay.pccredit.manager.dao.SalaryMarginDao;
import com.cardpay.pccredit.manager.dao.comdao.SalaryMarginComdao;
import com.cardpay.pccredit.manager.dao.comdao.SalaryRecordComdao;
import com.cardpay.pccredit.manager.model.SalaryRecord;
import org.aspectj.lang.annotation.After;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Johnny on 2017/7/6 0006.
 */

public abstract class EmployeeSalaryService {

    private String userID;

    private double margin;

    @Autowired
    private SalaryRecordComdao salaryRecordComdao;

    @Autowired
    private SalaryMarginComdao salaryMarginComdao;

    @Autowired
    private SalaryFineDao salaryFineDao;

    @Autowired
    private SalaryMarginDao salaryMarginDao;
    /**
    获得罚款金额
     */
    double getFine(String YM){
        salaryFineDao.getSumSalaryFine(getUserID(),YM);
        return 0;
    }

    /**
    获得风险保证金返还金额
     */
    double getReturnedMargin(String YM) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
        try {
            Date date=sdf.parse(YM);
            return salaryMarginDao.findSalaryMargin(getUserID(),get3YAgoDate(date)).getAmount();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }


    /**
    按月计算用户绩效
    */
    abstract double calculateSalary(String YM);

    /**
    计算保证金金额
     */
    abstract double calculateMargin(String YM);


    /**
     计算用户实际应得绩效
     计算细节在子类中实现，这里只处理罚款（不含逾期）
     */
    public double calculateRealSalary(String YM){
        return calculateSalary(YM)-getFine(YM)+getReturnedMargin(YM)-calculateMargin(YM);
    }

    //---------------------------------------------公共计算部分-----------------------------------------------------//
    /**
     * 获得全部收益总和
     * @param YM
     * @return
     */
    protected  double getSumSalary(String YM){
        SalaryRecord salary = salaryRecordComdao.getSalary(getUserID(),YM);

        if(salary==null)
            return 0;
        else
            //返回新增贷款所得+月末贷款余额所得+有效管户所得+当月辅调所得-逾期扣款+审贷会所得
            return salary.getAddedLoan()+salary.getLoanBalance()+
                    salary.getManagement()+salary.getAssistLoan()-salary.getOverdueLoan()+salary.getReviewLoan();
    }

    /**
     * 获得团队逾期率
     * @param YM
     * @return
     */
    protected float calculateTeamOverdueRate(String YM){
        //获得团队逾期金额
        int overdueAmount=0;
        //获得团队维护贷款余额
        int totalAmount=getTeamLoanBalance(YM);

        return Math.round((overdueAmount/totalAmount*100)/100);//两位小数
    }

    /**
     * 获得团队任务完成率
     * @param YM
     * @return
     */
    protected float calculateTeamTaskCompletionRate(String YM){
        //获得团队维护贷款余额
        int totalAmout=getTeamLoanBalance(YM);
        //获得团队计划余额
        int planAmout=100;

        return Math.round((totalAmout/planAmout*100)/100);//两位小数
    }

    /**
     * 获得团队贷款余额
     * @param YM
     * @return
     */
    protected int getTeamLoanBalance(String YM){

        return 1;
    }

    /**
     * 获得跨月逾期扣款
     * @param YM
     * @param D 每笔逾期扣款对应金额
     * @return
     */
    protected int calculateTeamOverdueFine(String YM,int D){
        //获得团队跨月逾期笔数
        int overdueNum=1;

        return overdueNum*D;
    }

    /**
     * 获得团队平均绩效
     * @param YM
     * @return
     */
    protected float calculateTeamAverageSalary(String YM){

        //获得团队客户经理平均绩效
        int averageSalary=0;

        return averageSalary;
    }
    /**
     * 获得上个月 todo:可提炼到工具类
     * @return 返回36个月前的yyyy-MM
     */
    protected String get3YAgoDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -36);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return sdf.format(cal.getTime());
    }

    //---------------------------------------------公共计算部分-----------------------------------------------------//

    @After("calculateMargin")
    void updateSalaryMargin(String YM){
        salaryMarginComdao.updateSalaryMargin(getUserID(),YM,getMargin());
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public double getMargin() {
        return margin;
    }

    public void setMargin(double margin) {
        this.margin = margin;
    }
}
