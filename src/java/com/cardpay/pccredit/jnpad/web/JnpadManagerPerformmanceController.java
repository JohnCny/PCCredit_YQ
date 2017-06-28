package com.cardpay.pccredit.jnpad.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.ipad.util.JsonDateValueProcessor;
import com.cardpay.pccredit.jnpad.model.MonthlyStatisticsModel;
import com.cardpay.pccredit.jnpad.service.MonthlyStatisticsService;
import com.cardpay.pccredit.manager.form.BankListForm;
import com.cardpay.pccredit.manager.form.DeptMemberForm;
import com.cardpay.pccredit.manager.form.ManagerPerformmanceForm;
import com.cardpay.pccredit.manager.model.ManagerPerformmance;
import com.cardpay.pccredit.manager.model.ManagerPerformmanceSum;
import com.cardpay.pccredit.manager.service.ManagerPerformmanceService;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.constant.JRadConstants;
import com.wicresoft.jrad.base.database.id.IDGenerator;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.result.JRadReturnMap;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.jrad.modules.privilege.model.User;
import com.wicresoft.util.spring.Beans;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Controller
public class JnpadManagerPerformmanceController {
	
	@Autowired
	private ManagerPerformmanceService managerPerformmanceService;
	@Autowired
	private MonthlyStatisticsService StatisticsService;
	
	//录入前查询
	@ResponseBody
	@RequestMapping(value = "/ipad/performmance/insertSelect.json")
	@JRadOperation(JRadOperation.BROWSE)
	public String create(HttpServletRequest request) {        
		String userId = request.getParameter("userId");
		//统计每天申请拒绝数
		int refuseNum=managerPerformmanceService.queryRefuse(userId);
		//统计每天申请数
		int applyNum= managerPerformmanceService.queryApply(userId);
		//统计每天内审数
		int auditNum=managerPerformmanceService.queryAudit(userId);
		//统计每天上会数
		int willNum=managerPerformmanceService.queryWill(userId);
		//统计每天通过数
		int passNum=managerPerformmanceService.queryPass(userId);
		Map<String, Integer> map=new HashMap<String, Integer>();
		map.put("applyNum", applyNum);
		map.put("refuseNum", refuseNum);
		map.put("auditNum", auditNum);
		map.put("willNum", willNum);
		map.put("passNum", passNum);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(map, jsonConfig);
		return json.toString();
	}
	/**
	 * 执行录入
	 * @param managerPerformmance
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ipad/performmance/update.json")
	public String update(@ModelAttribute ManagerPerformmance managerPerformmance,HttpServletRequest request) {        
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		String userId = request.getParameter("userId");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date=new Date();
		String time2=sdf.format(date);
		time2=time2.substring(0, 10);
			managerPerformmance.setManager_id(userId);
			List<ManagerPerformmance>  managerPerformmanceold = managerPerformmanceService.finManagerPerformmanceById(userId);
			if(managerPerformmanceold.size()>0){
				String time1=managerPerformmanceold.get(0).getCrateday();
				if(time1.equals(time2)){
					map.put("message", "当天已经提交过，不能重复提交");
				}else{
					String id = IDGenerator.generateID();
					managerPerformmance.setId(id);
					 Date d = new Date();  
				      SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");  
				      String dateNowStr = sdf1.format(d);  
					managerPerformmance.setCrateday(dateNowStr);
					managerPerformmanceService.insertManager(managerPerformmance); 
					managerPerformmanceService.updateManagerDateByUser(managerPerformmance);
					map.put("message", "提交成功");
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
				map.put("message", "提交成功");
			}
		
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(map, jsonConfig);
		return json.toString();
	}
	
	/**
	 * 查询当前客户经理的业绩信息（选择客户经理亦用此方法）
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ipad/performmance/selectManagerTeam.json")
	public String selectManagerTeam(HttpServletRequest request) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		String userId=request.getParameter("userId");
		  List<MonthlyStatisticsModel> resultModel=null;
		  List<ManagerPerformmanceForm>list=new ArrayList<ManagerPerformmanceForm>();
		List<ManagerPerformmanceForm>user=managerPerformmanceService.selectManagerTeam(userId);
		 Date d = new Date();  
	      SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");  
	      String dateNowStr = sdf1.format(d);
	      ManagerPerformmanceForm from=new ManagerPerformmanceForm();
	      from.setCrateday(dateNowStr);
	      from.setTeam(user.get(0).getTeam());
	      from.setOrdteam(user.get(0).getOrdteam());
	      resultModel=StatisticsService.findxzz(userId);
	      map.put("resultModel", resultModel);
	      map.put("size", resultModel.size());
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
	    	  map.put("form", list);
	    	  map.put("formsize", list.size());
	      }else{
	    	  from.setManager_id(userId);
	    	  List<ManagerPerformmanceForm> PerformmanceForm=managerPerformmanceService.selectAllManegerYj(from);
		      map.put("form", PerformmanceForm);
		      map.put("formsize", PerformmanceForm.size());
	      }
	      JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
			JSONObject json = JSONObject.fromObject(map, jsonConfig);
			return json.toString();
		
	}
	int ca=0;
/**
 * 管理员查询全部的业绩进度
 * @param request
 * @return
 */
	@ResponseBody
	@RequestMapping(value = "/ipad/performmance/selectAllManegerYj.page")
	public String selectAllManegerYj(HttpServletRequest request) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
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
	map.put("resultModel", result);
	map.put("size", result.size());
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
	for(int i=0;i<gxperformList.size();i++){
		for(int a=gxperformList.size()-1;a>=0;a--){
			ca=a;
			if(gxperformList.get(i).getName().equals(gxperformList.get(a).getName()) && !gxperformList.get(i).getName().contains("团队")){
				gxperformList.remove(a);
			}
		}
	}
		map.put("form", gxperformList);
		map.put("formsize", gxperformList.size());
		 JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
			JSONObject json = JSONObject.fromObject(map, jsonConfig);
			return json.toString();
	}	
	
	
	/**
	 * 管理员查询指定区域/团队的业绩
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/ipad/performmance/selectAllManagerByOrgTeam.page")
	public String selectAllManagerByOrgTeam(HttpServletRequest request) {
		 Date d = new Date();  
	      SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");  
	      String dateNowStr = sdf1.format(d);  
		Integer count=0;
		Map<String, Object> map = new LinkedHashMap<String, Object>();
	List<BankListForm> bankListForm = managerPerformmanceService.findALlbank();
		List<ManagerPerformmanceForm> gxperformList=new ArrayList<ManagerPerformmanceForm>();
		String team=request.getParameter("team");
		String orgteam=request.getParameter("orgteam");
		ManagerPerformmanceForm from=new ManagerPerformmanceForm();
		//如果传过来的有团队名称
		if(!team.equals("0")){
			if(!orgteam.equals("0")){
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
		map.put("form", gxperformList);
		map.put("formsize", gxperformList.size());
		 JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
			JSONObject json = JSONObject.fromObject(map, jsonConfig);
			return json.toString();
	}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 客户经理业绩进度查询
	 * @param request
	 * @return
	 *//*
	@ResponseBody
	@RequestMapping(value = "/ipad/performmance/browse.page")
	@JRadOperation(JRadOperation.BROWSE)
	public String browse(HttpServletRequest request) { 
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
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
				ManagerPerformmance managerPerformmanceold = managerPerformmanceService.finManagerPerformmanceById(managerId);
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
		map.put("result", gxperformList);
		map.put("satrtDate", satrtDate);
		map.put("endDate", endDate);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(map, jsonConfig);
		return json.toString();
	}

	//执行修改进度
	@ResponseBody
	@RequestMapping(value = "/ipad/performmance/performUpdate.json")
	public String updateinfo(@ModelAttribute ManagerPerformmance ManagerPerformmance,HttpServletRequest request) {        
		JRadReturnMap returnMap = new JRadReturnMap();
		
		if(ManagerPerformmance.getManager_id().equals("0")){
			returnMap.put("mess", "请选择客户经理");
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
			JSONObject json = JSONObject.fromObject(returnMap, jsonConfig);
			return json.toString();
		}
		try {
			String changedate = request.getParameter("changedate");
			if(changedate!=""){
			ManagerPerformmance managerperformmance= managerPerformmanceService.finManagerPerformmanceSumById(ManagerPerformmance.getManager_id(),changedate);
			if(managerperformmance==null){
				String id = IDGenerator.generateID();
				ManagerPerformmance.setId(id);
				String oldDate=changedate+" 12:00:00";
				ManagerPerformmance.setCrateday(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(oldDate));
				managerPerformmanceService.insertmanagerPerformmance(ManagerPerformmance);
				returnMap.put("mess", "该客户经理指定日期进度不存在，已补录成功！");
			}else{
				
				ManagerPerformmance.setId(managerperformmance.getId());
				managerPerformmanceService.updateManagerPerformmanceSumInfor(ManagerPerformmance);
				
				returnMap.put("mess", "修改进度成功");
			}
			}else{
				returnMap.put("mess", "日期不能为空");
			}
		} catch (Exception e) {
			returnMap.put(JRadConstants.SUCCESS, false);
			returnMap.put("mess", "提交失败");
			returnMap.addGlobalMessage("保存失败");
		}
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(returnMap, jsonConfig);
		return json.toString();
	}
	
	//根据ID查指定客户经理业绩进度总和
			@ResponseBody
			@RequestMapping(value = "/ipad/performmance/performselect.json")
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
*/
}
