package com.cardpay.pccredit.jnpad.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cardpay.pccredit.customer.constant.CustomerInforConstant;
import com.cardpay.pccredit.intopieces.web.LocalImageForm;
import com.cardpay.pccredit.ipad.util.JsonDateValueProcessor;
import com.cardpay.pccredit.jnpad.dao.JnpadPcCustomerDao;
import com.cardpay.pccredit.jnpad.model.CustomerInfo;
import com.cardpay.pccredit.jnpad.model.JnpadPcCustomer;
import com.cardpay.pccredit.jnpad.service.JnpadCustomerInfoInsertServ‎ice;
import com.cardpay.pccredit.jnpad.service.JnpadPcCustomerService;
import com.jcraft.jsch.SftpException;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.constant.JRadConstants;
import com.wicresoft.jrad.base.database.id.IDGenerator;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Controller
public class JnpadPcCustomerController {
	@Autowired
	private JnpadPcCustomerService CustomerService;
	@Autowired
	private JnpadCustomerInfoInsertServ‎ice JnpadCustomerInfoInsertServ‎ice;
	
	
	//查询当前客户经理普查客户
		@ResponseBody
		@RequestMapping(value = "/ipad/pccustormer/selectPCustomer.json")
		@JRadOperation(JRadOperation.BROWSE)
		public String selectPCustomer(HttpServletRequest request) { 
			Map<String, Object> map=new HashMap<String, Object>();
			List<JnpadPcCustomer> list=new ArrayList<JnpadPcCustomer>();
			Integer a=0;
			String userId = request.getParameter("userId");
			List<JnpadPcCustomer> customer=CustomerService.findAllqyjl();
			if(customer!=null){
			for(int i=0;i<customer.size();i++){
				if(customer.get(i)!=null){
					if(customer.get(i).getId().equals(userId)){
						a=1;
					}
				}
			}}
			Integer c=0;
			if(a==1){
				List<JnpadPcCustomer> customerUser=CustomerService.findxzz(userId);
				for(int b=0;b<customerUser.size();b++){
					List<JnpadPcCustomer> result= CustomerService.selectPCustomer(customerUser.get(b).getId());
					if(result.size()!=0){
					for(int xx=0;xx<result.size();xx++){
						JnpadPcCustomer aaa=result.get(xx);
						if(list.size()!=0){
							list.add(c+xx,aaa);
						}else{
							list.add(xx,aaa);
						}
					}}
					c+=result.size();
				}
				map.put("result", list);
				map.put("size", list.size());
			}else{
				List<JnpadPcCustomer> result= CustomerService.selectPCustomer(userId);
				map.put("result", result);
				map.put("size", result.size());
			}
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
			JSONObject json = JSONObject.fromObject(map, jsonConfig);
			return json.toString();
		}
		
		//当前客户经理添加普查客户
				@ResponseBody
				@RequestMapping(value = "/ipad/pccustormer/insertPCustomer.json")
				@JRadOperation(JRadOperation.BROWSE)
				public String insertPCustomer(HttpServletRequest request) { 
					CustomerInfo customerinfor=new CustomerInfo();
					customerinfor.setUserId(request.getParameter("userId"));
					customerinfor.setCardId(request.getParameter("cardid"));
					customerinfor.setCardType("0");
					customerinfor.setChineseName(request.getParameter("name"));
					customerinfor.setCreatedBy(request.getParameter("userId"));
					JnpadPcCustomer customer =new JnpadPcCustomer();
					customer.setId(IDGenerator.generateID());
					customer.setUserid(request.getParameter("userId"));
					customer.setCardid(request.getParameter("cardid"));
					Date date=new Date();
					SimpleDateFormat sdfe=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String datetime=sdfe.format(date);
					customer.setCjsj(datetime);
					customer.setCsrq(request.getParameter("csrq"));
					customer.setDpdz(request.getParameter("dpdz"));
					customer.setHy(request.getParameter("hy"));
					customer.setName(request.getParameter("name"));
					customer.setSfzdz(request.getParameter("sfzdz"));
					JnpadPcCustomer name=CustomerService.selectCardId(request.getParameter("cardid"));
					Map<String, Object> map=new HashMap<String, Object>();
					if(name==null){
					int a=CustomerService.insertPCustomer(customer);
					if(a>0){
						String id =  JnpadCustomerInfoInsertServ‎ice.customerInforInsert(customerinfor);
						map.put("result", "1");
					}else{
						map.put("result", "0");
					}}else{
						map.put("result", "此客户已挂名在客户经理"+name.getName()+"的名下!!");
					}
					JsonConfig jsonConfig = new JsonConfig();
					jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
					JSONObject json = JSONObject.fromObject(map, jsonConfig);
					return json.toString();
				}
				
				//上传普查影像资料
				@ResponseBody
				@RequestMapping(value = "/ipad/pccustormer/imagepcImport.json")
				public Map<String, Object> imagepcImport(@RequestParam(value = "file", required = false) MultipartFile file,HttpServletRequest request,HttpServletResponse response) throws Exception {        
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
						String cardid = request.getParameter("cardid");
						String userId = request.getParameter("userId");
						CustomerService.importpcImage(file,cardid,userId,fileName);
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
				public static Integer i=0;
				public static List<JnpadPcCustomer> list=new ArrayList<JnpadPcCustomer>();
				//查看普查图片
				@ResponseBody
				@RequestMapping(value = "/ipad/pccustormer/ckpcImageById.json",method = { RequestMethod.GET })
				@JRadOperation(JRadOperation.EXPORT)
				public AbstractModelAndView ckpcImageById(HttpServletRequest request,HttpServletResponse response){
					try {
						String id =request.getParameter("id");
						String uri =request.getParameter("uri");
						String attment =request.getParameter("attment");
						String userId=request.getParameter("userId");
						//Integer a=Integer.parseInt(request.getParameter("i"));
						List list=new ArrayList();
						if(userId!=null){
							CustomerService.ckpcImage1(response,id);
						}else{
							/*//开启服务器
							if(id!=null && id.equals("0")){
								String c=CustomerService.kqstfp();
								System.out.println(c);
							}else{
							*/
								CustomerService.ckpcImage(response,uri,attment);
							}
								
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
				
				//查看普查图片
				@ResponseBody
				@RequestMapping(value = "/ipad/pccustormer/ckpcImage.json",method = { RequestMethod.GET })
				@JRadOperation(JRadOperation.EXPORT)
				public String ckpcImage(HttpServletRequest request) throws IOException, SftpException{
					Map<String, Object> map = new HashMap<String, Object>();
						String cardid =request.getParameter("cardid");
						List<LocalImageForm> result=CustomerService.selectUri(cardid);
						System.out.println(result.get(0).getUri());
						map.put("result", result);
						map.put("size", result.size());
						JsonConfig jsonConfig = new JsonConfig();
						jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
						JSONObject json = JSONObject.fromObject(map, jsonConfig);
						return json.toString();
				}
				/**
				 * 删除照片
				 * @param request
				 * @return
				 */
				@ResponseBody
				@RequestMapping(value = "/ipad/pccustormer/deleteImage.json",method = { RequestMethod.GET })
				@JRadOperation(JRadOperation.EXPORT)
				public String deleteImage(HttpServletRequest request){
					Map<String, Object> map = new HashMap<String, Object>();
						String id =request.getParameter("id");
						CustomerService.deleteId(id);
						map.put("result", "删除成功");
						JsonConfig jsonConfig = new JsonConfig();
						jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
						JSONObject json = JSONObject.fromObject(map, jsonConfig);
						return json.toString();
				}
				
				/**
				 * 查询当前客户经理部门人员
				 * @param request
				 * @return
				 */
				@ResponseBody
				@RequestMapping(value = "/ipad/pccustormer/selectBmRy.json",method = { RequestMethod.GET })
				@JRadOperation(JRadOperation.EXPORT)
				public String findSpRy(HttpServletRequest request){
					Map<String, Object> map = new HashMap<String, Object>();
						String userId =request.getParameter("userId");
						List<JnpadPcCustomer> result=CustomerService.findSpRy(userId);
						map.put("result", result);
						map.put("size", result.size());
						JsonConfig jsonConfig = new JsonConfig();
						jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
						JSONObject json = JSONObject.fromObject(map, jsonConfig);
						return json.toString();
				}
				
				/*@ResponseBody
				@RequestMapping(value = "/ipad/pccustormer/selectBmRy1.json",method = { RequestMethod.GET })
				@JRadOperation(JRadOperation.EXPORT)
				public String findSpRy(){
					Map<String, Object> map = new HashMap<String, Object>();
						List result=CustomerService.downloadjn11111();
						map.put("result", result);
						map.put("size", result.size());
						JsonConfig jsonConfig = new JsonConfig();
						jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
						JSONObject json = JSONObject.fromObject(map, jsonConfig);
						return json.toString();
				}*/
}
