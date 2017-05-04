package com.wicresoft.jrad.base.database.dialect;

public class MySQLDialect extends AbstractDialect
{
  public boolean supportsLimitOffset()
  {
    return true;
  }

  public boolean supportsLimit() {
    return true;
  }

  public String getLimitSql(String sql, int offset, int limit) {
    if (offset > 0) {
      return sql + " limit " + offset + "," + limit;
    }
    return sql + " limit " + limit;
  }
  
  public static void main(String[] args) {
	    MySQLDialect dialect = new MySQLDialect();
		System.out.println(dialect.getLimitSql("select * from sample where name=?", 5, 10));
		System.out.println(dialect.getLimitSql("select * from sample where id=? for update", 5, 10));
	}
}