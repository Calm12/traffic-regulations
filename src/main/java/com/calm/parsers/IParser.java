package com.calm.parsers;

import java.io.IOException;

public interface IParser {
	
	/*
	 * Чтобы заюзать парсер, нужно раскоментить @Component. Очень осторожно, потому что лишний парсинг насрет в базу лишним.
	 * И надо както это нормально организовать, вынести в batch наверное.
	 */
	void parse() throws IOException;
}
