package com.cardpay.pccredit.manager.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import com.cardpay.pccredit.manager.dao.ManagerSalaryDao;
import com.cardpay.pccredit.manager.model.InComeStateMentDay;
import com.cardpay.pccredit.manager.model.InComeStateMentHistory;
import com.cardpay.pccredit.manager.model.TMibusidata;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;

/**
 * @Describe 每日利息统计计算
 * @author   songchen
 * @Time     2017年1月11日 16:49:09
 */
@Service
public class LxSynchScheduleService {
	
	//private Logger logger = Logger.getLogger(LxSynchScheduleService.class);
	
	
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private ManagerSalaryDao managerSalaryDao;
	
	@Autowired
	private PlatformTransactionManager txManager;
	
	 /**
	  * 1.日终跑批计算全部收息记录
	  *   只计算一次 
	  */
	 public void dosynchyxyeMethod(){
		 /**
		  * 每日跑批之后计算  第二天查询数据为 date-2 的数据
		  * 1.收息历史表 计入上一最新的收息   2.收息表记录每日的收息记录  当日无收息 记0
		  * 首先distinct 台帐表
		  * 1.select distinct(t.BUSICODE) from t_mibusidata t
		  * 2.for循环所有台帐业务编号 
		  */
		 
	     // 利息
		 BigDecimal lx = new BigDecimal("0");
		 
		 // 历史收息值
		 BigDecimal Hispaydebt = new BigDecimal("0");
		 
		 // 每日增加的收息  
		 BigDecimal addPaydebt = new BigDecimal("0");
		 
		 // 查询busicode
		 List<TMibusidata> alist = commonDao.queryBySql(TMibusidata.class,"select distinct (trim(t.busicode)) as busicode from t_mibusidata t" ,null);
		 
		 for(TMibusidata m:alist){
			 String paydebtSql =     "select t.busicode,			           "+
									 "       t.custid,                         "+
									 "       t.cname,                          "+
									 "       t.custidno,                       "+
									 "       t.instcode,                       "+
									 "       t.deptcode,                       "+
									 "       nvl(t.contractmoney,0),           "+
									 "       nvl(t.reqlmt,0),                  "+
									 "       t.busimanager,                    "+
									 "       t.assistbusimanager,              "+
									 "       t.loandate,                       "+
									 "       t.startdate,                      "+
									 "       t.enddate,                        "+
									 "       t.limit,                          "+
									 "       t.interest,                       "+
									 "       nvl(t.money,0),                   "+
									 "       nvl(t.balamt,0),                  "+
									 "       t.accountstate,                   "+
									 "       nvl(t.paydebt,0) as paydebt,      "+
									 "       t.operdatetime                    "+
									 "  from t_mibusidata t                    "+
								   //" where trim(busicode) = '901LN000650510' "+
									 " where trim(busicode) = '"+m.getBusicode()+"' "+
									 " order by operdatetime asc               ";
			 List<TMibusidata> blist = commonDao.queryBySql(TMibusidata.class,paydebtSql ,null);
			
			 for(TMibusidata mi : blist){
				 
				// 查询收息历史表有无记录
				int count = this.findIsCountExist(mi.getBusicode().trim());
				
				if(count > 0){
					// 查询历史表的收息值
					Hispaydebt = this.findSxHistory(mi.getBusicode().trim());
					
					// 取收息记录
					lx = mi.getPaydebt()==null ? new BigDecimal("0") : mi.getPaydebt();
					
					// 更新历史收息表 利息
					updateSxHistory(lx,mi.getOperdatetime(),mi.getBusicode().trim());
					
					// 新增 收息表
				    addPaydebt = lx.subtract(Hispaydebt);
					mi.setPaydebt(addPaydebt);
					insertIntoIncomeDay(mi);
				}else{
					// 新增收息历史
					insertIntoIncomeHistory(mi);
					
					// 新增收息表记录   
					insertIntoIncomeDay(mi);
				}
			 }
			 
		 }
	 }
	 
	 /**
	  * 查询历史收息表记录是否存在
	  */
	 public int findIsCountExist(String busicode){
		 return managerSalaryDao.findIsCountExist(busicode);
	 }
	 
	 /**
	  * 查询历史表的收息值
	  */
	 public BigDecimal findSxHistory(String busicode){
		String sql = "select nvl(PAYDEBT,0) as PAYDEBT  from t_income_statement_history t where trim(BUSICODE) = '"+busicode+"'";
		List<TMibusidata> list = commonDao.queryBySql(TMibusidata.class, sql, null);
		if(list!=null&&!list.isEmpty()){
			if(list.get(0).getPaydebt()!=null){
				return list.get(0).getPaydebt();
			}else {
				return new BigDecimal("0");
			}
		}else{
			return new BigDecimal("0");
		}
	  }
	 
	 
	 /**
	  * 更新历史收息表 利息
	  */
	  public void updateSxHistory(BigDecimal lx,String time,String busicode){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("lx", lx);
		params.put("time", time);
		params.put("busicode", busicode);
		String sql = "update t_income_statement_history set PAYDEBT=#{lx},operdatetime =#{time} where trim(BUSICODE)=#{busicode}";
		commonDao.queryBySql(sql, params);
	}
	
	 /**
	  * 新增收息历史表记录
	  */
	  public void insertIntoIncomeHistory(TMibusidata mi){
		 InComeStateMentHistory day = new InComeStateMentHistory();
		 day.setBusicode(mi.getBusicode().trim());//业务编号
		 day.setCustid(mi.getCustid());//客户编号
		 day.setCname(mi.getCname());//客户名称
		 day.setCustidno(mi.getCustidno());//客户证件号（核心）
		 day.setInstcode(mi.getInstcode());//所属机构
		 day.setDeptcode(mi.getDeptcode());//所属部门
		 day.setContractmoney(mi.getContractmoney());//合同金额
		 day.setReqlmt(mi.getReqlmt());//授信金额
		 day.setBusimanager(mi.getBusimanager());//主办客户经理
		 day.setAssistbusimanager(mi.getAssistbusimanager());//协办客户经理
		 day.setLoandate(mi.getLoandate());//发放日期（核心）
		 day.setStartdate(mi.getStartdate());//起始日期
		 day.setEnddate(mi.getEnddate());//现到期日/展期到期日（核心）
		 day.setLimit(mi.getLimit());//贷款期限(月)（核心）
		 day.setInterest(mi.getInterest());//利率
		 day.setMoney(mi.getMoney());//发放金额（核心）
		 day.setBalamt(mi.getBalamt());//贷款余额（核心）
		 day.setAccountstate(mi.getAccountstate());//账户状态（核心）
		 day.setPaydebt(mi.getPaydebt());//已收利息
		 day.setOperdatetime(mi.getOperdatetime());//操作时间
		 commonDao.insertObject(day);
	 }
	 
	 /**
	  * 新增收息表
	  */
	  public void insertIntoIncomeDay(TMibusidata mi){
		 InComeStateMentDay day = new InComeStateMentDay();
		 day.setBusicode(mi.getBusicode().trim());//业务编号
		 day.setCustid(mi.getCustid());//客户编号
		 day.setCname(mi.getCname());//客户名称
		 day.setCustidno(mi.getCustidno());//客户证件号（核心）
		 day.setInstcode(mi.getInstcode());//所属机构
		 day.setDeptcode(mi.getDeptcode());//所属部门
		 day.setContractmoney(mi.getContractmoney());//合同金额
		 day.setReqlmt(mi.getReqlmt());//授信金额
		 day.setBusimanager(mi.getBusimanager());//主办客户经理
		 day.setAssistbusimanager(mi.getAssistbusimanager());//协办客户经理
		 day.setLoandate(mi.getLoandate());//发放日期（核心）
		 day.setStartdate(mi.getStartdate());//起始日期
		 day.setEnddate(mi.getEnddate());//现到期日/展期到期日（核心）
		 day.setLimit(mi.getLimit());//贷款期限(月)（核心）
		 day.setInterest(mi.getInterest());//利率
		 day.setMoney(mi.getMoney());//发放金额（核心）
		 day.setBalamt(mi.getBalamt());//贷款余额（核心）
		 day.setAccountstate(mi.getAccountstate());//账户状态（核心）
		 day.setPaydebt(mi.getPaydebt());//已收利息
		 day.setOperdatetime(mi.getOperdatetime());//操作时间
		 commonDao.insertObject(day);
	}
	
	  
	  
	  
	  
	  

//============================================================================================================================//
	  
	/**
	 * 2.每日日终跑批  计算每日利息
	 * 
	 */
	  public void doyxyeMethod(){
			 /**
			  * 每日跑批之后计算  第二天查询数据为 date-2 的数据
			  * 1.收息历史表 计入上一最新的收息   2.收息表记录每日的收息记录  当日无收息 记0
			  * 首先distinct 台帐表
			  * 1.select distinct(t.BUSICODE) from t_mibusidata t
			  * 2.for循环所有台帐业务编号 
			  */
			 
		     // 利息
			 BigDecimal lx = new BigDecimal("0");
			 
			 // 历史收息值
			 BigDecimal Hispaydebt = new BigDecimal("0");
			 
			 // 每日增加的收息  
			 BigDecimal addPaydebt = new BigDecimal("0");
			 
			
			 
			String paydebtSql =          "select t.busicode,			           "+
										 "       t.custid,                         "+
										 "       t.cname,                          "+
										 "       t.custidno,                       "+
										 //"       t.instcode,                       "+
										 "       t.deptcode,                       "+
										 //"       nvl(t.contractmoney,0),           "+
										 "       nvl(t.reqlmt,0),                  "+
										 "       t.busimanager,                    "+
									   //"       t.assistbusimanager,              "+
										 "       t.loandate,                       "+
										 "       t.startdate,                      "+
										 "       t.enddate,                        "+
										 "       t.limit,                          "+
										 "       t.interest,                       "+
										 "       nvl(t.money,0),                   "+
										 "       nvl(t.balamt,0),                  "+
										 "       t.accountstate,                   "+
										 "       nvl(t.paydebt,0) as paydebt,      "+
										 "       t.operdatetime                    "+
										 "  from t_mibusidata_view t               ";
									   //" where trim(busicode) = '901LN000650510' "+
									   // " order by operdatetime asc               ";
				 List<TMibusidata> blist = commonDao.queryBySql(TMibusidata.class,paydebtSql ,null);
				
				 for(TMibusidata mi : blist){
					 
					// 查询收息历史表有无记录
					int count = this.findIsCountExist(mi.getBusicode().trim());
					
					if(count > 0){
						// 查询历史表的收息值
						Hispaydebt = this.findSxHistory(mi.getBusicode().trim());
						
						// 取收息记录
						lx = mi.getPaydebt()==null ? new BigDecimal("0") : mi.getPaydebt();
						
						// 更新历史收息表 利息
						updateSxHistory(lx,mi.getOperdatetime(),mi.getBusicode().trim());
						
						// 新增 收息表
					    addPaydebt = lx.subtract(Hispaydebt);
						mi.setPaydebt(addPaydebt);
						insertIntoIncomeDay(mi);
					}else{
						// 新增收息历史
						insertIntoIncomeHistory(mi);
						
						// 新增收息表记录   
						insertIntoIncomeDay(mi);
					}
				 }
		 }
	 
}
