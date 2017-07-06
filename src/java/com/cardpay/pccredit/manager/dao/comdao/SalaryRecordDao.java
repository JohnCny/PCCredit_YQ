package com.cardpay.pccredit.manager.dao.comdao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wicresoft.jrad.base.database.dao.common.CommonDao;

import com.cardpay.pccredit.manager.model.SalaryRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Johnny on 2017/7/6 0006.
 */
@Service
public class SalaryRecordDao {
    @Autowired
    private CommonDao commonDao;

    /**
     * 查询当月绩效收入
     * @return SalaryRecord
     */
    public List<SalaryRecord> getSalary(String userId,String YM){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("USERID",userId);
        params.put("YM",YM);
        String sql="SELECT * FROM SALARY_RECORD WHERE USERID=#{USERID} AND YM=#{YM}";
        List<SalaryRecord> salaryRecord=  commonDao.queryBySql(SalaryRecord.class,sql,params);

        return salaryRecord;
    }

}
