package connectionManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnectionProvider implements ConnectionProvider {
	//TODO read from XML
	private final String JDBC_URL = "jdbc:mysql://localhost:3306/java";
	private final String LOGIN = "root";
	private final String PASSWORD = "1111";



	@Override
	public Connection newConnection() throws SQLException {
		Connection conn = DriverManager
				.getConnection(JDBC_URL, LOGIN, PASSWORD);
		return conn;
	}



	@Override
	public void close() throws SQLException {
		// nothing to close
		
	}


}
