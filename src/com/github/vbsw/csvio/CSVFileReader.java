/**
 *   Copyright 2020, Vitali Baumtrok <vbsw@mailbox.org>.
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file BSL-1.0.txt or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */

package com.github.vbsw.csvio;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

/**
 * @author Vitali Baumtrok
 */
public class CSVFileReader implements AutoCloseable, Iterable<String[]> {

	protected final InputStream inputStream;
	protected final byte[] delimiter;
	protected final Charset charset;
	protected final String[] header;
	protected final IOStats stats;

	protected int[] headerMapping;

	public CSVFileReader (final Path path, final byte[] delimiter, final Charset charset, final String[] header) throws IOException {
		this(path,delimiter,charset,header,new IOStats());
	}

	public CSVFileReader (final Path path, final byte[] delimiter, final Charset charset, final String[] header, final IOStats stats) throws IOException {
		this.inputStream = Files.newInputStream(path);
		this.delimiter = delimiter;
		this.charset = charset;
		this.header = header;
		this.stats = stats;
	}

	@Override
	public void close() throws IOException {
		inputStream.close();
	}

	@Override
	public Iterator<String[]> iterator() {
		final CSVFileReaderIter iter = new CSVFileReaderIter(this);
		return iter;
	}

	public IOStats getStats() {
		return stats;
	}

	public int[] getHeaderMapping() {
		return headerMapping;
	}

	public void setHeaderMapping(final int[] mapping) {
		this.headerMapping = mapping;
		this.stats.headerAvailable = mapping != null;
	}

	public void setHeaderMapping(final String[] fields) {
		final int[] mapping;
		if (header != null)
			mapping = CSVParser.createHeaderMapping(header,fields);
		else
			mapping = null;
		setHeaderMapping(mapping);
	}

}
