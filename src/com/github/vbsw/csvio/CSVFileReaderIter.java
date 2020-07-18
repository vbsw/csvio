/**
 *   Copyright 2020, Vitali Baumtrok <vbsw@mailbox.org>.
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file BSL-1.0.txt or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */

package com.github.vbsw.csvio;


import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;

/**
 * @author Vitali Baumtrok
 */
public class CSVFileReaderIter implements Iterator<String[]> {

	protected final CSVFileReader reader;

	protected byte[] buffer;
	protected int lineBegin;
	protected int lineEnd;
	protected int bytesRead;
	protected String[] fields;

	public CSVFileReaderIter (final CSVFileReader reader) throws UncheckedIOException {
		this(reader,new byte[1024 * 8]);
	}

	public CSVFileReaderIter (final CSVFileReader reader, final byte[] buffer) throws UncheckedIOException {
		this.reader = reader;
		this.buffer = buffer;
		this.reader.getStats().reset();
		try {
			readFirstFields();
		} catch (final IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public boolean hasNext() throws UncheckedIOException {
		if (reader.stats.contentLinesCount > 0 || reader.stats.headerAvailable) {
			try {
				readNextFields();
			} catch (final IOException e) {
				throw new UncheckedIOException(e);
			}
		} else {
			reader.stats.contentLinesCount++;
		}
		reader.stats.ioSuccessful = (lineBegin >= bytesRead);
		return lineBegin < bytesRead;
	}

	@Override
	public String[] next() {
		return fields;
	}

	protected void readFirstFields() throws IOException {
		readFromStream(0);
		seekFirstFields();
		// fields are available
		if (lineBegin < bytesRead) {
			fields = CSVParser.split(buffer,lineBegin,lineEnd,reader.delimiter,reader.charset);
			reader.setHeaderMapping(fields);
		}
	}

	protected void readNextFields() throws IOException {
		seekNextFields();
		// fields are available
		if (lineBegin < bytesRead) {
			fields = CSVParser.split(buffer,lineBegin,lineEnd,reader.delimiter,reader.charset);
			reader.stats.contentLinesCount++;
		}
	}

	protected void seekFirstFields() throws IOException {
		if (bytesRead > 0) {
			reader.stats.totalLinesCount++;
			seekLineEnd();
			if (CSVParser.isWhitespace(buffer,lineBegin,lineEnd)) {
				reader.stats.emptyLinesCount++;
				seekNextFields();
			}
		}
	}

	protected void seekNextFields() throws IOException {
		boolean newLine = seekLineBegin();
		while (newLine) {
			newLine = false;
			reader.stats.totalLinesCount++;
			seekLineEnd();
			if (CSVParser.isWhitespace(buffer,lineBegin,lineEnd)) {
				reader.stats.emptyLinesCount++;
				// there is more to read
				if (lineBegin < bytesRead)
					newLine = seekLineBegin();
			}
		}
	}

	protected void seekLineEnd() throws IOException {
		lineEnd = CSVParser.seekLineEnd(buffer,lineBegin,bytesRead);
		// there is more to read
		while (lineEnd == buffer.length) {
			final int lineLength = expandBuffer();
			readFromStream(lineLength);
			if (lineLength < bytesRead)
				lineEnd = CSVParser.seekLineEnd(buffer,lineLength,bytesRead);
			else
				lineEnd = lineLength;
			lineBegin = 0;
		}
	}

	protected boolean seekLineBegin() throws IOException {
		// ensure at least two bytes are available
		if (lineEnd + 2 > bytesRead && bytesRead == buffer.length) {
			final int offset;
			if (lineEnd + 1 == bytesRead) {
				// preserve one byte
				buffer[0] = buffer[lineEnd];
				offset = 1;
			} else {
				offset = 0;
			}
			// because buffer is full
			readFromStream(offset);
			lineBegin = 0;
			lineEnd = 0;
		}
		// skip CR and LF
		if (lineEnd < bytesRead) {
			final byte b = buffer[lineEnd];
			if (b == '\n')
				lineBegin = lineEnd + 1;
			else if (b == '\r')
				if (lineEnd + 1 < bytesRead && buffer[lineEnd + 1] == '\n')
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

	protected void readFromStream(final int offset) throws IOException {
		bytesRead = reader.inputStream.read(buffer,offset,buffer.length - offset);
		if (bytesRead > 0)
			bytesRead += offset;
		else
			bytesRead = offset;
		reader.stats.bytesCount += bytesRead - offset;
	}

	protected int expandBuffer() {
		final int copyLength = lineEnd - lineBegin;
		// enough buffer left
		if (copyLength * 2 < buffer.length) {
			if (copyLength > 0 && lineBegin > 0)
				System.arraycopy(buffer,lineBegin,buffer,0,copyLength);
		} else {
			final byte[] newBytes = new byte[buffer.length * 2];
			if (copyLength > 0)
				System.arraycopy(buffer,lineBegin,newBytes,0,copyLength);
			buffer = newBytes;
		}
		return copyLength;
	}

}
