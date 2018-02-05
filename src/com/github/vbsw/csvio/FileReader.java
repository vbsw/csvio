/**
 *   Copyright 2018, Vitali Baumtrok <vbsw@mailbox.org>.
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file BSL-1.0.txt or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.csvio;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;


/**
 * Buffered file reader.
 * 
 * @author Vitali Baumtrok
 */
public class FileReader {

	protected static final int INITIAL_BUFFER_CAPACITY = 1024 * 8 - 64 * 8;

	protected ByteBuffer buffer;
	protected SeekableByteChannel channel;
	protected int bytesLength;
	protected int bytesRead;

	public FileReader ( ) {
		this(INITIAL_BUFFER_CAPACITY);
	}

	public FileReader ( final int initialBufferCapacity ) {
		buffer = createByteBuffer(initialBufferCapacity);
	}

	protected void startFileReading ( final SeekableByteChannel channel ) {
		this.channel = channel;
		this.bytesLength = 0;
		this.bytesRead = 0;
	}

	public void readFile ( ) throws IOException {
		bytesLength = buffer.position();
		bytesRead = channel.read(buffer);
		if ( bytesRead >= 0 ) {
			bytesLength += bytesRead;
		}
	}

	public byte[] getBytes ( ) {
		return buffer.array();
	}

	public int getBytesLength ( ) {
		return bytesLength;
	}

	public int getBytesRead ( ) {
		return bytesRead;
	}

	public int preserveBufferAndEnsureCapacity ( final int fromLeft, final int toRight ) {
		if ( fromLeft < toRight ) {
			final byte[] bytes = buffer.array();

			if ( fromLeft > 0 ) {
				bytesLength = toRight - fromLeft;
				System.arraycopy(bytes,fromLeft,bytes,0,bytesLength);
			} else if ( toRight >= bytes.length ) {
				buffer = createByteBufferExtended(bytes,0,bytes.length);
				bytesLength = bytes.length;
			} else {
				bytesLength = toRight;
			}
		} else {
			bytesLength = 0;
		}
		buffer.position(bytesLength);
		return bytesLength;
	}

	protected ByteBuffer createByteBuffer ( final int capacity ) {
		final ByteBuffer buffer = ByteBuffer.allocate(capacity);
		return buffer;
	}

	protected ByteBuffer createByteBufferExtended ( final byte[] bytes, final int from, final int copyLength ) {
		final ByteBuffer bufferNew = ByteBuffer.allocate(bytes.length * 2);
		final byte[] bufferBytesNew = bufferNew.array();
		System.arraycopy(bytes,from,bufferBytesNew,0,copyLength);
		return bufferNew;
	}

}
