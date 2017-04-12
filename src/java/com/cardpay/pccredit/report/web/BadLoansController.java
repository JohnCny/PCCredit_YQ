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

import com.cardpay.pccredit.report.filter.BadLoansFilter;
import com.cardpay.pccredit.report.model.BadLoansInfo;
import com.cardpay.pccredit.report.service.BadLoansService;
import com.wicresoft.jrad.base.auth.JRadModule;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.controller.BaseController;
import com.wicresoft.jrad.base.web.result.JRadPagedQueryResult;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;

/**
 * 不良贷款报表
 * @author sealy
 *
 */
@Controller
@RequestMapping("/report/badloans/*")
@JRadModule("report.badloans")
public class BadLoansController extends BaseController{

	@Autowired
	private BadLoansService badloansservice;
	
	/*
	 * 展示不良信息
	 */
	@ResponseBody
	@RequestMapping(value = "browse.page", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView browse(@ModelAttribute BadLoansFilter filter, HttpServletRequest request) {
		filter.setRequest(request);
		QueryResult<BadLoansInfo> result = badloansservice.findBadloansByfilter(filter);
		JRadPagedQueryResult<BadLoansInfo> pagedResult = new JRadPagedQueryResult<BadLoansInfo>(
				filter, result);
		JRadModelAndView mv = new JRadModelAndView("/report/badloans/badloans", request);
		mv.addObject(PAGED_RESULT, pagedResult);
		return mv;
	}
	
	/*
	 * 导出不良excel
	 */
	@ResponseBody
	@RequestMapping(value = "exportAll.page", method = { RequestMethod.GET })
	public void exportAll(@ModelAttribute BadLoansFilter filter, HttpServletRequest request,HttpServletResponse response){
		filter.setRequest(request);
		QueryResult<BadLoansInfo> result = badloansservice.findBadloansDataByfilter(filter);
		
		create(filter,result.getItems(),response);
	}
	
	public void create(BadLoansFilter filter,List<BadLoansInfo> list,HttpServletResponse response){
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet("不良贷款统计报表");  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        HSSFCell cellTmp = row.createCell((short) 0);
        String HeadName = "不良贷款统计报表";
		if(filter.getStartDate()!=""&&filter.getEndDate()!=""){
			HeadName = filter.getStartDate()+"至"+filter.getEndDate()+"不良贷款统计报表";
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
		// 设置居右
		HSSFCellStyle styleSecond = wb.createCellStyle();
		styleFirst.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styleFirst.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		styleFirst.setFont(font1);
		
		// 合并单元格
		CellRangeAddress region = new CellRangeAddress(0, 0, 0,9);
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
        HSSFCell tmp = row.createCell((short) 7);
        tmp.setCellValue("制表日期："+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        CellRangeAddress reg = new CellRangeAddress(1, 1, 7,9);
        sheet.addMergedRegion(reg);
        tmp.setCellStyle(styleFirst);
        
		sheet.setColumnWidth(0, 3500);
		sheet.setColumnWidth(1, 4000);
		sheet.setColumnWidth(2, 6000);
		sheet.setColumnWidth(3, 5000);
		sheet.setColumnWidth(4, 10000);
		sheet.setColumnWidth(5, 10000);
		sheet.setColumnWidth(6, 3500);
		sheet.setColumnWidth(7, 3500);
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
		cell.setCellValue("涉及金额");
		cell.setCellStyle(style);
		cell = row.createCell((short) 4);
		cell.setCellValue("不良记录主题");
		cell.setCellStyle(style);
		cell = row.createCell((short) 5);
		cell.setCellValue("不良记录描述");
		cell.setCellStyle(style);
		cell = row.createCell((short) 6);
		cell.setCellValue("不良记录日期");
		cell.setCellStyle(style);
		cell = row.createCell((short) 7);
		cell.setCellValue("所属客户经理");
		cell.setCellStyle(style);
		cell = row.createCell((short) 8);
		cell.setCellValue("建立人");
		cell.setCellStyle(style);
		cell = row.createCell((short) 9);
		cell.setCellValue("建立时间");
		for(int i = 0; i < list.size(); i++){
			BadLoansInfo move = list.get(i);
			row = sheet.createRow((int) i+3);
			row.createCell((short) 0).setCellValue(i+1);
			row.createCell((short) 1).setCellValue(move.getChinesename());
			row.createCell((short) 2).setCellValue(move.getCardid());
			row.createCell((short) 3).setCellValue(move.getReferaomunt());
			row.createCell((short) 4).setCellValue(getSubjectInfo(move.getSubject()));
			row.createCell((short) 5).setCellValue(move.getBaddescription());
			row.createCell((short) 6).setCellValue(move.getRecorddate());
			row.createCell((short) 7).setCellValue(move.getManagername());
			row.createCell((short) 8).setCellValue(move.getOpertor());
			row.createCell((short) 9).setCellValue(move.getOperDateTime());
		}
		String fileName = "不良贷款统计报表";
		if(filter.getStartDate()!=""&&filter.getEndDate()!=""){
			fileName = filter.getStartDate()+"---"+filter.getEndDate()+"不良贷款统计报表";
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
		if(code.equals("01")){
			subject="借款人被起诉";
		}else if(code.equals("02")){
			subject="企业逃废债务";
		}else if(code.equals("03")){
			subject="借款人提供虚假资料";
		}else if(code.equals("04")){
			subject="贷款逾期欠息";
		}else if(code.equals("05")){
			subject="不良对外担保记录";
		}else if(code.equals("06")){
			subject="用途违约";
		}else if(code.equals("07")){
			subject="其他情况";
		}
		return subject;
		}else{
			return code;
		}
	}
}
