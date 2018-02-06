/**
 *   Copyright 2018, Vitali Baumtrok <vbsw@mailbox.org>.
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file BSL-1.0.txt or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.csvio;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;


/**
 * The writer to write to a file or an abstract destination.
 * 
 * @author Vitali Baumtrok
 */
public class CSVWriter {

	public void writeFile ( final Path filePath, final CSVMarshaller marshaller ) {
		final CSVParser parser = getParser();
		final Charset charset = getCharset();

		marshaller.startMarshalling(parser,charset);

		try ( final BufferedWriter writer = getBufferedWriter(filePath,charset) ) {
			while ( marshaller.hasLine() ) {
				marshaller.marshallLine(writer);
				writer.newLine();
			}
		} catch ( final IOException e ) {
			marshaller.setException(e);
		} finally {
			marshaller.endMarshalling();
		}
	}

	public void writeAbstract ( final Writer writer, final CSVMarshaller marshaller ) {
		final CSVParser parser = getParser();

		marshaller.startMarshalling(parser,null);

		try {
			while ( marshaller.hasLine() ) {
				marshaller.marshallLine(writer);
				writer.write(System.lineSeparator());
			}

		} catch ( final IOException e ) {
			marshaller.setException(e);
		} finally {
			try {
				writer.close();
			} catch ( final IOException e ) {
				marshaller.setException(e);
			}
			marshaller.endMarshalling();
		}
	}

	protected Charset getCharset ( ) {
		return StandardCharsets.UTF_8;
	}

	protected CSVParser getParser ( ) {
		return new CSVParser();
	}

	protected BufferedWriter getBufferedWriter ( final Path filePath, final Charset charset ) throws IOException {
		final BufferedWriter writer = Files.newBufferedWriter(filePath,charset,StandardOpenOption.WRITE);
		return writer;
	}

}
