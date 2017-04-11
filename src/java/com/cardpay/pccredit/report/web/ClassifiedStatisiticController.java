package com.cardpay.pccredit.report.web;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.report.filter.ClassfiedFilter;
import com.cardpay.pccredit.report.model.ClassifiedForm;
import com.cardpay.pccredit.report.service.ClassifiedStatisiticService;
import com.wicresoft.jrad.base.auth.JRadModule;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.result.JRadPagedQueryResult;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;

/**
 * 分类统计信用、抵押、担保类报表
 * @author sealy
 *
 */
@Controller
@RequestMapping("/report/classified/*")
@JRadModule("report.classified")
public class ClassifiedStatisiticController {

	@Autowired
	private ClassifiedStatisiticService classifiedstatisiticservice;
	
	/*
	 * 展示贷款统计报表
	 */
	@ResponseBody
	@RequestMapping(value = "browse.page", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView browse(@ModelAttribute ClassfiedFilter filter, HttpServletRequest request) {
		filter.setRequest(request);
		QueryResult<ClassifiedForm> result = classifiedstatisiticservice.findloansInfoByfilter(filter);
		JRadPagedQueryResult<ClassifiedForm> pagedResult = new JRadPagedQueryResult<ClassifiedForm>(
				filter, result);
		JRadModelAndView mv = new JRadModelAndView("/report/classifiedStatisitic/classifiedStatisitic", request);
		mv.addObject("result", pagedResult);
		return mv;
	}
	
	/*
	 * 导出贷款统计报表
	 */
	@ResponseBody
	@RequestMapping(value = "exportAll.page", method = { RequestMethod.GET })
	public void exportAll(@ModelAttribute ClassfiedFilter filter, HttpServletRequest request,HttpServletResponse response){
		filter.setRequest(request);
		String className= request.getParameter("className");
		QueryResult<ClassifiedForm> result = classifiedstatisiticservice.findlExcelDataoansInfoByfilter(filter);
		
		create(filter,result.getItems(),response,className);
	}
	
	public void create(ClassfiedFilter filter,List<ClassifiedForm> list,HttpServletResponse response,String className){
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet("贷款信息统计报表");  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        HSSFCell cellTmp = row.createCell((short) 0);
        String HeadName = "贷款信息统计报表";
        if(className!=""&&className!=null){
        	HeadName=className+"类贷款信息统计报表";
        }
		if(filter.getStartDate()!=""&&filter.getEndDate()!=""){
			HeadName = filter.getStartDate()+"---"+filter.getEndDate()+HeadName;
			}
		cellTmp.setCellValue(HeadName);  //设置表格标题
		
		// 设置标题字体
		HSSFFont font16 = wb.createFont();
		font16.setFontHeightInPoints((short) 20);
		font16.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font16.setFontName("华文楷体");
		
		// 设置标题字体
		HSSFFont font1 = wb.createFont();
		font1.setFontHeightInPoints((short) 12);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font1.setFontName("宋体");
		
		// 设置单元格居中
		HSSFCellStyle styleCenter = wb.createCellStyle();
		styleCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleCenter.setFont(font16);
		
		// 设置居右
		HSSFCellStyle styleFirst = wb.createCellStyle();
		styleFirst.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styleFirst.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		styleFirst.setFont(font1);
		// 设置居左
		HSSFCellStyle styleSecond = wb.createCellStyle();
		styleFirst.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styleFirst.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		styleFirst.setFont(font1);
		
		// 合并单元格
		CellRangeAddress region = new CellRangeAddress(0, 0, 0,10);
		sheet.addMergedRegion(region);
		cellTmp.setCellStyle(styleCenter);
		
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        // 创建一个居中格式
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setWrapText(true);
        
        // 设置第二行 制表日期
        row = sheet.createRow((int) 1);
        HSSFCell tmp = row.createCell((short) 8);
        tmp.setCellValue("制表日期："+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        CellRangeAddress reg = new CellRangeAddress(1, 1, 8,10);
        sheet.addMergedRegion(reg);
        tmp.setCellStyle(styleFirst);
        
		sheet.setColumnWidth(0, 3500);
		sheet.setColumnWidth(1, 4000);
		sheet.setColumnWidth(2, 6000);
		sheet.setColumnWidth(3, 5000);
		sheet.setColumnWidth(4, 5000);
		sheet.setColumnWidth(5, 5000);
		sheet.setColumnWidth(6, 3500);
		sheet.setColumnWidth(7, 3500);
		sheet.setColumnWidth(8, 3500);
		sheet.setColumnWidth(9, 5000);
		sheet.setColumnWidth(10, 5000);
		//==========================
        row = sheet.createRow((int) 2);
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("序号");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("客户名称");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("客户证件号码");
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
		cell.setCellValue("产品名称");
		cell.setCellStyle(style);
		cell = row.createCell((short) 4);
		cell.setCellValue("授信金额");
		cell.setCellStyle(style);
		cell = row.createCell((short) 5);
		cell.setCellValue("发放金额");
		cell.setCellStyle(style);
		cell = row.createCell((short) 6);
		cell.setCellValue("贷款余额");
		cell.setCellStyle(style);
		cell = row.createCell((short) 7);
		cell.setCellValue("利率");
		cell.setCellStyle(style);
		cell = row.createCell((short) 8);
		cell.setCellValue("账户状态");
		cell.setCellStyle(style);
		cell = row.createCell((short) 9);
		cell.setCellValue("放款时间");
		cell.setCellStyle(style);
		cell = row.createCell((short) 10);
		cell.setCellValue("所属客户经理");
		cell.setCellStyle(style);
		for(int i = 0; i < list.size(); i++){
			ClassifiedForm move = list.get(i);
			row = sheet.createRow((int) i+3);
			row.createCell((short) 0).setCellValue(i+1);
			row.createCell((short) 1).setCellValue(move.getChineseName());
			row.createCell((short) 2).setCellValue(move.getCardId());
			row.createCell((short) 3).setCellValue(move.getProductName());
			row.createCell((short) 4).setCellValue(move.getReqlmt());
			row.createCell((short) 5).setCellValue(move.getMoney());
			row.createCell((short) 6).setCellValue(move.getBalamt());
			row.createCell((short) 7).setCellValue(move.getInterest());
			row.createCell((short) 8).setCellValue(getSubjectInfo(move.getAccountstate()));
			row.createCell((short) 9).setCellValue(move.getLoandate());
			row.createCell((short) 10).setCellValue(move.getManagerName());
		}
		String fileName = "贷款信息统计报表";
		if(className!=""&&className!=null){
			fileName=className+"类贷款信息统计报表";
        }
		if(filter.getStartDate()!=""&&filter.getEndDate()!=""){
			fileName = filter.getStartDate()+"至"+filter.getEndDate()+fileName;
			}
		try{
			response.setHeader("Content-Disposition", "attachment;fileName="+new String(fileName.getBytes("gbk"),"iso8859-1")+".xls");
			response.setHeader("Connection", "close");
			response.setHeader("Content-Type", "application/vnd.ms-excel");
			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
public String getSubjectInfo(String code){
		
		String subject=null;
		if(code!=null){
		if(code.equals("1")){
			subject="正常";
		}else if(code.equals("2")){
			subject="逾放";
		}else if(code.equals("3")){
			subject="非应计";
		}else if(code.equals("5")){
			subject="结清";
		}else if(code.equals("6")){
			subject="部分逾期";
		}
		return subject;
		}else{
			return code;
		}
	}
}
