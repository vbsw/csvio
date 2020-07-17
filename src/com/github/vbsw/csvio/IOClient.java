/**
 *   Copyright 2020, Vitali Baumtrok <vbsw@mailbox.org>.
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file BSL-1.0.txt or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */

package com.github.vbsw.csvio;


/**
 * @author Vitali Baumtrok
 */
public interface IOClient {

	public void prepairRead();

	public void processRead(String[] values, int[] headerMapping, IODetails details);

	public void prepairWrite();

	public void processWrite();

	public String[] getFieldNames();

}
