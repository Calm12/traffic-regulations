package com.calm.pdd.core.model.session;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {
	
	private static final long serialVersionUID = -7451940063156561339L;
	
	private int total;
	private int correct;
	private int wrong;
	private int correctRate;
	private int wrongRate;
	private int duration;
	private String formattedDuration;
}
