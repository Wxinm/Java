package edu.develop.accountser;
import java.sql.* ;

public class DatabaseConnection {
	private static final String DBDRIVER = "org.sqlite.JDBC";
 	private static final String DBURL = "jdbc:sqlite:D:/homework.sqlite";
// 	private static final String DBUSER = null;
//	private static final String DBPASSWORD = null;
	private Connection conn = null;
	
	public DatabaseConnection() throws Exception{
		try{
			Class.forName(DBDRIVER) ;
			this.conn = DriverManager.getConnection(DBURL);
			System.out.println("打开数据库连接");
		}catch(Exception e){
			throw e ;
		}
	}
	public Connection getConnection(){
		return this.conn ;
	}
	public void close() throws Exception{
		if(this.conn != null){
			try{
				this.conn.close() ;
			}catch(Exception e){
				throw e ;
			}
		}
	}
}