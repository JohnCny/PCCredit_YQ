package com.cardpay.pccredit.manager.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.customer.model.CustomerInfor;
import com.cardpay.pccredit.manager.dao.ManagerSalaryDao;
import com.cardpay.pccredit.manager.model.AccountManagerParameter;
import com.cardpay.pccredit.manager.model.TMibusidata;
import com.cardpay.pccredit.manager.model.TStatisticsAttentiveBalance;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.base.database.id.IDGenerator;

/**
 * 统计用信余额
 * @author songchen
 */
@Service
public class StatisticsAttentiveBalanceSynchScheduleService {
	
	private Logger logger = Logger.getLogger(StatisticsAttentiveBalanceSynchScheduleService.class);
	
	
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private ManagerSalaryDao managerSalaryDao;
	
	 /**
	  * 当月自动跑批 2016-08-01至2016-08-21
	  * 1.补充当月截止到今天的用信余额.
	  * 2.补充业务开展以来截止到今天的用信余额.
	  */
	 public void dosynchyxyeMethod(){
			//获取今日日期
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = format.format(new Date());
			logger.info(dateString+"**********统计当月截止到今天的用信余额start**********");
			
			// 1.统计当月截止到今天的用信余额
			String year  = dateString.substring(0, 4);
		    String month = dateString.substring(5, 7);
		    String day   = dateString.substring(8, 10);
		    
		    // 1.1取昨天的日期
		    String lastDate = getLastDay(year,month,day);
		    year  = lastDate.substring(0, 4);
		    month = lastDate.substring(5, 7);
		    day   = lastDate.substring(8, 10);
		    
			// 2.删除当月的历史数据
			String sql = "delete from t_statistics_attentive_balance where year ='"+year+"' and month ='"+month+"'";
			commonDao.queryBySql(sql, null);
			
		    // 查询客户经理
			List<AccountManagerParameter> list = commonDao.queryBySql(AccountManagerParameter.class,
							"select * from account_manager_parameter where manager_type in ('1','2')",null);
	
			// 生成 T_STATISTICS_ATTENTIVE_BALANCE 表数据
			for (AccountManagerParameter accountManagerParameter : list) {
				generateJxSpecificParameters(accountManagerParameter.getUserId(),year, month,day);
			}
			
			logger.info(dateString+"**********统计当月截止到今天的用信余额end**********");
			
    }
	 
	 
	 public String getLastDay(String year,String month,String day){
		    String lastMonth = "";
			Calendar calendar = Calendar.getInstance();
			calendar.set(Integer.parseInt(year),Integer.parseInt(month)-1,Integer.parseInt(day));
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			
			String lastYear = calendar.get(Calendar.YEAR)+"";
			lastMonth = calendar.get(Calendar.MONTH)+1+"";
			String lastDate = calendar.get(Calendar.DAY_OF_MONTH)+"";
			if(Integer.parseInt(lastDate)<10){
				lastDate = "0"+lastDate;
			}
			System.out.println(lastYear+"-"+lastMonth+"-"+lastDate);
			logger.info(lastYear+"-"+lastMonth+"-"+lastDate);
			
			String lastDateTime = lastYear+"-"+lastMonth+"-"+lastDate;
			return lastDateTime;
	 }
	 
	 /**
	  * 手工跑批
	  * @param month
	  */
	 public void dosynchyxyeMethod(String dateString){
			//获取今日日期
			//DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			//String dateString = format.format(new Date());
		    String []str = dateString.split("_");
		    String year  = str[0];
		    String month = str[1];
			logger.info(dateString+"**********统计当月截止到今天的用信余额start**********");
			
			//1.统计当月截止到今天的用信余额
			//String year  = dateString.substring(0, 4);
			
			
			// 2.删除当月的历史数据
			String sql = "delete from t_statistics_attentive_balance where year ='"+year+"' and month ='"+month+"'";
			commonDao.queryBySql(sql, null);
		    
		    // 查询客户经理
			List<AccountManagerParameter> list = commonDao.queryBySql(AccountManagerParameter.class,
							"select * from account_manager_parameter where manager_type in ('1','2')",null);
	
			// 生成 T_STATISTICS_ATTENTIVE_BALANCE 表数据
			for (AccountManagerParameter accountManagerParameter : list) {
				generateJxSpecificParameters(accountManagerParameter.getUserId(),year, month,"");
			}
			
			logger.info(dateString+"**********统计当月截止到今天的用信余额end**********");
			
     }
	 
	 
 	/**
	 * 生成 T_STATISTICS_ATTENTIVE_BALANCE
	 * 按客户id 区分 开
	 */
	public void generateJxSpecificParameters(String userId,String year,String month,String day){
		
		// 查询该客户经理名下的客户
		List<CustomerInfor> list = commonDao.queryBySql(CustomerInfor.class,
		"select * from basic_customer_information where TY_CUSTOMER_ID is not null and USER_ID='"+ userId + "' ", null);
	    
	    // 贷款余额
	    BigDecimal balamt = new BigDecimal("0");
	    
	    for(CustomerInfor fro :list){
	    	// 行内客户标识id
	    	String tyCustomerId = fro.getTyCustomerId();
	    	
	    	// 客户id
	    	String customerId = fro.getId();
	    	
	        // 计算贷款余额
	        balamt = findBalamt(tyCustomerId,year,month,day);
	        
	    	// 插入T_STATISTICS_ATTENTIVE_BALANCE表数据
	        TStatisticsAttentiveBalance parameters = new TStatisticsAttentiveBalance();
			parameters.setId(IDGenerator.generateID());
			parameters.setYear(year);
			parameters.setMonth(month);
			parameters.setMonthDayAverageCustLoanamt(balamt.toString());//当月客户日均贷款余额
			parameters.setCustomerId(customerId);//客户id
			parameters.setCustomerManagerId(userId);//客户经理id
			commonDao.insertObject(parameters);
	    }
	}
 
	 
 	/**
	 * 查询当月客户日均贷款余额
	 * tyCustomerId 行内客户标识id
	 */
	public BigDecimal findBalamt(String tyCustomerId,String year,String month,String day){
		// 该客户该月的日均贷款余额
		BigDecimal balamt = new BigDecimal("0");
		 
		// 查询客户当月生成台帐BUSICODE编号的次数
		List<Map<String, Object>> mapList =  managerSalaryDao.findDistinctBusicode(tyCustomerId, year, month);
		
		// 根据BUSICODE再次筛选
		for (Map<String, Object> obj : mapList){
			String sql =    "select t.busicode,				   		    		    "+				
							"       t.money,                                		"+
							"       t.loandate,                             		"+
							"       t.balamt,                               		"+
							"       t.operdatetime,                         		"+
							"       t.MAINASSURE,                          			"+
							"       t.custid                                		"+
							"  from t_mibusidata t                          	    "+
							" where substr(OPERDATETIME, '0', '4') = '"+year+"'     "+
							"   and substr(OPERDATETIME, '6', '2') = '"+month+"'    "+
							"   and custid = '"+tyCustomerId+"'                     "+
							"   and busicode = '"+obj.get("BUSICODE").toString()+"' "+
							" order by OPERDATETIME asc                             ";
			List<TMibusidata> mibusidataList = new ArrayList<TMibusidata>();
			mibusidataList =  commonDao.queryBySql(TMibusidata.class, sql, null);
			balamt = balamt.add(doCalAmt(mibusidataList, year, month,day));
		}
		return balamt;
	}
	
	/**
	 * 筛选计算
	 * @param mibusidataList
	 * @return
	 * 一笔busicode 的list 一笔算
	 */
	public BigDecimal doCalAmt(List<TMibusidata> mibusidataList,String year,String month,String day){
		// 获取当月实际天数
		String days ="";
		if(StringUtils.isEmpty(day)){
			days = getMonthLastDay(Integer.parseInt(year),Integer.parseInt(month))+"";
		}else{
			int currentMonthDay = getMonthLastDay(Integer.parseInt(year),Integer.parseInt(month));
			if(Integer.parseInt(day)>=currentMonthDay){
				days = currentMonthDay +"";
			}else{
				days = Integer.parseInt(day) +1 +"";
			}
		}
		
		if(Integer.parseInt(days)<10){
			days = "0"+days;
		}
		
		//System.out.println("**********************天数"+days);
		logger.info("**********************天数"+days);
		// 贷款余额
		String balamt = "";
		
		// 操作时间
		String operTime = "";
		
		/**
		 * ∑(贷款余额 x 实际使用天数)/当月实际天数  求和     分段求和
		 * 30000   2016-08-01
		 * 20000   2016-08-15
		 * 10000   2016-08-25
		 * 0	   2016-08-31(月末日期)
		 */
		List<String> list  = new ArrayList<String>();
		
		for(TMibusidata data : mibusidataList){
		    if(balamt != data.getBalamt().toString()){
		    	balamt = data.getBalamt().toString();
		    	operTime = data.getOperdatetime();
		    	list.add(balamt+"@"+operTime);
		    }
		}
		
		// 添加月末日期
		String lastDay = year +"-"+ month +"-"+ days;
		list.add(0+"@"+lastDay);
		
		// 汇总BUSICODE单笔台帐的日均贷款余额
		BigDecimal amt   = new BigDecimal("0");
		BigDecimal amt0  = new BigDecimal("0");
		BigDecimal amt1  = new BigDecimal("0");
		
		// 实际使用天数
		int ts =0;
		String str0[];
		String str1[];
		String time0;
		String time1;
	
		
		for(int i = 0;i<list.size();i++){
			//System.out.println(list.size());
			// 第0个List 的Amt 以及操作时间
			str0 = list.get(0).split("@");
			amt0 =  new BigDecimal(str0[0]);
			time0 = str0[1];
			
			// 第1个List 的Amt 以及操作时间
			str1 = list.get(1).split("@");
			amt1 =  new BigDecimal(str1[0]);
			time1 = str1[1];
			
			// 计算两个时间戳的时间差
		    ts = calMistTime(time0,time1);
		    
		    // 计算Amt
			amt = amt.add(amt0.multiply(new BigDecimal(ts)).divide(new BigDecimal(days),2,BigDecimal.ROUND_HALF_UP));
			
			// 计算完毕移除第0条数据 List的Size随之减小
			list.remove(0);
			//System.out.println(list.size());
			i =0;
		}
		return amt;
	}
	
	
	/**
	  * 计算两个时间戳的时间差 即天数 例 2016-09-01 2016-09-05
	  */
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
	 
	 /**
	  * @param date
	  * @return
	  */
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
	 * 获得当月实际使用天数
	 * @param year
	 * @param month
	 * @return
	 */
	 public static int getMonthLastDay(int year, int month)  
	  {  
	      Calendar a = Calendar.getInstance();  
	      a.set(Calendar.YEAR, year);  
	      a.set(Calendar.MONTH, month - 1);  
	      a.set(Calendar.DATE, 1);//把日期设置为当月第一天  
	      a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天  
	      int maxDate = a.get(Calendar.DATE);  
	      return maxDate;  
	  }  
}
