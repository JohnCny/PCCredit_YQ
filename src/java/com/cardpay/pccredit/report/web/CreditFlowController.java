package com.cardpay.pccredit.report.web;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.report.filter.CustomerMoveFilter;
import com.cardpay.pccredit.report.filter.ReportFilter;
import com.cardpay.pccredit.report.model.CustomerMoveForm;
import com.cardpay.pccredit.report.model.XdlctjbbForm;
import com.cardpay.pccredit.report.model.XdlctjbbForms;
import com.cardpay.pccredit.report.service.CustomerTransferFlowService;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.auth.JRadModule;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.controller.BaseController;
import com.wicresoft.jrad.base.web.result.JRadPagedQueryResult;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.util.spring.Beans;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;

@Controller
@RequestMapping("/credit/flow/*")
@JRadModule("credit.flow")
public class CreditFlowController extends BaseController{
	
	@Autowired
	private CustomerTransferFlowService customerTransferFlowService;
	
	/**
	 * 信贷流程统计查询
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "queryCreditFlow.page", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView queryExpireLoan(@ModelAttribute ReportFilter filter,HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/report/creditFlow/creditFlow", request);
		filter.setRequest(request);
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		String id = user.getId();
		Integer usertype=user.getUserType();
		if(usertype==1){
			filter.setUserId(id);
		}
		QueryResult<XdlctjbbForms> result =  customerTransferFlowService.findXdlctjbbFormList(filter);
		JRadPagedQueryResult<XdlctjbbForms> pagedResult = new JRadPagedQueryResult<XdlctjbbForms>(filter, result);
		mv.addObject(PAGED_RESULT, pagedResult);
		return mv;
	}
	
	
	/**
	 * 导出信贷流程统计
	 */
	@ResponseBody
	@RequestMapping(value = "exportAll.page", method = { RequestMethod.GET })
	public void exportAll(@ModelAttribute ReportFilter filter, HttpServletRequest request,HttpServletResponse response){
		filter.setRequest(request);
		List<XdlctjbbForms> list = customerTransferFlowService.getXdlctjbbFormList(filter);
		create(list,response);
	}
	
	public void create(List<XdlctjbbForms> list,HttpServletResponse response){
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("信贷流程统计报表");
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		
		sheet.setColumnWidth(0, 3500);
		sheet.setColumnWidth(1, 8000);
		sheet.setColumnWidth(2, 8000);
		sheet.setColumnWidth(3, 8000);
		sheet.setColumnWidth(4, 8000);
		sheet.setColumnWidth(5, 5000);
		sheet.setColumnWidth(6, 8000);
		sheet.setColumnWidth(7, 8000);
		sheet.setColumnWidth(8, 8000);
		sheet.setColumnWidth(9, 8000);
		sheet.setColumnWidth(10, 5000);
		sheet.setColumnWidth(11, 8000);
/*		sheet.setColumnWidth(12, 8000);
		sheet.setColumnWidth(13, 8000);
		sheet.setColumnWidth(14, 8000);*/
		//==========================
		HSSFRow row = sheet.createRow((int) 0);
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
		cell.setCellValue("所属产品");
		cell.setCellStyle(style);
		cell = row.createCell((short) 4);
		cell.setCellValue("申请金额");
		cell.setCellStyle(style);
		cell = row.createCell((short) 5);
		cell.setCellValue("申请日期");
		cell.setCellStyle(style);
		cell = row.createCell((short) 6);
		cell.setCellValue("审贷会日期");
		cell.setCellStyle(style);
		cell = row.createCell((short) 7);
		cell.setCellValue("贷款发放（拒绝）日");
		cell.setCellStyle(style);
		cell = row.createCell((short) 8);
		cell.setCellValue("总天数");
		cell.setCellStyle(style);
		cell = row.createCell((short) 9);
		cell.setCellValue("所属客户经理");
		cell.setCellStyle(style);
		cell = row.createCell((short) 10);
		cell.setCellValue("所属团队");
		cell.setCellStyle(style);
		cell = row.createCell((short) 11);
		cell.setCellValue("所属机构");
		cell.setCellStyle(style);
		
/*		cell = row.createCell((short) 11);
		cell.setCellValue("贷款发放（拒绝）日");
		cell.setCellStyle(style);
		cell = row.createCell((short) 12);
		cell.setCellValue("总天数");
		cell.setCellStyle(style);
		cell = row.createCell((short) 13);
		cell.setCellValue("所属客户经理");
		cell.setCellStyle(style);
		cell = row.createCell((short) 14);
		cell.setCellValue("所属机构");
		cell.setCellStyle(style);
		*/
		
		for(int i = 0; i < list.size(); i++){
			XdlctjbbForms move = list.get(i);
			row = sheet.createRow((int) i+1);
			row.createCell((short) 0).setCellValue(move.getRow_index());
			row.createCell((short) 1).setCellValue(move.getChinese_name());
			row.createCell((short) 2).setCellValue(move.getCard_id());
			row.createCell((short) 3).setCellValue(move.getPRODUCT_NAME());
			row.createCell((short) 4).setCellValue(move.getAPPLY_QUOTA());
			row.createCell((short) 5).setCellValue(move.getCREATED_TIME());
			row.createCell((short) 6).setCellValue(move.getStart_examine_time());
			row.createCell((short) 7).setCellValue(move.getJkrq());
			row.createCell((short) 8).setCellValue(move.getXdsj());
			row.createCell((short) 9).setCellValue(move.getDisplay_name());
			row.createCell((short) 10).setCellValue(move.getTeam1());
			row.createCell((short) 11).setCellValue(move.getTeamname());
		
		}
		String fileName = "信贷流程统计报表";
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
	
	
	
}


