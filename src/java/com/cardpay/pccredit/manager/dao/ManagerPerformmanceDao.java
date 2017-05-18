package com.cardpay.pccredit.manager.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cardpay.pccredit.manager.form.BankListForm;
import com.cardpay.pccredit.manager.form.DeptMemberForm;
import com.cardpay.pccredit.manager.form.ManagerPerformmanceForm;
import com.cardpay.pccredit.manager.model.ManagerPerformmance;
import com.cardpay.pccredit.manager.model.ManagerPerformmanceSum;
import com.wicresoft.util.annotation.Mapper;

@Mapper
public interface ManagerPerformmanceDao {
	//添加进度
	public void  insertManager(ManagerPerformmance ManagerPerformmance);
	//查询部门成员
	List<DeptMemberForm> findMumberByDeptId(@Param(value="id") String id);
	//查询客户经理当天进度
	List<ManagerPerformmance> finManagerPerformmanceById(@Param(value="managerId") String managerId);
	ManagerPerformmance finManagerPerformmanceById1(@Param(value="managerId") String managerId);
	//查询客户经理总进度
	ManagerPerformmance finManagerPerformmanceSumById(@Param(value="managerId") String managerId,@Param(value="changedate") String changedate);
	//将当天数量累加到总进度
	void updateManagerPerformmanceSum(ManagerPerformmanceSum managerPerformmance);
	//查询总进度
	ManagerPerformmanceForm findSumPerformmanceById(@Param(value="managerId") String Id,
													@Param(value="startDate") String startDate,
													@Param(value="endDate") String endDate);
	//查询指定支行进度
	ManagerPerformmanceForm findDeptSumPerformmanceById(@Param(value="orgId") String orgId,
														@Param(value="startDate") String satrtDate,
														@Param(value="endDate") String endDate);
	//查找所有支行
	List<BankListForm> findALlbank();
	//查询指定所有支行总进度
	ManagerPerformmanceForm findALLDeptSumPerformmanceById(@Param(value="startDate") String startDate,@Param(value="endDate") String endDate);
	//更新指定日期进度
	void updateManagerPerformmanceSumInfor(ManagerPerformmance managerPerformmance);
	
	//指定支行当天进度
	ManagerPerformmance findDeptTodayPerformmanceById(@Param(value="orgId") String id);
	//总行当天进度
	ManagerPerformmance findDeptTodaySumPerformmanceById();
	
	
	//统计当天的申请拒绝数
	int queryRefuse(@Param(value="userId") String userId);
	//统计当天的申请数
	int queryApply(@Param(value="userId") String userId);
	//统计当天的内审次数
	int queryAudit(@Param(value="userId") String userId);
	//统计当天的上会数
	int queryWill(@Param(value="userId") String userId);
	//统计当天的通过数
	int queryPass(@Param(value="userId") String userId);
	//根据客户经理id和日期查询进度
	ManagerPerformmance finManagerPerformmanceByIdAndDate(@Param(value="managerId")String managerId, @Param(value="reportDate")Date reportDate);
	//查询业绩进度排名
	List<ManagerPerformmance> findSumPerformmanceRanking(@Param(value="orgId")String orgId,
															 @Param(value="startDate")String satrtDate,
															 @Param(value="endDate") String endDate,
															 @Param(value="classes")String classes);
	//根据Id获取客户经理姓名
	String findmanagerName(@Param(value="managerId")String managerId);
	//查询新客户经理
	List<ManagerPerformmanceForm>selectNewManager();
	//查询新客户经理的基本业绩
	ManagerPerformmanceForm selectManagerYj(@Param(value="id")String id);
	 //添加新客户经理的业绩总数
	void insertALLManager(ManagerPerformmanceForm from);
	//每日更新客户经理的业绩
	void updateManagerDate(ManagerPerformmanceForm from);
	//查询所有客户经理
	List<ManagerPerformmanceForm>selectAllManager();
	//客户经理手动录入业绩
	void updateManagerDateByUser(ManagerPerformmance from);
	//查询所有的机构，团队
	List<ManagerPerformmanceForm>selectAllManegerTeam();
	//查询指定团队客户经理的业绩信息
	List<ManagerPerformmanceForm>selectAllManegerYj(ManagerPerformmanceForm from);
	//查询所有的机构，团队 总业绩
	ManagerPerformmanceForm selectAllTeamYj(ManagerPerformmanceForm from);
	//查询当前客户经理所属团队和区域
	List<ManagerPerformmanceForm>selectManagerTeam(@Param(value="id")String id);
	//查询指定团队的客户经理ID
	List<ManagerPerformmanceForm>selectAllManagerByTeam(ManagerPerformmanceForm ManagerPerformmanceForm);
	//查询指定区域的团队名称
	List<ManagerPerformmanceForm>selectAllManagerByOrgTeam(ManagerPerformmanceForm ManagerPerformmanceForm);
}
