package com.cardpay.pccredit.jnpad.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.ipad.util.JsonDateValueProcessor;
import com.cardpay.pccredit.jnpad.model.LocationInfoForm;
import com.cardpay.pccredit.jnpad.model.ManagerInfoForm;
import com.cardpay.pccredit.jnpad.service.JnpadLocationInfoService;
import com.wicresoft.util.format.DateFormat;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 
 * 客户经理位置信息
 * @author sealy
 *
 */
@Controller
public class JnpadLocationInfoController {

	@Autowired
	private JnpadLocationInfoService jnpadLocationInfoService;


	/**
	 * 
	 * 更新客户经理位置信息
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = "/ipad/intopieces/updateLocation.json", method = { RequestMethod.GET })
	public String updateLocation(HttpServletRequest request){

		Map<String, Object> map = new LinkedHashMap<String, Object>();
		LocationInfoForm locationInfoForm =new LocationInfoForm();
		//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//		String date =sdf.format(new Date());
		locationInfoForm.setUpdateTime(new Date());
		locationInfoForm.setLatitude(request.getParameter("lat"));
		locationInfoForm.setLongitude(request.getParameter("lon"));
		String managerId = request.getParameter("userId");
		locationInfoForm .setUserId(managerId);
		//判断是否在早八点至晚八点之间
						String startTime="08:00:00";
						String endTime="20:00:00";
							boolean isRightTime = isInDate(startTime,endTime);
							if(!isRightTime){
								map.put("message","晚八点之后，早八点以前不能提交位置信息");
								JsonConfig jsonConfig = new JsonConfig();
								jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
								JSONObject json = JSONObject.fromObject(map, jsonConfig);
								return json.toString();
							}
						
		//查询表中是否有该客户经理的信息
		try {
			int num = jnpadLocationInfoService.managerCount(managerId);

			if(num!=0){
				LocationInfoForm lastLocationInfoForm = jnpadLocationInfoService.selectlastManagerLocationById(managerId);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date1 =sdf.format(new Date());
				String date2 = sdf.format(lastLocationInfoForm.getUpdateTime());
				Date currentDate = sdf.parse(date1);
				Date lastDate = sdf.parse(date2);
				long minutes = (currentDate.getTime()-lastDate.getTime())/(60*1000);
				if(minutes<60){
					
					map.put("message","更新位置信息间隔不能小于一小时");
					JsonConfig jsonConfig = new JsonConfig();
					jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
					JSONObject json = JSONObject.fromObject(map, jsonConfig);
					return json.toString();
				}
				}
			if(num<=10){
				//如果位置信息表中没客户经理信息  插入

				ManagerInfoForm managerInfoForm = jnpadLocationInfoService.selectManagerInforById(managerId);
				locationInfoForm.setUserName(managerInfoForm.getDISPLAY_NAME());
				jnpadLocationInfoService.insertManagerLocation(locationInfoForm);

				map.put("message","提交成功");


			}else{
				//更新位置信息
				jnpadLocationInfoService.updateManagerLocation(locationInfoForm);
				map.put("message","提交位置信息成功");
			}
		} catch (Exception e) {
			map.put("message", "提交位置信息失败");
		}
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(map, jsonConfig);
		return json.toString();
	}
/**
 * 查询位置信息
 * @param request
 * @return
 */
	@ResponseBody
	@RequestMapping(value = "/ipad/intopieces/selectLocation.json", method = { RequestMethod.GET })
	public String selectLocation(HttpServletRequest request){
		Map<String, Object> map = new LinkedHashMap<String, Object>();


		List<LocationInfoForm> locationInfoForm=jnpadLocationInfoService.selectManagerLocationById(request.getParameter("userId"));
		//判断是否查询到位置信息
		if(locationInfoForm.size()==0){
			map.put("success", "false");
		}else{
			map.put("success", "true");
		}
		map.put("result", locationInfoForm);
		map.put("size",locationInfoForm.size());
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(map, jsonConfig);
		return json.toString();
	}

	 /** 
	     * 判断时间是否在时间段内 
	     *  
	     * @param date 
	     *            当前时间 yyyy-MM-dd HH:mm:ss 
	     * @param strDateBegin 
	     *            开始时间 00:00:00 
	     * @param strDateEnd 
	     *            结束时间 00:05:00 
	     * @return 
	     */  
	    public  boolean isInDate(String strDateBegin,  
	            String strDateEnd) {  
	    	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	//    	 String strDate = "2016-12-22 20:01:59" ; 
	         String strDate = sdf.format(new Date());  
	         // 截取当前时间时分秒  
	         int strDateH = Integer.parseInt(strDate.substring(11, 13));  
	         int strDateM = Integer.parseInt(strDate.substring(14, 16));  
	         int strDateS = Integer.parseInt(strDate.substring(17, 19));  
	         // 截取开始时间时分秒  
	         int strDateBeginH = Integer.parseInt(strDateBegin.substring(0, 2));  
	         int strDateBeginM = Integer.parseInt(strDateBegin.substring(3, 5));  
	         int strDateBeginS = Integer.parseInt(strDateBegin.substring(6, 8));  
	         // 截取结束时间时分秒  
	         int strDateEndH = Integer.parseInt(strDateEnd.substring(0, 2));  
	         int strDateEndM = Integer.parseInt(strDateEnd.substring(3, 5));  
	         int strDateEndS = Integer.parseInt(strDateEnd.substring(6, 8));  
	         
	   
	         if ((strDateH >= strDateBeginH && strDateH <= strDateEndH)) {  
	             // 当前时间小时数在开始时间和结束时间小时数之间  
	             if (strDateH > strDateBeginH && strDateH < strDateEndH) {  
	                 return true;  
	                 // 当前时间小时数等于开始时间小时数等于结束小时数，分钟数在开始和结束之间  
	             } else if (strDateH == strDateBeginH &&strDateH==strDateEndH&& strDateM >= strDateBeginM  
	                     && strDateM <= strDateEndM) {  
	                 return true;  
	                 // 当前时间小时数等于开始时间小时数，分钟数等于开始时间分钟数，秒数在开始和结束之间  
	             } else if (strDateH == strDateBeginH && strDateM == strDateBeginM  
	                     && strDateS >= strDateBeginS && strDateS <= strDateEndS) {  
	                 return true;  
	             }  
	             // 当前时间小时数大等于开始时间小时数，等于结束时间小时数，分钟数小等于结束时间分钟数  
	             else if (strDateH >= strDateBeginH && strDateH == strDateEndH  
	                     && strDateM <= strDateEndM) {  
	                 return true;  
	                 // 当前时间小时数大等于开始时间小时数，等于结束时间小时数，分钟数等于结束时间分钟数，秒数小等于结束时间秒数  
	             } else if (strDateH >= strDateBeginH && strDateH == strDateEndH  
	                     && strDateM == strDateEndM && strDateS <= strDateEndS) {  
	                 return true;  
	              // 当前时间小时数等于开始时间小时数，分钟数大于开始
	             }else if(strDateH == strDateBeginH && strDateM >= strDateBeginM){
	             	return true;
	             }else {  
	             
	             	System.out.println("1");
	                 return false;  
	             }  
	         } else {  
	         	System.out.println("2");
	            return false;  
	         }    
	    }  
}
