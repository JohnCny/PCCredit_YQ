package com.cardpay.pccredit.manager.filter;

import com.wicresoft.jrad.base.web.filter.BaseQueryFilter;

/**
 * Created by Johnny on 2017/7/12 0012.
 */
public class SalaryRecordFilter extends BaseQueryFilter {

    String userId;

    String YM;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getYM() {
        return YM;
    }

    public void setYM(String YM) {
        this.YM = YM;
    }

}
