package com.cardpay.pccredit.manager.dao.comdao;

import com.cardpay.pccredit.manager.dao.SalaryPaymentDao;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by Johnny on 2017/7/18 0018.
 */
@Service
public class SalaryPaymentComdao {

    @Autowired
    private ManagerBelongMapComdao managerBelongMapComdao;

    @Autowired
    private SalaryPaymentDao salaryPaymentDao;

    public double calculateAvgSalary(String userId,String YM){
        ArrayList<String> arrayList=managerBelongMapComdao.getManagerList(userId);
        String userIds=managerBelongMapComdao.managerSet(arrayList);

        return salaryPaymentDao.getAvgSalaryAvg(userIds,YM).getAmount();
    }
}
