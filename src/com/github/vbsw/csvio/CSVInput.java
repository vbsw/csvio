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

/**
 * @author Vitali Baumtrok
 */
public final class CSVInput {

	private final InputStream inputStream;
	private final IODetails details;

	private int[] headerMapping;
	private byte[] bytes;
	private int offset;
	private int lineBegin;
	private int lineEnd;
	private int bytesRead;

	public CSVInput (final InputStream inputStream, final IODetails details) {
		this(inputStream,details,1024 * 8);
	}

	public CSVInput (final InputStream inputStream, final IODetails details, final int capacity) {
		this.inputStream = inputStream;
		this.details = details;
		this.headerMapping = null;
		this.bytes = new byte[capacity];
		this.offset = 0;
		this.lineBegin = 0;
		this.lineEnd = 0;
		this.bytesRead = 0;
	}

	public int[] getHeaderMapping() {
		return headerMapping;
	}

	public String[] getLineValues(final byte[] delimiter, final Charset charset) {
		return CSVParser.split(bytes,lineBegin,lineEnd,delimiter,charset);
	}

	public void readFromStream() throws IOException {
		bytesRead = inputStream.read(bytes,offset,bytes.length - offset);
		if (bytesRead > 0)
			bytesRead += offset;
		else
			bytesRead = offset;
		details.bytesRead += bytesRead - offset;
	}

	public void seekFirstLineContent() throws IOException {
		if (bytesRead > 0) {
			details.totalLinesCount++;
			seekLineEnd();
			if (CSVParser.isWhitespace(bytes,lineBegin,lineEnd)) {
				details.emptyLinesCount++;
				seekLineContent();
			}
		}
	}

	public void seekLineContent() throws IOException {
		boolean newLine = seekLineBegin();
		while (newLine) {
			newLine = false;
			details.totalLinesCount++;
			seekLineEnd();
			if (CSVParser.isWhitespace(bytes,lineBegin,lineEnd)) {
				details.emptyLinesCount++;
				if (isMoreToRead())
					newLine = seekLineBegin();
			}
		}
	}

	public boolean isMoreToRead() {
		return lineBegin < bytesRead;
	}

	private boolean seekLineBegin() throws IOException {
		ensureNextTwoBytes();
		if (lineEnd < bytesRead) {
			final byte b = bytes[lineEnd];
			if (b == '\n')
				lineBegin = lineEnd + 1;
			else if (b == '\r')
				if (lineEnd + 1 < bytesRead && bytes[lineEnd + 1] == '\n')
					lineBegin = lineEnd + 2;
				else
					lineBegin = lineEnd + 1;
			else
				lineBegin = lineEnd;
		} else {
			lineBegin = lineEnd;
		}
		return lineBegin > lineEnd;
	}

	private void seekLineEnd() throws IOException {
		lineEnd = CSVParser.seekLineEnd(bytes,lineBegin,bytesRead);
		// there is more to read
		while (lineEnd == bytes.length) {
			final int lineLength = expandBuffer();
			readFromStream();
			if (lineLength < bytesRead)
				lineEnd = CSVParser.seekLineEnd(bytes,lineLength,bytesRead);
			else
				lineEnd = lineLength;
			lineBegin = 0;
		}
	}

	private void ensureNextTwoBytes() throws IOException {
		if (lineEnd + 2 > bytesRead && bytesRead == bytes.length) {
			if (lineEnd + 1 == bytesRead) {
				bytes[0] = bytes[lineEnd + 1];
				offset = 1;
			} else {
				offset = 0;
			}
			readFromStream();
			lineBegin = 0;
			lineEnd = 0;
		}
	}

	private int expandBuffer() {
		final int copyLength = lineEnd - lineBegin;
		if (copyLength < bytes.length) {
			if (copyLength > 0 && lineBegin > 0)
				System.arraycopy(bytes,lineBegin,bytes,0,copyLength);
		} else {
			final byte[] newBytes = new byte[bytes.length * 2];
			if (copyLength > 0)
				System.arraycopy(bytes,lineBegin,newBytes,0,copyLength);
			bytes = newBytes;
		}
		return copyLength;
	}

}
