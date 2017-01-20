package hello.util;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.io.ByteStreams;

import hello.TbatchBase;

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
	
//	@After
//	public void af() throws IOException {
//		fs.delete(tresource, false);
//	}

	@Test
	public void t() throws IOException {
		assertNotNull(fs);
		String ps = "hdfs:" + tresource.toString(); // because I constructed HdfsResourceLoader with no user, so must give full path.
		InputStream is = applicationContext.getResource(ps).getInputStream();
		String c = new String(ByteStreams.toByteArray(is));
		is.close();
		
		assertThat("content should be right", c, equalTo("hello resource"));
		assertThat("content should be right", fsUtil.getFileContent(tresource), equalTo("hello resource"));

		ps = "classpath:log4j2.xml";
		is = applicationContext.getResource(ps).getInputStream();
		c = new String(ByteStreams.toByteArray(is));
		is.close();
		
		assertThat("log4j2.xml can load by resource", c.length(), greaterThan(0));
		
		
	}
}
