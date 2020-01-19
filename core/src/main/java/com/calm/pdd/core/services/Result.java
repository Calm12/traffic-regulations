package com.calm.pdd.core.services;

import lombok.Data;

@Data
public class Result {
	private int total;
	private int correct;
	private int wrong;
	private int correctRate;
	private int wrongRate;
	private long duration;
	private String formattedDuration;
}
