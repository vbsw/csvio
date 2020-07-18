/**
 *   Copyright 2020, Vitali Baumtrok <vbsw@mailbox.org>.
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file BSL-1.0.txt or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */

package com.github.vbsw.csvio;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @author Vitali Baumtrok
 */
public class CSVMain {

	public static void main(String[] args) throws Exception {
		final Path path = Paths.get(System.getProperty("user.home"),"csvio.test.csv");
		final String content = "asdf,qwer,yxcv \n a,b,c,d";
		final CSVFile file = new CSVFile(path);

		Files.write(path,content.getBytes());
		
		CSVFileReader reader = file.getReader();
		for (String[] fields: reader) {
			System.out.println(Arrays.toString(fields));
		}
		reader.close();
		System.out.println("bytes read: " + reader.getStats().bytesCount);
		Files.delete(path);

		//		final Path path = Paths.get("C:\\Users\\vbkau\\Documents\\dev\\java\\Test\\testdata\\person.csv");
		//		final Charset charset = StandardCharsets.UTF_8;
		//		final String[] header = new String[] {"id", "mother_id", "father_id", "name_id"};
		//		final CSVFile file = new CSVFile(path,";",charset,header);
		//		try (final CSVFileReader reader = file.getReader()) {
		//			for (String[] fields: reader) {
		//				System.out.println(Arrays.toString(fields));
		//			}
		//			System.out.println(reader.getStats());
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//		}
	}

}
