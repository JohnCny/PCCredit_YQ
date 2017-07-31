package com.cardpay.pccredit.manager.dao;

import com.cardpay.pccredit.manager.model.SalaryPayment;
import com.wicresoft.util.annotation.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Johnny on 2017/7/18 0018.
 */
@Mapper
public interface SalaryPaymentDao {
    /**
     * ��������û���ʵ�ʼ�Ч
     * @param userId
     * @param YM
     * @return
     */
    public SalaryPayment getAvgSalaryAvg(@Param("userId") String userId, @Param("YM") String YM);
}
