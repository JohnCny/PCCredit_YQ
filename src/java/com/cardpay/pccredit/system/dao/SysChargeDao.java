package com.cardpay.pccredit.system.dao;

import java.util.List;

import com.cardpay.pccredit.system.filter.SysLoginLogFilter;
import com.cardpay.pccredit.system.filter.SystemChargeFilter;
import com.cardpay.pccredit.system.model.SystemCharge;
import com.cardpay.pccredit.system.model.SystemUser;
import com.wicresoft.jrad.modules.log.model.LoginLog;
import com.wicresoft.util.annotation.Mapper;

@Mapper
public interface SysChargeDao {
	
	public List<SystemUser> findUserByFilter(SystemChargeFilter filter);
	public int findUserCountByFilter(SystemChargeFilter filter);
	public List<SystemCharge> findChargeByFilter(SystemChargeFilter filter);
	public SystemCharge findCharge(SystemCharge charge);
	
	public List<LoginLog> findLoginLogByFilter(SysLoginLogFilter filter);
	public int findLoginLogCountByFilter(SysLoginLogFilter filter);
	
	
	public List<LoginLog> findLoginLogList(SysLoginLogFilter filter);
	
}
