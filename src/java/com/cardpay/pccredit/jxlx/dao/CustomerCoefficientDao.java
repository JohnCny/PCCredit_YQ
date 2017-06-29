package com.cardpay.pccredit.jxlx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cardpay.pccredit.jxlx.model.COEFFICIENT;
import com.cardpay.pccredit.jxlx.model.SPLITOFINTEREST;
import com.wicresoft.util.annotation.Mapper;

@Mapper
public interface CustomerCoefficientDao {
	//跑入每个客户经理的系数
	void insertCoefficient(COEFFICIENT COEFFICIENT);
	//修改系数
	void updateCoefficient(COEFFICIENT COEFFICIENT);
	//查询没有进入系数表的客户经理的信息
	List<COEFFICIENT>selectNewManager();
	//查询所有客户经理的系数值
	List<COEFFICIENT>selectallCOEFFICIENT(COEFFICIENT COEFFICIENT);
	int selectallCOEFFICIENTcount(COEFFICIENT COEFFICIENT);
	//查询所有的机构
	List<COEFFICIENT>selectallOrgteam();
	//查询所有的团队
		List<COEFFICIENT>selectallTeam();
		//查询单个客户经理的系数值
		COEFFICIENT selectallCOEFFICIENTByUserId(@Param("userid")String userid);
		
		//每月跑入每个客户经理的利息分拆
		void insertSPLITOFINTEREST(SPLITOFINTEREST SPLITOFINTEREST);
		//查询没有进入利息分拆表的客户经理的信息
		List<SPLITOFINTEREST>selectNewManager1(SPLITOFINTEREST SPLITOFINTEREST);
		
		//查询不是质押的贷款
		List<SPLITOFINTEREST>selecthkb(@Param("userid")String userid);
		//查询是质押的贷款
				List<SPLITOFINTEREST>selecthkb1(@Param("userid")String userid);
				
				//根据团队查询所有团队成员的分拆情况
				List<SPLITOFINTEREST>selectSpliByTeam(SPLITOFINTEREST SPLITOFINTEREST);
				
				//查询整个团队的分拆情况
				SPLITOFINTEREST selectSumSpliByTeam(SPLITOFINTEREST SPLITOFINTEREST);
}
