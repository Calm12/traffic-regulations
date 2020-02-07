package com.calm.pdd.core.services.statistic;

import com.calm.pdd.core.model.entity.Coverage;
import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.entity.User;
import com.calm.pdd.core.model.entity.UserStatistic;
import com.calm.pdd.core.model.repository.UserStatisticRepository;
import com.calm.pdd.core.model.session.QuestionProgressUnit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class QuestionsCoverageHandler {
	
	private UserStatisticRepository userStatisticRepository;
	
	public QuestionsCoverageHandler(UserStatisticRepository userStatisticRepository) {
		this.userStatisticRepository = userStatisticRepository;
	}
	
	@Transactional
	public void updateCoverage(User user, Question question, QuestionProgressUnit progressUnit) {
		UserStatistic statistic = userStatisticRepository
				.findByUserId(user.getId())
				.orElseThrow(() -> new RuntimeException("Wtf?! User [" + user.getId() + "] has not UserStatistic!"));
		
		Optional<Coverage> currentQuestionCoverage = statistic.getCoverages().stream().filter(c -> c.getQuestionId() == question.getId()).findFirst();
		
		if(currentQuestionCoverage.isPresent()) {
			currentQuestionCoverage.get().setCorrect(progressUnit.isCorrectAnswered());
		}
		else {
			statistic.addCoverage(new Coverage(question.getId(), question.getSection().getId(), progressUnit.isCorrectAnswered()));
		}
	}
}
