package com.github.vbsw.csvio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
		System.out.print("values:");
		for ( int i = 0; i < values.length; i += 1 ) {
			System.out.print(" " + values[i]);
		}
		System.out.println();
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
