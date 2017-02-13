package hello.batch.learning;

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

import hello.TbatchBase;

public class TestLearningBatchJob extends TbatchBase {
	
	public TestLearningBatchJob() {
		super(TaskletForLearning.JOB_NAME);
	}
	
	@Test
	public void t() throws NoSuchJobException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, NoSuchJobInstanceException, InterruptedException, JobParametersNotFoundException, UnexpectedJobExecutionException, IOException {
		Job jb1 = jobRegistry.getJob(getJobName());
		JobExecution je1 = syncJobLauncher.run(jb1,startJobParameters().withCurrentTime().withKeyValue("forceFetch", 1L).buildJobParameters());
		assertTrue("status should be compeleted", je1.getStatus() == BatchStatus.COMPLETED);
	}

}
