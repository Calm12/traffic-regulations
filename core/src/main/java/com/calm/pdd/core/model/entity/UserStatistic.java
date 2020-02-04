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
	
	public void addToTotalTestingTime(int newDuration) {
		totalTestingTime += newDuration;
	}
	
	public void incrementSuccessExamAttempts() {
		successExamAttempts += 1;
	}
	
	public void addToTotalExamScoreSum(int newScore) {
		totalTestingTime += newScore;
	}
	
	public void incrementExamAttemptsWithoutErrors() {
		examAttemptsWithoutErrors += 1;
	}
	
	public void incrementTotalExamAttempts() {
		totalExamAttempts += 1;
	}
	
	public int getAverageExamScore() {
		if(totalExamAttempts == 0) {
			return 0;
		}
		
		return totalExamScoreSum / totalExamAttempts;
	}
}
