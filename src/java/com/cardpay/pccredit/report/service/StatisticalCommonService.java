package com.cardpay.pccredit.report.service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.common.PieJsonData;
import com.cardpay.pccredit.customer.filter.FkRankingFilter;
import com.cardpay.pccredit.jnpad.model.MonthlyStatisticsModel;
import com.cardpay.pccredit.jnpad.service.MonthlyStatisticsService;
import com.cardpay.pccredit.manager.form.ManagerPerformmanceForm;
import com.cardpay.pccredit.manager.service.ManagerPerformmanceService;
import com.cardpay.pccredit.report.dao.StatisticalCommonDao;
import com.cardpay.pccredit.report.model.NameValueRecord;

/**
 * @author chenzhifang
 *
 * 2014-12-18下午3:38:35
 */
@Service
public class StatisticalCommonService {
	@Autowired
	private StatisticalCommonDao statisticalCommonDao;
	@Autowired
	private ManagerPerformmanceService managerPerformmanceService;
	/**
     * 统计当前进件状况
     * @param filter
     * @return
     */
	public List<NameValueRecord> statisticalApplicationStatus(String id){
		List<NameValueRecord> list=new ArrayList<NameValueRecord>();
		NameValueRecord  statisticalApplicationStatussum=statisticalCommonDao.statisticalApplicationStatussum("audit",id);
	
		if(statisticalApplicationStatussum!=null){
			System.out.println("1");
			if(statisticalApplicationStatussum.getValue()==null || statisticalApplicationStatussum.getValue()==""){
				statisticalApplicationStatussum.setValue("0");
			}
			statisticalApplicationStatussum.setName("已申请");
			list.add(0, statisticalApplicationStatussum);
		}
		NameValueRecord  statisticalApplicationStatussum1=statisticalCommonDao.statisticalApplicationStatussum("refuse",id);
		if(statisticalApplicationStatussum1!=null){
		if(statisticalApplicationStatussum1.getValue()==null || statisticalApplicationStatussum1.getValue()==""){
			statisticalApplicationStatussum1.setValue("0");
		}
		statisticalApplicationStatussum1.setName("被拒绝");
		list.add(0, statisticalApplicationStatussum1);
		}
		NameValueRecord  statisticalApplicationStatussum2=statisticalCommonDao.statisticalApplicationStatussum("returnedToFirst",id);
		if(statisticalApplicationStatussum2!=null){
		if(statisticalApplicationStatussum2.getValue()==null || statisticalApplicationStatussum2.getValue()==""){
			statisticalApplicationStatussum2.setValue("0");
		}
		statisticalApplicationStatussum2.setName("被退回");
		list.add(0, statisticalApplicationStatussum2);}
		NameValueRecord  statisticalApplicationStatussum3=statisticalCommonDao.statisticalApplicationStatussum1(id);
		if(statisticalApplicationStatussum3!=null){
		if(statisticalApplicationStatussum3.getValue()==null || statisticalApplicationStatussum3.getValue()==""){
			statisticalApplicationStatussum3.setValue("0");
		}
		statisticalApplicationStatussum3.setName("通过");
		list.add(0, statisticalApplicationStatussum3);
		}
		
		
		
		
		
		
	/*	List<NameValueRecord> list= statisticalCommonDao.statisticalApplicationStatus();
		for(int a=0;a<list.size();a++){
			try {
				if(list.get(a).getValue()==null || list.get(a).getValue()==""){
					list.get(a).setValue("0");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		return list;
	}
	
	
	/**
     * 统计当前进件状况json
     * @param filter
     * @return
     */
	public String getApplicationStatusJson(String id){
		List<PieJsonData> pList = getPieJsonData(statisticalApplicationStatus(id),id);
		if(pList.size()>0){
			PieJsonData pieJsonData = pList.get(0);
			pieJsonData.setSliced(true);
			pieJsonData.setSelected(true);
		}
		
		return JSONArray.fromObject(pList).toString();
	}
	
	/**
     * 统计当前贷款状况
     * @param filter
     * @return
     */
	public List<NameValueRecord> statisticalCreditStatus(){
		return statisticalCommonDao.statisticalCreditStatus();
	}
	
	/**
     * 统计当前贷款状况json
     * @param filter
     * @return
     */
	public String getCreditStatusJson(String Id){
		List<PieJsonData> pList = getPieJsonData(statisticalCreditStatus(),Id);
		PieJsonData pieJsonData = pList.get(1);
		pieJsonData.setSliced(true);
		pieJsonData.setSelected(true);
		
		DecimalFormat df = new DecimalFormat("####.0000");
		for(int i =0; i < pList.size(); i++){
			pieJsonData = pList.get(i);
			pieJsonData.setY(Double.valueOf(df.format(pieJsonData.getY())));
		}
		
		return JSONArray.fromObject(pList).toString();
	}
	
	/**
     * 统计当前卡片状况
     * @param filter
     * @return
     */
	public List<NameValueRecord> statisticalCardStatus(){
		return statisticalCommonDao.statisticalCardStatus();
	}
	
	/**
     * 统计当前卡片状况柱状图标签
     * @param filter
     * @return
     */
	public String getCardStatusCategoriesJson(List<NameValueRecord> records){
		List<String> list = new ArrayList<String>();
		for(NameValueRecord nameValueRecord : records){
			list.add(nameValueRecord.getName());
		}
		return JSONArray.fromObject(list).toString();
	}
	
	/**
     * 统计当前卡片状况柱状图标签
     * @param filter
     * @return
     */
	public String getCardStatusValuesJson(List<NameValueRecord> records){
		List<Double> list = new ArrayList<Double>();
		for(NameValueRecord nameValueRecord : records){
			if(StringUtils.isNotEmpty(nameValueRecord.getValue())){
				list.add(Double.valueOf(nameValueRecord.getValue()));
			}else{
				list.add(0d);
			}
			
		}
		return JSONArray.fromObject(list).toString();
	}
	
	public List<PieJsonData> getPieJsonData(List<NameValueRecord> list,String id){
		List<NameValueRecord> alist = statisticalCommonDao.statisticalApplicationStatusAmt(id);
		for(int a=0;a<alist.size();a++){
			if(alist.get(a).getValue()=="" || alist.get(a).getValue()==null){
				alist.get(a).setValue("0");
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		for(NameValueRecord nameValueRecord1 : alist){
			map.put(nameValueRecord1.getName(), nameValueRecord1.getValue());
		}
		
		List<PieJsonData> pList= new ArrayList<PieJsonData>();
		for(NameValueRecord nameValueRecord : list){
			PieJsonData pieJsonData = new PieJsonData();
			pieJsonData.setName(nameValueRecord.getName());
			
			if(StringUtils.isNotEmpty(nameValueRecord.getValue())){
				pieJsonData.setY(Double.valueOf(nameValueRecord.getValue()));
			}else{
				pieJsonData.setY(0);
			}
			pieJsonData.setSliced(false);
			pieJsonData.setSelected(false);
			pList.add(pieJsonData);
		}
		return pList;
	}
	
	
	
	public String getOrganApplicationAuditNumJson(){
		List<Double> list = new ArrayList<Double>();
		List<NameValueRecord> records = statisticalCommonDao.statisticalAuditStatus();
		for(NameValueRecord nameValueRecord : records){
			if(StringUtils.isNotEmpty(nameValueRecord.getValue())){
				list.add(Double.valueOf(nameValueRecord.getValue()));
			}else{
				list.add(0d);
			}
		}
		return JSONArray.fromObject(list).toString();
	}
	
	public String getOrganApplicationApprovedNumJson(){
		List<Double> list = new ArrayList<Double>();
		List<NameValueRecord> records = statisticalCommonDao.statisticalApprovedStatus();
		for(NameValueRecord nameValueRecord : records){
			if(StringUtils.isNotEmpty(nameValueRecord.getValue())){
				list.add(Double.valueOf(nameValueRecord.getValue()));
			}else{
				list.add(0d);
			}
		}
		return JSONArray.fromObject(list).toString();
	}
	
	
	
	
	public String statisticaljine(){
		List<Double> list = new ArrayList<Double>();
		List<NameValueRecord> records = statisticalCommonDao.statisticaljine();
		List<NameValueRecord> records3 = statisticalCommonDao.statisticalsxorgan3();
		List<NameValueRecord> records4 = statisticalCommonDao.statisticalsxorgan4();
		Double value=(Double.valueOf(records.get(0).getValue())+Double.valueOf(records3.get(0).getValue())+Double.valueOf(records4.get(0).getValue()));
		records.get(0).setValue(value.toString());
		for(NameValueRecord nameValueRecord : records){
			if(StringUtils.isNotEmpty(nameValueRecord.getValue())){
				list.add(Double.valueOf(nameValueRecord.getValue()));
			}else{
				list.add(0d);
			}
		}
		return JSONArray.fromObject(list).toString();
	}
	
	public String statisticalye(String team){
		NameValueRecord result=statisticalCommonDao.statisticaljineYe(team);
		NameValueRecord result1=statisticalCommonDao.statisticaljineYe1(team);
		List<Double> list = new ArrayList<Double>();
		Double value=Double.valueOf(result.getValue())+Double.valueOf(result.getValue());
		Double name=Double.valueOf(result.getName())+Double.valueOf(result.getName());
		list.add(value);
		list.add(name);
		return JSONArray.fromObject(list).toString();
	}
	public String statisticalsxorgan(){
		List<Double> list = new ArrayList<Double>();
		List<NameValueRecord> records = statisticalCommonDao.statisticalsxorgan();
		List<NameValueRecord> records1 = statisticalCommonDao.statisticalsxorgan1();
		List<NameValueRecord> records2 = statisticalCommonDao.statisticalsxorgan2();
		list.add(0, Double.valueOf(records.get(0).getValue())+Double.valueOf(records1.get(0).getValue())+Double.valueOf(records2.get(0).getValue()));
		list.add(1, Double.valueOf(records.get(1).getValue())+Double.valueOf(records1.get(1).getValue())+Double.valueOf(records2.get(1).getValue()));
		list.add(2, Double.valueOf(records.get(2).getValue())+Double.valueOf(records1.get(2).getValue())+Double.valueOf(records2.get(2).getValue()));
/*		System.out.println(records.get(0).getValue());
		System.out.println(records.get(1).getValue());
		System.out.println(records.get(2).getValue());
		
		System.out.println(records1.get(0).getValue());
		System.out.println(records1.get(1).getValue());
		System.out.println(records.get(2).getValue());
		
		System.out.println(records2.get(0).getValue());
		System.out.println(records2.get(1).getValue());
		System.out.println(records2.get(2).getValue());*/
	/*	for(NameValueRecord nameValueRecord : records){
			if(StringUtils.isNotEmpty(nameValueRecord.getValue())){
				list.add(Double.valueOf(nameValueRecord.getValue()));
			}else{
				list.add(0d);
			}
		}*/
		return JSONArray.fromObject(list).toString();
	}
	
	public String statisticalyqorgan(){
		List<Double> list = new ArrayList<Double>();
		List<NameValueRecord> records = statisticalCommonDao.statisticalyqorgan();
		for(NameValueRecord nameValueRecord : records){
			if(StringUtils.isNotEmpty(nameValueRecord.getValue())){
				list.add(Double.valueOf(nameValueRecord.getValue()));
			}else{
				list.add(0d);
			}
		}
		return JSONArray.fromObject(list).toString();
	}
	
	public String statisticalblorgan(){
		List<Double> list = new ArrayList<Double>();
		List<NameValueRecord> records = statisticalCommonDao.statisticalblorgan();
		for(NameValueRecord nameValueRecord : records){
			if(StringUtils.isNotEmpty(nameValueRecord.getValue())){
				list.add(Double.valueOf(nameValueRecord.getValue()));
			}else{
				list.add(0d);
			}
		}
		return JSONArray.fromObject(list).toString();
	}

//放款排名
	public List<FkRankingFilter> queryFkRanking() {
		// TODO Auto-generated method stub
		return statisticalCommonDao.queryFkRanking();
	}
	@Autowired
	private MonthlyStatisticsService StatisticsService;
	//统计月季贷款总额
	public String tjsydk(String team){
		 MonthlyStatisticsModel StatisticsModel =new MonthlyStatisticsModel();
		 Date d = new Date();  
	      SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");  
	      String dateNowStr = sdf1.format(d);  
	      StatisticsModel.setCustomeryeah(Integer.parseInt(dateNowStr));
	      StatisticsModel.setTeam(team);
	      MonthlyStatisticsModel result=StatisticsService.selectTeamYear(StatisticsModel);
	List<Double> list = new ArrayList<Double>();
	if(result!=null){
		
	list.add(0, Double.valueOf(result.getCustomerJanuary()));
	list.add(1, Double.valueOf(result.getCustomerFebruary()));
	list.add(2, Double.valueOf(result.getCustomerMarch()));
	list.add(3, Double.valueOf(result.getCustomerApril()));
	list.add(4, Double.valueOf(result.getCustomerMay()));
	list.add(5, Double.valueOf(result.getCustomerJune()));
	list.add(6, Double.valueOf(result.getCustomerJuly()));
	list.add(7, Double.valueOf(result.getCustomerAugust()));
	list.add(8, Double.valueOf(result.getCustomerSeptember()));
	list.add(9, Double.valueOf(result.getCustomerOctober()));
	list.add(10, Double.valueOf(result.getCustomerNovember()));
	list.add(11, Double.valueOf(result.getCustomerDecember()));
	MonthlyStatisticsModel result1=StatisticsService.selectTeamYear(StatisticsModel);
    double money=result1.getTotalAmount();
	list.add(12, Double.valueOf(money));
	}
	return JSONArray.fromObject(list).toString();
	}
}
