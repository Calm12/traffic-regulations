package com.calm.pdd.parsers.services;

import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.repository.QuestionRepository;
import com.calm.pdd.parsers.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@Component
@Slf4j
public class ImagesParser implements IParser{
	
	private final QuestionRepository questionRepository;
	private final AppConfig appConfig;
	
	public ImagesParser(QuestionRepository questionRepository, AppConfig appConfig) throws IOException {
		this.questionRepository = questionRepository;
		this.appConfig = appConfig;
		log.warn("IMAGES PARSE START");
		//parse();
		//parseImagesUrlsFromJs();
	}
	
	@Override
	public void parse() throws IOException {
		List<Question> allQuestions = questionRepository.findAll();
		
		int downloaded = 0;
		
		for(Question question : allQuestions) {
			FileUtils.copyURLToFile(new URL(question.getImageUrl()), new File(String.format("images/%d.jpg", question.getId())));
			downloaded++;
		}
		log.info("Downloaded: " + downloaded);
		log.info("Expected: " + questionRepository.count());
	}
	
	public void parseImagesUrlsFromJs() {
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
	}
}
