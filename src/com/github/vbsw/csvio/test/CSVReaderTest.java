/**
 *   Copyright 2018, Vitali Baumtrok <vbsw@mailbox.org>.
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file BSL-1.0.txt or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.csvio.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import com.github.vbsw.csvio.CSVParser;
import com.github.vbsw.csvio.CSVProcessor;
import com.github.vbsw.csvio.CSVReader;


/**
 * @author Vitali Baumtrok
 */
class CSVReaderTest extends CSVProcessor {

	private CSVParser parser = null;
	private IOException exeption = null;
	private int bytesReadTotal = -1;
	private int[] lineNumbers = null;
	private int entryCounter = 0;
	private String[][] values = null;

	@Test
	void testA ( ) {
		final String content = "asdf,qwer,yxcv\n\ntt,uu,vv,ww\n\n\n\nxyz";
		final Path filePath = Paths.get(System.getProperty("user.home"),"csvio.test.csv");
		final CSVReader reader = new CSVReader();
		final CSVReaderTest csvProcessor = new CSVReaderTest();

		createFile(filePath,content);
		reader.readFile(filePath,csvProcessor);
		deleteFile(filePath);

		assertNotEquals(null,csvProcessor.parser);
		assertNotEquals(null,csvProcessor.lineNumbers);
		assertNotEquals(null,csvProcessor.values);
		assertEquals(null,csvProcessor.exeption);
		assertEquals(content.length(),csvProcessor.bytesReadTotal);
		assertEquals(4,csvProcessor.values.length);
		assertEquals(3,csvProcessor.values[0].length);
		assertEquals(4,csvProcessor.values[1].length);
		assertEquals(1,csvProcessor.values[2].length);
		assertEquals(4,csvProcessor.lineNumbers.length);
		assertEquals("asdf",csvProcessor.values[0][0]);
		assertEquals("qwer",csvProcessor.values[0][1]);
		assertEquals("yxcv",csvProcessor.values[0][2]);
		assertEquals("tt",csvProcessor.values[1][0]);
		assertEquals("uu",csvProcessor.values[1][1]);
		assertEquals("vv",csvProcessor.values[1][2]);
		assertEquals("ww",csvProcessor.values[1][3]);
		assertEquals("xyz",csvProcessor.values[2][0]);
		assertEquals(1,csvProcessor.lineNumbers[0]);
		assertEquals(3,csvProcessor.lineNumbers[1]);
		assertEquals(7,csvProcessor.lineNumbers[2]);
		assertEquals(-1,csvProcessor.lineNumbers[3]);
		assertEquals((String) null,csvProcessor.values[3]);
	}

	private void createFile ( final Path filePath, final String str ) {
		try {
			Files.write(filePath,str.getBytes());
		} catch ( IOException e ) {
		}
	}

	private void deleteFile ( final Path filePath ) {
		try {
			Files.delete(filePath);
		} catch ( IOException e ) {
		}
	}

	@Override
	public void startProcessing ( final CSVParser csvParser ) {
		this.parser = csvParser;
		this.lineNumbers = new int[] { -1, -1, -1, -1 };
		this.values = new String[this.lineNumbers.length][];
	}

	@Override
	public void processCSV ( final byte[] bytes, final int fromLeft, final int toRight, final int lineNumber, final int bytesReadTotal ) {
		if ( this.entryCounter < this.lineNumbers.length ) {
			final byte[][] values = this.parser.splitValues(bytes,fromLeft,toRight);
			this.lineNumbers[this.entryCounter] = lineNumber;
			this.values[this.entryCounter] = new String[values.length];
			for ( int i = 0; i < values.length; i += 1 ) {
				this.values[this.entryCounter][i] = new String(values[i]);
			}
		}
		this.entryCounter += 1;
		this.bytesReadTotal = bytesReadTotal;
	}

	@Override
	public void endProcessing ( final int bytesReadTotal ) {
		this.bytesReadTotal = bytesReadTotal;
	}

	@Override
	public void setException ( final IOException e ) {
		this.exeption = e;
	}

	@Override
	public String toString ( ) {
		final StringBuilder stringBuilder = new StringBuilder();
		for ( int i = 0; i < this.values.length; i += 1 ) {
			if ( this.values[i] != null ) {
				stringBuilder.append('[');
				for ( int j = 0; j < this.values[i].length; j += 1 ) {
					if ( j > 0 ) {
						stringBuilder.append(',');
					}
					stringBuilder.append(new String(this.values[i][j]));
				}
				stringBuilder.append(']');
				stringBuilder.append('\n');
			}
		}
		return stringBuilder.toString();
	}

}
