package com.calm.pdd.parsers;

import com.calm.pdd.parsers.services.ImagesParser;
import com.calm.pdd.parsers.services.QuestionsParser;
import com.calm.pdd.parsers.services.SectionParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@Slf4j
@SpringBootApplication(scanBasePackages = "com.calm.pdd")
public class ParsersApplication implements CommandLineRunner {
	
	private final SectionParser sectionParser;
	private final QuestionsParser questionsParser;
	private final ImagesParser imagesParser;
	
	public ParsersApplication(SectionParser sectionParser, QuestionsParser questionsParser, ImagesParser imagesParser) {
		this.sectionParser = sectionParser;
		this.questionsParser = questionsParser;
		this.imagesParser = imagesParser;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ParsersApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws IOException {
		if(args.length == 0) {
			printHelpMessage();
			
			return;
		}
		
		switch(args[0]) {
			case "1":
				sectionParser.parse();
				break;
			case "2":
				questionsParser.parse();
				break;
			case "3":
				imagesParser.parse();
				break;
			case "4":
				imagesParser.download();
				break;
			case "5":
				questionsParser.answerCodesFix();
				break;
			default:
				log.error("Invalid argument " + args[0]);
				printHelpMessage();
				break;
		}
	}
	
	private void printHelpMessage() {
		log.warn("Specify argument: \n 1 - parse sections \n 2 - parse questions \n 3 - parse images urls from js \n 4 - download images \n 5 - decode answers codes (if its really need!)");
	}
}
