package com.calm.pdd.core.services.statistic;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class SectionCoverage implements Serializable {
	private static final long serialVersionUID = 6421295448899829334L;
	
	private final int sectionId;
	private final String sectionName;
	private final int totalQuestions;
	private final int correctAnswered;
	private final int wrongAnswered;
	private final int totalAnswered;
}
