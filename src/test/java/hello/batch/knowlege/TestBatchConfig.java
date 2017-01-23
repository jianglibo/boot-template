package hello.batch.knowlege;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.Test;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.scope.JobScope;
import org.springframework.batch.core.scope.StepScope;
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

		assertThat("there should be one and only one stepscope bean.", applicationContext.getBeansOfType(StepScope.class).size(), equalTo(1));
		assertThat("there should be one and only one jobscope bean.", applicationContext.getBeansOfType(JobScope.class).size(), equalTo(1));
	}

}
