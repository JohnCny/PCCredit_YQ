/**
 * 
 */
package com.cardpay.pccredit.manager.service;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.customer.model.CustomerInfor;
import com.cardpay.pccredit.intopieces.constant.Constant;
import com.cardpay.pccredit.intopieces.model.IntoPieces;
import com.cardpay.pccredit.intopieces.service.IntoPiecesService;
import com.cardpay.pccredit.manager.model.REIMBURSEMENT;
import com.cardpay.pccredit.postLoan.model.MibusidataForm;
import com.cardpay.pccredit.system.model.SystemUser;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;

/**
 * 描述 ：同步系统中的进件的statusservice
 * @author 宋辰
 */
@Service
public class CustomerApplicationInfoSynchScheduleService {
	

	private Logger logger = Logger.getLogger(CustomerApplicationInfoSynchScheduleService.class);
	
	@Autowired
	private IntoPiecesService intoPiecesService;
	
	@Autowired
	private AccountManagerParameterService accountManagerParameterService;
	
	@Autowired
	private CommonDao commonDao;
	
	
	/**
	 * 济南
	 * 同步进件状态 
	 * 已放款
	 * 贷款已结清
	 */
	public void dosynchJnCustAppInfoMethod(){
		//获取今日日期
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		String dateString = format.format(new Date());
		
		logger.info(dateString+"进件状态更新开始（已放款）**********");
		List<IntoPieces> intoPiecesList = intoPiecesService.findCustomerApplicationInfoJnFK();
		for(IntoPieces intoPieces:intoPiecesList){
			IntoPieces  pieces = new IntoPieces();
			pieces.setStatus(Constant.END);//已放款
			pieces.setActualAmt(intoPieces.getAmt());//批准金额
			pieces.setId(intoPieces.getId());
			intoPiecesService.updateCustomerApplicationInfoJn(pieces);
		}
		logger.info(dateString+"进件状态更新结束（已放款）**********");
		
		
		//2016-12-21
		logger.info(dateString+"进件状态更新开始（已还清）**********");
		List<IntoPieces> list = intoPiecesService.findCustomerApplicationInfoJnHQ();
		for(IntoPieces intoPieces:list){
			IntoPieces  pieces = new IntoPieces();
			pieces.setRepayStatus("1");//1-已还清
			pieces.setId(intoPieces.getId());
			intoPiecesService.updateCustomerApplicationInfoJn(pieces);
		}
		logger.info(dateString+"进件状态更新结束（已还清）**********");
	}
	

	
	
	
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	

	/**
	 * 同步进件状态(更新为已放款)
	 * @throws IOException 
	 */
	/*private void dosynchMethod() throws IOException{
		//获取今日日期
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		String dateString = format.format(new Date());
		logger.info(dateString+"进件状态更新开始（已放款）**********");
		//查询已经审核通过的进件信息
		List<IntoPieces> intoPiecesList = intoPiecesService.findCustomerApplicationInfo();
		for(IntoPieces intoPieces:intoPiecesList){
			//更新进件申请表 进件状态 status、借据号关联
			IntoPieces  pieces = new IntoPieces();
			pieces.setStatus(Constant.END);//放款成功
			pieces.setId(intoPieces.getId());
			pieces.setJjh(intoPieces.getJjh());
			pieces.setJkrq(intoPieces.getJkrq());
			intoPiecesService.updateCustomerApplicationInfo(pieces);
		}
		logger.info(dateString+"进件状态更新结束（已放款）**********");
	}*/
	
/*	
	private void dosynchMethodEnd() throws IOException{
		//获取今日日期
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		String dateString = format.format(new Date());
		logger.info(dateString+"进件状态更新开始（还款结束）**********");
		//查询已经审核通过的进件信息
		List<IntoPieces> intoPiecesList = intoPiecesService.findCustomerApplicationInfo();
		for(IntoPieces intoPieces:intoPiecesList){
			//更新进件申请表 进件状态 status
			IntoPieces  pieces = new IntoPieces();
			pieces.setCustomerId(intoPieces.getCustomerId());
			pieces.setStatus(Constant.END);//放款成功
			pieces.setProductId(intoPieces.getProductId());
			intoPiecesService.updateCustomerApplicationInfo(pieces);
		}
		logger.info(dateString+"进件状态更新结束（还款结束）**********");
	}*/
	
	/**
	 * 每月定时计算客户经理管户、主调、辅调等信息
	 */
	/*private void monthParmter() throws Exception{
		//获取所有客户经理
		List<AccountManagerParameterForm> accountList = accountManagerParameterService.findAccountManagerParameterAll();
	      //yyyyMMdd格式
		DateFormat format = new SimpleDateFormat("yyyyMM");
		Calendar c = Calendar.getInstance(); //获得当前时间
		c.add(Calendar.MONTH, -1); //减一,就是上一个月
		//获得上月时间（yyyyMM格式）
		String month = format.format(c.getTime());
		//获取上月进件总数
		String sql = "select a.id,b.user_id ,a.actual_quote  from customer_application_info a left join basic_customer_information b on a.customer_id=b.id where SUBSTR(a.jkrq, 1, 6)='"+month+"'";
		List<IntoPieces> appList = commonDao.queryBySql(IntoPieces.class, sql, null);
		//获取上月进件余额不为0（管户数）
		String tubesSql = "select a.id,c.user_id  from CUSTOMER_APPLICATION_INFO a LEFT JOIN TY_REPAY_YEHZ b on a.JJH=b.JJH  LEFT JOIN BASIC_CUSTOMER_INFORMATION c on a.customer_id=c.id where a.STATUS='end' and b.dkye!='0.00'";
		List<IntoPieces> tubesList = commonDao.queryBySql(IntoPieces.class, tubesSql, null);
		//循环计算客户经理当月工资
		for(int i=0;i<accountList.size();i++){
			//客户经理id
			String userId = accountList.get(i).getUserId();
			//主调数
			int zdCount =0;
			//管户数
			int tubesCount =0;
			//辅调数
			int fdCount =0;
			//审批数
			int spCount =0;
			//完成折算笔数(主调笔数折算)
			int competerCount=0;
			for(int j=0;j<appList.size();j++){
				if(appList.get(j).getUserId().equals(userId)){
					zdCount++;
					competerCount+=countCompeter(appList.get(j).getActualQuote());
				}
			}
			for(int j=0;j<tubesList.size();j++){
				if(tubesList.get(j).getUserId().equals(userId)){
					tubesCount++;
				}
			}
			//获取辅调数
			List<TyApplicationLog> fdList = commonDao.queryBySql(TyApplicationLog.class, "select * from TY_APPLICATION_LOG  where type='2' and APPLICATION_ID in (select id from CUSTOMER_APPLICATION_INFO where jkrq='"+month+"')", null);
			fdCount = fdList.size();
			//获取审批数
			List<TyApplicationLog> spList = commonDao.queryBySql(TyApplicationLog.class, "select * from TY_APPLICATION_LOG  where type='1' and APPLICATION_ID in (select id from CUSTOMER_APPLICATION_INFO where jkrq='"+month+"')", null);
			spCount = spList.size();
			
			//保存
			ManagerSalaryParameter parameter = new ManagerSalaryParameter();
			parameter.setId(IDGenerator.generateID());
			parameter.setMonth(month);
			parameter.setUserId(userId);
			parameter.setZdCount(competerCount+"");//主调折算
			parameter.setFdCount(fdCount+"");
			parameter.setSpCount(spCount+"");
			parameter.setTubes(tubesCount+"");
			parameter.setCompeterCount(zdCount+"");//完成笔数不折算，按实际笔数
			commonDao.insertObject(parameter);
		}
}*/
	
	/*
	 * 笔数折算
	 */
	/*private int countCompeter(String quote){
		Double amount = Double.parseDouble(quote);
		if(5000<amount&&amount<100000){
			return 1;
		}else if(amount>=100000&&amount<300000){
			return 2;
		}else if(amount>=300000&&amount<500000){
			return 3;
		}else if(amount>=500000&&amount<1000000){
			return 4;
		}else{
			return 6;
		}
	}
	*/
	
	//================================================济南项目=======================================//
	/**
	 * 还款提醒 不包含随借随还的贷款
	 * 批处理 每月月初生成 还款提醒记录
	 * @author songchen 
	 * @datetime 2016-10-09 下午 14:00:00
	 * 批处理自动
	 */
	public void reimbursement(){
		/**
		 * 1.定期结息，到期日利随本清（即按月付息 到期还本） 每月20号扣款  25再扣一次 ,如果是循环的 随借随还  还使用期限的利息和本金
		 * 2.等额本息 例:10月7号放款 下月 10月7日扣款  客户经理需提前两三天提醒客户还款 提前存入指定账户
		 */
		
		//获取今日日期
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		String dateString = format.format(new Date());
		logger.info(dateString+"还款提醒生成数据start**********");
		
		//1.查询未结清的贷款且非循环的贷款 2.循环的贷款可以随借随还难以控制
		String sql = "select * from t_mibusidata_view  where trim(ACCOUNTSTATE) != '5' and trim(USEMODE) ='0001'";
		List<MibusidataForm> lists = commonDao.queryBySql(MibusidataForm.class,sql, null);
		
		
		for(MibusidataForm mibu:lists){
			//台帐号
			String busiCode = mibu.getBusicode();
			
			//贷款发放日期
			String loandate = mibu.getLoandate();
			
			//利率
			String lv = mibu.getInterest().toString();
			
			//贷款期限 
			String qx = mibu.getLimit().toString();
			
			//发放金额（核心）
			String money = mibu.getMoney().toString();
			
			//还款方法(01-定期结息，到期日利随本清;03-等额本息;)    
			String repayMethod = mibu.getRepaymethod();
			
			//还款日期
			String  repaydate ="";
			
			//还款本金
			String repaybj ="";
			
			//还款利息
			String repaylx ="";
			
			//计算使用天数
			String day="";
			
			//等额本息 还款总额
			String repayZe ="";
			
			/**
			 * 1).等额本息 ：
			 * 例：贷款发放日 10.07 下次还款日期 11.07
			 * 2).按月付息 到期还本 
			 * 1.22前的贷款   例：贷款发放日期10月07日     下次还款日  10月20日  计息周期（10月7日  至  10月20日） 这个时间段  下次 还款日期11月20日（10月20日  下次还款日  12月20日）
			 * 2.22后的贷款   例：贷款发放日期10月23日     下次还款日  11月20日  计息周期（10月23日  至  11月20日） 这个时间段  下次 还款日期12月20日（11月20日  下次还款日  12月20日）
			 */
			if("03".equals(repayMethod)){//等额本息
				repaydate = getDeBx(dateString,loandate);
				day = "31";
				repayZe = getAmt(money,lv,qx);
				
			}else{//按月付息,到期还本
				if(loandate.substring(0, 4).equals(dateString.substring(0, 4))
				 &&loandate.substring(5, 7).equals(dateString.substring(4, 6))){//当年当月的放款
					//TODO 按月付息 放款日期不固定 跑批时间难设定
				}else{
					day = "31";
					repaydate = getAnyueBx2(dateString);
					repaybj ="";//一年清一次本金
					repaylx = gotLx(money, lv,day);
				}
			}
			
			/**
			 * 查询客户经理 id name
			 */
			String customerManagerId ="";
			String customerManagerName ="";
			String mysql = "select *                                                             "+
						   "  from sys_user                                                      "+
						   " where id = (select t.USER_ID                                        "+
						   "               from basic_customer_information t          			 "+
						   "              where t.ty_customer_id = '"+mibu.getCustid()+"')       ";
			List<SystemUser> alist = commonDao.queryBySql(SystemUser.class,mysql, null);
			
			if(alist !=null&&alist.size()>0){
				customerManagerId=alist.get(0).getId();
				customerManagerName=alist.get(0).getDisplayName();
			}
			
			/**
			 * 插入还款计划提醒表
			 */
			REIMBURSEMENT re = new REIMBURSEMENT();
			re.setCustomerId(mibu.getCustid());//ty_customer_id
			re.setCustomerName(mibu.getCname());
			re.setCustomerManagerId(customerManagerId);
			re.setCustomerManagerName(customerManagerName);
			re.setLoandate(loandate);
			re.setMoney(money);
			re.setLv(lv);
			re.setRepayTime(repaydate);
			re.setRepayBj(repaybj);
			re.setRepayLx(repaylx);
			re.setRepayMethod(repayMethod);
			re.setRepayMzee(repayZe);
			re.setBusiCode(busiCode);
			re.setHasTell("0");
			commonDao.insertObject(re);
		}
		//succ
		//accountManagerParameterService.updBatchTaskFlow("100","hk",dateString);
		logger.info(dateString+"还款提醒生成数据end**********");
	}
	
	
	
	/**
	 * 还款提醒 不包含随借随还的贷款
	 * 批处理 每月月初生成 还款提醒记录
	 * @author songchen 
	 * @datetime 2016-10-09 下午 14:00:00
	 * 批处理手工
	 */
	public void returnReimbursement(String dateString){
		/**
		 * 1.定期结息，到期日利随本清（即按月付息 到期还本） 每月20号扣款  25再扣一次 ,如果是循环的 随借随还  还使用期限的利息和本金
		 * 2.等额本息 例:10月7号放款 下月 10月7日扣款  客户经理需提前两三天提醒客户还款 提前存入指定账户
		 */
		
		//获取今日日期
		//DateFormat format = new SimpleDateFormat("yyyyMMdd");
		//String dateString = format.format(new Date());
		logger.info(dateString+"还款提醒生成数据start**********");
		
		//1.查询未结清的贷款且非循环的贷款 2.循环的贷款可以随借随还难以控制
		String sql = "select * from t_mibusidata_view  where trim(ACCOUNTSTATE) != '5' and trim(USEMODE) ='0001'";
		List<MibusidataForm> lists = commonDao.queryBySql(MibusidataForm.class,sql, null);
		
		
		for(MibusidataForm mibu:lists){
			//台帐号
			String busiCode = mibu.getBusicode();
			
			//贷款发放日期
			String loandate = mibu.getLoandate();
			
			//利率
			String lv = mibu.getInterest().toString();
			
			//贷款期限 
			String qx = mibu.getLimit().toString();
			
			//发放金额（核心）
			String money = mibu.getMoney().toString();
			
			//还款方法(01-定期结息，到期日利随本清;03-等额本息;)    
			String repayMethod = mibu.getRepaymethod();
			
			//还款日期
			String  repaydate ="";
			
			//还款本金
			String repaybj ="";
			
			//还款利息
			String repaylx ="";
			
			//计算使用天数
			String day="";
			
			//等额本息 还款总额
			String repayZe ="";
			
			/**
			 * 1).等额本息 ：
			 * 例：贷款发放日 10.07 下次还款日期 11.07
			 * 2).按月付息 到期还本 
			 * 1.22前的贷款   例：贷款发放日期10月07日     下次还款日  11月20日  计息周期（10月7日  至  11月20日） 这个时间段  下次 还款日期12月20日（11月20日  下次还款日  12月20日）
			 * 2.22后的贷款   例：贷款发放日期10月23日     下次还款日  11月20日  计息周期（10月23日  至  11月20日） 这个时间段  下次 还款日期12月20日（11月20日  下次还款日  12月20日）
			 */
			if("03".equals(repayMethod)){//等额本息
				repaydate = getDeBx(dateString,loandate);
				day = "31";
				repayZe = getAmt(money,lv,qx);
				
			}else{//按月付息,到期还本
				if(loandate.substring(0, 4).equals(dateString.substring(0, 4))
				 &&loandate.substring(5, 7).equals(dateString.substring(4, 6))){
					//TODO 按月付息 放款日期不固定 跑批时间难设定
				}else{
					day = "31";
					repaydate = getAnyueBx2(dateString);
					repaybj ="";//一年清一次本金
					repaylx = gotLx(money, lv,day);
				}
			}
			
			/**
			 * 查询客户经理 id name
			 */
			String customerManagerId ="";
			String customerManagerName ="";
			String mysql = "select *                                                             "+
						   "  from sys_user                                                      "+
						   " where id = (select t.USER_ID                                        "+
						   "               from basic_customer_information t          			 "+
						   "              where t.ty_customer_id = '"+mibu.getCustid()+"')       ";
			List<SystemUser> alist = commonDao.queryBySql(SystemUser.class,mysql, null);
			
			if(alist !=null&&alist.size()>0){
				customerManagerId=alist.get(0).getId();
				customerManagerName=alist.get(0).getDisplayName();
			}
			
			/**
			 * 插入还款计划提醒表
			 */
			REIMBURSEMENT re = new REIMBURSEMENT();
			re.setCustomerId(mibu.getCustid());//ty_customer_id
			re.setCustomerName(mibu.getCname());
			re.setCustomerManagerId(customerManagerId);
			re.setCustomerManagerName(customerManagerName);
			re.setLoandate(loandate);
			re.setMoney(money);
			re.setLv(lv);
			re.setRepayTime(repaydate);
			re.setRepayBj(repaybj);
			re.setRepayLx(repaylx);
			re.setRepayMethod(repayMethod);
			re.setRepayMzee(repayZe);
			re.setBusiCode(busiCode);
			re.setHasTell("0");
			commonDao.insertObject(re);
		}
		//succ
		accountManagerParameterService.updBatchTaskFlow("100","hk",dateString);
		logger.info(dateString+"还款提醒生成数据end**********");
	}
	
	
	
	/**
	 * 按月付息,到期还款计算还款日
	 * loandate 2016-09-11
	 */
	
	public String getAnyueBx(String dateString){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(dateString.substring(0, 4)),
				     Integer.parseInt(dateString.substring(5, 7))-1, 
				     Integer.parseInt(dateString.substring(8, 10)));
	    calendar.add(Calendar.MONTH, 1);
	    String year_1 = calendar.get(Calendar.YEAR)+"";
		String month_1 = calendar.get(Calendar.MONTH)+1+"";
		
		if(month_1.length()==1){
			month_1 = "0"+month_1;
		}
		
		//获得当月的还款日期
		String repaydate = year_1+"-"+month_1+"-"+"20";
		
		return repaydate;
	}
	
	/**
	 * 按月付息,到期还款计算还款日
	 * loandate 20160911
	 */
	
	public String getAnyueBx2(String dateString){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(dateString.substring(0, 4)),
				     Integer.parseInt(dateString.substring(4, 6))-1, 
				     Integer.parseInt(dateString.substring(6, 8)));
	    //calendar.add(Calendar.MONTH, 1);
	    String year_1 = calendar.get(Calendar.YEAR)+"";
		String month_1 = calendar.get(Calendar.MONTH)+1+"";
		
		if(month_1.length()==1){
			month_1 = "0"+month_1;
		}
		
		//获得当月的还款日期
		String repaydate = year_1+"-"+month_1+"-"+"20";
		
		return repaydate;
	}
	
	
	
	/**
	 * 等额本息还款计算还款日
	 * 2016-09-09
	 * loandate 贷款日期yyyy-MM-dd
	 * dateString 跑批日期 yyyyMMdd
	 */
	
	public String getDeBx(String dateString,String loanDate){
		//获得下个扣款日的年份和月份
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(dateString.substring(0, 4)), 
				     Integer.parseInt(dateString.substring(4, 6))-1, 
				     Integer.parseInt(dateString.substring(6, 8)));
	    //calendar.add(Calendar.MONTH, 1);
	    calendar.add(Calendar.DATE, 0);
	    String year_1 = calendar.get(Calendar.YEAR)+"";
		String month_1 = calendar.get(Calendar.MONTH)+1+"";
		String day_1 = calendar.get(Calendar.DATE)+"";
		
		if(day_1.length()==1){day_1 = "0"+day_1;}
		
		if(month_1.length()==1){month_1 = "0"+month_1;}
		
		//获得当月的还款日期
		String repaydate = year_1+"-"+month_1+"-"+loanDate.substring(8, 10);
		return repaydate;
	}
	
	
	/**
	 * 计算还款利息
	 * bj-本金
	 * lv-利率
	 * day-使用天数
	 */
	public String gotLx(String bj,String lv,String day){
	   /**
		 * if ($('#int-type')[0].selectedIndex == 0) {
		 *		interest = interest / (360*100);
		 *	} else if ($('#int-type')[0].selectedIndex == 1) {
		 *		interest = interest / (30*1000);
		 *	} else if ($('#int-type')[0].selectedIndex == 2) {
		 *		interest = interest / 10000;
		 *	} 
		 */
		
		BigDecimal lx = new BigDecimal("0");
		//日利率
		BigDecimal daliyLv = new BigDecimal(lv).divide(new BigDecimal("36000"),4,BigDecimal.ROUND_HALF_UP);
		//利息
		lx = new BigDecimal(bj).multiply(daliyLv).multiply(new BigDecimal(day)).setScale(2,BigDecimal.ROUND_HALF_UP);
		return lx.toString();
	}







	public int calMistTime(String startDate,String endDate){
		 // 创建一个日历对象。
		 Calendar calendar = Calendar.getInstance();
		
		 // start
		 List<String> startDateList = findYearAndMonthAndDay(startDate);
		 calendar.set(Integer.parseInt(startDateList.get(0)), 
				 	  Integer.parseInt(startDateList.get(1)), 
					  Integer.parseInt(startDateList.get(2))); 
	     long start = calendar.getTimeInMillis();
	     
	     // end
	     List<String> endDateList = findYearAndMonthAndDay(endDate);
	     calendar.set(Integer.parseInt(endDateList.get(0)),
	    		 	  Integer.parseInt(endDateList.get(1)),
	    			  Integer.parseInt(endDateList.get(2))); 
	     long end = calendar.getTimeInMillis();
	     
	     // 时间差
	     long interdays = (end - start) / (1000 * 60 * 60 * 24);
	     return new Long(interdays).intValue();
	 }
	 
	 public static List<String> findYearAndMonthAndDay(String date){
		  List<String>  list = new ArrayList<String>();
		  String year = date.substring(0, 4);
		  String month = date.substring(6, 7);
		  String day = date.substring(8, 10);
		  list.add(year);
		  list.add(month);
		  list.add(day);
		  return list;
	  }
	
	 
	 /**
	  * 等额本息计划还款金额
	  * bj  本金
	  * lv  利率
	  * qx  期限
	  */
	 public String getAmt(String bj,String lv,String qx){
		//月利率 
		BigDecimal  monthLv = new BigDecimal(lv).divide(new BigDecimal("100")).divide(new BigDecimal("12"),4,BigDecimal.ROUND_HALF_UP);
		
		BigDecimal lvs = new BigDecimal("1").add(monthLv);
		
		BigDecimal part1 = new BigDecimal(bj).multiply(monthLv).multiply(lvs.pow(Integer.parseInt(qx))).setScale(4,BigDecimal.ROUND_HALF_UP);
		
		BigDecimal part2 = lvs.pow(12).subtract(new BigDecimal("1")).setScale(4,BigDecimal.ROUND_HALF_UP);
		
	    BigDecimal hk= part1.divide(part2,2,BigDecimal.ROUND_HALF_UP);
	    
	    return hk+""; 
	 }
	 
	 
	 
	 /**
	  * 用信余额
	  * 1.补充当月截止到今天的用信余额.
	  * 2.补充业务开展以来截止到今天的用信余额.
	  */
	 public void dosynchyxyeMethod(){
			//获取今日日期
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = format.format(new Date());
			//1.当月截止到今天的用信余额
			
			//2.补充业务开展以来截止到今天的用信余额
		}
		
}
