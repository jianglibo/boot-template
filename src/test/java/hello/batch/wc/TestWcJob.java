package hello.batch.wc;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.After;
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

import hello.TbatchBase;
import hello.hadoopwc.mrcode.WordCount2;
import hello.util.FsUtil;
import hello.util.WordCountFolderUtil;

public class TestWcJob extends TbatchBase {
	
	@Autowired
	private FsUtil fsUtil;
	
	@Autowired
	private WordCountFolderUtil wordCountFolderUtil;
	
	public TestWcJob() {
		super(WcJob.JOB_NAME);
	}
	
	
	private java.nio.file.Path localWcTxt;
	
	private java.nio.file.Path localWcJar;

	
	@Before
	public void b() throws IOException {
		wordCountFolderUtil.clearFolder();
		localWcTxt = Paths.get(BATCH_FIXTURE_BASE, "wc.txt");
		if (!Files.exists(localWcTxt)) {
			throw new RuntimeException("Please invoke command in project root folder, like this: '. .\\randomword.ps1 -DstFile .\\src\\test\\resources\\fixtnotingit\\wc.txt -UniqueWordNumber 1000 -TotalWordNumber 1000000 -MaxWordsPerLine 23'");
		}
		localWcJar = Paths.get("mrjars", "wc.jar");
		if (!Files.exists(localWcJar)) {
			throw new RuntimeException(localWcJar.toAbsolutePath().normalize().toString() + " doesn't exists. Pleas follow the README.md in project root to build jar first.");
		}
		wordCountFolderUtil.initFolder();
	}
	
	@After
	public void a() throws IOException {
		wordCountFolderUtil.clearFolder();
	}
	
	@Test
	public void t() throws NoSuchJobException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, NoSuchJobInstanceException, InterruptedException, JobParametersNotFoundException, UnexpectedJobExecutionException, IOException {
		Job jb1 = jobRegistry.getJob(getJobName());
		JobParameters jps = wordCountFolderUtil.newJobParametersBuilder().jar(localWcJar.toAbsolutePath().normalize().toString()).localFolder(localWcTxt.toAbsolutePath().normalize().toString()).mainClass(WordCount2.class.getName()).build();
		JobExecution je1 = syncJobLauncher.run(jb1, jps);
		assertTrue("status should be compeleted", je1.getStatus() == BatchStatus.COMPLETED);
	}

}
