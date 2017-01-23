package hello.batch.knowlege;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.Test;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import hello.TbatchBase;

public class TestBatchConfig extends TbatchBase {
	
	@Autowired
	@Qualifier("syncJobLauncher")
	private JobLauncher defaultJobLauncher;
	
	@Test
	public void tJobLauncher() {
		Map<String,JobLauncher> jls = applicationContext.getBeansOfType(JobLauncher.class);
		assertThat(jls.size(), equalTo(3));
	}

}
