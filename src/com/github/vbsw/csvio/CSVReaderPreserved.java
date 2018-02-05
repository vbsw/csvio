/**
 *   Copyright 2018, Vitali Baumtrok <vbsw@mailbox.org>.
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file BSL-1.0.txt or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.csvio;


import java.nio.ByteBuffer;


/**
 * @author Vitali Baumtrok
 */
public class CSVReaderPreserved extends CSVReader {

	protected CSVParser parser;
	protected ByteBuffer byteBuffer;
	protected char[] charBuffer;

	@Override
	protected CSVParser getParser ( ) {
		if ( parser == null ) {
			parser = super.getParser();
		}
		return parser;
	}

	@Override
	protected ByteBuffer getByteBuffer ( ) {
		if ( byteBuffer == null ) {
			byteBuffer = super.getByteBuffer();
		}
		return byteBuffer;
	}

	@Override
	protected char[] getCharBuffer ( ) {
		if ( charBuffer == null ) {
			charBuffer = super.getCharBuffer();
		}
		return charBuffer;
	}

}
