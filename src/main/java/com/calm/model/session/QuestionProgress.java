package com.calm.model.session;

import com.calm.model.enums.AnswerResult;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class QuestionProgress implements Serializable {
	
	private static final long serialVersionUID = -1813352285109896976L;
	
	private final int questionId;
	
	private final int questionNumber;
	
	private int answeredNumber;
	
	private AnswerResult answerResult = AnswerResult.NOT_ANSWERED;
	
	public boolean isAnswered() {
		return answerResult != AnswerResult.NOT_ANSWERED;
	}
	
	public boolean isWrongAnswered() {
		return answerResult == AnswerResult.WRONG;
	}
}
