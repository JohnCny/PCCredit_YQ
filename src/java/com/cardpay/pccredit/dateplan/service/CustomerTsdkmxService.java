package com.cardpay.pccredit.dateplan.service;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.dateplan.dao.CustomerTsdkmxDao;
import com.cardpay.pccredit.dateplan.model.CustomerTsdkmxModel;

@Service
public class CustomerTsdkmxService {
	@Autowired
	private CustomerTsdkmxDao UserDao;
	public void insertCustomerTsdk(CustomerTsdkmxModel CustomerTsdkmxModel){
		UserDao.insertCustomerTsdk(CustomerTsdkmxModel);
	}
	public CustomerTsdkmxModel selectMaxLOANDATE(@Param(value = "userId")String userId){
		return UserDao.selectMaxLOANDATE(userId);
	}
	public CustomerTsdkmxModel selectCustomerTsdk(CustomerTsdkmxModel CustomerTsdkmxModel){
		return UserDao.selectCustomerTsdk(CustomerTsdkmxModel);
	}
}
