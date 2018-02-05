/**
 *   Copyright 2018, Vitali Baumtrok <vbsw@mailbox.org>.
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file BSL-1.0.txt or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.csvio;


import java.io.IOException;
import java.io.Reader;
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

	public void readBytes ( final Path filePath, final CSVProcessor csvProcessor ) {
		int bytesReadTotal = 0;
		csvProcessor.startProcessing(csvParser);
		try ( final SeekableByteChannel channel = Files.newByteChannel(filePath,StandardOpenOption.READ) ) {
			int lineBegin = 0;
			int lineEnd = 0;
			int lineNumber = 0;

			fileReader.startFileReading(channel);
			fileReader.readFile();

			while ( fileReader.getBytesRead() > 0 ) {
				lineNumber += 1;
				lineEnd = csvParser.seekAfterLF(fileReader.getBytes(),lineEnd,fileReader.getBytesLength());

				while ( !csvParser.endsWithLF(fileReader.getBytes(),lineEnd,lineBegin) && fileReader.getBytesRead() > 0 ) {
					lineEnd = fileReader.preserveBufferAndEnsureCapacity(lineBegin,lineEnd);
					lineBegin = 0;
					fileReader.readFile();
					lineEnd = csvParser.seekAfterLF(fileReader.getBytes(),lineEnd,fileReader.getBytesLength());
				}
				bytesReadTotal += (lineEnd - lineBegin);
				if ( !csvParser.isWhitespace(fileReader.getBytes(),lineBegin,lineEnd) ) {
					csvProcessor.processCSV(fileReader.getBytes(),lineBegin,lineEnd,lineNumber,bytesReadTotal);
				}
				lineBegin = lineEnd;
			}
			csvProcessor.endProcessing(bytesReadTotal);

		} catch ( final IOException e ) {
			csvProcessor.setException(e);
			csvProcessor.endProcessing(bytesReadTotal);
		}
	}

	public void readChars ( final Reader reader, final CSVProcessor csvProcessor ) {
		int bytesReadTotal = 0;
		csvProcessor.startProcessing(csvParser);
		try {
			char[] buffer = createCharBuffer();
			int charsRead = 0;
			int charsLength = 0;
			int lineBegin = 0;
			int lineEnd = 0;
			int lineNumber = 0;

			charsRead = reader.read(buffer);
			charsLength = charsRead;

			while ( charsRead > 0 ) {
				lineNumber += 1;
				lineEnd = csvParser.seekAfterLF(buffer,lineEnd,charsLength);

				while ( !csvParser.endsWithLF(buffer,lineEnd,lineBegin) && charsRead > 0 ) {
					buffer = preserveBufferAndEnsureCapacity(buffer,lineBegin,lineEnd);
					charsLength = lineEnd - lineBegin;
					lineEnd = charsLength;
					lineBegin = 0;
					charsRead = reader.read(buffer,lineEnd,buffer.length - lineEnd);
					charsLength += charsRead;
					lineEnd = csvParser.seekAfterLF(buffer,lineEnd,charsLength);
				}
				bytesReadTotal += (lineEnd - lineBegin);
				if ( !csvParser.isWhitespace(buffer,lineBegin,lineEnd) ) {
					csvProcessor.processCSV(buffer,lineBegin,lineEnd,lineNumber,bytesReadTotal);
				}
				lineBegin = lineEnd;
			}
			csvProcessor.endProcessing(bytesReadTotal);

		} catch ( final IOException e ) {
			csvProcessor.setException(e);
			csvProcessor.endProcessing(bytesReadTotal);
		}
	}

	protected char[] createCharBuffer ( ) {
		return new char[1024 * 8 - 64 * 8];
	}

	protected char[] createCharBufferExtended ( final char[] chars, final int offset, final int copyLength ) {
		final char[] charsNew = new char[chars.length * 2];
		System.arraycopy(chars,offset,charsNew,0,copyLength);
		return charsNew;
	}

	private char[] preserveBufferAndEnsureCapacity ( char[] chars, final int fromLeft, final int toRight ) {
		if ( fromLeft < toRight ) {
			if ( fromLeft > 0 ) {
				System.arraycopy(chars,fromLeft,chars,0,toRight - fromLeft);
			} else if ( toRight >= chars.length ) {
				chars = createCharBufferExtended(chars,0,chars.length);
			}
		}
		return chars;
	}

}
