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
public abstract class CSVCharProcessor {

	public abstract void startProcessing ( final CSVParser csvParser );

	public abstract void processCSV ( final char[] chars, final int fromLeft, final int toRight, final int lineNumber, final int bytesReadTotal );

	public abstract void endProcessing ( final int bytesReadTotal );

	public abstract void setException ( final IOException e );

}
