package com.cardpay.pccredit.jnpad.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.intopieces.model.LocalExcel;
import com.cardpay.pccredit.intopieces.service.AddIntoPiecesService;
import com.cardpay.pccredit.ipad.util.JsonDateValueProcessor;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.util.web.RequestHelper;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@Controller
public class JnpadInquiryModelController {

	@Autowired
	private AddIntoPiecesService addIntoPiecesService;
	
	
	@ResponseBody
	@RequestMapping(value = "/ipad/product/browerModel.json")
	public String browerModel(HttpServletRequest request) {
		Map<Object,Object> map =new LinkedHashMap<Object, Object>();
		String appId = RequestHelper.getStringValue(request, "appId");
		if (StringUtils.isNotEmpty(appId)) {
			try {
				
			
			LocalExcel localExcel = addIntoPiecesService.findLocalEXcelByApplication(appId);
			String tableContentjyb = getFromBASE64(localExcel.getSheetJy()).replaceAll("\n", "<br>").replace("><br><", "><");
			String tableContentjbzkb = getFromBASE64(localExcel.getSheetJbzk()).replaceAll("\n", "<br>").replace("><br><", "><");
			String tableContentzcfzb = getFromBASE64(localExcel.getSheetFz()).replaceAll("\n", "<br>").replace("><br><", "><");
			String tableContentbzlrb = getFromBASE64(localExcel.getSheetBzlrb()).replaceAll("\n", "<br>").replace("><br><", "><");
			String tableContentxjlb = getFromBASE64(localExcel.getSheetXjllb()).replaceAll("\n", "<br>").replace("><br><", "><");
			String tableContentxjXb = getFromBASE64(localExcel.getSheetJc()).replaceAll("\n", "<br>").replace("><br><", "><");
			String tableContentxgzb = getFromBASE64(localExcel.getSheetGdzc()).replaceAll("\n", "<br>").replace("><br><", "><");
			String tableContentyfysb = getFromBASE64(localExcel.getSheetYfys()).replaceAll("\n", "<br>").replace("><br><", "><");
			String tableContentysyfb = getFromBASE64(localExcel.getSheetYsyf()).replaceAll("\n", "<br>").replace("><br><", "><");
			String tableContentjueyb = getFromBASE64(localExcel.getJyb()).replaceAll("\n", "<br>").replace("><br><", "><");
			
			
			String tableContentjyztb=getFromBASE64(localExcel.getSheetJyzt()).replaceAll("\n", "<br>").replace("><br><", "><");
			String tableContentscztb=getFromBASE64(localExcel.getSheetSczt()).replaceAll("\n", "<br>").replace("><br><", "><");
			String tableContentddpzb=getFromBASE64(localExcel.getSheetDdpz()).replaceAll("\n", "<br>").replace("><br><", "><");
			String tableContentlrjbb=getFromBASE64(localExcel.getSheetLrjb()).replaceAll("\n", "<br>").replace("><br><", "><");
			String tableContentzyywb=getFromBASE64(localExcel.getZyyw()).replaceAll("\n", "<br>").replace("><br><", "><");
			String tableContentdhdb=getFromBASE64(localExcel.getSheetDhd()).replaceAll("\n", "<br>").replace("><br><", "><");
			String tableContentlsb=getFromBASE64(localExcel.getSheetLsfx()).replaceAll("\n", "<br>").replace("><br><", "><");
			String tableContentbzrb=getFromBASE64(localExcel.getSheetdbrxx()).replaceAll("\n", "<br>").replace("><br><", "><");
			
			map.put("tableContentjyztb", tableContentjyztb);
			map.put("tableContentscztb", tableContentscztb);
			map.put("tableContentddpzb", tableContentddpzb);
			map.put("tableContentlrjbb", tableContentlrjbb);
			map.put("tableContentzyywb", tableContentzyywb);
			map.put("tableContentdhdb", tableContentdhdb);
			map.put("tableContentlsb", tableContentlsb);
			map.put("tableContentbzrb", tableContentbzrb);
			
			
			
			
			map.put("tableContentjyb", tableContentjyb);
			map.put("tableContentjbzkb", tableContentjbzkb);
			map.put("tableContentzcfzb", tableContentzcfzb);
			map.put("tableContentbzlrb", tableContentbzlrb);
			map.put("tableContentxjlb", tableContentxjlb);
			map.put("tableContentxjXb", tableContentxjXb);
			map.put("tableContentxgzb", tableContentxgzb);
			map.put("tableContentyfysb", tableContentyfysb);
			map.put("tableContentysyfb", tableContentysyfb);
			map.put("tableContentjueyb", tableContentjueyb);
			} catch (Exception e) {
				map.put("mess", e.getMessage());
			}
		}
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		JSONObject json = JSONObject.fromObject(map, jsonConfig);
		return json.toString();
	}
	
	//base64解码
		public static String getFromBASE64(String s) { 
	    	if (s == null) return null; 
	    	BASE64Decoder decoder = new BASE64Decoder(); 
	    	try { 
	    	byte[] b = decoder.decodeBuffer(s); 
	    	return new String(b); 
	    	} catch (Exception e) { 
	    	return null; 
	    	} 
		} 
		
		
		@ResponseBody
		@RequestMapping(value = "/ipad/product/downLoadYxzlJnaaaaa.json",method = { RequestMethod.GET })
		public String downLoadYxzlJnaaaaa(HttpServletRequest request,HttpServletResponse response){
			 String base64String = getPDFBinary(new File("C:/Users/maoya/Desktop/转正申请/aaaaa.pdf"));
			 String aaa=getFromBASE64(base64String);
				Map<Object,Object> map =new LinkedHashMap<Object, Object>();
				map.put("aaa", base64String);
				JsonConfig jsonConfig = new JsonConfig();
				jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
				JSONObject json = JSONObject.fromObject(map, jsonConfig);
				return json.toString();
		}
		 static String getPDFBinary(File file) {  
			 BASE64Encoder encoder = new sun.misc.BASE64Encoder();      
	         BASE64Decoder decoder = new sun.misc.BASE64Decoder();      
		        FileInputStream fin =null;  
		        BufferedInputStream bin =null;  
		        ByteArrayOutputStream baos = null;  
		        BufferedOutputStream bout =null;  
		        try {  
		            //建立读取文件的文件输出流  
		            fin = new FileInputStream(file);  
		            //在文件输出流上安装节点流（更大效率读取）  
		            bin = new BufferedInputStream(fin);  
		            // 创建一个新的 byte 数组输出流，它具有指定大小的缓冲区容量  
		            baos = new ByteArrayOutputStream();  
		            //创建一个新的缓冲输出流，以将数据写入指定的底层输出流  
		            bout = new BufferedOutputStream(baos);  
		            byte[] buffer = new byte[1024];  
		            int len = bin.read(buffer);  
		            while(len != -1){  
		                bout.write(buffer, 0, len);  
		                len = bin.read(buffer);  
		            }  
		            //刷新此输出流并强制写出所有缓冲的输出字节，必须这行代码，否则有可能有问题  
		            bout.flush();  
		             byte[] bytes = baos.toByteArray();  
		             //sun公司的API  
		             return encoder.encodeBuffer(bytes).trim();    
		             //apache公司的API  
		             //return Base64.encodeBase64String(bytes);  
		              
		        } catch (FileNotFoundException e) {  
		            e.printStackTrace();  
		        } catch (IOException e) {  
		            e.printStackTrace();  
		        }finally{  
		            try {  
		                fin.close();  
		                bin.close();  
		                //关闭 ByteArrayOutputStream 无效。此类中的方法在关闭此流后仍可被调用，而不会产生任何 IOException  
		                //baos.close();  
		                bout.close();  
		            } catch (IOException e) {  
		                e.printStackTrace();  
		            }  
		        }  
		        return null;  
		    }  

}
