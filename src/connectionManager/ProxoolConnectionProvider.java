package connectionManager;

import java.sql.Connection;


import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
									//TODO (Not done! Do not use)

public class ProxoolConnectionProvider implements  ConnectionProvider {
	private Properties info;
	
	public ProxoolConnectionProvider () {
		//TODO read from XML
		info = new Properties();
		info.setProperty("proxool.maximum-connection-count", "20");
		info.setProperty("proxool.house-keeping-test-sql", "select CURRENT_DATE");
		info.setProperty("user", "root");
		info.setProperty("password", "1111");
	}

	@Override
	public Connection newConnection() throws SQLException {
		String alias = "test";
		String driverClass = "com.mysql.jdbc.Driver";
		String driverUrl = "jdbc:mysql://localhost:3306/java";
		String url = "proxool." + alias + ":" + driverClass + ":" + driverUrl;
		Connection connection = null;
		connection = DriverManager.getConnection(url, info);
		if (connection != null) {
			System.out.println("Connection created succesfully");
		}
		else {
			throw new SQLException("Can't create Proxool connection"); 
		}
		return connection;
	}

	@Override
	public void close() throws SQLException {
		// TODO Auto-generated method stub
		
	}
	
}
