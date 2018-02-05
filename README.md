# CSV IO

## About
CSV IO is a library to read and write CSV files. CSV IO is published at <https://github.com/vbsw/csvio>.

## Copyright
See file COPYRIGHT.

## Example
Code:

	import java.io.IOException;
	import java.nio.file.Files;
	import java.nio.file.Path;
	import java.nio.file.Paths;
	
	public class Main extends CSVProcessor {

		private CSVParser parser;

		public static void main ( String[] args ) {
			final Path filePath = Paths.get(System.getProperty("user.home"),"csvio.test.csv");
			final String content = "asdf,qwer,yxcv";
			final Main processor = new Main();
			final CSVReader reader = new CSVReader();
			try {
				Files.write(filePath,content.getBytes());
				reader.readFile(filePath,processor);
				Files.delete(filePath);
			} catch ( IOException e ) {
			}
		}

		@Override
		public void startProcessing ( CSVParser csvParser ) {
			this.parser = csvParser;
		}

		@Override
		public void processCSV ( byte[] bytes, int from, int to, int lineNumber, int bytesReadTotal ) {
			final byte[][] values = this.parser.splitValues(bytes,from,to);
			System.out.print("values:");
			for ( int i = 0; i < values.length; i += 1 ) {
				System.out.print(" " + new String(values[i]));
			}
			System.out.println();
		}

		@Override
		public void endProcessing ( int bytesReadTotal ) {
			System.out.println("bytes read: " + bytesReadTotal);
		}

		@Override
		public void setException ( IOException e ) {
			e.printStackTrace();
		}

	}

Output:

	values: asdf qwer yxcv
	bytes read: 14

## Compiling
Install [Java](http://www.oracle.com/technetwork/java/javase/downloads/index.html), [Git](https://git-scm.com) and [Eclipse](https://www.eclipse.org).

Clone this project into your Eclipse workspace

	$ git clone https://github.com/vbsw/csvio.git

Open Eclipse and create a Java Project named csvio (like previously created directory). To create a jar file, right click on project, then "export...", then export as "JAR file".

## References
- <https://git-scm.com/book/en/v2/Getting-Started-Installing-Git>
- <https://www.eclipse.org>
- <http://www.oracle.com/technetwork/java/javase/downloads/index.html>
