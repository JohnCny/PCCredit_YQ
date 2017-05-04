package com.cardpay.pccredit.toolsjn;

import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.sql.Statement;

 

public class ConnectionOracle {

 

/**

* @param args

*/

String sd="oracle.jdbc.driver.OracleDriver";

// String sc="jdbc:oracle:thin:@192.168.1.170:1521:cst";

String sc="jdbc:oracle:thin:@127.0.0.1:1521:orcl";

String userName = "pccredit_jn";

String password = "pccredit_jn";

// String sd="com.mysql.jdbc.Driver";

// String sc="jdbc:mysql://localhost:3306/payManagerDB?useUnicode=true&amp;characterEncoding=utf8";

Connection con=null;

Statement stmt=null;

ResultSet rs=null;


public ConnectionOracle()

  {

  try

  {

  Class.forName(sd);

  }

  catch(Exception e)

  {

  System.err.println(e.getMessage());

  }

  }

public static void main(String[] args) {

// TODO Auto-generated method stub


}

public ResultSet executeQuery(String sql) throws SQLException

  {

  con=DriverManager.getConnection(sc,userName,password);

  Statement stmt=con.createStatement();

  rs=stmt.executeQuery(sql);

  return rs;

  }

public void executeUpdate(String sql) throws SQLException

  {

  con=DriverManager.getConnection(sc,userName,password);

  Statement stmt=con.createStatement();

  stmt.executeUpdate(sql);

  }

public void close() throws SQLException

  {

if(rs!=null)

rs.close();

if(stmt!=null)

stmt.close();

if(con!=null)

con.close();

  }

}

 


