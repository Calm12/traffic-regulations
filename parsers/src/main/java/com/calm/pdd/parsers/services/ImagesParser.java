package com.calm.pdd.parsers.services;

import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.repository.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Profile("parser")
@Component
@Slf4j
public class ImagesParser implements IParser{
	
	private final QuestionRepository questionRepository;
	
	public ImagesParser(QuestionRepository questionRepository) {
		this.questionRepository = questionRepository;
		log.info("Images parser ready");
	}
	
	@Override
	public void parse() {
		log.info("Images urls parsing from js start");
		List<Question> allQuestions = questionRepository.findAll();
		
		int hasImg = 0;
		int noImg = 0;
		
		for(Question question : allQuestions) {
			String imageJs = question.getImageJs();
			
			Pattern patternHasImg = Pattern.compile("https:\\/\\/green-way\\.com\\.ua\\/storage\\/app\\/uploads\\/public.*(.jpg)", Pattern.MULTILINE);
			Pattern patternNoImg = Pattern.compile("https:\\/\\/green-way\\.com\\.ua\\/themes\\/greenway\\/assets\\/images\\/no_image_uk\\.png", Pattern.MULTILINE);
			
			Matcher matcherHasImg = patternHasImg.matcher(imageJs);
			while(matcherHasImg.find()) {
				question.setImageUrl(matcherHasImg.group(0));
				hasImg++;
			}
			
			Matcher matcherNoImg = patternNoImg.matcher(imageJs);
			while(matcherNoImg.find()) {
				question.setImageUrl(matcherNoImg.group(0));
				noImg++;
			}
			
			questionRepository.save(question);
		}
		log.info("Good img questions: " + (hasImg+noImg));
		log.info("Expected img questions: " + questionRepository.count());
		log.info("Images urls parsing from js end");
	}
	
	public void download() throws IOException {
		log.info("Images downloading start");
		List<Question> allQuestions = questionRepository.findAll();
		
		int downloaded = 0;
		
		for(Question question : allQuestions) {
			FileUtils.copyURLToFile(new URL(question.getImageUrl()), new File(String.format("images/%d.jpg", question.getId())));
			downloaded++;
		}
		log.info("Downloaded: " + downloaded);
		log.info("Expected: " + questionRepository.count());
		log.info("Images downloading end");
	}
}
