package com.cardpay.pccredit.customeradd.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.customer.constant.CustomerInforConstant;
import com.cardpay.pccredit.customer.dao.MaintenanceDao;
import com.cardpay.pccredit.customer.filter.MaintenanceFilter;
import com.cardpay.pccredit.customer.filter.ModelFilter;
import com.cardpay.pccredit.customer.service.MaintenanceService;
import com.cardpay.pccredit.customeradd.model.CIPERSONBASINFO;
import com.cardpay.pccredit.customeradd.model.MaintenanceForm;
import com.cardpay.pccredit.customeradd.service.KuglService;
import com.cardpay.pccredit.jnpad.model.CustomerInfo;
import com.cardpay.pccredit.manager.model.FourMonthModel;
import com.cardpay.pccredit.manager.service.LxSynchScheduleService;
import com.cardpay.pccredit.manager.service.ManagerSalaryService;
import com.cardpay.pccredit.manager.web.AccountManagerParameterForm;
import com.cardpay.pccredit.manager.web.ManagerSalaryForm;
import com.cardpay.pccredit.riskControl.model.RiskCustomer;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.auth.JRadModule;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.constant.JRadConstants;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.controller.BaseController;
import com.wicresoft.jrad.base.web.result.JRadPagedQueryResult;
import com.wicresoft.jrad.base.web.result.JRadReturnMap;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.jrad.base.web.utility.WebRequestHelper;
import com.wicresoft.util.spring.Beans;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;
import com.wicresoft.util.web.RequestHelper;
/**
 * 
 * 描述 ：管辖客户管理
 * @author  郑博
 *
 * 2014-11-10 下午3:32:01
 */
@Controller
@RequestMapping("/manager/khgl/*")
@JRadModule("manager.khgl")
public class ManagerkhglController extends BaseController{
	
	@Autowired
	private KuglService service;
	
	@Autowired
	private MaintenanceService maintenanceService;
	
	@Autowired
	private MaintenanceDao maintenanceDao;
	
	@Autowired
	private ManagerSalaryService managerSalaryService;
	
	
	/**
	 * 通过客户经理查询
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "browse.page", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView query(@ModelAttribute MaintenanceFilter filter,HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/customeradd/maintenance_plan_browse", request);
		filter.setRequest(request);
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		List<AccountManagerParameterForm>forms=new ArrayList<AccountManagerParameterForm>();
		//查询客户经理
		//不是客户经理的显示全部客户经理
		if(user.getUserType()!=1){
			forms=service.findcustomermanager();
		}else{
			//客户经理组长显示他及手下客户经理  客户经理则只显示自己
			forms = maintenanceService.findSubListManagerByManagerId(user);
		}
		String customerManagerId = filter.getCustomerManagerId();
		QueryResult<MaintenanceForm> result = null;
		//如果页面 传过来有信息怎显示客户经理手下信息
		if(customerManagerId!=null && !customerManagerId.equals("")){
			result = service.findMaintenancePlansList(filter);
		}else{
			//如果页面穿过来没有信息则显示所有客户经理手下信息
			if(forms.size()>0){
				filter.setCustomerManagerIds(forms);
				result = service.findMaintenancePlansList(filter);
			}else{
				//直接返回页面
				return mv;
			}
		}
		JRadPagedQueryResult<MaintenanceForm> pagedResult = new JRadPagedQueryResult<MaintenanceForm>(filter, result);
		mv.addObject(PAGED_RESULT, pagedResult);
		mv.addObject("forms", forms);
		return mv;
	}
	
	/**
	 * 
	 * 客户详情
	 * */
	@ResponseBody
	@RequestMapping(value = "xs.page", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView xs(@ModelAttribute MaintenanceFilter filter,HttpServletRequest request) {
		JRadModelAndView mv = null;
		String productId[] = RequestHelper.getStringValue(request, ID).split("/");
		QueryResult<MaintenanceForm> result = null;
			String cardid=productId[2];
			//通过页面取得cardid查询用户的具体信息
			List<CIPERSONBASINFO>customerinfo=service.findcustomerinfo(cardid);
			mv = new JRadModelAndView("/customeradd/findcustomerinfo", request);
			mv.addObject("customerInfor", customerinfo);
		return mv;
	}
	
	
	
	
	
	
	//====================================================================================================================//
	@ResponseBody
	@RequestMapping(value = "dimensionsCreditModel.page", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView xs(HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/static/score", request);
		return mv;
	}
	
	
	@ResponseBody
	@RequestMapping(value = "getScore.json")
	public JRadReturnMap getScore(HttpServletRequest request) {
		String score = request.getParameter("score");
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				String message="";
				
				if(Integer.parseInt(score)<=55){
				   message ="得分"+score+"分,不予授信";
				   
				}else if(Integer.parseInt(score)>56&&Integer.parseInt(score)<=60){
				   message ="得分"+score+"分,拟授信额度上限5万";
				   
				}else if(Integer.parseInt(score)>61&&Integer.parseInt(score)<=65){
				   message ="得分"+score+"分,拟授信额度上限8万";
				   
				}else if(Integer.parseInt(score)>66&&Integer.parseInt(score)<=70){
				   message ="得分"+score+"分,拟授信额度上限13万";
				   
				}else if(Integer.parseInt(score)>71&&Integer.parseInt(score)<=75){
				   message ="得分"+score+"分,拟授信额度上限16万";
				   
				}else if(Integer.parseInt(score)>76&&Integer.parseInt(score)<=80){
				   message ="得分"+score+"分,拟授信额度上限20万";
				   
				}else if(Integer.parseInt(score)>81&&Integer.parseInt(score)<=85){
				   message ="得分"+score+"分,拟授信额度上限25万";
					   
				}else if(Integer.parseInt(score)>85&&Integer.parseInt(score)<=90){
				   message ="得分"+score+"分,拟授信额度上限30万";
					   
				}else if(Integer.parseInt(score)>90&&Integer.parseInt(score)<=95){
				   message ="得分"+score+"分,拟授信额度上限40万";
						   
				}else if(Integer.parseInt(score)>95){
				   message ="得分"+score+"分,拟授信额度上限50万";
					   
				}
				
				returnMap.put("isInList", message);
			}catch (Exception e) {
				returnMap.put(JRadConstants.MESSAGE,"系统异常");
				returnMap.put(JRadConstants.SUCCESS, false);
				return WebRequestHelper.processException(e);
			}
		}else{
			returnMap.setSuccess(false);
			returnMap.addGlobalError(CustomerInforConstant.CREATEERROR);
		}
		return returnMap;
	}
	
	
	/**
	 * 四维授信打分
	 */
	@ResponseBody
	@RequestMapping(value = "scoreBrowse.page", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView scoreBrowse(@ModelAttribute ModelFilter filter,HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/manager/performmance/four_month_model", request);
		filter.setRequest(request);
		QueryResult<FourMonthModel> result =  service.findModelList(filter);
		JRadPagedQueryResult<FourMonthModel> pagedResult = new JRadPagedQueryResult<FourMonthModel>(filter, result);
		mv.addObject(PAGED_RESULT, pagedResult);
		return mv;
	}
	
	/**
	 * 四维授信模型生成数据
	 */
	@ResponseBody
	@RequestMapping(value = "doGet.json")
	@JRadOperation(JRadOperation.CHANGE)
	public JRadReturnMap doGet(@ModelAttribute ManagerSalaryForm form, HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		try {
			managerSalaryService.doGet();
			returnMap.setSuccess(true);
		}
		catch (Exception e) {
			returnMap.setSuccess(false);
			returnMap.addGlobalError(e.getMessage());
			return returnMap;
		}
		return returnMap;
	}
	
	
}
