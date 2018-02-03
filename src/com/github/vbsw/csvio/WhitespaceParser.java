/**
 *   Copyright 2018, Vitali Baumtrok <vbsw@mailbox.org>.
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file BSL-1.0.txt or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.csvio;


/**
 * @author Vitali Baumtrok
 */
public class WhitespaceParser {

	public int seekAfterLF ( final byte[] bytes, final int fromLeft, final int toRight ) {
		for ( int i = fromLeft; i < toRight; i += 1 ) {
			if ( bytes[i] == '\n' ) {
				return i + 1;
			}
		}
		return toRight;
	}

	public int seekContent ( final byte[] bytes, final int fromLeft, final int toRight ) {
		if (fromLeft < toRight) {
			for ( int i = fromLeft; i < toRight; i += 1 ) {
				if ( !isWhitespace(bytes[i]) ) {
					return i;
				}
			}
			return toRight;
		}
		return fromLeft;
	}

	public boolean endsWithLF ( final byte[] bytes, final int fromRight, final int toLeft ) {
		return fromRight > toLeft && bytes[fromRight - 1] == '\n';
	}

	public boolean isWhitespace ( final byte[] bytes, final int fromLeft, final int toRight ) {
		for ( int i = fromLeft; i < toRight; i += 1 ) {
			if ( !isWhitespace(bytes[i]) ) {
				return false;
			}
		}
		return true;
	}

	private boolean isWhitespace ( final byte character ) {
		return character >= 0 && character <= 32;
	}

}
