package com.cardpay.pccredit.report.dao;

import java.util.List;

import com.cardpay.pccredit.report.filter.ClassfiedFilter;
import com.cardpay.pccredit.report.model.ClassifiedForm;
import com.wicresoft.util.annotation.Mapper;

@Mapper
public interface ClassifiedStatisiticDao {

	List<ClassifiedForm> findloansInfoByfilter(ClassfiedFilter filter);

	int findloansInfoSizeByfilter(ClassfiedFilter filter);

	List<ClassifiedForm> findlExcelDataoansInfoByfilter(ClassfiedFilter filter);

}
