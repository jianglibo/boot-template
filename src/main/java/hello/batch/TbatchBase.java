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
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.google.common.base.Strings;

import hello.Tbase;


public class TbatchBase extends Tbase {
	
	public static String BATCH_FIXTURE_BASE = "./src/test/resources/fixtnotingit/";
	
	public static Logger LOGGER = LoggerFactory.getLogger(TbatchBase.class);

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
	
	@Autowired
	protected ApplicationContext applicationContext;
	
	private String jobName;
	
	public TbatchBase(String jobName) {
		this.jobName = jobName;
	}
	
	private static String[] batchDbnames = {"STEP_EXECUTION_CONTEXT", "STEP_EXECUTION", "JOB_EXECUTION_CONTEXT", "JOB_EXECUTION_PARAMS", "JOB_EXECUTION", "JOB_INSTANCE", };
	
	public void clearBatchDb() {
		Stream.of(batchDbnames).map(bn -> "BATCH_" + bn).forEach(bn -> {
			jdbcTemplate.update("delete from " + bn);
		});
	}
	
	public int getCommitCount(JobExecution je) {
		return je.getStepExecutions().iterator().next().getCommitCount();
	}
	
	public int getWriteCount(JobExecution je) {
		return je.getStepExecutions().iterator().next().getWriteCount();
	}

	public int getSkipCount(JobExecution je) {
		return je.getStepExecutions().iterator().next().getSkipCount();
	}

	
	protected void bassertExitCode(JobExecution je, ExitStatus exitStatus) {
		assertThat("job exit code should be " + exitStatus, je.getExitStatus(), equalTo(exitStatus));
	}
	
	protected int countCurrentJobInstanceNumber() {
		return jobExplorer.getJobInstances(jobName, 0, 10000).size();
	}
	
	protected void bassertJobInstanceNumber(int num) {
		assertThat("job instance number should be " + num, countCurrentJobInstanceNumber(), equalTo(num));
	}
	
	protected int countCurrentJobExecNumber() {
		List<JobInstance> jins = jobExplorer.getJobInstances(jobName, 0, 10000);
		return jins.stream().map(j -> jobExplorer.getJobExecutions(j).size()).reduce(0, Integer::sum);
	}

	protected int countCurrentStepExecNumber() {
		List<JobInstance> jins = jobExplorer.getJobInstances(jobName, 0, 10000);
		return jins.stream().flatMap(j -> jobExplorer.getJobExecutions(j).stream()).map(je -> je.getStepExecutions().size()).reduce(0, Integer::sum);
	}
	
	protected void bassertStepExecutionNumber(int num) {
		assertThat("step execute instance number should be " + num, countCurrentStepExecNumber(), equalTo(num));
	}
	
	protected int countCurrentItemNumberInDb() {
		return countCurrentItemNumberInDb("people1");
	}
	
	protected int countCurrentItemNumberInDb(String tableName) {
		if (Strings.isNullOrEmpty(tableName)) {
			tableName = "people1";
		}
		return jdbcTemplate.queryForObject("select count(*) from " + tableName, Integer.class);
	}
	
	protected void bassertCountTable(String tableName, int expectedCount) {
		if (Strings.isNullOrEmpty(tableName)) {
			tableName = "people1";
		}
		assertThat("count in talbe " + tableName, countCurrentItemNumberInDb(tableName), equalTo(expectedCount));
	}


	protected void bassertJobExecNumber(int num) {
		assertThat("execute instance number should be " + num, countCurrentJobExecNumber(), equalTo(num));
	}
	
	protected void clearDb(String tableName) {
		if (Strings.isNullOrEmpty(tableName)) {
			tableName = "people1";
		}
		int affected = jdbcTemplate.update("delete from " + tableName);
		LOGGER.info("delete {} from {}", affected, tableName);
	}
	
	protected Path getFixturePath() {
		Path pp = Paths.get(BATCH_FIXTURE_BASE);
		if (!Files.exists(pp)) {
			pp.toFile().mkdirs();
		}
		return pp.resolve(jobName + ".csv");
	}
	
	protected String getFixtureResouceName() {
		return "file:///" + getFixturePath().toAbsolutePath().normalize().toString().replaceAll("\\\\", "/");
	}

	
	protected void setupFixtures(int itemNumbers) throws NoSuchJobException, IOException {
		Path datap = getFixturePath();
		PrintWriter pw = new PrintWriter(datap.toFile());
		IntStream.range(0, itemNumbers).mapToObj(i -> UUID.randomUUID().toString() + "," + UUID.randomUUID().toString()).forEach(s -> pw.println(s));
		pw.flush();
		pw.close();
		
//		Path instancCountp = pp.resolve(jobName + "InstanceCount.txt");
//		List<Long> jins = jobOperator.getJobInstances(MissingInputBatchConfiguration.JOB_NAME, 0, itemNumbers);
//		int c = jins.size();
//		Files.write(instancCountp, Lists.newArrayList(String.valueOf(c)));
//		
//		Path itemCountp = pp.resolve(jobName + "ItemCount.txt");
//		int i = jdbcTemplate.queryForObject("select count(*) from people1", Integer.class);
//		Files.write(itemCountp, Lists.newArrayList(String.valueOf(i)));
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
}
