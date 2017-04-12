package com.cardpay.pccredit.report.service;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.customer.constant.CommonConstant;
import com.cardpay.pccredit.manager.filter.ManagerSalaryFilter;
import com.cardpay.pccredit.manager.model.AccountManagerParameter;
import com.cardpay.pccredit.manager.model.ManagerSalaryForm;
import com.cardpay.pccredit.manager.model.TPerformanceParameters;
import com.cardpay.pccredit.manager.web.AccountManagerParameterForm;
import com.cardpay.pccredit.manager.web.ManagerBelongMapForm;
import com.cardpay.pccredit.postLoan.filter.PostLoanFilter;
import com.cardpay.pccredit.postLoan.model.ContractForm;
import com.cardpay.pccredit.postLoan.model.MibusidataForm;
import com.cardpay.pccredit.report.dao.CustomerTransferFlowDao;
import com.cardpay.pccredit.report.filter.AccLoanCollectFilter;
import com.cardpay.pccredit.report.filter.CustomerMoveFilter;
import com.cardpay.pccredit.report.filter.ReportFilter;
import com.cardpay.pccredit.report.model.AccLoanCollectInfo;
import com.cardpay.pccredit.report.model.BjjdktjbbForm;
import com.cardpay.pccredit.report.model.CustomerMove;
import com.cardpay.pccredit.report.model.CustomerMoveForm;
import com.cardpay.pccredit.report.model.DkyetjbbForm;
import com.cardpay.pccredit.report.model.DqzzdktjbbForm;
import com.cardpay.pccredit.report.model.XdlctjbbForm;
import com.cardpay.pccredit.report.model.XdlctjbbForms;
import com.cardpay.pccredit.report.model.YffdktjbbForm;
import com.cardpay.pccredit.report.model.YqdktjbbForm;
import com.cardpay.pccredit.report.model.YqhkdktjbbForm;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.modules.privilege.model.User;

@Service
public class CustomerTransferFlowService {

	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private CustomerTransferFlowDao customerTransferFlowDao;
	/**
	 * 客户经理移交流水查询
	 * @param filter
	 * @return
	 */
	public QueryResult<CustomerMoveForm> findCustomerMoveList(CustomerMoveFilter filter){
		List<CustomerMoveForm> list = customerTransferFlowDao.findCustomerTransferList(filter);
		int size = customerTransferFlowDao.findCustomerMoveCountList(filter);
		QueryResult<CustomerMoveForm> result = new QueryResult<CustomerMoveForm>(size,list);
		return result;
	} 
	
	public List<CustomerMoveForm> getCustomerMovelList(CustomerMoveFilter filter){
		return customerTransferFlowDao.getCustomerMovelList(filter);
	}
	
	/**
	 *  已发放贷款统计
	 */
	public QueryResult<YffdktjbbForm> findYffdktjbbFormList(ReportFilter filter){
		List<YffdktjbbForm> list = customerTransferFlowDao.findYffdktjbbFormList(filter);
		int size = customerTransferFlowDao.findYffdktjbbFormCountList(filter);
		QueryResult<YffdktjbbForm> result = new QueryResult<YffdktjbbForm>(size,list);
		return result;
	} 
	
	
	public List<YffdktjbbForm> getYffdktjbbFormlList(ReportFilter filter){
		return customerTransferFlowDao.getYffdktjbbFormlList(filter);
	}
	/**
	 *	被拒绝贷款统计
	 */
	public QueryResult<BjjdktjbbForm> findbjjdktjbbFormList(ReportFilter filter){
		List<BjjdktjbbForm> list = customerTransferFlowDao.findbjjdktjbbFormList(filter);
		int size = customerTransferFlowDao.findbjjdktjbbFormCountList(filter);
		QueryResult<BjjdktjbbForm> result = new QueryResult<BjjdktjbbForm>(size,list);
		return result;
	} 
	
	public List<BjjdktjbbForm> getbjjdktjbbFormList(ReportFilter filter){
		return customerTransferFlowDao.getbjjdktjbbFormList(filter);
	}
	
	/**
	 *	到期终止贷款统计
	 */
	public QueryResult<DqzzdktjbbForm> findDqzzdktjbbFormList(ReportFilter filter){
		List<DqzzdktjbbForm> list = customerTransferFlowDao.findDqzzdktjbbFormList(filter);
		int size = customerTransferFlowDao.findDqzzdktjbbFormCountList(filter);
		QueryResult<DqzzdktjbbForm> result = new QueryResult<DqzzdktjbbForm>(size,list);
		return result;
	} 
	
	public List<DqzzdktjbbForm> getDqzzdktjbbFormList(ReportFilter filter){
		return customerTransferFlowDao.getDqzzdktjbbFormList(filter);
	}
	
	
	/**
	 *	贷款余额统计
	 */
	public QueryResult<DkyetjbbForm> findDkyetjbbFormList(ReportFilter filter){
		List<DkyetjbbForm> list = customerTransferFlowDao.findDkyetjbbFormList(filter);
		int size = customerTransferFlowDao.findDkyetjbbFormCountList(filter);
		QueryResult<DkyetjbbForm> result = new QueryResult<DkyetjbbForm>(size,list);
		return result;
	} 
	
	public List<DkyetjbbForm> getDkyetjbbFormList(ReportFilter filter){
		return customerTransferFlowDao.getDkyetjbbFormList(filter);
	}
	
	/**
	 * 逾期贷款统计
	 */
	public QueryResult<YqdktjbbForm> findYqdktjbbFormList(ReportFilter filter){
		List<YqdktjbbForm> list = customerTransferFlowDao.findYqdktjbbFormList(filter);
		int size = customerTransferFlowDao.findYqdktjbbFormCountList(filter);
		QueryResult<YqdktjbbForm> result = new QueryResult<YqdktjbbForm>(size,list);
		return result;
	} 
	
	public List<YqdktjbbForm> getYqdktjbbFormList(ReportFilter filter){
		return customerTransferFlowDao.getYqdktjbbFormList(filter);
	}
	
	/**
	 *  预期还款贷款统计
	 */
	public QueryResult<YqhkdktjbbForm> findYqhkdktjbbFormList(ReportFilter filter){
		List<YqhkdktjbbForm> list = customerTransferFlowDao.findYqhkdktjbbFormList(filter);
		int size = customerTransferFlowDao.findYqhkdktjbbFormCountList(filter);
		QueryResult<YqhkdktjbbForm> result = new QueryResult<YqhkdktjbbForm>(size,list);
		return result;
	} 
	
	public List<YqhkdktjbbForm> getYqhkdktjbbFormList(ReportFilter filter){
		return customerTransferFlowDao.getYqhkdktjbbFormList(filter);
	}
	
	
	/**
	 *	信贷流程统计
	 */
	public QueryResult<XdlctjbbForms> findXdlctjbbFormList(ReportFilter filter){
		List<XdlctjbbForms> list = customerTransferFlowDao.findXdlctjbbFormList(filter);
		int size = customerTransferFlowDao.findXdlctjbbFormCountList(filter);
		QueryResult<XdlctjbbForms> result = new QueryResult<XdlctjbbForms>(size,list);
		return result;
	} 
	
	public List<XdlctjbbForms> getXdlctjbbFormList(ReportFilter filter){
		return customerTransferFlowDao.getXdlctjbbFormList(filter);
	}
	
	/**
	 * 贷款汇总统计
	 */
	public List<AccLoanCollectInfo> getAccLoanCollect(AccLoanCollectFilter filter){
		return customerTransferFlowDao.getAccLoanCollect(filter);
	}
	
	public List<AccountManagerParameter> findManager(String userId){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		String sql = "select * from account_manager_parameter where user_id = #{userId}";
		List<AccountManagerParameter> list = commonDao.queryBySql(AccountManagerParameter.class, sql, params);
		return list;
	}
	
	
	/**
	 * 查询 post流水贷产品的贷款余额
	 */
	public BigDecimal  findSubListManagerByManagerId(IUser user){
		Map<String, Object> params = new HashMap<String, Object>();
		String userId ="";
		if(CommonConstant.USER_TYPE.USER_TYPE_1 == user.getUserType()){
			userId = user.getId();
		}
		params.put("userId", userId);
		
	   StringBuffer sql = new StringBuffer("  select nvl(sum(nvl(t.BALAMT, 0)), 0) as   pslsd  "+
			   "      from t_mibusidata_view t                                                 "+
			   "     where t.CUSTID  in (select distinct(cust.ty_customer_id) as user_id from   "+
			   "  customer_application_info info,                                              "+
			   "  basic_customer_information cust,                                             "+
			   "  product_attribute prod ,                                                     "+
			   "  sys_user u                                                                   "+
			   "  where info.customer_id = cust.id                                             "+
			   "    and info.product_id = prod.id                                              "+
			   "    and u.id = cust.user_id                                                    "+
			   "    and prod.product_name ='POS流水贷'                                           ");
	   
	    if(StringUtils.trimToNull(userId)!=null){
			sql.append(" and cust.user_id =#{userId}");
		}
	    sql.append(" ) ");
		List<AccLoanCollectInfo> list = commonDao.queryBySql(AccLoanCollectInfo.class, sql.toString(), params);
		
		if(list!=null&&list.size()>0){
			return list.get(0).getPslsd();
		}
		return null;
	}
	
	
	public int findManagerListCount(){
		// 查询客户经理
		List<AccountManagerParameter> list = commonDao.queryBySql(AccountManagerParameter.class,
						"select * from account_manager_parameter where manager_type in ('1','2')",null);
		return list.size();
	}
	
	
	
	/**
	 * 查询当月日均用信
	 */
	public BigDecimal  findMonthAverageAmt(){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = format.format(new Date());
		String year  = dateString.substring(0, 4);
	    String month = dateString.substring(5, 7);
		
	    String sql = "select nvl(sum(t.month_day_average_cust_loanamt),0) as monthaverageamt from t_statistics_attentive_balance t  "
	    		+ "where year ='"+year+"' and month ='"+month+"' ";
	   
		List<AccLoanCollectInfo> list = commonDao.queryBySql(AccLoanCollectInfo.class, sql, null);
		
		if(list!=null&&list.size()>0){
			return list.get(0).getMonthaverageamt();
		}
		return null;
	}
	
	
	/**
	 * 累计到今日的月度日均用信
	 */
	public BigDecimal  findToalMonthAverageAmt(){
		
	    String sql = "select nvl(sum(t.month_day_average_cust_loanamt),0) as totalmonthaverageamt from t_statistics_attentive_balance t  ";
	   
		List<AccLoanCollectInfo> list = commonDao.queryBySql(AccLoanCollectInfo.class, sql, null);
		
		if(list!=null&&list.size()>0){
			return list.get(0).getTotalmonthaverageamt();
		}
		return null;
	}
	
	
	
	public void getExportWageData(PostLoanFilter filter,HttpServletResponse response) throws Exception{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = format.format(new Date());
		
		String title ="";
		String startDate ="2016-05-01";
	    title ="济南农商行小微信贷中心利息统计表("+startDate+"至"+filter.getEndDate()+")";
		
		
		// 查询台帐表
	    List<MibusidataForm> list = customerTransferFlowDao.findTzJnListByFilter(filter);
		
	
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet("sheet1");  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        HSSFCell cellTmp = row.createCell((short) 0);
		cellTmp.setCellValue(title);  //设置表格标题 For Example :济南农商行小微信贷中心外聘客户经理薪酬测算表（2016年09月）
		
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
		CellRangeAddress region = new CellRangeAddress(0, 0, 0,11);
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
        HSSFCell tmp = row.createCell((short) 7);
        tmp.setCellValue("制表日期："+dateString);
        CellRangeAddress reg = new CellRangeAddress(1, 1,7,11);
        sheet.addMergedRegion(reg);
        tmp.setCellStyle(styleFirst);
        
        
        // excel 正文内容
        row = sheet.createRow((int) 2);
        //row.s
        HSSFCell cell = row.createCell((short) 0);  
        cell.setCellValue("序号");  
        cell.setCellStyle(style);
        	        
        cell = row.createCell((short) 1);  
        cell.setCellValue("业务编号");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(1, 10*256);
        
        cell = row.createCell((short) 2);  
        cell.setCellValue("客户经理");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(2, 10*256);
        
        cell = row.createCell((short) 3);  
        cell.setCellValue("客户名称");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(3, 10*256);
        
        cell = row.createCell((short) 4);  
        cell.setCellValue("机构");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(4, 10*256);
        
        cell = row.createCell((short) 5);  
        cell.setCellValue("贷款余额");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(5, 10*256);
        
        cell = row.createCell((short) 6);  
        cell.setCellValue("已收利息");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(6, 10*256);
        
        cell = row.createCell((short) 7);  
        cell.setCellValue("合同金额");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(7, 10*256);
        
        cell = row.createCell((short) 8);  
        cell.setCellValue("授信金额");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(8, 10*256);
        
        cell = row.createCell((short) 9);  
        cell.setCellValue("发放日期");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(9, 10*256);
        
        cell = row.createCell((short) 10);  
        cell.setCellValue("发放金额");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(10, 10*256);
        
        cell = row.createCell((short) 11);  
        cell.setCellValue("操作时间");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(11, 10*256);
        
        for(int i=0;i<list.size();i++){
        	row = sheet.createRow((int) i + 3);
        	MibusidataForm form = list.get(i);
        	row.createCell((short) 0).setCellValue(i+1);  
        	row.createCell((short) 1).setCellValue(form.getBusicode());  
        	row.createCell((short) 2).setCellValue(form.getDisplayName());  
        	row.createCell((short) 3).setCellValue(form.getCname());                       
        	row.createCell((short) 4).setCellValue(form.getDeptcode());     						          
        	row.createCell((short) 5).setCellValue(Double.parseDouble(form.getBalamt()+""));
        	//row.createCell((short) 6).setCellValue(form.getPaydebt()+"");
        	row.createCell((short) 6).setCellValue(Double.parseDouble(form.getPaydebt()+""));
        	row.createCell((short) 7).setCellValue(Double.parseDouble(form.getContractmoney()));        
        	row.createCell((short) 8).setCellValue(Double.parseDouble(form.getReqlmt()+""));
        	row.createCell((short) 9).setCellValue(form.getLoandate());         
        	row.createCell((short) 10).setCellValue(Double.parseDouble(form.getMoney()+""));
        	row.createCell((short) 11).setCellValue(form.getOperdatetime());   
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
	
	
	
	/**
	 * 分段导出利息
	 */
	public void getExportLxSubsectData(PostLoanFilter filter,HttpServletResponse response) throws Exception{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = format.format(new Date());
		
		String title ="";
	    title ="济南农商行小微信贷中心利息分段统计表("+filter.getStartDate()+"至"+filter.getEndDate()+")";
		
		// 查询台帐表
	    List<MibusidataForm> list = customerTransferFlowDao.findLxSubsectJnListByFilter(filter);
	
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet("sheet1");  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        HSSFCell cellTmp = row.createCell((short) 0);
		cellTmp.setCellValue(title);  //设置表格标题 For Example :济南农商行小微信贷中心外聘客户经理薪酬测算表（2016年09月）
		
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
		CellRangeAddress region = new CellRangeAddress(0, 0, 0,7);
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
        CellRangeAddress reg = new CellRangeAddress(1, 1,4,7);
        sheet.addMergedRegion(reg);
        tmp.setCellStyle(styleFirst);
        
        
        // excel 正文内容
        row = sheet.createRow((int) 2);
        //row.s
        HSSFCell cell = row.createCell((short) 0);  
        cell.setCellValue("序号");  
        cell.setCellStyle(style);
        	        
        cell = row.createCell((short) 1);  
        cell.setCellValue("业务编号");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(1, 10*256);
        
        cell = row.createCell((short) 2);  
        cell.setCellValue("客户经理");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(2, 10*256);
        
        cell = row.createCell((short) 3);  
        cell.setCellValue("客户名称");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(3, 10*256);
        
        cell = row.createCell((short) 4);  
        cell.setCellValue("机构");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(4, 10*256);
       
        cell = row.createCell((short) 5);  
        cell.setCellValue("已收利息");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(5, 10*256);
        
        cell = row.createCell((short) 6);  
        cell.setCellValue("发放日期");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(6, 10*256);
         
        cell = row.createCell((short) 7);  
        cell.setCellValue("操作时间");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(7, 10*256);
        
        for(int i=0;i<list.size();i++){
        	row = sheet.createRow((int) i + 3);
        	MibusidataForm form = list.get(i);
        	row.createCell((short) 0).setCellValue(i+1);  
        	row.createCell((short) 1).setCellValue(form.getBusicode());  
        	row.createCell((short) 2).setCellValue(form.getDisplayName());  
        	row.createCell((short) 3).setCellValue(form.getCname());                       
        	row.createCell((short) 4).setCellValue(form.getDeptcode());     						          
        	row.createCell((short) 5).setCellValue(Double.parseDouble(form.getPaydebt()+""));
        	row.createCell((short) 6).setCellValue(form.getLoandate());         
        	row.createCell((short) 7).setCellValue(form.getOperdatetime());   
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
	
	
	
	/**
	 * 分段导出授信（合同）
	 */
	public void exportContractSubsectData(PostLoanFilter filter,HttpServletResponse response) throws Exception{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = format.format(new Date());
		
		String title ="";
	    title ="济南农商行小微信贷中心授信分段统计表("+filter.getStartDate()+"至"+filter.getEndDate()+")";
		
		// 查询合同表
	    List<ContractForm> list = customerTransferFlowDao.findContractJnListByFilter(filter);
	
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet("sheet1");  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        HSSFCell cellTmp = row.createCell((short) 0);
		cellTmp.setCellValue(title);  //设置表格标题 For Example :济南农商行小微信贷中心外聘客户经理薪酬测算表（2016年09月）
		
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
        cell.setCellValue("业务编号");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(1, 10*256);
        
        cell = row.createCell((short) 2);  
        cell.setCellValue("客户经理");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(2, 10*256);
        
        cell = row.createCell((short) 3);  
        cell.setCellValue("客户名称");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(3, 10*256);
        
        cell = row.createCell((short) 4);  
        cell.setCellValue("机构");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(4, 10*256);
       
        cell = row.createCell((short) 5);  
        cell.setCellValue("合同金额");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(5, 10*256);
        
        cell = row.createCell((short) 6);  
        cell.setCellValue("起始日");  
        cell.setCellStyle(style);
        sheet.setColumnWidth(6, 10*256);
      
        
        for(int i=0;i<list.size();i++){
        	row = sheet.createRow((int) i + 3);
        	ContractForm form = list.get(i);
        	row.createCell((short) 0).setCellValue(i+1);  
        	row.createCell((short) 1).setCellValue(form.getKeycode());  
        	row.createCell((short) 2).setCellValue(form.getDisplayName());  
        	row.createCell((short) 3).setCellValue(form.getChineseName());                       
        	row.createCell((short) 4).setCellValue(form.getDeptCode());     						          
        	row.createCell((short) 5).setCellValue(Double.parseDouble(form.getMoney()+""));
        	row.createCell((short) 6).setCellValue(form.getStartDate());         
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
	
	
	
	
	
	
	/**
	 *  查询信用,抵押，担保类产品贷款余额
	 *  C101-保证    C102-抵押   C100-信用 
	 */
	public BigDecimal  findAmtByUserIdAndProdType(IUser user,String prodType){
		Map<String, Object> params = new HashMap<String, Object>();
		String userId ="";
		if(CommonConstant.USER_TYPE.USER_TYPE_1 == user.getUserType()){
			userId = user.getId();
		}
		params.put("userId", userId);
		params.put("prodType", prodType);
		
	    StringBuffer sql = new StringBuffer("select nvl(sum(money),0) as  pslsd from t_mibusidata_view mi,  "+
			   							   "              basic_customer_information cust                  "+
			   							   "where cust.ty_customer_id = mi.CUSTID                          ");
	   
	    if(StringUtils.trimToNull(userId)!=null){
			sql.append(" and cust.user_id =#{userId}");
		}
	    
	    if(StringUtils.trimToNull(prodType)!=null){
			sql.append(" and mi.MAINASSURE =#{prodType}");
		}
	    
		List<AccLoanCollectInfo> list = commonDao.queryBySql(AccLoanCollectInfo.class, sql.toString(), params);
		
		if(list!=null&&list.size()>0){
			return list.get(0).getPslsd();
		}
		return null;
	}
	
	
	/**
	 * 查询信用,抵押，担保类产品贷款笔数
	 */
	public BigDecimal  findNumsOfPensByUserIdAndProdType(IUser user,String prodType){
		Map<String, Object> params = new HashMap<String, Object>();
		String userId ="";
		if(CommonConstant.USER_TYPE.USER_TYPE_1 == user.getUserType()){
			userId = user.getId();
		}
		params.put("userId", userId);
		params.put("prodType", prodType);
		
	   StringBuffer sql = new StringBuffer("select nvl(count(*),0) as  pslsd from t_mibusidata_view mi,    "+
			   							   "              basic_customer_information cust                  "+
			   							   "where cust.ty_customer_id = mi.CUSTID                          ");
	   
	    if(StringUtils.trimToNull(userId)!=null){
			sql.append(" and cust.user_id =#{userId}");
		}
	    
	    if(StringUtils.trimToNull(prodType)!=null){
			sql.append(" and mi.MAINASSURE =#{prodType}");
		}
	    
		List<AccLoanCollectInfo> list = commonDao.queryBySql(AccLoanCollectInfo.class, sql.toString(), params);
		
		if(list!=null&&list.size()>0){
			return list.get(0).getPslsd();
		}
		return null;
	}
}
