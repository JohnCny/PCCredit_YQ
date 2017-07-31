package com.cardpay.pccredit.manager.dao.comdao;

import com.cardpay.pccredit.manager.model.ManagerBelongMap;
import com.cardpay.pccredit.manager.model.SalaryRecord;
import com.cardpay.pccredit.tools.structure.Tree;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import org.apache.poi.util.Internal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Johnny on 2017/7/18 0018.
 */
@Service
public class SalaryRecordComdao {
    @Autowired
    private CommonDao commonDao;

    /**
     * 查询当月绩效收入
     * @return SalaryRecord
     */
    public SalaryRecord getSalary(String userId,String YM) {

        String sql = "SELECT SUM(add_loan) AS addedLoan, SUM(Loan_Balance) AS loanBalance, " +
                "SUM(Management) AS management, SUM(Assist_Loan) AS assistLoan, " +
                "SUM(Overdue_Loan) AS overdueLoan, SUM(Review_Loan) AS reviewLoan " +
                "FROM SALARY_RECORD " +
                "WHERE userid='"+userId+"' AND YM='"+YM+"'";


        List<SalaryRecord> salaryRecord = commonDao.queryBySql(SalaryRecord.class, sql, null);

        if(salaryRecord.isEmpty()){
            return null;
        }
        else{
            return salaryRecord.get(0);
        }
    }

}
