package com.cardpay.pccredit.dateplan.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.dateplan.dao.SysUserDao;
import com.cardpay.pccredit.dateplan.model.JBUser;
import com.cardpay.pccredit.dateplan.model.datePlanModel;
import com.cardpay.pccredit.dateplan.model.dateTimeModel;
import com.cardpay.pccredit.intopieces.filter.IntoPiecesFilter;
import com.cardpay.pccredit.intopieces.model.IntoPieces;
import com.cardpay.pccredit.manager.web.ManagerBelongMapForm;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.base.database.model.QueryResult;

@Service
public class SysUserService {
	@Autowired
	private SysUserDao UserDao;
	
	public List<ManagerBelongMapForm>selectAllGxUser(){
		return UserDao.selectAllGxUser();
	}
	public List<JBUser>selectDepart(@Param(value = "id")String id){
		return UserDao.selectDepart(id);
	}
	public int insertRw(datePlanModel planmodel){
		return UserDao.insertRw(planmodel);
	}
	public List<datePlanModel>selectAllTime(@Param(value = "id")String id){
		return UserDao.selectAllTime(id);
	}
	public List<datePlanModel>selectByUser(@Param(value = "id")String id){
		return UserDao.selectByUser(id);
	}
	public datePlanModel selectByUser1(@Param(value = "id")String id){
		return UserDao.selectByUser1(id);
	}
	public int updateRw(@Param(value = "id")String id){
		return UserDao.updateRw(id);
	}
	public List<datePlanModel>selectdqsl(@Param(value = "id")String id){
		return UserDao.selectdqsl(id);
	}
	public List<datePlanModel>selectmbsl(@Param(value = "id")String id){
		return UserDao.selectmbsl(id);
	}
	public List<datePlanModel>selectwhsl(@Param(value = "id")String id){
		return UserDao.selectwhsl(id);
	}
	public JBUser selectzgUser(@Param(value = "id")String id){
		return UserDao.selectzgUser(id);
	}
	public List<datePlanModel>selectdhsl(@Param(value = "id")String id){
		return UserDao.selectdhsl(id);
	}
	public List<dateTimeModel>selectByTime(@Param(value = "id")String id){
		return UserDao.selectByTime(id);
	}
	public datePlanModel selectAllTime1(@Param(value = "id")String id){
		return UserDao.selectAllTime1(id);
	}
	
	public QueryResult<JBUser>selectAlluser(JBUser filter){
		List<JBUser> result =  UserDao.selectAlluser(filter);
		int count=UserDao.selectzgUserCount();
		Integer rwzs=0;
		Integer wcrwzs=0;
		Integer dqsll=0;
		Integer mbsll=0;
		Integer whsll=0;
		Integer dhsll=0;
		List list=new ArrayList<JBUser>();
		for(int a=0;a<result.size();a++){
			JBUser user=new JBUser();
			user.setName(result.get(a).getName());
			//1.查看当前客户经理是否布置任务
			Date date=new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String newDate = sdf.format(date).substring(0, 10);
				datePlanModel time=UserDao.selectAllTime1(result.get(a).getId());
				if(time!=null){
						if(sdf.format(time.getZdtime()).substring(0, 10).trim().equals(newDate)){
							user.setZt("已下达任务");
						}else{
							user.setZt("未下达任务");
						}
					
				}else{
					user.setZt("未下达任务");
				}
			
			//如果是已经下达任务查出任务数量以及完成数量
			if(user.getZt()=="已下达任务"){
				//a.查出任务总数
				List<datePlanModel> resul=UserDao.selectByUser(result.get(a).getId());
				for(int b=0;b<resul.size();b++){
					String newDate1 = sdf.format(resul.get(b).getZdtime());
					if(newDate1.substring(0,10).trim().equals(newDate)){
						rwzs=resul.get(b).getDcsl()+resul.get(b).getMbsl()+resul.get(b).getDhsl()+resul.get(b).getWhsl();
					}
				}
				//b.查出完成任务总数
			
					List<datePlanModel> result0=UserDao.selectdqsl(result.get(a).getId());
					if(result0.size()>0){
					for(int aa=0;aa<result0.size();aa++){
						String newDate1 = sdf.format(result0.get(aa).getZdtime());
						if(newDate1.substring(0, 10).trim().equals(newDate)){
							dqsll+=1;
						}
					}}
					List<datePlanModel> result1=UserDao.selectmbsl(result.get(a).getId());
					if(result1.size()>0){
					for(int as=0;as<result1.size();as++){
						String newDate1 = sdf.format(result1.get(as).getZdtime());
						
						if(newDate1.substring(0, 10).trim().equals(newDate)){
							mbsll+=1;
						}
					}}
					List<datePlanModel> result2=UserDao.selectwhsl(result.get(a).getId());
					if(result2.size()>0){
					for(int ad=0;ad<result2.size();ad++){
						String newDate1 = sdf.format(result2.get(ad).getZdtime());
						if(newDate1.substring(0, 10).trim().equals(newDate)){
							whsll+=1;
						}
					}}
					List<datePlanModel> result3=UserDao.selectdhsl(result.get(a).getId());
					if(result3.size()>0){
						for(int af=0;af<result3.size();af++){
							String newDate1 = sdf.format(result3.get(af).getZdtime());
							if(newDate1.substring(0, 10).trim().equals(newDate)){
								dhsll+=1;
							}
						}
					}
					wcrwzs=dhsll+whsll+mbsll+dqsll;
					user.setWcrezl(wcrwzs);
					user.setRezl(rwzs);
					if(user.getRezl()>user.getWcrezl()){
						user.setRebfl(user.getWcrezl()+"/"+user.getRezl());
					}else{
						user.setRebfl("1");
					}
					list.add(a, user);
					rwzs=0;
					wcrwzs=0;
					 dqsll=0;
					 mbsll=0;
					 whsll=0;
					 dhsll=0;
			}else{
				//2.查出未下达任务经理当日完成的数量
				rwzs=0;
			
					List<datePlanModel> result0=UserDao.selectdqsl(result.get(a).getId());
					if(result0.size()>0){
					for(int aa=0;aa<result0.size();aa++){
						String newDate1 = sdf.format(result0.get(aa).getZdtime());
						if(newDate.substring(0, 10).trim().equals(newDate)){
							dqsll+=1;
						}
					}}
					List<datePlanModel> result1=UserDao.selectmbsl(result.get(a).getId());
					if(result1.size()>0){
					for(int as=0;as<result1.size();as++){
						String newDate1 = sdf.format(result1.get(as).getZdtime());
						if(newDate1.substring(0, 10).trim().equals(newDate)){
							mbsll+=1;
						}
					}}
					List<datePlanModel> result2=UserDao.selectwhsl(result.get(a).getId());
					if(result2.size()>0){
					for(int ad=0;ad<result2.size();ad++){
							String newDate1 = sdf.format(result2.get(ad).getZdtime());
							if(newDate1.substring(0, 10).trim().equals(newDate)){
								whsll+=1;
						}
					}}
					List<datePlanModel> result3=UserDao.selectdhsl(result.get(a).getId());
					if(result3.size()>0){
						for(int af=0;af<result3.size();af++){
							String newDate1 = sdf.format(result3.get(af).getZdtime());
							if(newDate1.substring(0, 10).trim().equals(newDate)){
								dhsll+=1;
							}
						}
					}
					wcrwzs=dhsll+whsll+mbsll+dqsll;
					user.setWcrezl(wcrwzs);
					user.setRezl(rwzs);
					user.setRebfl("待定");
			
						
			}
			list.add(a, user);
			rwzs=0;
			wcrwzs=0;
			 dqsll=0;
			 mbsll=0;
			 whsll=0;
			 dhsll=0;
		}
		QueryResult<JBUser> qs = new QueryResult<JBUser>(count, list);
		return qs;
	}

}
