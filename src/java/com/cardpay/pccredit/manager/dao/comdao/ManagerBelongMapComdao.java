/**
 * 
 */
package com.cardpay.pccredit.manager.dao.comdao;

import java.util.*;

import com.cardpay.pccredit.tools.structure.Tree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.manager.model.ManagerBelongMap;
import com.cardpay.pccredit.riskControl.model.NplsInfomationConfiguration;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;

/**
 * 
 * 描述 ：
 * @author 张石树
 *
 * 2014-11-12 下午4:59:00
 */
@Service
public class ManagerBelongMapComdao {

	@Autowired
	private CommonDao commonDao;

	/**
	 * 根据用户Id查询客户经理从属关系
	 * @param userId
	 * @return
	 */
	public ManagerBelongMap findManagerBelongMapByUserId(String userId){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		String sql = "select t.* from manager_belong_map t left join account_manager_parameter amp on amp.id = t.child_id where amp.user_id = #{userId}";
		List<ManagerBelongMap> tempList = commonDao.queryBySql(ManagerBelongMap.class, sql, params);
		if(tempList != null && tempList.size() > 0){
			return tempList.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 获取不良资产配置信息
	 * @return
	 */
	public NplsInfomationConfiguration findNplsInfomationConfiguration(){
		String sql = "select * from npls_information_configuration";
		List<NplsInfomationConfiguration> tempList = commonDao.queryBySql(NplsInfomationConfiguration.class, sql, null);
		if(tempList != null && tempList.size() > 0){
			return tempList.get(0);
		} else {
			return null;
		}
	}

    /**
     * 获得客户经理管理树ID
     * @param userId
     * @return
     */
    public ArrayList<String> getManagerList(String userId){
        Tree managerTree=new Tree();

        String sql="select * from manager_belong_map";
        List<ManagerBelongMap> tempList=commonDao.queryBySql(ManagerBelongMap.class,sql,null);


        Iterator iterator=tempList.iterator();

        while(iterator.hasNext()){
            ManagerBelongMap managerBelongMap=(ManagerBelongMap)iterator.next();
            managerTree.addNode(managerBelongMap.getParentId(),managerBelongMap.getChildId());
        }

        managerTree.traversalTree(managerTree.findNode(managerTree.getRoot(),userId));

        return (ArrayList<String>)managerTree.getDataList();
    }

    /**
     * 根据传入的list转化为ID清单 todo:可作为公共接口
     * @param arrayList
     * @return
     */
    public String managerSet(ArrayList<String> arrayList){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("( ");

        Iterator iterator=arrayList.iterator();

        while (iterator.hasNext()){
            stringBuilder.append("'"+(String)iterator.next()+"',");
        }

        stringBuilder.append(" )");
        return stringBuilder.toString();
    }
}
