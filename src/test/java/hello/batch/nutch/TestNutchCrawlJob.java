package hello.batch.nutch;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
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

public class TestNutchCrawlJob extends TbatchBase {
	
	public static Path logDir =  new Path("yarnlog");
	
	@Autowired
	private NutchFolderUtil nutchFolderUtil;
	
	private String crawlId = "fhgov";
	
	private String batchId = "1486710034-46875";
	
	public TestNutchCrawlJob() {
		super(NutchTaskNames.NUTCH_CRAWL);
	}
	
	@Before
	public void b() throws IOException {
		
		FileSystem fs = FileSystem.get(hadoopConfiguration);
		RemoteIterator<LocatedFileStatus> ril = fs.listFiles(logDir, true);
		
		while(ril.hasNext()) {
			fs.delete(ril.next().getPath(),true);
		}
		
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
		Job injectJob = jobRegistry.getJob(NutchTaskNames.NUTCH_INJECT);
		JobExecution je = syncJobLauncher.run(injectJob, 
				nutchFolderUtil.newCrawJobParameterBuilder()
				.withCommonJobParameterBuilder()
				.crawlId(crawlId)
				.remoteJobJar(nutchFolderUtil.copyJobJar(crawlId))
				.and()
				.addTimeStamp()
				.build());
		int maxRetries = 3;
		int retries = 0;
		JobExecution je1;
		while(true) {
			je1 = syncJobLauncher.run(jb1, nutchFolderUtil
					.newCrawJobParameterBuilder()
					.withCommonJobParameterBuilder()
	//				.debug()
					.crawlId(crawlId)
					.batchId(batchId)
					.remoteJobJar(nutchFolderUtil.copyJobJar(crawlId))
					.numSlaves(3)
					.and()
					.withGenerateParameterBuilder()
	//				.addDays(35L)
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
					.addTimeStamp()
					.build());
			if (je1.getStatus() == BatchStatus.COMPLETED) { // generate stop may failed. this is job's success, not step's success.
				if (je1.getExecutionContext().containsKey(NutchTasklets.Constants.GENERATE_NO_NEW_ITEM)) { // no new item. break loop.
					break;
				} else { // other reasons cause step failed.
					if (je1.getExecutionContext().containsKey(NutchTasklets.Constants.GENERATE_OK)) { // has new item and has completed successfully.
						noOp();
					} else {
						retries++;
					}
				}
			} else { // failed in other steps. continue
				retries++;
			}
			
			if (retries > maxRetries) {
				break;
			}
		}
		// Because had not called addTimeStamp(), and batchId is the same, So success steps will not get executed again. 
		assertTrue("status should be compeleted", je1.getStatus() == BatchStatus.COMPLETED);
	}
	
	private void noOp() {
	}
}
