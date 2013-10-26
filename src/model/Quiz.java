package model;

import java.util.List;

public class Quiz {
	private final int id;
	
	private String description;
	private String title;
	private List<Question> questions;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	
	public Quiz (int id) {
		this.id = id;
	}
	
	public int getId () {
		return id;
	}
}
