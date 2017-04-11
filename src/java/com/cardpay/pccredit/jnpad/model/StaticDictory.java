package com.cardpay.pccredit.jnpad.model;

import java.util.HashMap;
import java.util.Map;

public class StaticDictory {
	
	public  Map<String,String> MINZU(){
		//民族
		Map<String,String> minzu = new HashMap<String, String>();
		minzu.put("1", "汉族");
		minzu.put("10", "朝鲜族");
		minzu.put("11", "满族");
		minzu.put("12", "侗族");
		minzu.put("13", "瑶族");
		minzu.put("14", "白族");
		minzu.put("15", "土家族");
		minzu.put("16", "哈尼族");
		minzu.put("17", "哈萨克族");
		minzu.put("18", "傣族");
		minzu.put("19", "黎族");
		minzu.put("2", "蒙古族");
		minzu.put("20", "傈僳族");
		minzu.put("21", "佤族");
		minzu.put("22", "畲族");
		minzu.put("23", "高山族");
		minzu.put("24", "拉祜族");
		minzu.put("25", "水族");
		minzu.put("26", "东乡族");
		minzu.put("27", "纳西族");
		minzu.put("28", "景颇族");
		minzu.put("29", "柯尔克孜族");
		minzu.put("3", "回族");
		minzu.put("30", "土族");
		minzu.put("31", "达斡尔族");
		minzu.put("32", "仫佬族");
		minzu.put("33", "羌族");
		minzu.put("34", "布朗族");
		minzu.put("35", "撒拉族");
		minzu.put("36", "毛南族");
		minzu.put("37", "仡佬族");
		minzu.put("38", "锡伯族");
		minzu.put("39", "阿昌族");
		minzu.put("4", "藏族");
		minzu.put("40", "普米族");
		minzu.put("41", "塔吉克族");
		minzu.put("42", "怒族");
		minzu.put("43", "乌兹别克族");
		minzu.put("44", "俄罗斯族");
		minzu.put("45", "鄂温克族");
		minzu.put("46", "德昂族");
		minzu.put("47", "保安族");
		minzu.put("48", "裕固族");
		minzu.put("49", "京族");
		minzu.put("5", "维吾尔族");
		minzu.put("50", "塔塔尔族");
		minzu.put("51", "独龙族");
		minzu.put("52", "鄂伦春族");
		minzu.put("53", "赫哲族");
		minzu.put("54", "门巴族");
		minzu.put("55", "珞巴族");
		minzu.put("56", "基诺族");
		minzu.put("6", "苗族");
		minzu.put("7", "彝族");
		minzu.put("8", "壮族");
		minzu.put("9", "布依族");
		
		return minzu;
	}
	public  Map<String,String> CUSTTYPEJN(){
//		客户类型
		Map<String,String> custtypejn = new HashMap<String, String>();
		custtypejn.put("01", "农户");
		custtypejn.put("02", "个体工商户");
		custtypejn.put("03", "城镇居民");
		custtypejn.put("04", "学生");
		custtypejn.put("05", "军官");
		custtypejn.put("06", "港澳台同胞");
		custtypejn.put("07", "外籍人士");
		
		
		return custtypejn;
	}
	public  Map<String,String> SEX(){
		
		Map<String,String> sex = new HashMap<String, String>();
		
		sex.put("1", "男");
		sex.put("2", "女");
		return sex;
	}
	public  Map<String,String> CARDTYPE(){
		//证件类型
		Map<String,String> cardtype = new HashMap<String, String>();
		cardtype.put("0", "身份证");
		cardtype.put("1", "军官证");
		cardtype.put("2", "护照");
		cardtype.put("3", "香港身份证");
		cardtype.put("4", "澳门身份证");
		cardtype.put("5", "台湾身份证");
		
		return cardtype;
	}
	public  Map<String,String> MARRIGE(){
		//婚姻状况
		Map<String,String> marrige = new HashMap<String, String>();
		marrige.put("0", "未婚");
		marrige.put("1", "已婚");
		marrige.put("2", "丧偶");
		marrige.put("3", "离婚");
		return marrige;
	}
	public  Map<String,String> RESIDE(){
		//居住状况
		Map<String,String> reside = new HashMap<String, String>();
		reside.put("0", "自置");
		reside.put("1", "按揭");
		reside.put("2", "租房");
		reside.put("3", "集体宿舍");
		reside.put("4", "亲属楼宇");
		reside.put("5", "共有住宅");
		reside.put("6", "其他");
		reside.put("7", "未知");
		return reside;
	}
	public  Map<String,String> EDUCATIONLEVEL(){
		//最高学位
		Map<String,String> educationlevel = new HashMap<String, String>();
		educationlevel.put("0", "博士");
		educationlevel.put("1", "名誉博士");
		educationlevel.put("2", "硕士");
		educationlevel.put("3", "学士");
		educationlevel.put("4", "其他");
		educationlevel.put("5", "无");
		educationlevel.put("6", "未知");
		return educationlevel;
	}
	public  Map<String,String> DEGREE(){
		//最高学历
		Map<String,String> degree = new HashMap<String, String>();
		degree.put("1", "研究生");
		degree.put("2", "大学本科");
		degree.put("3", "大学专科或专科学校");
		degree.put("4", "中等专业或技术学校");
		degree.put("5", "高中");
		degree.put("6", "初中");
		degree.put("7", "小学");
		degree.put("8", "文盲或半文盲");
		degree.put("9", "未知");
		return degree;
	}
	

}
