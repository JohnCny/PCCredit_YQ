/*package com.cardpay.pccredit.report.web;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis.utils.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.manager.constant.ManagerLevelAdjustmentConstant;
import com.cardpay.pccredit.manager.filter.ManagerSalaryFilter;
import com.cardpay.pccredit.manager.model.AccountManagerParameter;
import com.cardpay.pccredit.manager.model.ManagerSalaryForm;
import com.cardpay.pccredit.manager.model.TPerformanceParameters;
import com.cardpay.pccredit.manager.service.StatisticsAttentiveBalanceSynchScheduleService;
import com.cardpay.pccredit.postLoan.filter.PostLoanFilter;
import com.cardpay.pccredit.postLoan.model.MibusidataForm;
import com.cardpay.pccredit.postLoan.service.PostLoanService;
import com.cardpay.pccredit.report.filter.AccLoanCollectFilter;
import com.cardpay.pccredit.report.model.AccLoanCollectInfo;
import com.cardpay.pccredit.report.service.CustomerTransferFlowService;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.auth.JRadModule;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.controller.BaseController;
import com.wicresoft.jrad.base.web.result.JRadPagedQueryResult;
import com.wicresoft.jrad.base.web.result.JRadReturnMap;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.jrad.base.web.utility.WebRequestHelper;
import com.wicresoft.util.date.DateHelper;
import com.wicresoft.util.spring.Beans;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;
import com.wicresoft.util.web.RequestHelper;

*//**
 * 贷款汇总查询（客户经理）
 * @author chinh
 *
 * 2014-11-27上午10:50:49
 *//*
@Controller
@RequestMapping("/report/afterLoancollect/*")
@JRadModule("report.afterLoancollect")
public class AfterLoanCollectController extends BaseController{
	
	@Autowired
	private CustomerTransferFlowService customerTransferFlowService;
	
	@Autowired
	private StatisticsAttentiveBalanceSynchScheduleService  attentiveBalanceSynchScheduleService;
	
	@Autowired
	private CommonDao commonDao;
	
	private static final Logger logger = Logger.getLogger(AfterLoanCollectController.class);
	*//**
	 * 贷款汇总查询(卡中心)
	 * 
	 * @param filter
	 * @param request
	 * @return
	 * 新增用信客户:首次提款时间在选定时间段内的客户		  |新增用信余额:提款时间在选定时间段内 且在结束时间前未还清的借据
	 * 累计用信客户:结束时间前提过款的客户                                              |累计用信余额:结束时间前未还清的所有借据
	 * 新增授信客户:授信时间在选定时间段内的客户                                  |新增授信余额:授信时间在选定时间段内的合同,且结束日期前未结束的合同
	 * 累计授信客户:结束时间前授信的客户                                                  |累计授信总额:结束时间前授信的合同,且结束日期前未结束的合同
	 * 新增逾期客户数:逾期时间在选定时间段内的客户                              |新增逾期余额:逾期时间在选定时间段内的借据
	 * 累计逾期客户数:逾期时间在结束时间前的客户                                  |累计逾期余额:逾期时间在结束时间前的借据
	 * 用信余额（日均):(选定时间段内每天的用信余额求和)/时间段的天数
	 * @throws ParseException 
	 *//*
	@ResponseBody
	@RequestMapping(value = "browseAll.page", method = { RequestMethod.GET })
	public AbstractModelAndView browseAll(@ModelAttribute AccLoanCollectFilter filter, HttpServletRequest request) throws ParseException {
		filter.setRequest(request);
        IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
        
        //如果当前客户是客户经理角色 userId is not null
        List<AccountManagerParameter> list = customerTransferFlowService.findManager(user.getId());
        if(list != null && list.size() > 0){
        	if(user.getUserType() ==1){//客户经理
        		filter.setUserId(user.getId());
        	}
		}
		
		if(StringUtils.isEmpty(filter.getStartDate())){
			filter.setStartDate("2016-05-01");
		}
		
		if(StringUtils.isEmpty(filter.getEndDate())){
			filter.setEndDate(DateHelper.getDateFormat(new Date(),"yyyy-MM-dd"));
		}
		
		long start = System.currentTimeMillis();
		List<AccLoanCollectInfo> accloanList = customerTransferFlowService.getAccLoanCollect(filter);
		//查询 pos流水贷产品的贷款余额
		BigDecimal pslsd = customerTransferFlowService.findSubListManagerByManagerId(user);
		for(AccLoanCollectInfo acc :accloanList){
			acc.setPos(pslsd.toString());
		}
		
		//查询客户经理count 去掉平均到客户经理的条件
		//int count = customerTransferFlowService.findManagerListCount();
		
		//查询当月日均用信
		BigDecimal monthAverageAmt = customerTransferFlowService.findMonthAverageAmt();
		for(AccLoanCollectInfo acc :accloanList){
			acc.setMa(monthAverageAmt.multiply(calMonthGrade()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		}
		
		//查询业务开展以来截止到如今的日均用信
		BigDecimal totalMonthAverageAmt = customerTransferFlowService.findToalMonthAverageAmt();
		int monthCount = returnMonthCount();
		BigDecimal monthManagerCount = new BigDecimal(monthCount);
		for(AccLoanCollectInfo acc :accloanList){
			acc.setTa(totalMonthAverageAmt.divide(monthManagerCount,2,BigDecimal.ROUND_HALF_UP).toString());
		}
		
		//信用、抵押 ,保证类贷款笔数占比   信用、抵押 ,保证类贷款金额占比  C101-保证    C102-抵押   C100-信用 
		Map<String, String> map =  calProportion(user);
		for(AccLoanCollectInfo acc :accloanList){
			acc.setC101numproportion(map.get("C101NumProportion"));
			acc.setC102numproportion(map.get("C102NumProportion"));
			acc.setC100numproportion(map.get("C100NumProportion"));
			acc.setC101amtproportion(map.get("C101AmtProportion"));
			acc.setC102amtproportion(map.get("C102AmtProportion"));
			acc.setC100amtproportion(map.get("C100AmtProportion"));
		}
		long end = System.currentTimeMillis();
		logger.info("贷款汇总查询时间花费：" + (end - start) + "毫秒");
		JRadModelAndView mv = new JRadModelAndView("/report/afteraccloan/afterAccLoanCollect_centre_browseAll", request);
		mv.addObject("list", accloanList);
		mv.addObject("filter", filter);
		mv.addObject("urlType", user.getUserType());
		
		// 控制参数 按钮显示
		boolean lock = false;
		String sql = "select * from dict where dict_type = 'CTRL_STATUS_PARAM' ";
		String PARAM = (String) commonDao.queryBySql(sql, null).get(0).get("TYPE_CODE");
		if("1".equals(PARAM)){
			lock = true;
		}
		mv.addObject("lock", lock);
		return mv;
		
	}
	
	*//**
	 * 各类贷款产品笔数和金额占比
	 * C101-保证    C102-抵押   C100-信用 
	 * @return
	 *//*
	public Map<String, String> calProportion(IUser user){
		Map<String, String> map  = new HashMap<String, String>();
		
		//笔数
		BigDecimal totalNum = customerTransferFlowService.findNumsOfPensByUserIdAndProdType(user,"");//总笔数
		BigDecimal C101Num  = customerTransferFlowService.findNumsOfPensByUserIdAndProdType(user,"C101");
		BigDecimal C102Num  = customerTransferFlowService.findNumsOfPensByUserIdAndProdType(user,"C102");
		BigDecimal C100Num  = customerTransferFlowService.findNumsOfPensByUserIdAndProdType(user,"C100");
		
		map.put("C101NumProportion",C101Num.divide(totalNum,4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).toString());
		map.put("C102NumProportion",C102Num.divide(totalNum,4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).toString());
		map.put("C100NumProportion",C100Num.divide(totalNum,4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).toString());
		
		//金额
		BigDecimal totalAmt = customerTransferFlowService.findAmtByUserIdAndProdType(user,"");//总金额
		BigDecimal C101Amt = customerTransferFlowService.findAmtByUserIdAndProdType(user,"C101");//保证
		BigDecimal C102Amt = customerTransferFlowService.findAmtByUserIdAndProdType(user,"C102");//抵押
		BigDecimal C100Amt = customerTransferFlowService.findAmtByUserIdAndProdType(user,"C100");//信用
		
		map.put("C101AmtProportion",C101Amt.divide(totalAmt,4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).toString());
		map.put("C102AmtProportion",C102Amt.divide(totalAmt,4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).toString());
		map.put("C100AmtProportion",C100Amt.divide(totalAmt,4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).toString());
		return map;
	}
	
	*//**
	 * 1/31
	 * @return
	 *//*
	public BigDecimal calMonthGrade(){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = format.format(new Date());
		
		
		String year  = dateString.substring(0, 4);
	    String month = dateString.substring(5, 7);
	    String day   = dateString.substring(8, 10);
	    
	    //当月实际天数
		int days = getMonthLastDay(Integer.parseInt(year),Integer.parseInt(month));
		
		BigDecimal dayGrade = new BigDecimal(Integer.parseInt(day)-1).divide(new BigDecimal(days),4,BigDecimal.ROUND_HALF_UP);
		return dayGrade;
	}
	
	*//**
	 * 获得当月实际使用天数
	 * @param year
	 * @param month
	 * @return
	 *//*
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
	
	
	@ResponseBody
	@RequestMapping(value = "handle.json")
	public JRadReturnMap handle(HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		try {
			String month = RequestHelper.getStringValue(request, ID);
			attentiveBalanceSynchScheduleService.dosynchyxyeMethod(month);
			
			returnMap.addGlobalMessage(ManagerLevelAdjustmentConstant.IF_HANDLE_SUCCESS);
		} catch (Exception e) {
			return WebRequestHelper.processException(e);
		}
		return returnMap;
	}
	
	*//**
	 * 计算两个月中间相差多少月
	 * 2016-11-28
	 * 2016-07-01
	 * @throws ParseException 
	 *//*
	public int returnMonthCount() throws ParseException{
	
	   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	   String str1 = "2016-07-01";//start 
	   String str2 = sdf.format(new Date());//end

	   Calendar bef = Calendar.getInstance();
	   Calendar aft = Calendar.getInstance();
	   bef.setTime(sdf.parse(str1));
	   aft.setTime(sdf.parse(str2));
      
	   int result=0;
	   result = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
	   if (result == 0) {
	     result = 1;
	   }
	   return result;
	}
	
	
	*//**
	 * 计算两个月中间相差多少月
	 * 台帐数据日期从2016-07-01开始
	 *//*
	public int returnMonthCount() throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str2 = sdf.format(new Date());//end
		
		Map<Integer, Integer> map = getMonthAndDaysBetweenDate("2016-07-01", str2);
		int months = map.get(1).intValue();//月数
		//int days = map.get(2).intValue();//天数
		return months;
	}
	
	
	public Map<Integer, Integer> getMonthAndDaysBetweenDate(String date1,String date2) {
		Map<Integer, Integer> map = new HashMap();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		Date d1 = null;
		try {
			d1 = sd.parse(date1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date d2 = null;
		try {
			d2 = sd.parse(date2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int months = 0;// 相差月份
		int days = 0;
		int y1 = d1.getYear();
		int y2 = d2.getYear();
		int dm1 = d2.getMonth();// 起始日期月份
		int dm2 = d2.getMonth();// 结束日期月份
		int dd1 = d1.getDate(); // 起始日期天
		int dd2 = d2.getDate(); // 结束日期天
		if (d1.getTime() < d2.getTime()) {
			months = d2.getMonth() - d1.getMonth() + (y2 - y1) * 12;
			if (dd2 < dd1) {
				months = months - 1;
			}
			
			 * System.out.println(getFormatDateTime(addMonthsToDate(DateUtil.
			 * parseDate(date1, "yyyy-MM-dd"),months),"yyyy-MM-dd"));
			 * days=getDaysBetweenDate
			 * (getFormatDateTime(addMonthsToDate(DateUtil.parseDate(date1,
			 * "yyyy-MM-dd"),months),"yyyy-MM-dd"),date2);
			 
			map.put(1, months);
			map.put(2, days);
		}
		return map;
	}
	
	public Date parseDate(String dateString, String dateFormate) {
		SimpleDateFormat sd = new SimpleDateFormat(dateFormate);
		Date date = null;
		try {
			date = sd.parse(dateString);
		} catch (Exception ex) {
			logger.error("Pase the Date(" + dateString
					+ ") occur errors:" + ex.getMessage());
		}
		return date;
	}
	
	
	
	
	
	
	*//**
	 * 贷款利息查询统计报表
	 * @param filter
	 * @param request
	 * @return
	 *//*
	@ResponseBody
	@RequestMapping(value = "tzbrowse.page", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView tzbrowse(@ModelAttribute PostLoanFilter filter,HttpServletRequest request) {
		filter.setRequest(request);
		JRadModelAndView mv = new JRadModelAndView("/report/lx/lx_browse", request);
		mv.addObject(PAGED_RESULT, "");
		return mv;
	}
	
	*//**
	 * 贷款利息查询统计报表导出
	 * @param filter
	 * @param request
	 * @return
	 *//*
	@ResponseBody
	@RequestMapping(value = "exportData.json",method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.CHANGE)
	public JRadReturnMap exportData(PostLoanFilter filter,HttpServletRequest request,HttpServletResponse response) {
		JRadReturnMap returnMap = new JRadReturnMap();
		
		returnMap.setSuccess(true);
		if (returnMap.isSuccess()) {
			try {
				customerTransferFlowService.getExportWageData(filter,response);
			}
			catch (Exception e) {
				return WebRequestHelper.processException(e);
			}
		}
		return returnMap;
	}
	
	
	
	
	*//**
	 * 贷款利息分段查询统计报表
	 *//*
	@ResponseBody
	@RequestMapping(value = "LxSubsectBrowse.page", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView LxSubsectBrowse(@ModelAttribute PostLoanFilter filter,HttpServletRequest request) {
		filter.setRequest(request);
		JRadModelAndView mv = new JRadModelAndView("/report/lx/lx_subsect_browse", request);
		mv.addObject(PAGED_RESULT, "");
		
		// 控制参数 按钮显示
		boolean lock = false;
		String sql = "select * from dict where dict_type = 'CTRL_STATUS_PARAM' ";
		String PARAM = (String) commonDao.queryBySql(sql, null).get(0).get("TYPE_CODE");
		if("1".equals(PARAM)){
			lock = true;
		}
		mv.addObject("lock", lock);
		return mv;
	}
	
	*//**
	 * 贷款利息分段查询统计报表导出
	 * @param filter
	 * @param request
	 * @param response
	 * @return
	 *//*
	@ResponseBody
	@RequestMapping(value = "exportLxSubsectData.json",method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.CHANGE)
	public JRadReturnMap exportLxSubsectData(PostLoanFilter filter,HttpServletRequest request,HttpServletResponse response) {
		JRadReturnMap returnMap = new JRadReturnMap();
		returnMap.setSuccess(true);
		if (returnMap.isSuccess()) {
			try {
				customerTransferFlowService.getExportLxSubsectData(filter,response);
			}
			catch (Exception e) {
				return WebRequestHelper.processException(e);
			}
		}
		return returnMap;
	}
	
	
	*//**
	 * 授信(合同)分段查询统计报表
	 *//*
	@ResponseBody
	@RequestMapping(value = "ContractSubsectBrowse.page", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView ContracSubsectBrowse(@ModelAttribute PostLoanFilter filter,HttpServletRequest request) {
		filter.setRequest(request);
		JRadModelAndView mv = new JRadModelAndView("/report/lx/contract_subsect_browse", request);
		mv.addObject(PAGED_RESULT, "");
		return mv;
	}
	
	
	*//**
	 * 授信(合同)分段查询统计报表导出
	 * @param filter
	 * @param request
	 * @param response
	 * @return
	 *//*
	@ResponseBody
	@RequestMapping(value = "exportContractSubsectData.json",method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.CHANGE)
	public JRadReturnMap exportContractSubsectData(PostLoanFilter filter,HttpServletRequest request,HttpServletResponse response) {
		JRadReturnMap returnMap = new JRadReturnMap();
		returnMap.setSuccess(true);
		if (returnMap.isSuccess()) {
			try {
				customerTransferFlowService.exportContractSubsectData(filter,response);
			}
			catch (Exception e) {
				return WebRequestHelper.processException(e);
			}
		}
		return returnMap;
	}
	
}
*/package com.cardpay.pccredit.report.web;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.axis.utils.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cardpay.pccredit.manager.model.AccountManagerParameter;
import com.cardpay.pccredit.report.filter.AccLoanCollectFilter;
import com.cardpay.pccredit.report.model.AccLoanCollectInfo;
import com.cardpay.pccredit.report.service.CustomerTransferFlowService;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.auth.JRadModule;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.controller.BaseController;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.util.date.DateHelper;
import com.wicresoft.util.spring.Beans;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;

/**
 * 贷款汇总查询（客户经理）
 * @author chinh
 *
 * 2014-11-27上午10:50:49
 */
@Controller
@RequestMapping("/report/afterLoancollect/*")
@JRadModule("report.afterLoancollect")
public class AfterLoanCollectController extends BaseController{
	
	@Autowired
	private CustomerTransferFlowService customerTransferFlowService;
	
	private static final Logger logger = Logger.getLogger(AfterLoanCollectController.class);
	/**
	 * 贷款汇总查询(卡中心)
	 * 
	 * @param filter
	 * @param request
	 * @return
	 * 新增用信客户:首次提款时间在选定时间段内的客户		 |新增用信余额:提款时间在选定时间段内 且在结束时间前未还清的借据
	 * 累计用信客户:结束时间前提过款的客户                                             |累计用信余额:结束时间前未还清的所有借据
	 * 新增授信客户:授信时间在选定时间段内的客户                              |新增授信余额:授信时间在选定时间段内的合同,且结束日期前未结束的合同
	 * 累计授信客户:结束时间前授信的客户                                                  |累计授信总额:结束时间前授信的合同,且结束日期前未结束的合同
	 * 新增逾期客户数:逾期时间在选定时间段内的客户                         |新增逾期余额:逾期时间在选定时间段内的借据
	 * 累计逾期客户数:逾期时间在结束时间前的客户                              |累计逾期余额:逾期时间在结束时间前的借据
	 * 用信余额（日均):(选定时间段内每天的用信余额求和)/时间段的天数
	 */
	@ResponseBody
	@RequestMapping(value = "browseAll.page", method = { RequestMethod.GET })
	public AbstractModelAndView browseAll(@ModelAttribute AccLoanCollectFilter filter, HttpServletRequest request) {
		filter.setRequest(request);
        IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
        
        //如果当前客户是客户经理角色 userId is not null
        List<AccountManagerParameter> list = customerTransferFlowService.findManager(user.getId());
        if(list != null && list.size() > 0){
        	if(user.getUserType() ==1){//客户经理
        		filter.setUserId(user.getId());
        	}
		}
		
		if(StringUtils.isEmpty(filter.getStartDate())){
			filter.setStartDate("2016-05-01");
		}
		
		if(StringUtils.isEmpty(filter.getEndDate())){
			filter.setEndDate(DateHelper.getDateFormat(new Date(),"yyyy-MM-dd"));
		}
		
		long start = System.currentTimeMillis();
		List<AccLoanCollectInfo> accloanList = customerTransferFlowService.getAccLoanCollect(filter);
		long end = System.currentTimeMillis();
		logger.info("#########################贷款汇总查询时间花费：" + (end - start) + "毫秒");
		JRadModelAndView mv = new JRadModelAndView("/report/afteraccloan/afterAccLoanCollect_centre_browseAll", request);
		mv.addObject("list", accloanList);
		mv.addObject("filter", filter);
		return mv;
		
	}
}
