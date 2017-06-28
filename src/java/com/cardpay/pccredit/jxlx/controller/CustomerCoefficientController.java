package com.cardpay.pccredit.jxlx.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.mapping.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.customer.model.CIPERSONFAMILY;
import com.cardpay.pccredit.dateplan.model.JBUser;
import com.cardpay.pccredit.ipad.util.JsonDateValueProcessor;
import com.cardpay.pccredit.jxlx.dao.CustomerCoefficientDao;
import com.cardpay.pccredit.jxlx.model.COEFFICIENT;
import com.cardpay.pccredit.jxlx.service.CustomerCoefficientService;
import com.wicresoft.jrad.base.auth.JRadModule;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.controller.BaseController;
import com.wicresoft.jrad.base.web.result.JRadPagedQueryResult;
import com.wicresoft.jrad.base.web.result.JRadReturnMap;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Controller
@RequestMapping("/customer/Coefficient/*")
@JRadModule("customer.Coefficient")
public class CustomerCoefficientController extends BaseController{
	@Autowired
	private CustomerCoefficientService CoefficientService;
	/**
	 * 修改系数
	 */
	@ResponseBody
	@RequestMapping(value = "updateCoefficient.page", method = { RequestMethod.GET })
	public JRadReturnMap updateCoefficient(HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		//当前登录用户ID
		String orgteam=request.getParameter("orgteam");
		String team = request.getParameter("team");
		String userid = request.getParameter("userid");
		String code = request.getParameter("code");
		COEFFICIENT c=new COEFFICIENT();
		c.setUserid(userid);
		if(!team.equals("0") && !team.equals(null)){
			c.setTeam(team);
		}
		if(!orgteam.equals("0") && !orgteam.equals(null)){
			c.setOrgteam(orgteam);
		}
		c.setCode(Double.parseDouble(code));
		CoefficientService.updateCoefficient(c);
		
		return returnMap;
	}
	
	/**
	 * 查询所有客户经理的系数值
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "selectCoefficient.page", method = { RequestMethod.GET })
	public AbstractModelAndView selectCoefficient(@ModelAttribute COEFFICIENT filter, HttpServletRequest request) {
		//当前登录用户ID
		String orgteam=request.getParameter("orgteam");
		String team = request.getParameter("team");
		String name = request.getParameter("name");
		filter.setTeam(team);
		filter.setOrgteam(orgteam);
		filter.setRequest(request);
		filter.setName(name);
		QueryResult<COEFFICIENT> result = CoefficientService.selectallCOEFFICIENT(filter);
		JRadPagedQueryResult<COEFFICIENT> pagedResult = new JRadPagedQueryResult<COEFFICIENT>(
				filter, result);
		JRadModelAndView mv = new JRadModelAndView("/jxlx/lifc", request);
		mv.addObject(PAGED_RESULT, pagedResult);
		return mv;
	}
	/**
	 * 查询单个客户经理的系数值
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "selectCoefficientByuserId.page", method = { RequestMethod.GET })
	public AbstractModelAndView selectCoefficientByuserId(@ModelAttribute COEFFICIENT filter, HttpServletRequest request) {
		//当前登录用户ID
		String userid=request.getParameter("userid");
		COEFFICIENT result = CoefficientService.selectallCOEFFICIENTByUserId(userid);
		JRadModelAndView mv = new JRadModelAndView("/jxlx/update1", request);
		mv.addObject("result", result);
		return mv;
	}
	
	/**
	 * 跳转到修改机构系数页面
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "selectTeam.page", method = { RequestMethod.GET })
	public AbstractModelAndView selectTeam(@ModelAttribute COEFFICIENT filter, HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/jxlx/update2", request);
		return mv;
	}
}
