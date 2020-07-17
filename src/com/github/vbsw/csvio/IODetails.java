/**
 *   Copyright 2020, Vitali Baumtrok <vbsw@mailbox.org>.
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file BSL-1.0.txt or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */

package com.github.vbsw.csvio;


import java.io.IOException;

/**
 * @author Vitali Baumtrok
 */
public class IODetails {

	public IOException ioException;
	public boolean readSuccessful;
	public boolean headerAvailable;
	public int totalLinesCount;
	public int contentLinesCount;
	public int errorLinesCount;
	public int emptyLinesCount;
	public int bytesRead;

	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder(256);
		stringBuilder.append("io exception         ");
		stringBuilder.append(toString(ioException != null));
		stringBuilder.append('\n');
		stringBuilder.append("read successful      ");
		stringBuilder.append(toString(readSuccessful));
		stringBuilder.append('\n');
		stringBuilder.append("header available     ");
		stringBuilder.append(toString(headerAvailable));
		stringBuilder.append('\n');
		stringBuilder.append("total lines count    ");
		stringBuilder.append(totalLinesCount);
		stringBuilder.append('\n');
		stringBuilder.append("content lines count  ");
		stringBuilder.append(contentLinesCount);
		stringBuilder.append('\n');
		stringBuilder.append("error lines count    ");
		stringBuilder.append(errorLinesCount);
		stringBuilder.append('\n');
		stringBuilder.append("empty lines count    ");
		stringBuilder.append(emptyLinesCount);
		stringBuilder.append('\n');
		stringBuilder.append("bytes read           ");
		stringBuilder.append(bytesRead);
		return stringBuilder.toString();
	}

	private String toString(final boolean b) {
		if (b)
			return "yes";
		return "no";
	}

}
