/**
 *   Copyright 2018, Vitali Baumtrok <vbsw@mailbox.org>.
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file BSL-1.0.txt or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.csvio;


import java.io.IOException;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;


/**
 * @author Vitali Baumtrok
 */
public class CSVReader {

	protected static final int INITIAL_BYTE_BUFFER_CAPACITY = 1024 * 8 - 64 * 8;
	protected static final int INITIAL_CHAR_BUFFER_CAPACITY = 1024 * 8 - 64 * 8;

	public void readFile ( final Path filePath, final CSVByteProcessor processor ) {
		final CSVParser parser = getParser();
		int bytesReadTotal = 0;

		processor.startProcessing(parser);

		try ( final SeekableByteChannel channel = Files.newByteChannel(filePath,StandardOpenOption.READ) ) {
			ByteBuffer buffer = getByteBuffer();
			int bytesRead = 0;
			int bytesLength = 0;
			int lineBegin = 0;
			int lineEnd = 0;
			int lineNumber = 0;

			bytesRead = channel.read(buffer);
			bytesLength = bytesRead;

			while ( bytesRead > 0 ) {
				lineNumber += 1;
				lineEnd = parser.seekAfterLF(buffer.array(),lineEnd,bytesLength);

				while ( !parser.endsWithLF(buffer.array(),lineEnd,lineBegin) && bytesRead > 0 ) {
					buffer = preserveBufferAndEnsureCapacity(buffer,lineBegin,lineEnd);
					bytesLength = lineEnd - lineBegin;
					lineEnd = bytesLength;
					lineBegin = 0;
					bytesRead = channel.read(buffer);
					if ( bytesRead > 0 ) {
						bytesLength += bytesRead;
					}
					lineEnd = parser.seekAfterLF(buffer.array(),lineEnd,bytesLength);
				}
				bytesReadTotal += (lineEnd - lineBegin);
				if ( !parser.isWhitespace(buffer.array(),lineBegin,lineEnd) ) {
					processor.processCSV(buffer.array(),lineBegin,lineEnd,lineNumber,bytesReadTotal);
				}
				lineBegin = lineEnd;
			}
			processor.endProcessing(bytesReadTotal);

		} catch ( final IOException e ) {
			processor.setException(e);
			processor.endProcessing(bytesReadTotal);
		}
	}

	public void readAbstract ( final Reader reader, final CSVCharProcessor processor ) {
		final CSVParser parser = getParser();
		int bytesReadTotal = 0;

		processor.startProcessing(parser);

		try {
			char[] buffer = getCharBuffer();
			int charsRead = 0;
			int charsLength = 0;
			int lineBegin = 0;
			int lineEnd = 0;
			int lineNumber = 0;

			charsRead = reader.read(buffer);
			charsLength = charsRead;

			while ( charsRead > 0 ) {
				lineNumber += 1;
				lineEnd = parser.seekAfterLF(buffer,lineEnd,charsLength);

				while ( !parser.endsWithLF(buffer,lineEnd,lineBegin) && charsRead > 0 ) {
					buffer = preserveBufferAndEnsureCapacity(buffer,lineBegin,lineEnd);
					charsLength = lineEnd - lineBegin;
					lineEnd = charsLength;
					lineBegin = 0;
					charsRead = reader.read(buffer,lineEnd,buffer.length - lineEnd);
					if ( charsRead > 0 ) {
						charsLength += charsRead;
					}
					lineEnd = parser.seekAfterLF(buffer,lineEnd,charsLength);
				}
				bytesReadTotal += (lineEnd - lineBegin);
				if ( !parser.isWhitespace(buffer,lineBegin,lineEnd) ) {
					processor.processCSV(buffer,lineBegin,lineEnd,lineNumber,bytesReadTotal);
				}
				lineBegin = lineEnd;
			}
			processor.endProcessing(bytesReadTotal);

		} catch ( final IOException e ) {
			processor.setException(e);
			processor.endProcessing(bytesReadTotal);
		}
	}

	protected CSVParser getParser ( ) {
		return new CSVParser();
	}

	protected ByteBuffer getByteBuffer ( ) {
		return ByteBuffer.allocate(INITIAL_BYTE_BUFFER_CAPACITY);
	}

	protected ByteBuffer getByteBufferExtended ( final byte[] bytes ) {
		final ByteBuffer bufferNew = ByteBuffer.allocate(bytes.length * 2);
		final byte[] bufferBytesNew = bufferNew.array();
		System.arraycopy(bytes,0,bufferBytesNew,0,bytes.length);
		return bufferNew;
	}

	protected char[] getCharBuffer ( ) {
		return new char[INITIAL_CHAR_BUFFER_CAPACITY];
	}

	protected char[] getCharBufferExtended ( final char[] chars, final int offset, final int copyLength ) {
		final char[] charsNew = new char[chars.length * 2];
		System.arraycopy(chars,offset,charsNew,0,copyLength);
		return charsNew;
	}

	private ByteBuffer preserveBufferAndEnsureCapacity ( final ByteBuffer buffer, final int lineBegin, final int lineEnd ) {
		if ( lineBegin < lineEnd ) {
			final byte[] bytes = buffer.array();
			if ( lineBegin > 0 ) {
				final int bytesLength = lineEnd - lineBegin;
				System.arraycopy(bytes,lineBegin,bytes,0,bytesLength);
				buffer.position(bytesLength);
			} else if ( lineEnd == bytes.length ) {
				final ByteBuffer bufferNew = getByteBufferExtended(bytes);
				bufferNew.position(lineEnd);
				return bufferNew;
			}
		}
		return buffer;
	}

	private char[] preserveBufferAndEnsureCapacity ( final char[] chars, final int fromLeft, final int toRight ) {
		if ( fromLeft < toRight ) {
			if ( fromLeft > 0 ) {
				System.arraycopy(chars,fromLeft,chars,0,toRight - fromLeft);
			} else if ( toRight == chars.length ) {
				return getCharBufferExtended(chars,0,chars.length);
			}
		}
		return chars;
	}

}
