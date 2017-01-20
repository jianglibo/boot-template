package hello.source;

import java.io.IOException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import hello.Tbase;

public class TestDisiMonitor extends Tbase {
	
	@Autowired
	private DiskMonitor dm;
	
	@Test
	public void t() throws IOException, InterruptedException {
		dm.startRecursiveWatcher();
		Thread.sleep(1000* 60 * 10);
	}

}
