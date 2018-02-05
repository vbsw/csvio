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
public class CSVParser {

	public int seekAfterLF ( final byte[] bytes, final int fromLeft, final int toRight ) {
		for ( int i = fromLeft; i < toRight; i += 1 ) {
			if ( bytes[i] == '\n' ) {
				return i + 1;
			}
		}
		return toRight;
	}

	public int seekAfterLF ( final char[] chars, final int fromLeft, final int toRight ) {
		for ( int i = fromLeft; i < toRight; i += 1 ) {
			if ( chars[i] == '\n' ) {
				return i + 1;
			}
		}
		return toRight;
	}

	public int seekContent ( final byte[] bytes, final int fromLeft, final int toRight ) {
		if ( fromLeft < toRight ) {
			for ( int i = fromLeft; i < toRight; i += 1 ) {
				if ( !isWhitespace(bytes[i]) ) {
					return i;
				}
			}
			return toRight;
		}
		return fromLeft;
	}

	public int seekContent ( final char[] chars, final int fromLeft, final int toRight ) {
		if ( fromLeft < toRight ) {
			for ( int i = fromLeft; i < toRight; i += 1 ) {
				if ( !isWhitespace(chars[i]) ) {
					return i;
				}
			}
			return toRight;
		}
		return fromLeft;
	}

	public int seekContentReverse ( final byte[] bytes, final int fromRight, final int toLeft ) {
		if ( fromRight > toLeft ) {
			for ( int i = fromRight - 1; i >= toLeft; i -= 1 ) {
				if ( !isWhitespace(bytes[i]) ) {
					return i + 1;
				}
			}
			return toLeft;
		}
		return fromRight;
	}

	public int seekContentReverse ( final char[] chars, final int fromRight, final int toLeft ) {
		if ( fromRight > toLeft ) {
			for ( int i = fromRight - 1; i >= toLeft; i -= 1 ) {
				if ( !isWhitespace(chars[i]) ) {
					return i + 1;
				}
			}
			return toLeft;
		}
		return fromRight;
	}

	public boolean endsWithLF ( final byte[] bytes, final int fromRight, final int toLeft ) {
		return fromRight > toLeft && bytes[fromRight - 1] == '\n';
	}

	public boolean endsWithLF ( final char[] chars, final int fromRight, final int toLeft ) {
		return fromRight > toLeft && chars[fromRight - 1] == '\n';
	}

	public boolean isWhitespace ( final byte[] bytes, final int fromLeft, final int toRight ) {
		for ( int i = fromLeft; i < toRight; i += 1 ) {
			if ( !isWhitespace(bytes[i]) ) {
				return false;
			}
		}
		return true;
	}

	public boolean isWhitespace ( final char[] chars, final int fromLeft, final int toRight ) {
		for ( int i = fromLeft; i < toRight; i += 1 ) {
			if ( !isWhitespace(chars[i]) ) {
				return false;
			}
		}
		return true;
	}

	public byte[][] splitValues ( final byte[] bytes, final int fromLeft, final int toRight ) {
		return splitValues(bytes,fromLeft,toRight,(byte) ',');
	}

	public byte[][] splitValues ( final byte[] bytes, final int fromLeft, final int toRight, final byte separator ) {
		final int valuesCount = getValuesCount(bytes,fromLeft,toRight,separator);
		final byte[][] values = new byte[valuesCount][];
		int offset = fromLeft;
		for ( int i = 0; i < values.length; i += 1 ) {
			final int offsetNew = seekPosition(bytes,offset,toRight,separator);
			final int left = seekContent(bytes,offset,offsetNew);
			final int right = seekContentReverse(bytes,offsetNew,left);
			values[i] = new byte[right - left];
			offset = offsetNew + 1;
			if ( values[i].length > 0 ) {
				System.arraycopy(bytes,left,values[i],0,values[i].length);
			}
		}
		return values;
	}

	public byte[][] splitValues ( final byte[] bytes, final int fromLeft, final int toRight, final byte[] separator ) {
		final int valuesCount = getValuesCount(bytes,fromLeft,toRight,separator);
		final byte[][] values = new byte[valuesCount][];
		int offset = fromLeft;
		for ( int i = 0; i < values.length; i += 1 ) {
			final int offsetNew = seekPosition(bytes,offset,toRight,separator);
			final int left = seekContent(bytes,offset,offsetNew);
			final int right = seekContentReverse(bytes,offsetNew,left);
			values[i] = new byte[right - left];
			offset = offsetNew + separator.length;
			if ( values[i].length > 0 ) {
				System.arraycopy(bytes,left,values[i],0,values[i].length);
			}
		}
		return values;
	}

	public char[][] splitValues ( final char[] chars, final int fromLeft, final int toRight ) {
		return splitValues(chars,fromLeft,toRight,',');
	}

	public char[][] splitValues ( final char[] chars, final int fromLeft, final int toRight, final char separator ) {
		final int valuesCount = getValuesCount(chars,fromLeft,toRight,separator);
		final char[][] values = new char[valuesCount][];
		int offset = fromLeft;
		for ( int i = 0; i < values.length; i += 1 ) {
			final int offsetNew = seekPosition(chars,offset,toRight,separator);
			final int left = seekContent(chars,offset,offsetNew);
			final int right = seekContentReverse(chars,offsetNew,left);
			values[i] = new char[right - left];
			offset = offsetNew + 1;
			if ( values[i].length > 0 ) {
				System.arraycopy(chars,left,values[i],0,values[i].length);
			}
		}
		return values;
	}

	public char[][] splitValues ( final char[] chars, final int fromLeft, final int toRight, final char[] separator ) {
		final int valuesCount = getValuesCount(chars,fromLeft,toRight,separator);
		final char[][] values = new char[valuesCount][];
		int offset = fromLeft;
		for ( int i = 0; i < values.length; i += 1 ) {
			final int offsetNew = seekPosition(chars,offset,toRight,separator);
			final int left = seekContent(chars,offset,offsetNew);
			final int right = seekContentReverse(chars,offsetNew,left);
			values[i] = new char[right - left];
			offset = offsetNew + separator.length;
			if ( values[i].length > 0 ) {
				System.arraycopy(chars,left,values[i],0,values[i].length);
			}
		}
		return values;
	}

	public int getValuesCount ( final byte[] bytes, final int fromLeft, final int toRight ) {
		return getValuesCount(bytes,fromLeft,toRight,(byte) ',');
	}

	public int getValuesCount ( final byte[] bytes, final int fromLeft, final int toRight, final byte character ) {
		final int left = seekContent(bytes,fromLeft,toRight);
		final int right = seekContentReverse(bytes,toRight,left);
		int count = 0;
		if ( left < right ) {
			count = 1;
			for ( int i = fromLeft; i < toRight; i += 1 ) {
				if ( bytes[i] == character ) {
					count += 1;
				}
			}
		}
		return count;
	}

	public int getValuesCount ( final byte[] bytes, final int fromLeft, final int toRight, final byte[] characters ) {
		final int left = seekContent(bytes,fromLeft,toRight);
		final int right = seekContentReverse(bytes,toRight,left);
		int count = 0;
		if ( left < right ) {
			count = 1;
			for ( int i = fromLeft; i < toRight; i += 1 ) {
				if ( isMatch(bytes,i,characters,0,characters.length) ) {
					i += characters.length - 1;
					count += 1;
				}
			}
		}
		return count;
	}

	public int getValuesCount ( final char[] bytes, final int fromLeft, final int toRight ) {
		return getValuesCount(bytes,fromLeft,toRight,',');
	}

	public int getValuesCount ( final char[] chars, final int fromLeft, final int toRight, final char character ) {
		final int left = seekContent(chars,fromLeft,toRight);
		final int right = seekContentReverse(chars,toRight,left);
		int count = 0;
		if ( left < right ) {
			count = 1;
			for ( int i = fromLeft; i < toRight; i += 1 ) {
				if ( chars[i] == character ) {
					count += 1;
				}
			}
		}
		return count;
	}

	public int getValuesCount ( final char[] chars, final int fromLeft, final int toRight, final char[] characters ) {
		final int left = seekContent(chars,fromLeft,toRight);
		final int right = seekContentReverse(chars,toRight,left);
		int count = 0;
		if ( left < right ) {
			count = 1;
			for ( int i = fromLeft; i < toRight; i += 1 ) {
				if ( isMatch(chars,i,characters,0,characters.length) ) {
					i += characters.length - 1;
					count += 1;
				}
			}
		}
		return count;
	}

	public int seekPosition ( final byte[] bytes, final int fromLeft, final int toRight, final byte character ) {
		if ( fromLeft < toRight ) {
			for ( int i = fromLeft; i < toRight; i += 1 ) {
				if ( bytes[i] == character ) {
					return i;
				}
			}
			return toRight;
		}
		return fromLeft;
	}

	public int seekPosition ( final byte[] bytes, final int fromLeft, final int toRight, final byte[] characters ) {
		if ( fromLeft < toRight ) {
			for ( int i = fromLeft; i < toRight - characters.length; i += 1 ) {
				if ( isMatch(bytes,i,characters,0,characters.length) ) {
					return i;
				}
			}
			return toRight - characters.length;
		}
		return fromLeft;
	}

	public int seekPosition ( final char[] chars, final int fromLeft, final int toRight, final char character ) {
		if ( fromLeft < toRight ) {
			for ( int i = fromLeft; i < toRight; i += 1 ) {
				if ( chars[i] == character ) {
					return i;
				}
			}
			return toRight;
		}
		return fromLeft;
	}

	public int seekPosition ( final char[] chars, final int fromLeft, final int toRight, final char[] characters ) {
		if ( fromLeft < toRight ) {
			for ( int i = fromLeft; i < toRight - characters.length; i += 1 ) {
				if ( isMatch(chars,i,characters,0,characters.length) ) {
					return i;
				}
			}
			return toRight - characters.length;
		}
		return fromLeft;
	}

	private boolean isWhitespace ( final byte character ) {
		return character >= 0 && character <= 32;
	}

	private boolean isWhitespace ( final char character ) {
		return character >= 0 && character <= 32;
	}

	private boolean isMatch ( final byte[] bytesA, final int offsetA, final byte[] bytesB, final int offsetB, final int length ) {
		final int limit = offsetA + length;
		for ( int i = offsetA, j = offsetB; i < limit; i += 1,j += 1 ) {
			if ( bytesA[i] != bytesB[j] ) {
				return false;
			}
		}
		return true;
	}

	private boolean isMatch ( final char[] charsA, final int offsetA, final char[] charsB, final int offsetB, final int length ) {
		final int limit = offsetA + length;
		for ( int i = offsetA, j = offsetB; i < limit; i += 1,j += 1 ) {
			if ( charsA[i] != charsB[j] ) {
				return false;
			}
		}
		return true;
	}

}
