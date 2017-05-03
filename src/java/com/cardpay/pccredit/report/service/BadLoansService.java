package com.cardpay.pccredit.report.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.report.dao.BadLoansDao;
import com.cardpay.pccredit.report.filter.BadLoansFilter;
import com.cardpay.pccredit.report.model.BadLoansInfo;
import com.wicresoft.jrad.base.database.model.QueryResult;

@Service
public class BadLoansService {

	@Autowired
	BadLoansDao badLoansDao;
	
	public QueryResult<BadLoansInfo> findBadloansByfilter(BadLoansFilter filter) {
		// TODO Auto-generated method stub
		List<BadLoansInfo> items = badLoansDao.findBadloansByfilter(filter);
		int totalCount = badLoansDao.findBadloansSizeByfilter(filter);
		QueryResult<BadLoansInfo> qs = new QueryResult<BadLoansInfo>(totalCount, items);
		return qs;
	}
	public QueryResult<BadLoansInfo> findBadloansDataByfilter(BadLoansFilter filter) {
		// TODO Auto-generated method stub
		List<BadLoansInfo> items = badLoansDao.findBadloansDataByfilter(filter);
		int totalCount = badLoansDao.findBadloansSizeByfilter(filter);
		QueryResult<BadLoansInfo> qs = new QueryResult<BadLoansInfo>(totalCount, items);
		return qs;
	}
	
}
