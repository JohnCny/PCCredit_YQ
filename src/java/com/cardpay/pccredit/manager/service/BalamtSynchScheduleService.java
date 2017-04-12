package com.cardpay.pccredit.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import com.cardpay.pccredit.manager.dao.ManagerSalaryDao;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;

/**
 * TODO
 * @Describe 贷款余额每日计算统计计算
 * @author   songchen
 * @time     2017年2月6日 14:38:11
 */
@Service
public class BalamtSynchScheduleService {
	
	//private Logger logger = Logger.getLogger(LxSynchScheduleService.class);
	
	
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private ManagerSalaryDao managerSalaryDao;
	
	@Autowired
	private PlatformTransactionManager txManager;
	
	
	 public void dosynchyxyeMethod(){
		 
	 }
	 
}
