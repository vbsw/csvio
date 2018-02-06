/**
 *   Copyright 2018, Vitali Baumtrok <vbsw@mailbox.org>.
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file BSL-1.0.txt or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.csvio;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;


public class Main extends CSVByteProcessor {

	private CSVParser parser;

	public static void main ( String[] args ) {
		final Path filePath = Paths.get(System.getProperty("user.home"),"csvio.test.csv");
		final String content = "asdf,qwer,yxcv";

		final Main processor = new Main();
		final CSVReader reader = new CSVReader();

		try {
			Files.write(filePath,content.getBytes());
			reader.readFile(filePath,processor);
			Files.delete(filePath);

		} catch ( IOException e ) {
		}
	}

	@Override
	public void startProcessing ( CSVParser csvParser ) {
		this.parser = csvParser;
	}

	@Override
	public void processLine ( byte[] bytes, int from, int to, int lineNumber, int bytesReadTotal ) {
		final String[] values = this.parser.splitValues(bytes,from,to);
		System.out.println("values: " + Arrays.toString(values));
	}

	@Override
	public void endProcessing ( int bytesReadTotal ) {
		System.out.println("bytes read: " + bytesReadTotal);
	}

	@Override
	public void setException ( IOException e ) {
		e.printStackTrace();
	}

}
