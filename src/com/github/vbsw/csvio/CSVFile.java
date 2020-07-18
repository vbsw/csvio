/**
 *   Copyright 2020, Vitali Baumtrok <vbsw@mailbox.org>.
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file BSL-1.0.txt or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */

package com.github.vbsw.csvio;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Vitali Baumtrok
 */
public class CSVFile {

	protected Path path;
	protected String delimiter;
	protected Charset charset;
	protected String[] header;

	public CSVFile () {
		this(null,",",StandardCharsets.UTF_8,null);
	}

	public CSVFile (final Path path) {
		this(path,",",StandardCharsets.UTF_8,null);
	}

	public CSVFile (final Path path, final String delimiter) {
		this(path,delimiter,StandardCharsets.UTF_8,null);
	}

	public CSVFile (final String delimiter, final Charset charset) {
		this(null,delimiter,charset,null);
	}

	public CSVFile (final String delimiter, final Charset charset, final String[] header) {
		this(null,delimiter,charset,header);
	}

	public CSVFile (final Path path, final String delimiter, final Charset charset, final String[] header) {
		this.path = path;
		this.delimiter = delimiter;
		this.charset = charset;
		this.header = header;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(final Path path) {
		this.path = path;
	}

	public Path getDirectory() {
		if (path != null) {
			return path.getParent();
		}
		return null;
	}

	public void setDirectory(final String dir) {
		final Path dirPath = Paths.get(dir);
		setDirectory(dirPath);
	}

	public void setDirectory(final Path dir) {
		if (path != null) {
			final Path fileName = path.getFileName();
			if (fileName != null)
				path = dir.resolve(fileName);
			else
				path = dir;
		} else {
			path = dir;
		}
	}

	public Path getName() {
		if (path != null) {
			return path.getFileName();
		}
		return null;
	}

	public void setName(final String name) {
		final Path namePath = Paths.get(name);
		setName(namePath);
	}

	public void setName(final Path name) {
		if (path != null) {
			final Path parent = path.getParent();
			if (parent != null)
				parent.resolve(name);
			else
				path = name;
		} else {
			path = name;
		}
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(final String delimiter) {
		this.delimiter = delimiter;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(final Charset charset) {
		this.charset = charset;
	}

	public CSVFileReader getReader() throws IOException {
		return getReader(path);
	}

	public CSVFileReader getReader(final Path path) throws IOException {
		final byte[] delimiter = this.delimiter.getBytes(charset);
		final CSVFileReader reader = new CSVFileReader(path,delimiter,charset,header);
		return reader;
	}

}
