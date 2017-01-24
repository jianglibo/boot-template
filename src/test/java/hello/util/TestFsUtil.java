package hello.util;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import com.google.common.io.ByteStreams;

import hello.TbatchBase;

public class TestFsUtil extends TbatchBase {
	
	@Autowired
	private FsUtil fsUtil;
	
	@Test
	public void tRemoteUriString() {
		String s = fsUtil.getRemoteFileURIString("abc", "xx/xx.txt");
		assertThat(s, equalTo("abc/xx.txt"));
	}
	
	@Test
	public void tLocalResource() throws IOException {
		Path p = Paths.get(FIXTURE_BASE, "string", "tlocalfile.txt");
		Resource lr = fsUtil.localFileAsResource(p.toAbsolutePath().normalize().toString());
		String str = new String(ByteStreams.toByteArray(lr.getInputStream()));
		assertThat(str, equalTo("LocalFileAsResource"));
	}

}
