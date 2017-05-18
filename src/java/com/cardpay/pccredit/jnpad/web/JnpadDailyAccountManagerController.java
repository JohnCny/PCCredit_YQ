package com.cardpay.pccredit.jnpad.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.ipad.util.JsonDateValueProcessor;
import com.cardpay.pccredit.jnpad.model.MonthlyStatisticsModel;
import com.cardpay.pccredit.jnpad.service.JnpadDailyAccountManagerService;
import com.cardpay.pccredit.jnpad.service.MonthlyStatisticsService;
import com.cardpay.pccredit.manager.filter.DailyAccountManagerFilter;
import com.cardpay.pccredit.manager.model.DailyAccountManager;
import com.cardpay.pccredit.manager.model.ManagerPerformmance;
import com.cardpay.pccredit.manager.service.DailyAccountService;
import com.cardpay.pccredit.manager.service.ManagerPerformmanceService;
import com.cardpay.pccredit.manager.web.DailyAccountManagerForm;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.database.model.QueryResult;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;


@Controller
public class JnpadDailyAccountManagerController {

	@Autowired
	private JnpadDailyAccountManagerService jnpadDailyAccountManagerService;
	@Autowired
	private MonthlyStatisticsService StatisticsService;
	@Autowired
	private DailyAccountService dailyAccountService;
	@Autowired
	private ManagerPerformmanceService managerPerformmanceService;
/*	*//**
	 * 客户经理日报浏览页面
	 * @param filter
	 * @param request
	 * @return
	 *//*
	@ResponseBody
	@RequestMapping(value = "/ipad/dailyAccount/browse.json", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.BROWSE)
	public String browse(@ModelAttribute DailyAccountManagerFilter filter, HttpServletRequest request) {
		filter.setRequest(request);
		String loginId = request.getParameter("userId");
		String userType = request.getParameter("userType");
		if(userType.equals("1")){
		filter.setLoginId(loginId);
		}
		QueryResult<DailyAccountManagerForm> result = jnpadDailyAccountManagerService.findDailyAccountManagersByFilter(filter);
		
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(result, jsonConfig);
		return json.toString();
	}*/
	
	/**
	 * 客户经理日报浏览页面
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ipad/dailyAccount/browse.json", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.BROWSE)
	public String browse(@ModelAttribute DailyAccountManagerFilter filter, HttpServletRequest request) {
		filter.setRequest(request);
		List<MonthlyStatisticsModel> resultModel=null;
		String loginId = request.getParameter("userId");
		filter.setLoginId(loginId);
		List<DailyAccountManagerForm> result = jnpadDailyAccountManagerService.findDailyAccountManagersByFilter1(filter);
		 //查询当前客户经理是否为小组长以及手下的客户经理ID
	     resultModel=StatisticsService.findxzz(loginId);
	     Map<String,Object> map = new LinkedHashMap<String,Object>();
	     map.put("resultModel", resultModel);
	     map.put("size", resultModel.size());
	     map.put("result", result);
	     map.put("resultsize", result.size());
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(map, jsonConfig);
		return json.toString();
	}
	/**
	 * 日报修改
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ipad/dailyAccount/update.json")
	@JRadOperation(JRadOperation.CHANGE)
	public String update(@ModelAttribute DailyAccountManagerForm dailyAccountManagerForm, HttpServletRequest request) {
		Map<String,Object> map = new LinkedHashMap<String,Object>();
			try {
				String loginId = request.getParameter("userId");
				DailyAccountManager dailyAccountManager = new DailyAccountManager();
				dailyAccountManager.setModifiedBy(loginId);
				dailyAccountManager.setTodayplan(request.getParameter("todayplan"));;
				dailyAccountManager.setTomorrowplan(request.getParameter("tomorrowplan"));
				dailyAccountManager.setId(request.getParameter("id"));
				dailyAccountService.updateAccountManager(dailyAccountManager);
				map.put("message", "提交成功");
				map.put("success", "true");
				
			} catch (Exception e) {
				map.put("message", "提交失败");
				map.put("success", "false");
				JsonConfig jsonConfig = new JsonConfig();
				jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
				JSONObject json = JSONObject.fromObject(map, jsonConfig);
				return json.toString();
			}

				JsonConfig jsonConfig = new JsonConfig();
				jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
				JSONObject json = JSONObject.fromObject(map, jsonConfig);
				return json.toString();
	}

	//根据指定日期和id查询业绩进度
		@ResponseBody
		@RequestMapping(value = "/ipad/dailyAccount/getpermanceByDateandId.json", method = { RequestMethod.GET })
		@JRadOperation(JRadOperation.BROWSE)
		public String getpermanceByDateandId( HttpServletRequest request) {
			String loginId = request.getParameter("userId");
			System.out.println(loginId);
			String ReportDate = request.getParameter("reportDate");
			SimpleDateFormat sf= new SimpleDateFormat("yyyy-MM-dd");
			Date date =null;
			try {
				date = sf.parse(ReportDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ManagerPerformmance managerPerformmance = managerPerformmanceService.finManagerPerformmanceByIdAndDate(loginId,date);		
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
			JSONObject json = JSONObject.fromObject(managerPerformmance, jsonConfig);
			return json.toString();
		}
	}
