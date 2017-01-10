package hello.sql;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobInstanceException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import com.google.common.collect.Lists;

import hello.Tbase;
import hello.batch.BatchConfiguration;

@Sql({"classpath:schema1-all.sql"})
public class TestSqlInit extends Tbase {
	
	@Autowired
	private DataSource primayDataSource;
	
	@Autowired
	private JobRegistry jobRegistry;
	
	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private JobOperator jobOperator;
	
	@Autowired
	private JobExplorer jobExplorer;
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Before
	public void b() throws NoSuchJobException, IOException {
		Path pp = Paths.get("src/test/resources/fixtnotingit");
		if (!Files.exists(pp)) {
			pp.toFile().mkdirs();
		}
		Path datap = pp.resolve("sample-data.csv");
		
		if (!Files.exists(datap)) {
			PrintWriter pw = new PrintWriter(datap.toFile());
			IntStream.range(1, 1000).mapToObj(i -> UUID.randomUUID().toString() + "," + UUID.randomUUID().toString()).forEach(s -> pw.println(s));
			pw.flush();
			pw.close();
		}
		
		Path instancCountp = pp.resolve("instanceCount.txt");
		List<Long> jins = jobOperator.getJobInstances(BatchConfiguration.IMPORT_USER_JOB, 0, 10000);
		int c = jins.size();
		Files.write(instancCountp, Lists.newArrayList(String.valueOf(c)));
		
		Path itemCountp = pp.resolve("itemCount.txt");
		int i = jdbcTemplate.queryForObject("select count(*) from people1", Integer.class);
		Files.write(itemCountp, Lists.newArrayList(String.valueOf(i)));
	}
	
	@Test
	public void t() throws NoSuchJobException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, NoSuchJobInstanceException {
		List<JobInstance> jins = jobExplorer.getJobInstances(BatchConfiguration.IMPORT_USER_JOB, 0, 10000);
		if (jins.size() > 0) {
			List<JobExecution> jes = jobExplorer.getJobExecutions(jins.get(0));
			jes.stream().forEach(je -> System.out.println(je));
		}
		Job jb = jobRegistry.getJob(BatchConfiguration.IMPORT_USER_JOB);
		JobExecution je = jobLauncher.run(jb, new JobParameters());
		assertThat(je.getStatus(), equalTo(BatchStatus.COMPLETED));
	}
}
