package dao;

import java.sql.Connection;
import java.sql.SQLException;

public class JdbcUtils {

	public static void rollbackQuietly(Connection conn) {
		if (conn != null) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				// ignore
			}
		}

	}

	public static void closeQuietly(AutoCloseable obj) {
		if (obj != null) {
			try {
				obj.close();
			} catch (Exception e) {
				System.out.println("Error closing " + obj.getClass());
				e.printStackTrace();
			}
		}
	}
}
