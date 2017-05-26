package com.cardpay.pccredit.manager.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wicresoft.jrad.base.web.result.JRadReturnMap;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.jrad.base.web.utility.WebRequestHelper;
import com.wicresoft.jrad.modules.privilege.model.User;
import com.cardpay.pccredit.ipad.util.JsonDateValueProcessor;
import com.cardpay.pccredit.jnpad.model.MonthlyStatisticsModel;
import com.cardpay.pccredit.jnpad.service.MonthlyStatisticsService;
import com.cardpay.pccredit.manager.form.BankListForm;
import com.cardpay.pccredit.manager.form.DeptMemberForm;
import com.cardpay.pccredit.manager.form.ManagerPerformmanceForm;
import com.cardpay.pccredit.manager.model.ManagerPerformmance;
import com.cardpay.pccredit.manager.service.ManagerPerformmanceService;
import com.wicresoft.jrad.base.auth.JRadModule;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.constant.JRadConstants;
import com.wicresoft.jrad.base.database.id.IDGenerator;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.controller.BaseController;
import com.wicresoft.util.spring.Beans;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Controller
@RequestMapping("/manager/performmance/*")
@JRadModule("manager.performmance")
public class ManagerPerformmanceController extends BaseController {

	@Autowired
	private ManagerPerformmanceService managerPerformmanceService;
	@Autowired
	private MonthlyStatisticsService StatisticsService;
	/**
	 * 业绩录入页面
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "insert.page")
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView create(HttpServletRequest request) {        
		JRadModelAndView mv = new JRadModelAndView("/manager/performmance/performmanceInsert", request);
		User user = (User) Beans.get(LoginManager.class).getLoggedInUser(request);
		
		//统计每天申请拒绝数
		int refuseNum=managerPerformmanceService.queryRefuse(user.getId());
		//统计每天申请数
		int applyNum= managerPerformmanceService.queryApply(user.getId());
		//统计每天内审数
		int auditNum=managerPerformmanceService.queryAudit(user.getId());
		//统计每天上会数
		int willNum=managerPerformmanceService.queryWill(user.getId());
		//统计每天通过数
		int passNum=managerPerformmanceService.queryPass(user.getId());
		Map<String, Integer> map=new HashMap<String, Integer>();
		map.put("applyNum", applyNum);
		map.put("refuseNum", refuseNum);
		map.put("auditNum", auditNum);
		map.put("willNum", willNum);
		map.put("passNum", passNum);
		mv.addObject("ssss", map);
		return mv;
	}
	/**
	 * 客户经理业绩进度查询
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "browse.page")
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView browse(HttpServletRequest request) { 
		List<BankListForm> bankListForm = managerPerformmanceService.findALlbank();
		List<ManagerPerformmanceForm> gxperformList = new ArrayList<ManagerPerformmanceForm>();
		String satrtDate = request.getParameter("startdate");
		String endDate = request.getParameter("enddate");
		if(satrtDate!=null&&satrtDate!=""){
			satrtDate+=" 00:00:00";
		}
		if(endDate!=null&&endDate!=""){
			endDate+=" 23:59:59";
		}
		for(int j=0;j<bankListForm.size();j++){
			String id= bankListForm.get(j).getId();
			List<DeptMemberForm> gxMumberList = managerPerformmanceService.findMumberByDeptId(id);
			for(int i=0;i<gxMumberList.size();i++){
				String managerId =gxMumberList.get(i).getId();
				ManagerPerformmanceForm managerPerformmanceForm= managerPerformmanceService.findSumPerformmanceById(managerId,satrtDate,endDate);
				if(managerPerformmanceForm==null){
					continue;
				}
				ManagerPerformmance managerPerformmanceold = managerPerformmanceService.finManagerPerformmanceById1(managerId);
				if(managerPerformmanceold!=null){
				managerPerformmanceForm.setApplycount(managerPerformmanceold.getApplycount());
				managerPerformmanceForm.setApplyrefuse(managerPerformmanceold.getApplyrefuse());
				managerPerformmanceForm.setCreditcount(managerPerformmanceold.getCreditcount());
				managerPerformmanceForm.setCreditrefuse(managerPerformmanceold.getCreditrefuse());
				managerPerformmanceForm.setGivemoneycount(managerPerformmanceold.getGivemoneycount());
				managerPerformmanceForm.setInternalcount(managerPerformmanceold.getInternalcount());
				managerPerformmanceForm.setMeetingcout(managerPerformmanceold.getMeetingcout());
				managerPerformmanceForm.setPasscount(managerPerformmanceold.getPasscount());
				managerPerformmanceForm.setRealycount(managerPerformmanceold.getRealycount());
				managerPerformmanceForm.setReportcount(managerPerformmanceold.getReportcount());
				managerPerformmanceForm.setSigncount(managerPerformmanceold.getSigncount());
				managerPerformmanceForm.setVisitcount(managerPerformmanceold.getVisitcount());
				}
				managerPerformmanceForm.setName(gxMumberList.get(i).getOname());
				managerPerformmanceForm.setManagerName(gxMumberList.get(i).getDisplay_name());
				gxperformList.add(managerPerformmanceForm);
			}
			ManagerPerformmanceForm managerPerformmanceForm1 = managerPerformmanceService.findDeptSumPerformmanceById(id,satrtDate,endDate);
			if(managerPerformmanceForm1==null){
				continue;
			}
			ManagerPerformmance managerPerformmancezhi = managerPerformmanceService.findDeptTodayPerformmanceById(id);
			if(managerPerformmancezhi!=null){
				managerPerformmanceForm1.setApplycount(managerPerformmancezhi.getApplycount());
				managerPerformmanceForm1.setApplyrefuse(managerPerformmancezhi.getApplyrefuse());
				managerPerformmanceForm1.setCreditcount(managerPerformmancezhi.getCreditcount());
				managerPerformmanceForm1.setCreditrefuse(managerPerformmancezhi.getCreditrefuse());
				managerPerformmanceForm1.setGivemoneycount(managerPerformmancezhi.getGivemoneycount());
				managerPerformmanceForm1.setInternalcount(managerPerformmancezhi.getInternalcount());
				managerPerformmanceForm1.setMeetingcout(managerPerformmancezhi.getMeetingcout());
				managerPerformmanceForm1.setPasscount(managerPerformmancezhi.getPasscount());
				managerPerformmanceForm1.setRealycount(managerPerformmancezhi.getRealycount());
				managerPerformmanceForm1.setReportcount(managerPerformmancezhi.getReportcount());
				managerPerformmanceForm1.setSigncount(managerPerformmancezhi.getSigncount());
				managerPerformmanceForm1.setVisitcount(managerPerformmancezhi.getVisitcount());
			}
			managerPerformmanceForm1.setName(bankListForm.get(j).getName());
			managerPerformmanceForm1.setManagerName("汇总");
			gxperformList.add(managerPerformmanceForm1);
		}
		ManagerPerformmanceForm managerPerformmanceForm2 = managerPerformmanceService.findALLDeptSumPerformmanceById(satrtDate,endDate);
		if(managerPerformmanceForm2!=null){
			ManagerPerformmance managerPerformmancezong = managerPerformmanceService.findDeptTodaySumPerformmanceById();	
			if(managerPerformmancezong!=null){
				managerPerformmanceForm2.setApplycount(managerPerformmancezong.getApplycount());
				managerPerformmanceForm2.setApplyrefuse(managerPerformmancezong.getApplyrefuse());
				managerPerformmanceForm2.setCreditcount(managerPerformmancezong.getCreditcount());
				managerPerformmanceForm2.setCreditrefuse(managerPerformmancezong.getCreditrefuse());
				managerPerformmanceForm2.setGivemoneycount(managerPerformmancezong.getGivemoneycount());
				managerPerformmanceForm2.setInternalcount(managerPerformmancezong.getInternalcount());
				managerPerformmanceForm2.setMeetingcout(managerPerformmancezong.getMeetingcout());
				managerPerformmanceForm2.setPasscount(managerPerformmancezong.getPasscount());
				managerPerformmanceForm2.setRealycount(managerPerformmancezong.getRealycount());
				managerPerformmanceForm2.setReportcount(managerPerformmancezong.getReportcount());
				managerPerformmanceForm2.setSigncount(managerPerformmancezong.getSigncount());
				managerPerformmanceForm2.setVisitcount(managerPerformmancezong.getVisitcount());
			}
			managerPerformmanceForm2.setName("统计");
			managerPerformmanceForm2.setManagerName("总计");
			gxperformList.add(managerPerformmanceForm2);
		}
		JRadModelAndView mv = new JRadModelAndView("/manager/performmance/performmance_sum", request);
		mv.addObject("gxperformList", gxperformList);
		mv.addObject("satrtDate", satrtDate);
		mv.addObject("endDate", endDate);
		return mv;
	}

	/**
	 * 执行录入
	 * @param managerPerformmance
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "update.json")
	public JRadReturnMap update(@ModelAttribute ManagerPerformmance managerPerformmance,HttpServletRequest request) {        
		JRadReturnMap returnMap = new JRadReturnMap();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date=new Date();
		String time2=sdf.format(date);
		time2=time2.substring(0, 10);
			User user = (User) Beans.get(LoginManager.class).getLoggedInUser(request);
			managerPerformmance.setManager_id(user.getId());
			List<ManagerPerformmance>  managerPerformmanceold = managerPerformmanceService.finManagerPerformmanceById(user.getId());
			if(managerPerformmanceold.size()>0){
				String time1=managerPerformmanceold.get(0).getCrateday();
			/*	time1=time1.toString().substring(0,10);*/
				if(time1.equals(time2)){
					returnMap.put("message", "当天已经提交过，不能重复提交");
				}else{
					String id = IDGenerator.generateID();
					managerPerformmance.setId(id);
					 Date d = new Date();  
				      SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");  
				      String dateNowStr = sdf1.format(d);  
					managerPerformmance.setCrateday(dateNowStr);
					managerPerformmanceService.insertManager(managerPerformmance); 
					managerPerformmanceService.updateManagerDateByUser(managerPerformmance);
					returnMap.put("message", "提交成功");
				}
			}else{
				String id = IDGenerator.generateID();
				managerPerformmance.setId(id);
				 Date d = new Date();  
			      SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");  
			      String dateNowStr = sdf1.format(d);  
				managerPerformmance.setCrateday(dateNowStr);
				managerPerformmanceService.insertManager(managerPerformmance);
				managerPerformmanceService.updateManagerDateByUser(managerPerformmance);
				returnMap.put("message", "提交成功");
			}
			return returnMap;
	}

	//修改进度页面
	@ResponseBody
	@RequestMapping(value = "performUpdate.page")
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView performUpdate(HttpServletRequest request) {        
		JRadModelAndView mv = new JRadModelAndView("/manager/performmance/performmanceUpdate", request);
		return mv;
	}
	//根据ID查指定客户经理业绩进度总和
		@ResponseBody
		@RequestMapping(value = "performsumselect.json")
		@JRadOperation(JRadOperation.BROWSE)
		public String performselect(HttpServletRequest request) {        
			
			String managerId= request.getParameter("managerId");
			String changedate = request.getParameter("changedate");
			ManagerPerformmance managerperformmance= managerPerformmanceService.finManagerPerformmanceSumById(managerId,changedate);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
			JSONObject json = JSONObject.fromObject(managerperformmance, jsonConfig);
			return json.toString();
		}

/*	//导出业绩进度信息
	@ResponseBody
	@RequestMapping(value = "performexport.json", method = { RequestMethod.GET })
	public JRadReturnMap JRadReturnMap(HttpServletRequest request,HttpServletResponse response) {        

		JRadReturnMap returnMap = new JRadReturnMap();
		String satrtDate = request.getParameter("startdate");
		String endDate = request.getParameter("enddate");
		String satrtDate1 = satrtDate;
		String endDate1 = endDate;
		try{
			List<BankListForm> bankListForm = managerPerformmanceService.findALlbank();
			List<ManagerPerformmanceForm> gxperformList = new ArrayList<ManagerPerformmanceForm>();
			if(satrtDate!=null&&satrtDate!=""){
				satrtDate+=" 00:00:00";
			}
			if(endDate!=null&&endDate!=""){
				endDate+=" 23:59:59";
			}
			for(int j=0;j<bankListForm.size();j++){
				String id= bankListForm.get(j).getId();
				List<DeptMemberForm> gxMumberList = managerPerformmanceService.findMumberByDeptId(id);
				for(int i=0;i<gxMumberList.size();i++){
					String managerId =gxMumberList.get(i).getId();
					ManagerPerformmanceForm managerPerformmanceForm= managerPerformmanceService.findSumPerformmanceById(managerId,satrtDate,endDate);
					if(managerPerformmanceForm==null){
						continue;
					}
					ManagerPerformmance managerPerformmanceold = managerPerformmanceService.finManagerPerformmanceById1(managerId);
					if(managerPerformmanceold!=null){
					managerPerformmanceForm.setApplycount(managerPerformmanceold.getApplycount());
					managerPerformmanceForm.setApplyrefuse(managerPerformmanceold.getApplyrefuse());
					managerPerformmanceForm.setCreditcount(managerPerformmanceold.getCreditcount());
					managerPerformmanceForm.setCreditrefuse(managerPerformmanceold.getCreditrefuse());
					managerPerformmanceForm.setGivemoneycount(managerPerformmanceold.getGivemoneycount());
					managerPerformmanceForm.setInternalcount(managerPerformmanceold.getInternalcount());
					managerPerformmanceForm.setMeetingcout(managerPerformmanceold.getMeetingcout());
					managerPerformmanceForm.setPasscount(managerPerformmanceold.getPasscount());
					managerPerformmanceForm.setRealycount(managerPerformmanceold.getRealycount());
					managerPerformmanceForm.setReportcount(managerPerformmanceold.getReportcount());
					managerPerformmanceForm.setSigncount(managerPerformmanceold.getSigncount());
					managerPerformmanceForm.setVisitcount(managerPerformmanceold.getVisitcount());
					}
					managerPerformmanceForm.setName(gxMumberList.get(i).getOname());
					managerPerformmanceForm.setManagerName(gxMumberList.get(i).getDisplay_name());
					gxperformList.add(managerPerformmanceForm);
				}
				ManagerPerformmanceForm managerPerformmanceForm1 = managerPerformmanceService.findDeptSumPerformmanceById(id,satrtDate,endDate);
				if(managerPerformmanceForm1==null){
					continue;
				}
				ManagerPerformmance managerPerformmancezhi = managerPerformmanceService.findDeptTodayPerformmanceById(id);
				if(managerPerformmancezhi!=null){
					managerPerformmanceForm1.setApplycount(managerPerformmancezhi.getApplycount());
					managerPerformmanceForm1.setApplyrefuse(managerPerformmancezhi.getApplyrefuse());
					managerPerformmanceForm1.setCreditcount(managerPerformmancezhi.getCreditcount());
					managerPerformmanceForm1.setCreditrefuse(managerPerformmancezhi.getCreditrefuse());
					managerPerformmanceForm1.setGivemoneycount(managerPerformmancezhi.getGivemoneycount());
					managerPerformmanceForm1.setInternalcount(managerPerformmancezhi.getInternalcount());
					managerPerformmanceForm1.setMeetingcout(managerPerformmancezhi.getMeetingcout());
					managerPerformmanceForm1.setPasscount(managerPerformmancezhi.getPasscount());
					managerPerformmanceForm1.setRealycount(managerPerformmancezhi.getRealycount());
					managerPerformmanceForm1.setReportcount(managerPerformmancezhi.getReportcount());
					managerPerformmanceForm1.setSigncount(managerPerformmancezhi.getSigncount());
					managerPerformmanceForm1.setVisitcount(managerPerformmancezhi.getVisitcount());
				}
				managerPerformmanceForm1.setName(bankListForm.get(j).getName());
				managerPerformmanceForm1.setManagerName("汇总");
				gxperformList.add(managerPerformmanceForm1);
			}
			ManagerPerformmanceForm managerPerformmanceForm2 = managerPerformmanceService.findALLDeptSumPerformmanceById(satrtDate,endDate);
			if(managerPerformmanceForm2!=null){
				ManagerPerformmance managerPerformmancezong = managerPerformmanceService.findDeptTodaySumPerformmanceById();	
				if(managerPerformmancezong!=null){
					managerPerformmanceForm2.setApplycount(managerPerformmancezong.getApplycount());
					managerPerformmanceForm2.setApplyrefuse(managerPerformmancezong.getApplyrefuse());
					managerPerformmanceForm2.setCreditcount(managerPerformmancezong.getCreditcount());
					managerPerformmanceForm2.setCreditrefuse(managerPerformmancezong.getCreditrefuse());
					managerPerformmanceForm2.setGivemoneycount(managerPerformmancezong.getGivemoneycount());
					managerPerformmanceForm2.setInternalcount(managerPerformmancezong.getInternalcount());
					managerPerformmanceForm2.setMeetingcout(managerPerformmancezong.getMeetingcout());
					managerPerformmanceForm2.setPasscount(managerPerformmancezong.getPasscount());
					managerPerformmanceForm2.setRealycount(managerPerformmancezong.getRealycount());
					managerPerformmanceForm2.setReportcount(managerPerformmancezong.getReportcount());
					managerPerformmanceForm2.setSigncount(managerPerformmancezong.getSigncount());
					managerPerformmanceForm2.setVisitcount(managerPerformmancezong.getVisitcount());
				}
				managerPerformmanceForm2.setName("统计");
				managerPerformmanceForm2.setManagerName("总计");
				gxperformList.add(managerPerformmanceForm2);
			}
			managerPerformmanceService.getExportWageData(gxperformList, response,satrtDate1,endDate1);
		}
		catch (Exception e) {
			return WebRequestHelper.processException(e);
		}
		return returnMap;
	}*/
		//导出业绩进度信息
		@ResponseBody
		@RequestMapping(value = "performexport.json", method = { RequestMethod.GET })
		public JRadReturnMap JRadReturnMap(HttpServletRequest request,HttpServletResponse response) {        
			User user = (User) Beans.get(LoginManager.class).getLoggedInUser(request);
			String userId=user.getId();
			JRadReturnMap returnMap = new JRadReturnMap();
			if(user.getUserType()!=1){
			Integer count=0;
			 Date d = new Date();  
		      SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");  
		      String dateNowStr = sdf1.format(d);  
		List<ManagerPerformmanceForm> gxperformList=new ArrayList<ManagerPerformmanceForm>();
		List<ManagerPerformmanceForm>result=managerPerformmanceService.selectAllManegerTeam();
		for(int c=result.size()-1;c>=0;c--){
			result.get(c).setCrateday(dateNowStr);
			if(result.get(c).getTeam().equals("管理团队")){
				result.remove(c);			}
		}
		for(int i=0;i<result.size();i++){
			List<ManagerPerformmanceForm> form=managerPerformmanceService.selectAllManegerYj(result.get(i));
			for(int a=0;a<form.size();a++){
				gxperformList.add(count+a, form.get(a));
			}
			ManagerPerformmanceForm Form =new ManagerPerformmanceForm();
			Form.setTeam(result.get(i).getTeam());
			Form.setCrateday(dateNowStr);
			ManagerPerformmanceForm form1=managerPerformmanceService.selectAllTeamYj(Form);
			form1.setOrdteam("总计");
			form1.setTeam(result.get(i).getTeam());
			form1.setName(result.get(i).getTeam());
			gxperformList.add(count+form.size(), form1);
			count+=form.size()+1;
		}
		for(int b=0;b<result.size();b++){
			ManagerPerformmanceForm Form1 =new ManagerPerformmanceForm();
			Form1.setOrdteam(result.get(b).getOrdteam());
			Form1.setCrateday(dateNowStr);
			ManagerPerformmanceForm form1=managerPerformmanceService.selectAllTeamYj(Form1);
			form1.setOrdteam("汇总");
			form1.setTeam(result.get(b).getOrdteam());
			form1.setName(result.get(b).getOrdteam());
			gxperformList.add(count, form1);
			count+=1;
		}
		managerPerformmanceService.getExportWageData(gxperformList, response);
			
			}else{
				  List<MonthlyStatisticsModel> resultModel=null;
				  List<ManagerPerformmanceForm>list=new ArrayList<ManagerPerformmanceForm>();
				List<ManagerPerformmanceForm>user1=managerPerformmanceService.selectManagerTeam(userId);
				 Date d = new Date();  
			      SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");  
			      String dateNowStr = sdf1.format(d);
			      ManagerPerformmanceForm from=new ManagerPerformmanceForm();
			      from.setCrateday(dateNowStr);
			      from.setTeam(user1.get(0).getTeam());
			      from.setOrdteam(user1.get(0).getOrdteam());
			      resultModel=StatisticsService.findxzz(userId);
			      //如果该客户经理是组长则查询团队业绩
			      if(resultModel!=null && resultModel.size()!=0){
			    	  for(int i=0;i<resultModel.size();i++){
			    		  from.setManager_id(resultModel.get(i).getUserId());
			    		  List<ManagerPerformmanceForm> PerformmanceForm=managerPerformmanceService.selectAllManegerYj(from);
			    		  list.add(i, PerformmanceForm.get(0));
			    		  from.setManager_id("");
			    	  }
			    	  from.setOrdteam("");
			    	  ManagerPerformmanceForm form1=managerPerformmanceService.selectAllTeamYj(from);
			    	  form1.setOrdteam("总计");
			    	  form1.setTeam(from.getTeam());
			    	  form1.setName(from.getTeam());
			    	  list.add(resultModel.size(), form1);
			    	  managerPerformmanceService.getExportWageData(list, response);
			      }else{
			    	  from.setManager_id(userId);
			    	  List<ManagerPerformmanceForm> PerformmanceForm=managerPerformmanceService.selectAllManegerYj(from);
			    	  managerPerformmanceService.getExportWageData(PerformmanceForm, response);
			      }
			}
			return returnMap;
		}	/**
		 * 业务进度查询
		 * @param request
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "selectAllManegerYj.page")
		@JRadOperation(JRadOperation.BROWSE)
		public AbstractModelAndView selectAllManegerYj(HttpServletRequest request) {
			User user = (User) Beans.get(LoginManager.class).getLoggedInUser(request);
			String userId=user.getId();
			if(user.getUserType()!=1){
			Integer count=0;
			 Date d = new Date();  
		      SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");  
		      String dateNowStr = sdf1.format(d);  
		List<ManagerPerformmanceForm> gxperformList=new ArrayList<ManagerPerformmanceForm>();
		List<ManagerPerformmanceForm>result=managerPerformmanceService.selectAllManegerTeam();
		for(int c=result.size()-1;c>=0;c--){
			result.get(c).setCrateday(dateNowStr);
			if(result.get(c).getTeam().equals("管理团队")){
				result.remove(c);			}
		}

		
		for(int i=0;i<result.size();i++){
			List<ManagerPerformmanceForm> form=managerPerformmanceService.selectAllManegerYj(result.get(i));
			for(int a=0;a<form.size();a++){
				gxperformList.add(count+a, form.get(a));
			}
			ManagerPerformmanceForm Form =new ManagerPerformmanceForm();
			Form.setTeam(result.get(i).getTeam());
			Form.setCrateday(dateNowStr);
			ManagerPerformmanceForm form1=managerPerformmanceService.selectAllTeamYj(Form);
			form1.setOrdteam("总计");
			form1.setTeam(result.get(i).getTeam());
			form1.setName(result.get(i).getTeam());
			gxperformList.add(count+form.size(), form1);
			count+=form.size()+1;
		}
		
		for(int b=0;b<result.size();b++){
			
			for(int j=result.size()-1;j>=0;j--){
				if(j!=b){
					if(result.get(j).getOrdteam().equals(result.get(b).getOrdteam())){
						result.remove(j);
					}
				}
			}
		}
		
		for(int b=0;b<result.size();b++){
			
			ManagerPerformmanceForm Form1 =new ManagerPerformmanceForm();
			Form1.setOrdteam(result.get(b).getOrdteam());
			Form1.setCrateday(dateNowStr);
			ManagerPerformmanceForm form1=managerPerformmanceService.selectAllTeamYj(Form1);
			form1.setOrdteam("汇总");
			form1.setTeam(result.get(b).getOrdteam());
			form1.setName(result.get(b).getOrdteam());
			gxperformList.add(count, form1);
			count+=1;
		}
		ManagerPerformmanceForm form2 =new ManagerPerformmanceForm();
		ManagerPerformmanceForm Form2=managerPerformmanceService.selectAllTeamYj(form2);
		Form2.setOrdteam("累计");
		Form2.setTeam("累计");
		Form2.setName("累计");
		gxperformList.add(count, Form2);
			JRadModelAndView mv = new JRadModelAndView("/manager/performmance/performmance_sum", request);
			mv.addObject("gxperformList", gxperformList);
			mv.addObject("result", result);
			return mv;
			}else{
				JRadModelAndView mv = new JRadModelAndView("/manager/performmance/performmance_sum1", request);
				  List<MonthlyStatisticsModel> resultModel=null;
				  List<ManagerPerformmanceForm>list=new ArrayList<ManagerPerformmanceForm>();
				List<ManagerPerformmanceForm>user1=managerPerformmanceService.selectManagerTeam(userId);
				 Date d = new Date();  
			      SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");  
			      String dateNowStr = sdf1.format(d);
			      ManagerPerformmanceForm from=new ManagerPerformmanceForm();
			      from.setCrateday(dateNowStr);
			      from.setTeam(user1.get(0).getTeam());
			      from.setOrdteam(user1.get(0).getOrdteam());
			      resultModel=StatisticsService.findxzz(userId);
			  	mv.addObject("resultModel", resultModel);
			      //如果该客户经理是组长则查询团队业绩
			      if(resultModel!=null && resultModel.size()!=0){
			    	  for(int i=0;i<resultModel.size();i++){
			    		  from.setManager_id(resultModel.get(i).getUserId());
			    		  List<ManagerPerformmanceForm> PerformmanceForm=managerPerformmanceService.selectAllManegerYj(from);
			    		  list.add(i, PerformmanceForm.get(0));
			    		  from.setManager_id("");
			    	  }
			    	  from.setOrdteam("");
			    	  ManagerPerformmanceForm form1=managerPerformmanceService.selectAllTeamYj(from);
			    	  form1.setOrdteam("总计");
			    	  form1.setTeam(from.getTeam());
			    	  form1.setName(from.getTeam());
			    	  list.add(resultModel.size(), form1);
			    	  mv.addObject("gxperformList", list);
			    	  mv.addObject("hava", "有");
			    	  return mv;
			      }else{
			    	  from.setManager_id(userId);
			    	  List<ManagerPerformmanceForm> PerformmanceForm=managerPerformmanceService.selectAllManegerYj(from);
			    	  mv.addObject("gxperformList", PerformmanceForm);
			    	  mv.addObject("hava", "无");
			    	  return mv;
			      }
			}
		}	
		
		
		/**
		 * 客户经理主管查询制定客户经理的业绩
		 * @param request
		 * @param response
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "selectUserName.json", method = { RequestMethod.GET })
		public AbstractModelAndView selectUserName(HttpServletRequest request,HttpServletResponse response) { 
			User user = (User) Beans.get(LoginManager.class).getLoggedInUser(request);
			JRadModelAndView mv = new JRadModelAndView("/manager/performmance/performmance_sum1", request);
			List<MonthlyStatisticsModel> resultModel=null;
			  resultModel=StatisticsService.findxzz(user.getId());
			  mv.addObject("resultModel", resultModel);
				 Date d = new Date();  
			      SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");  
			      String dateNowStr = sdf1.format(d);
			      ManagerPerformmanceForm from=new ManagerPerformmanceForm();
			      from.setCrateday(dateNowStr);
			      from.setManager_id(request.getParameter("id"));
			      List<ManagerPerformmanceForm> PerformmanceForm=managerPerformmanceService.selectAllManegerYj(from);
		    	  mv.addObject("gxperformList", PerformmanceForm);
		    	  mv.addObject("hava", "有");
				return mv;
		}
		
		
		/**
		 * 查询指定团队/区域的业绩
		 * @param request
		 * @param response
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "selectTeamName.json", method = { RequestMethod.GET })
		public AbstractModelAndView selectTeamName(HttpServletRequest request,HttpServletResponse response) { 
			JRadModelAndView mv = new JRadModelAndView("/manager/performmance/performmance_sum", request);
			Date d = new Date();  
		      SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");  
		      String dateNowStr = sdf1.format(d);  
			Integer count=0;
			List<ManagerPerformmanceForm> gxperformList=new ArrayList<ManagerPerformmanceForm>();
			String team=request.getParameter("team");
			String orgteam=request.getParameter("orgteam");
			ManagerPerformmanceForm from=new ManagerPerformmanceForm();
			//如果传过来的有团队名称
			if(!team.equals("")){
				if(!orgteam.equals("")){
					from.setOrdteam(orgteam);
				}
				from.setTeam(team);
				from.setCrateday(dateNowStr);
				List<ManagerPerformmanceForm> result=managerPerformmanceService.selectAllManegerYj(from);
				if(result.size()>0){
					for(int i=0;i<result.size();i++){
						gxperformList.add(i, result.get(i));
					}
					ManagerPerformmanceForm result1= managerPerformmanceService.selectAllTeamYj(from);
					if(result1!=null){
						result1.setOrdteam("总计");
						result1.setTeam(team);
						result1.setName(team);
						gxperformList.add(result.size(), result1);
					}
				}
			}else{
				ManagerPerformmanceForm from1=new ManagerPerformmanceForm();
				from.setOrdteam(orgteam);
				from.setCrateday(dateNowStr);
				List<ManagerPerformmanceForm> result=managerPerformmanceService.selectAllManagerByOrgTeam(from);
				if(result.size()>0){
				for(int i=0;i<result.size();i++){
					from1.setTeam(result.get(i).getTeam());
					from1.setCrateday(dateNowStr);
					List<ManagerPerformmanceForm> result1=managerPerformmanceService.selectAllManegerYj(from1);
					if(result1.size()>0){
						for(int a=0;a<result1.size();a++){
							gxperformList.add(count+a, result1.get(a));	
						}
						ManagerPerformmanceForm result2= managerPerformmanceService.selectAllTeamYj(from1);
						if(result2!=null){
							result2.setOrdteam("总计");
							result2.setTeam(result.get(i).getTeam());
							result2.setName(result.get(i).getTeam());
							gxperformList.add(count+result1.size(), result2);
							count+=result1.size()+1;
						}
					}
				}
				ManagerPerformmanceForm result3= managerPerformmanceService.selectAllTeamYj(from);
				if(result3!=null){
					result3.setOrdteam("汇总");
					result3.setTeam(orgteam);
					result3.setName(orgteam);
					gxperformList.add(count, result3);
				}
				}
			}
			mv.addObject("gxperformList", gxperformList);
			List<ManagerPerformmanceForm>result=managerPerformmanceService.selectAllManegerTeam();
			for(int c=result.size()-1;c>=0;c--){
				result.get(c).setCrateday(dateNowStr);
				if(result.get(c).getTeam().equals("管理团队")){
					result.remove(c);			}
			}
			mv.addObject("result", result);
			return mv;
		}
		
		
		
		
		@ResponseBody
		@RequestMapping(value = "portselectTeamName.json", method = { RequestMethod.GET })
		public JRadReturnMap portselectTeamName(HttpServletRequest request,HttpServletResponse response) { 
			JRadReturnMap returnMap = new JRadReturnMap();
			Date d = new Date();  
		      SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");  
		      String dateNowStr = sdf1.format(d);  
			Integer count=0;
			List<ManagerPerformmanceForm> gxperformList=new ArrayList<ManagerPerformmanceForm>();
			String team=request.getParameter("team");
			String orgteam=request.getParameter("orgteam");
			ManagerPerformmanceForm from=new ManagerPerformmanceForm();
			//如果传过来的有团队名称
			if(!team.equals("")){
				if(!orgteam.equals("")){
					from.setOrdteam(orgteam);
				}
				from.setTeam(team);
				from.setCrateday(dateNowStr);
				List<ManagerPerformmanceForm> result=managerPerformmanceService.selectAllManegerYj(from);
				if(result.size()>0){
					for(int i=0;i<result.size();i++){
						gxperformList.add(i, result.get(i));
					}
					ManagerPerformmanceForm result1= managerPerformmanceService.selectAllTeamYj(from);
					if(result1!=null){
						result1.setOrdteam("总计");
						result1.setTeam(team);
						result1.setName(team);
						gxperformList.add(result.size(), result1);
					}
				}
			}else{
				ManagerPerformmanceForm from1=new ManagerPerformmanceForm();
				from.setOrdteam(orgteam);
				from.setCrateday(dateNowStr);
				List<ManagerPerformmanceForm> result=managerPerformmanceService.selectAllManagerByOrgTeam(from);
				if(result.size()>0){
				for(int i=0;i<result.size();i++){
					from1.setTeam(result.get(i).getTeam());
					from1.setCrateday(dateNowStr);
					List<ManagerPerformmanceForm> result1=managerPerformmanceService.selectAllManegerYj(from1);
					if(result1.size()>0){
						for(int a=0;a<result1.size();a++){
							gxperformList.add(count+a, result1.get(a));	
						}
						ManagerPerformmanceForm result2= managerPerformmanceService.selectAllTeamYj(from1);
						if(result2!=null){
							result2.setOrdteam("总计");
							result2.setTeam(result.get(i).getTeam());
							result2.setName(result.get(i).getTeam());
							gxperformList.add(count+result1.size(), result2);
							count+=result1.size()+1;
						}
					}
				}
				ManagerPerformmanceForm result3= managerPerformmanceService.selectAllTeamYj(from);
				if(result3!=null){
					result3.setOrdteam("汇总");
					result3.setTeam(orgteam);
					result3.setName(orgteam);
					gxperformList.add(count, result3);
				}
				}
			}
			managerPerformmanceService.getExportWageData(gxperformList, response);
			return returnMap;
		}
		
}

