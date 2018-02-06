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
import java.io.Writer;
import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;

import com.github.vbsw.csvio.CSVMarshaller;
import com.github.vbsw.csvio.CSVParser;
import com.github.vbsw.csvio.CSVWriter;


/**
 * @author Vitali Baumtrok
 */
class CSVWriterTest {

	private static final String[] content = new String[] { "asdf,qwer,yxcv", "tt,uu,vv,ww", "xyz" };
	private static final String result = content[0] + System.lineSeparator() + content[1] + System.lineSeparator() + content[2] + System.lineSeparator();

	@Test
	void testA ( ) {
		final CSVWriter csvWriter = new CSVWriter();
		final TestWriter abstractWriter = new TestWriter();
		final TestMarshaller marshaller = new TestMarshaller();

		csvWriter.writeAbstract(abstractWriter,marshaller);

		assertNotEquals(null,marshaller.parser);
		assertNotEquals(null,marshaller.exeption);
		assertEquals(null,marshaller.charset);
		assertEquals(1,marshaller.startCalled);
		assertEquals(1,marshaller.endCalled);
		assertEquals(3,marshaller.lineNumber);
		assertEquals(true,abstractWriter.fileContent.equals(result));
	}

	private static class TestMarshaller extends CSVMarshaller {

		CSVParser parser = null;
		Charset charset = null;
		IOException exeption = null;
		int lineNumber = 0;
		int startCalled = 0;
		int endCalled = 0;

		@Override
		public void startMarshalling ( final CSVParser parser, final Charset charset ) {
			this.parser = parser;
			this.charset = charset;
			this.startCalled += 1;
		}

		@Override
		public boolean hasLine ( ) {
			return lineNumber < 3;
		}

		@Override
		public void marshallLine ( final Writer writer ) throws IOException {
			if ( lineNumber < content.length ) {
				writer.write(content[lineNumber]);
			}
			lineNumber += 1;
		}

		@Override
		public void endMarshalling ( ) {
			endCalled += 1;
		}

		@Override
		public void setException ( final IOException e ) {
			this.exeption = e;
		}
	}

	private static final class TestWriter extends Writer {

		private String fileContent = "";

		@Override
		public void write ( final String str ) throws IOException {
			fileContent += str;
		}

		@Override
		public void write ( char[] cbuf, int off, int len ) throws IOException {
		}

		@Override
		public void flush ( ) throws IOException {
		}

		@Override
		public void close ( ) throws IOException {
			throw new IOException("test exception");
		}

	}

}
