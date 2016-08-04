/*package testingPackage;

import java.sql.*;
import javax.swing.*;

public class sqliteConnectionTEST {
	Connection conn = null;
	public static Connection dbConnector(){
		try{
			Class.forName("org.sqlite.JDBC");
			Connection conn= DriverManager.getConnection("jdbc:sqlite:" + System.getProperty("user.dir") + "\\Sqlite\\ResortManagement.sqlite");
 
			//JOptionPane.showMessageDialog(null, "Connection Successful!");
			return conn;
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, e);
			return null;
		}
	}

}*/

package applicationPackage;

import java.sql.*;
import javax.swing.*;

public class MySQLConnection {
	Connection conn = null;
	public static Connection dbConnector(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			String url ="jdbc:mysql://104.154.114.159/new_schema";
			String userName = "root";
			String password = "resort";
			Connection conn= DriverManager.getConnection(url, userName, password);
			
			
 
			return conn;
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, e);
			return null;
		}
	}

}

