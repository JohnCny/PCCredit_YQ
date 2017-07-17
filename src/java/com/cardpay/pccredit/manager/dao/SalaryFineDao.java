package com.cardpay.pccredit.manager.dao;

import com.cardpay.pccredit.manager.model.SalaryFine;
import com.wicresoft.util.annotation.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Johnny on 2017/7/14 0014.
 */
@Mapper
public interface SalaryFineDao {
    /**
     * 获得用户当月惩罚金额总额
     * @param userId
     * @param YM
     * @return
     */
    public SalaryFine getSumSalaryFine(@Param("user_id") String userId,@Param("YM") String YM);

    /**
     * 获得用户当月所有惩罚明细
     * @param userId
     * @param YM
     * @return
     */
    public List<SalaryFine> getSalaryFine(@Param("user_id") String userId,@Param("YM") String YM);

}
