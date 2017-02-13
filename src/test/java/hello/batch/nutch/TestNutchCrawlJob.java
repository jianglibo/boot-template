package hello.batch.nutch;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

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

public class TestNutchCrawlJob extends TbatchBase {
	
	@Autowired
	private NutchFolderUtil nutchFolderUtil;
	
	private String crawlId = "fhgov";
	
	private String batchId = "1486710034-46874";
	
	public TestNutchCrawlJob() {
		super(NutchTaskNames.NUTCH_CRAWL);
	}
	
	/**
	 * batchId is essential, cause of every of 4 steps is needed by.
	 * @throws NoSuchJobException
	 * @throws JobExecutionAlreadyRunningException
	 * @throws JobRestartException
	 * @throws JobInstanceAlreadyCompleteException
	 * @throws JobParametersInvalidException
	 * @throws NoSuchJobInstanceException
	 * @throws InterruptedException
	 * @throws JobParametersNotFoundException
	 * @throws UnexpectedJobExecutionException
	 * @throws IOException
	 */
	@Test
	public void t() throws NoSuchJobException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, NoSuchJobInstanceException, InterruptedException, JobParametersNotFoundException, UnexpectedJobExecutionException, IOException {
		Job jb1 = jobRegistry.getJob(getJobName());
		JobExecution je1 = syncJobLauncher.run(jb1, nutchFolderUtil
				.newCrawJobParameterBuilder()
				.withCommonJobParameterBuilder()
				.crawlId(crawlId)
				.batchId(batchId)
				.numSlaves(3)
				.and()
				.withGenerateParameterBuilder()
				.addDays(35L)
				.forceFetch()
				.and()
				.withFetchParameterBuilder()
				.threads(50L)
				.timeLimitFetch(180L)
				.and()
				.withParseParameterBuilder()
				.skipRecords(1L)
				.startSkipping(2L)
				.and()
				.withUpdateDbParameterBuilder()
				.and()
				.build());
		// Because had not called addTimeStamp(), and batchId is the same, So success steps will not get executed again. 
		assertTrue("status should be compeleted", je1.getStatus() == BatchStatus.COMPLETED);
	}
}
