package com.cardpay.pccredit.dateplan.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cardpay.pccredit.dateplan.model.CustomerTsdkmxModel;
import com.cardpay.pccredit.jnpad.model.MonthlyStatisticsModel;
import com.wicresoft.util.annotation.Mapper;
@Mapper
public interface CustomerTsdkmxDao {
	void insertCustomerTsdk(CustomerTsdkmxModel CustomerTsdkmxModel);
	CustomerTsdkmxModel selectCustomerTsdk(CustomerTsdkmxModel CustomerTsdkmxModel);
	//查询随借随还之贷款
	List<CustomerTsdkmxModel>selectCustomerByDkfs();
	int selectCustomerByDkfsCount(CustomerTsdkmxModel CustomerTsdkmxModel);
	//查询客户经理随借随还贷款月季
List<MonthlyStatisticsModel>selectCustomerTsdk(@Param("userId") String userId);
//查询客户经理质押贷款月季
List<MonthlyStatisticsModel>selectMaxLOANDATE(@Param("userId") String userId);
}
