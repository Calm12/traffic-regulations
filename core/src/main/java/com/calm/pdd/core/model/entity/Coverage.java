package com.calm.pdd.core.model.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Getter
@Setter
@Accessors(chain = true)
@ToString(exclude = {"statistic"})
@EqualsAndHashCode(of = {"questionId", "statistic"})
@NoArgsConstructor
@Entity
@Table(name = "user_coverage")
public class Coverage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@ManyToOne
	@JoinColumn(name="statistic_id", nullable=false)
	private UserStatistic statistic;
	
	private int questionId;
	
	private int sectionId;
	
	private boolean correct;
	
	public Coverage(int questionId, int sectionId, boolean correct) {
		this.questionId = questionId;
		this.sectionId = sectionId;
		this.correct = correct;
	}
}
