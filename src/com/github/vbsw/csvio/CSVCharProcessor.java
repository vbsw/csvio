/**
 *   Copyright 2018, Vitali Baumtrok <vbsw@mailbox.org>.
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file BSL-1.0.txt or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.csvio;


import java.io.IOException;


/**
 * Template for processing lines as character array.
 * 
 * @author Vitali Baumtrok
 */
public abstract class CSVCharProcessor {

	/**
	 * It is called before any other method.
	 * @param parser Default Parser or some custom parser to help processing.
	 */
	public abstract void startProcessing ( final CSVParser parser );

	/**
	 * It is called, when non empty line is detected.
	 * @param chars Characters of the line.
	 * @param fromLeft Start index (inclusive).
	 * @param toRight End index (exclusive).
	 * @param lineNumber Number of the line to process.
	 * @param bytesReadTotal Number of bytes read until now (including this line).
	 */
	public abstract void processLine ( final char[] chars, final int fromLeft, final int toRight, final int lineNumber, final int bytesReadTotal );

	/**
	 * It is called at very end (and after setException).
	 * @param bytesReadTotal Number of bytes processed. Could be less then excepted if setException has been called. 
	 */
	public abstract void endProcessing ( final int bytesReadTotal );

	/**
	 * It is called, when some exception occurs. Processing stops then.
	 * @param e Exception.
	 */
	public abstract void setException ( final IOException e );

}
