package model;

public class User {
	private String name;
	private String email;
	private int id;

	public User(String name, String email) {
		this.name = name;
		this.email = email;
		id = 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", email=" + email + ", id=" + id + "]";
	}

}
