package com.calm.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "questions")
public class Question {
	
	@Id
	private int id;
	
	@ManyToOne
	@JoinColumn(name="section", nullable=false)
	private Section section;
	
	private int number;
	
	@Column(length = 2000)
	private String text;
	
	@OneToMany(mappedBy="question")
	private List<Answer> answers;
	
	private int answerCode;
	
	private int answer;
	
	private String imageUrl;
	
	@Column(length = 2000)
	private String imageJs;
	
	public Question(int id, Section section, int number, String text, int answerCode, int answer) {
		this.id = id;
		this.section = section;
		this.number = number;
		this.text = text;
		this.answerCode = answerCode;
		this.answer = answer;
	}
	
	public Question addAnswer(Answer answer) {
		if(this.answers == null) {
			this.answers = new ArrayList<>();
		}
		
		this.answers.add(answer);
		if (answer.getQuestion() != this) {
			answer.setQuestion(this);
		}
		
		return this;
	}
}
