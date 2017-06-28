package com.cardpay.pccredit.dateplan.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.dateplan.dao.CustomerTsdkmxDao;
import com.cardpay.pccredit.dateplan.model.CustomerTsdkmxModel;
import com.cardpay.pccredit.dateplan.model.DisplayUser;
import com.cardpay.pccredit.dateplan.service.CustomerTsdkmxService;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.auth.JRadOperation;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.util.spring.Beans;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;

@Controller
public class CustomerTsdkmxController {
	@Autowired
	private CustomerTsdkmxService TsdkmxService;
	
	/**
	 *添加特殊贷款明细
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "creat.page", method = { RequestMethod.GET })
	@JRadOperation(JRadOperation.BROWSE)
	public AbstractModelAndView view1( HttpServletRequest request) {
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		CustomerTsdkmxModel model=TsdkmxService.selectMaxLOANDATE(user.getId());
		CustomerTsdkmxModel customermodel=new CustomerTsdkmxModel();
		customermodel.setUSERID(user.getId());
		customermodel.setLOANDATE(model.getLOANDATE());
		CustomerTsdkmxModel TsdkmxModel=TsdkmxService.selectCustomerTsdk(customermodel);
			JRadModelAndView mv = new JRadModelAndView(
					"/dateplan/createplan", request);
			
			mv.addObject("user", user);
			return mv;
	}
}
