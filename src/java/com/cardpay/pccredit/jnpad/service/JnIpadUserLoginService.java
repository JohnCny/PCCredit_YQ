package com.cardpay.pccredit.jnpad.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.customer.filter.CustomerInforFilter;
import com.cardpay.pccredit.customer.model.CustomerInfor;
import com.cardpay.pccredit.jnpad.dao.JnIpadUserLoginDao;
import com.cardpay.pccredit.jnpad.model.CustomerAppInfoIpad;
import com.cardpay.pccredit.jnpad.model.JnUserLoginIpad;
import com.cardpay.pccredit.product.dao.ProductDao;
import com.cardpay.pccredit.product.model.ProductAttribute;
import com.cardpay.pccredit.product.model.ProductsAgencyAssociation;
import com.cardpay.pccredit.riskControl.model.RiskCustomer;
import com.cardpay.pccredit.system.model.SystemUser;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.base.database.id.IDGenerator;
import com.wicresoft.jrad.modules.privilege.model.Organization;
import com.wicresoft.jrad.modules.privilege.model.User;
import com.wicresoft.jrad.modules.privilege.service.OrganizationService;

@Service
public class JnIpadUserLoginService {
	
	@Autowired
	private JnIpadUserLoginDao jnIpadUserLoginDao;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	 private CommonDao commonDao;
	
	@Autowired
	private ProductDao productDao;
	
	/**
	 * 登陆
	 *
	 */
	public JnUserLoginIpad login(String login,String passwd){
		User user = jnIpadUserLoginDao.find(login, passwd);
		JnUserLoginIpad ipad = null ;
		if(user!=null){
			ipad = new JnUserLoginIpad();
			ipad.setId(user.getId());
			ipad.setDisplayName(user.getDisplayName());
			ipad.setUserType(user.getUserType());
		}
		return ipad;
	}
	
	
	
	/**
	 * 得到pageSize*(currentPage-1)到pageSize*currentPage行数据 
	 * @param currentPage 
	 * @param pageSize
	 * @return
	 */
	public List<com.cardpay.pccredit.ipad.model.ProductAttribute> findProducts(int currentPage,int pageSize){
		currentPage = currentPage - 1;
		if(currentPage<0){
			currentPage = 0;
		}
		return productDao.findProductsPad(currentPage,pageSize);
	}
	
	/**
	 * 已发布,筛选产品数量
	 * @return
	 */
	public int findProductsCount(){
		return productDao.findProductsCount();
	}
	
	/**
	 * 根据产品id查询已经发布产品
	 * @param productId
	 * @return
	 */
	public ProductAttribute findPublishedProductAttributeByProductId(String productId) {
		return productDao.findPublishedProductAttributeByProductId(productId);
	}
	
	/*List<CustomerAppInfoIpad> findCustomerAppIntoCount(){
		
	}*/
	
	public String findOrg(String id){
		List<HashMap<String, Object>> list = jnIpadUserLoginDao.findOrg(id);
		if(list != null && list.size() > 0){
			HashMap<String, Object> map = list.get(0);
			return (String) map.get("NAME");
		} else {
			return null;
		}
	}
	
	public SystemUser findUser(String userId){
		return jnIpadUserLoginDao.findUser(userId);
	}



	public String findLastLogin(String loginId) {
		
		String sql ="SELECT action_time FROM ( SELECT rownum rm,action_time FROM ( SELECT s.* FROM SYS_LOGIN_LOG s  INNER JOIN SYS_USER u ON s.login = u.LOGIN WHERE u.ID = '"+loginId+"' and s.action='SignIn' ORDER BY s.action_time DESC) )WHERE rm=2";
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		String time="";
		List<HashMap<String, Object>> LastLogin = commonDao.queryBySql(sql, null);
		if(LastLogin.size()>0){
		params =LastLogin.get(0);
		time = params.get("ACTION_TIME").toString();
		}else{
			time="无上次登录信息";
		}
		return time;
	}
	public String findServer5(){
				String sql = "select * from dict where dict_type = 'LocationType' ";
				String PARAM = (String) commonDao.queryBySql(sql, null).get(0).get("TYPE_CODE");
				return PARAM;
			}
}
