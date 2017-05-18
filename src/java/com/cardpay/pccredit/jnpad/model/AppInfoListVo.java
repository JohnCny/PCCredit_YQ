package com.cardpay.pccredit.jnpad.model;

/**
 * 客户进件信息
 * @author Administrator
 *
 */
public class AppInfoListVo {
   private int refuseNum;
   private int approvedNum;
 private int returncount;
   private int allcount;
public int getReturncount() {
	return returncount;
}
public void setReturncount(int returncount) {
	this.returncount = returncount;
}
public int getAllcount() {
	return allcount;
}
public void setAllcount(int allcount) {
	this.allcount = allcount;
}
public int getRefuseNum() {
	return refuseNum;
}
public void setRefuseNum(int refuseNum) {
	this.refuseNum = refuseNum;
}
public int getApprovedNum() {
	return approvedNum;
}
public void setApprovedNum(int approvedNum) {
	this.approvedNum = approvedNum;
}
   
}
