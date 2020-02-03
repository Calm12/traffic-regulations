package com.calm.pdd.core.model.session;

import com.calm.pdd.core.model.enums.AnswerResult;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@RequiredArgsConstructor
public class QuestionProgressUnit implements Serializable {
	
	private static final long serialVersionUID = 4099747150946973423L;
	
	private final int questionId;
	
	private final int questionNumber;
	
	private int answeredNumber;
	
	private AnswerResult answerResult = AnswerResult.NOT_ANSWERED;
	
	public boolean isAnswered() {
		return answerResult != AnswerResult.NOT_ANSWERED;
	}
	
	public boolean isUnanswered() {
		return !isAnswered();
	}
	
	public boolean isWrongAnswered() {
		return answerResult == AnswerResult.WRONG;
	}
	
	public boolean isCorrectAnswered() {
		return answerResult == AnswerResult.CORRECT;
	}
}
