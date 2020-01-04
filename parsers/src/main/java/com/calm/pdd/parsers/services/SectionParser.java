package com.calm.pdd.parsers.services;

import com.calm.pdd.parsers.config.AppConfig;
import com.calm.pdd.core.model.entity.Section;
import com.calm.pdd.core.model.repository.SectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;

@Profile("parser")
@Component
@Slf4j
public class SectionParser implements IParser{
	
	private final SectionRepository sectionRepository;
	private Map<String, String> cookies;
	private final AppConfig appConfig;
	
	public SectionParser(SectionRepository sectionRepository, Map<String, String> cookies, AppConfig appConfig) {
		this.sectionRepository = sectionRepository;
		this.cookies = cookies;
		this.appConfig = appConfig;
		log.info("Sections parser ready");
	}
	
	@Override
	public void parse() throws IOException {
		log.info("Sections parsing start");
		Response res = Jsoup.
				connect(String.format("%s/test-pdd/sections", appConfig.getParsers().getUrl()))
				.cookies(cookies)
				.execute();
		
		cookies = res.cookies();
		
		Document document = res.parse();
		
		Elements select = document.body().select(".section");
		
		for(Element element : select) {
			if(element.parent().hasClass("disabled")) {
				continue;
			}
			
			if(Arrays.asList("82", "83").contains(element.id())) {
				continue;
			}
			
			String[] text = element.select(".info-text").text().split(Pattern.quote(". "));
			sectionRepository.save(new Section(Integer.parseInt(element.id()), text[0], text[1]));
		}
		log.info("Sections parsing end");
	}
}
