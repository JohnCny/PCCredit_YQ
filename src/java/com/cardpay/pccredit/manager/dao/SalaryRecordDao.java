package com.cardpay.pccredit.manager.dao;

import com.cardpay.pccredit.manager.model.SalaryRecord;
import com.wicresoft.util.annotation.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Johnny on 2017/7/11 0011.
 */
@Mapper
public interface SalaryRecordDao {

    /**
     * 获得绩效项总和
     * @param
     * @return
     */
//    public SalaryRecord getSumSalary(SalaryRecordFilter salaryRecordFilter);
    public SalaryRecord getSumSalary(@Param("userId") String userId,@Param("YM") String YM);

}
