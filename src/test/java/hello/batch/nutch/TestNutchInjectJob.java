package hello.batch.nutch;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobInstanceException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;

import hello.TbatchBase;

public class TestNutchInjectJob extends TbatchBase {
	
	@Autowired
	private NutchFolderUtil nutchFolderUtil;
	
	private String crawlId = "fhgov";
	
	public TestNutchInjectJob() {
		super(NutchTaskNames.NUTCH_INJECT);
	}
	
	@Before
	public void b() throws IllegalArgumentException, IOException {
		nutchFolderUtil.deleteSeedDir(crawlId);
	}
	
	@Test
	public void t() throws NoSuchJobException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, NoSuchJobInstanceException, InterruptedException, JobParametersNotFoundException, UnexpectedJobExecutionException, IOException {
		Job jb1 = jobRegistry.getJob(getJobName());
		JobExecution je1 = syncJobLauncher.run(jb1, nutchFolderUtil.newCrawJobParameterBuilder().withCommonJobParameterBuilder().crawlId(crawlId).and().build());
		assertTrue("status should be compeleted", je1.getStatus() == BatchStatus.COMPLETED);
	}
}
