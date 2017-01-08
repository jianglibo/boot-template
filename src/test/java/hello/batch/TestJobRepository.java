package hello.batch;

import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;

import hello.Tbase;

public class TestJobRepository extends Tbase {
	
//	a JobRepository (bean name "jobRepository")
//	a JobLauncher (bean name "jobLauncher")
//	a JobRegistry (bean name "jobRegistry")
//	a PlatformTransactionManager (bean name "transactionManager")
//	a JobBuilderFactory (bean name "jobBuilders") as a convenience to prevent you from having to inject the job repository into every job, as in the examples above
//	a StepBuilderFactory (bean name "stepBuilders")
	
	@Autowired
	private JobRegistry jobRegistry;
	
	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private JobOperator jobOperator;
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Test
	public void jobCounts() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, NoSuchJobException {

		Collection<String> jns = jobOperator.getJobNames();
		jobLauncher.run(jobRegistry.getJob("importUserJob"), new JobParameters());
		jns = jobRegistry.getJobNames();	
		assertTrue(true);
	}

}
