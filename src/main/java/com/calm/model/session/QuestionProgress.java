package com.calm.model.session;

import com.calm.model.enums.AnswerResult;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class QuestionProgress implements Serializable {
	
	private final int questionId;
	
	private final int questionNumber;
	
	private int answeredNumber;
	
	private AnswerResult answerResult = AnswerResult.NOT_ANSWERED;
}
