/**
 *   Copyright 2020, Vitali Baumtrok <vbsw@mailbox.org>.
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file BSL-1.0.txt or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */

package com.github.vbsw.csvio;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Vitali Baumtrok
 */
public class CSVFile {

	protected CSVProperties properties;
	protected IOClient client;

	public CSVFile (final CSVProperties properties, final IOClient client) {
		this.properties = properties;
		this.client = client;
	}

	public CSVProperties getProperties() {
		return properties;
	}

	public IODetails read() {
		final IODetails details = createDetails();
		final CSVProperties properties = getProperties();
		final Path filePath = properties.getFilePath();
		try (final InputStream inputStream = Files.newInputStream(filePath)) {
			final CSVInput input = new CSVInput(inputStream,details);
			final Charset charset = properties.getCharset();
			final byte[] delimiter = properties.getDelimiter().getBytes(charset);
			final int[] headerMapping = readFirstLine(input,delimiter,charset,details);
			while (input.isMoreToRead()) {
				final String[] values = input.getLineValues(delimiter,charset);
				details.contentLinesCount++;
				client.processRead(values,headerMapping,details);
				input.seekLineContent();
			}
			details.readSuccessful = details.errorLinesCount == 0;
		} catch (final IOException e) {
			details.ioException = e;
		}
		return details;
	}

	public IODetails write() {
		final IODetails details = createDetails();
		client.prepairWrite();
		client.processWrite();
		return details;
	}

	protected IODetails createDetails() {
		return new IODetails();
	}

	protected int[] readFirstLine(final CSVInput input, final byte[] delimiter, final Charset charset, final IODetails details) throws IOException {
		input.readFromStream();
		input.seekFirstLineContent();
		client.prepairRead();
		if (input.isMoreToRead()) {
			final String[] lineValues = input.getLineValues(delimiter,charset);
			final int[] headerMapping = createHeaderMapping(lineValues);
			if (headerMapping == null) {
				final String[] values = input.getLineValues(delimiter,charset);
				details.contentLinesCount++;
				client.processRead(values,headerMapping,details);
			} else {
				details.headerAvailable = true;
			}
		}
		return null;
	}

	protected int[] createHeaderMapping(final String[] csvFieldNames) {
		final String[] fieldNames = client.getFieldNames();
		final int[] mapping = new int[fieldNames.length];
		for (int i = 0; i < fieldNames.length; i++) {
			mapping[i] = -1;
			for (int j = 0; j < csvFieldNames.length; j++) {
				if (fieldNames[i].equals(csvFieldNames[j])) {
					mapping[i] = j;
					break;
				}
			}
		}
		for (int i: mapping)
			if (i >= 0)
				return mapping;
		return null;
	}

}
