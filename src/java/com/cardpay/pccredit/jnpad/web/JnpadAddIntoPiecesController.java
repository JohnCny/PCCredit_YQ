package com.cardpay.pccredit.jnpad.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cardpay.pccredit.customer.constant.CustomerInforConstant;
import com.cardpay.pccredit.customer.filter.CustomerInforFilter;
import com.cardpay.pccredit.customer.model.CustomerInfor;
import com.cardpay.pccredit.customer.service.CustomerInforService;
import com.cardpay.pccredit.intopieces.filter.AddIntoPiecesFilter;
import com.cardpay.pccredit.intopieces.service.AddIntoPiecesService;
import com.cardpay.pccredit.intopieces.service.CustomerSqInfoService;
import com.cardpay.pccredit.intopieces.service.IntoPiecesService;
import com.cardpay.pccredit.intopieces.web.AddIntoPiecesForm;
import com.cardpay.pccredit.intopieces.web.LocalExcelForm;
import com.cardpay.pccredit.ipad.util.JsonDateValueProcessor;
import com.cardpay.pccredit.jnpad.service.JnpadAddIntoPiecesService;
import com.cardpay.pccredit.riskControl.model.RiskCustomer;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.constant.JRadConstants;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.result.JRadReturnMap;
import com.wicresoft.jrad.base.web.utility.WebRequestHelper;
import com.wicresoft.jrad.modules.log.model.OperationLog;
import com.wicresoft.jrad.modules.log.service.UserLogService;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;


@Controller
public class JnpadAddIntoPiecesController {

	@Autowired
	private IntoPiecesService intoPiecesService;
	
	@Autowired
	private CustomerInforService customerInforservice;
	
	@Autowired
	private AddIntoPiecesService addIntoPiecesService;
	
	@Autowired
	private JnpadAddIntoPiecesService jnpadaddIntoPiecesService;
	@Autowired
	private UserLogService userLogService;
	//选择客户
	@ResponseBody
	@RequestMapping(value = "/ipad/addIntoPieces/browseCustomer.json", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.BROWSE)
	public String browseCustomer(@ModelAttribute CustomerInforFilter filter,HttpServletRequest request) {
      
		filter.setUserId(request.getParameter("userId"));
		filter.setProductId(request.getParameter("productId"));
		filter.setLimit(Integer.MAX_VALUE);
		if(!request.getParameter("name").equals("") && !request.getParameter("name").equals(null)){
			filter.setChineseName("%"+request.getParameter("name")+"%");
		}
		QueryResult<CustomerInfor> result = customerInforservice.findCustomerInforByFilterAndProductId(filter);
		
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(result, jsonConfig);
		return json.toString();
	}
	
	//上传影像资料
		@ResponseBody
		@RequestMapping(value = "/ipad/addIntopieces/imageImport.json")
		public Map<String, Object> imageImport(@RequestParam(value = "file", required = false) MultipartFile file,HttpServletRequest request,HttpServletResponse response) throws Exception {        
			response.setContentType("text/html;charset=utf-8");
			Map<String, Object> map = new HashMap<String, Object>();
			try {
				if(file==null||file.isEmpty()){
					map.put(JRadConstants.SUCCESS, false);
					map.put(JRadConstants.MESSAGE, CustomerInforConstant.IMPORTEMPTY);
					JSONObject obj = JSONObject.fromObject(map);
					response.getWriter().print(obj.toString());
				}
				String fileName =request.getParameter("fileName");
				String productId = request.getParameter("productId");
				String customerId = request.getParameter("customerId");
				String applicationId = request.getParameter("applicationId");
				String phone_type=request.getParameter("phone_type");
				jnpadaddIntoPiecesService.importImage(file,productId,customerId,applicationId,phone_type);
				map.put(JRadConstants.SUCCESS, true);
				map.put(JRadConstants.MESSAGE, CustomerInforConstant.IMPORTSUCCESS);
				JSONObject obj = JSONObject.fromObject(map);
				response.getWriter().print(obj.toString());
			} catch (Exception e) {
				e.printStackTrace();
				map.put(JRadConstants.SUCCESS, false);
				map.put(JRadConstants.MESSAGE, "上传失败:"+e.getMessage());
				JSONObject obj = JSONObject.fromObject(map);
				response.getWriter().print(obj.toString());
			}
			return null;
		}
		
		
		//导入调查报告
		@ResponseBody
		@RequestMapping(value = "/ipad/addIntopieces/reportImport.json")
		public Map<String, Object> reportImport_json(@RequestParam(value = "file", required = false) MultipartFile file,HttpServletRequest request,HttpServletResponse response) throws Exception {        
			response.setContentType("text/html;charset=utf-8");
			Map<String, Object> map = new HashMap<String, Object>();
			try {
				if(file==null||file.isEmpty()){
					map.put(JRadConstants.SUCCESS, false);
					map.put(JRadConstants.MESSAGE, CustomerInforConstant.IMPORTEMPTY);
					JSONObject obj = JSONObject.fromObject(map);
					response.getWriter().print(obj.toString());
				}
				String fileName =request.getParameter("fileName");
				String productId = request.getParameter("productId");
				String customerId = request.getParameter("customerId");
				jnpadaddIntoPiecesService.importExcel(file,productId,customerId);
				map.put(JRadConstants.SUCCESS, true);
				map.put(JRadConstants.MESSAGE, CustomerInforConstant.IMPORTSUCCESS);
				JSONObject obj = JSONObject.fromObject(map);
				response.getWriter().print(obj.toString());
			} catch (Exception e) {
				e.printStackTrace();
				map.put(JRadConstants.SUCCESS, false);
				map.put(JRadConstants.MESSAGE,e.getMessage());
				JSONObject obj = JSONObject.fromObject(map);
				response.getWriter().print(obj.toString());
			}
			return null;
		}
		@ResponseBody
		@RequestMapping(value = "/ipad/addIntopiece/isInRiskList.json", method = { RequestMethod.GET })
		@JRadOperation(JRadOperation.BROWSE)
		public String isInRiskList(HttpServletRequest request) {
			String customerId = request.getParameter("customerId");
			Map<String, Object> map = new HashMap<String, Object>();
					RiskCustomer riskCustomer = intoPiecesService.findRiskListByCustomerId(customerId);
					if(riskCustomer != null){
						map.put("isInList", true);
					}
					
				JsonConfig jsonConfig = new JsonConfig();
				jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
				JSONObject json = JSONObject.fromObject(map, jsonConfig);
				return json.toString();
			}
		//查询调查报告是否导入
		@ResponseBody
		@RequestMapping(value = "/ipad/addIntopieces/reportIsExist.json", method = { RequestMethod.GET })
		public String reportImport(@ModelAttribute AddIntoPiecesFilter filter,HttpServletRequest request) {
			filter.setRequest(request);
			QueryResult<LocalExcelForm> result = addIntoPiecesService.findLocalExcelByProductAndCustomer1(filter);
			Map<String, Object> map = new HashMap<String, Object>();
			String excelId="";
			if(!result.getItems().isEmpty()){
				excelId=result.getItems().get(0).getId();
			}
			map.put("excelId", excelId);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
			JSONObject json = JSONObject.fromObject(map, jsonConfig);
			return json.toString();
		}
		
		//提交申请
				@ResponseBody
				@RequestMapping(value = "/ipad/addIntopieces/addIntopieces.json", method = { RequestMethod.GET })
				public String addIntopieces(@ModelAttribute AddIntoPiecesForm addIntoPiecesForm,HttpServletRequest request) {
					JRadReturnMap returnMap = new JRadReturnMap();
					String loginId = request.getParameter("userId");
					String displayName = request.getParameter("displayName");
					Map<String, Object> map = new HashMap<String, Object>();
					try {
						addIntoPiecesService.addIntopieces(addIntoPiecesForm,loginId);
						//日志记录
						OperationLog ol = new OperationLog();
						ol.setUser_id(loginId);
					    ol.setUser_login(displayName);
					    ol.setModule("进件新增");
					    ol.setOperation_result("SUCCESS");
					    ol.setOperation_name("ADD");
					    ol.setIp_address(request.getRemoteAddr());
						userLogService.addUserLog(ol);
						returnMap.addGlobalMessage("system.change.success");
						map.put("mess", "提交进件成功");
					} catch (Exception e) {
						//日志记录
						OperationLog ol = new OperationLog();
						ol.setUser_id(loginId);
					    ol.setUser_login(displayName);
					    ol.setModule("进件新增");
					    ol.setOperation_result("FAIL");
					    ol.setOperation_name("ADD");
					    ol.setIp_address(request.getRemoteAddr());
						userLogService.addUserLog(ol);
						map.put("mess", e.getMessage());
					}

					JsonConfig jsonConfig = new JsonConfig();
					jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
					JSONObject json = JSONObject.fromObject(map, jsonConfig);
					return json.toString();
				}
}
