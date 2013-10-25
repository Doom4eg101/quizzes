package quizProject;


import java.util.List;
import dao.UserDao;
import dao.UserDaoJdbcImpl;
import model.User;
import exceptions.*;

public class Main {

	private static void printUsers(List<User> users) {
		if (users.size() == 0)
			System.out.println("Пусто");
		else {
			for (int i = 0; i < users.size(); i++) {
				System.out.println(users.get(i).toString());
			}
			System.out.println("Total users: " + users.size());
		}
	}

	public static void main(String[] args) {
		UserDao userDao = null;
		List<User> users = null;
		try {
		//	ConnectionProviderFactory.setConnectionProvider(ConnectionProviderFactory.chooseProvider.JDBC_DEFAULT);
			userDao = new UserDaoJdbcImpl ();
			long start = System.currentTimeMillis();
			for (int i = 0; i < 1000; i++) {
				users = userDao.getAllUsers();
			}
			long finish = System.currentTimeMillis() - start;
			printUsers(users);
			System.out.println(finish);
		}	
		catch (DBException e){
			e.printStackTrace();
		}
	}

}
