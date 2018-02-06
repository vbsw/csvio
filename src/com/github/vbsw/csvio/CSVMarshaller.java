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
 * @author Vitali Baumtrok
 */
public abstract class CSVMarshaller {

	public abstract void startMarshalling ( final CSVParser parser, final Charset charset );

	public abstract boolean hasLine ( );

	public abstract void marshallLine ( final Writer writer );

	public abstract void endMarshalling ( );

	public abstract void setException ( final IOException e );

}
