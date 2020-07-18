/**
 *   Copyright 2020, Vitali Baumtrok <vbsw@mailbox.org>.
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file BSL-1.0.txt or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */

package com.github.vbsw.csvio;


/**
 * @author Vitali Baumtrok
 */
public class IOStats {

	public boolean ioSuccessful;
	public boolean headerAvailable;
	public int totalLinesCount;
	public int contentLinesCount;
	public int errorLinesCount;
	public int emptyLinesCount;
	public int bytesCount;

	public void reset() {
		ioSuccessful = false;
		headerAvailable = false;
		totalLinesCount = 0;
		contentLinesCount = 0;
		errorLinesCount = 0;
		emptyLinesCount = 0;
		bytesCount = 0;
	}

	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder(256);
		stringBuilder.append("io successful        ");
		stringBuilder.append(toString(ioSuccessful));
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
		stringBuilder.append("bytes count          ");
		stringBuilder.append(bytesCount);
		return stringBuilder.toString();
	}

	private String toString(final boolean b) {
		if (b)
			return "yes";
		return "no";
	}

}
