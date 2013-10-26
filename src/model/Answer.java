package model;

public class Answer {
	private final int id;
	
	private boolean isCorrect;
	private String answer;
	
	public Answer (int id, boolean isCorrect, String answer) {
		this.id = id;
		this.answer = answer;
		this.isCorrect = isCorrect;
	}
	
	public boolean isCorrect() {
		return isCorrect;
	}

	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getId() {
		return id;
	}

}
