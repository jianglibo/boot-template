package hello.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.beust.jcommander.internal.Maps;

/**
 * For nutch jobs we have a convention, under the baseFolder there have a folder named seeds.
 * @author Administrator
 *
 */

@Component
public class NutchFolderUtil {

	@Value("${spring.nutch.baseFolder}")
	private String baseFolder;
	
	@Value("${spring.nutch.localBaseFolder}")
	private String localBaseFolder;
	
	@Value("${spring.hadoop.fsUri}")
	private String fsUri;

	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private org.apache.hadoop.conf.Configuration configuration;
	
	@Autowired
	private FileSystem fs;

	@Autowired
	private FsUtil fsUtil;
	
	/**
	 * web must read zip file, and get nutch-default.xml and nutch-site.xml content. 
	 * @param crawlId
	 * @return
	 * @throws IOException 
	 */
	public org.apache.hadoop.conf.Configuration getNutchConfiguration(String crawlId) throws IOException {
		org.apache.hadoop.conf.Configuration c = new org.apache.hadoop.conf.Configuration(configuration);
		c.addResource(getZipEntryInputstreamUnclosed(getNutchJobJar(crawlId), "nutch-default.xml"));
		c.addResource(getZipEntryInputstreamUnclosed(getNutchJobJar(crawlId), "nutch-site.xml"));
		return c;
	}
	
	public InputStream getZipEntryInputstreamUnclosed(String zipFile, String fn) throws IOException {
		ZipFile zf = new ZipFile(zipFile);
		Enumeration<? extends ZipEntry> entries =  zf.entries();
		InputStream is = null;
		while(entries.hasMoreElements()) {
			ZipEntry ze = entries.nextElement();
			if (!ze.isDirectory() && ze.getName().equals(fn)) {
				is = zf.getInputStream(ze);
			}
		}
		return is;
	}
	
	public String getNutchJobJar(String crawlId) throws IOException {
		java.nio.file.Path localPath = Paths.get(localBaseFolder, crawlId);
		return Files.list(localPath).filter(p -> p.getFileName().toString().matches(".*\\.job")).findAny().get().normalize().toAbsolutePath().toString();
	}
	
	public String getSeedDir(String crawlId) {
		return new Path(baseFolder, crawlId).toString();
	}
	
	public void deleteSeedDir(String crawlId) throws IllegalArgumentException, IOException {
		fsUtil.getFs().delete(new Path(getSeedDir(crawlId)), true);
	}

	public void copySeeds(String crawlId) throws IOException {
		Path rp = new Path(baseFolder, crawlId);
		fs.mkdirs(rp);
		java.nio.file.Path localPath = Paths.get(localBaseFolder, crawlId);
		List<File> files = Stream.of(localPath.toFile().listFiles()).filter(File::isFile).collect(Collectors.toList());
		for (File f : files) {
			String fn = f.getName();
			if (fn.matches(".*seed.*") && f.isFile()) {
				fsUtil.copyFromLocalFile(rp.toString(), f.getAbsolutePath());
			}
		}
	}
	
	
	public String getBaseFolder() {
		return baseFolder;
	}
	
	public void setBaseFolder(String baseFolder) {
		this.baseFolder = baseFolder;
	}

	public String getFsUri() {
		return fsUri;
	}
	
	public void setFsUri(String fsUri) {
		this.fsUri = fsUri;
	}
	
	
	public JobParametersBuilder newJobParametersBuilder() {
		return new JobParametersBuilder();
	}

	
	public class JobParametersBuilder {
		private JobParameter crawlId;
		
		public JobParametersBuilder crawlId(String crawlId) {
			this.crawlId = new JobParameter(crawlId);
			return this;
		}
		
		public JobParameters build() {
			Map<String, JobParameter> jpmap = Maps.newHashMap();
			jpmap.put("crawlId", crawlId);
			jpmap.put("startTime", new JobParameter(Date.from(Instant.now()))); // distinguish jobs.
			return new JobParameters(jpmap);
		}
	}
}
