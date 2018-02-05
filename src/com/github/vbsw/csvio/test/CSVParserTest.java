/**
 *   Copyright 2018, Vitali Baumtrok <vbsw@mailbox.org>.
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file BSL-1.0.txt or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.csvio.test;


import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.vbsw.csvio.CSVParser;


/**
 * @author Vitali Baumtrok
 */
class CSVParserTest {

	@Test
	void testSeekAfterLF ( ) {
		final CSVParser parser = new CSVParser();
		final String str1 = "   asdf adf\n asdf";
		final String str2 = "\n asdf";
		final String str3 = "   asdf adf asdf";
		final String str4 = "   \t\t\r\t\r    ";

		assertEquals(12,parser.seekAfterLF(str1.getBytes(),0,str1.length()));
		assertEquals(5,parser.seekAfterLF(str1.getBytes(),5,5));
		assertEquals(1,parser.seekAfterLF(str2.getBytes(),0,str2.length()));
		assertEquals(str3.length(),parser.seekAfterLF(str3.getBytes(),0,str3.length()));
		assertEquals(str4.length(),parser.seekAfterLF(str4.getBytes(),0,str4.length()));
	}

	@Test
	void testSeekContent ( ) {
		final CSVParser parser = new CSVParser();
		final String str1 = "     asdf adf\n asdf";
		final String str2 = "\n asdf";
		final String str4 = "   \t\t\r\t\r    ";

		assertEquals(5,parser.seekContent(str1.getBytes(),0,str1.length()));
		assertEquals(1,parser.seekContent(str1.getBytes(),1,-10));
		assertEquals(2,parser.seekContent(str2.getBytes(),0,str2.length()));
		assertEquals(str4.length(),parser.seekContent(str4.getBytes(),0,str4.length()));
	}

	@Test
	void testSeekContentReverse ( ) {
		final CSVParser parser = new CSVParser();
		final String str1 = "     asdf adf\n asdf";
		final String str2 = "\n asdf   ";
		final String str4 = "   \t\t\r\t\r    ";

		assertEquals(0,parser.seekContentReverse(str1.getBytes(),5,0));
		assertEquals(5,parser.seekContentReverse(str1.getBytes(),5,10));
		assertEquals(13,parser.seekContentReverse(str1.getBytes(),15,0));
		assertEquals(6,parser.seekContentReverse(str2.getBytes(),str2.length(),0));
		assertEquals(0,parser.seekContentReverse(str4.getBytes(),str4.length(),0));
		assertEquals(str4.length(),parser.seekContentReverse(str4.getBytes(),str4.length(),str4.length()));
	}

	@Test
	void testSplitValues ( ) {
		final CSVParser parser = new CSVParser();
		final String str1 = "asdf   ,  qwer,yxcv,";
		final String str2 = "asdf   {|}  qwer{|}yxcv{|}";
		final byte[] separator2 = new byte[] { '{', '|', '}' };
		final byte[][] values1 = parser.splitValues(str1.getBytes(),0,str1.length());
		final byte[][] values2 = parser.splitValues(str2.getBytes(),0,str2.length(),separator2);

		assertEquals(4,values1.length);
		assertEquals(true,new String(values1[0]).equals("asdf"));
		assertEquals(true,new String(values1[1]).equals("qwer"));
		assertEquals(true,new String(values1[2]).equals("yxcv"));
		assertEquals(true,new String(values1[3]).equals(""));

		assertEquals(4,values2.length);
		assertEquals(true,new String(values2[0]).equals("asdf"));
		assertEquals(true,new String(values2[1]).equals("qwer"));
		assertEquals(true,new String(values2[2]).equals("yxcv"));
		assertEquals(true,new String(values2[3]).equals(""));
	}

	@Test
	void testEndsWithLF ( ) {
		final CSVParser parser = new CSVParser();
		final String str1 = "   asdf adf\n asdf";
		final String str2 = "\n asdf";
		final String str3 = "   asdf adf asdf";
		final String str4 = "   \t\t\r\t\r    ";

		assertEquals(true,parser.endsWithLF(str1.getBytes(),12,0));
		assertEquals(false,parser.endsWithLF(str1.getBytes(),12,12));
		assertEquals(false,parser.endsWithLF(str1.getBytes(),12,100));
		assertEquals(true,parser.endsWithLF(str2.getBytes(),1,0));
		assertEquals(false,parser.endsWithLF(str2.getBytes(),str2.length(),0));
		assertEquals(false,parser.endsWithLF(str3.getBytes(),4,0));
		assertEquals(false,parser.endsWithLF(str3.getBytes(),str3.length(),0));
		assertEquals(false,parser.endsWithLF(str4.getBytes(),4,0));
		assertEquals(false,parser.endsWithLF(str4.getBytes(),6,0));
	}

	@Test
	void testIsWhitespace ( ) {
		final CSVParser parser = new CSVParser();
		final String str1 = "   asdf adf\n asdf";
		final String str2 = "\n asdf";
		final String str3 = "   asdf adf asdf";
		final String str4 = "   \t\t\r\t\r    ";

		assertEquals(false,parser.isWhitespace(str1.getBytes(),0,12));
		assertEquals(true,parser.isWhitespace(str1.getBytes(),0,3));
		assertEquals(true,parser.isWhitespace(str1.getBytes(),100,0));
		assertEquals(true,parser.isWhitespace(str2.getBytes(),0,2));
		assertEquals(false,parser.isWhitespace(str2.getBytes(),0,3));
		assertEquals(false,parser.isWhitespace(str3.getBytes(),4,str3.length()));
		assertEquals(true,parser.isWhitespace(str3.getBytes(),7,8));
		assertEquals(true,parser.isWhitespace(str4.getBytes(),4,0));
		assertEquals(true,parser.isWhitespace(str4.getBytes(),0,str4.length()));
		assertEquals(true,parser.isWhitespace(str4.getBytes(),3,8));
	}

}
