package com.cardpay.pccredit.customer.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cardpay.pccredit.intopieces.web.LocalImageForm;
import com.cardpay.pccredit.jnpad.model.JnpadPcCustomer;
import com.wicresoft.util.annotation.Mapper;
@Mapper
public interface PcCustomerInfoDao {
	List<JnpadPcCustomer> selectPCustomer(@Param("userId") String userId);
	int insertPCustomer(JnpadPcCustomer JnpadPcCustomer);
	JnpadPcCustomer selectCardId(@Param("cardid") String cardid);
	void insertPCImage(LocalImageForm LocalImageForm);
	List<LocalImageForm> selectUri(@Param("cardid") String cardid);
	LocalImageForm selectUriById(@Param("id") String id);
	void deleteId(@Param("id") String id);
	List<JnpadPcCustomer>findAllqyjl();
	List<JnpadPcCustomer>findxzz(@Param("id") String id);
	List<JnpadPcCustomer>findSpRy(@Param("userId") String userId);
}
