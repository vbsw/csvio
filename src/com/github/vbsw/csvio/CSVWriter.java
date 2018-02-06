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

		marshaller.startMarshalling(parser);

		try ( final BufferedWriter writer = Files.newBufferedWriter(filePath,charset,StandardOpenOption.WRITE) ) {
			while ( marshaller.hasLine() ) {
				marshaller.marshallLine(writer);
				writer.newLine();
			}
			marshaller.endMarshalling();
		} catch ( final IOException e ) {
			marshaller.setException(e);
			marshaller.endMarshalling();
		}
	}

	public void writeAbstract ( final Writer writer, final CSVMarshaller marshaller ) {
		final CSVParser parser = getParser();

		marshaller.startMarshalling(parser);

		try {
			while ( marshaller.hasLine() ) {
				marshaller.marshallLine(writer);
				writer.write(System.lineSeparator());
			}
			marshaller.endMarshalling();
		} catch ( final IOException e ) {
			marshaller.setException(e);
			marshaller.endMarshalling();
		}
	}

	protected Charset getCharset ( ) {
		return StandardCharsets.UTF_8;
	}

	protected CSVParser getParser ( ) {
		return new CSVParser();
	}

}
