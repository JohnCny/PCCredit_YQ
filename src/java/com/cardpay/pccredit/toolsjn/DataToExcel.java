package com.cardpay.pccredit.toolsjn;

import java.io.File;

import java.io.FileOutputStream;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.util.ArrayList;

import java.util.Iterator;

import java.util.List;

 

import org.apache.poi.hssf.usermodel.HSSFCell;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import org.apache.poi.hssf.usermodel.HSSFFont;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;

import org.apache.poi.hssf.usermodel.HSSFRow;

import org.apache.poi.hssf.usermodel.HSSFSheet;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.hssf.util.Region;

 

/**

 * 将oracle中的数据表结构导入到excel中保存

 * @class DataToExcel

 * @description 

 * @author 李智慧

 * @copyRight copyright(c) 2011 广东南航易网通电子商务有限公司,Rights Reserved

 * @time Dec 27, 2011 10:02:08 AM

 */

public class DataToExcel {

	public static void main(String[] args) {

		String result = "";

		List listAll = new ArrayList();

		System.out.println("正在读取数据库中所有的表");

		try {

			List tableList = getTableList();

			System.out.println("数据库表读取完成");

			for (int i = 0; i < tableList.size(); i++) {

				String[] strings = (String[]) tableList.get(i);

				String tableName = strings[0].toString();

				List list = new ArrayList();

				list.add(tableName);

				list.add(getStructOfTable(tableName));

				System.out.println("正在生成表" + tableName + "的结构");

				listAll.add(list);

			}

			result = TableStructInfoToExcel(listAll, "D:");

			System.out.println("数据库中表结构导入已完成");

		} catch (Exception e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

			File file = new File(e.getMessage().toString());

			if (file.exists()) {

				file.delete();

			}

		}

		System.out.println(result);

		// showView(list);

	}

	/**
	 * 
	 * 获取数据库中所有的表
	 * 
	 * @return
	 */

	public static List getTableList() {

		String sql = "select object_name From user_objects Where object_type='TABLE'";

		return getResult(sql, 1);

	}

	/**
	 * 
	 * 根据表明
	 * 
	 * @param tableName
	 * 
	 * @return
	 */

	public static List getStructOfTable(String tableName) {

		String sql = "SELECT u.column_name,u.data_type,u.data_length,u.data_precision,u.data_Scale,u.nullable,u.data_default,c.comments FROM user_tab_columns u,user_col_comments c"
				+

				" WHERE u.table_name='"
				+ tableName
				+ "' and u.table_name=c.table_name and c.column_name=u.column_name";

		return getResult(sql, 8);

	}

	/**
	 * 
	 * 获取结果的公用类
	 * 
	 * @param sql
	 * 
	 * @param length
	 * 
	 * @return
	 */

	public static List getResult(String sql, int length) {

		List list = new ArrayList();

		ResultSet rs = null;

		ConnectionOracle c = new ConnectionOracle();

		try {

			rs = c.executeQuery(sql);

			while (rs.next()) {

				String[] string = new String[length];

				for (int i = 1; i < length + 1; i++) {

					string[i - 1] = rs.getString(i);

				}

				list.add(string);

			}

			c.close();

		} catch (SQLException e) {

			e.printStackTrace();

		}

		return list;

	}

	/**
	 * 
	 * 输出对应list中的数据
	 * 
	 * @param list
	 */

	public static void showView(List list) {

		for (Iterator iterator = list.iterator(); iterator.hasNext();) {

			String[] name = (String[]) iterator.next();

			for (int i = 0; i < name.length; i++) {

				System.out.println(name[i]);

			}

		}

	}

	/**
	 * 
	 * 将数据导入到excel中
	 * 
	 * @param list
	 * 
	 * @param tableName
	 * 
	 * @param path
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */

	public static String TableStructInfoToExcel(List list, String path)
			throws Exception {

		String FileName = "";

		FileOutputStream fos = null;

		HSSFRow row = null;

		HSSFCell cell = null;

		HSSFCellStyle style = null;

		HSSFFont font = null;

		int currentRowNum = 0;

	/*	String[] tableFiled = { "column_name", "data_type", "data_length",
				"data_precision", "data_Scale", "nullable", "data_default",
				"comments" };*/
		String[] tableFiled = { "字段代码", "数据类型", "长度",
				"数字类型的实际长度", "精度", "非空", "默认值",
				"备注" };

		try {

			FileName = path + "\\" + "济南小微贷数据表结构.xls";

			fos = new FileOutputStream(FileName);

			// 创建新的sheet并设置名称

			HSSFWorkbook wb = new HSSFWorkbook();

			HSSFSheet s = wb.createSheet();

			wb.setSheetName(0, "CSN数据库表结构");

			style = wb.createCellStyle();

			font = wb.createFont();

			for (int z = 0; z < list.size(); z++) {

				List listBean = (List) list.get(z);

				// 新建一行,再在行上面新建一列

				row = s.createRow(currentRowNum);

				int pad = currentRowNum;

				currentRowNum++;

				// 设置样式

				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体加粗

				style.setFont(font);

				style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中

				style.setFillForegroundColor((short) 13);// 设置背景色

				style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

				style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框

				style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框

				style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框

				style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框

				for (int i = 0; i < tableFiled.length; i++) {

					cell = row.createCell((short) i);

					cell.setCellValue("");

					cell.setCellStyle(style);

				}

				row.getCell((short) 0).setCellValue(
						"数据库表" + listBean.get(0).toString() + "的结构");

				// 创建第二行

				row = s.createRow(currentRowNum);

				currentRowNum++;

				for (int i = 0; i < tableFiled.length; i++) {

					// 创建多列并设置每一列的值和宽度

					cell = row.createCell((short) i);

					cell.setCellValue(new HSSFRichTextString(tableFiled[i]));

					s.setColumnWidth((short) i, (short) 5000);

				}

				List list2 = (List) listBean.get(1);

				for (int i = 0; i < list2.size(); i++) {

					row = s.createRow(currentRowNum);

					currentRowNum++;

					String[] strings = (String[]) list2.get(i);

					for (int j = 0; j < strings.length; j++) {

						cell = row.createCell((short) j);

						cell.setCellValue(new HSSFRichTextString(strings[j]));

					}

				}

				// 合并单元格

				s.addMergedRegion(new Region(pad, (short) 0, pad,
						(short) (tableFiled.length - 1)));

				currentRowNum++;

			}

			wb.write(fos);

			fos.close();

		} catch (Exception e) {

			e.printStackTrace();

			fos.close();

			throw new Exception(FileName);

		}

		return FileName;

	}

}


