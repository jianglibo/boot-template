package hello.batch.nutch;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import hello.util.DirectoryDeleter;

public class TestUnjarCleaner {
	
	private static final Path tp = Paths.get("testtmp");
	
	private int i = 0;
	
	@Before
	public void b() throws IOException {
		if (Files.exists(tp)) {
			DirectoryDeleter.deleteRecursive(tp.toFile());
		}
		Files.createDirectories(tp);
		i = 0;
	}
	
	@After
	public void a() throws IOException {
		Files.createDirectories(tp);
	}
	
	private void createAFile(int n) throws IOException, InterruptedException {
		int top = i + n;
		for (; i < top ; i++) {
			Thread.sleep(100);
			Files.write(tp.resolve(String.valueOf(i)), "abc".getBytes());
		}
	}
	
	@Test
	public void t() throws IOException, InterruptedException {
		createAFile(5);
		assertThat("there should be 5 files.",tp.toFile().list().length, equalTo(5) );
		
		UnjarCleaner uc = new UnjarCleaner();
		uc.setMaxUnjarNumber(3);
		List<File> files = uc.getOutDatedDirectory(tp);
		assertThat("there should be 2 outdated files.",files.size(), equalTo(2));
		String outdateds =  files.stream().map(f -> f.getName()).collect(Collectors.joining());
		assertThat("outdated files should be 01.",outdateds, equalTo("01") );
		
		uc.clean(tp);
		
		files = uc.getOutDatedDirectory(tp);
		assertThat("there should be 0 outdated files.",files.size(), equalTo(0));
		outdateds =  files.stream().map(f -> f.getName()).collect(Collectors.joining());
		assertThat("outdated files should be 01.",outdateds, equalTo("") );

	}

}
