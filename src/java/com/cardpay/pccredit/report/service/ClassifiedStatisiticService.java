package com.cardpay.pccredit.report.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.report.dao.ClassifiedStatisiticDao;
import com.cardpay.pccredit.report.filter.ClassfiedFilter;
import com.cardpay.pccredit.report.model.ClassifiedForm;
import com.wicresoft.jrad.base.database.model.QueryResult;

@Service
public class ClassifiedStatisiticService {
	
	@Autowired
	 private ClassifiedStatisiticDao classifiedstatisiticdao;
	
	 public QueryResult<ClassifiedForm> findloansInfoByfilter(ClassfiedFilter filter) {

		 List<ClassifiedForm> items = classifiedstatisiticdao.findloansInfoByfilter(filter);
			int totalCount = classifiedstatisiticdao.findloansInfoSizeByfilter(filter);
			QueryResult<ClassifiedForm> qs = new QueryResult<ClassifiedForm>(totalCount, items);
			return qs;
	}

	
	
	 public QueryResult<ClassifiedForm> findlExcelDataoansInfoByfilter(ClassfiedFilter filter) {

		 List<ClassifiedForm> items = classifiedstatisiticdao.findlExcelDataoansInfoByfilter(filter);
			int totalCount = classifiedstatisiticdao.findloansInfoSizeByfilter(filter);
			QueryResult<ClassifiedForm> qs = new QueryResult<ClassifiedForm>(totalCount, items);
			return qs;
	}
	
}
