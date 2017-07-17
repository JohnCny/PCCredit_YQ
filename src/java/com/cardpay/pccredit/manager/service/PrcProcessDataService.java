package com.cardpay.pccredit.manager.service;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.cardpay.pccredit.manager.dao.ManagerSalaryDao;
import com.cardpay.pccredit.manager.model.InComeStateMentDay;
import com.cardpay.pccredit.manager.model.InComeStateMentHistory;
import com.cardpay.pccredit.manager.model.TMibusidata;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;

/**
 * @author admin
 *
 */
/**
 * @author admin
 *
 */
@Service
public class PrcProcessDataService {

	private Logger logger = Logger.getLogger(PrcProcessDataService.class);

	@Autowired
	private CommonDao commonDao;

	/**
	 * 调用存储过程
	 */
	public void prcProcessData(String date) {
		boolean flag = false;
		String sql = "{call PRC_PROCESS_DATA(?, ?)}";
		Connection conn = null;
		CallableStatement statement = null;
		String result = "0";// 0:成功，其他 为 错误 信息
		System.out.println("开始执行数据加工！");
		try {
			conn = commonDao.getSqlSession().getConnection();
			statement = conn.prepareCall(sql);
			statement.setString(1, date);
			statement.registerOutParameter(2, Types.VARCHAR);
			statement.execute();
			result = statement.getString(2);
			if ("0".equals(result)) {
				flag = true;
				System.out.println("数据加工完成！");
			} else {
				System.out.println("数据加工失败 ：" + result);
				flag = false;
			}
		} catch (SQLException e) {
			flag = false;
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					System.out.println("数据加工失败 ：" + e.getMessage());
				}
			}
		}
	}

}
