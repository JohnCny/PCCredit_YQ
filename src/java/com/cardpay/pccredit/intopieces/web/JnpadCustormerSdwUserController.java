package com.cardpay.pccredit.intopieces.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.intopieces.model.AppManagerAuditLog;
import com.cardpay.pccredit.intopieces.model.CustomerSpUser;
import com.cardpay.pccredit.intopieces.model.CustormerSdwUser;
import com.cardpay.pccredit.intopieces.model.IntoPieces;
import com.cardpay.pccredit.intopieces.model.IntoPiecesFilters;
import com.cardpay.pccredit.intopieces.service.CustomerApplicationIntopieceWaitService;
import com.cardpay.pccredit.intopieces.service.JnpadCustormerSdwUserService;
import com.cardpay.pccredit.intopieces.service.JnpadSpUserService;
import com.cardpay.pccredit.ipad.util.JsonDateValueProcessor;
import com.cardpay.pccredit.riskControl.model.RiskCustomer;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.auth.JRadModule;
import com.wicresoft.jrad.base.database.id.IDGenerator;
import com.wicresoft.jrad.base.web.result.JRadReturnMap;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.util.spring.Beans;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Controller 
@RequestMapping("/jnpad/CustormerSdwUserPc/*")
@JRadModule("jnpad.CustormerSdwUserPc")
public class JnpadCustormerSdwUserController {
	@Autowired
	private JnpadCustormerSdwUserService SdwUserService;
	@Autowired
	private JnpadSpUserService UserService;
	@Autowired
	private CustomerApplicationIntopieceWaitService IntopieceWaitService;
	
	/**
	 * PC初审成功添加
	 * @param CustormerSdwUser
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "insertCSPC.json", method = { RequestMethod.GET })
	public JRadReturnMap insertCSPC(@ModelAttribute CustomerSpUser CustomerSpUser,@ModelAttribute AppManagerAuditLog AppManagerAuditLog,@ModelAttribute CustormerSdwUser CustormerSdwUser,HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
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
		AppManagerAuditLog.setUserId_4(user.getId());
		AppManagerAuditLog.setDbfs(request.getParameter("dbfs"));
		SdwUserService.insertCsJl(AppManagerAuditLog);
		//初审通过状态
		String applicationId=result.getApplicationId();
		String userId=user.getId();
		Date times=new Date();
		String money=request.getParameter("decisionAmount");
		SdwUserService.updateCSZT(userId,times,money,applicationId);
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
		return returnMap;
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
	@RequestMapping(value="insertsdjycs.json", method = { RequestMethod.GET })
	public JRadReturnMap insertsdjyPCcs(@ModelAttribute CustormerSdwUser CustormerSdwUser,@ModelAttribute IntoPieces IntoPieces,@ModelAttribute RiskCustomer RiskCustomer,@ModelAttribute CustomerSpUser CustomerSpUser,HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		CustomerSpUser.setSpje(request.getParameter("decisionAmount"));
		CustomerSpUser.setSptime(new Date());
		CustomerSpUser.setSpqx(request.getParameter("qx"));
		CustomerSpUser.setSpuserid(user.getId());
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
							returnMap.put("message", "提交成功");
						}
					}else{
						returnMap.put("message", "提交失败");
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
						CustormerSdwUser2.setSDWUSER1(user.getId());
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
			IntoPieces.setUserId(user.getId());
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
					RiskCustomer.setUserId(user.getId());
					RiskCustomer.setCustManagerId(request.getParameter("userId"));
					RiskCustomer.setId(IDGenerator.generateID());
					int e=SdwUserService.insertRiskSdwUser(RiskCustomer);
					//拒绝时修改节点状态
					String applicationId=request.getParameter("id");
					Date times=new Date();
					SdwUserService.updateHistorys(user.getId(),times,applicationId);
					if(e>0){
						returnMap.put("message", "提交成功");
					}else{
						returnMap.put("message", "提交失败");
					}
				}else{
					returnMap.put("message", "提交失败");
				}
			}else{
				returnMap.put("message", "提交失败");
			}
		
		}else if(request.getParameter("status").equals("returnedToFirst")){
			CustomerSpUser.setStatus("3");
			IntoPieces.setStatus("returnedToFirst");
			IntoPieces.setId(request.getParameter("id"));
			IntoPieces.setFallBackReason(request.getParameter("decisionRefusereason"));
			IntoPieces.setUserId(user.getId());
			IntoPieces.setCreatime(new Date());
			int c=SdwUserService.updateCustormerInfoSdwUser(IntoPieces);
			//退回时修改节点状态
			String applicationId=request.getParameter("id");
			Date times=new Date();
			SdwUserService.updateHistory(user.getId(),times,applicationId);
			if(c>0){
				int d=SdwUserService.updateCustormerProSdwUser(IntoPieces);
				if(d>0){
					returnMap.put("message", "提交成功");	
				}else{
					returnMap.put("message", "提交失败");
				}
			}
		}
		//添加当前客户经理的审贷
		int a=UserService.addSpUser1(CustomerSpUser);
		if(a>0){
			returnMap.put("message", "提交成功");	
		}else{
			returnMap.put("message", "提交失败");
		}
		return returnMap;
}
	
	/**
	 * 审贷决议
	 * @param CustormerSdwUser
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="insertsdjy.json", method = { RequestMethod.GET })
	public JRadReturnMap insertsdjy(@ModelAttribute RiskCustomer RiskCustomer,@ModelAttribute CustormerSdwUser CustormerSdwUser,@ModelAttribute IntoPieces IntoPieces,HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		String userId=user.getId();
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
				IntoPiecesFilters time=IntopieceWaitService.findfsw(request.getParameter("id"), user.getId());
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
							returnMap.put("message", "提交成功");
						}else{
							returnMap.put("message", "提交失败");
						}
				}
			}
				
			}
		}
		//如果当前审贷委拒绝或退回该进件，进件直接审批完成退回或拒绝到申请客户经理处
		else if(request.getParameter("status").equals("refuse")){
			
			IntoPieces.setStatus("refuse");
			IntoPieces.setCreatime(new Date());
			IntoPieces.setId(request.getParameter("id"));
			IntoPieces.setUserId(user.getId());
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
					RiskCustomer.setUserId(user.getId());
					RiskCustomer.setCustManagerId(request.getParameter("userId"));
					RiskCustomer.setId(IDGenerator.generateID());
					int e=SdwUserService.insertRiskSdwUser(RiskCustomer);
					//拒绝时修改节点状态
					String applicationId=request.getParameter("id");
					Date times=new Date();
					SdwUserService.updateHistorys(user.getId(),times,applicationId);
					if(e>0){
						returnMap.put("message", "提交成功");
					}else{
						returnMap.put("message", "提交失败");
					}
				}else{
					returnMap.put("message", "提交失败");
				}
			}else{
				returnMap.put("message", "提交失败");
			}
		
		}else if(request.getParameter("status").equals("returnedToFirst")){
			IntoPieces.setStatus("returnedToFirst");
			IntoPieces.setId(request.getParameter("id"));
			IntoPieces.setFallBackReason(request.getParameter("decisionRefusereason"));
			IntoPieces.setUserId(user.getId());
			IntoPieces.setCreatime(new Date());
			int c=SdwUserService.updateCustormerInfoSdwUser(IntoPieces);
			//退回时修改节点状态
			String applicationId=request.getParameter("id");
			Date times=new Date();
			SdwUserService.updateHistory(user.getId(),times,applicationId);
			if(c>0){
				int d=SdwUserService.updateCustormerProSdwUser(IntoPieces);
				if(d>0){
					returnMap.put("message", "提交成功");	
				}else{
					returnMap.put("message", "提交失败");
				}
			}
		}
			
		
		return returnMap;
}
}
