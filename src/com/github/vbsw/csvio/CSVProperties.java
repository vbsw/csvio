/**
 *   Copyright 2020, Vitali Baumtrok <vbsw@mailbox.org>.
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file BSL-1.0.txt or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */

package com.github.vbsw.csvio;


import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * @author Vitali Baumtrok
 */
public class CSVProperties {

	protected Path filePath;
	protected String delimiter;
	protected Charset charset;

	public Path getFilePath() {
		return filePath;
	}

	public void setFilePath(final Path filePath) {
		this.filePath = filePath;
	}

	public Path getFileDirectory() {
		if (filePath != null) {
			return filePath.getParent();
		}
		return null;
	}

	public void setFileDirectory(final Path fileDir) {
		if (filePath != null) {
			final Path fileName = filePath.getFileName();
			if (fileName != null)
				filePath = fileDir.resolve(fileName);
			else
				filePath = fileDir;
		} else {
			filePath = fileDir;
		}
	}

	public Path getFileName() {
		if (filePath != null) {
			return filePath.getFileName();
		}
		return null;
	}

	public void setFileName(final Path fileName) {
		if (filePath != null) {
			final Path parent = filePath.getParent();
			if (parent != null)
				parent.resolve(fileName);
			else
				filePath = fileName;
		} else {
			filePath = fileName;
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

}
