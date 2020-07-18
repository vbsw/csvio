/**
 *   Copyright 2020, Vitali Baumtrok <vbsw@mailbox.org>.
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file BSL-1.0.txt or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */

package com.github.vbsw.csvio;


import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * @author Vitali Baumtrok
 */
public class CSVFileWriter implements AutoCloseable {

	protected final BufferedWriter writer;
	protected final char[] delimiter;
	protected final IOStats stats;

	public CSVFileWriter (final Path path) throws FileNotFoundException {
		this(createBufferedWriter(path,StandardCharsets.UTF_8),new char[] {','},new IOStats());
	}

	public CSVFileWriter (final Path path, final char[] delimiter) throws FileNotFoundException {
		this(createBufferedWriter(path,StandardCharsets.UTF_8),delimiter,new IOStats());
	}

	public CSVFileWriter (final Path path, final char[] delimiter, final Charset charset) throws FileNotFoundException {
		this(createBufferedWriter(path,charset),delimiter,new IOStats());
	}

	public CSVFileWriter (final Path path, final char[] delimiter, final Charset charset, final IOStats stats) throws FileNotFoundException {
		this(createBufferedWriter(path,charset),delimiter,stats);
	}

	public CSVFileWriter (final BufferedWriter writer, final char[] delimiter, final IOStats stats) {
		this.writer = writer;
		this.delimiter = delimiter;
		this.stats = stats;
		this.stats.reset();
	}

	public IOStats getStats() {
		return stats;
	}

	public void write(String... fields) throws IOException {
		for (int i = 0; i < fields.length; i++) {
			if (i > 0)
				writer.write(delimiter);
			writer.write(fields[i].toCharArray());
		}
		writer.newLine();
	}

	public void write(final char[] fields) throws IOException {
		writer.write(fields);
		writer.newLine();
	}

	public void close() throws IOException {
		writer.close();
	}

	protected static BufferedWriter createBufferedWriter(final Path path, final Charset charset) throws FileNotFoundException {
		final FileOutputStream stream = new FileOutputStream(path.toFile());
		final OutputStreamWriter streamWriter = new OutputStreamWriter(stream,charset);
		final BufferedWriter writer = new BufferedWriter(streamWriter);
		return writer;
	}

}
