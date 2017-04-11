package com.cardpay.pccredit.jnpad.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
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
import com.cardpay.pccredit.jnpad.model.ManagerInfoForm;
import com.cardpay.pccredit.jnpad.service.JnpadIntopiecesDecisionService;
import com.cardpay.pccredit.jnpad.service.JnpadRetrainingService;
import com.cardpay.pccredit.manager.filter.RetrainingFilter;
import com.cardpay.pccredit.manager.model.Retraining;
import com.cardpay.pccredit.manager.service.AccountManagerRetrainingService;
import com.cardpay.pccredit.manager.service.RetrainingService;
import com.cardpay.pccredit.manager.web.RetrainingForm;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.result.JRadPagedQueryResult;
import com.wicresoft.jrad.base.web.result.JRadReturnMap;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.jrad.base.web.utility.WebRequestHelper;
import com.wicresoft.jrad.modules.privilege.model.User;
import com.wicresoft.util.spring.Beans;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;
import com.wicresoft.util.web.RequestHelper;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Controller
public class JnpadRetrainingController {

	@Autowired
	private JnpadRetrainingService retrainingService;
	
	@Autowired
	private RetrainingService retrainingServices;
	
	@Autowired
	private JnpadIntopiecesDecisionService jnpadIntopiecesDecisionService;
	
	@Autowired
	private AccountManagerRetrainingService accountManagerRetrainingService;
	
	/**
	 * 浏览页面
	 * 
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ipad/retraining/browse.page", method = { RequestMethod.GET })
	public String browse(@ModelAttribute RetrainingFilter filter, HttpServletRequest request) {
		
		Map<String,Object> result = new LinkedHashMap<String,Object>();
		List<Retraining> list = retrainingService.findRetrainingByFilter(filter);
		result.put("result", list);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(result, jsonConfig);
		return json.toString();
	}

	//客户经理信息
		@ResponseBody
		@RequestMapping(value = "/ipad/retraining/managerInfoi.json", method = { RequestMethod.GET })
		public String managerInfo(HttpServletRequest request){
			
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			List<ManagerInfoForm> result = jnpadIntopiecesDecisionService.findManagerInfo();
			
		       map.put("manager",result );
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
			JSONObject json = JSONObject.fromObject(map,jsonConfig);
			return json.toString();
		}
		
		/**
		 * 执行添加
		 * 
		 * @param sample2
		 * @param request
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "/ipad/retraining/planinsert.json")
		public String insert(@ModelAttribute RetrainingForm form, HttpServletRequest request) {
			JRadReturnMap returnMap = new JRadReturnMap();
				try {
					String trainingTimess = RequestHelper.getStringValue(request, "trainingTimess");
					Date date = new SimpleDateFormat("yyyy-MM-dd").parse(trainingTimess);
					form.setTrainingTime(date);
					Retraining retraining = form.createModel(Retraining.class);
					String userId=RequestHelper.getStringValue(request, "userId");
					retraining.setCreatedBy(userId);
					retraining.setModifiedBy(userId);
					String id = retrainingServices.insertRetraining(retraining);
					//returnMap.addGlobalMessage(CREATE_SUCCESS);
					String deleteManagerIds = RequestHelper.getStringValue(request, "deleteManagerIds");
					String newAddManagerIds = RequestHelper.getStringValue(request, "newAddManagerIds");
					String retrainId = id;
					accountManagerRetrainingService.saveManagers(retrainId, deleteManagerIds, newAddManagerIds,userId);
					returnMap.put("mess", "保存成功");
				}
				catch (Exception e) {
					returnMap.put("mess", "保存失败");
				}

			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
			JSONObject json = JSONObject.fromObject(returnMap,jsonConfig);
			return json.toString();
		}
	//删除培训计划
		@ResponseBody
		@RequestMapping(value = "/ipad/retraining/delete.json")
		@JRadOperation(JRadOperation.DELETE)
		public String delete(HttpServletRequest request) {
			JRadReturnMap returnMap = new JRadReturnMap();

			try {
				String retrainId = RequestHelper.getStringValue(request, "id");
				retrainingServices.deleteRetraining(retrainId);
				returnMap.put("mess", "删除成功");
			}
			catch (Exception e) {
				returnMap.put("mess", "删除失败");
			}

			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
			JSONObject json = JSONObject.fromObject(returnMap,jsonConfig);
			return json.toString();
		}
}
