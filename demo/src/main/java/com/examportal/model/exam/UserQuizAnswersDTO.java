package com.examportal.model.exam;

public class UserQuizAnswersDTO {

	private Long questionId;
	private String userAnswer;

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public String getUserAnswer() {
		return userAnswer;
	}

	public void setUserAnswer(String userAnswer) {
		this.userAnswer = userAnswer;
	}

	UserQuizAnswersDTO(Long questionId, String userAnswer) {
		super();
		this.questionId = questionId;
		this.userAnswer = userAnswer;
	}

	UserQuizAnswersDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

}
