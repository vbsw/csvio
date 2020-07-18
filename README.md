# CSV IO

## About
CSV IO is a library to read and write CSV files. CSV IO is published on <https://github.com/vbsw/csvio>.

## Example
Code:

	import java.nio.file.Files;
	import java.nio.file.Path;
	import java.nio.file.Paths;
	import java.util.Arrays;

	public class Main {

		public static void main ( String[] args ) throws Exception {
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
		}

	}

Output:

	[asdf, qwer, yxcv]
	[a, b, c, d]
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
