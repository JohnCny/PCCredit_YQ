package com.cardpay.pccredit.dateplan.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cardpay.pccredit.dateplan.model.JBUser;
import com.cardpay.pccredit.dateplan.model.datePlanModel;
import com.cardpay.pccredit.dateplan.model.dateTimeModel;
import com.cardpay.pccredit.manager.web.ManagerBelongMapForm;
import com.wicresoft.util.annotation.Mapper;

/**
 * @author chenzhifang
 *
 * 2014-11-11上午9:43:57
 */
@Mapper
public interface SysUserDao {
	List<ManagerBelongMapForm>selectAllGxUser();
	List<JBUser>selectDepart(@Param(value = "id")String id);
	int insertRw(datePlanModel planmodel);
	List<datePlanModel>selectAllTime(@Param(value = "id")String id);
	datePlanModel selectAllTime1(@Param(value = "id")String id);
	List<datePlanModel>selectByUser(@Param(value = "id")String id);
	datePlanModel selectByUser1(@Param(value = "id")String id);
	int updateRw(@Param(value = "id")String id);
	List<datePlanModel>selectdqsl(@Param(value = "id")String id);
	List<datePlanModel>selectmbsl(@Param(value = "id")String id);
	List<datePlanModel>selectwhsl(@Param(value = "id")String id);
	List<datePlanModel>selectdhsl(@Param(value = "id")String id);
	JBUser selectzgUser(@Param(value = "id")String id);
	List<dateTimeModel>selectByTime(@Param(value = "id")String id);
	List<JBUser>selectAlluser(JBUser filter);
	int selectzgUserCount();
}
