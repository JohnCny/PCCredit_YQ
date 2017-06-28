package com.cardpay.pccredit.dateplan.dao;

import org.apache.ibatis.annotations.Param;

import com.cardpay.pccredit.dateplan.model.CustomerTsdkmxModel;
import com.wicresoft.util.annotation.Mapper;
@Mapper
public interface CustomerTsdkmxDao {
	void insertCustomerTsdk(CustomerTsdkmxModel CustomerTsdkmxModel);
	CustomerTsdkmxModel selectMaxLOANDATE(@Param(value = "userId")String userId);
	CustomerTsdkmxModel selectCustomerTsdk(CustomerTsdkmxModel CustomerTsdkmxModel);
}
