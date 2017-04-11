package com.cardpay.pccredit.tdyjcx.model;

import java.util.Date;

public class Tdyjcx {
	private static final long serialVersionUID = 1L;
	//台帐信息
	private String customerId;
	private String chineseName;
	private String customerManagerId;
	private String userName;
	private String maintenanceGoal;
	private String maintenanceWay;
	private String maintenanceDay;
	private String createWay;
	private String endResult;
	private String remarksCreateReason;
	private Date maintenanceEndtime;
	private String maintenancePlanId;
	private String maintenanceResult;
	private String maintenanceStartTime;
	private String maintenanceEndTime;
	
	
	private String productName;
	private String cardId;
	private String actualQuote;
	private String debitWay;
	
	private String appId;
	
	private String displayName;
	private String userId;
	
	private String repayWay;
	private String repayStatus;
	private String productId;
	private String dkye;
	
	//进件信息
	private String user_type;
	private int customercount;
	
	private String manager_id;
	private int visitcount;
	private int applycount;
	private int applyrefuse;
	private int creditcount;
	private int creditrefuse;
	private int realycount;
	private int reportcount;
	private int internalcount;
	private int meetingcout;
	private int passcount;
	private int signcount;
	private int givemoneycount;
	private Date crateday;
	private String reqlmtsum;//贷款总额
	private String balamtsum;//贷款余额总额
	private String dlayamtsum;//逾期金额总额
	private String badAmountsum;//不良金额
	private String zhlv;
	
	
	public String getBadAmountsum() {
		return badAmountsum;
	}
	public void setBadAmountsum(String badAmountsum) {
		this.badAmountsum = badAmountsum;
	}
	public int getCustomercount() {
		return customercount;
	}
	public void setCustomercount(int customercount) {
		this.customercount = customercount;
	}
	public String getReqlmtsum() {
		return reqlmtsum;
	}
	public void setReqlmtsum(String reqlmtsum) {
		this.reqlmtsum = reqlmtsum;
	}
	public String getBalamtsum() {
		return balamtsum;
	}
	public void setBalamtsum(String balamtsum) {
		this.balamtsum = balamtsum;
	}
	public String getDlayamtsum() {
		return dlayamtsum;
	}
	public void setDlayamtsum(String dlayamtsum) {
		this.dlayamtsum = dlayamtsum;
	}
	public String getZhlv() {
		return zhlv;
	}
	public void setZhlv(String zhlv) {
		this.zhlv = zhlv;
	}
	public String getManager_id() {
		return manager_id;
	}
	public void setManager_id(String manager_id) {
		this.manager_id = manager_id;
	}
	public int getVisitcount() {
		return visitcount;
	}
	public void setVisitcount(int visitcount) {
		this.visitcount = visitcount;
	}
	public int getApplyrefuse() {
		return applyrefuse;
	}
	public void setApplyrefuse(int applyrefuse) {
		this.applyrefuse = applyrefuse;
	}
	public int getCreditcount() {
		return creditcount;
	}
	public void setCreditcount(int creditcount) {
		this.creditcount = creditcount;
	}
	public int getCreditrefuse() {
		return creditrefuse;
	}
	public void setCreditrefuse(int creditrefuse) {
		this.creditrefuse = creditrefuse;
	}
	public int getRealycount() {
		return realycount;
	}
	public void setRealycount(int realycount) {
		this.realycount = realycount;
	}
	public int getReportcount() {
		return reportcount;
	}
	public void setReportcount(int reportcount) {
		this.reportcount = reportcount;
	}
	public int getInternalcount() {
		return internalcount;
	}
	public void setInternalcount(int internalcount) {
		this.internalcount = internalcount;
	}
	public int getMeetingcout() {
		return meetingcout;
	}
	public void setMeetingcout(int meetingcout) {
		this.meetingcout = meetingcout;
	}
	public int getPasscount() {
		return passcount;
	}
	public void setPasscount(int passcount) {
		this.passcount = passcount;
	}
	public int getSigncount() {
		return signcount;
	}
	public void setSigncount(int signcount) {
		this.signcount = signcount;
	}
	public int getGivemoneycount() {
		return givemoneycount;
	}
	public void setGivemoneycount(int givemoneycount) {
		this.givemoneycount = givemoneycount;
	}
	public Date getCrateday() {
		return crateday;
	}
	public void setCrateday(Date crateday) {
		this.crateday = crateday;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	public int getApplycount() {
		return applycount;
	}
	public void setApplycount(int applycount) {
		this.applycount = applycount;
	}
	public String getDkye() {
		return dkye;
	}
	public void setDkye(String dkye) {
		this.dkye = dkye;
	}
	public String getRepayWay() {
		return repayWay;
	}
	public void setRepayWay(String repayWay) {
		this.repayWay = repayWay;
	}
	public String getRepayStatus() {
		return repayStatus;
	}
	public void setRepayStatus(String repayStatus) {
		this.repayStatus = repayStatus;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getActualQuote() {
		return actualQuote;
	}
	public void setActualQuote(String actualQuote) {
		this.actualQuote = actualQuote;
	}
	public String getDebitWay() {
		return debitWay;
	}
	public void setDebitWay(String debitWay) {
		this.debitWay = debitWay;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMaintenancePlanId() {
		return maintenancePlanId;
	}
	public void setMaintenancePlanId(String maintenancePlanId) {
		this.maintenancePlanId = maintenancePlanId;
	}
	public String getMaintenanceResult() {
		return maintenanceResult;
	}
	public void setMaintenanceResult(String maintenanceResult) {
		this.maintenanceResult = maintenanceResult;
	}
	
	public String getMaintenanceStartTime() {
		return maintenanceStartTime;
	}
	public void setMaintenanceStartTime(String maintenanceStartTime) {
		this.maintenanceStartTime = maintenanceStartTime;
	}
	public String getMaintenanceEndTime() {
		return maintenanceEndTime;
	}
	public void setMaintenanceEndTime(String maintenanceEndTime) {
		this.maintenanceEndTime = maintenanceEndTime;
	}
	public Date getMaintenanceEndtime() {
		return maintenanceEndtime;
	}
	public void setMaintenanceEndtime(Date maintenanceEndtime) {
		this.maintenanceEndtime = maintenanceEndtime;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getChineseName() {
		return chineseName;
	}
	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}
	public String getCustomerManagerId() {
		return customerManagerId;
	}
	public void setCustomerManagerId(String customerManagerId) {
		this.customerManagerId = customerManagerId;
	}
	public String getMaintenanceGoal() {
		return maintenanceGoal;
	}
	public void setMaintenanceGoal(String maintenanceGoal) {
		this.maintenanceGoal = maintenanceGoal;
	}
	public String getMaintenanceWay() {
		return maintenanceWay;
	}
	public void setMaintenanceWay(String maintenanceWay) {
		this.maintenanceWay = maintenanceWay;
	}
	public String getMaintenanceDay() {
		return maintenanceDay;
	}
	public void setMaintenanceDay(String maintenanceDay) {
		this.maintenanceDay = maintenanceDay;
	}
	public String getCreateWay() {
		return createWay;
	}
	public void setCreateWay(String createWay) {
		this.createWay = createWay;
	}
	public String getEndResult() {
		return endResult;
	}
	public void setEndResult(String endResult) {
		this.endResult = endResult;
	}
	public String getRemarksCreateReason() {
		return remarksCreateReason;
	}
	public void setRemarksCreateReason(String remarksCreateReason) {
		this.remarksCreateReason = remarksCreateReason;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
}
