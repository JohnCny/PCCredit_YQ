package com.cardpay.pccredit.jnpad.dao;

import java.util.List;

import com.cardpay.pccredit.manager.filter.RetrainingFilter;
import com.cardpay.pccredit.manager.model.Retraining;
import com.wicresoft.util.annotation.Mapper;

@Mapper
public interface JnpadRetrainingDao {

	
	public List<Retraining> findRetrainingsByFilter(RetrainingFilter filter);
	
}
