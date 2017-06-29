package com.cardpay.pccredit.jxlx.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.dateplan.model.JBUser;
import com.cardpay.pccredit.jnpad.dao.JnIpadUserLoginDao;
import com.cardpay.pccredit.jxlx.dao.CustomerCoefficientDao;
import com.cardpay.pccredit.jxlx.model.COEFFICIENT;
import com.cardpay.pccredit.jxlx.model.SPLITOFINTEREST;
import com.wicresoft.jrad.base.database.model.QueryResult;

@Service
public class CustomerCoefficientService {
	@Autowired
	private CustomerCoefficientDao CoefficientDao;
	
	public void updateCoefficient(COEFFICIENT COEFFICIENT){
		CoefficientDao.updateCoefficient(COEFFICIENT);
	}
	public QueryResult<COEFFICIENT>selectallCOEFFICIENT(COEFFICIENT COEFFICIENT){
		List<COEFFICIENT>list =CoefficientDao.selectallCOEFFICIENT(COEFFICIENT);
		int count =CoefficientDao.selectallCOEFFICIENTcount(COEFFICIENT);
		QueryResult<COEFFICIENT> qs = new QueryResult<COEFFICIENT>(count, list);
		return qs;
	}
	public List<COEFFICIENT>selectallOrgteam(){
		return CoefficientDao.selectallOrgteam();
	}
		public List<COEFFICIENT>selectallTeam(){
			return CoefficientDao.selectallTeam();
		}
		public COEFFICIENT selectallCOEFFICIENTByUserId(@Param("userid")String userid){
			return CoefficientDao.selectallCOEFFICIENTByUserId(userid);
		}
		public List<SPLITOFINTEREST>selectSpliByTeam(SPLITOFINTEREST SPLITOFINTEREST){
			return CoefficientDao.selectSpliByTeam(SPLITOFINTEREST);
		}
		public SPLITOFINTEREST selectSumSpliByTeam(SPLITOFINTEREST SPLITOFINTEREST){
			return CoefficientDao.selectSumSpliByTeam(SPLITOFINTEREST);
			
		}

}
