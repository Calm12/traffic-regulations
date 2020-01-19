package com.calm.pdd.core.model.session;

import com.calm.pdd.core.exceptions.UnknownQuestionException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

public class QuestionProgress implements Serializable {
	
	private static final long serialVersionUID = 8436522977827859517L;
	
	private final List<QuestionProgressUnit> progressUnits;
	
	@Setter(AccessLevel.PACKAGE)
	@Getter
	private SectionType sectionType;
	
	@Setter(AccessLevel.PACKAGE)
	@Getter
	private int sectionId;
	
	QuestionProgress(List<QuestionProgressUnit> progressUnits) {
		this.progressUnits = progressUnits;
	}
	
	public QuestionProgressUnit getFirst() {
		return progressUnits.get(0); //что если лист пустой?
	}
	
	public QuestionProgressUnit getByNumber(int questionNumber) {
		try {
			return progressUnits.get(questionNumber - 1);
		}
		catch(IndexOutOfBoundsException e) {
			throw new UnknownQuestionException("Wrong question number for this section!");
		}
	}
	
	public List<QuestionProgressUnit> getList() {
		return progressUnits;
	}
	
	public boolean isRandomSet() {
		return sectionType == SectionType.RANDOM;
	}
	
	public boolean isFixedSection(int sectionId) {
		return this.sectionId == sectionId && sectionType == SectionType.FIXED;
	}
}
