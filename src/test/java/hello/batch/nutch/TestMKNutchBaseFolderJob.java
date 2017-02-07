package hello.batch.nutch;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobInstanceException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import hello.TbatchBase;
import hello.util.FsUtil;

public class TestMKNutchBaseFolderJob extends TbatchBase {

	@Value("${spring.nutchBaseFolder}")
	private String nutchBaseFolder;
	
	@Autowired
	private FsUtil fsUtil;
	
	public TestMKNutchBaseFolderJob() {
		super(NutchTaskNames.MK_NUTCH_BASE_FOLDER);
	}
	
	@Before
	public void b() throws IllegalArgumentException, IOException {
		fsUtil.getFs().delete(new Path(nutchBaseFolder), true);
	}
	
	@Test
	public void t() throws NoSuchJobException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, NoSuchJobInstanceException, InterruptedException, JobParametersNotFoundException, UnexpectedJobExecutionException, IOException {
		Job jb1 = jobRegistry.getJob(getJobName());
		JobExecution je1 = syncJobLauncher.run(jb1, withCurrentTime()); // add time parameter to keep out of running once.
		// run again
		syncJobLauncher.run(jb1, new JobParameters());
		assertTrue("status should be compeleted", je1.getStatus() == BatchStatus.COMPLETED);
		assertTrue("folder should be created.", fsUtil.getFs().exists(new Path(nutchBaseFolder)));
	}
}
