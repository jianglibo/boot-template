package hello.batch.nutch;

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
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
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
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.beust.jcommander.internal.Maps;

import hello.util.FsUtil;

/**
 * org.apache.hadoop.util.RunJar create a custom classLoader to handle.
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
	
	private static final ConcurrentMap<String, String> remoteUploads = new ConcurrentHashMap<>();
	
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
		return configuration;
//		org.apache.hadoop.conf.Configuration c = new org.apache.hadoop.conf.Configuration(configuration);
		// when in classloader phrase, these item should be used.
//	    c.addResource("nutch-default.xml");
//	    c.addResource("nutch-site.xml");
	    // boot side.
//		c.addResource(getZipEntryInputstreamUnclosed(getNutchJobJar(crawlId), "nutch-default.xml"));
//		c.addResource(getZipEntryInputstreamUnclosed(getNutchJobJar(crawlId), "nutch-site.xml"));
//		return c;
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
	
	public Resource[] getLibs() throws IOException {
		String libs = Paths.get(localBaseFolder, "libs").normalize().toAbsolutePath().toString().replaceAll("\\\\", "/");
		if (!libs.endsWith("/")) {
			libs = libs + "/";
		}
		libs = libs + "*.jar";
		return applicationContext.getResources("file:///" + libs);
	}

	public Resource[] getLibsWithJobSelf(String crawlId) throws IOException {
		Resource[] libsr = getLibs();
		Resource[] libsr1 = new Resource[libsr.length + 1];
		for(int i=0; i< libsr.length; i++) {
			libsr1[i] = libsr[i];
		}
		libsr1[libsr.length] = getJobArchive(crawlId);
		return libsr1; 
	}
	
	public Resource getJobArchive(String crawlId) throws IOException {
		return applicationContext.getResource("file:///" + getNutchJobJar(crawlId).replaceAll("\\\\", "/"));
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
	
	public String copyJobJar(String crawlId) throws IOException {
		if (remoteUploads.containsKey(crawlId)) {
			return remoteUploads.get(crawlId);
		}
		Path rp = new Path(baseFolder, crawlId);
		fs.mkdirs(rp);
		java.nio.file.Path localPath = Paths.get(localBaseFolder, crawlId);
		File file = Stream.of(localPath.toFile().listFiles()).filter(File::isFile).filter(f-> f.getName().matches(".*\\.job$")).findAny().get();
		fsUtil.copyFromLocalFile(rp.toString(), file.getAbsolutePath());
		String s = fsUtil.convertToFullUserPath(fsUtil.getRemoteFileURIString(rp.toString(),file.getName()));
		remoteUploads.put(crawlId, s);
		return s;
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
	
	public String getRandomBatchId() {
		return Instant.now().getEpochSecond() + "-" + new Random().nextInt(100000);
	}
	
	public CrawJobParameterBuilder newCrawJobParameterBuilder() {
		return new CrawJobParameterBuilder();
	}
	
	
	public class CrawJobParameterBuilder {
		Map<String, JobParameter> jpmap = Maps.newHashMap();
		
		public CommonJobParameterBuilder withCommonJobParameterBuilder() {
			return new CommonJobParameterBuilder(this);
		}
		
		public GenerateJobParametersBuilder withGenerateParameterBuilder() {
			return new GenerateJobParametersBuilder(this);
		}
		
		public FetchJobParametersBuilder withFetchParameterBuilder() {
			return new FetchJobParametersBuilder(this);
		}
		
		public ParseJobParametersBuilder withParseParameterBuilder() {
			return new ParseJobParametersBuilder(this);
		}
		
		
		public UpdateDbJobParametersBuilder withUpdateDbParameterBuilder() {
			return new UpdateDbJobParametersBuilder(this);
		}
		
		protected void addParameter(String key, JobParameter jp) {
			jpmap.put(key, jp);
		}
		
		public CrawJobParameterBuilder addTimeStamp() {
			jpmap.put("startTime", new JobParameter(Date.from(Instant.now()))); // distinguish jobs.
			return this;
		}
		
		public JobParameters build() {
			if (!jpmap.containsKey("crawlId")) {
				throw new RuntimeException("crawlId jobparameter is required!");
			}
//			if (!jpmap.containsKey("batchId")) {
//				throw new RuntimeException("batchId jobparameter is required!");
//			}
//			if (!jpmap.containsKey("numberSlaves")) {
//				throw new RuntimeException("numberSlaves jobparameter is required!");
//			}
			return new JobParameters(jpmap);
		}
	}
	
	public class CommonJobParameterBuilder {
		private CrawJobParameterBuilder combinedBuilder;
		
		public CommonJobParameterBuilder(CrawJobParameterBuilder combinedBuilder) {
			this.combinedBuilder = combinedBuilder;
			this.combinedBuilder.addParameter("debug", new JobParameter(0L));
		}
		
		public CrawJobParameterBuilder and() {
			return combinedBuilder;
		}
		
		public CommonJobParameterBuilder remoteJobJar(String jobJarUrl) {
			combinedBuilder.addParameter("jobJarUrl", new JobParameter(jobJarUrl));
			return this;
		}

		
		public CommonJobParameterBuilder crawlId(String crawlId) {
			combinedBuilder.addParameter("crawlId", new JobParameter(crawlId));
			return this;
		}
		
		public CommonJobParameterBuilder batchId(String batchId) {
			combinedBuilder.addParameter("batchId", new JobParameter(batchId));
			return this;
		}
		
		public CommonJobParameterBuilder debug() {
			combinedBuilder.addParameter("debug", new JobParameter(1L));
			return this;
		}
		
		public CommonJobParameterBuilder numSlaves(long numSlaves) {
			combinedBuilder.addParameter("numSlaves", new JobParameter(numSlaves));
			return this;
		}
	}

	public class GenerateJobParametersBuilder {
		private CrawJobParameterBuilder combinedBuilder;
		
		public GenerateJobParametersBuilder(CrawJobParameterBuilder combinedBuilder) {
			this.combinedBuilder = combinedBuilder;
			this.combinedBuilder.addParameter(NutchTasklets.Constants.FORCE_FETCH, new JobParameter(0L));
			this.combinedBuilder.addParameter("addDays", new JobParameter(0L));
		}
		
		public CrawJobParameterBuilder and() {
			return combinedBuilder;
		}

		public GenerateJobParametersBuilder addDays(long addDays) {
			this.combinedBuilder.addParameter("addDays", new JobParameter(addDays));
			return this;
		}
		
		public GenerateJobParametersBuilder forceFetch() {
			this.combinedBuilder.addParameter(NutchTasklets.Constants.FORCE_FETCH, new JobParameter(1L));
			return this;
		}
	}
	

	public class UpdateDbJobParametersBuilder {
		private CrawJobParameterBuilder combinedBuilder;
		
		public UpdateDbJobParametersBuilder(CrawJobParameterBuilder combinedBuilder) {
			this.combinedBuilder = combinedBuilder;
		}
		
		public CrawJobParameterBuilder and() {
			return combinedBuilder;
		}
	}
	
	
	public class ParseJobParametersBuilder {
		private CrawJobParameterBuilder combinedBuilder;
		
		public ParseJobParametersBuilder(CrawJobParameterBuilder combinedBuilder) {
			this.combinedBuilder = combinedBuilder;
			this.combinedBuilder.addParameter("startSkipping", new JobParameter(2L));
			this.combinedBuilder.addParameter("skipRecords", new JobParameter(1L));
		}
		
		public CrawJobParameterBuilder and() {
			return combinedBuilder;
		}

		public ParseJobParametersBuilder startSkipping(long startSkipping) {
			this.combinedBuilder.addParameter("startSkipping", new JobParameter(startSkipping));
			return this;
		}
		

		public ParseJobParametersBuilder skipRecords(long skipRecords) {
			this.combinedBuilder.addParameter("skipRecords", new JobParameter(skipRecords));
			return this;
		}
	}
	
	public class FetchJobParametersBuilder {
		private CrawJobParameterBuilder combinedBuilder;
		
		public FetchJobParametersBuilder(CrawJobParameterBuilder combinedBuilder) {
			this.combinedBuilder = combinedBuilder;
			this.combinedBuilder.addParameter("timeLimitFetch", new JobParameter(180L));
			this.combinedBuilder.addParameter("threads", new JobParameter(50L));
		}
		
		public CrawJobParameterBuilder and() {
			return combinedBuilder;
		}
		
		public FetchJobParametersBuilder timeLimitFetch(long timeLimitFetch) {
			this.combinedBuilder.addParameter("timeLimitFetch", new JobParameter(timeLimitFetch));
			return this;
		}
		
		public FetchJobParametersBuilder threads(long threads) {
			this.combinedBuilder.addParameter("threads", new JobParameter(threads));
			return this;
		}
	}
}
