package dao;


import java.util.List;

import model.User;
import exceptions.DBSystemException;
public interface UserDao {
	
	
	public List<User> getAllUsers () throws DBSystemException;
	
	public void insertUser (User user) throws DBSystemException;
	
	public User getUserById (int id) throws DBSystemException;
	
	public void deleteUserByid (int id) throws DBSystemException;

	public void addUsers(List<User> users) throws DBSystemException;

	public void deleteAllUsers() throws DBSystemException;
	
}
 