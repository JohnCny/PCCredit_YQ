package com.cardpay.pccredit.jxlx.service;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.dateplan.model.JBUser;
import com.cardpay.pccredit.jnpad.dao.JnIpadUserLoginDao;
import com.cardpay.pccredit.jxlx.dao.CustomerCoefficientDao;
import com.cardpay.pccredit.jxlx.model.COEFFICIENT;
import com.cardpay.pccredit.jxlx.model.SPLITOFINTEREST;
import com.cardpay.pccredit.manager.form.ManagerPerformmanceForm;
import com.wicresoft.jrad.base.database.model.QueryResult;

@Service
public class CustomerCoefficientService {
	@Autowired
	private CustomerCoefficientDao CoefficientDao;
	
	public void updateCoefficient(COEFFICIENT COEFFICIENT){
		CoefficientDao.updateCoefficient(COEFFICIENT);
	}
	public QueryResult<COEFFICIENT>selectallCOEFFICIENT(COEFFICIENT COEFFICIENT){
		List<COEFFICIENT>list =CoefficientDao.selectallCOEFFICIENT(COEFFICIENT);
		int count =CoefficientDao.selectallCOEFFICIENTcount(COEFFICIENT);
		QueryResult<COEFFICIENT> qs = new QueryResult<COEFFICIENT>(count, list);
		return qs;
	}
	public List<COEFFICIENT>selectallOrgteam(){
		return CoefficientDao.selectallOrgteam();
	}
		public List<COEFFICIENT>selectallTeam(){
			return CoefficientDao.selectallTeam();
		}
		public COEFFICIENT selectallCOEFFICIENTByUserId(@Param("userid")String userid){
			return CoefficientDao.selectallCOEFFICIENTByUserId(userid);
		}
		public List<SPLITOFINTEREST>selectSpliByTeam(SPLITOFINTEREST SPLITOFINTEREST){
			return CoefficientDao.selectSpliByTeam(SPLITOFINTEREST);
		}
		public SPLITOFINTEREST selectSumSpliByTeam(SPLITOFINTEREST SPLITOFINTEREST){
			return CoefficientDao.selectSumSpliByTeam(SPLITOFINTEREST);
		}
		
		
		
		
		//导出利息分拆总览
		public void getExportWageData(List<SPLITOFINTEREST> gxperformList, SPLITOFINTEREST sp1,HttpServletResponse response) {
			// 第一步，创建一个webbook，对应一个Excel文件  
	        HSSFWorkbook wb = new HSSFWorkbook();  
	        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
	        HSSFSheet sheet = wb.createSheet("sheet1");  
	        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
	        HSSFRow row = sheet.createRow((int) 0);  
	        HSSFCell cellTmp = row.createCell((short) 0);
	        String titles=sp1.getYear()+sp1.getMonth()+"阳泉农村信用社联社客户经理每月利息分拆报表";
	        cellTmp.setCellValue(titles);  //设置表格标题
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
			CellRangeAddress region = new CellRangeAddress(0, 0, 0,14);
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
	        HSSFCell tmp = row.createCell((short) 11);
	        tmp.setCellValue("制表日期："+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
	        CellRangeAddress reg = new CellRangeAddress(1, 1, 12,14);
	        sheet.addMergedRegion(reg);
	        tmp.setCellStyle(styleSecond);
	        
	        // excel 正文内容
	        row = sheet.createRow((int) 2);
	        HSSFCell cell = row.createCell((short) 0);  
	        cell.setCellValue("区域");  
	        cell.setCellStyle(style);
	        	        
	        cell = row.createCell((short) 1);  
	        cell.setCellValue("团队");  
	        cell.setCellStyle(style);  
	        
	        cell = row.createCell((short) 2);  
	        cell.setCellValue("客户经理");  
	        cell.setCellStyle(style);
	        
	        cell = row.createCell((short) 3);  
	        cell.setCellValue("融资成本");  
	        cell.setCellStyle(style);
	        
	        cell = row.createCell((short) 4);  
	        cell.setCellValue("拨备");  
	        cell.setCellStyle(style);
	        
	        cell = row.createCell((short) 5);  
	        cell.setCellValue("纯利润");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(4, 10*256);
	       

	        for(int i=0;i<gxperformList.size();i++){
	        	row = sheet.createRow((int) i + 3);
	        	SPLITOFINTEREST gxperform = gxperformList.get(i);
	        	row.createCell((short) 0).setCellValue((String) gxperform.getOrgteam());            //管辖行
	        	row.createCell((short) 1).setCellValue((String) gxperform.getTeam()); 
	        	row.createCell((short) 2).setCellValue((String) gxperform.getName());//姓名                       
	        	row.createCell((short) 3).setCellValue(gxperform.getCost());     	//拜访数       
	        	row.createCell((short) 4).setCellValue(gxperform.getProvision());      //申请数
	        	row.createCell((short) 5).setCellValue(gxperform.getNetprofit());    //申请拒绝数 
	        									          
	        }
	        try {
	        response.setHeader("Connection", "close");
	        response.setHeader("Content-Type", "application/vnd.ms-excel;charset=GBK");
	        response.setHeader("Content-Disposition", "attachment;filename="
	   + new String("阳泉农村信用社联社客户经理每月利息拆分报表.xls".getBytes(), "iso-8859-1"));
	        OutputStream out = response.getOutputStream();  
			        wb.write(out);     
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


}
