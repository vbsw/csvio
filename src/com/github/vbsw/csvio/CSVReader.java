/**
 *   Copyright 2018, Vitali Baumtrok <vbsw@mailbox.org>.
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file BSL-1.0.txt or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.csvio;


import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;


/**
 * @author Vitali Baumtrok
 */
public class CSVReader {

	protected final FileReader fileReader;
	protected final CSVParser csvParser;

	public CSVReader ( ) {
		fileReader = createFileReader();
		csvParser = createCSVParser();
	}

	public CSVReader ( final FileReader fileReader ) {
		this.fileReader = fileReader;
		this.csvParser = createCSVParser();
	}

	public CSVReader ( final FileReader fileReader, final CSVParser cSVParser ) {
		this.fileReader = fileReader;
		this.csvParser = cSVParser;
	}

	protected CSVParser createCSVParser ( ) {
		final CSVParser csvParser = new CSVParser();
		return csvParser;
	}

	protected FileReader createFileReader ( ) {
		final FileReader fileReader = new FileReader();
		return fileReader;
	}

	public void readFile ( final Path filePath, final CSVProcessor csvProcessor ) {
		csvProcessor.prepareProcessing(csvParser);
		try ( final SeekableByteChannel channel = Files.newByteChannel(filePath,StandardOpenOption.READ) ) {
			int lineBegin = 0;
			int lineEnd = 0;
			int lineNumber = 0;

			fileReader.prepareFileReading(channel);
			fileReader.readFile();

			while ( fileReader.getBytesRead() > 0 ) {
				lineNumber += 1;
				lineEnd = csvParser.seekAfterLF(fileReader.getBytes(),lineEnd,fileReader.getBytesLength());

				while ( !csvParser.endsWithLF(fileReader.getBytes(),lineEnd,lineBegin) && fileReader.isBufferFull() ) {
					lineEnd = fileReader.preserveBufferAndEnsureCapacity(lineBegin,lineEnd);
					lineBegin = 0;
					fileReader.readFile();
					lineEnd = csvParser.seekAfterLF(fileReader.getBytes(),lineEnd,fileReader.getBytesLength());
				}
				lineBegin = csvParser.seekContent(fileReader.getBytes(),lineBegin,lineEnd);
				if ( !csvParser.isWhitespace(fileReader.getBytes(),lineBegin,lineEnd) ) {
					csvProcessor.processCSV(fileReader.getBytes(),lineBegin,lineEnd,lineNumber);
				}
				lineBegin = lineEnd;
			}

		} catch ( final IOException e ) {
			csvProcessor.setException(e);
		}
	}

}
