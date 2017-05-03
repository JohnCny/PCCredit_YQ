package com.cardpay.pccredit.system.service;

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.postLoan.filter.PostLoanFilter;
import com.cardpay.pccredit.postLoan.model.MibusidataForm;
import com.cardpay.pccredit.system.dao.SysChargeDao;
import com.cardpay.pccredit.system.filter.SysLoginLogFilter;
import com.cardpay.pccredit.system.filter.SystemChargeFilter;
import com.cardpay.pccredit.system.model.Choujiang;
import com.cardpay.pccredit.system.model.SystemCharge;
import com.cardpay.pccredit.system.model.SystemUser;
import com.cardpay.pccredit.system.web.SystemChargeForm;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.modules.log.model.LoginLog;

@Service
public class SystemChargeService {
	
	@Autowired
	private SysChargeDao sysChargeDao;
	
	@Autowired
	private CommonDao commonDao;
	/**
	 * 过滤查询
	 * @param filter
	 * @return
	 */
	public QueryResult<SystemUser> findChargeByFilter(SystemChargeFilter filter) {
		List<SystemUser> users = sysChargeDao.findUserByFilter(filter);
		int size = sysChargeDao.findUserCountByFilter(filter);
		QueryResult<SystemUser> qs = new QueryResult<SystemUser>(size, users);
		return qs;
	}
	
	public void addCharge(SystemChargeForm systemChargeForm){
		String userIds = systemChargeForm.getUserIds();
		String[] userIdArr = userIds.split(",");
		for(String userId : userIdArr){
			SystemCharge systemCharge = new SystemCharge();
			systemCharge.setUserId(userId);
			systemCharge.setOrgId(systemChargeForm.getOrgId());
			systemCharge.setDeptId(systemChargeForm.getDeptId());
			
			//判断是否存在
			SystemCharge tmp = sysChargeDao.findCharge(systemCharge);
			if(tmp != null){
				continue;
			}
			commonDao.insertObject(systemCharge);
		}
	}
	
	public void deleteCharge(SystemChargeForm systemChargeForm){
		String userIds = systemChargeForm.getUserIds();
		SystemChargeFilter filter = new SystemChargeFilter();
		filter.setUserIds(userIds);
		filter.setDeptId(systemChargeForm.getDeptId());
		filter.setOrgId(systemChargeForm.getOrgId());
		List<SystemCharge> charges = sysChargeDao.findChargeByFilter(filter);
		for(SystemCharge obj : charges){
			commonDao.deleteObject(SystemCharge.class, obj.getId());
		}
	}
	
	/*
	 * 显示未抽奖成员
	 */
	public List<Choujiang> query(){
		List<Choujiang> list = commonDao.queryBySql(Choujiang.class, "select * From ty_tmp where result is null", null);
		return list;
	}
	/*
	 * 显示已抽奖成员
	 */
	public List<Choujiang> query1(){
		List<Choujiang> list = commonDao.queryBySql(Choujiang.class, "select * From ty_tmp where result is not null order by result", null);
		return list;
	}
	/*
	 * 保存结果
	 */
	public void save(String result,String level){
		String name = result.split("-")[0];
		String exentId = result.split("-")[1];
		commonDao.queryBySql(Choujiang.class, "update ty_tmp set result="+level+" where exent_id='"+exentId+"' and name='"+name+"'", null);
	}
	
	
	
	public QueryResult<LoginLog> findLoginLogByFilter(SysLoginLogFilter filter) {
		List<LoginLog> users = sysChargeDao.findLoginLogByFilter(filter);
		int size = sysChargeDao.findLoginLogCountByFilter(filter);
		QueryResult<LoginLog> qs = new QueryResult<LoginLog>(size, users);
		return qs;
	}
	
	
	public void getExportWageData(SysLoginLogFilter filter,HttpServletResponse response) throws Exception{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = format.format(new Date());
		
		String title ="";
	    title ="用户登录日志表";
		
		// 查询登录用户
	    List<LoginLog> list = sysChargeDao.findLoginLogList(filter);
		
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet("sheet1");  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        HSSFCell cellTmp = row.createCell((short) 0);
		cellTmp.setCellValue(title);  //设置表格标题 
		
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
		
		// 合并单元格
		CellRangeAddress region = new CellRangeAddress(0, 0, 0,6);
		sheet.addMergedRegion(region);
		cellTmp.setCellStyle(styleCenter);
		
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        // 创建一个居中格式
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setWrapText(true);
        style.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        // 设置第二行 制表日期
        row = sheet.createRow((int) 1);
        HSSFCell tmp = row.createCell((short) 4);
        tmp.setCellValue("制表日期："+dateString);
        CellRangeAddress reg = new CellRangeAddress(1, 1,4,6);
        sheet.addMergedRegion(reg);
        tmp.setCellStyle(styleFirst);
        
        
        // excel 正文内容
        row = sheet.createRow((int) 2);
        //row.s
        HSSFCell cell = row.createCell((short) 0);  
        cell.setCellValue("序号");  
        cell.setCellStyle(style);
        	        
        cell = row.createCell((short) 1);  
        cell.setCellValue("唯一标识ID");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(1, 10*256);
        
        cell = row.createCell((short) 2);  
        cell.setCellValue("登录名");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(2, 10*256);
        
        cell = row.createCell((short) 3);  
        cell.setCellValue("操作");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(3, 10*256);
        
        cell = row.createCell((short) 4);  
        cell.setCellValue("操作时间");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(4, 10*256);
        
        cell = row.createCell((short) 5);  
        cell.setCellValue("登录结果");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(5, 10*256);
        
        cell = row.createCell((short) 6);  
        cell.setCellValue("IP地址");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(6, 10*256);
        
        
        for(int i=0;i<list.size();i++){
        	row = sheet.createRow((int) i + 3);
        	LoginLog form = list.get(i);
        	row.createCell((short) 0).setCellValue(i+1);  
        	row.createCell((short) 1).setCellValue(form.getId());  
        	row.createCell((short) 2).setCellValue(form.getLogin());  
        	row.createCell((short) 3).setCellValue(form.getAction());                       
        	row.createCell((short) 4).setCellValue(format.format(form.getActionTime()));     						          
        	row.createCell((short) 5).setCellValue(form.getResult());
        	row.createCell((short) 6).setCellValue(form.getIpAddress());
        }
        
        title = title+".xls";
        response.setHeader("Connection", "close");
        response.setHeader("Content-Type", "application/vnd.ms-excel;charset=gbk");
        response.setHeader("Content-Disposition", "attachment;filename="
        + new String(title.getBytes("gbk"), "iso-8859-1"));
        OutputStream out = response.getOutputStream();  
        wb.write(out);
        out.close();
	}
}
