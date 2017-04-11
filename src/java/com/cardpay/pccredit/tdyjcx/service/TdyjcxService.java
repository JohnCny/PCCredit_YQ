package com.cardpay.pccredit.tdyjcx.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.customer.filter.MaintenanceFilter;
import com.cardpay.pccredit.customeradd.dao.KuglDao;
import com.cardpay.pccredit.customeradd.model.CIPERSONBASINFO;
import com.cardpay.pccredit.manager.web.AccountManagerParameterForm;
import com.cardpay.pccredit.tdyjcx.dao.TdyjcxDao;
import com.cardpay.pccredit.tdyjcx.model.MaintenanceForm;
import com.cardpay.pccredit.tdyjcx.model.ManagerPerformmance;
import com.cardpay.pccredit.tdyjcx.model.Tdyjcx;
import com.wicresoft.jrad.base.database.model.QueryResult;

@Service
public class TdyjcxService {
	@Autowired
	private KuglDao dao;
	@Autowired
	private TdyjcxDao tdyjcxdao;
	
	public List<AccountManagerParameterForm> findcustomermanager() {
		// TODO Auto-generated method stub
		return dao.findcustomermanager();
	}
	public List<CIPERSONBASINFO> findcustomerinfo(String cardid) {
		// TODO Auto-generated method stub
		return dao.fincustomerinfo(cardid);
	}
	public QueryResult<MaintenanceForm> findTdyjcxList(MaintenanceFilter filter) {
		// TODO Auto-generated method stub
		List<MaintenanceForm> plans = tdyjcxdao.findMaintenancePlansList(filter);
		int size = tdyjcxdao.findMaintenancePlansCountList(filter);
		QueryResult<MaintenanceForm> qr = new QueryResult<MaintenanceForm>(size,plans);
		return qr;
	}
	public List<ManagerPerformmance> finManagerPerformmanceById(
			MaintenanceFilter filter) {
		// TODO Auto-generated method stub
		return tdyjcxdao.finManagerPerformmanceById(filter);
	}
	public int fiindcustomercount(MaintenanceFilter filter) {
		// TODO Auto-generated method stub
		return tdyjcxdao.fiindcustomercount(filter);
	}
	public String findbadAmountsum(MaintenanceFilter filter) {
		// TODO Auto-generated method stub
		return tdyjcxdao.findbadAmountsum(filter);
	}
	public int finappcount(MaintenanceFilter filter) {
		// TODO Auto-generated method stub
		return tdyjcxdao.finappcount(filter);
	}
	public List<Tdyjcx> findappcount(MaintenanceFilter filter) {
		// TODO Auto-generated method stub
		return tdyjcxdao.findappcount(filter);
	}
}
