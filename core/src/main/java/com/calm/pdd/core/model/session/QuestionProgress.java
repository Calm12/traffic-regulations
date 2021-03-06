package com.calm.pdd.core.model.session;

import com.calm.pdd.core.exceptions.UnknownQuestionException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class QuestionProgress implements Serializable {
	
	private static final long serialVersionUID = 8436522977827859517L;
	
	private final List<QuestionProgressUnit> progressUnits;
	
	@Getter
	private String id;
	
	@Getter
	private long startTime;
	
	@Getter
	@Setter
	private Result result;
	
	@Setter(AccessLevel.PACKAGE)
	@Getter
	private SectionType sectionType;
	
	@Setter(AccessLevel.PACKAGE)
	@Getter
	private int sectionId;
	
	@Setter(AccessLevel.PACKAGE)
	@Getter
	private String progressName;
	
	QuestionProgress(List<QuestionProgressUnit> progressUnits) {
		this.progressUnits = progressUnits;
		id = UUID.randomUUID().toString();
		startTime = System.currentTimeMillis();
	}
	
	public QuestionProgressUnit getFirst() {
		try {
			return progressUnits.get(0);
		}
		catch(IndexOutOfBoundsException e) {
			log.error("Empty questions progress!", e);
			throw new UnknownQuestionException("Empty questions progress!");
		}
	}
	
	public QuestionProgressUnit getByNumber(int questionNumber) {
		try {
			return progressUnits.get(questionNumber - 1);
		}
		catch(IndexOutOfBoundsException e) {
			throw new UnknownQuestionException("Wrong question number for this section!");
		}
	}
	
	public boolean hasNextUnanswered(int questionNumber) {
		return progressUnits.stream().skip(questionNumber).anyMatch(q -> q.isUnanswered());
	}
	
	public QuestionProgressUnit getNextUnanswered(int questionNumber) {
		return progressUnits.stream().skip(questionNumber).filter(q -> q.isUnanswered()).findFirst().orElseThrow(() -> new UnknownQuestionException("Please use QuestionProgress::hasNextUnanswered before QuestionProgress::getNextUnanswered."));
	}
	
	public boolean hasUnanswered() {
		return progressUnits.stream().anyMatch(q -> q.isUnanswered());
	}
	
	public QuestionProgressUnit getFirstUnanswered() {
		return progressUnits.stream().filter(q -> q.isUnanswered()).findFirst().orElseThrow(() -> new UnknownQuestionException("Please use QuestionProgress::hasUnanswered before QuestionProgress::getFirstUnanswered."));
	}
	
	public Optional<QuestionProgressUnit> findNextQuestion(int questionNumber) {
		if(hasNextUnanswered(questionNumber)) {
			return Optional.of(getNextUnanswered(questionNumber));
		}
		
		if(hasUnanswered()) {
			return Optional.of(getFirstUnanswered());
		}
		
		return Optional.empty();
	}
	
	public boolean hasTwoErrors() {
		return progressUnits.stream().filter(q -> q.isWrongAnswered()).count() >= 2;
	}
	
	public boolean hasNoErrors() {
		return progressUnits.stream().noneMatch(q -> q.isWrongAnswered());
	}
	
	public List<QuestionProgressUnit> getList() {
		return progressUnits;
	}
	
	public boolean isRandomSet() {
		return sectionType == SectionType.RANDOM;
	}
	
	public boolean isExam() {
		return sectionType == SectionType.EXAM;
	}
	
	public boolean isFixedSection(int sectionId) {
		return this.sectionId == sectionId && sectionType == SectionType.FIXED;
	}
	
	public boolean isCompleted() {
		return result != null;
	}
}
