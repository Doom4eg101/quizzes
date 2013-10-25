package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connectionManager.ConnectionProvider;
import connectionManager.ConnectionProviderFactory;
import model.User;
import exceptions.DBException;
import exceptions.DBSystemException;

public class UserDaoJdbcImpl implements UserDao {
	
	// private final String JDBC_URL = "jdbc:mysql://localhost:3306/java";
	// private final String LOGIN = "root";
	// private final String PASSWORD = "1111";
	
	//TODO read from file or XML
	private final String SELECT_ALL_SQL = "SELECT * FROM users";
	private final String INSERT_USER_SQL = "INSERT INTO users VALUES";
	private final String SELECT_BY_ID = "SELECT * FROM users WHERE id = ";
	private final String DELETE_BY_ID = "DELETE FROM users WHERE id = ";
	private final String INSERT_USERS_SQL = "INSERT INTO users (id, name, email) VALUES (default, ?, ?)";
	private final String DELETE_ALL_SQL = "Truncate table users";

	private ConnectionProvider connectionProvider;

	public UserDaoJdbcImpl() throws DBException {
		ConnectionProvider newProv = ConnectionProviderFactory.getConnectionProvider();
		
		if (newProv == null) {
			throw new DBException("Error getting connection Provider!");
		}
		else {
			connectionProvider = newProv;
		}
	}

	@Override
	public List<User> getAllUsers() throws DBSystemException {
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<User> result = new ArrayList<User>();
		try {
			conn = connectionProvider.newConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(SELECT_ALL_SQL);
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String email = resultSet.getString("email");

				User u = new User(name, email);
				u.setId(id);
				result.add(u);
			}
			return result;
		}

		catch (SQLException e) {
			e.printStackTrace();
			throw new DBSystemException("Can't get all users");
		}

		finally {
			closeQuietly(resultSet);
			closeQuietly(statement);
			closeQuietly(conn);
		}
	}

	@Override
	public void insertUser(User user) throws DBSystemException {
		Connection conn = null;
		Statement statement = null;

		try {
			
			conn = connectionProvider.newConnection();
			statement = conn.createStatement();
			String sqlRequest = INSERT_USER_SQL + " (default, '"
					+ user.getName() + "','" + user.getEmail() + "')";
			System.out.println(sqlRequest);
			statement.execute(sqlRequest);
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBSystemException("Can't insert user" + user.toString());
		} finally {
			closeQuietly(statement);
			closeQuietly(conn);
		}
	}

	@Override
	public User getUserById(int id) throws DBSystemException {
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			conn = connectionProvider.newConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(SELECT_BY_ID + id);

			if (resultSet.next() == false) {
				return null;
			}
			String email = resultSet.getString("email");
			String name = resultSet.getString("name");
			User u = new User(name, email);
			u.setId(id);
			return u;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBSystemException("Can't get user" + id);
		} finally {
			closeQuietly(statement);
			closeQuietly(conn);
		}
	}

	@Override
	public void deleteUserByid(int id) throws DBSystemException {
		Connection conn = null;
		Statement statement = null;

		try {
			conn = connectionProvider.newConnection();
			statement = conn.createStatement();
			statement.executeUpdate(DELETE_BY_ID + id);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBSystemException ("Can't delete user" + id);
		} finally {
			closeQuietly(statement);
			closeQuietly(conn);
		}
	}

	@Override
	public void addUsers(List<User> users) throws DBSystemException {
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = connectionProvider.newConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(INSERT_USERS_SQL);
			for (User u : users) {
				ps.setString(1, u.getName());
				ps.setString(2, u.getEmail());
				ps.addBatch();
			}
			ps.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBSystemException("Can't add users");
		} finally {
			closeQuietly(ps);
			closeQuietly(conn);
		}
	}

	public void deleteAllUsers() throws DBSystemException {
		Connection conn = null;
		Statement statement = null;
		try {
			conn = connectionProvider.newConnection();
			statement = conn.createStatement();
			statement.executeUpdate(DELETE_ALL_SQL);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBSystemException("Can't delete all users");
		} finally {
			closeQuietly(statement);
			closeQuietly(conn);
		}
	}

	private void closeQuietly(AutoCloseable obj) {
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
