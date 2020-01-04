package com.calm.pdd.parsers.services;

import com.calm.pdd.parsers.config.AppConfig;
import com.calm.pdd.core.model.entity.Answer;
import com.calm.pdd.core.model.entity.Question;
import com.calm.pdd.core.model.entity.Section;
import com.calm.pdd.core.model.repository.AnswerRepository;
import com.calm.pdd.core.model.repository.QuestionRepository;
import com.calm.pdd.core.model.repository.SectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Profile("parser")
@Component
@Slf4j
public class QuestionsParser implements IParser{
	
	private final SectionRepository sectionRepository;
	private final QuestionRepository questionRepository;
	private final AnswerRepository answerRepository;
	private Map<String, String> cookies;
	private final AppConfig appConfig;
	
	public QuestionsParser(SectionRepository sectionRepository, QuestionRepository questionRepository, AnswerRepository answerRepository, Map<String, String> cookies, AppConfig appConfig) {
		this.sectionRepository = sectionRepository;
		this.questionRepository = questionRepository;
		this.answerRepository = answerRepository;
		this.cookies = cookies;
		this.appConfig = appConfig;
		log.info("Questions parser ready");
	}
	
	@Override
	public void parse() throws IOException {
		log.info("Questions parsing start");
		List<Section> sections = sectionRepository.findAll();
		
		for(Section section : sections) {
			
			Response res = Jsoup.
					connect(String.format("%s/test-pdd/question/%d", appConfig.getParsers().getUrl(), section.getId()))
					.cookies(cookies)
					.maxBodySize(Integer.MAX_VALUE)
					.execute();
			
			Document document = res.parse();
			
			//if need html from file instead http
			/*File file = new File("html/36_x.html");
			String html = FileUtils.readFileToString(file, "UTF-8");
			Document document = Jsoup.parse(html);*/
			
			String html = document.outerHtml();
			File file = new File(String.format("html/%d.html", section.getId()));
			FileUtils.writeStringToFile(file, html, "UTF-8");
			
			Elements questionsElements = document.body().select(".pagination").get(0).children();
			for(Element questionElement: questionsElements) {
				String questionHtmlId = questionElement.attr("href");
				log.info(questionHtmlId);
				
				int id = Integer.parseInt(questionHtmlId.split("_")[1]);
				int number = Integer.parseInt(questionElement.text());
				
				Element questionDiv = document.body().selectFirst(questionHtmlId);
				String text = questionDiv.selectFirst(".text_question").child(0).text();
				Element imageDiv = questionDiv.selectFirst(".question-img");
				
				String imageJs = imageDiv.select("script").first().html();
				
				Element answersUl = questionDiv.selectFirst(".answers");
				
				String answerCodeSource = answersUl.parent().select("script").first().html();
				int answerCode = Integer.parseInt(StringUtils.chop(answerCodeSource.split(Pattern.quote(" = "))[1]));
				int answer = ((answerCode - 3) / 7) + 1 - id;
				
				Question question = new Question(id, section, number, text, answerCode, answer);
				question.setImageJs(imageJs);
				
				for(Element answerLi : answersUl.children()) {
					int answerNumber = Integer.parseInt((answerLi.id().split("a"))[1]);
					String answerText = answerLi.child(0).text();
					question.addAnswer(new Answer(answerNumber, answerText));
				}
				
				questionRepository.save(question);
				answerRepository.saveAll(question.getAnswers());
			}
			
		}
		log.info("Questions parsing end");
	}
	
	public void answerCodesFix() {
		List<Question> questions = questionRepository.findAll();
		for(Question question : questions) {
			int answer = ((question.getAnswerCode() - 3) / 7) + 1 - question.getId();
			question.setAnswer(answer);
			questionRepository.save(question);
		}
		log.info("Answers codes fixed");
	}
}
