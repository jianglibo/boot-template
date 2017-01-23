package hello.util;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

public class TestHadoopLocalFileSystem {

	@Test
	public void t() throws FileNotFoundException, IllegalArgumentException, IOException {
		FileSystem fs = new LocalFileSystem();
		
		fs.listFiles(new Path("."), false);
	}
}
