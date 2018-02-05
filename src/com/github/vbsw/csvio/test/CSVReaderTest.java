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
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import com.github.vbsw.csvio.CSVByteProcessor;
import com.github.vbsw.csvio.CSVCharProcessor;
import com.github.vbsw.csvio.CSVParser;
import com.github.vbsw.csvio.CSVReader;


/**
 * @author Vitali Baumtrok
 */
class CSVReaderTest {

	@Test
	void testA ( ) {
		final String content = "asdf,qwer,yxcv\n\ntt,uu,vv,ww\n\n\n\nxyz";
		final Path filePath = Paths.get(System.getProperty("user.home"),"csvio.test.csv");
		final CSVReader reader = new CSVReader();
		final TestByteProcessor processor = new TestByteProcessor();

		createFile(filePath,content);
		reader.readFile(filePath,processor);
		deleteFile(filePath);

		assertNotEquals(null,processor.parser);
		assertNotEquals(null,processor.lineNumbers);
		assertNotEquals(null,processor.values);
		assertEquals(null,processor.exeption);
		assertEquals(content.length(),processor.bytesReadTotal);
		assertEquals(4,processor.values.length);
		assertEquals(3,processor.values[0].length);
		assertEquals(4,processor.values[1].length);
		assertEquals(1,processor.values[2].length);
		assertEquals(4,processor.lineNumbers.length);
		assertEquals("asdf",processor.values[0][0]);
		assertEquals("qwer",processor.values[0][1]);
		assertEquals("yxcv",processor.values[0][2]);
		assertEquals("tt",processor.values[1][0]);
		assertEquals("uu",processor.values[1][1]);
		assertEquals("vv",processor.values[1][2]);
		assertEquals("ww",processor.values[1][3]);
		assertEquals("xyz",processor.values[2][0]);
		assertEquals(1,processor.lineNumbers[0]);
		assertEquals(3,processor.lineNumbers[1]);
		assertEquals(7,processor.lineNumbers[2]);
		assertEquals(-1,processor.lineNumbers[3]);
		assertEquals((String) null,processor.values[3]);
	}

	@Test
	void testB ( ) {
		final String content = "asdf,qwer,yxcv\n\ntt,uu,vv,ww\n\n\n\nxyz";
		final CharReader charReader = new CharReader(content);
		final CSVReader reader = new CSVReader();
		final TestCharProcessor processor = new TestCharProcessor();

		reader.readAbstract(charReader,processor);

		assertNotEquals(null,processor.parser);
		assertNotEquals(null,processor.lineNumbers);
		assertNotEquals(null,processor.values);
		assertEquals(null,processor.exeption);
		assertEquals(content.length(),processor.bytesReadTotal);
		assertEquals(4,processor.values.length);
		assertEquals(3,processor.values[0].length);
		assertEquals(4,processor.values[1].length);
		assertEquals(1,processor.values[2].length);
		assertEquals(4,processor.lineNumbers.length);
		assertEquals("asdf",processor.values[0][0]);
		assertEquals("qwer",processor.values[0][1]);
		assertEquals("yxcv",processor.values[0][2]);
		assertEquals("tt",processor.values[1][0]);
		assertEquals("uu",processor.values[1][1]);
		assertEquals("vv",processor.values[1][2]);
		assertEquals("ww",processor.values[1][3]);
		assertEquals("xyz",processor.values[2][0]);
		assertEquals(1,processor.lineNumbers[0]);
		assertEquals(3,processor.lineNumbers[1]);
		assertEquals(7,processor.lineNumbers[2]);
		assertEquals(-1,processor.lineNumbers[3]);
		assertEquals((String) null,processor.values[3]);
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

	private static class TestByteProcessor extends CSVByteProcessor {

		CSVParser parser = null;
		IOException exeption = null;
		int bytesReadTotal = -1;
		int[] lineNumbers = null;
		int entryCounter = 0;
		String[][] values = null;

		@Override
		public void startProcessing ( CSVParser csvParser ) {
			this.parser = csvParser;
			this.lineNumbers = new int[] { -1, -1, -1, -1 };
			this.values = new String[this.lineNumbers.length][];
		}

		@Override
		public void processCSV ( byte[] bytes, int fromLeft, int toRight, int lineNumber, int bytesReadTotal ) {
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
		public void endProcessing ( int bytesReadTotal ) {
			this.bytesReadTotal = bytesReadTotal;
		}

		@Override
		public void setException ( IOException e ) {
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

	private static class TestCharProcessor extends CSVCharProcessor {

		CSVParser parser = null;
		IOException exeption = null;
		int bytesReadTotal = -1;
		int[] lineNumbers = null;
		int entryCounter = 0;
		String[][] values = null;

		@Override
		public void startProcessing ( CSVParser csvParser ) {
			this.parser = csvParser;
			this.lineNumbers = new int[] { -1, -1, -1, -1 };
			this.values = new String[this.lineNumbers.length][];
		}

		@Override
		public void processCSV ( char[] chars, int fromLeft, int toRight, int lineNumber, int bytesReadTotal ) {
			if ( this.entryCounter < this.lineNumbers.length ) {
				final char[][] values = this.parser.splitValues(chars,fromLeft,toRight);
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
		public void endProcessing ( int bytesReadTotal ) {
			this.bytesReadTotal = bytesReadTotal;
		}

		@Override
		public void setException ( IOException e ) {
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
	
	private static class CharReader extends Reader {

		private final String content;
		private boolean contentRead;

		public CharReader ( final String content ) {
			this.content = content;
		}

		@Override
		public int read ( final char[] cbuf, final int off, final int len ) throws IOException {
			final int length;
			if ( contentRead == false ) {
				final char[] chars = content.toCharArray();
				length = chars.length <= len ? chars.length : len;
				System.arraycopy(chars,0,cbuf,off,length);
			} else {
				length = 0;
			}
			contentRead = true;
			return length;
		}

		@Override
		public void close ( ) throws IOException {
		}

	}

}
