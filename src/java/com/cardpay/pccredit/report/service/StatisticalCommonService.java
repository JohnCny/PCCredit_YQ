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
	public List<NameValueRecord> statisticalApplicationStatus(){
		List<NameValueRecord> list= statisticalCommonDao.statisticalApplicationStatus();
		for(int a=0;a<list.size();a++){
			try {
				if(list.get(a).getValue()==null || list.get(a).getValue()==""){
					list.get(a).setValue("0");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}
	
	
	/**
     * 统计当前进件状况json
     * @param filter
     * @return
     */
	public String getApplicationStatusJson(){
		List<PieJsonData> pList = getPieJsonData(statisticalApplicationStatus());
		PieJsonData pieJsonData = pList.get(0);
		pieJsonData.setSliced(true);
		pieJsonData.setSelected(true);
		
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
	public String getCreditStatusJson(){
		List<PieJsonData> pList = getPieJsonData(statisticalCreditStatus());
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
	
	public List<PieJsonData> getPieJsonData(List<NameValueRecord> list){
		/*//获取各种状况的金额
		List<NameValueRecord> alist = statisticalCommonDao.statisticalApplicationStatusAmt();
		for(int a=0;a<alist.size();a++){
			if(alist.get(a).getValue()=="" || alist.get(a).getValue()==null){
				alist.get(a).setValue("0");
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		for(NameValueRecord nameValueRecord : alist){
			map.put(nameValueRecord.getName(), nameValueRecord.getValue());
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
			
			//q-金额
			pieJsonData.setQ(Double.valueOf(map.get(nameValueRecord.getId())+""));
			
			pieJsonData.setSliced(false);
			pieJsonData.setSelected(false);
			pList.add(pieJsonData);
		}
		return pList;*/List<PieJsonData> pList= new ArrayList<PieJsonData>();
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
		for(NameValueRecord nameValueRecord : records){
			if(StringUtils.isNotEmpty(nameValueRecord.getValue())){
				list.add(Double.valueOf(nameValueRecord.getValue()));
			}else{
				list.add(0d);
			}
		}
		return JSONArray.fromObject(list).toString();
	}
	
	public String statisticalsxorgan(){
		List<Double> list = new ArrayList<Double>();
		List<NameValueRecord> records = statisticalCommonDao.statisticalsxorgan();
		for(NameValueRecord nameValueRecord : records){
			if(StringUtils.isNotEmpty(nameValueRecord.getValue())){
				list.add(Double.valueOf(nameValueRecord.getValue()));
			}else{
				list.add(0d);
			}
		}
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
	public String tjsydk(){
		 MonthlyStatisticsModel StatisticsModel =new MonthlyStatisticsModel();
		 Date d = new Date();  
	      SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");  
	      String dateNowStr = sdf1.format(d);  
	      StatisticsModel.setCustomeryeah(Integer.parseInt(dateNowStr));
	      List<MonthlyStatisticsModel> result=StatisticsService.selectTeamYear(StatisticsModel);
	List<Double> list = new ArrayList<Double>();
	list.add(0, Double.valueOf(result.get(0).getCustomerJanuary()));
	list.add(1, Double.valueOf(result.get(0).getCustomerFebruary()));
	list.add(2, Double.valueOf(result.get(0).getCustomerMarch()));
	list.add(3, Double.valueOf(result.get(0).getCustomerApril()));
	list.add(4, Double.valueOf(result.get(0).getCustomerMay()));
	list.add(5, Double.valueOf(result.get(0).getCustomerJune()));
	list.add(6, Double.valueOf(result.get(0).getCustomerJuly()));
	list.add(7, Double.valueOf(result.get(0).getCustomerAugust()));
	list.add(8, Double.valueOf(result.get(0).getCustomerSeptember()));
	list.add(9, Double.valueOf(result.get(0).getCustomerOctober()));
	list.add(10, Double.valueOf(result.get(0).getCustomerNovember()));
	list.add(11, Double.valueOf(result.get(0).getCustomerDecember()));
	List<MonthlyStatisticsModel> result1=StatisticsService.selectTeamYear(StatisticsModel);
    double money=result1.get(0).getTotalAmount();
	list.add(12, Double.valueOf(money));
	return JSONArray.fromObject(list).toString();
	}
}
