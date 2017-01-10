package hello.batch.failedjobs.inputmissing;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobInstanceException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.test.context.jdbc.Sql;

import hello.batch.TbatchBase;

@Sql({"classpath:schema1-all.sql"})
public class TestMissingInputJob extends TbatchBase {
	
	@Before
	public void b() throws NoSuchJobException, IOException {
		setupFixtures(MissingInputBatchConfiguration.JOB_NAME, 1000);
	}
	
	@Test
	public void t() throws NoSuchJobException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, NoSuchJobInstanceException {
		
		Job jb = jobRegistry.getJob(MissingInputBatchConfiguration.JOB_NAME);
		JobExecution je = jobLauncher.run(jb, new JobParameters());
		
		assertInstanceNumber(MissingInputBatchConfiguration.JOB_NAME, 1);
		assertThat(je.getStatus(), equalTo(BatchStatus.FAILED));
		
		// run failed job again.
		int currentJobExecNumber = getCurrentJobExecNumber(MissingInputBatchConfiguration.JOB_NAME);
		int currentStepExecNumber = getCurrentStepExecNumber(MissingInputBatchConfiguration.JOB_NAME);
		jobLauncher.run(jb, new JobParameters());
		
		// If launch job for same parameters, no new job instance should be created.
		assertInstanceNumber(MissingInputBatchConfiguration.JOB_NAME, 1);
		
		// Every execution of job should create a new execution instance.
		assertJobExecNumber(MissingInputBatchConfiguration.JOB_NAME, currentJobExecNumber + 1);
		
		assertStepExecutionNumber(MissingInputBatchConfiguration.JOB_NAME, currentStepExecNumber + 1);
		
	}

}
