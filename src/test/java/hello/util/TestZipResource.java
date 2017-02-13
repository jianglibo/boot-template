package hello.util;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipException;

import org.junit.Test;

import hello.batch.nutch.NutchFolderUtil;

public class TestZipResource {
	
	@Test
	public void t() throws ZipException, IOException {
		NutchFolderUtil nf = new NutchFolderUtil();
		Path p = Paths.get("jobproperties", "fhgov", "apache-nutch-2.3.1.job");
		assertNotNull(nf.getZipEntryInputstreamUnclosed(p.toAbsolutePath().toString(),"nutch-site.xml"));
	}

}
