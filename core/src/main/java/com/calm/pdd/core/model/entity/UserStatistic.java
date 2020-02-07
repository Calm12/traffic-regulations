package com.calm.pdd.core.model.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@ToString(exclude = {"coverages"})
@EqualsAndHashCode(of = {"userId"})
@NoArgsConstructor
@Entity
@Table(name = "user_statistic")
public class UserStatistic {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(unique=true)
	private int userId;
	
	private int topExamTime = Integer.MAX_VALUE;
	
	private int totalTestingTime;
	
	private int totalExamAttempts;
	
	private int totalExamScoreSum;
	
	private int successExamAttempts;
	
	private int examAttemptsWithoutErrors;
	
	@OneToMany(mappedBy="statistic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Coverage> coverages;
	
	public UserStatistic(int userId) {
		this.userId = userId;
	}
	
	public void addCoverage(Coverage coverage) {
		if(this.coverages == null) {
			this.coverages = new HashSet<>();
		}
		
		this.coverages.add(coverage);
		if (coverage.getStatistic() != this) {
			coverage.setStatistic(this);
		}
	}
	
	public void addToTotalTestingTime(int newDuration) {
		totalTestingTime += newDuration;
	}
	
	public void incrementSuccessExamAttempts() {
		successExamAttempts += 1;
	}
	
	public void addToTotalExamScoreSum(int newScore) {
		totalExamScoreSum += newScore;
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
