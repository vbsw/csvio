/**
 *   Copyright 2020, Vitali Baumtrok <vbsw@mailbox.org>.
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file BSL-1.0.txt or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */

package com.github.vbsw.csvio;


import java.nio.charset.Charset;

/**
 * @author Vitali Baumtrok
 */
public class CSVParser {

	public static boolean isWhiteSpace(final byte b) {
		return b >= 0 && b <= 32;
	}

	public static boolean isWhitespace(final byte[] bytes, final int from, final int to) {
		for (int i = from; i < to; i++) {
			if (!isWhiteSpace(bytes[i]))
				return false;
		}
		return true;
	}

	public static boolean equals(byte[] bytesA, byte[] bytesB, int offset) {
		boolean equals = false;
		if (bytesB.length <= bytesA.length - offset) {
			equals = true;
			for (int i = 0; i < bytesB.length && equals; i++)
				equals = bytesA[offset + i] == bytesB[i];
		}
		return equals;
	}

	public static int count(final byte[] bytes, final int from, final int to, final byte[] key) {
		int count = 0;
		for (int i = from; i < to - key.length + 1; i++) {
			if (equals(bytes,key,i)) {
				count++;
				i += key.length - 1;
			}
		}
		return count;
	}

	public static int seek(final byte[] bytes, final byte[] key, final int from, final int to) {
		for (int i = from; i < to - key.length + 1; i++)
			if (equals(bytes,key,i))
				return i;
		return to;
	}

	public static int seekLineEnd(final byte[] bytes, final int from, final int to) {
		for (int lineEnd = from; lineEnd < to; lineEnd++) {
			final byte b = bytes[lineEnd];
			if (b == '\n' || b == '\r')
				return lineEnd;
		}
		return to;
	}

	public static int seekContent(final byte[] bytes, final int from, final int to) {
		for (int i = from; i < to; i++)
			if (!isWhiteSpace(bytes[i]))
				return i;
		return to;
	}

	public static int seekWhitespaceRight(final byte[] bytes, final int from, final int to) {
		for (int i = to - 1; i >= from; i--)
			if (!isWhiteSpace(bytes[i]))
				return i + 1;
		return 0;
	}

	public static String[] split(final byte[] bytes, final int from, final int to, final byte[] delimiter, final Charset charset) {
		final int valuesCount = count(bytes,from,to,delimiter) + 1;
		final String[] values = new String[valuesCount];
		int beginIndex = from;
		int endIndex = seek(bytes,delimiter,beginIndex,to);
		for (int i = 0; i < values.length; i++) {
			values[i] = createString(bytes,beginIndex,endIndex,charset);
			beginIndex = endIndex + delimiter.length;
			endIndex = seek(bytes,delimiter,beginIndex,to);
		}
		return values;
	}

	public static int[] createHeaderMapping(final String[] fieldNames, final String[] availableFieldNames) {
		final int[] mapping = new int[fieldNames.length];
		for (int i = 0; i < fieldNames.length; i++) {
			mapping[i] = -1;
			for (int j = 0; j < availableFieldNames.length; j++) {
				if (fieldNames[i].equals(availableFieldNames[j])) {
					mapping[i] = j;
					break;
				}
			}
		}
		for (int i: mapping)
			if (i >= 0)
				return mapping;
		return null;
	}

	private static String createString(final byte[] bytes, final int from, final int to, Charset charset) {
		final int beginIndex = seekContent(bytes,from,to);
		final int endIndex = seekWhitespaceRight(bytes,beginIndex,to);
		final int length = endIndex - beginIndex;
		if (length > 0)
			return new String(bytes,beginIndex,length,charset);
		return "";
	}

}
