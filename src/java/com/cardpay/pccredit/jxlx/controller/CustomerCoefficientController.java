package com.cardpay.pccredit.jxlx.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
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
import com.cardpay.pccredit.jxlx.model.SPLITOFINTEREST;
import com.cardpay.pccredit.jxlx.service.CustomerCoefficientService;
import com.cardpay.pccredit.manager.form.ManagerPerformmanceForm;
import com.cardpay.pccredit.manager.service.ManagerPerformmanceService;
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
	@Autowired
	private ManagerPerformmanceService managerPerformmanceService;
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
	
	
	/**
	 * 查询所有的利息收入情况
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "selectallSPLITOFINTEREST.page", method = { RequestMethod.GET })
	public AbstractModelAndView selectallSPLITOFINTEREST(@ModelAttribute SPLITOFINTEREST filter, HttpServletRequest request) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Date date=new Date();
		List<SPLITOFINTEREST> resultNewUser=new ArrayList<SPLITOFINTEREST>();
		List<ManagerPerformmanceForm>result=managerPerformmanceService.selectAllManegerTeam();
		for(int c=result.size()-1;c>=0;c--){
			if(result.get(c).getTeam().equals("管理团队")){
				result.remove(c);			}
		}
		List<ManagerPerformmanceForm>result1=managerPerformmanceService.selectAllManegerTeam();
		for(int c=result1.size()-1;c>=0;c--){
			if(result1.get(c).getOrdteam().equals("阳泉市农村信用社")){
				result1.remove(c);			}
		}
		for(int b=0;b<result1.size();b++){
			for(int j=result1.size()-1;j>=0;j--){
				if(j!=b){
					if(result1.get(j).getOrdteam().equals(result1.get(b).getOrdteam())){
						result1.remove(j);
					}
				}
			}
		}
		Integer size=0;
		for(int i=0;i<result.size();i++){
			SPLITOFINTEREST sp=new SPLITOFINTEREST();
			sp.setTeam(result.get(i).getTeam());
			sp.setYear(sdf.format(date).substring(0, 4));
			sp.setMonth(sdf.format(date).substring(4, 6));
			List<SPLITOFINTEREST>resultUser=CoefficientService.selectSpliByTeam(sp);
			for(int i1=0;i1<resultUser.size();i1++){
				resultNewUser.add(size+i1, resultUser.get(i1));
			}
		SPLITOFINTEREST resultUser1=CoefficientService.selectSumSpliByTeam(sp);
		resultUser1.setOrgteam("总计");
		resultUser1.setTeam(result.get(i).getTeam());
		resultUser1.setName(result.get(i).getTeam());
		resultUser1.setYear(sdf.format(date).substring(0, 4));
		resultUser1.setMonth(sdf.format(date).substring(4, 6));
		resultNewUser.add(size+resultUser.size(), resultUser1);
		size+=resultUser.size()+1;
		}
		for(int a=0;a<result1.size();a++){
			SPLITOFINTEREST sp=new SPLITOFINTEREST();
			sp.setOrgteam(result1.get(a).getOrdteam());
			sp.setYear(sdf.format(date).substring(0, 4));
			sp.setMonth(sdf.format(date).substring(4, 6));
			SPLITOFINTEREST resultUser=CoefficientService.selectSumSpliByTeam(sp);
			resultUser.setOrgteam("汇总");
			resultUser.setTeam(result1.get(a).getOrdteam());
			resultUser.setName(result1.get(a).getOrdteam());
			resultUser.setYear(sdf.format(date).substring(0, 4));
			resultUser.setMonth(sdf.format(date).substring(4, 6));
			resultNewUser.add(size, resultUser);
			size=size+1;
		}
		SPLITOFINTEREST sp=new SPLITOFINTEREST();
		sp.setYear(sdf.format(date).substring(0, 4));
		sp.setMonth(sdf.format(date).substring(4, 6));
		SPLITOFINTEREST resultUser=CoefficientService.selectSumSpliByTeam(sp);
		resultUser.setOrgteam("累计");
		resultUser.setTeam("累计");
		resultUser.setName("累计");
		resultUser.setYear(sdf.format(date).substring(0, 4));
		resultUser.setMonth(sdf.format(date).substring(4, 6));
		resultNewUser.add(size, resultUser);
		SPLITOFINTEREST sp1=new SPLITOFINTEREST();
		sp1.setYear(sdf.format(date).substring(0, 4));
		sp1.setMonth(sdf.format(date).substring(4, 6));
		JRadModelAndView mv = new JRadModelAndView("/jxlx/lxcfzl", request);
		mv.addObject("gxperformList", resultNewUser);
		mv.addObject("sp1", sp1);
		return mv;
	}
	
	
	
	
	
	
	/**
	 * 条件查询
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "selectByUserOrByTeam.page", method = { RequestMethod.GET })
	public AbstractModelAndView selectByUserOrByTeam(@ModelAttribute SPLITOFINTEREST filter, HttpServletRequest request) {
		if(!request.getParameter("name").equals("0")){
			filter.setName(request.getParameter("name"));
		}
		if(!request.getParameter("team").equals("0")){
			filter.setTeam(request.getParameter("team"));
		}if(!request.getParameter("orgteam").equals("0")){
			filter.setOrgteam(request.getParameter("orgteam"));
		}
		filter.setYear(request.getParameter("year"));
		filter.setMonth(request.getParameter("month"));
		Integer size=0;
		List<SPLITOFINTEREST> resultNewUser=new ArrayList<SPLITOFINTEREST>();
		//如果传过来的值有name
		if(!request.getParameter("name").equals("0")){
			List<SPLITOFINTEREST>resultUser=CoefficientService.selectSpliByTeam(filter);
			resultNewUser.add(0, resultUser.get(0));
		}
		//如果传过来有team
		else if(!request.getParameter("team").equals("0")){
			List<SPLITOFINTEREST>resultUser=CoefficientService.selectSpliByTeam(filter);
			for(int i1=0;i1<resultUser.size();i1++){
				resultNewUser.add(size+i1, resultUser.get(i1));
			}
			SPLITOFINTEREST resultUser1=CoefficientService.selectSumSpliByTeam(filter);
			resultUser1.setOrgteam("总计");
			resultUser1.setTeam(request.getParameter("team"));
			resultUser1.setName(request.getParameter("team"));
			resultUser1.setYear(request.getParameter("year"));
			resultUser1.setMonth(request.getParameter("month"));
			resultNewUser.add(size+resultUser.size(), resultUser1);
		}else{
			
			ManagerPerformmanceForm from1=new ManagerPerformmanceForm();
			from1.setOrdteam(request.getParameter("orgteam"));
			List<ManagerPerformmanceForm> result=managerPerformmanceService.selectAllManagerByOrgTeam(from1);
			if(result.size()>0){
				for(int i=0;i<result.size();i++){
					SPLITOFINTEREST spt=new SPLITOFINTEREST();
					spt.setYear(request.getParameter("year"));
					spt.setMonth(request.getParameter("month"));
					spt.setTeam(result.get(i).getTeam());
					List<SPLITOFINTEREST>resultUser=CoefficientService.selectSpliByTeam(spt);
					for(int i1=0;i1<resultUser.size();i1++){
						resultNewUser.add(size+i1, resultUser.get(i1));
					}
					SPLITOFINTEREST resultUser1=CoefficientService.selectSumSpliByTeam(spt);
					resultUser1.setOrgteam("总计");
					resultUser1.setTeam(request.getParameter("team"));
					resultUser1.setName(request.getParameter("team"));
					resultUser1.setYear(request.getParameter("year"));
					resultUser1.setMonth(request.getParameter("month"));
					resultNewUser.add(size+resultUser.size(), resultUser1);
					size+=resultUser.size()+1;
				}
				}
			SPLITOFINTEREST sp=new SPLITOFINTEREST();
			sp.setYear(request.getParameter("year"));
			sp.setMonth(request.getParameter("month"));
			SPLITOFINTEREST resultUser=CoefficientService.selectSumSpliByTeam(sp);
			resultUser.setOrgteam("累计");
			resultUser.setTeam("累计");
			resultUser.setName("累计");
			resultUser.setYear(request.getParameter("year"));
			resultUser.setMonth(request.getParameter("month"));
			resultNewUser.add(size, resultUser);
			
		}
		SPLITOFINTEREST sp1=new SPLITOFINTEREST();
		sp1.setYear(request.getParameter("year"));
		sp1.setMonth(request.getParameter("month"));
		JRadModelAndView mv = new JRadModelAndView("/jxlx/lxcfzl", request);
		mv.addObject("gxperformList", resultNewUser);
		mv.addObject("sp1", sp1);
		return mv;
		
		
		
		
	}
}
