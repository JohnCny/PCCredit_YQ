package com.cardpay.pccredit.intopieces.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cardpay.pccredit.intopieces.model.AppManagerAuditLog;
import com.cardpay.pccredit.intopieces.model.CustormerSdwUser;
import com.cardpay.pccredit.intopieces.model.IntoPieces;
import com.cardpay.pccredit.jnpad.model.JnpadCsSdModel;
import com.cardpay.pccredit.riskControl.model.RiskCustomer;
import com.wicresoft.util.annotation.Mapper;
@Mapper
public interface JnpadCustormerSdwUserDao {
	//查询进件ID
		AppManagerAuditLog selectaId(@Param(value = "cid")String cid,@Param(value = "pid")String pid);
		//添加初审信息
		int insertCsJl(AppManagerAuditLog sdwuser);
		//初审通过
		void updateCSZT(@Param(value = "userId")String userId, @Param(value = "times")Date times, @Param(value = "money")String money, @Param(value = "applicationId")String applicationId);
		//查询初审信息
		AppManagerAuditLog selectCSJLAPC(@Param(value = "appId")String appId,@Param(value = "uId")String uId);
		JnpadCsSdModel findCsSds(@Param(value = "appId")String appId,@Param(value ="uId")String uId);
		JnpadCsSdModel findZzCsSds(@Param(value = "appId")String appId);
		List<JnpadCsSdModel> findCsSdId(String appId);
		List<JnpadCsSdModel> findCsSdId1(String appId);
		JnpadCsSdModel findBySdwId(@Param(value = "sdwId")String sdwId,@Param(value = "appId")String appId);
		 void insertCustormerSdwUser1(CustormerSdwUser CustormerSdwUser);
		 void updateCustormerSdwUser1(CustormerSdwUser CustormerSdwUser);
		//进件表注入进件状态
		int updateCustormerInfoSdwUser(IntoPieces sdwuser);
		//通过
		void updateCSZTs(@Param(value = "userId")String userId, @Param(value = "times")Date times, @Param(value = "money")String money, @Param(value = "applicationId")String applicationId);
		//审贷决议
		int updateCustormerSdwUser(CustormerSdwUser sdwuser);
		int updateCustormerProSdwUser(IntoPieces sdwuser);
		//添加风险客户
		int insertRiskSdwUser(RiskCustomer sdwuser);
		//拒绝
		void updateHistorys(@Param(value = "userId")String userId,@Param(value = "times")Date times, @Param(value = "applicationId")String applicationId);
		//退回
		void updateHistory(@Param(value = "userId")String userId,@Param(value = "times")Date times, @Param(value = "applicationId")String applicationId);
		//退回进件重新申请时删除初审以及审贷记录。
		int deletePCCustormerSdwUser(@Param(value = "applicationId")String applicationId);
		int deletePCCsJl(@Param(value = "applicationId")String applicationId);
		void deleteByApplicationId(@Param(value = "applicationId")String applicationId);
		//审批记录查看查询审贷记录
		List<IntoPieces>findsdh(@Param(value = "id")String id);
		List<IntoPieces>findsdh1(@Param(value = "id")String id);
		//审批记录查看查询最终审贷记录
		List<IntoPieces> findsph(@Param(value = "id")String id);
		List<IntoPieces> findsph1(@Param(value = "id")String id);
		IntoPieces findsdh2(@Param(value = "id")String id);
		List<IntoPieces> findEndCount(@Param(value = "id")String id);
		IntoPieces findHISTORY(@Param(value = "id")String id);
}
