package com.cardpay.pccredit.manager.dao;

import com.cardpay.pccredit.manager.model.SalaryMargin;
import com.wicresoft.util.annotation.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Johnny on 2017/7/14 0014.
 */
@Mapper
public interface SalaryMarginDao {
    /**
     * 获得用户保证金
     * @param userId
     * @param YM
     * @return
     */
    public SalaryMargin findSalaryMargin(@Param("user_id") String userId,@Param("YM") String YM);
}
