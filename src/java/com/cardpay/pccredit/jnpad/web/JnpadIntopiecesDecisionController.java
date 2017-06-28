package com.cardpay.pccredit.jnpad.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.customer.model.CustomerFirsthendBase;
import com.cardpay.pccredit.customer.model.CustomerInfor;
import com.cardpay.pccredit.customer.service.CustomerInforService;
import com.cardpay.pccredit.customer.service.MaintenanceService;
import com.cardpay.pccredit.intopieces.filter.IntoPiecesFilter;
import com.cardpay.pccredit.intopieces.model.AppManagerAuditLog;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationInfo;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationProcessForm;
import com.cardpay.pccredit.intopieces.model.CustomerSpUser;
import com.cardpay.pccredit.intopieces.model.CustomerSqInfo;
import com.cardpay.pccredit.intopieces.model.CustormerSdwUser;
import com.cardpay.pccredit.intopieces.model.ImageMore;
import com.cardpay.pccredit.intopieces.model.IntoPieces;
import com.cardpay.pccredit.intopieces.model.IntoPiecesFilters;
import com.cardpay.pccredit.intopieces.service.AddIntoPiecesService;
import com.cardpay.pccredit.intopieces.service.CustomerApplicationIntopieceWaitService;
import com.cardpay.pccredit.intopieces.service.CustomerSqInfoService;
import com.cardpay.pccredit.intopieces.service.IntoPiecesService;
import com.cardpay.pccredit.intopieces.service.JnpadCustormerSdwUserService;
import com.cardpay.pccredit.intopieces.service.JnpadSpUserService;
import com.cardpay.pccredit.intopieces.web.ApproveHistoryForm;
import com.cardpay.pccredit.intopieces.web.CustomerApplicationIntopieceWaitForm;
import com.cardpay.pccredit.ipad.util.JsonDateValueProcessor;
import com.cardpay.pccredit.jnpad.model.JnpadCsSdModel;
import com.cardpay.pccredit.jnpad.model.ManagerInfoForm;
import com.cardpay.pccredit.jnpad.model.ProductAttributes;
import com.cardpay.pccredit.jnpad.service.JnpadIntopiecesDecisionService;
import com.cardpay.pccredit.manager.form.ManagerPerformmanceForm;
import com.cardpay.pccredit.manager.web.AccountManagerParameterForm;
import com.cardpay.pccredit.product.model.ProductAttribute;
import com.cardpay.pccredit.product.service.ProductService;
import com.cardpay.pccredit.riskControl.model.RiskCustomer;
import com.jcraft.jsch.SftpException;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.database.id.IDGenerator;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.controller.BaseController;
import com.wicresoft.jrad.base.web.result.JRadPagedQueryResult;
import com.wicresoft.jrad.base.web.result.JRadReturnMap;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.jrad.modules.privilege.model.User;
import com.wicresoft.util.spring.Beans;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;
import com.wicresoft.util.web.RequestHelper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Controller
public class JnpadIntopiecesDecisionController extends BaseController{
	@Autowired
	private AddIntoPiecesService addIntoPiecesService;
	@Autowired
	private JnpadSpUserService UserService;
	@Autowired
	private JnpadCustormerSdwUserService SdwUserService;
	@Autowired
	private JnpadIntopiecesDecisionService jnpadIntopiecesDecisionService;
	@Autowired
	private IntoPiecesService intoPiecesService;
	@Autowired
	private CustomerInforService customerInforService;
	@Autowired
	private ProductService productService;
	@Autowired
	private CustomerSqInfoService SqInfoService;
	@Autowired
	private CustomerApplicationIntopieceWaitService IntopieceWaitService;
	@Autowired
	private CustomerApplicationIntopieceWaitService customerApplicationIntopieceWaitService;
	@Autowired
	private MaintenanceService maintenanceService;
	//审核信息查询
	@ResponseBody
	@RequestMapping(value = "/ipad/intopieces/csBrowse.json", method = { RequestMethod.GET })
	public String csBrowse(@ModelAttribute IntoPiecesFilter filter,HttpServletRequest request) {
		
		
		filter.setNextNodeName(request.getParameter("nextNodeName"));
		filter.setUserId(request.getParameter("userId"));
		QueryResult<CustomerApplicationIntopieceWaitForm> result = jnpadIntopiecesDecisionService.findCustomerApplicationIntopieceDecison(filter);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(result, jsonConfig);
		return json.toString();
		
	}
	
	
	//显示初审信息
	@ResponseBody
	@RequestMapping(value = "/ipad/intopieces/csInfo.json", method = { RequestMethod.GET })
	public String csInfo(HttpServletRequest request){
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
//		List<ManagerInfoForm> result = jnpadIntopiecesDecisionService.findManagerInfo();
//		StringBuffer s=new StringBuffer();
//		Iterator<ManagerInfoForm> it = result.iterator(); 
//        while(it.hasNext()){  
//        ManagerInfoForm manager = it.next();
//        s.append("<option value = '"+manager.getID()+"'>"+manager.getEXTERNAL_ID()+manager.getDISPLAY_NAME()+"</option>"); 
//        } 
//        String ss = s.toString();
//        int size = result.size();  
        String appId = request.getParameter("appId");
		CustomerApplicationInfo customerApplicationInfo = intoPiecesService.findCustomerApplicationInfoById(appId);
		ProductAttribute producAttribute =  productService.findProductAttributeById(customerApplicationInfo.getProductId());
		CustomerInfor  customerInfor  = intoPiecesService.findCustomerManager(customerApplicationInfo.getCustomerId());
		map.put("customerApplicationInfo", customerApplicationInfo);
		map.put("producAttribute", producAttribute);
		map.put("customerInfor",customerInfor);
//		map.put("managerInfo", result);
		map.put("prodCreditRange",producAttribute.getProdCreditRange());
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(map, jsonConfig);
		return json.toString();
		
	}
	
	//下拉框选择客户经理信息
	@ResponseBody
	@RequestMapping(value = "/ipad/intopieces/managerInfoi.json", method = { RequestMethod.GET })
	public String managerInfo(HttpServletRequest request){
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		List<ManagerInfoForm> result = jnpadIntopiecesDecisionService.findManagerInfo();
		Iterator<ManagerInfoForm> it = result.iterator(); 
		int i = 1;
		int j = 1;
		String  s="";
	        while(it.hasNext()){  
	        	ManagerInfoForm mana = it.next();
	        	s =s+"<option value = '"+mana.getID()+"'>"+mana.getEXTERNAL_ID()+mana.getDISPLAY_NAME()
	        	+"</option>";
	        	
	        }
	       map.put("manager", s);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(map,jsonConfig);
		return json.toString();
	}
	
	//下拉框选择审贷老师信息
	@ResponseBody
	@RequestMapping(value = "/ipad/intopieces/teacherInfo.json", method = { RequestMethod.GET })
	public String teacherInfo(HttpServletRequest request){
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		List<ManagerInfoForm> result = jnpadIntopiecesDecisionService.findteacherInfo();
		Iterator<ManagerInfoForm> it = result.iterator(); 
		int i = 1;
		int j = 1;
		String  s="";
		while(it.hasNext()){  
			ManagerInfoForm mana = it.next();
			s =s+"<option value = '"+mana.getID()+"'>"+mana.getDISPLAY_NAME()
			+"</option>";
			
		}
		map.put("manager", s);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(map,jsonConfig);
		return json.toString();
	}
	
	
	/**
	 * 提交信息
	 * @param request
	 * @return
	 */
	
	@ResponseBody
	@RequestMapping(value = "/ipad/intopieces/updateAll.json")
	@JRadOperation(JRadOperation.APPROVE)
	public String update(HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();

		if (returnMap.isSuccess()) {
			try {
				jnpadIntopiecesDecisionService.updateCustomerApplicationProcessBySerialNumber(request);
				returnMap.put("message","提交成功");
			} catch (Exception e) {
				returnMap.put("message","提交失败");
			}
		}
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(returnMap, jsonConfig);
		return json.toString();
	}
	
	
//	//审贷决议
//	@ResponseBody
//	@RequestMapping(value = "/ipad/intopieces/sdBrowse.json", method = { RequestMethod.GET })
//	public String sdbrowse(@ModelAttribute IntoPiecesFilter filter,HttpServletRequest request) {
//		filter.setRequest(request);
//		String userId = request.getParameter("userId");
//		filter.setNextNodeName("审贷决议");
//		filter.setUserId(userId);
//		QueryResult<CustomerApplicationIntopieceWaitForm> result = jnpadIntopiecesDecisionService.findCustomerApplicationIntopieceDecison(filter);
//		JsonConfig jsonConfig = new JsonConfig();
//		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
//		JSONObject json = JSONObject.fromObject(result, jsonConfig);
//		return json.toString();
//	}
//	
	//显示审议决议
	@ResponseBody
	@RequestMapping(value = "/ipad/intopieces/sdjy.json", method = { RequestMethod.GET })
	public String input_decision(HttpServletRequest request) {
		
//		List<ManagerInfoForm> result = jnpadIntopiecesDecisionService.findManagerInfo();
//		StringBuffer s=new StringBuffer();
//		Iterator<ManagerInfoForm> it = result.iterator(); 
//        while(it.hasNext()){  
//        ManagerInfoForm manager = it.next();
//        s.append("<option value = '"+manager.getID()+"'>"+manager.getEXTERNAL_ID()+manager.getDISPLAY_NAME()+"</option>"); 
//        } 
//        String ss = s.toString();
//		int size = result.size();
		String appId = request.getParameter("appId");
		List<ProductAttributes> productList = jnpadIntopiecesDecisionService.findProductList();
		CustomerApplicationInfo customerApplicationInfo = intoPiecesService.findCustomerApplicationInfoById(appId);
//		CustomerApplicationProcessForm  processForm  = intoPiecesService.findCustomerApplicationProcessById(appId);
		ProductAttribute producAttribute =  productService.findProductAttributeById(customerApplicationInfo.getProductId());
		AppManagerAuditLog appManagerAuditLog = jnpadIntopiecesDecisionService.findAppManagerAuditLog(appId,"1");
		CustomerInfor  customerInfor  = intoPiecesService.findCustomerManager(customerApplicationInfo.getCustomerId());
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("customerApplicationInfo", customerApplicationInfo);
		map.put("producAttribute", producAttribute);
//		map.put("customerApplicationProcess", processForm);
		map.put("appManagerAuditLog", appManagerAuditLog);
		map.put("custManagerId", customerInfor.getUserId());
		map.put("prodCreditRange",producAttribute.getProdCreditRange());
		map.put("productList",productList);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(map, jsonConfig);
		return json.toString();
	}
	

	
	
	//显示部门审批
	@ResponseBody
	@RequestMapping(value = "/ipad/intopieces/bzsp.json", method = { RequestMethod.GET })
	public String bumen_decision(HttpServletRequest request) {
		String appId = request.getParameter("appId");
		CustomerApplicationInfo customerApplicationInfo = intoPiecesService.findCustomerApplicationInfoById(appId);
//		CustomerApplicationProcessForm  processForm  = intoPiecesService.findCustomerApplicationProcessById(appId);
		ProductAttribute producAttribute =  productService.findProductAttributeById(customerApplicationInfo.getProductId());
		AppManagerAuditLog appManagerAuditLog1 = jnpadIntopiecesDecisionService.findAppManagerAuditLog(appId,"1");
		AppManagerAuditLog appManagerAuditLog2 = jnpadIntopiecesDecisionService.findAppManagerAuditLog(appId,"2");
		List<ProductAttributes> productList = jnpadIntopiecesDecisionService.findProductList();
		CustomerInfor  customerInfor  = intoPiecesService.findCustomerManager(customerApplicationInfo.getCustomerId());
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("customerApplicationInfo", customerApplicationInfo);
		map.put("productList",productList);
		map.put("producAttribute", producAttribute);
//		map.put("customerApplicationProcess", processForm);
		map.put("appManagerAuditLog1", appManagerAuditLog1);
		map.put("appManagerAuditLog2", appManagerAuditLog2);
		map.put("custManagerId", customerInfor.getUserId());
		map.put("prodCreditRange",producAttribute.getProdCreditRange());
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(map, jsonConfig);
		return json.toString();
	}
	//显示零售部业务负责人审批
	@ResponseBody
	@RequestMapping(value = "/ipad/intopieces/lsbfzr.json", method = { RequestMethod.GET })
	public String lingshou_decision(HttpServletRequest request) {
		String appId = request.getParameter("appId");
		CustomerApplicationInfo customerApplicationInfo = intoPiecesService.findCustomerApplicationInfoById(appId);
//		CustomerApplicationProcessForm  processForm  = intoPiecesService.findCustomerApplicationProcessById(appId);
		ProductAttribute producAttribute =  productService.findProductAttributeById(customerApplicationInfo.getProductId());
		AppManagerAuditLog appManagerAuditLog1 = jnpadIntopiecesDecisionService.findAppManagerAuditLog(appId,"1");
		AppManagerAuditLog appManagerAuditLog2 = jnpadIntopiecesDecisionService.findAppManagerAuditLog(appId,"2");
		AppManagerAuditLog appManagerAuditLog3 = jnpadIntopiecesDecisionService.findAppManagerAuditLog(appId,"3");
		CustomerInfor  customerInfor  = intoPiecesService.findCustomerManager(customerApplicationInfo.getCustomerId());
		List<ProductAttributes> productList = jnpadIntopiecesDecisionService.findProductList();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("productList",productList);
		map.put("customerApplicationInfo", customerApplicationInfo);
		map.put("producAttribute", producAttribute);
//		map.put("customerApplicationProcess", processForm);
		map.put("appManagerAuditLog1", appManagerAuditLog1);
		map.put("appManagerAuditLog2", appManagerAuditLog2);
		map.put("appManagerAuditLog3", appManagerAuditLog3);
		map.put("custManagerId", customerInfor.getUserId());
		map.put("prodCreditRange",producAttribute.getProdCreditRange());
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(map, jsonConfig);
		return json.toString();
	}
	//显示行长审批
	@ResponseBody
	@RequestMapping(value = "/ipad/intopieces/hzspjl.json", method = { RequestMethod.GET })
	public String hangzhang_decision(HttpServletRequest request) {
		String appId = request.getParameter("appId");
		CustomerApplicationInfo customerApplicationInfo = intoPiecesService.findCustomerApplicationInfoById(appId);
//		CustomerApplicationProcessForm  processForm  = intoPiecesService.findCustomerApplicationProcessById(appId);
		ProductAttribute producAttribute =  productService.findProductAttributeById(customerApplicationInfo.getProductId());
		AppManagerAuditLog appManagerAuditLog1 = jnpadIntopiecesDecisionService.findAppManagerAuditLog(appId,"1");
		AppManagerAuditLog appManagerAuditLog2 = jnpadIntopiecesDecisionService.findAppManagerAuditLog(appId,"2");
		AppManagerAuditLog appManagerAuditLog3 = jnpadIntopiecesDecisionService.findAppManagerAuditLog(appId,"3");
		AppManagerAuditLog appManagerAuditLog4 = jnpadIntopiecesDecisionService.findAppManagerAuditLog(appId,"4");
		CustomerInfor  customerInfor  = intoPiecesService.findCustomerManager(customerApplicationInfo.getCustomerId());
		List<ProductAttributes> productList = jnpadIntopiecesDecisionService.findProductList();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("productList",productList);
		map.put("customerApplicationInfo", customerApplicationInfo);
		map.put("producAttribute", producAttribute);
//		map.put("customerApplicationProcess", processForm);
		map.put("appManagerAuditLog1", appManagerAuditLog1);
		map.put("appManagerAuditLog2", appManagerAuditLog2);
		map.put("appManagerAuditLog3", appManagerAuditLog3);
		map.put("appManagerAuditLog4", appManagerAuditLog4);
		map.put("custManagerId", customerInfor.getUserId());
		map.put("prodCreditRange",producAttribute.getProdCreditRange());
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(map, jsonConfig);
		return json.toString();
	}
	
//	//提交审贷决议
//	@ResponseBody
//	@RequestMapping(value = "update.json", method = { RequestMethod.GET })
//	public JRadReturnMap update(@ModelAttribute CustomerApplicationInfo customerApplicationInfo,HttpServletRequest request) {
//		JRadReturnMap returnMap = new JRadReturnMap();
//		try {
//			customerApplicationInfo.setActualQuote(customerApplicationInfo.getDecisionAmount());
//			jnpadIntopiecesDecisionService.update(customerApplicationInfo,request);
//			returnMap.addGlobalMessage(CHANGE_SUCCESS);
//		} catch (Exception e) {
//			return WebRequestHelper.processException(e);
//		}
//
//		return returnMap;
//	}

	@ResponseBody
	@RequestMapping(value = "/ipad/intopieces/productInfo.json", method = { RequestMethod.GET })
	public String productInfo(HttpServletRequest request) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		List<ProductAttributes> result = jnpadIntopiecesDecisionService.findProductList();
		map.put("result", result);
		map.put("size", result.size());
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(map, jsonConfig);
		return json.toString();
		
	}
	
	
	//pad显示进件初审 界面
		@ResponseBody
		@RequestMapping(value = "/ipad/intopieces/input_decision_chusheng.json", method = { RequestMethod.GET })
		public String input_decision_chusheng(HttpServletRequest request) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			String appId = request.getParameter("appId");
			String userId = request.getParameter("userId");
			String cid = request.getParameter("cid");
			String pid = request.getParameter("pid");
			CustomerSqInfo SqInfo=SqInfoService.selectSqInfoBycpId(pid, cid);
			CustomerApplicationInfo customerApplicationInfo = intoPiecesService.findCustomerApplicationInfoById(appId);
			ProductAttribute producAttribute =  productService.findProductAttributeById(customerApplicationInfo.getProductId());
			CustomerInfor  customerInfor  = intoPiecesService.findCustomerManager(customerApplicationInfo.getCustomerId());
			List<CustomerApplicationIntopieceWaitForm> list=customerApplicationIntopieceWaitService.findSpRy(userId);
			for(int a=0;a<list.size();a++){
				if(list.get(a).getUserId().equals(userId)){
					list.remove(a);
				}
			}
			map.put("customerApplicationInfo", customerApplicationInfo);
			map.put("producAttribute", producAttribute);
			map.put("custManagerId", customerInfor.getUserId());
			map.put("customerApplicationInfo", customerApplicationInfo);
			map.put("list", list);
			map.put("size", list.size());
			map.put("SqInfo", SqInfo);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
			JSONObject json = JSONObject.fromObject(map, jsonConfig);
			return json.toString();
		}
		
		/**
		 * 初审成功添加初审以及审贷委信息
		 * @param CustomerSpUser
		 * @param AppManagerAuditLog
		 * @param CustormerSdwUser
		 * @param request
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/ipad/intopieces/insertCSPC.json", method = { RequestMethod.GET })
		public String insertCSPC(@ModelAttribute CustomerSpUser CustomerSpUser,@ModelAttribute AppManagerAuditLog AppManagerAuditLog,@ModelAttribute CustormerSdwUser CustormerSdwUser,HttpServletRequest request) {
			String userId = request.getParameter("userId");
			List<CustomerSpUser> list=new ArrayList<CustomerSpUser>();
			String cid=request.getParameter("customerId");
			String pid=request.getParameter("productId");
			AppManagerAuditLog result=SdwUserService.selectaId(cid,pid);
			AppManagerAuditLog.setId(IDGenerator.generateID());
			AppManagerAuditLog.setApplicationId(result.getApplicationId());
			AppManagerAuditLog.setAuditType("1");
			AppManagerAuditLog.setExamineLv(request.getParameter("decisionRate"));
			AppManagerAuditLog.setQx(request.getParameter("qx"));
			AppManagerAuditLog.setExamineAmount(request.getParameter("decisionAmount"));
			AppManagerAuditLog.setUserId_1(request.getParameter("cyUser1"));
			AppManagerAuditLog.setUserId_2(request.getParameter("cyUser2"));
			AppManagerAuditLog.setUserId_3(request.getParameter("fdUser"));
			AppManagerAuditLog.setUserId_4(userId);
			AppManagerAuditLog.setDbfs(request.getParameter("dbfs"));
			SdwUserService.insertCsJl(AppManagerAuditLog);
			//初审通过状态
			String applicationId=result.getApplicationId();
			String userId1=userId;
			Date times=new Date();
			String money=request.getParameter("decisionAmount");
			SdwUserService.updateCSZT(userId1,times,money,applicationId);
				CustomerSpUser.setId(IDGenerator.generateID());
				CustomerSpUser.setCid(cid);
				CustomerSpUser.setPid(pid);
				CustomerSpUser.setCapid(result.getApplicationId());
				CustomerSpUser.setTime(new Date());
				CustomerSpUser.setStatus("0");
				CustomerSpUser c=new CustomerSpUser();
				c.setSpuserid(request.getParameter("user_Id1"));
				c.setZsw(1);
				list.add(0, c);
				CustomerSpUser c1=new CustomerSpUser();
				c1.setSpuserid(request.getParameter("user_Id2"));
				c1.setZsw(0);
				list.add(1, c1);
				CustomerSpUser c2=new CustomerSpUser();
				c2.setSpuserid(request.getParameter("user_Id3"));
				c2.setZsw(0);
				list.add(2, c2);
				for(int sd=0;sd<list.size();sd++){
					CustomerSpUser.setSpuserid(list.get(sd).getSpuserid());
					CustomerSpUser.setZsw(list.get(sd).getZsw());
					UserService.addSpUser(CustomerSpUser);
				}
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("map", "添加成功");
				JsonConfig jsonConfig = new JsonConfig();
				jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
				JSONObject json = JSONObject.fromObject(map, jsonConfig);
				return json.toString();
		}
		/**
		 * pad审贷进件列表
		 * @param filter
		 * @param request
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/ipad/intopieces/insertSD.json", method = { RequestMethod.GET })
		public String browse(@ModelAttribute IntoPiecesFilters filter,HttpServletRequest request) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			String userId =request.getParameter("userId");
			filter.setUserId(userId);
			List<IntoPiecesFilters> result = customerApplicationIntopieceWaitService.findCustomerApplicationIntopieceDecisons1(filter);
			map.put("result", result);
			map.put("size", result.size());
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
			JSONObject json = JSONObject.fromObject(map, jsonConfig);
			return json.toString();
		}
		
		/**
		 * 审贷时查看此客户的申请信息
		 * @param request
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/ipad/intopieces/selectSDSQ.json", method = { RequestMethod.GET })
		public String selectSDSQ(HttpServletRequest request) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			String appId = request.getParameter("appId");
			String cid = request.getParameter("cid");
			String pid = request.getParameter("pid");
			String userId = request.getParameter("userId");
			Integer sdw = Integer.parseInt(request.getParameter("sdw"));
			IntoPiecesFilters IntoPiecesFilters=new IntoPiecesFilters();
			IntoPiecesFilters.setUserId(userId);
			if(sdw==1){
				IntoPiecesFilters.setGroupName("主审");
			}else{
				IntoPiecesFilters.setGroupName("副审");
			}
			String uId=userId;
			CustomerSqInfo SqInfo=SqInfoService.selectSqInfoBycpId(pid, cid);
			CustomerApplicationInfo customerApplicationInfo = intoPiecesService.findCustomerApplicationInfoById(appId);
			CustomerApplicationProcessForm  processForm  = intoPiecesService.findCustomerApplicationProcessById(appId);
			ProductAttribute producAttribute =  productService.findProductAttributeById(customerApplicationInfo.getProductId());
			List<AppManagerAuditLog> appManagerAuditLog = productService.findAppManagerAuditLog(appId,"1");
			CustomerInfor  customerInfor  = intoPiecesService.findCustomerManager(customerApplicationInfo.getCustomerId());
			AppManagerAuditLog result=SdwUserService.selectCSJLAPC(appId,uId);
			JnpadCsSdModel sdwinfo=SdwUserService.findCsSds(appId,uId);
			map.put("customerApplicationInfo", customerApplicationInfo);
			map.put("producAttribute", producAttribute);
			map.put("customerApplicationProcess", processForm);
			map.put("appManagerAuditLog", appManagerAuditLog.get(0));
			map.put("custManagerId", customerInfor.getUserId());
			map.put("result", result);
			map.put("SqInfo", SqInfo);
			map.put("sdwinfo", sdwinfo);
			map.put("IntoPiecesFilters", IntoPiecesFilters);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
			JSONObject json = JSONObject.fromObject(map, jsonConfig);
			return json.toString();
		}
		
		
		/**
		 * 当前审贷委审批
		 * @param RiskCustomer
		 * @param CustormerSdwUser
		 * @param IntoPieces
		 * @param request
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value="/ipad/intopieces/insertsdjycs.json", method = { RequestMethod.GET })
		public String insertsdjyPCcs(@ModelAttribute CustormerSdwUser CustormerSdwUser,@ModelAttribute IntoPieces IntoPieces,@ModelAttribute RiskCustomer RiskCustomer,@ModelAttribute CustomerSpUser CustomerSpUser,HttpServletRequest request) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			String userId=request.getParameter("userId");
			CustomerSpUser.setSpje(request.getParameter("decisionAmount"));
			CustomerSpUser.setSptime(new Date());
			CustomerSpUser.setSpqx(request.getParameter("qx"));
			CustomerSpUser.setSpuserid(userId);
			CustomerSpUser.setBeizhu(request.getParameter("SDWUSER1YJ"));
			CustomerSpUser.setSplv(request.getParameter("decisionRate"));
			CustomerSpUser.setCapid(request.getParameter("id"));
			CustomerSpUser.setJlyys(request.getParameter("decisionRefusereason"));
			CustomerSpUser.setDbfs(request.getParameter("dbfs"));
			//如果前两位审贷委审贷状况都为通过，且当前审贷委的审贷状况也为通过，则比较三位审贷的金额，利息，期限
			if(request.getParameter("status").equals("approved")){
				CustomerSpUser.setStatus("1");
				
				//查询是否该进件已经有两位审贷委审贷并且状态都为通过
				List<IntoPieces> spry=SdwUserService.findEndCount(request.getParameter("id"));
				//如果是,比较三位审贷委审批的金额，利息，期限，担保方式
				if(spry.size()>0 && spry.size()==2){
					//如果金额，利息，期限，担保方式相同直接审贷成功，审贷结束，不需到终审
					if(spry.get(0).getApplyQuota().equals(CustomerSpUser.getSpje()) && spry.get(1).getApplyQuota().equals(CustomerSpUser.getSpje()) &&
					spry.get(0).getSplv().equals(CustomerSpUser.getSplv()) && spry.get(1).getSplv().equals(CustomerSpUser.getSplv()) &&
					spry.get(0).getSpqx().equals(CustomerSpUser.getSpqx()) && spry.get(1).getSpqx().equals(CustomerSpUser.getSpqx())){
						IntoPieces.setFinal_approval(request.getParameter("decisionAmount"));
						IntoPieces.setStatus("approved");
						IntoPieces.setId(request.getParameter("id"));
						IntoPieces.setCreatime(new Date());
						IntoPieces.setApplyQuota(request.getParameter("decisionAmount"));
						IntoPieces.setSplv(request.getParameter("decisionRate"));
						IntoPieces.setSpqx(request.getParameter("qx"));
						IntoPieces.setDbfs(request.getParameter("dbfs"));
						int b=SdwUserService.updateCustormerInfoSdwUser(IntoPieces);
						if(b>0){
							int d=SdwUserService.updateCustormerProSdwUser(IntoPieces);
							if(d>0){
								map.put("message", "提交成功");
							}
						}else{
							map.put("message", "提交失败");
						}
					}else{
						//如果金额，利息，期限，担保方式不同需到终审
						
							CustormerSdwUser.setId(IDGenerator.generateID());
							CustormerSdwUser.setCAPID(request.getParameter("id"));
							CustormerSdwUser.setTIME(new Date());
							CustormerSdwUser.setSDWUSER1(spry.get(0).getId());
							CustormerSdwUser.setZSW(spry.get(0).getZsw());
							SdwUserService.insertCustormerSdwUser1(CustormerSdwUser);
							CustormerSdwUser CustormerSdwUser1=new CustormerSdwUser();
							CustormerSdwUser1.setId(IDGenerator.generateID());
							CustormerSdwUser1.setCAPID(request.getParameter("id"));
							CustormerSdwUser1.setTIME(new Date());
							CustormerSdwUser1.setSDWUSER1(spry.get(1).getId());
							CustormerSdwUser1.setZSW(spry.get(1).getZsw());
							SdwUserService.insertCustormerSdwUser1(CustormerSdwUser1);
							CustormerSdwUser CustormerSdwUser2=new CustormerSdwUser();
							CustormerSdwUser2.setId(IDGenerator.generateID());
							CustormerSdwUser2.setCAPID(request.getParameter("id"));
							CustormerSdwUser2.setTIME(new Date());
							CustormerSdwUser2.setSDWUSER1(userId);
							if(spry.get(0).getZsw()==0 && spry.get(1).getZsw()==0){
								CustormerSdwUser2.setZSW(1);
							}else{
								CustormerSdwUser2.setZSW(0);
							}
							SdwUserService.insertCustormerSdwUser1(CustormerSdwUser2);
						
					}
				}
			}
			//如果当前审贷委拒绝或退回该进件，进件直接审批完成退回或拒绝到申请客户经理处，不必通过终审
			else if(request.getParameter("status").equals("refuse")){
				CustomerSpUser.setStatus("2");
				
				IntoPieces.setStatus("refuse");
				IntoPieces.setCreatime(new Date());
				IntoPieces.setId(request.getParameter("id"));
				IntoPieces.setUserId(userId);
				IntoPieces.setREFUSAL_REASON(request.getParameter("decisionRefusereason"));
				int c=SdwUserService.updateCustormerInfoSdwUser(IntoPieces);
				if(c>0){
					int d=SdwUserService.updateCustormerProSdwUser(IntoPieces);
					if(d>0){
						RiskCustomer.setCustomerId(request.getParameter("customerId"));
						RiskCustomer.setProductId(request.getParameter("productId"));
						RiskCustomer.setRiskCreateType("manual");
						RiskCustomer.setRefuseReason(request.getParameter("decisionRefusereason"));
						RiskCustomer.setCREATED_TIME(new Date());
						RiskCustomer.setUserId(userId);
						RiskCustomer.setCustManagerId(request.getParameter("userId"));
						RiskCustomer.setId(IDGenerator.generateID());
						int e=SdwUserService.insertRiskSdwUser(RiskCustomer);
						//拒绝时修改节点状态
						String applicationId=request.getParameter("id");
						Date times=new Date();
						SdwUserService.updateHistorys(userId,times,applicationId);
						if(e>0){
							map.put("message", "提交成功");
						}else{
							map.put("message", "提交失败");
						}
					}else{
						map.put("message", "提交失败");
					}
				}else{
					map.put("message", "提交失败");
				}
			
			}else if(request.getParameter("status").equals("returnedToFirst")){
				CustomerSpUser.setStatus("3");
				IntoPieces.setStatus("returnedToFirst");
				IntoPieces.setId(request.getParameter("id"));
				IntoPieces.setFallBackReason(request.getParameter("decisionRefusereason"));
				IntoPieces.setUserId(userId);
				IntoPieces.setCreatime(new Date());
				int c=SdwUserService.updateCustormerInfoSdwUser(IntoPieces);
				//退回时修改节点状态
				String applicationId=request.getParameter("id");
				Date times=new Date();
				SdwUserService.updateHistory(userId,times,applicationId);
				if(c>0){
					int d=SdwUserService.updateCustormerProSdwUser(IntoPieces);
					if(d>0){
						map.put("message", "提交成功");	
					}else{
						map.put("message", "提交失败");
					}
				}
			}
			//添加当前客户经理的审贷
			int a=UserService.addSpUser1(CustomerSpUser);
			if(a>0){
				map.put("message", "提交成功");	
			}else{
				map.put("message", "提交失败");
			}
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
			JSONObject json = JSONObject.fromObject(map, jsonConfig);
			return json.toString();
	}
		
		
		
		/**
		 * 最终审贷
		 * @param request
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/ipad/intopieces/selectZzSD.json", method = { RequestMethod.GET })
		public String selectZzSD(@ModelAttribute IntoPiecesFilters filter,HttpServletRequest request) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			String userId = request.getParameter("userId");
			filter.setUserId(userId);
			List<IntoPiecesFilters> result = customerApplicationIntopieceWaitService.findCustomerApplicationIntopieceDecisones1(filter,request);
			map.put("result", result);
			map.put("size", result.size());
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
			JSONObject json = JSONObject.fromObject(map, jsonConfig);
			return json.toString();
		}
		
		
		/**
		 * 显示终审信息
		 * @param filter
		 * @param request
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/ipad/intopieces/selecZzSSqxx.json", method = { RequestMethod.GET })
		public String selecZzSSqxx(@ModelAttribute IntoPiecesFilters filter,HttpServletRequest request) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			IntoPiecesFilters IntoPieces=null;
			String appId = request.getParameter("appId");
			String cid = request.getParameter("cid");
			String pid = request.getParameter("pid");
			Integer sdw = Integer.parseInt(request.getParameter("sdw"));
			String uId=request.getParameter("userId");
			String name=request.getParameter("name");
			CustomerSqInfo SqInfo=SqInfoService.selectSqInfoBycpId(pid, cid);
			CustomerApplicationInfo customerApplicationInfo = intoPiecesService.findCustomerApplicationInfoById(appId);
			ProductAttribute producAttribute =  productService.findProductAttributeById(customerApplicationInfo.getProductId());
			CustomerInfor  customerInfor  = intoPiecesService.findCustomerManager(customerApplicationInfo.getCustomerId());
			List<AppManagerAuditLog> appManagerAuditLog = productService.findAppManagerAuditLog(appId,"1");
			List<JnpadCsSdModel> sdwinfos=SdwUserService.findCsSdId(appId);
			String sdwId1 = null;
			String sdwId2 = null;
			String sdwId3 = null;
				sdwId1=sdwinfos.get(0).getSpuserid();
				sdwId2=sdwinfos.get(1).getSpuserid();
				sdwId3=sdwinfos.get(2).getSpuserid();
			JnpadCsSdModel sdwinfo1=SdwUserService.findBySdwId(sdwId1,appId);
			JnpadCsSdModel sdwinfo2=SdwUserService.findBySdwId(sdwId2,appId);
			JnpadCsSdModel sdwinfo3=SdwUserService.findBySdwId(sdwId3,appId);
			CustomerInfor CustomerInfor1=new CustomerInfor();
			if(sdw==1){
				CustomerInfor1.setChineseName(name);
				CustomerInfor1.setCardId("主审");
			}else{
				CustomerInfor1.setChineseName(name);
				CustomerInfor1.setCardId("副审");
				 IntoPieces= customerApplicationIntopieceWaitService.findzsw1(appId);
			}
			map.put("IntoPieces", IntoPieces);
			map.put("customerApplicationInfo", customerApplicationInfo);
			map.put("producAttribute", producAttribute);
			map.put("custManagerId", customerInfor.getUserId());
			map.put("SqInfo", SqInfo);
			map.put("appManagerAuditLog", appManagerAuditLog.get(0));
			map.put("sdwinfo1", sdwinfo1);
			map.put("sdwinfo2", sdwinfo2);
			map.put("sdwinfo3", sdwinfo3);
			map.put("CustomerInfor1", CustomerInfor1);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
			JSONObject json = JSONObject.fromObject(map, jsonConfig);
			return json.toString();
		}
		
		/**
		 * 终审决议
		 * @param RiskCustomer
		 * @param CustormerSdwUser
		 * @param IntoPieces
		 * @param request
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value="/ipad/intopieces/insertsdjy.json", method = { RequestMethod.GET })
		public String insertsdjy(@ModelAttribute RiskCustomer RiskCustomer,@ModelAttribute CustormerSdwUser CustormerSdwUser,@ModelAttribute IntoPieces IntoPieces,HttpServletRequest request) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			String userId=request.getParameter("userId");
			CustormerSdwUser.setSDJE(request.getParameter("decisionAmount"));
			CustormerSdwUser.setSDTIME(new Date());
			CustormerSdwUser.setSDQX(request.getParameter("qx"));
			CustormerSdwUser.setSDWUSER1YJ(request.getParameter("SDWUSER1YJ"));
			CustormerSdwUser.setLV(request.getParameter("decisionRate"));
			CustormerSdwUser.setCAPID(request.getParameter("id"));
			CustormerSdwUser.setJJYY(request.getParameter("decisionRefusereason"));
			CustormerSdwUser.setZSW(Integer.parseInt(request.getParameter("zsw")));
			CustormerSdwUser.setDbfs(request.getParameter("dbfs"));
			CustormerSdwUser.setSDWUSER1(userId);
			SdwUserService.updateCustormerSdwUser1(CustormerSdwUser);
			//如果进件通过，则需判断是否为主审
			if(request.getParameter("status").equals("approved")){
				//如果进件通过，则需判断是否为主审,如果不是需要判断另一个副审是否审批，并且审批结果为通过
				if(CustormerSdwUser.getZSW()!=1){
					IntoPiecesFilters time=IntopieceWaitService.findfsw(request.getParameter("id"), userId);
					//如果另一个复审已经确定，进件直接通过，整个流程走完
					if(time!=null){
						//查询主审决审的信息加入进件表
						IntoPiecesFilters results=IntopieceWaitService.findzsw1(request.getParameter("id"));
						IntoPieces.setFinal_approval(results.getApplyQuota());
						IntoPieces.setStatus("approved");
						IntoPieces.setId(request.getParameter("id"));
						IntoPieces.setCreatime(new Date());
						IntoPieces.setApplyQuota(results.getApplyQuota());
						IntoPieces.setSplv(results.getChineseName());
						IntoPieces.setSpqx(results.getCustomerId());
						IntoPieces.setDbfs(results.getDisplayName());
						int b=SdwUserService.updateCustormerInfoSdwUser(IntoPieces);
						if(b>0){
							int d=SdwUserService.updateCustormerProSdwUser(IntoPieces);
							if(d>0){
								map.put("message", "提交成功");
							}else{
								map.put("message", "提交失败");
							}
					}
				}else{
					map.put("message", "提交成功");
				}
					
				}else{
					map.put("message", "提交成功");
				}
			}
			//如果当前审贷委拒绝或退回该进件，进件直接审批完成退回或拒绝到申请客户经理处
			else if(request.getParameter("status").equals("refuse")){
				
				IntoPieces.setStatus("refuse");
				IntoPieces.setCreatime(new Date());
				IntoPieces.setId(request.getParameter("id"));
				IntoPieces.setUserId(userId);
				IntoPieces.setREFUSAL_REASON(request.getParameter("decisionRefusereason"));
				int c=SdwUserService.updateCustormerInfoSdwUser(IntoPieces);
				if(c>0){
					int d=SdwUserService.updateCustormerProSdwUser(IntoPieces);
					if(d>0){
						RiskCustomer.setCustomerId(request.getParameter("cid"));
						RiskCustomer.setProductId(request.getParameter("pid"));
						RiskCustomer.setRiskCreateType("manual");
						RiskCustomer.setRefuseReason(request.getParameter("decisionRefusereason"));
						RiskCustomer.setCREATED_TIME(new Date());
						RiskCustomer.setUserId(userId);
						RiskCustomer.setCustManagerId(request.getParameter("userId"));
						RiskCustomer.setId(IDGenerator.generateID());
						int e=SdwUserService.insertRiskSdwUser(RiskCustomer);
						//拒绝时修改节点状态
						String applicationId=request.getParameter("id");
						Date times=new Date();
						SdwUserService.updateHistorys(userId,times,applicationId);
						if(e>0){
							map.put("message", "提交成功");
						}else{
							map.put("message", "提交失败");
						}
					}else{
						map.put("message", "提交失败");
					}
				}else{
					map.put("message", "提交失败");
				}
			
			}else if(request.getParameter("status").equals("returnedToFirst")){
				IntoPieces.setStatus("returnedToFirst");
				IntoPieces.setId(request.getParameter("id"));
				IntoPieces.setFallBackReason(request.getParameter("decisionRefusereason"));
				IntoPieces.setUserId(userId);
				IntoPieces.setCreatime(new Date());
				int c=SdwUserService.updateCustormerInfoSdwUser(IntoPieces);
				//退回时修改节点状态
				String applicationId=request.getParameter("id");
				Date times=new Date();
				SdwUserService.updateHistory(userId,times,applicationId);
				if(c>0){
					int d=SdwUserService.updateCustormerProSdwUser(IntoPieces);
					if(d>0){
						map.put("message", "提交成功");	
					}else{
						map.put("message", "提交失败");
					}
				}
			}
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
			JSONObject json = JSONObject.fromObject(map, jsonConfig);
			return json.toString();
	}
		/**
		 * 查看照片
		 * @param filter
		 * @param request
		 * @return
		 * @throws IOException
		 * @throws SftpException
		 */
		@ResponseBody
		@RequestMapping(value = "/ipad/intopieces/selectAllImage.json", method = { RequestMethod.GET })
		public String selectAllImage(@ModelAttribute ImageMore filter,HttpServletRequest request) throws IOException, SftpException {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			filter.setPid(request.getParameter("pid"));
			filter.setCid(request.getParameter("cid"));
			filter.setLimit(Integer.MAX_VALUE);
			List<ImageMore> result = addIntoPiecesService.selectAllImageByPcId(filter);
			map.put("result", result);
			map.put("size", result.size());
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
			JSONObject json = JSONObject.fromObject(map, jsonConfig);
			return json.toString();
		}
		
		
		
		
		/**
		 * 查看审批历史
		 * 
		 * @param request
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/ipad/intopieces/findApproveHistoryById.page", method = { RequestMethod.GET })
		public String findApproveHistoryById(HttpServletRequest request) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			List<ApproveHistoryForm> historyForms =new ArrayList<ApproveHistoryForm>();
			ApproveHistoryForm ApproveHistoryForm=null;
			ApproveHistoryForm ApproveHistoryForm1=null;
			ApproveHistoryForm ApproveHistoryForm2=null;
			ApproveHistoryForm ApproveHistoryForm3=null;
			ApproveHistoryForm ApproveHistoryForm4=null;
			ApproveHistoryForm ApproveHistoryForm5=null;
			String id = request.getParameter("id");
			if(StringUtils.isNotEmpty(id)){
				//查询初审
				IntoPieces IP=SdwUserService.findsph2(id);
				if(IP.getApplyQuota()==null && !IP.getStatus().equals("audit")){
					ApproveHistoryForm5=new ApproveHistoryForm();
					ApproveHistoryForm5.setStatusName("进件初审");
					if(IP.getStatus().equals("nopass") || IP.getStatus().equals("refuse")){
						ApproveHistoryForm5.setDisplayName(IP.getDisplayName());
						ApproveHistoryForm5.setStartExamineTime(IP.getCreatime1());
						ApproveHistoryForm5.setExamineResult("初审拒绝");
						historyForms.add(0, ApproveHistoryForm5);
					}
					else if(IP.getStatus().equals("nopass_replenish") || IP.getStatus().equals("returnedToFirst")){
						ApproveHistoryForm5.setDisplayName(IP.getDisplayName());
						ApproveHistoryForm5.setStartExamineTime(IP.getCreatime1());
						ApproveHistoryForm5.setExamineResult("初审退回");
						historyForms.add(0, ApproveHistoryForm5);
					}
				}
				 try {
					if(IP.getApplyQuota()!=null){
						ApproveHistoryForm4=new ApproveHistoryForm();
						ApproveHistoryForm4.setExamineResult("初审通过");
						ApproveHistoryForm4.setDisplayName(IP.getDisplayName());
						ApproveHistoryForm4.setStatusName("进件初审");
						ApproveHistoryForm4.setExamineAmount(IP.getApplyQuota());
						ApproveHistoryForm4.setStartExamineTime(IP.getCreatime());
						historyForms.add(0, ApproveHistoryForm4);
						//查询审贷
						List<IntoPieces> result=SdwUserService.findsdh(id);
						if(result.size()==3){
						//查询三位审贷是否都为通过
						if(result.get(0).getStatus().equals("1") && result.get(1).getStatus().equals("1") && result.get(2).getStatus().equals("1")){
							//查询三位审贷结果都一样
							if(result.get(0).getSplv()==result.get(1).getSplv() && result.get(0).getSplv()==result.get(2).getSplv()&&
							result.get(0).getSpqx()==result.get(1).getSpqx() && result.get(0).getSpqx()==result.get(2).getSpqx()&&
							result.get(0).getDbfs()==result.get(1).getDbfs() && result.get(0).getDbfs()==result.get(2).getDbfs()&&
							result.get(0).getApplyQuota()==result.get(1).getApplyQuota() && result.get(0).getApplyQuota()==result.get(2).getApplyQuota()){
							//如果一样无需查询终审
								for(int a=0;a<result.size();a++){
										ApproveHistoryForm=new ApproveHistoryForm();
										ApproveHistoryForm.setStatusName("审贷决议");
										if(result.get(a).getZsw()==1){
											ApproveHistoryForm.setExamineResult("主审"+"审贷通过");
										}else if(result.get(a).getZsw()==0){
											ApproveHistoryForm.setExamineResult("副审"+"审贷通过");
										}
										ApproveHistoryForm.setDisplayName(result.get(a).getDisplayName());
										ApproveHistoryForm.setExamineAmount(result.get(a).getApplyQuota());
										ApproveHistoryForm.setStartExamineTime(result.get(a).getCreatime());
										historyForms.add(a+1, ApproveHistoryForm);
									
								}}else{
									//否则需要查询终审
									for(int a=0;a<result.size();a++){
										ApproveHistoryForm=new ApproveHistoryForm();
										ApproveHistoryForm.setStatusName("审贷决议");
										if(result.get(a).getZsw()==1){
											ApproveHistoryForm.setExamineResult("主审"+"审贷通过");
										}else if(result.get(a).getZsw()==0){
											ApproveHistoryForm.setExamineResult("副审"+"审贷通过");
										}
										ApproveHistoryForm.setDisplayName(result.get(a).getDisplayName());
										ApproveHistoryForm.setExamineAmount(result.get(a).getApplyQuota());
										ApproveHistoryForm.setStartExamineTime(result.get(a).getCreatime());
										historyForms.add(a+1, ApproveHistoryForm);
									
								}
									//查询最终审批
									List<IntoPieces>IntoPieces= SdwUserService.findsph(id);
									if(IntoPieces!=null){
										for(int a=0;a<IntoPieces.size();a++){
											if(IntoPieces.get(a).getApplyQuota()==null){
												 if(IntoPieces.get(a).getStatus().equals("nopass") || IntoPieces.get(a).getStatus().equals("refuse")){
													ApproveHistoryForm3=new ApproveHistoryForm();
													if(IntoPieces.get(a).getZsw()==1){
														ApproveHistoryForm3.setStatusName("主审决审");
													}else if(IntoPieces.get(a).getZsw()==0){
														ApproveHistoryForm3.setStatusName("副审决审");
													}
													ApproveHistoryForm3.setExamineResult("审批拒绝");
													ApproveHistoryForm3.setDisplayName(IntoPieces.get(a).getDisplayName());
													ApproveHistoryForm3.setStartExamineTime(IntoPieces.get(a).getCreatime());
													historyForms.add(a+4, ApproveHistoryForm3);
												}else if(IntoPieces.get(a).getStatus().equals("nopass_replenish") || IntoPieces.get(a).getStatus().equals("returnedToFirst")){
													ApproveHistoryForm3=new ApproveHistoryForm();
													if(IntoPieces.get(a).getZsw()==1){
														ApproveHistoryForm3.setStatusName("主审决审");
													}else if(IntoPieces.get(a).getZsw()==0){
														ApproveHistoryForm3.setStatusName("副审决审");
													}
													ApproveHistoryForm3.setExamineResult("审批退回");
													ApproveHistoryForm3.setDisplayName(IntoPieces.get(a).getDisplayName());
													ApproveHistoryForm3.setStartExamineTime(IntoPieces.get(a).getCreatime());
													historyForms.add(a+4, ApproveHistoryForm3);
												}
											}else{
												ApproveHistoryForm3=new ApproveHistoryForm();
												if(IntoPieces.get(a).getZsw()==1){
													ApproveHistoryForm3.setStatusName("主审决审");
												}else if(IntoPieces.get(a).getZsw()==0){
													ApproveHistoryForm3.setStatusName("副审决审");
												}
												ApproveHistoryForm3.setExamineResult("审批通过");
												ApproveHistoryForm3.setDisplayName(IntoPieces.get(a).getDisplayName());
												ApproveHistoryForm3.setExamineAmount(IntoPieces.get(a).getApplyQuota());
												ApproveHistoryForm3.setStartExamineTime(IntoPieces.get(a).getCreatime());
												historyForms.add(a+4, ApproveHistoryForm3);
											}
										}
									}
							}
						}else{
							for(int a=0;a<result.size();a++){
								if(result.get(a).getStatus().equals("1")){
									ApproveHistoryForm=new ApproveHistoryForm();
									ApproveHistoryForm.setStatusName("审贷决议");
									if(result.get(a).getZsw()==1){
										ApproveHistoryForm.setExamineResult("主审"+"审贷通过");
									}else if(result.get(a).getZsw()==0){
										ApproveHistoryForm.setExamineResult("副审"+"审贷通过");
									}
									ApproveHistoryForm.setDisplayName(result.get(a).getDisplayName());
									ApproveHistoryForm.setExamineAmount(result.get(a).getApplyQuota());
									ApproveHistoryForm.setStartExamineTime(result.get(a).getCreatime());
									historyForms.add(a+1, ApproveHistoryForm);
								
								}else
									if(result.get(a).getStatus().equals("2")){
										ApproveHistoryForm1=new ApproveHistoryForm();
										ApproveHistoryForm1.setStatusName("审贷决议");
										if(result.get(a).getZsw()==1){
											ApproveHistoryForm1.setExamineResult("主审"+"审贷拒绝");
										}else if(result.get(a).getZsw()==0){
											ApproveHistoryForm1.setExamineResult("副审"+"审贷拒绝");
										}
										ApproveHistoryForm1.setDisplayName(result.get(a).getDisplayName());
										ApproveHistoryForm1.setStartExamineTime(result.get(a).getCreatime());
										historyForms.add(a+1, ApproveHistoryForm1);
										
									}else
										if(result.get(a).getStatus().equals("3")){
											ApproveHistoryForm2=new ApproveHistoryForm();
											ApproveHistoryForm2.setStatusName("审贷决议");
											if(result.get(a).getZsw()==1){
												ApproveHistoryForm2.setExamineResult("主审"+"审贷退回");
											}else if(result.get(a).getZsw()==0){
												ApproveHistoryForm2.setExamineResult("副审"+"审贷退回");
											}
											ApproveHistoryForm2.setDisplayName(result.get(a).getDisplayName());
											ApproveHistoryForm2.setStartExamineTime(result.get(a).getCreatime());
											historyForms.add(a+1, ApproveHistoryForm2);
										}
							}
						}
						}else{
							for(int a=0;a<result.size();a++){
								if(result.get(a).getStatus().equals("1")){
									ApproveHistoryForm=new ApproveHistoryForm();
									ApproveHistoryForm.setStatusName("审贷决议");
									if(result.get(a).getZsw()==1){
										ApproveHistoryForm.setExamineResult("主审"+"审贷通过");
									}else if(result.get(a).getZsw()==0){
										ApproveHistoryForm.setExamineResult("副审"+"审贷通过");
									}
									ApproveHistoryForm.setDisplayName(result.get(a).getDisplayName());
									ApproveHistoryForm.setExamineAmount(result.get(a).getApplyQuota());
									ApproveHistoryForm.setStartExamineTime(result.get(a).getCreatime());
									historyForms.add(a+1, ApproveHistoryForm);
								
								}else
									if(result.get(a).getStatus().equals("2")){
										ApproveHistoryForm1=new ApproveHistoryForm();
										ApproveHistoryForm1.setStatusName("审贷决议");
										if(result.get(a).getZsw()==1){
											ApproveHistoryForm1.setExamineResult("主审"+"审贷拒绝");
										}else if(result.get(a).getZsw()==0){
											ApproveHistoryForm1.setExamineResult("副审"+"审贷拒绝");
										}
										ApproveHistoryForm1.setDisplayName(result.get(a).getDisplayName());
										ApproveHistoryForm1.setStartExamineTime(result.get(a).getCreatime());
										historyForms.add(a+1, ApproveHistoryForm1);
										
									}else
										if(result.get(a).getStatus().equals("3")){
											ApproveHistoryForm2=new ApproveHistoryForm();
											ApproveHistoryForm2.setStatusName("审贷决议");
											if(result.get(a).getZsw()==1){
												ApproveHistoryForm.setExamineResult("主审"+"审贷退回");
											}else if(result.get(a).getZsw()==0){
												ApproveHistoryForm.setExamineResult("副审"+"审贷退回");
											}
											ApproveHistoryForm2.setDisplayName(result.get(a).getDisplayName());
											ApproveHistoryForm2.setStartExamineTime(result.get(a).getCreatime());
											historyForms.add(a+1, ApproveHistoryForm2);
										}
							}
						}}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				map.put("historyForms", historyForms);
				map.put("size", historyForms.size());
			}
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
			JSONObject json = JSONObject.fromObject(map, jsonConfig);
			return json.toString();
		}
		
		
		/**
		 * 审贷会纪要
		 * @param request
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/ipad/intopieces/findAuditConfigureFormById.page", method = { RequestMethod.GET })
		public String findAuditConfigureFormById(HttpServletRequest request) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			String id = request.getParameter("id");
			String appId = request.getParameter("id");
			String cid = request.getParameter("cid");
			String pid = request.getParameter("pid");
			String uId=request.getParameter("userId");
			CustomerSqInfo SqInfo=SqInfoService.selectSqInfoBycpId(pid, cid);
			CustomerApplicationInfo customerApplicationInfo = intoPiecesService.findCustomerApplicationInfoById(appId);
			ProductAttribute producAttribute =  productService.findProductAttributeById(customerApplicationInfo.getProductId());
			CustomerInfor  customerInfor  = intoPiecesService.findCustomerManager(customerApplicationInfo.getCustomerId());
			List<AppManagerAuditLog> appManagerAuditLog = productService.findAppManagerAuditLog(appId,"1");
			if(appManagerAuditLog.size()>0){
				map.put("appManagerAuditLog", appManagerAuditLog.get(0));
				List<IntoPieces> result=SdwUserService.findsdh1(id);
				if(result.size()>0){
					for(int a=0;a<result.size();a++){
						if(result.get(a).getStatus().equals("1")){
							result.get(a).setStatus("通过");
						}else if(result.get(a).getStatus().equals("2")){
							result.get(a).setStatus("拒绝");
						}else if(result.get(a).getStatus().equals("3")){
							result.get(a).setStatus("退回");
						}
					}
					map.put("result", result);
					map.put("resultsize", result.size());
					if(result.size()>=3 && result.get(0).getStatus()=="通过"&& result.get(1).getStatus()=="通过"&& result.get(2).getStatus()=="通过"){
						if(result.get(0).getSplv()==result.get(1).getSplv() && result.get(0).getSplv()==result.get(2).getSplv()&&
								result.get(0).getSpqx()==result.get(1).getSpqx() && result.get(0).getSpqx()==result.get(2).getSpqx()&&
								result.get(0).getDbfs()==result.get(1).getDbfs() && result.get(0).getDbfs()==result.get(2).getDbfs()&&
								result.get(0).getApplyQuota()==result.get(1).getApplyQuota() && result.get(0).getApplyQuota()==result.get(2).getApplyQuota()){
						}else{
							List<IntoPieces>  IntoPieces= SdwUserService.findsph1(id);
							if(IntoPieces.size()>0){
								for(int a=0;a<IntoPieces.size();a++){
									if(IntoPieces.get(a).getApplyQuota()!=null){
										IntoPieces.get(a).setStatus("通过");
									}else{
										if(IntoPieces.get(a).getStatus().equals("nopass") || IntoPieces.get(a).getStatus().equals("refuse")){
											IntoPieces.get(a).setStatus("拒绝");
										}else if(IntoPieces.get(a).getStatus().equals("nopass_replenish") || IntoPieces.get(a).getStatus().equals("returnedToFirst")){
											IntoPieces.get(a).setStatus("退回");
										}
									}
								}
								map.put("bss", IntoPieces);
								map.put("bsssize", IntoPieces.size());
							}else{
								map.put("bsssize", IntoPieces.size());
							}
						}
					}
				}else{
					map.put("resultsize", result.size());
				}
				
			}else{
					AppManagerAuditLog AppManagerAuditLog=new AppManagerAuditLog();
					AppManagerAuditLog.setId("0");
					map.put("appManagerAuditLog", AppManagerAuditLog);
				}
			map.put("customerApplicationInfo", customerApplicationInfo);
			map.put("producAttribute", producAttribute);
			map.put("custManagerId", customerInfor.getUserId());
			map.put("SqInfo", SqInfo);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
			JSONObject json = JSONObject.fromObject(map, jsonConfig);
			return json.toString();
		}
		
		
		/**
		 * 审贷会通知
		 * @param filter
		 * @param request
		 * @return
		 * @throws IOException
		 * @throws SftpException
		 */
		@ResponseBody
		@RequestMapping(value = "/ipad/intopieces/selectSDTZ.json", method = { RequestMethod.GET })
		public String selectSDTZ(@ModelAttribute IntoPiecesFilter filter,HttpServletRequest request) throws IOException, SftpException {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			List<CustomerApplicationIntopieceWaitForm> list=new ArrayList<CustomerApplicationIntopieceWaitForm>();
			filter.setNextNodeName("进件初审");
			filter.setUserId(request.getParameter("userId"));
			//进件初审
			List<CustomerApplicationIntopieceWaitForm> result=jnpadIntopiecesDecisionService.findCustomerApplicationIntopieceDecison1(filter);
			for(int i=0;i<result.size();i++){
				result.get(i).setZsw("无审贷级别");
				result.get(i).setNodeName("初审");
				list.add(i, result.get(i));
			}
			IntoPiecesFilters Filters =new IntoPiecesFilters();
			Filters.setUserId(request.getParameter("userId"));
			//审贷决议
			List<IntoPiecesFilters> result1 = customerApplicationIntopieceWaitService.findCustomerApplicationIntopieceDecisons1(Filters);
			for(int a=0;a<result1.size();a++){
				CustomerApplicationIntopieceWaitForm WaitForm=new CustomerApplicationIntopieceWaitForm();
				WaitForm.setChineseName(result1.get(a).getChineseName());
				WaitForm.setCardId(result1.get(a).getCardId());
				WaitForm.setProductName(result1.get(a).getProductName());
				WaitForm.setApplyQuota(result1.get(a).getApplyQuota());
				if(result1.get(a).getZsw()==1){
					WaitForm.setZsw("主审委");
				}else{
					WaitForm.setZsw("副审委");
				}
				WaitForm.setNodeName("审贷");
				WaitForm.setDisplayName(result1.get(a).getDisplayName());
				list.add(list.size(), WaitForm);
			}
			//终审
			IntoPiecesFilters IntoPieces=new IntoPiecesFilters();
			IntoPieces.setUserId(request.getParameter("userId"));
			List<IntoPiecesFilters> result2 = customerApplicationIntopieceWaitService.findCustomerApplicationIntopieceDecisones1(IntoPieces,request);
			for(int a=0;a<result2.size();a++){
				CustomerApplicationIntopieceWaitForm WaitForm=new CustomerApplicationIntopieceWaitForm();
				WaitForm.setChineseName(result2.get(a).getChineseName());
				WaitForm.setCardId(result2.get(a).getCardId());
				WaitForm.setProductName(result2.get(a).getProductName());
				WaitForm.setApplyQuota(result2.get(a).getApplyQuota());
				if(result2.get(a).getZsw()==1){
					WaitForm.setZsw("主审委");
				}else{
					WaitForm.setZsw("副审委");
				}
				WaitForm.setNodeName("终审");
				WaitForm.setDisplayName(result2.get(a).getDisplayName());
				list.add(list.size(), WaitForm);
			}
			map.put("result", list);
			map.put("size", list.size());
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
			JSONObject json = JSONObject.fromObject(map, jsonConfig);
			return json.toString();
		}
		/**
		 * pad重新申请
		 * @param request
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/ipad/custAppInfo/applyInfo.json")
		public String doMethod(HttpServletRequest request) {
				String appId = request.getParameter("id");
				addIntoPiecesService.doMethod(appId);
				Map<String,Object> map = new LinkedHashMap<String,Object>();
				map.put("message", "提交成功");
				JsonConfig jsonConfig = new JsonConfig();
				jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
				JSONObject json = JSONObject.fromObject(map, jsonConfig);
				return json.toString();
		}	
		
		
		
		
		/**
		 * 客户原始信息
		 * @param filter
		 * @param request
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/ipad/custAppInfo/khysxxck.page", method = { RequestMethod.GET })
		public String browse(@ModelAttribute IntoPiecesFilter filter,HttpServletRequest request) {
			filter.setUserId("");
			filter.setLimit(Integer.MAX_VALUE);
			String userId=request.getParameter("userId");
			Integer userType=Integer.parseInt(request.getParameter("userType"));
			//查询客户经理
			List<AccountManagerParameterForm> forms = maintenanceService.findSubListManagerByManagerId1(userId,userType);
			if(forms != null && forms.size() > 0){
				StringBuffer userIds = new StringBuffer();
				userIds.append("(");
				for(AccountManagerParameterForm form : forms){
					userIds.append("'").append(form.getUserId()).append("'").append(",");
				}
				userIds = userIds.deleteCharAt(userIds.length() - 1);
				userIds.append(")");
				filter.setCustManagerIds(userIds.toString());
			}else{
				filter.setUserId(userId);
			}
			QueryResult<IntoPieces> result = intoPiecesService.findintoPiecesByFilter(filter);
			for(int i=0;i<result.getTotalCount();i++){
				if(result.getItems().get(i).getStatus().equals("audit")){
					result.getItems().get(i).setStatusName("已申请");
				}else if(result.getItems().get(i).getStatus().equals("approved")){
					result.getItems().get(i).setStatusName("已通过");
				}else if(result.getItems().get(i).getStatus().equals("refuse")){
					result.getItems().get(i).setStatusName("已拒绝");
				}else if(result.getItems().get(i).getStatus().equals("returnedToFirst")){
					result.getItems().get(i).setStatusName("已退回");
				}
			}
			Map<String,Object> map = new LinkedHashMap<String,Object>();
			map.put("result", result);
			map.put("size", result.getTotalCount());
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
			JSONObject json = JSONObject.fromObject(map, jsonConfig);
			return json.toString();
		}
		/**
		 * 制定客户基本信息
		 * @param request
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/ipad/custAppInfo/showInfoJn.page")
		public String showInfoJn(HttpServletRequest request) {
			Map<String,Object> map = new LinkedHashMap<String,Object>();
			String customerInforId = request.getParameter("id");
				CustomerFirsthendBase base=customerInforService.findCustomerFirsthendBase(customerInforId);
				map.put("customerInfor", base);
				JsonConfig jsonConfig = new JsonConfig();
				jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
				JSONObject json = JSONObject.fromObject(map, jsonConfig);
				return json.toString();
		}
		/**
		 * 查看客户影像资料
		 * @param filter
		 * @param request
		 * @return
		 * @throws IOException
		 * @throws SftpException
		 */
		@ResponseBody
		@RequestMapping(value = "/ipad/custAppInfo/selectBycardId.page")
		public String selectBycardId(@ModelAttribute ImageMore filter,HttpServletRequest request) throws IOException, SftpException {
			Map<String,Object> map = new LinkedHashMap<String,Object>();
			String pid = request.getParameter("cardId");
			filter.setPid(pid);
			filter.setLimit(Integer.MAX_VALUE);
			List<ImageMore> result = addIntoPiecesService.selectBycardId(filter);
			map.put("result", result);
			map.put("size", result.size());
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
			JSONObject json = JSONObject.fromObject(map, jsonConfig);
			return json.toString();
		}
		
		
		
		
		
		
		
		
		
		
		
		
}
