package com.cardpay.pccredit.manager.dao.comdao;

import com.cardpay.pccredit.manager.model.SalaryFine;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Johnny on 2017/7/14 0014.
 */
@Service
public class SalaryFineComdao {

    @Autowired
    private CommonDao commonDao;

    public void insertSalaryFine(SalaryFine salaryFine){
        commonDao.insertObject(salaryFine);
    }
}
