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
public abstract class CSVToDataConverter {

	public abstract void prepareConversion ( final WhitespaceParser whitespaceParser );

	public abstract void convertCSV ( final byte[] bytes, final int from, final int to, final int lineNumber );

	public abstract void setException ( final IOException e );

}
