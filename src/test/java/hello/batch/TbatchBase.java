package hello.batch;

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

import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.google.common.collect.Lists;

import hello.Tbase;
import hello.batch.failedjobs.inputmissing.MissingInputBatchConfiguration;

public class TbatchBase extends Tbase {

	@Autowired
	protected DataSource primayDataSource;
	
	@Autowired
	protected JobRegistry jobRegistry;
	
	@Autowired
	protected JobRepository jobRepository;
	
	@Autowired
	protected JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	protected JobOperator jobOperator;
	
	@Autowired
	protected JobExplorer jobExplorer;
	
	@Autowired
	protected JobLauncher jobLauncher;
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	
	protected void assertInstanceNumber(String jobName,  int num) {
		List<JobInstance> jins = jobExplorer.getJobInstances(jobName, 0, 10000);
		assertThat("job instance number should be " + num, num, equalTo(jins.size()));
	}
	
	protected int getCurrentJobExecNumber(String jobName) {
		List<JobInstance> jins = jobExplorer.getJobInstances(jobName, 0, 10000);
		return jins.stream().map(j -> jobExplorer.getJobExecutions(j).size()).reduce(0, Integer::sum);
	}

	protected int getCurrentStepExecNumber(String jobName) {
		List<JobInstance> jins = jobExplorer.getJobInstances(jobName, 0, 10000);
		return jins.stream().flatMap(j -> jobExplorer.getJobExecutions(j).stream()).map(je -> je.getStepExecutions().size()).reduce(0, Integer::sum);
	}
	
	protected void assertStepExecutionNumber(String jobName,  int num) {
		assertThat("step execute instance number should be " + num, num, equalTo(getCurrentStepExecNumber(jobName)));
	}


	protected void assertJobExecNumber(String jobName,  int num) {
		assertThat("execute instance number should be " + num, num, equalTo(getCurrentJobExecNumber(jobName)));
	}
	
	protected void setupFixtures(String jobName, int itemNumbers) throws NoSuchJobException, IOException {
		Path pp = Paths.get("src/test/resources/fixtnotingit");
		if (!Files.exists(pp)) {
			pp.toFile().mkdirs();
		}
		Path datap = pp.resolve(jobName + ".csv");
		
		if (!Files.exists(datap)) {
			PrintWriter pw = new PrintWriter(datap.toFile());
			IntStream.range(1, 1000).mapToObj(i -> UUID.randomUUID().toString() + "," + UUID.randomUUID().toString()).forEach(s -> pw.println(s));
			pw.flush();
			pw.close();
		}
		
		Path instancCountp = pp.resolve(jobName + "InstanceCount.txt");
		List<Long> jins = jobOperator.getJobInstances(MissingInputBatchConfiguration.JOB_NAME, 0, itemNumbers);
		int c = jins.size();
		Files.write(instancCountp, Lists.newArrayList(String.valueOf(c)));
		
		Path itemCountp = pp.resolve(jobName + "ItemCount.txt");
		int i = jdbcTemplate.queryForObject("select count(*) from people1", Integer.class);
		Files.write(itemCountp, Lists.newArrayList(String.valueOf(i)));
	}
	
}
