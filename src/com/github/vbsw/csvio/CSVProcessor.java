/**
 *   Copyright 2018, Vitali Baumtrok <vbsw@mailbox.org>.
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file BSL-1.0.txt or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.csvio;


import java.io.IOException;


/**
 * @author Vitali Baumtrok
 */
public abstract class CSVProcessor {

	public void startProcessing ( final CSVParser csvParser ) {
	}

	public void processCSV ( final byte[] bytes, final int fromLeft, final int toRight, final int lineNumber, final int bytesReadTotal ) {
	}

	public void endProcessing ( final int bytesReadTotal ) {
	}

	public void setException ( final IOException e ) {
	}

	public void processCSV ( final char[] buffer, final int fromLeft, final int toRight, final int lineNumber, final int bytesReadTotal ) {
	}

}
