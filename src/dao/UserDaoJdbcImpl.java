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
import exceptions.*;

public class UserDaoJdbcImpl implements UserDao {
	
	// private final String JDBC_URL = "jdbc:mysql://localhost:3306/java";
	// private final String LOGIN = "root";
	// private final String PASSWORD = "1111";
	
	//TODO read from file or XML
	private final String SELECT_ALL_SQL = "SELECT * FROM users";
	private final String SELECT_BY_ID = "SELECT * FROM users WHERE id = ";
	private final String SELECT_BY_NAME = "SELECT * FROM users WHERE name = ";
	private final String SELECT_BY_EMAIL = "SELECT * FROM users WHERE email = ";
	private final String DELETE_BY_ID = "DELETE FROM users WHERE id = ";
	private final String INSERT_USER_SQL = "INSERT INTO users (id, name, email) VALUES (default, ?, ?)";
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
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			
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
			conn.commit();
			return result;
		}

		catch (SQLException e) {
			JdbcUtils.rollbackQuietly(conn);
			e.printStackTrace();
			throw new DBSystemException("Can't get all users");
		}

		finally {
			JdbcUtils.closeQuietly(resultSet);
			JdbcUtils.closeQuietly(statement);
			JdbcUtils.closeQuietly(conn);
		}
	}

	

	@Override
	public void insertUser(User u) throws DBSystemException, NotUniqueEmailException, NotUniqueNameException {
		Connection conn = null;
		Statement statement = null;

		try {
			
			conn = connectionProvider.newConnection();
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			
			if (emailAlreadyExists (u.getEmail(), conn)){
				throw new NotUniqueEmailException ("Email " + u.getEmail() + " already exists");
			}
			
			if (nameAlreadyExists (u.getName(), conn)){
				throw new NotUniqueNameException ("Name " + u.getName() + " already exists");
			}
			
			PreparedStatement ps = conn.prepareStatement(INSERT_USER_SQL);
			ps.setString(1, u.getName());
			ps.setString(2, u.getEmail());
			ps.execute();
			
			conn.commit();
		} catch (SQLException e) {
			JdbcUtils.rollbackQuietly(conn);
			e.printStackTrace();
			throw new DBSystemException("Can't insert user" + u.toString());
		} finally {
			JdbcUtils.closeQuietly(statement);
			JdbcUtils.closeQuietly(conn);
		}
	}
	
	private boolean emailAlreadyExists (String email, Connection conn) {
		ResultSet resultSet = null;
		
		try {
			Statement st = conn.createStatement();
			resultSet = st.executeQuery(SELECT_BY_EMAIL + "'" + email + "'");
			return resultSet.next();
		} catch (SQLException e) {
			System.out.println("Error checking email");
			e.printStackTrace();
		} finally {
			JdbcUtils.closeQuietly(resultSet);
		}
		
		return false;
	}
	
	private boolean nameAlreadyExists (String name, Connection conn) {
		ResultSet resultSet = null;
		try {
			Statement st = conn.createStatement();
			resultSet = st.executeQuery(SELECT_BY_NAME + "'" + name + "'");
			return resultSet.next();
		} catch (SQLException e) {
			System.out.println("Error checking name");
			e.printStackTrace();
		} finally {
			JdbcUtils.closeQuietly(resultSet);
		}
		return false;
	}
	


	@Override
	public User getUserById(int id) throws DBSystemException {
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			conn = connectionProvider.newConnection();
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			
			statement = conn.createStatement();
			resultSet = statement.executeQuery(SELECT_BY_ID + id);
			conn.commit();
			
			if (!resultSet.next()) {
				return null;
			}
			String email = resultSet.getString("email");
			String name = resultSet.getString("name");
			User u = new User(name, email);
			u.setId(id);
			return u;
		} catch (SQLException e) {
			JdbcUtils.rollbackQuietly(conn);
			e.printStackTrace();
			throw new DBSystemException("Can't get user" + id);
		} finally {
			JdbcUtils.closeQuietly(statement);
			JdbcUtils.closeQuietly(conn);
		}
	}

	@Override
	public boolean deleteUserByid(int id) throws DBSystemException {
		Connection conn = null;
		Statement statement = null;

		try {
			conn = connectionProvider.newConnection();
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			
			statement = conn.createStatement();
			
			// return false if where is no such user
			
			if (statement.executeUpdate(DELETE_BY_ID + id) == 0){
				return false;
			} else {
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBSystemException ("Can't delete user" + id);
		} finally {
			JdbcUtils.closeQuietly(statement);
			JdbcUtils.closeQuietly(conn);
		}
	}

	public void addUsers(List<User> users) throws DBSystemException {
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = connectionProvider.newConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(INSERT_USER_SQL);
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
			JdbcUtils.closeQuietly(ps);
			JdbcUtils.closeQuietly(conn);
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
			JdbcUtils.closeQuietly(statement);
			JdbcUtils.closeQuietly(conn);
		}
	}
}
