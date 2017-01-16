package hello.util;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import hello.batch.TbatchBase;

public class TestHiveTestTable extends TbatchBase {
	
	@Autowired
	private FileSystem fs;
	
	@Autowired
	private FsUtil fsUtil;
	
	private Path tresource;
	
	@Before
	public void be() throws IOException {
		tresource =  fsUtil.convertToUserPath("tresource.txt");
		if (!fs.exists(tresource)) {
			FSDataOutputStream fsdos = fs.create(tresource);
			fsdos.write("hello resource".getBytes());
			fsdos.flush();
			fsdos.close();
		}
	}
	
	@After
	public void af() throws IOException {
		fs.delete(tresource, false);
	}

	@Test
	public void t() throws IOException {
		assertNotNull(fs);
		assertThat("content should be right", fsUtil.getFileContent(tresource), equalTo("hello resource"));
	}
}
