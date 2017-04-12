package com.cardpay.pccredit.report.dao;

import java.util.List;

import com.cardpay.pccredit.report.filter.BadLoansFilter;
import com.cardpay.pccredit.report.model.BadLoansInfo;
import com.wicresoft.util.annotation.Mapper;

@Mapper
public interface BadLoansDao {
	//查询不良数量
	public int findBadloansSizeByfilter(BadLoansFilter filter);
	//查询不良
	public List<BadLoansInfo> findBadloansByfilter(BadLoansFilter filter);
	//excle数据
	public List<BadLoansInfo> findBadloansDataByfilter(BadLoansFilter filter);

}
