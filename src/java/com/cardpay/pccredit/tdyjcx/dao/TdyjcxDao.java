package com.cardpay.pccredit.tdyjcx.dao;

import java.util.List;

import com.cardpay.pccredit.customer.filter.MaintenanceFilter;
import com.cardpay.pccredit.tdyjcx.model.MaintenanceForm;
import com.cardpay.pccredit.tdyjcx.model.ManagerPerformmance;
import com.cardpay.pccredit.tdyjcx.model.Tdyjcx;
import com.wicresoft.util.annotation.Mapper;
@Mapper
public interface TdyjcxDao {

	List<MaintenanceForm> findMaintenancePlansList(MaintenanceFilter filter);

	int findMaintenancePlansCountList(MaintenanceFilter filter);

	List<ManagerPerformmance> finManagerPerformmanceById(MaintenanceFilter filter);

	int fiindcustomercount(MaintenanceFilter filter);

	String findbadAmountsum(MaintenanceFilter filter);

	int finappcount(MaintenanceFilter filter);

	List<Tdyjcx> findappcount(MaintenanceFilter filter);

}
