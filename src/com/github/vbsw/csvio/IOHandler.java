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
public interface IOHandler {

	public void prepairInput();

	public void processInput(String[] values, int[] headerMapping, IOStats stats);

	public void prepairOutput();

	public void processOutput();

	public String[] getFieldNames();

}
