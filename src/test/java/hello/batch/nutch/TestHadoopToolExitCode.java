package hello.batch.nutch;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;

import org.junit.Test;

public class TestHadoopToolExitCode {
	
	@Test
	public void t() {
		String s = "StepExecution: id=262, version=1,name=nutch-generate-step, status=FAILED, exitStatus=FAILED, readCount=0, filterCount=0,writeCount=0 readSkipCount=0, writeSkipCount=0, processSkipCount=0, commitCount=0,rollbackCount=1, exitDescription=java.io.IOException: Hadoop tool failed with exit code: 1";
		Matcher m = NutchTasklets.hadoopToolOutPattern.matcher(s);
		assertTrue(m.matches());
		assertThat(m.group(1), equalTo("1"));
	}

}
