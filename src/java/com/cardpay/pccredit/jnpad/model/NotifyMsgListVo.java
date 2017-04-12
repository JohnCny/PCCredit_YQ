package com.cardpay.pccredit.jnpad.model;

import java.util.List;

import com.cardpay.pccredit.customer.model.CIPERSONBASINFOCOPY;
import com.cardpay.pccredit.customer.model.CustomerInfor;

/**
 * 通知
 * @author songchen
 *
 */
public class NotifyMsgListVo {
	private int shendaihui;/*审贷会通知*/
	private int yuanshiziliao;/*客户原始资料变更通知*/
	private int peixun;///*培训通知*/ 
	private int kaocha;/*考察成绩通知*/
	private int qita;/*其他通知*/
	
	private int refuseCount;/*拒绝进件数量*/
	private int returnCount;/*补充调查*/
	private int risk;/*风险客户数量*/
	private int sum;/*总和*/
	
	private int ziliaobiangeng;
	private List<CustomerInfor> bianggeng;
	
	
	
	public List<CustomerInfor> getBianggeng() {
		return bianggeng;
	}
	public void setBianggeng(List<CustomerInfor> bianggeng) {
		this.bianggeng = bianggeng;
	}
	public int getZiliaobiangeng() {
		return ziliaobiangeng;
	}
	public void setZiliaobiangeng(int ziliaobiangeng) {
		this.ziliaobiangeng = ziliaobiangeng;
	}

	
	public int getSum() {
		return sum;
	}
	public void setSum(int sum) {
		this.sum = sum;
	}
	public int getRisk() {
		return risk;
	}
	public void setRisk(int risk) {
		this.risk = risk;
	}
	public int getRefuseCount() {
		return refuseCount;
	}
	public void setRefuseCount(int refuseCount) {
		this.refuseCount = refuseCount;
	}
	public int getReturnCount() {
		return returnCount;
	}
	public void setReturnCount(int returnCount) {
		this.returnCount = returnCount;
	}
	public int getShendaihui() {
		return shendaihui;
	}
	public void setShendaihui(int shendaihui) {
		this.shendaihui = shendaihui;
	}
	public int getYuanshiziliao() {
		return yuanshiziliao;
	}
	public void setYuanshiziliao(int yuanshiziliao) {
		this.yuanshiziliao = yuanshiziliao;
	}
	public int getPeixun() {
		return peixun;
	}
	public void setPeixun(int peixun) {
		this.peixun = peixun;
	}
	public int getKaocha() {
		return kaocha;
	}
	public void setKaocha(int kaocha) {
		this.kaocha = kaocha;
	}
	public int getQita() {
		return qita;
	}
	public void setQita(int qita) {
		this.qita = qita;
	}




}
