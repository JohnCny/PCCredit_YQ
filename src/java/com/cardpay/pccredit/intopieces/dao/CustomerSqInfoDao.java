package com.cardpay.pccredit.intopieces.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cardpay.pccredit.intopieces.model.CustomerSqInfo;
import com.wicresoft.util.annotation.Mapper;

@Mapper
public interface CustomerSqInfoDao {
	//上传调查模板前取出表里面的申请信息进行保存
	int addSqInfo(CustomerSqInfo CustomerSqInfo);
	//查询此进件第几次上传模板
	int selectMaxIcount(@Param(value = "pid")String pid, @Param(value = "cid")String cid);
	//进件初审时查询调查模板申请记录
	CustomerSqInfo selectSqInfoBycpId(@Param(value = "pid")String pid, @Param(value = "cid")String cid);

}
