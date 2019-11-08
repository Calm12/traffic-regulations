package com.calm.parsers;

import com.calm.config.AppConfig;
import com.calm.model.entity.Section;
import com.calm.model.repository.SectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;

//@Component
@Slf4j
public class SectionParser implements IParser{
	
	private final SectionRepository sectionRepository;
	private Map<String, String> cookies;
	private final AppConfig appConfig;
	
	public SectionParser(SectionRepository sectionRepository, Map<String, String> cookies, AppConfig appConfig) throws IOException {
		this.sectionRepository = sectionRepository;
		this.cookies = cookies;
		this.appConfig = appConfig;
		log.warn("SECTIONS PARSE START");
		//parse();
	}
	
	@Override
	public void parse() throws IOException {
		Response res = Jsoup.
				connect("https://green-way.com.ua/uk/test-pdd/sections")
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
	}
}
