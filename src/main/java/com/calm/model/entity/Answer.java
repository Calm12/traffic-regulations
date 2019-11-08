package com.calm.model.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Accessors(chain = true)
@ToString(exclude = {"question"})
@EqualsAndHashCode(exclude = {"id"})
@NoArgsConstructor
@Entity
@Table(name = "answers")
public class Answer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private int number;
	
	@Column(length = 2000)
	private String text;
	
	@ManyToOne
	@JoinColumn(name="question_id", nullable=false)
	private Question question;
	
	public Answer(int number, String text) {
		this.number = number;
		this.text = text;
	}
	
	public Answer setQuestion(Question question) {
		this.question = question;
		if (!question.getAnswers().contains(this)) {
			question.getAnswers().add(this);
		}
		
		return this;
	}
}
