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
		User u = new User ("Jackie", "jackiee@gmail.com");
		try {
			
			userDao = new UserDaoJdbcImpl ();
			userDao.insertUser(u);
			
		} 
		catch (NotUniqueEmailException e){
			System.out.println(e.getMessage());
		}
		
		catch (NotUniqueNameException e){
			System.out.println(e.getMessage());
		}
		
		catch (DBException e){
			e.printStackTrace();
		}
	}

}
