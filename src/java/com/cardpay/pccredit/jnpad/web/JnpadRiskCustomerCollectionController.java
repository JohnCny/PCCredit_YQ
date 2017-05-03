package com.cardpay.pccredit.jnpad.web;

import java.io.PrintWriter;
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

import com.cardpay.pccredit.customer.constant.CustomerInforConstant;
import com.cardpay.pccredit.customer.constant.MarketingCreateWayEnum;
import com.cardpay.pccredit.datapri.constant.DataPriConstants;
import com.cardpay.pccredit.ipad.util.JsonDateValueProcessor;
import com.cardpay.pccredit.jnpad.service.JnpadRiskCustomerCollectionService;
import com.cardpay.pccredit.manager.service.ManagerBelongMapService;
import com.cardpay.pccredit.manager.web.AccountManagerParameterForm;
import com.cardpay.pccredit.riskControl.constant.RiskCustomerCollectionConstant;
import com.cardpay.pccredit.riskControl.constant.RiskCustomerCollectionEndResultEnum;
import com.cardpay.pccredit.riskControl.constant.RiskCustomerCollectionEnum;
import com.cardpay.pccredit.riskControl.filter.RiskCustomerCollectionPlanFilter;
import com.cardpay.pccredit.riskControl.model.RiskCustomerCollectionPlan;
import com.cardpay.pccredit.riskControl.service.RiskCustomerCollectionService;
import com.cardpay.pccredit.riskControl.web.RiskCustomerCollectionPlanForm;
import com.cardpay.pccredit.system.model.Dict;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.constant.JRadConstants;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.controller.BaseController;
import com.wicresoft.jrad.base.web.result.JRadReturnMap;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.jrad.base.web.utility.WebRequestHelper;
import com.wicresoft.util.spring.Beans;
import com.wicresoft.util.web.RequestHelper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Controller
public class JnpadRiskCustomerCollectionController extends BaseController{
	@Autowired
	private JnpadRiskCustomerCollectionService JnpadriskCustomerCollectionService;
	
	@Autowired
	private RiskCustomerCollectionService riskCustomerCollectionService;
	
	@Autowired
	private ManagerBelongMapService managerBelongMapService;
	
	/**
	 * 通过id得到逾期客户催收计划
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ipad/product/findRiskCustomerCollectionPlanById.json")
	public String findRiskCustomerCollectionPlanById(HttpServletRequest request){
		String collectionPlanId = RequestHelper.getStringValue(request, "id");
		
		RiskCustomerCollectionPlanForm collectionplan = JnpadriskCustomerCollectionService.findRiskCustomerCollectionPlanById(collectionPlanId);
		
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(collectionplan, jsonConfig);
		return json.toString();
	}
	
	
	
	/**
	 * 过滤查询逾期催收客户
	 */
	@ResponseBody
	@RequestMapping(value = "/ipad/product/findRiskCustomerCollectionPlansByFilter.json")
	public String findRiskCustomerCollectionPlansByFilter(@ModelAttribute RiskCustomerCollectionPlanFilter filter,HttpServletRequest request){
		
		String id = RequestHelper.getStringValue(request, "userId");
		filter.setCustomerManagerId(id);
 
		
		QueryResult<RiskCustomerCollectionPlanForm> result = JnpadriskCustomerCollectionService.findRiskCustomerCollectionPlansByFilter(filter);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(result, jsonConfig);
		return json.toString();
	}
	
	/**
	 * 得到当前客户经理下属经理名下的逾期客户催收信息数量
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/ipad/product/findRiskViewSubCollectionPlansCountByFilter.json")
	public String findRiskViewSubCollectionPlansCountByFilter(@ModelAttribute RiskCustomerCollectionPlanFilter filter,HttpServletRequest request){
		String id = RequestHelper.getStringValue(request, "id");
		filter.setCustomerManagerId(id);
		int result=JnpadriskCustomerCollectionService.findRiskViewSubCollectionPlansCountByFilter(filter);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(result, jsonConfig);
		return json.toString();
	}
	
	/**
	 * 显示催收计划详细信息
	 * 
	 */
	
	@ResponseBody
	@RequestMapping(value = "/ipad/product/browerRiskcustomer.json")
	public String display(HttpServletRequest request) {
		Map<String, Object> map =new LinkedHashMap<String, Object>();
		String collectionPlanId = request.getParameter("collectionPlanId");
		RiskCustomerCollectionPlanForm collectionplan = new RiskCustomerCollectionPlanForm();
//		if (StringUtils.isNotEmpty(collectionPlanId)) {
			collectionplan = JnpadriskCustomerCollectionService.findRiskCustomerCollectionPlanById(collectionPlanId);
//			List<RiskCustomerCollectionPlansAction> collectionActions = riskCustomerCollectionService.findRiskCustomerCollectionPlansActionByCollectionPlanId(collectionPlanId);
			map.put("collectionPlan", collectionplan);
//			map.put("collectionActions",collectionActions);
//		}
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(map, jsonConfig);
		return json.toString();
	}

	//获取客户经理
	@ResponseBody
	@RequestMapping(value = "/ipad/risk/getManager.json",method = RequestMethod.GET)
	public String getManager(HttpServletRequest request){
		try {
			
			String userId = request.getParameter("userId");
			List<AccountManagerParameterForm> accountManagerParameterForms = managerBelongMapService.findSubManagerBelongMapByManagerId(userId);
			JSONArray json = new JSONArray();
			json = JSONArray.fromObject(accountManagerParameterForms);
			return json.toString();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
//	获取逾期客户
	@ResponseBody
	@RequestMapping(value = "/ipad/risk/getCustomer.json",method = RequestMethod.GET)
	public String getCustomer(HttpServletRequest request){
		try {
		String userId = RequestHelper.getStringValue(request, "userId");
		List<Dict> riskCustomers = riskCustomerCollectionService.findCustomerIdAndName(userId);
		JSONArray json = new JSONArray();
		json = JSONArray.fromObject(riskCustomers);
		return json.toString();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
//	获取产品
	@ResponseBody
	@RequestMapping(value = "/ipad/risk/getProduct.json",method = RequestMethod.GET)
	public String getProductIdAndName(HttpServletRequest request) {
		 try {
			 String customerId = RequestHelper.getStringValue(request, "customerId");
			 List<Dict> products = JnpadriskCustomerCollectionService.getProductIdAndName(customerId);
			 JSONArray json = new JSONArray();
			 json = JSONArray.fromObject(products);
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	//添加客户维护计划
	@ResponseBody
	@RequestMapping(value = "/ipad/risk/insertplan.json")
	public String insert(@ModelAttribute RiskCustomerCollectionPlanForm form, HttpServletRequest request) {
		boolean flag = riskCustomerCollectionService.checkCollectionPlan(form.getCustomerId(),form.getProductId(),RiskCustomerCollectionEndResultEnum.collection,RiskCustomerCollectionEndResultEnum.repaymentcommitments);
		Map<String, String> returnMap = new LinkedHashMap<String, String>();//WebRequestHelper.requestValidation(getModuleName(), form);
		if(!flag){
				try {
					RiskCustomerCollectionPlan riskCustomerCollectionPlan = form.createModel(RiskCustomerCollectionPlan.class);
					
					String createdBy = request.getParameter("userId");
					String customerManagerId = riskCustomerCollectionPlan.getCustomerManagerId();
					if(createdBy!=null && createdBy.equals(customerManagerId)){
						riskCustomerCollectionPlan.setCreateWay(MarketingCreateWayEnum.myself.toString());
					}else{
						riskCustomerCollectionPlan.setCreateWay(MarketingCreateWayEnum.manager.toString());
					}
					riskCustomerCollectionPlan.setCreatedBy(createdBy);
					String id = riskCustomerCollectionService.insertRiskCustomerCollectionPlan(riskCustomerCollectionPlan);
					returnMap.put("mess","创建成功");
				}catch (Exception e) {
					returnMap.put("mess", DataPriConstants.SYS_EXCEPTION_MSG);
				}
		}else{
			returnMap.put("mess",RiskCustomerCollectionConstant.alreadyExists);
		}
		
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(returnMap, jsonConfig);
		return json.toString();
	}
	
	/**
	 * 修改催收计划
	 * @param form
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ipad/risk/update.json")
	@JRadOperation(JRadOperation.CHANGE)
	public String update(@ModelAttribute RiskCustomerCollectionPlanForm form, HttpServletRequest request) {
		Map<String, String> returnMap = new LinkedHashMap<String, String>();//WebRequestHelper.requestValidation(getModuleName(), form);
			try {
				String createWay = riskCustomerCollectionService.findRiskCustomerCollectionPlanById(form.getId()).getCreateWay();
				if(createWay!=null && !createWay.equals(RiskCustomerCollectionEnum.myself.toString())){
					
					returnMap.put("mess",CustomerInforConstant.UPDATEERROR);
					JsonConfig jsonConfig = new JsonConfig();
					jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
					JSONObject json = JSONObject.fromObject(returnMap, jsonConfig);
					return json.toString();
				}
				
				String modifiedBy = request.getParameter("userId"); 
				
				RiskCustomerCollectionPlan riskCustomerCollectionPlan = form.createModel(RiskCustomerCollectionPlan.class);
				riskCustomerCollectionPlan.setModifiedBy(modifiedBy);
				boolean flag=riskCustomerCollectionService.updateRiskCustomerCollectionPlan(riskCustomerCollectionPlan);
				if(flag){
					returnMap.put(RECORD_ID, riskCustomerCollectionPlan.getId());
					returnMap.put("mess","修改成功");
				}else{
					returnMap.put("mess",CustomerInforConstant.UPDATEERROR);
					JsonConfig jsonConfig = new JsonConfig();
					jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
					JSONObject json = JSONObject.fromObject(returnMap, jsonConfig);
					return json.toString();
				}
			}catch (Exception e) {
				returnMap.put("mess", DataPriConstants.SYS_EXCEPTION_MSG);
				JsonConfig jsonConfig = new JsonConfig();
				jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
				JSONObject json = JSONObject.fromObject(returnMap, jsonConfig);
				return json.toString();
			}
		
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
			JSONObject json = JSONObject.fromObject(returnMap, jsonConfig);
			return json.toString();
	}
}
