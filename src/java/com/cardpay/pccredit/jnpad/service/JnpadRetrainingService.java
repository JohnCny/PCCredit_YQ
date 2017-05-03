package com.cardpay.pccredit.jnpad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.jnpad.dao.JnpadRetrainingDao;
import com.cardpay.pccredit.manager.filter.RetrainingFilter;
import com.cardpay.pccredit.manager.model.Retraining;
import com.wicresoft.jrad.base.database.model.QueryResult;

@Service
public class JnpadRetrainingService {

	@Autowired
	JnpadRetrainingDao retrainingDao;
	/**
	 * 过滤查询
	 * @param filter
	 * @return
	 */
	public List<Retraining> findRetrainingByFilter(RetrainingFilter filter) {
		List<Retraining> list = retrainingDao.findRetrainingsByFilter(filter);
		
		return list;
	}
}
