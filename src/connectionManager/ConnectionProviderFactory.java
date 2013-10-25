package connectionManager;

public class ConnectionProviderFactory {
	public enum chooseProvider {
		C3P0_POOL, JDBC_DEFAULT, PROXOOL_POOL;
	}
	
	private static chooseProvider defaultProvider = chooseProvider.C3P0_POOL;
	
	public static void setConnectionProvider (chooseProvider newProvider) {
		defaultProvider = newProvider;
	}
	
	public static ConnectionProvider getConnectionProvider () {
		
		switch (defaultProvider) {
		case C3P0_POOL:
			return new C3p0ConnectionProvider();
		case JDBC_DEFAULT:
			return new JdbcConnectionProvider();
		case PROXOOL_POOL:
			return new ProxoolConnectionProvider();
		default:
			return null;
		}
		
	}
}


