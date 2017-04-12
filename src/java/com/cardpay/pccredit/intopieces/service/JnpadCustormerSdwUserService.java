package com.cardpay.pccredit.intopieces.service;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.intopieces.dao.JnpadCustormerSdwUserDao;
import com.cardpay.pccredit.intopieces.model.AppManagerAuditLog;
import com.cardpay.pccredit.intopieces.model.CustormerSdwUser;
import com.cardpay.pccredit.intopieces.model.IntoPieces;
import com.cardpay.pccredit.jnpad.model.JnpadCsSdModel;
import com.cardpay.pccredit.riskControl.model.RiskCustomer;
@Service
public class JnpadCustormerSdwUserService {
	@Autowired
	private JnpadCustormerSdwUserDao SdwUserDao;
	public AppManagerAuditLog selectaId(@Param(value = "cid")String cid,@Param(value = "pid")String pid){
		return SdwUserDao.selectaId(cid,pid);
	}
	public int insertCsJl(AppManagerAuditLog sdwuser){
		return SdwUserDao.insertCsJl(sdwuser);
	}
	public void updateCSZT(@Param(value = "userId")String userId, @Param(value = "times")Date times, @Param(value = "money")String money, @Param(value = "applicationId")String applicationId) {
		// TODO Auto-generated method stub
		SdwUserDao.updateCSZT(userId,times,money,applicationId);
	}
	public AppManagerAuditLog selectCSJLAPC(@Param(value = "appId")String appId,@Param(value = "uId")String uId){
		return SdwUserDao.selectCSJLAPC(appId,uId);
	}
	public JnpadCsSdModel findCsSds(@Param(value ="appId")String appId,@Param(value ="uId")String uId){
		return SdwUserDao.findCsSds(appId,uId);
	}
	public JnpadCsSdModel findZzCsSds(@Param(value ="appId")String appId){
		return SdwUserDao.findZzCsSds(appId);
	}
	public List<JnpadCsSdModel> findCsSdId(String appId) {
		// TODO Auto-generated method stub
		return SdwUserDao.findCsSdId(appId);
	}
	public List<JnpadCsSdModel> findCsSdId1(String appId) {
		// TODO Auto-generated method stub
		return SdwUserDao.findCsSdId1(appId);
	}
	public JnpadCsSdModel findBySdwId(@Param(value = "sdwId")String sdwId,@Param(value = "appId")String appId) {
		// TODO Auto-generated method stub
		return SdwUserDao.findBySdwId(sdwId,appId);
	}
	public void insertCustormerSdwUser1(CustormerSdwUser CustormerSdwUser){
		 SdwUserDao.insertCustormerSdwUser1(CustormerSdwUser);
	}
	public void updateCustormerSdwUser1(CustormerSdwUser CustormerSdwUser){
		SdwUserDao.updateCustormerSdwUser1(CustormerSdwUser);
	}
	public int updateCustormerInfoSdwUser(IntoPieces sdwuser){
		return SdwUserDao.updateCustormerInfoSdwUser(sdwuser);
	}
	//通过时的状态
	public void updateCSZTs(@Param(value = "userId")String userId, @Param(value = "times")Date times, @Param(value = "money")String money, @Param(value = "applicationId")String applicationId) {
		// TODO Auto-generated method stub
		SdwUserDao.updateCSZTs(userId,times,money,applicationId);
	}
	public int updateCustormerSdwUser(CustormerSdwUser sdwuser){
		return SdwUserDao.updateCustormerSdwUser(sdwuser);
	}
	public int updateCustormerProSdwUser(IntoPieces sdwuser){
		return SdwUserDao.updateCustormerProSdwUser(sdwuser);
	}
	public int insertRiskSdwUser(RiskCustomer sdwuser){
		return SdwUserDao.insertRiskSdwUser(sdwuser);
	}
	//拒绝时修改节点状态
			public void updateHistorys(@Param(value = "userId")String userId,@Param(value = "times")Date times, @Param(value = "applicationId")String applicationId) {
				// TODO Auto-generated method stub
				SdwUserDao.updateHistorys(userId,times,applicationId);
			}
			//退回时修改节点状态
			public void updateHistory(@Param(value = "userId")String userId,@Param(value = "times")Date times, @Param(value = "applicationId")String applicationId) {
				// TODO Auto-generated method stub
				SdwUserDao.updateHistory(userId,times,applicationId);
			}
	public List<IntoPieces>findsdh(@Param(value = "id")String id){
		return SdwUserDao.findsdh(id);
	}
	public List<IntoPieces>findsdh1(@Param(value = "id")String id){
		return SdwUserDao.findsdh1(id);
	}
	public List<IntoPieces> findsph(@Param(value = "id")String id){
		return SdwUserDao.findsph(id);
	}
	public List<IntoPieces> findsph1(@Param(value = "id")String id){
		return SdwUserDao.findsph1(id);
	}
	public IntoPieces findsph2(@Param(value = "id")String id){
		return SdwUserDao.findsdh2(id);
	}
	public List<IntoPieces> findEndCount(@Param(value = "id")String id){
		return SdwUserDao.findEndCount(id);
	}
}
