package com.calm.pdd.core.model.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Getter
@Setter
@Accessors(chain = true)
@ToString//(exclude = {"totalProgress"})
@EqualsAndHashCode(exclude = {"id"})
@NoArgsConstructor
@Entity
@Table(name = "user_statistic")
public class UserStatistic {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private int userId;
	
	private int topExamTime;
	
	private int totalTestingTime;
	
	private int totalExamAttempts;
	
	private int totalExamScoreSum;
	
	private int successExamAttempts;
	
	private int examAttemptsWithoutErrors;
	
	//private Set<TotalProgressQuestion> totalProgress;
	
	public UserStatistic(int userId) {
		this.userId = userId;
	}
}
