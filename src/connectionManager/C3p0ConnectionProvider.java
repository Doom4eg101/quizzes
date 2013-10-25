package connectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3p0ConnectionProvider implements ConnectionProvider {
	
	private Properties props;
    private ComboPooledDataSource cpds;
    
	@Override
	public Connection newConnection() throws SQLException {
		if (cpds == null){
			createDatasource();
		}
		return cpds.getConnection();
	}

	
	 private void createDatasource() throws SQLException {
		 	// initialize pool
	        props = setProperties ();
	        cpds = new ComboPooledDataSource();
	        cpds.setJdbcUrl(props.getProperty("jdbcUrl"));
	        cpds.setUser(props.getProperty("username"));
	        cpds.setPassword(props.getProperty("password"));

	        cpds.setInitialPoolSize(new Integer((String) props.getProperty("initialPoolSize")));
	        cpds.setAcquireIncrement(new Integer((String) props.getProperty("acquireIncrement")));
	        cpds.setMaxPoolSize(new Integer((String) props.getProperty("maxPoolSize")));
	        cpds.setMinPoolSize(new Integer((String) props.getProperty("minPoolSize")));
	        cpds.setMaxStatements(new Integer((String) props.getProperty("maxStatements")));

	        Connection testConnection = null;
	        Statement testStatement = null;

	        // test connectivity
	        try {
	               testConnection = cpds.getConnection();
	               testStatement = testConnection.createStatement();
	               testStatement.executeQuery("select 1+1 from DUAL");
	            } catch (SQLException e) {
	                throw e;
	            } finally {
	                testStatement.close();
	                testConnection.close();
	        }

	    }

	private Properties setProperties() {
		//TODO read from XML 
		Properties props = new Properties ();
		props.setProperty("driverClass", "com.mysql.jdbc.Driver");
		props.setProperty("jdbcUrl", "jdbc:mysql://localhost:3306/java");
		props.setProperty("username", "root");
		props.setProperty("password", "1111");
		props.setProperty("acquireIncrement", "10");
		props.setProperty("initialPoolSize", "10");
		props.setProperty("maxPoolSize", "200");
		props.setProperty("minPoolSize", "5");
		props.setProperty("maxStatements", "200");
		
		return props;
	}


	@Override
	public void close() throws SQLException {
		cpds.close();
	}
}
