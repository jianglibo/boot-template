package hello.batch.wc;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
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

import com.beust.jcommander.internal.Maps;

import hello.TbatchBase;

public class TestWcJob extends TbatchBase {
	
	@Autowired
	private FileSystem fs;
	
	@Value("${spring.nutchSeeds}")
	private String nutchSeedsFolder;
	
	public TestWcJob() {
		super(WcJob.JOB_NAME);
	}
	
	private Path remotePath;
	private java.nio.file.Path localWcTxt;
	
	@Before
	public void b() throws IOException {
		localWcTxt = Paths.get(BATCH_FIXTURE_BASE, "wc.txt");
		if (!Files.exists(localWcTxt)) {
			throw new RuntimeException("Please invoke command in project root folder, like this: '. .\\randomword.ps1 -DstFile .\\src\test\\resources\\fixtnotingit\\wc.txt -UniqueWordNumber 1000 -TotalWordNumber 1000000 -MaxWordsPerLine 23'");
		}
		remotePath = new Path(nutchSeedsFolder,"wc.txt");
		if (fs.exists(remotePath)) {
			fs.delete(remotePath, true);
		}
	}
	
	@Test
	public void t() throws NoSuchJobException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, NoSuchJobInstanceException, InterruptedException, JobParametersNotFoundException, UnexpectedJobExecutionException, IOException {
		Job jb1 = jobRegistry.getJob(getJobName());
		JobParameter jp = new JobParameter(localWcTxt.normalize().toAbsolutePath().toString());
		Map<String, JobParameter> jpmap = Maps.newHashMap();
		jpmap.put(WcJob.PARAM_FILE_TO_WC, jp);
		JobParameters jps = new JobParameters(jpmap);
		JobExecution je1 = syncJobLauncher.run(jb1, jps);
		assertTrue("status should be compeleted", je1.getStatus() == BatchStatus.COMPLETED);
		assertTrue(fs.exists(remotePath));
	}

}
