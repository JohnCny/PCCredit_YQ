package com.cardpay.pccredit.intopieces.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.intopieces.dao.CustomerSqInfoDao;
import com.cardpay.pccredit.intopieces.model.CustomerSqInfo;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;

@Service
public class CustomerSqInfoService {
	@Autowired
	private CustomerSqInfoDao SqInfoDao;
	
	public int addSqInfo(CustomerSqInfo CustomerSqInfo){
		return SqInfoDao.addSqInfo(CustomerSqInfo);
	}
	public int selectMaxIcount(@Param(value = "pid")String pid, @Param(value = "cid")String cid){
		return SqInfoDao.selectMaxIcount(pid,cid);
	}
	public CustomerSqInfo selectSqInfoBycpId(@Param(value = "pid")String pid, @Param(value = "cid")String cid){
		return SqInfoDao.selectSqInfoBycpId(pid,cid);
	}

}
