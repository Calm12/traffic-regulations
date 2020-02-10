package com.calm.pdd.core.services.statistic;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class Statistic implements Serializable {
	private static final long serialVersionUID = 7866159825597161546L;
	
	private final String totalTestingTime;
	private final String topExamTime;
	private final int totalExamAttempts;
	private final int successExamAttempts;
	private final int examAttemptsWithoutErrors;
	private final int averageExamScore;
	
	private final int completedPartRate;
	private final int completedPartSuccessRate;
	
	private final List<SectionCoverage> coverage;
}
