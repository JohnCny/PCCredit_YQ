package com.cardpay.pccredit.manager.service;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.common.Arith;
import com.cardpay.pccredit.customer.model.CustomerInfor;
import com.cardpay.pccredit.manager.constant.ReturnReceiptConstant;
import com.cardpay.pccredit.manager.dao.ManagerSalaryDao;
import com.cardpay.pccredit.manager.filter.ManagerCashConfigurationFilter;
import com.cardpay.pccredit.manager.filter.ManagerSalaryFilter;
import com.cardpay.pccredit.manager.model.AccountManagerParameter;
import com.cardpay.pccredit.manager.model.FourMonthModel;
import com.cardpay.pccredit.manager.model.ManagerCashConfiguration;
import com.cardpay.pccredit.manager.model.ManagerSalary;
import com.cardpay.pccredit.manager.model.ManagerSalaryForm;
import com.cardpay.pccredit.manager.model.ManagerSalaryParameter;
import com.cardpay.pccredit.manager.model.SalaryParameter;
import com.cardpay.pccredit.manager.model.TJxParameters;
import com.cardpay.pccredit.manager.model.TJxSpecificParameters;
import com.cardpay.pccredit.manager.model.TMibusidata;
import com.cardpay.pccredit.manager.model.TPerformanceParameters;
import com.cardpay.pccredit.manager.model.TRiskMargin;
import com.cardpay.pccredit.manager.model.TRiskMarginSpecific;
import com.cardpay.pccredit.manager.model.TyPerformanceParameters;
import com.cardpay.pccredit.manager.model.TyRiskMargin;
import com.cardpay.pccredit.manager.model.TyRiskMarginSpecific;
import com.cardpay.pccredit.manager.web.AccountManagerParameterForm;
import com.cardpay.pccredit.system.model.SystemUser;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.base.database.id.IDGenerator;
import com.wicresoft.jrad.base.database.model.QueryResult;

/**
 * @author songchen
 * 2016-09-01下午5:56:18
 */
@Service
public class ManagerSalaryService {
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private ManagerSalaryDao managerSalaryDao;
	
	@Autowired
	private ManagerCashConfigurationService managerCashConfigurationService;
	
	@Autowired
	private ManagerAssessmentScoreService managerAssessmentScoreService;
	
	@Autowired
	private ManagerPerformanceParametersService managerPerformanceParametersService;
	
	@Autowired
	private AccountManagerParameterService accountManagerParameterService;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;  
	/**
	 * 过滤查询
	 * @param filter
	 * @return
	 */
	public QueryResult<ManagerSalary> findManagerSalaryByFilter(ManagerSalaryFilter filter) {
		List<ManagerSalary> list = managerSalaryDao.findManagerSalarysByFilter(filter);
		int size = managerSalaryDao.findManagerSalarysCountByFilter(filter);
		QueryResult<ManagerSalary> qs = new QueryResult<ManagerSalary>(size, list);
		return qs;
	}
	
	/**
	 * 插入客户经理薪资
	 * @param riskCustomer
	 * @return
	 */
	public String insertManagerSalary(ManagerSalary managerSalary) {
		if(managerSalary.getCreatedTime() == null){
			managerSalary.setCreatedTime(Calendar.getInstance().getTime());
		}
		if(managerSalary.getModifiedTime() == null){
			managerSalary.setModifiedTime(Calendar.getInstance().getTime());
		}
		commonDao.insertObject(managerSalary);
		return managerSalary.getId();
	}
	/**
	 * 计算客户经理月度薪资
	 * @param year
	 * @param month
	 * @return
	 */
	public boolean calculateMonthlySalary(int year, int month){
		boolean flag = true;
		try{
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month, 1);
			
			calendar.add(Calendar.MONTH, -1);
			
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH);
			managerSalaryDao.deleteManagerSalaryByYearAndMonth(year, month);
			// 获取客户经理最大层级
			int maxLevel = managerSalaryDao.getMaxManagerLevel();
			for(int i = maxLevel; i > 0; i--){
				calculateLevelSalary(year, month, i);
			}
		}catch(Exception e){
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 计算某个层级的客户经理的薪资
	 * @param year
	 * @param month
	 * @param level
	 * @return
	 */
	public void calculateLevelSalary(int year, int month, int level){
		// 
		List<SalaryParameter> list = managerSalaryDao.findSalaryParametersByFilter(level, year, month);
		Map<String, ManagerSalary> salaryMap = getManagerSalary(list, year, month);
		
		ManagerSalary managerSalary = null;
		SalaryParameter salaryParameter = null;
		for(int i = 0; i < list.size(); i++){
			salaryParameter = list.get(i);
			/*if("0000000049ea417e0149f05573f81d2e".equals(salaryParameter.getManagerId())){
				int a = 0;
			}*/
			managerSalary = salaryMap.get(salaryParameter.getManagerId());
			// 责任工资
			managerSalary.setDutySalary(salaryParameter.calculateDutySalary());
			// 津贴
			managerSalary.setAllowance(salaryParameter.getAllowance());
			// 底薪
			managerSalary.setBasePay(salaryParameter.getBasePay());
			// 返还金额(等于三年前本月存入)
			managerSalary.setReturnPrepareAmount(salaryParameter.getInsertPrepareAmount());
			
			// 管理绩效
			String managePerformance = "0";
			// 判断是否为叶子节点
			if(!salaryParameter.isLeaf()){
				// 获取客户经理管理绩效利息
				managePerformance = managerSalaryDao.getManagePerformance(managerSalary.getCustomerId(), year, month);
			}
			// 计算并设置绩效工资
			managerSalary.setRewardIncentiveInformation(salaryParameter.calculatePerformanceSalary(managePerformance));
			
			String reward = managerSalary.getRewardIncentiveInformation();
			// 计算并设置存入的风险准备金(乘以风险准备金权数)
			String insertPrepareAmount = Arith.mulReturnStr(getExtractRate(reward),reward);
			insertPrepareAmount = Arith.mulReturnStr(insertPrepareAmount,salaryParameter.getMarginExtractInfo());
			managerSalary.setInsertPrepareAmount(insertPrepareAmount);
			
			// 本月风险准备金余额  = 上月风险准备金余额  + 本月存入的风险准备金 - 返还金额(等于三年前本月存入)
			String reserveBalances = Arith.subReturnStr(Arith.addReturnStr(salaryParameter.getRiskReserveBalances(), managerSalary.getInsertPrepareAmount()), managerSalary.getReturnPrepareAmount());
			managerSalary.setRiskReserveBalances(reserveBalances);
			// 保存客户经理薪资
			insertManagerSalary(managerSalary);
		}
	}
	
	public Map<String, ManagerSalary> getManagerSalary(List<SalaryParameter> list, int year, int month){
		Map<String, ManagerSalary> hm = new HashMap<String, ManagerSalary>();
		ManagerSalary managerSalary = null;
		for(SalaryParameter salaryParameter : list){
			managerSalary = new ManagerSalary();
			managerSalary.setCustomerId(salaryParameter.getManagerId());
			managerSalary.setYear(year+"");
			managerSalary.setMonth(month+"");
			hm.put(managerSalary.getCustomerId(), managerSalary);
		}
		return hm;
	}
	
	/*
	 * 获取提取比率
	 */
	public String getExtractRate(String amount){
		ManagerCashConfigurationFilter filter = new ManagerCashConfigurationFilter();
		QueryResult<ManagerCashConfiguration> qs = managerCashConfigurationService.findManagerCashConfigurationByFilter(filter);
		for(ManagerCashConfiguration cashConfiguration : qs.getItems()){
			if(Arith.compare(amount, cashConfiguration.getStartAmount()) >= 0 
					&& Arith.compare(cashConfiguration.getEndAmount(), amount) >= 0){
				return Arith.divReturnStr(cashConfiguration.getMarginExtractInfo(), "100");
			}
		}
		return "0";
	}
	/**
	 * 得到风险保证余额
	 * @param year
	 * @param month
	 * @param id
	 * @return
	 */
	public String getReturnPrepareAmountById(int year,int month,String id){
		if(StringUtils.isNotEmpty(id)){
			return managerSalaryDao.getReturnPrepareAmountById(year, month, id);
		}else{
			return "-1";
		}
	}
	/**
	 * 得到客户经理绩效工资
	 * @param year
	 * @param month
	 * @param id
	 * @return
	 */
	public String getRewardIncentiveInformation(int year,int month,String id){
		if(StringUtils.isNotEmpty(id)){
			return managerSalaryDao.getRewardIncentiveInformation(year, month, id);
		}else{
			return "-1";
		}
	}
	
	/**
	 * 计算客户经理月度薪资(太原)
	 * @param year
	 * @param month
	 * @return
	 */
	public boolean calculateMonthlySalaryTy(int year, int month){
		boolean flag = true;
		try{
			managerSalaryDao.deleteManagerSalaryByYearAndMonth(year, month);
			//获取所有客户经理
			List<AccountManagerParameterForm> accountList = accountManagerParameterService.findAccountManagerParameterAll();
			//循环计算客户经理当月工资
			for(int i=0;i<accountList.size();i++){
				//获取此客户经理的绩效参数
				TyPerformanceParameters parameter = managerPerformanceParametersService.getParameterByLevel(accountList.get(i).getUserId());
				if(parameter!=null){
					calculateSalaryExactly(year, month,parameter,accountList.get(i).getUserId());
				}
			}
			//计算主管额外绩效
			String accountSql = "select b.* from manager_belong_map a,account_manager_parameter b where  a.child_id=b.id and a.parent_id in (select child_id from manager_belong_map where parent_id is null)";
			List<AccountManagerParameter> accountGroupList = commonDao.queryBySql(AccountManagerParameter.class, accountSql, null);
			//循环计算客户经理主管当月工资
			for(int i=0;i<accountGroupList.size();i++){
				calculateSalaryExactlyGroup(year, month,accountGroupList.get(i).getUserId());
			}
		}catch(Exception e){
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 *具体每月工资计算 （客户经理）
	 */
	private void calculateSalaryExactly(int year,int month,TyPerformanceParameters parameter ,String customerId){
		String date = "";
		if(month<10){
			date = year +"0"+month;
		}else{
			date = year +""+month;
		}
		
		//获得每月绩效参数值
		String sql = "select * from manager_salary_parameter where user_id='"+customerId+"' and month='"+date+"'";
		List<ManagerSalaryParameter> salaryList = commonDao.queryBySql(ManagerSalaryParameter.class, sql, null);
		if(salaryList.size()>0){
			ManagerSalaryParameter salaryParameter = salaryList.get(0);
			//主调绩效
			Double zdPerform = Double.parseDouble(parameter.getA()) * Double.parseDouble(salaryParameter.getZdCount());
			//辅调绩效
			Double fdPerform = Double.parseDouble(parameter.getF()) * Double.parseDouble(salaryParameter.getFdCount());
			//管户绩效
			Double ghPerform = Double.parseDouble(parameter.getB()) * Double.parseDouble(salaryParameter.getTubes());
			//审批绩效
			Double spPerform = Double.parseDouble(parameter.getD()) * Double.parseDouble(salaryParameter.getSpCount());
			//岗位绩效
			Double gwPerform = Double.parseDouble(parameter.getE());
			//完成度
			Double pet = Double.parseDouble(salaryParameter.getCompeterCount())/Double.parseDouble(parameter.getObjectCount());
			//完成绩效
			Double compet = 0.00;
			if(pet>=0.8&&pet<1){
				compet = 600.00;
			}else if(pet>=1&&pet<1.2){
				compet = 1000.00;
			}else if(pet>=1.2){
				compet = 1300.00;
			}
			String competerPet = String.format("%.2f", pet);
			Double monthPerform = zdPerform+fdPerform+ghPerform+spPerform+gwPerform+compet;
			//保存当月工资单
			ManagerSalary salary = new ManagerSalary();
			salary.setYear(year+"");
			salary.setMonth(month+"");
			salary.setCustomerId(customerId);
			salary.setBasePay(parameter.getBasicPerformance());
			salary.setCompeterPet(competerPet);
			salary.setRewardIncentiveInformation(String.valueOf(monthPerform));
			salary.setZdPerform(zdPerform+"");
			salary.setFdPerform(fdPerform+"");
			salary.setSpPerform(spPerform+"");
			salary.setGhPerform(ghPerform+"");
			salary.setGwPerform(gwPerform+"");
			salary.setCompeterPerform(compet+"");
			commonDao.insertObject(salary);
			
		}

	}
	/**
	 *具体每月工资计算 （主管）
	 */
	private void calculateSalaryExactlyGroup(int year,int month ,String customerId){
		//获取组员客户经理id
		String childSql = "select * from manager_salary where customer_id in (select user_id from ACCOUNT_MANAGER_PARAMETER where id in( SELECT a.CHILD_ID FROM MANAGER_BELONG_MAP a ,ACCOUNT_MANAGER_PARAMETER b where a.PARENT_ID=b.id and b.USER_ID='"+customerId+"')) and year='"+year+"' and month='"+month+"'";
		List<ManagerSalary> childsSalaryList = commonDao.queryBySql(ManagerSalary.class,childSql , null);
		//团队完成数
		Double groupAll=0.00;
		//团队完成比例
		Double groupAllPet=0.00;
		//主管额外绩效
		Double groupSalary =0.00;
		for(int i=0;i<childsSalaryList.size();i++){
			groupAll+=Double.parseDouble(childsSalaryList.get(i).getCompeterPet());
		}
		groupAllPet = groupAll/childsSalaryList.size();
		if(groupAllPet>=0.8){
			groupSalary = 1000.00;
		}else{
			groupSalary = 500.00;
		}
		List<ManagerSalary> groupSalaryList = commonDao.queryBySql(ManagerSalary.class,"select * from manager_salary where customer_id='"+customerId+"' and  year='"+year+"' and month='"+month+"'" , null);
		if(groupSalaryList.size()>0){
			ManagerSalary group = groupSalaryList.get(0);
			group.setGroupSalary(groupSalary+"");
			group.setRewardIncentiveInformation(Double.parseDouble(group.getRewardIncentiveInformation())+groupSalary+"");
			commonDao.updateObject(group);
		}
	}

	
	/*
	 * 逾期扣款比例
	 */
	private double getOverLoanPer(double overdueLoan){
		if(overdueLoan==0){
			return 0;
		}else if(overdueLoan<=0.005){
			return 0.05;
		}else if(overdueLoan>0.005&&overdueLoan<=0.01){
			return 0.1;
		}else if(overdueLoan>0.01&&overdueLoan<=0.015){
			return  0.2;
		}else{
			return 0.3;
		}
	}
	/*
	 * 新增风险保证金计算
	 */
	private double getAddRisk(double monthPerform){
		double addRisk = 0;
		if(monthPerform<=2000){
			return addRisk;
		}else if(monthPerform>2000&&monthPerform<=5000){
			addRisk = (monthPerform-2000)*0.1;
		}else{
			addRisk=3000*0.1+(monthPerform-5000)*0.2;
		}
		return addRisk;
	}
	
	/*
	 * 返还风险保证金
	 */
	private double outRiskMargin(int year,int month,String customerId){
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, 1);
		
		calendar.add(Calendar.MONTH, -18);
		//获取18个月前的时间
		int  year_18 = calendar.get(Calendar.YEAR);
		int month_18 = calendar.get(Calendar.MONTH);
		//查询18月前风险保证金
		TyRiskMarginSpecific specific = getRiskMarginSpecific(year_18,month_18,customerId);
		if(specific==null){
			return 0;
		}else{
			return (Double.parseDouble(specific.getInRiskMargin())-Double.parseDouble(specific.getDeductRiskMargin()))>0?Double.parseDouble(specific.getInRiskMargin())-Double.parseDouble(specific.getDeductRiskMargin()):0;
		}
	}
	
	/*
	 * 根据年月查询风险保证金log
	 */
	public TyRiskMarginSpecific getRiskMarginSpecific(int year,int month,String customerId){
		String sql ="select * from ty_risk_margin_specific where risk_id in (select id from ty_risk_margin where customer_id='"+customerId+"') and year='"+year+"' and month='"+month+"'";
		List<TyRiskMarginSpecific> tyRiskMarginSpecifics = commonDao.queryBySql(TyRiskMarginSpecific.class, sql, null);
		if(tyRiskMarginSpecifics.size()>0){
			return tyRiskMarginSpecifics.get(0);
		}else{
			return null;
		}
	}
	/*
	 * 根据customerId获取总风险保证金
	 */
	public TyRiskMargin getRiskMarginByCustomerId(String customerId){
		String sql = "select * from ty_risk_margin where customer_id='"+customerId+"'";
		List<TyRiskMargin> tyRiskMarginsList = commonDao.queryBySql(TyRiskMargin.class, sql, null);
		if(tyRiskMarginsList.size()>0){
			return tyRiskMarginsList.get(0);
		}else{
			return null;
		}
	}
	/*
	 * 绩效导出
	 */
	public void getExportData(ManagerSalaryFilter filter,HttpServletResponse response) throws Exception{
		List<ManagerSalary> salaryList = managerSalaryDao.findManagerSalarys(filter);
		
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet("客户经理绩效详情");  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        HSSFCell cell = row.createCell((short) 0);  
        cell.setCellValue("年份");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 1);  
        cell.setCellValue("月份");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 2);  
        cell.setCellValue("小微团队");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 3);  
        cell.setCellValue("客户经理");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 4);  
        cell.setCellValue("底薪");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 5);  
        cell.setCellValue("主管绩效");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 6);  
        cell.setCellValue("主调绩效");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 7);  
        cell.setCellValue("辅调绩效");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 8);  
        cell.setCellValue("管户绩效");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 9);  
        cell.setCellValue("审批绩效");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 10);  
        cell.setCellValue("岗位绩效");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 11);  
        cell.setCellValue("完成度绩效");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 12);  
        cell.setCellValue("总绩效");  
        cell.setCellStyle(style);
        for(int i=0;i<salaryList.size();i++){
        	row = sheet.createRow((int) i + 1);
        	ManagerSalary salary = salaryList.get(i);
        	row.createCell((short) 0).setCellValue((String) salary.getYear());  
        	row.createCell((short) 1).setCellValue((String) salary.getMonth());  
        	row.createCell((short) 2).setCellValue((String) salary.getShortName());  
        	row.createCell((short) 3).setCellValue((String) salary.getManagerName());  
        	row.createCell((short) 4).setCellValue((String) salary.getBasePay());  
        	row.createCell((short) 5).setCellValue((String) salary.getGroupSalary());  
        	row.createCell((short) 6).setCellValue((String) salary.getZdPerform());  
        	row.createCell((short) 7).setCellValue((String) salary.getFdPerform());  
        	row.createCell((short) 8).setCellValue((String) salary.getGhPerform());  
        	row.createCell((short) 9).setCellValue((String) salary.getSpPerform());  
        	row.createCell((short) 10).setCellValue((String) salary.getGwPerform());  
        	row.createCell((short) 11).setCellValue((String) salary.getCompeterPerform());  
        	row.createCell((short) 12).setCellValue((String) salary.getRewardIncentiveInformation());  
        }
        response.setHeader("Connection", "close");
        response.setHeader("Content-Type", "application/vnd.ms-excel;charset=GBK");
        response.setHeader("Content-Disposition", "attachment;filename="                + new String("客户经理绩效详情.xls".getBytes(), "iso-8859-1"));
        OutputStream out = response.getOutputStream();  
        wb.write(out);
        
        out.close();
	}
	
	
	//----------------------------------------------济南绩效start----------------------------------------------------//
	
	public QueryResult<ManagerSalaryForm> findManagerSalaryByFilterJn(ManagerSalaryFilter filter) {
		List<ManagerSalaryForm> list = managerSalaryDao.findManagerSalarysByFilterJn(filter);
		int size = managerSalaryDao.findManagerSalarysCountByFilterJn(filter);
		QueryResult<ManagerSalaryForm> qs = new QueryResult<ManagerSalaryForm>(size, list);
		return qs;
	}
	
	
	public List<ManagerSalaryForm> findManagerSalaryList(ManagerSalaryFilter filter) {
		List<ManagerSalaryForm> list = managerSalaryDao.findManagerSalarysList(filter);
		return list;
	}
	
	
	public ManagerSalary findManagerSalaryById(String id) {
		ManagerSalary salary = commonDao.findObjectById(ManagerSalary.class, id);
		SystemUser user = commonDao.findObjectById(SystemUser.class,salary.getCustomerId());
		salary.setManagerName(user.getDisplayName());
		return salary;
	}
	

	public int updateManagerSalary(ManagerSalary salary) {
		return commonDao.updateObject(salary);
	}
	
	/**
	 * 四维授信模型生成数据
	 */
	public void doGet() {
		String arg0[] = {"外地人","本省外地人","济南本地"};//户籍
		String arg1[] = {"25岁以下","25-30","30-35","35-50","50以上"};//年龄
		String arg2[] = {"专科以下","专科","本科,","研究生及以上"};//学历
		String arg3[] = {"离异","未婚","已婚"};//婚姻状况
		String arg4[] = {"无子女","有未成年子女","有成年子女"};//子女情况
		String arg5[] = {"公务员及事业编","央企、国企","上市公司","世界五百强企业","公立学院、公立医院员工","银行员工","证券、保险非销售岗","其他"};//单位性质
		String arg6[] = {"无房","有1套房","2套及以上"};//房 产
		String arg7[] = {"无车","有车（私家车"};//车产情况
		String arg8[] = {"无逾期","有逾期（次数）"};//借款人24个月信用情况
		String arg9[] = {"无","有"};//与我行业务往来
		String arg10[] = {"1年-3年","3年-5年","5年以上"};//经营年限
		String arg11[] = {"50%-70%","50%（含）以下"};//资产负债率
		String arg12[] = {"10万以下","10-20万","20-30万","30-50万","50万以上"};//家庭年收入
		
		String str;
		int i = 0;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		for(int a1 =0;a1<arg0.length;a1++){
			for(int a2 =0;a2<arg1.length;a2++){
				for(int a3 =0;a3<arg2.length;a3++){
					for(int a4 =0;a4<arg3.length;a4++){
						for(int a5 =0;a5<arg4.length;a5++){
							for(int a6 =0;a6<arg5.length;a6++){
								for(int a7 =0;a7<arg6.length;a7++){
									for(int a8 =0;a8<arg7.length;a8++){
										for(int a9 =0;a9<arg8.length;a9++){
											for(int a10 =0;a10<arg9.length;a10++){
												for(int a11 =0;a11<arg10.length;a11++){
													for(int a12 =0;a12<arg11.length;a12++){
														for(int a13 =0;a13<arg12.length;a13++){
															str = arg0[a1]+","+arg1[a2]+","+arg2[a3]+","+arg3[a4]+","+arg4[a5]+","+
																	   arg5[a6]+","+arg6[a7]+","+arg7[a8]+","+arg8[a9]+","+arg9[a10]+","+
																	   arg10[a11]+","+arg11[a12]+","+arg12[a13];
															goData(str);//生成数据
															/*map.put("a1", str.split(",")[0]);
															map.put("a2", str.split(",")[1]);
															map.put("a3", str.split(",")[2]);
															map.put("a4", str.split(",")[3]);
															map.put("a5", str.split(",")[4]);
															map.put("a6", str.split(",")[5]);
															map.put("a7", str.split(",")[6]);
															map.put("a8", str.split(",")[7]);
															map.put("a9", str.split(",")[8]);
															map.put("a10", str.split(",")[9]);
															map.put("a11", str.split(",")[10]);
															map.put("a12", str.split(",")[11]);
															map.put("a13", str.split(",")[12]);
															list.add(map);*/
															System.out.println(i++);
															/*insertCIPERSONBADRECORD(list);
															System.out.println(i++);*/
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		insertCIPERSONBADRECORD(list);
	}
	
	public void goData(String str){
		FourMonthModel model= new FourMonthModel();
		model.setA1(str.split(",")[0]);
		model.setA2(str.split(",")[1]);
		model.setA3(str.split(",")[2]);
		model.setA4(str.split(",")[3]);
		model.setA5(str.split(",")[4]);
		model.setA6(str.split(",")[5]);
		model.setA7(str.split(",")[6]);
		model.setA8(str.split(",")[7]);
		model.setA9(str.split(",")[8]);
		model.setA10(str.split(",")[9]);
		model.setA11(str.split(",")[10]);
		model.setA12(str.split(",")[11]);
		model.setA13(str.split(",")[12]);
		commonDao.insertObject(model);
	}
	
	
	/**
     * 对私客户不良记录
     * @param list 
     */
    public void insertCIPERSONBADRECORD(List<Map<String, Object>> list){
        final List<Map<String, Object>> shopsList = list;
        String sql =    "  insert into FOUR_MONTH_MODEL 	(a1,                "+
                "											a2,                 "+
                "									        a3,                 "+
                "									        a4,                 "+
                "										    a5,                 "+
                "											a6,                 "+
                "											a7,                 "+
                "										    a8,                 "+
                "					                        a9,a10,a11,a12,a13) "+
				"            values                        (?,                  "+
				"    		    						    ?,                  "+
				"    		                                ?,                  "+
				"    		                                ?,                  "+
				"    		                                ?,                  "+
				"    		                                ?,                  "+
				"    		                                ?,                  "+
				"    		                                ?,                  "+
				"    		                                ?,?,?,?,?)          ";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter(){
            public void setValues(PreparedStatement ps,int i)throws SQLException
            {
                ps.setString(1, ((Map<String, Object>)shopsList.get(i)).get("a1").toString());
                ps.setString(2, ((Map<String, Object>)shopsList.get(i)).get("a2").toString());
                ps.setString(3, ((Map<String, Object>)shopsList.get(i)).get("a3").toString());
                ps.setString(4, ((Map<String, Object>)shopsList.get(i)).get("a4").toString());
                ps.setString(5, ((Map<String, Object>)shopsList.get(i)).get("a5").toString());
                ps.setString(6, ((Map<String, Object>)shopsList.get(i)).get("a6").toString());
                ps.setString(7, ((Map<String, Object>)shopsList.get(i)).get("a7").toString());
                ps.setString(8, ((Map<String, Object>)shopsList.get(i)).get("a8").toString());
                ps.setString(9, ((Map<String, Object>)shopsList.get(i)).get("a9").toString());
                ps.setString(10, ((Map<String, Object>)shopsList.get(i)).get("a10").toString());
                ps.setString(11, ((Map<String, Object>)shopsList.get(i)).get("a11").toString());
                ps.setString(12, ((Map<String, Object>)shopsList.get(i)).get("a12").toString());
                ps.setString(13, ((Map<String, Object>)shopsList.get(i)).get("a13").toString());
            }
            public int getBatchSize()
            {
                return shopsList.size();
            }
        });
    }
    
    
    /**
     * 每月的5号跑批自动生成上个月的绩效工资信息
     * @Time 2017年2月4日 10:10:24
     */
    public void doRunBatchGeneratePerformancePay(){
    	//获取今日日期
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = format.format(new Date());
		String year  = dateString.substring(0, 4);
	    String month = dateString.substring(5, 7);
	    year  = "2016";
	    month = "11";
		
		// 计算当月的上一个月
		String lastMonth = "";
		String lastYear = "";
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(year),Integer.parseInt(month)-1,1);
		calendar.add(Calendar.MONTH, -1);
		lastYear = calendar.get(Calendar.YEAR)+"";
		lastMonth = calendar.get(Calendar.MONTH)+1+"";
		
		if(Integer.parseInt(lastMonth)<10){
			lastMonth = "0"+lastMonth;
		}
		
		//System.out.println(lastYear+"----"+lastMonth);
		docalculateMonthlySalaryTy(lastYear,lastMonth);
    }

	
	/**
	 * 计算当月绩效
	 * 客户经理应发薪酬 = 基本薪酬 + 贷款业务绩效 + 其他业务绩效 - 风险保证金
	 * @param year
	 * @param month
	 */
	public void docalculateMonthlySalaryTy(String year,String month) {
		
		// 判断该月客户经理月度薪资是否已经存在
		int count = managerSalaryDao.findManagerSalaryCount(year, month);
		if (count > 0) {
			throw new RuntimeException("该月客户经理薪资已存在！");
		}

		// 查询行编以及外聘客户经理list
		List<AccountManagerParameter> list = commonDao
				.queryBySql(
						AccountManagerParameter.class,
						"select * from account_manager_parameter where manager_type in ('1','2')",
						null);

		// 查询风险岗list
		List<AccountManagerParameter> alist = commonDao
				.queryBySql(
						AccountManagerParameter.class,
						"select * from account_manager_parameter where manager_type in ('3')",
						null);

		

		// 生成 T_JX_SPECIFIC_PARAMETERS表数据
		for (AccountManagerParameter accountManagerParameter : list) {
			generateJxSpecificParameters(accountManagerParameter.getUserId(),year, month);
		}
		
		// 生成 T_JX_PARAMETERS表数据
		for (AccountManagerParameter accountManagerParameter : list) {
			generateJxParameters(accountManagerParameter.getUserId(), year,month);
		}

		// 具体计算行编以及外聘客户经理的当月工资
		for (AccountManagerParameter accountManagerParameter : list) {
			doCalculateSalaryExactly(year, month,
					accountManagerParameter.getUserId(),
					accountManagerParameter.getBasePay(),
					accountManagerParameter.getManagerType());
		}

		// 具体计算风险岗的当月工资
		for (AccountManagerParameter accountManagerParameter : alist) {
			doCalculateSalary(year, month, accountManagerParameter.getUserId(),
					accountManagerParameter.getBasePay(),
					accountManagerParameter.getManagerType());
		}
	}
	
	
	/**
	 * 具体计算
	 * @param year 年份
	 * @param month  月份
	 * @param ManagerId 客户经理id
	 * @param basePay 基本工资
	 * @param managerType 客户经理类型  风险岗
	 * 基本薪酬+岗位津贴+业务绩效+其他业务绩效-差错扣款 
	 * 业务绩效 = 当月参与审贷会审议通过笔数 * 30元/笔
	 */
	private void doCalculateSalary(String year,String month,String ManagerId,String basePay,String managerType){
		// 查询客户所属机构
		String organName = managerSalaryDao.getOrganName(ManagerId);
		
		// 查询客户经理绩效参数
		TPerformanceParameters parameters = commonDao.queryBySql(TPerformanceParameters.class,
				"select * from T_PERFORMANCE_PARAMETERS ",null).get(0);
		
		// 查询当月参与审贷会审议通过笔数 30元/笔
		int count = managerSalaryDao.findSdAprovedCountByManagerId(ManagerId,year,month);
		
		// 保存当月工资单
		ManagerSalary salary = new ManagerSalary();
		salary.setYear(year);//年份
		salary.setMonth(month);//月份
		salary.setInstCode(organName);//所属机构
		salary.setCustomerId(ManagerId);//客户经理
		salary.setBasePay(basePay);//固定工资
		salary.setSubsidies(parameters.getL());//岗位津贴
		
		// 计算业务绩效
		BigDecimal subsidies = new BigDecimal("30").multiply(new BigDecimal(count));
		salary.setVolumePerformance(subsidies+"");//业务量绩效
		salary.setAuditNum(count+"");
		commonDao.insertObject(salary);
	}
	
	/**
	 * 具体计算
	 * @param year 年份
	 * @param month  月份
	 * @param ManagerId 客户经理id
	 * @param basePay 基本工资
	 * @param managerType 客户经理类型
	 */
	private void doCalculateSalaryExactly(String year,String month,String ManagerId,String basePay,String managerType){
		
		// 1.查询客户所属机构
		String organName = managerSalaryDao.getOrganName(ManagerId);
				
		// 2.计算贷款业务绩效
		Map<String, Object> map = doCalLoanPerformance(year,month,ManagerId);
		BigDecimal loanBusinessPerformance = new BigDecimal(map.get("MonthPerformance").toString());//当月贷款业务绩效
		
		// 3.计算风险保证金
		double addRisk = getAddMonthVentureDeposit(loanBusinessPerformance.doubleValue());//本月新增风险保证金
		
		// 本月问责风险保证金 (走页面调整流程)
		double deduceRisk = 0;
		
		// 本月返还风险保证金
		double outRisk = outMonthVentureDeposit(year,month,ManagerId);
		
		// 计算得当月风险保证金
		double monthRisk = addRisk-deduceRisk;
		
		// 获取总风险保证金
		TRiskMargin tRiskMargin = getTRiskMarginByCustomerId(ManagerId);
		
		// 保存风险保证金总表
		double lastRisk = 0;
		TRiskMarginSpecific specific = new TRiskMarginSpecific();
		
		if(tRiskMargin!=null){
			double totalRisk = monthRisk + Double.parseDouble(tRiskMargin.getTotalRiskMargin());
			tRiskMargin.setTotalRiskMargin(String.valueOf(totalRisk));
			tRiskMargin.setUpdateTime(new Date());
			commonDao.updateObject(tRiskMargin);
			specific.setRiskId(tRiskMargin.getId());
			lastRisk = totalRisk;
		}else{
			TRiskMargin riskMargin = new TRiskMargin();
			riskMargin.setId(IDGenerator.generateID());
			riskMargin.setCustomerId(ManagerId);
			riskMargin.setTotalRiskMargin(String.valueOf(monthRisk));
			riskMargin.setUpdateTime(new Date());
			riskMargin.setAccountOpenYear(year);
			riskMargin.setAccountOpenMonth(Integer.parseInt(month)+"");
			commonDao.insertObject(riskMargin);
			specific.setRiskId(riskMargin.getId());
			lastRisk = monthRisk;
		}
		
		// 保存log表
		specific.setInRiskMargin(String.valueOf(addRisk));
		specific.setOutRiskMargin(String.valueOf(outRisk));
		specific.setDeductRiskMargin(String.valueOf(deduceRisk));
		specific.setYear(year);
		specific.setMonth(month);
		commonDao.insertObject(specific);
		
		// 保存当月工资单
		ManagerSalary salary = new ManagerSalary();
		salary.setYear(year);
		salary.setMonth(month);
		salary.setInstCode(organName);//所属机构
		salary.setCustomerId(ManagerId);//客户经理
		salary.setBasePay(basePay);//固定工资
		
		// 基础任务量奖金
		if("2".equals(managerType)){
			// 外聘客户经理
			TJxParameters jxParameters = findTJxParameters(year,month,ManagerId);
			salary.setBasicTaskBonus(Integer.parseInt(jxParameters.getMonthLoanNum())>=2?"500":"0");
		}else{
			// 行编客户经理
			salary.setBasicTaskBonus("0");
		}
		
		salary.setRewardIncentiveInformation(String.valueOf(loanBusinessPerformance));//绩效工资
		salary.setRiskReserveBalances(String.valueOf(lastRisk));//风险准备金余额总额
		//salary.setDeductAmount(String.valueOf(deduceRisk));//当月扣除金额
		salary.setReturnPrepareAmount(String.valueOf(outRisk));//当月返还金额
		salary.setInsertPrepareAmount(new BigDecimal(String.valueOf(addRisk)).setScale(2,BigDecimal.ROUND_HALF_UP)+"");//当月存入的风险准备金
		salary.setVolumePerformance(map.get("portfolioPerformance").toString());//业务量绩效
		salary.setProfitDraw(map.get("lr").toString());//利润提成
		salary.setOverdueDeduct(map.get("overdueDeduct").toString());//逾期扣款
		commonDao.insertObject(salary);
	}
	
	/**
	 * 计算贷款业务绩效
	 */
	public Map<String, Object> doCalLoanPerformance(String year,String month,String ManagerId){
		
		// 查询客户经理绩效参数
		TPerformanceParameters parameters = commonDao.queryBySql(TPerformanceParameters.class,"select * from T_PERFORMANCE_PARAMETERS ",null).get(0);
		
		// 查询客户经理绩效每月绩效参数表 
		TJxParameters jxParameters = findTJxParameters(year,month,ManagerId);
		
		// 查询客户经理详细绩效参数 主要是客户日均贷款余额 产品利率等
		List<TJxSpecificParameters> list = findTJxSpecificParametersList(year,month,ManagerId);
		
		// 1.业务量绩效=当月发放贷款户数*计提单价+管户维护奖金
		//(monthLoanNum-当月发放贷款户数 ;A-主办客户经理计提单价(元/户);B-协办客户经理计提单价(元/户);D-管户维护奖金(元/户);monthEffectNum-当月有效管户数)
		BigDecimal zb  = new BigDecimal(jxParameters.getMonthLoanNum()).multiply(new BigDecimal(parameters.getA()));//主办  
		BigDecimal xb  = new BigDecimal(jxParameters.getMonthTimes()).multiply(new BigDecimal(parameters.getB()));//协办  
		BigDecimal ghwhjj  = new BigDecimal(jxParameters.getMonthEffectNum()).multiply(new BigDecimal(parameters.getD()));//管户维护奖金
		BigDecimal portfolioPerformance = zb.add(xb).add(ghwhjj);//业务量绩效
		
		
		// 2.利润提成=当月日均贷款余额*（产品利率 - FTP价格）/ 12 * A * R 
		//(E-FTP价格;F-利润提成系数;R-风险保障系数)
		BigDecimal lr = calLr(list,parameters);
		
		// 3.逾期、不良贷款扣款
		BigDecimal overdueBs = new BigDecimal(parameters.getJ()).multiply(new BigDecimal(jxParameters.getMonthOverdueLoannum()));
		BigDecimal overdueTs = new BigDecimal(parameters.getJ()).multiply(new BigDecimal(jxParameters.getMonthOverdueDays()));
		BigDecimal overdueDeduct = overdueBs.add(overdueTs);
		
		// 4.当月贷款业务绩效(贷款业务绩效=业务量绩效+利润提成–逾期扣款)
		BigDecimal MonthPerformance = portfolioPerformance.add(lr).subtract(overdueDeduct);
		
		// map
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("portfolioPerformance", portfolioPerformance);//业务量绩效
		map.put("lr", lr);//利润提成
		map.put("overdueDeduct", overdueDeduct);//逾期扣款
		map.put("MonthPerformance", MonthPerformance);//当月贷款业务绩效
		return map;
	}
	
	/**
	 * 查询客户经理绩效每月绩效参数表 
	 * @param year
	 * @param month
	 * @param ManagerId
	 * @return
	 */
	public TJxParameters findTJxParameters(String year,String month,String ManagerId){
		String sql = "select * from T_JX_PARAMETERS where "+
					 "year = '"+year+"' "+ 
					 "and month = '"+month+"' "+ 
					 "and CUSTOMER_MANAGER_ID = '"+ManagerId+"'";
		return commonDao.queryBySql(TJxParameters.class, sql, null).get(0);
	}
	
	/**
	 * 查询客户经理详细绩效参数 主要是客户日均贷款余额 产品利率等
	 * @param year
	 * @param month
	 * @param ManagerId
	 * @return
	 */
	public List<TJxSpecificParameters> findTJxSpecificParametersList(String year,String month,String ManagerId){
		String sqls = "select * from T_JX_SPECIFIC_PARAMETERS where "+ 
				  "year = '"+year+"' "+ 
			      "and month = '"+month+"' "+ 
	              "and CUSTOMER_MANAGER_ID = '"+ManagerId+"'";
		return commonDao.queryBySql(TJxSpecificParameters.class, sqls, null);
	}
	
	
	/**
	 * 计算利润提成 list 是当月该客户经理下所有客户的集合
	 * 当月日均贷款余额*（产品利率 - FTP价格）/ 12 * A * R 
	 * @param list
	 * @param parameters
	 * @return
	 */
	public BigDecimal calLr(List<TJxSpecificParameters> list,TPerformanceParameters parameters){
		BigDecimal lrs = new BigDecimal("0");
		
		for(TJxSpecificParameters tJxSpecificParameters :list){
		/*	String R = returnR(tJxSpecificParameters,parameters);
			BigDecimal  cha =  new BigDecimal(tJxSpecificParameters.getProdLimit()).subtract(new BigDecimal(parameters.getE()));
			cha = cha.divide(new BigDecimal("100"));
			BigDecimal  ar =  new BigDecimal(ReturnReceiptConstant.lr).multiply(new BigDecimal(parameters.getF())).multiply(new BigDecimal(R)).divide(new BigDecimal("100"));
			BigDecimal  profit = new BigDecimal(tJxSpecificParameters.getMonthDayAverageCustLoanamt()).multiply(cha).divide(ar,2,BigDecimal.ROUND_HALF_UP);
			lrs = lrs.add(profit);*/
			
			String R = returnR(tJxSpecificParameters,parameters);
			BigDecimal  cha =  new BigDecimal(tJxSpecificParameters.getProdLimit()).subtract(new BigDecimal(parameters.getE()));
			cha = cha.divide(new BigDecimal("100"));//（产品利率 - FTP价格）/100
			BigDecimal  profit = new BigDecimal(tJxSpecificParameters.getMonthDayAverageCustLoanamt()).multiply(cha).multiply(new BigDecimal(parameters.getF())).multiply(new BigDecimal(R)).divide(new BigDecimal("100")).divide(new BigDecimal(ReturnReceiptConstant.lr),2,BigDecimal.ROUND_HALF_UP);
			lrs = lrs.add(profit);
		}
		return lrs;
	}
	
	
	/**
	 * 获取风险保障系数R
	 * 根据产品类型
	 */
	public String returnR(TJxSpecificParameters tJxSpecificParameters,TPerformanceParameters parameters){
		//判断产品类型 
		String R ="";
		if(ReturnReceiptConstant.PR0D_TYPE_C101.equals(tJxSpecificParameters.getProdType())){
			R = parameters.getH(); //风险保障系数(R-保证类)
		}else if(ReturnReceiptConstant.PR0D_TYPE_C102.equals(tJxSpecificParameters.getProdType())){
			R = parameters.getI(); //风险保障系数(R-抵押类)
		}else{
			R = parameters.getG(); //风险保障系数(R-信用类)
		}
		return R;
	}
	
	/**
	 * 新增风险保证金计算
	 */
	private double getAddMonthVentureDeposit(double monthPerform){
		double addRisk = 0;
		if(monthPerform<=2000){
			addRisk = 2000 * 0.1;
			
		}else if(monthPerform>2000&&monthPerform<=5000){
			addRisk = 2000 * 0.1 + (monthPerform-2000)*0.2;
			
		}else if(monthPerform>5000&&monthPerform<=8000) {
			addRisk = 2000 * 0.1 + 3000*0.2 + (monthPerform-5000)*0.3;
			
		}else if(monthPerform>8000&&monthPerform<=15000) {
			addRisk = 2000 * 0.1 + 3000*0.2 + 3000*0.3 + (monthPerform-8000)*0.4;
			
		}else{
			addRisk = 2000 * 0.1 + 3000*0.2 + 3000*0.3 + 7000*0.4+ (monthPerform-15000)*0.5;
		}
		return addRisk;
	}
	
	/**
	 * 返还风险保证金
	 * (风险保证金采取延期支付的方式,延期支付年限为三年,即从第四年开始,
	 * 每年按上年度末风险保证金累计总额（除扣除部分之外）的1/3支付;)
	 * customerId-客户经理id
	 */
	private double outMonthVentureDeposit(String year,String month,String customerId){
		
		TPerformanceParameters parameters = commonDao.queryBySql(TPerformanceParameters.class,
				"select * from T_PERFORMANCE_PARAMETERS ",null).get(0);

		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(year),Integer.parseInt(month),1);
		calendar.add(Calendar.MONTH, -Integer.parseInt(parameters.getK()));
		
		// 获取36个月前的时间
		int  year36 = calendar.get(Calendar.YEAR);
		int  month36 = calendar.get(Calendar.MONTH);
		
		
		// 查询36月前风险保证金
		TRiskMargin margin = getSpecificRiskMargin(String.valueOf(year36),String.valueOf(month36),customerId);
		if(margin==null){
			return 0;
		}else{
			int years = Integer.parseInt(parameters.getK())+12;
			parameters.setK(years+"");
			commonDao.updateObject(parameters);
			return Double.parseDouble(margin.getTotalRiskMargin())/3;
		}
	}
	
	
	public TRiskMargin getSpecificRiskMargin(String year,String month,String customerId){
		String sql ="select * from t_risk_margin where CUSTOMER_ID='"+customerId+"'and ACCOUNT_OPEN_YEAR='"+year+"' and ACCOUNT_OPEN_MONTH='"+month+"'";
		List<TRiskMargin> riskMargins = commonDao.queryBySql(TRiskMargin.class, sql, null);
		if(riskMargins.size()>0){
			return riskMargins.get(0);
		}else{
			return null;
		}
	}
	

	public TRiskMargin getTRiskMarginByCustomerId(String customerId){
		String sql = "select * from t_risk_margin where customer_id='"+customerId+"'";
		List<TRiskMargin> tRiskMarginsList = commonDao.queryBySql(TRiskMargin.class, sql, null);
		if(tRiskMarginsList.size()>0){
			return tRiskMarginsList.get(0);
		}else{
			return null;
		}
	}
	
	
	/**
	 * 生成 T_JX_PARAMETERS表数据
	 * userId 客户经理
	 */
	public void generateJxParameters(String userId,String year,String month){
		
		// 查询当月该客户经理协办次数 end
		int xbNum = managerSalaryDao.findXbCountByManagerId(userId,year,month);
				
		TJxParameters jxParameters = new TJxParameters();
		jxParameters.setId(IDGenerator.generateID());
		jxParameters.setYear(year);
		jxParameters.setMonth(month);
		jxParameters.setCustomerManagerId(userId);//客户经理
		jxParameters.setMonthLoanNum(findLoanCusts(userId,year,month)+"");//当月发放贷款户数
		jxParameters.setMonthEffectNum(findEffectLoanCusts(userId,year,month)+"");//当月有效管户数
		jxParameters.setMonthOverdueLoannum(findOverdueNum(userId,year,month)+"");//当月逾期贷款笔数
		jxParameters.setMonthOverdueDays(findOverdueDays(userId,year,month)+"");//当月逾期贷款天数
		jxParameters.setMonthTimes(xbNum+"");//当月协办次数
		commonDao.insertObject(jxParameters);
	}
	
	/**
	 * 当月发放贷款户数
	 */
	public Integer findLoanCusts(String customerManagerId,String year,String month){
		String sql = "select count(distinct(a.CUSTID)) as HYK                    "+
					 "  from t_mibusidata_view a, basic_customer_information b   "+
					 " where a.custid = b.ty_customer_id                         "+
					 "   and substr(loandate, '0', '4') = '"+year+"'             "+
					 "   and substr(loandate, '6', '2') = '"+month+"'            "+
					 "   and b.USER_ID = '"+customerManagerId+"'                 ";
		List<HashMap<String, Object>> list = commonDao.queryBySql(sql, null);
		BigDecimal b = (BigDecimal) list.get(0).get("HYK");
		return b.intValue();
	}
	/**
	 * 当月有效管户数
	 * 有效管户即有有收息的客户
	 */
	public Integer findEffectLoanCusts(String customerManagerId,String year,String month){
		/*String sql = " select count(distinct(b.CUSTID))		as HYK			"+
					 "        from t_gccontractmain           a,         		"+
					 "             t_gccontractmulticlient    b,          		"+
					 "             basic_customer_information c           		"+
					 "       where trim(a.KEYCODE) = b.KEYCODE            		"+
					 "         and c.ty_customer_id = b.CUSTID           		"+
					 "         and a.keyeffectedstate ='1'                		"+
					 "         and b.keyeffectedstate ='1'                		"+
					 "         and substr(a.STARTDATE, '0', '4') = '"+year+"' 	"+
					 "         and substr(a.STARTDATE, '6', '2') = '"+month+"'  "+
					 "         and c.USER_ID = '"+customerManagerId+"'    		";*/
	/*	String sql =    "select count(distinct(t.custid)) as HYK                  "+
						"  from t_mibusidata t, basic_customer_information b      "+
						" where 1 = 1                                             "+
						"   and substr(t.operdatetime, '0', '4') = '"+year+"'     "+
						"   and substr(t.operdatetime, '6', '2') = '"+month+"'    "+
						"   and t.custid = b.ty_customer_id                       "+
						"   and nvl(PAYDEBT, 0) > 0                               "+
						"   and b.user_id = '"+customerManagerId+"'    			  ";*/
		
 String sql1  =     " select count(*)  as HYK            					       "+
				    "         from t_jx_specific_parameters t           		   "+
				    "        where t.year = '"+year+"'         					   "+
				    "          and t.month = '"+month+"'       					   "+
				    "          and t.CUSTOMER_MANAGER_ID = '"+customerManagerId+"' "+
				    "          and t.month_day_average_cust_loanamt >0             ";

		List<HashMap<String, Object>> list = commonDao.queryBySql(sql1, null);
		BigDecimal b = (BigDecimal) list.get(0).get("HYK");
		return b.intValue();
	}
	
	/**
	 * 当月逾期贷款笔数
	 */
	public Integer findOverdueNum(String customerManagerId,String year,String month){
		String sql =    " select nvl(sum(DEBTINTERESTTIMES),0)  as HYK             "+
						"  from t_mibusidata_view a, basic_customer_information b  "+
						" where a.custid = b.ty_customer_id                        "+
						"   and substr(loandate, '0', '4') = '"+year+"'            "+
						"   and substr(loandate, '6', '2') = '"+month+"'           "+
						"   and b.USER_ID ='"+customerManagerId+"'                 ";
		List<HashMap<String, Object>> list = commonDao.queryBySql(sql, null);
		BigDecimal b = (BigDecimal) list.get(0).get("HYK");
		return b.intValue();
	}
	
	/**
	 * 当月逾期贷款天数
	 */
	public int findOverdueDays(String customerManagerId,String year,String month){
	   return 0 ;
	}
	
	/**
	 * 生成 t_jx_specific_parameters
	 * 按客户id 区分 开
	 */
	public void generateJxSpecificParameters(String userId,String year,String month){
		
		// 查询该客户经理名下的客户
		List<CustomerInfor> list = commonDao.queryBySql(CustomerInfor.class,
		"select * from basic_customer_information where TY_CUSTOMER_ID is not null and USER_ID='"+ userId + "' ", null);
	    
	    // 贷款余额
	    BigDecimal balamt = new BigDecimal("0");
	    
	    for(CustomerInfor fro :list){
	    	// 产品利率
	    	String prodLimit="";
	    	
	    	// 产品类型
	    	String prodType="";
	    	
	    	// 客户id
	    	String customerId = fro.getId();
	    	
	    	// 行内客户标识id
	    	String tyCustomerId = fro.getTyCustomerId();
	    	
	    	// 查询客户贷款[产品利率]和[产品类型]
	    	List<Map<String, Object>> mapList = findProdLimitAndType(customerId,year,month);
	    	
	    	if(mapList == null || mapList.size() ==0){
	    		prodLimit = "0";
	    		prodType  = "0";
	    	}else{
		    	Map<String, Object> obj = mapList.get(0);
		    	prodLimit = obj.get("INTEREST").toString();
		        prodType =  obj.get("MAINASSURE").toString();
	    	}
	    	
	        // 计算贷款余额
	        balamt = findBalamt(tyCustomerId,year,month);
	        
	    	// 插入t_jx_specific_parameters表数据
		    TJxSpecificParameters jxSpecificParameters = new TJxSpecificParameters();
			jxSpecificParameters.setId(IDGenerator.generateID());
			jxSpecificParameters.setYear(year);
			jxSpecificParameters.setMonth(month);
			jxSpecificParameters.setMonthDayAverageCustLoanamt(balamt.toString());//当月客户日均贷款余额
			jxSpecificParameters.setProdLimit(prodLimit);//产品利率
			jxSpecificParameters.setProdType(prodType);//产品类型（C101-保证  C102-抵押  C100-信用）
			jxSpecificParameters.setCustomerId(customerId);//客户id
			jxSpecificParameters.setCustomerManagerId(userId);//客户经理id
			commonDao.insertObject(jxSpecificParameters);
	    }
	}
	
	/**
	 * 查询产品利率以及产品类型
	 * @param customerId
	 * @param year
	 * @param month
	 * @return
	 */
	public List<Map<String, Object>> findProdLimitAndType(String customerId,String year,String month){
		return managerSalaryDao.findProdLimitAndType(customerId, year, month);
	}
	
	/**
	 * 查询当月客户日均贷款余额
	 * tyCustomerId 行内客户标识id
	 */
	public BigDecimal findBalamt(String tyCustomerId,String year,String month){
		// 该客户该月的日均贷款余额
		BigDecimal balamt = new BigDecimal("0");
		 
		// 查询客户当月生成台帐BUSICODE编号的次数
		List<Map<String, Object>> mapList =  managerSalaryDao.findDistinctBusicode(tyCustomerId, year, month);
		
		// 根据BUSICODE再次筛选
		for (Map<String, Object> obj : mapList){
			String sql =    " select t.busicode,				   		    		"+				
							"       t.money,                                		"+
							"       t.loandate,                             		"+
							"       t.balamt,                               		"+
							"       t.operdatetime,                         		"+
							//"       t.limit,                                		"+
							"       t.MAINASSURE,                          			"+
							"       t.custid                                		"+
							"  from t_mibusidata t                          	    "+
							" where substr(OPERDATETIME, '0', '4') = '"+year+"'     "+
							"   and substr(OPERDATETIME, '6', '2') = '"+month+"'    "+
							"   and custid = '"+tyCustomerId+"'                     "+
							"   and busicode = '"+obj.get("BUSICODE").toString()+"' "+
							" order by OPERDATETIME asc                             ";
			List<TMibusidata> mibusidataList = new ArrayList<TMibusidata>();
			mibusidataList =  commonDao.queryBySql(TMibusidata.class, sql, null);
			balamt = balamt.add(doCalAmt(mibusidataList, year, month));
		}
		return balamt;
	}
	
	/**
	 * 筛选计算
	 * @param mibusidataList
	 * @return
	 * 一笔busicode 的list 一笔算
	 */
	public BigDecimal doCalAmt(List<TMibusidata> mibusidataList,String year,String month){
		// 获取当月实际天数
		int  days = getMonthLastDay(Integer.parseInt(year),Integer.parseInt(month));
		
		// 贷款余额
		String balamt = "";
		
		// 操作时间
		String operTime = "";
		
		/**
		 * ∑(贷款余额 x 实际使用天数)/当月实际天数  求和     分段求和
		 * 30000   2016-08-01
		 * 20000   2016-08-15
		 * 10000   2016-08-25
		 * 	0	   2016-08-31(月末日期)
		 */
		List<String> list  = new ArrayList<String>();
		
		for(TMibusidata data : mibusidataList){
		    if(balamt != data.getBalamt().toString()){
		    	balamt = data.getBalamt().toString();
		    	operTime = data.getOperdatetime();
		    	list.add(balamt+"@"+operTime);
		    }
		}
		
		// 添加月末日期
		String lastDay = year +"-"+ month +"-"+ days;
		list.add(0+"@"+lastDay);
		
		// 汇总BUSICODE单笔台帐的日均贷款余额
		BigDecimal amt   = new BigDecimal("0");
		BigDecimal amt0  = new BigDecimal("0");
		BigDecimal amt1  = new BigDecimal("0");
		
		// 实际使用天数
		int ts =0;
		String str0[];
		String str1[];
		String time0;
		String time1;
	
		
		for(int i = 0;i<list.size();i++){
			//System.out.println(list.size());
			// 第0个List 的Amt 以及操作时间
			str0 = list.get(0).split("@");
			amt0 =  new BigDecimal(str0[0]);
			time0 = str0[1];
			
			// 第1个List 的Amt 以及操作时间
			str1 = list.get(1).split("@");
			amt1 =  new BigDecimal(str1[0]);
			time1 = str1[1];
			
			// 计算两个时间戳的时间差
		    ts = calMistTime(time0,time1);
		    
		    // 计算Amt
			amt = amt.add(amt0.multiply(new BigDecimal(ts)).divide(new BigDecimal(days),2,BigDecimal.ROUND_HALF_UP));
			
			// 计算完毕移除第0条数据 List的Size随之减小
			list.remove(0);
			//System.out.println(list.size());
			i =0;
		}
		return amt;
	}
	
	/**
	 * 获得当月实际使用天数
	 * @param year
	 * @param month
	 * @return
	 */
	 public static int getMonthLastDay(int year, int month)  
	  {  
	      Calendar a = Calendar.getInstance();  
	      a.set(Calendar.YEAR, year);  
	      a.set(Calendar.MONTH, month - 1);  
	      a.set(Calendar.DATE, 1);//把日期设置为当月第一天  
	      a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天  
	      int maxDate = a.get(Calendar.DATE);  
	      return maxDate;  
	  }  
	 
	 /**
	  * 计算两个时间戳的时间差 即天数 例 2016-09-01 2016-09-05
	  */
	 public int calMistTime(String startDate,String endDate){
		 // 创建一个日历对象。
		 Calendar calendar = Calendar.getInstance();
		
		 // start
		 List<String> startDateList = findYearAndMonthAndDay(startDate);
		 calendar.set(Integer.parseInt(startDateList.get(0)), 
				 	  Integer.parseInt(startDateList.get(1)), 
					  Integer.parseInt(startDateList.get(2))); 
	     long start = calendar.getTimeInMillis();
	     
	     // end
	     List<String> endDateList = findYearAndMonthAndDay(endDate);
	     calendar.set(Integer.parseInt(endDateList.get(0)),
	    		 	  Integer.parseInt(endDateList.get(1)),
	    			  Integer.parseInt(endDateList.get(2))); 
	     long end = calendar.getTimeInMillis();
	     
	     // 时间差
	     long interdays = (end - start) / (1000 * 60 * 60 * 24);
	     return new Long(interdays).intValue();
	 }
	 
	 /**
	  * @param date
	  * @return
	  */
	  public static List<String> findYearAndMonthAndDay(String date){
		  List<String>  list = new ArrayList<String>();
		  String year = date.substring(0, 4);
		  String month = date.substring(6, 7);
		  String day = date.substring(8, 10);
		  list.add(year);
		  list.add(month);
		  list.add(day);
		  return list;
	  }
	  
	  
	  
	  /**
	   * 济南绩效导出
	   * 行编和外聘 客户经理
	   * @param filter
	   * @param response
	   * @throws Exception
	   */
		public void getExportWageData(ManagerSalaryFilter filter,HttpServletResponse response) throws Exception{
			String year  = filter.getYear();// 年份
			String month = filter.getMonth();// 月份
			String mon = Integer.parseInt(month)+"";
			
			// 计算当月的下一个月
			String nextMonth = "";
			Calendar calendar = Calendar.getInstance();
			calendar.set(Integer.parseInt(year),Integer.parseInt(month)-1,1);
			calendar.add(Calendar.MONTH, 1);
			nextMonth = calendar.get(Calendar.MONTH)+1+"";
			
			// 判断客户经理 类型
			String title ="";
			if("1".equals(filter.getManagerType())){
				 title ="济南农商行小微信贷中心行编客户经理薪酬测算表("+year+"年"+month+"月)";
			}else{
				 title ="济南农商行小微信贷中心外聘客户经理薪酬测算表("+year+"年"+month+"月)";
			}
			
			// 查询工资单
			List<ManagerSalaryForm> salaryList = managerSalaryDao.findManagerSalaryForm(filter);
			
			// 查询客户经理绩效参数
			TPerformanceParameters parameters = commonDao.queryBySql(TPerformanceParameters.class,"select * from T_PERFORMANCE_PARAMETERS ",null).get(0);
			
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
			CellRangeAddress region = new CellRangeAddress(0, 0, 0,22);
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
	        HSSFCell tmp = row.createCell((short) 18);
	        tmp.setCellValue("制表日期：");
	        CellRangeAddress reg = new CellRangeAddress(1, 1, 18,22);
	        sheet.addMergedRegion(reg);
	        tmp.setCellStyle(styleFirst);
	        
	        // excel 正文内容
	        row = sheet.createRow((int) 2);
	        // row
	        HSSFCell cell = row.createCell((short) 0);  
	        cell.setCellValue("序号");  
	        cell.setCellStyle(style);
	        	        
	        cell = row.createCell((short) 1);  
	        cell.setCellValue("管辖行");  
	        cell.setCellStyle(style);  
	        
	        cell = row.createCell((short) 2);  
	        cell.setCellValue("姓名");  
	        cell.setCellStyle(style);
	        
	        cell = row.createCell((short) 3);  
	        cell.setCellValue("岗位");  
	        cell.setCellStyle(style);
	        
	        cell = row.createCell((short) 4);  
	        cell.setCellValue(nextMonth+"月基本\n工资\n（元）");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(4, 10*256);
	        
	        cell = row.createCell((short) 5);  
	        cell.setCellValue(mon+"月基础\n任务量\n奖金\n（元）");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(5, 10*256);
	        
	        cell = row.createCell((short) 6);  
	        cell.setCellValue(mon+"月主办\n放款户\n数\n（户）");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(6, 10*256);
	        
	        cell = row.createCell((short) 7);  
	        cell.setCellValue(mon+"月主办\n放款户\n数奖金\n（元）");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(7, 10*256);
	        
	        cell = row.createCell((short) 8);  
	        cell.setCellValue(mon+"月协办\n放款户\n数\n（户）");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(8, 10*256);
	        
	        cell = row.createCell((short) 9);  
	        cell.setCellValue(mon+"月协办\n放款户\n数奖金\n（元）");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(9, 10*256);
	        
	        cell = row.createCell((short) 10);  
	        cell.setCellValue(mon+"月有效\n管户数\n（户）");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(10, 10*256);
	        
	        cell = row.createCell((short) 11);  
	        cell.setCellValue(mon+"月管户\n维护奖\n金\n（元）");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(11, 10*256);
	        
	        cell = row.createCell((short) 12);  
	        cell.setCellValue(mon+"月利润\n提成\n（元）");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(12, 10*256);
	        
	        cell = row.createCell((short) 13);  
	        cell.setCellValue(mon+"月逾期\n天数总\n数\n（天）");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(13, 10*256);
	        
	        cell = row.createCell((short) 14);  
	        cell.setCellValue(mon+"月逾期\n扣罚\n（元）");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(14, 10*256);
	        
	        cell = row.createCell((short) 15);  
	        cell.setCellValue(mon+"月业务\n量绩效\n（元）");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(15, 10*256);
	        
	        cell = row.createCell((short) 16);  
	        cell.setCellValue(mon+"月缺勤\n扣（元）");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(16, 10*256);
	        
	        cell = row.createCell((short) 17);  
	        cell.setCellValue("前期差\n错补扣\n（元）");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(17, 10*256);
	        
	        cell = row.createCell((short) 18);  
	        cell.setCellValue(mon+"月其他\n业务绩\n效\n（元）");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(18, 10*256);
	        
	        cell = row.createCell((short) 19);  
	        cell.setCellValue(mon+"月应发\n绩效\n（元）");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(19, 10*256);
	        
	        cell = row.createCell((short) 20);  
	        cell.setCellValue(nextMonth+"月应发\n薪酬\n（元）");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(20, 10*256);
	        
	        cell = row.createCell((short) 21);  
	        cell.setCellValue("风险保\n证金\n（元）");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(21, 10*256);
	        
	        cell = row.createCell((short) 22);  
	        cell.setCellValue(nextMonth+"月实发\n薪酬\n（元）");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(22, 10*256);
	        
	        
	        
	        for(int i=0;i<salaryList.size();i++){
	        	row = sheet.createRow((int) i + 3);
	        	ManagerSalaryForm salary = salaryList.get(i);
	        	row.createCell((short) 0).setCellValue(i+1);  
	        	row.createCell((short) 1).setCellValue((String) salary.getInstCode());            																	      //管辖行
	        	row.createCell((short) 2).setCellValue((String) salary.getManagerName());        																          //姓名                       
	        	row.createCell((short) 3).setCellValue("");     						          																	      //岗位  备注： 暂无  后续会设定客户经理级别 级别与基本工资挂钩                     
	        	row.createCell((short) 4).setCellValue(Double.parseDouble(salary.getBasePay()));  																          //9月基本工资（元）          
	        	row.createCell((short) 5).setCellValue(Double.parseDouble(salary.getBasicTaskBonus()));																      //8月基础任务量奖金（元） 备注:外聘客户经理所有 行编客户经理无   
	        	row.createCell((short) 6).setCellValue(Double.parseDouble(salary.getMonthLoanNum()));        														      //8月主办放款户数（户）      
	        	row.createCell((short) 7).setCellValue(Double.parseDouble(new BigDecimal(parameters.getA()).multiply(new BigDecimal(salary.getMonthLoanNum()))+""));      //8月主办放款户数奖金（元）  
	        	row.createCell((short) 8).setCellValue(Double.parseDouble(salary.getMonthTimes()));          													          //8月协办放款户数（户）      
	        	row.createCell((short) 9).setCellValue(Double.parseDouble(new BigDecimal(parameters.getB()).multiply(new BigDecimal(salary.getMonthTimes()))+""));        //8月协办放款户数奖金（元）  
	        	row.createCell((short) 10).setCellValue(Double.parseDouble(salary.getMonthEffectNum()));                                                                  //8月有效管户数（户）        
	        	row.createCell((short) 11).setCellValue(Double.parseDouble(new BigDecimal(parameters.getD()).multiply(new BigDecimal(salary.getMonthEffectNum()))+""));   //8月管户维护奖金（元）      
	        	row.createCell((short) 12).setCellValue(Double.parseDouble(salary.getProfitDraw()));         															  //8月利润提成（元）          
	        	row.createCell((short) 13).setCellValue(Double.parseDouble(salary.getMonthOverdueDays()));   														      //8月逾期天数总数（天）      
	        	row.createCell((short) 14).setCellValue(Double.parseDouble(new BigDecimal(parameters.getJ()).multiply(new BigDecimal( salary.getMonthOverdueDays()))+""));//8月逾期扣罚（元）          
	        	row.createCell((short) 15).setCellValue(Double.parseDouble(salary.getVolumePerformance()));  															  //8月业务量绩效（元）        
	        	row.createCell((short) 16).setCellValue("");  									  																		  //8月缺勤扣（元）             手填
	        	row.createCell((short) 17).setCellValue("");  									  																		  //前期差错补扣（元）        手填
	        	row.createCell((short) 18).setCellValue("");  									  																          //8月其他业务绩效（元） 手填
	        	row.createCell((short) 19).setCellValue("");  									  																		  //8月应发绩效（元）         待确定      
	        	row.createCell((short) 20).setCellValue("");  									  																		  //9月应发薪酬（元）        待确定        
	        	row.createCell((short) 21).setCellValue(Double.parseDouble(salary.getInsertPrepareAmount()));															  //风险保证金（元）           待确定      
	        	row.createCell((short) 22).setCellValue("");  									  																		  //9月实发薪酬（元）        待确定      
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
		   * 济南绩效导出 
		   * 风险岗
		   * @param filter
		   * @param response
		   * @throws Exception
		   */
		public void getExportWageDatas(ManagerSalaryFilter filter,HttpServletResponse response) throws Exception{
			String year  = filter.getYear();// 年份
			String month = filter.getMonth();// 月份
			String mon = Integer.parseInt(month)+"";
			
			// 计算当月的下一个月
			String nextMonth = "";
			Calendar calendar = Calendar.getInstance();
			calendar.set(Integer.parseInt(year),Integer.parseInt(month)-1,1);
			calendar.add(Calendar.MONTH, 1);
			nextMonth = calendar.get(Calendar.MONTH)+1+"";
			
			// 判断客户经理 类型
			String title ="济南农商行小微信贷中心风险岗薪酬测算表("+year+"年"+month+"月)";;
			/*if("1".equals(filter.getManagerType())){
				 title ="济南农商行小微信贷中心行编客户经理薪酬测算表("+year+"年"+month+"月)";
			}else if("2".equals(filter.getManagerType())){
				 title ="济南农商行小微信贷中心外聘客户经理薪酬测算表("+year+"年"+month+"月)";
			}else{
				 title ="济南农商行小微信贷中心风险岗薪酬测算表("+year+"年"+month+"月)";
			}*/
			
			// 查询工资单
			List<ManagerSalaryForm> salaryList = managerSalaryDao.findManagerSalaryForm(filter);
			
			// 查询客户经理绩效参数
			TPerformanceParameters parameters = commonDao.queryBySql(TPerformanceParameters.class,"select * from T_PERFORMANCE_PARAMETERS ",null).get(0);
			
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
			CellRangeAddress region = new CellRangeAddress(0, 0, 0,13);
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
	        HSSFCell tmp = row.createCell((short) 9);
	        tmp.setCellValue("制表日期：");
	        CellRangeAddress reg = new CellRangeAddress(1, 1, 9,13);
	        sheet.addMergedRegion(reg);
	        tmp.setCellStyle(styleFirst);
	        
	        // excel 正文内容
	        row = sheet.createRow((int) 2);
	        // row
	        HSSFCell cell = row.createCell((short) 0);  
	        cell.setCellValue("序号");  
	        cell.setCellStyle(style);
	        	        
	        cell = row.createCell((short) 1);  
	        cell.setCellValue("管辖行");  
	        cell.setCellStyle(style);  
	        
	        cell = row.createCell((short) 2);  
	        cell.setCellValue("姓名");  
	        cell.setCellStyle(style);
	        
	        cell = row.createCell((short) 3);  
	        cell.setCellValue("岗位");  
	        cell.setCellStyle(style);
	        
	        cell = row.createCell((short) 4);  
	        cell.setCellValue(nextMonth+"月基本\n工资\n（元）");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(4, 10*256);
	        
	        cell = row.createCell((short) 5);  
	        cell.setCellValue("岗位津贴");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(4, 10*256);
	        
	        cell = row.createCell((short) 6);  
	        cell.setCellValue(mon+"当月参与审贷会\n审议\n通过\n笔数(笔)");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(4, 10*256);
	        
	        cell = row.createCell((short) 7);  
	        cell.setCellValue(mon+"月业务\n量绩效\n（元）");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(15, 10*256);
	        
	        cell = row.createCell((short) 8);  
	        cell.setCellValue(mon+"月缺勤\n扣（元）");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(16, 10*256);
	        
	        cell = row.createCell((short) 9);  
	        cell.setCellValue("前期差\n错补扣\n（元）");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(17, 10*256);
	        
	        cell = row.createCell((short) 10);  
	        cell.setCellValue(mon+"月其他\n业务绩\n效\n（元）");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(18, 10*256);
	        
	        cell = row.createCell((short) 11);  
	        cell.setCellValue(mon+"月应发\n绩效\n（元）");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(19, 10*256);
	        
	        cell = row.createCell((short) 12);  
	        cell.setCellValue(nextMonth+"月应发\n薪酬\n（元）");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(20, 10*256);
	       
	        cell = row.createCell((short) 13);  
	        cell.setCellValue(nextMonth+"月实发\n薪酬\n（元）");  
	        cell.setCellStyle(style);
	        sheet.setColumnWidth(22, 10*256);
	        
	        
	        
	        for(int i=0;i<salaryList.size();i++){
	        	row = sheet.createRow((int) i + 3);
	        	ManagerSalaryForm salary = salaryList.get(i);
	        	row.createCell((short) 0).setCellValue(i+1);  
	        	row.createCell((short) 1).setCellValue((String) salary.getInstCode());                       //管辖行
	        	row.createCell((short) 2).setCellValue((String) salary.getManagerName());                    //姓名                       
	        	row.createCell((short) 3).setCellValue("");     						          		     //岗位  备注： 暂无  后续会设定客户经理级别 级别与基本工资挂钩                     
	        	row.createCell((short) 4).setCellValue(Double.parseDouble(salary.getBasePay()));             //9月基本工资（元）  
	        	row.createCell((short) 5).setCellValue(Double.parseDouble(salary.getSubsidies()));			 //岗位津贴
	        	row.createCell((short) 6).setCellValue(Double.parseDouble(salary.getAuditNum()));			 //当月参与审贷会审议通过笔数
	        	row.createCell((short) 7).setCellValue(Double.parseDouble(salary.getVolumePerformance()));   //8月业务量绩效（元）        
	        	row.createCell((short) 8).setCellValue("");  									             //8月缺勤扣（元）             手填
	        	row.createCell((short) 9).setCellValue("");  									             //前期差错补扣（元）        手填
	        	row.createCell((short) 10).setCellValue("");  									             //8月其他业务绩效（元） 手填
	        	row.createCell((short) 11).setCellValue("");  									             //8月应发绩效（元）         待确定      
	        	row.createCell((short) 12).setCellValue("");  									             //9月应发薪酬（元）         待确定        
	        	row.createCell((short) 13).setCellValue("");  									             //9月实发薪酬（元）         待确定      
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
	//----------------------------------------------济南绩效end----------------------------------------------------//
	
}
