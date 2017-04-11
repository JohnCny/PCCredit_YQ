package com.cardpay.pccredit.report.filter;

import com.wicresoft.jrad.base.web.filter.BaseQueryFilter;

public class BadLoansFilter extends BaseQueryFilter {

	
		// 起始日期
		private String startDate;
		// 截止日期
		private String endDate;
		//证件号码
		private String cardId;
		//客户经理姓名
		private String managerName;
		
		
		public String getStartDate() {
			return startDate;
		}
		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}
		public String getEndDate() {
			return endDate;
		}
		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}
		public String getCardId() {
			return cardId;
		}
		public void setCardId(String cardId) {
			this.cardId = cardId;
		}
		public String getManagerName() {
			return managerName;
		}
		public void setManagerName(String managerName) {
			this.managerName = managerName;
		}
		
		
}
