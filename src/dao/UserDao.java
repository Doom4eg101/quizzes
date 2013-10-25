package dao;


import java.util.List;

import model.User;
import exceptions.*;
public interface UserDao {
	
	
	public List<User> getAllUsers () throws DBSystemException;
	
	public void insertUser (User user) throws DBSystemException, NotUniqueEmailException, NotUniqueNameException;
	
	public User getUserById (int id) throws DBSystemException;
	
	public void deleteUserByid (int id) throws DBSystemException;

	public void deleteAllUsers() throws DBSystemException;
	
}
 