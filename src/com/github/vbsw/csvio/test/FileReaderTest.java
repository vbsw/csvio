/**
 *   Copyright 2018, Vitali Baumtrok <vbsw@mailbox.org>.
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file BSL-1.0.txt or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.csvio.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import com.github.vbsw.csvio.CSVReader;
import com.github.vbsw.csvio.FileReader;


class FileReaderTest extends FileReader {

	public FileReaderTest ( ) {
		super(6);
	}

	@Test
	void test ( ) {
		final String content = "asdf,qwer,yxcv\n\ntt,uu,vv,ww\n\n\n\nxyz";
		final Path filePath = Paths.get(System.getProperty("user.home"),"csvio.test.csv");
		final CSVReader reader = new CSVReader(this);
		final CSVReaderTest csvProcessor = new CSVReaderTest();

		createFile(filePath,content);
		reader.readFile(filePath,csvProcessor);
		deleteFile(filePath);

		assertNotEquals(null,csvProcessor.parser);
		assertNotEquals(null,csvProcessor.lineNumbers);
		assertNotEquals(null,csvProcessor.values);
		assertEquals(null,csvProcessor.exeption);
		assertEquals(content.length(),csvProcessor.bytesReadTotal);
		assertEquals(4,csvProcessor.values.length);
		assertEquals(3,csvProcessor.values[0].length);
		assertEquals(4,csvProcessor.values[1].length);
		assertEquals(1,csvProcessor.values[2].length);
		assertEquals(4,csvProcessor.lineNumbers.length);
		assertEquals("asdf",csvProcessor.values[0][0]);
		assertEquals("qwer",csvProcessor.values[0][1]);
		assertEquals("yxcv",csvProcessor.values[0][2]);
		assertEquals("tt",csvProcessor.values[1][0]);
		assertEquals("uu",csvProcessor.values[1][1]);
		assertEquals("vv",csvProcessor.values[1][2]);
		assertEquals("ww",csvProcessor.values[1][3]);
		assertEquals("xyz",csvProcessor.values[2][0]);
		assertEquals(1,csvProcessor.lineNumbers[0]);
		assertEquals(3,csvProcessor.lineNumbers[1]);
		assertEquals(7,csvProcessor.lineNumbers[2]);
		assertEquals(-1,csvProcessor.lineNumbers[3]);
		assertEquals((String) null,csvProcessor.values[3]);
	}

	private void createFile ( final Path filePath, final String str ) {
		try {
			Files.write(filePath,str.getBytes());
		} catch ( IOException e ) {
		}
	}

	private void deleteFile ( final Path filePath ) {
		try {
			Files.delete(filePath);
		} catch ( IOException e ) {
		}
	}

}
