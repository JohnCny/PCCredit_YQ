package com.cardpay.pccredit.dateplan.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.dateplan.dao.CustomerTsdkmxDao;
import com.cardpay.pccredit.dateplan.model.CustomerTsdkmxModel;
import com.cardpay.pccredit.dateplan.model.DisplayUser;
import com.cardpay.pccredit.dateplan.model.JBUser;
import com.cardpay.pccredit.dateplan.service.CustomerTsdkmxService;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.controller.BaseController;
import com.wicresoft.jrad.base.web.result.JRadPagedQueryResult;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.util.spring.Beans;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;

@Controller
public class CustomerTsdkmxController extends BaseController{
	@Autowired
	private CustomerTsdkmxService TsdkmxService;
	
	/**
	 *添加并查询随借随还贷款明细
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "creat.page", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView view1(@ModelAttribute CustomerTsdkmxModel filter, HttpServletRequest request) {
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		CustomerTsdkmxModel model=new CustomerTsdkmxModel();
		model.setUSERID(user.getId());
		QueryResult<CustomerTsdkmxModel>result=null;
		JRadPagedQueryResult<CustomerTsdkmxModel> pagedResult = new JRadPagedQueryResult<CustomerTsdkmxModel>(
				filter, result);
			JRadModelAndView mv = new JRadModelAndView(
					"/dateplan/createplan", request);
			mv.addObject(PAGED_RESULT, pagedResult);
			return mv;
			
			
			
	}
}
