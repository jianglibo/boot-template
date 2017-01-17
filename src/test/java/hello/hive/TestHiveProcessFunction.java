package hello.hive;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.io.ByteStreams;

import hello.batch.TbatchBase;

public class TestHiveProcessFunction extends TbatchBase {
	
	@Autowired
	private HiveCreateTable hct;
	
	
	@Test
	public void tresource() throws IOException {
		String c = new String(ByteStreams.toByteArray(applicationContext.getResource("classpath:fixtures/string/singlequota.txt").getInputStream()));
		
		c = hct.escapeSingleQuota(c);
		assertThat(c, equalTo("\\'abcu\\'\\'\\'zz"));
	}
	
	@Test
	public void t() {
		String s = "'''''a\\'";
		String news = hct.escapeSingleQuota(s);
		assertThat(news, equalTo("\\'\\'\\'\\'\\'a\\'"));
		
		s = "\'''''a\\'";
		news = hct.escapeSingleQuota(s);
		assertThat(news, equalTo("\\'\\'\\'\\'\\'a\\'"));
		
		s = "\'\'\'\''a\\'";
		news = hct.escapeSingleQuota(s);
		assertThat(news, equalTo("\\'\\'\\'\\'\\'a\\'"));
	}
	
	@Test
	public void tresource1() throws IOException {
		String c = new String(ByteStreams.toByteArray(applicationContext.getResource("classpath:fixtures/string/singlequota.txt").getInputStream()));
		
		c = c.replaceAll("\\\\?'", "\\\\'");
		assertThat(c, equalTo("\\'abcu\\'\\'\\'zz"));
	}
	
	@Test
	public void t1() {
		String result = "\\'\\'\\'\\'\\'a\\'";
		
		String s = "'''''a\\'";
		String news = s.replaceAll("\\\\?'", "\\\\'");
		assertThat(news, equalTo(result));
		
		s = "\'''''a\\'";
		news = s.replaceAll("\\\\?'", "\\\\'");
		assertThat(news, equalTo(result));
		
		s = "\'\'\'\''a\\'";
		news = s.replaceAll("\\\\?'", "\\\\'");
		assertThat(news, equalTo(result));
	}

}
