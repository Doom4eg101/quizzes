package connectionManager;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionProvider {
	public Connection newConnection () throws SQLException;
	
	public void close () throws SQLException;
}
