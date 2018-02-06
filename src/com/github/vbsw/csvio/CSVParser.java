/**
 *   Copyright 2018, Vitali Baumtrok <vbsw@mailbox.org>.
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file BSL-1.0.txt or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.csvio;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


/**
 * Parser to parse byte and char arrays.
 * 
 * @author Vitali Baumtrok
 */
public class CSVParser {

	/**
	 * Returns the index after line feed (LF) or the end index.
	 * @param bytes Bytes to parse.
	 * @param fromLeft Start index (inclusive).
	 * @param toRight End index (exclusive).
	 * @return The index after line feed or the end index.
	 */
	public int seekAfterLF ( final byte[] bytes, final int fromLeft, final int toRight ) {
		if ( fromLeft < toRight ) {
			for ( int i = fromLeft; i < toRight; i += 1 ) {
				if ( bytes[i] == '\n' ) {
					return i + 1;
				}
			}
			return toRight;
		}
		return fromLeft;
	}

	/**
	 * Returns the index after line feed (LF) or the end index.
	 * @param chars Characters to parse.
	 * @param fromLeft Start index (inclusive).
	 * @param toRight End index (exclusive).
	 * @return The index after line feed or the end index.
	 */
	public int seekAfterLF ( final char[] chars, final int fromLeft, final int toRight ) {
		if ( fromLeft < toRight ) {
			for ( int i = fromLeft; i < toRight; i += 1 ) {
				if ( chars[i] == '\n' ) {
					return i + 1;
				}
			}
			return toRight;
		}
		return fromLeft;
	}

	/**
	 * Returns the index of the first byte that is not whitespace or returns the end index.
	 * @param bytes Bytes to parse.
	 * @param fromLeft Start index (inclusive).
	 * @param toRight End index (exclusive).
	 * @return The index of the first non whitespace byte or the end index.
	 */
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

	/**
	 * Returns the index of the first byte that is not whitespace or returns the end index.
	 * @param chars Characters to parse.
	 * @param fromLeft Start index (inclusive).
	 * @param toRight End index (exclusive).
	 * @return The index of the first non whitespace byte or the end index.
	 */
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

	/**
	 * Checks the bytes from right to left and returns the index of the first byte that is not whitespace or returns the start index.
	 * @param bytes Bytes to parse.
	 * @param fromRight Start index (exclusive).
	 * @param toLeft End index (inclusive).
	 * @return The index of the first non whitespace byte or the start index.
	 */
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

	/**
	 * Checks the bytes from right to left and returns the index of the first byte that is not whitespace or returns the start index.
	 * @param chars Characters to parse.
	 * @param fromRight Start index (exclusive).
	 * @param toLeft End index (inclusive).
	 * @return The index of the first non whitespace byte or the start index.
	 */
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

	/**
	 * Returns true, if the byte before the start index is a line feed (LF).
	 * @param bytes Bytes to parse.
	 * @param fromRight Start index (exclusive).
	 * @param toLeft End index (inclusive).
	 * @return true, if the byte before the start index is a line feed.
	 */
	public boolean endsWithLF ( final byte[] bytes, final int fromRight, final int toLeft ) {
		return fromRight > toLeft && bytes[fromRight - 1] == '\n';
	}

	/**
	 * Returns true, if the byte before the start index is a line feed (LF).
	 * @param chars Characters to parse.
	 * @param fromRight Start index (exclusive).
	 * @param toLeft End index (inclusive).
	 * @return true, if the byte before the start index is a line feed.
	 */
	public boolean endsWithLF ( final char[] chars, final int fromRight, final int toLeft ) {
		return fromRight > toLeft && chars[fromRight - 1] == '\n';
	}

	/**
	 * Returns true, if all bytes are whitespace. Empty interval counts as whitespace.
	 * @param bytes Bytes to parse.
	 * @param fromLeft Start index (inclusive).
	 * @param toRight End index (exclusive).
	 * @return true, if all bytes are whitespace.
	 */
	public boolean isWhitespace ( final byte[] bytes, final int fromLeft, final int toRight ) {
		for ( int i = fromLeft; i < toRight; i += 1 ) {
			if ( !isWhitespace(bytes[i]) ) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns true, if all bytes are whitespace. Empty interval counts as whitespace.
	 * @param chars Characters to parse.
	 * @param fromLeft Start index (inclusive).
	 * @param toRight End index (exclusive).
	 * @return true, if all bytes are whitespace.
	 */
	public boolean isWhitespace ( final char[] chars, final int fromLeft, final int toRight ) {
		for ( int i = fromLeft; i < toRight; i += 1 ) {
			if ( !isWhitespace(chars[i]) ) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns values that are separated by comma.
	 * @param bytes Bytes to parse.
	 * @param fromLeft Start index (inclusive).
	 * @param toRight End index (exclusive).
	 * @return Values that are separated by comma.
	 */
	public String[] splitValues ( final byte[] bytes, final int fromLeft, final int toRight ) {
		return splitValues(bytes,fromLeft,toRight,(byte) ',',StandardCharsets.UTF_8);
	}

	/**
	 * Returns values that are separated by <code>separator</code>.
	 * @param bytes Bytes to parse.
	 * @param fromLeft Start index (inclusive).
	 * @param toRight End index (exclusive).
	 * @param separator The separator dividing the values.
	 * @param charset The encoding of the values stored in bytes to parse.
	 * @return Values that are separated by <code>separator</code>.
	 */
	public String[] splitValues ( final byte[] bytes, final int fromLeft, final int toRight, final byte separator, final Charset charset ) {
		final int valuesCount = getValuesCount(bytes,fromLeft,toRight,separator);
		final String[] values = new String[valuesCount];
		int offset = fromLeft;
		for ( int i = 0; i < values.length; i += 1 ) {
			final int offsetNew = seekPosition(bytes,offset,toRight,separator);
			final int left = seekContent(bytes,offset,offsetNew);
			final int right = seekContentReverse(bytes,offsetNew,left);
			values[i] = new String(bytes,left,right - left,charset);
			offset = offsetNew + 1;
		}
		return values;
	}

	/**
	 * Returns values that are separated by <code>separator</code>.
	 * @param bytes Bytes to parse.
	 * @param fromLeft Start index (inclusive).
	 * @param toRight End index (exclusive).
	 * @param separator The separator dividing the values.
	 * @param charset The encoding of the values stored in bytes to parse.
	 * @return Values that are separated by <code>separator</code>.
	 */
	public String[] splitValues ( final byte[] bytes, final int fromLeft, final int toRight, final byte[] separator, final Charset charset ) {
		final int valuesCount = getValuesCount(bytes,fromLeft,toRight,separator);
		final String[] values = new String[valuesCount];
		int offset = fromLeft;
		for ( int i = 0; i < values.length; i += 1 ) {
			final int offsetNew = seekPosition(bytes,offset,toRight,separator);
			final int left = seekContent(bytes,offset,offsetNew);
			final int right = seekContentReverse(bytes,offsetNew,left);
			values[i] = new String(bytes,left,right - left,charset);
			offset = offsetNew + separator.length;
		}
		return values;
	}

	/**
	 * Returns values that are separated by comma.
	 * @param chars Characters to parse.
	 * @param fromLeft Start index (inclusive).
	 * @param toRight End index (exclusive).
	 * @return Values that are separated by comma.
	 */
	public String[] splitValues ( final char[] chars, final int fromLeft, final int toRight ) {
		return splitValues(chars,fromLeft,toRight,',');
	}

	/**
	 * Returns values that are separated by <code>separator</code>.
	 * @param chars Characters to parse.
	 * @param fromLeft Start index (inclusive).
	 * @param toRight End index (exclusive).
	 * @param separator The separator dividing the values.
	 * @param charset The encoding of the values stored in bytes to parse.
	 * @return Values that are separated by <code>separator</code>.
	 */
	public String[] splitValues ( final char[] chars, final int fromLeft, final int toRight, final char separator ) {
		final int valuesCount = getValuesCount(chars,fromLeft,toRight,separator);
		final String[] values = new String[valuesCount];
		int offset = fromLeft;
		for ( int i = 0; i < values.length; i += 1 ) {
			final int offsetNew = seekPosition(chars,offset,toRight,separator);
			final int left = seekContent(chars,offset,offsetNew);
			final int right = seekContentReverse(chars,offsetNew,left);
			values[i] = new String(chars,left,right - left);
			offset = offsetNew + 1;
		}
		return values;
	}

	/**
	 * Returns values that are separated by <code>separator</code>.
	 * @param chars Characters to parse.
	 * @param fromLeft Start index (inclusive).
	 * @param toRight End index (exclusive).
	 * @param separator The separator dividing the values.
	 * @param charset The encoding of the values stored in bytes to parse.
	 * @return Values that are separated by <code>separator</code>.
	 */
	public String[] splitValues ( final char[] chars, final int fromLeft, final int toRight, final char[] separator ) {
		final int valuesCount = getValuesCount(chars,fromLeft,toRight,separator);
		final String[] values = new String[valuesCount];
		int offset = fromLeft;
		for ( int i = 0; i < values.length; i += 1 ) {
			final int offsetNew = seekPosition(chars,offset,toRight,separator);
			final int left = seekContent(chars,offset,offsetNew);
			final int right = seekContentReverse(chars,offsetNew,left);
			values[i] = new String(chars,left,right - left);
			offset = offsetNew + separator.length;
		}
		return values;
	}

	/**
	 * Returns the number of values separated by comma.
	 * @param bytes Bytes to parse.
	 * @param fromLeft Start index (inclusive).
	 * @param toRight End index (exclusive).
	 * @return Number of values separated by comma.
	 */
	public int getValuesCount ( final byte[] bytes, final int fromLeft, final int toRight ) {
		return getValuesCount(bytes,fromLeft,toRight,(byte) ',');
	}

	/**
	 * Returns the number of values separated by the <code>separator</code>.
	 * @param bytes Bytes to parse.
	 * @param fromLeft Start index (inclusive).
	 * @param toRight End index (exclusive).
	 * @param separator The separator dividing the values.
	 * @return Number of values separated by the <code>separator</code>.
	 */
	public int getValuesCount ( final byte[] bytes, final int fromLeft, final int toRight, final byte separator ) {
		final int left = seekContent(bytes,fromLeft,toRight);
		final int right = seekContentReverse(bytes,toRight,left);
		int count = 0;
		if ( left < right ) {
			count = 1;
			for ( int i = fromLeft; i < toRight; i += 1 ) {
				if ( bytes[i] == separator ) {
					count += 1;
				}
			}
		}
		return count;
	}

	/**
	 * Returns the number of values separated by the <code>separator</code>.
	 * @param bytes Bytes to parse.
	 * @param fromLeft Start index (inclusive).
	 * @param toRight End index (exclusive).
	 * @param separator The separator dividing the values.
	 * @return Number of values separated by the <code>separator</code>.
	 */
	public int getValuesCount ( final byte[] bytes, final int fromLeft, final int toRight, final byte[] separator ) {
		final int left = seekContent(bytes,fromLeft,toRight);
		final int right = seekContentReverse(bytes,toRight,left);
		int count = 0;
		if ( left < right ) {
			count = 1;
			for ( int i = fromLeft; i < toRight; i += 1 ) {
				if ( isMatch(bytes,i,separator,0,separator.length) ) {
					i += separator.length - 1;
					count += 1;
				}
			}
		}
		return count;
	}

	/**
	 * Returns the number of values separated by comma.
	 * @param chars Characters to parse.
	 * @param fromLeft Start index (inclusive).
	 * @param toRight End index (exclusive).
	 * @return Number of values separated by comma.
	 */
	public int getValuesCount ( final char[] chars, final int fromLeft, final int toRight ) {
		return getValuesCount(chars,fromLeft,toRight,',');
	}

	/**
	 * Returns the number of values separated by the <code>separator</code>.
	 * @param chars Characters to parse.
	 * @param fromLeft Start index (inclusive).
	 * @param toRight End index (exclusive).
	 * @param separator The separator dividing the values.
	 * @return Number of values separated by the <code>separator</code>.
	 */
	public int getValuesCount ( final char[] chars, final int fromLeft, final int toRight, final char separator ) {
		final int left = seekContent(chars,fromLeft,toRight);
		final int right = seekContentReverse(chars,toRight,left);
		int count = 0;
		if ( left < right ) {
			count = 1;
			for ( int i = fromLeft; i < toRight; i += 1 ) {
				if ( chars[i] == separator ) {
					count += 1;
				}
			}
		}
		return count;
	}

	/**
	 * Returns the number of values separated by the <code>separator</code>.
	 * @param chars Characters to parse.
	 * @param fromLeft Start index (inclusive).
	 * @param toRight End index (exclusive).
	 * @param separator The separator dividing the values.
	 * @return Number of values separated by the <code>separator</code>.
	 */
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

	/**
	 * Returns the index of the searched byte or the end index.
	 * @param bytes Bytes to parse.
	 * @param fromLeft Start index (inclusive).
	 * @param toRight End index (exclusive).
	 * @param b Byte to search for.
	 * @return The index of the searched byte or the end index.
	 */
	public int seekPosition ( final byte[] bytes, final int fromLeft, final int toRight, final byte b ) {
		if ( fromLeft < toRight ) {
			for ( int i = fromLeft; i < toRight; i += 1 ) {
				if ( bytes[i] == b ) {
					return i;
				}
			}
			return toRight;
		}
		return fromLeft;
	}

	/**
	 * Returns the index of the searched bytes or the end index.
	 * @param bytes Bytes to parse.
	 * @param fromLeft Start index (inclusive).
	 * @param toRight End index (exclusive).
	 * @param bb Bytes to search for.
	 * @return The index of the searched bytes or the end index.
	 */
	public int seekPosition ( final byte[] bytes, final int fromLeft, final int toRight, final byte[] bb ) {
		if ( fromLeft < toRight ) {
			for ( int i = fromLeft; i < toRight - bb.length; i += 1 ) {
				if ( isMatch(bytes,i,bb,0,bb.length) ) {
					return i;
				}
			}
			return toRight - bb.length;
		}
		return fromLeft;
	}

	/**
	 * Returns the index of the searched character or the end index.
	 * @param chars Characters to parse.
	 * @param fromLeft Start index (inclusive).
	 * @param toRight End index (exclusive).
	 * @param character Character to search for.
	 * @return The index of the character or the end index.
	 */
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

	/**
	 * Returns the index of the beginning of the searched characters or the end index.
	 * @param chars Characters to parse.
	 * @param fromLeft Start index (inclusive).
	 * @param toRight End index (exclusive).
	 * @param characters Characters to search for.
	 * @return The index of the beginning of the characters or the end index.
	 */
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

	/**
	 * Converts given value to String.
	 * @param value Value to convert.
	 * @return Given value as String.
	 */
	public String toString ( final long value ) {
		return Long.toString(value);
	}

	/**
	 * Converts given value to String.
	 * @param value Value to convert.
	 * @return Given value as String.
	 */
	public String toString ( final int value ) {
		return Integer.toString(value);
	}

	/**
	 * Converts given value to String.
	 * @param value Value to convert.
	 * @return Given value as String.
	 */
	public String toString ( final short value ) {
		return Short.toString(value);
	}

	/**
	 * Converts given value to String.
	 * @param value Value to convert.
	 * @return Given value as String.
	 */
	public String toString ( final byte value ) {
		return Byte.toString(value);
	}

	/**
	 * Converts given value to String.
	 * @param value Value to convert.
	 * @return Given value as String.
	 */
	public String toString ( final double value ) {
		return Double.toString(value);
	}

	/**
	 * Converts given value to String.
	 * @param value Value to convert.
	 * @return Given value as String.
	 */
	public String toString ( final float value ) {
		return Float.toString(value);
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
