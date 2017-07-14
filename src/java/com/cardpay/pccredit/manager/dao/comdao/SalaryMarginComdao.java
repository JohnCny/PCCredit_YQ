package com.cardpay.pccredit.manager.dao.comdao;

import com.cardpay.pccredit.manager.dao.SalaryMarginDao;
import com.cardpay.pccredit.manager.model.SalaryMargin;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Johnny on 2017/7/14 0014.
 */

@Service
public class SalaryMarginComdao {
    @Autowired
    private CommonDao commonDao;

    @Autowired
    private SalaryMarginDao salaryMarginDao;

    public void updateSalaryMargin(String user_id,String YM,Double margin){

        SalaryMargin salaryMargin=salaryMarginDao.findSalaryMargin(user_id,YM);
        if(salaryMargin==null){
            salaryMargin=new SalaryMargin();
            salaryMargin.setAmount(margin);
            salaryMargin.setUserId(user_id);
            salaryMargin.setYm(YM);
            commonDao.insertObject(salaryMargin);
        }
        else {
            salaryMargin.setAmount(margin);
            commonDao.updateObject(salaryMargin);
        }
    }
}
