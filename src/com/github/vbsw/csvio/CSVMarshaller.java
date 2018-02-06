/**
 *   Copyright 2018, Vitali Baumtrok <vbsw@mailbox.org>.
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file BSL-1.0.txt or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.csvio;


import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;


/**
 * Template for marshalling lines to a writer.
 * 
 * @author Vitali Baumtrok
 */
public abstract class CSVMarshaller {

	/**
	 * It is called before any other method.
	 * @param parser Default Parser or some custom parser to help processing.
	 * @param charset Charset used to convert strings to bytes.
	 */
	public abstract void startMarshalling ( CSVParser parser, Charset charset );

	/**
	 * Returns true, if marshaller has lines to write.
	 * @return true, if marshaller has lines to write.
	 */
	public abstract boolean hasLine ( );

	/**
	 * Puts a line to writer. Line feed (LF) will be added automatically and doesn't need to be added here.
	 * @param abstractWriter Writer to write to.
	 * @throws IOException If an I/O error occurs.
	 */
	public abstract void marshallLine ( Writer abstractWriter ) throws IOException;

	/**
	 * It is called at the very end (even after setException).
	 */
	public abstract void endMarshalling ( );

	/**
	 * It is called, when some exception occurs. Processing stops then.
	 * @param e Exception.
	 */
	public abstract void setException ( IOException e );

}
