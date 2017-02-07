package hello.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.beust.jcommander.internal.Maps;
import com.google.common.io.ByteStreams;

/**
 * For wc jobs we have a convention, under the baseFolder there have two folders named in and out.
 * @author Administrator
 *
 */

@Component
public class WordCountFolderUtil {

	private final String baseFolder = "wc";
	
	@Value("${spring.hadoop.fsUri}")
	private String fsUri;
	
	@Autowired
	private FsUtil fsUtil;

	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private org.apache.hadoop.conf.Configuration configuration;
	
	// because of filesystem Cache, fs and secondFs are actually same.
//	@Autowired
//	private FileSystem fs;
	
//	@Autowired
//	@Qualifier("secondFs")
//	private FileSystem secondFs;

	
	@Value("${myapp.mapredout}")
	private String mapredout;
	
	/**
	 * Before hadoop job starts, output folder must not exists. 
	 * @throws IOException
	 */
	public void initFolder() throws IOException {
		fsUtil.getFs().mkdirs(new Path(baseFolder, "in"));
//		fs.mkdirs(new Path(baseFolder, "out"));
	}

	public void clearFolder() throws IOException {
		fsUtil.getFs().delete(new Path(baseFolder, "in"), true);
		fsUtil.getFs().delete(new Path(baseFolder, "out"), true);
	}
	
	public void copyFilesToWc(String localFile) throws IOException {
		java.nio.file.Path localPath = Paths.get(localFile);
		if (Files.isDirectory(localPath)) {
			List<File> files = Stream.of(localPath.toFile().listFiles()).filter(File::isFile).collect(Collectors.toList());
			for (File f : files) {
				fsUtil.copyFromLocalFile("wc/in", f.getAbsolutePath());
			}
		} else {
			fsUtil.copyFromLocalFile("wc/in", localFile);
		}
	}
	
	public void copyOutToLocal() throws FileNotFoundException, IllegalArgumentException, IOException {
		if (fsUtil.getOrCreate().exists(new Path("wc/out"))) {
			RemoteIterator<LocatedFileStatus> ri = fsUtil.getOrCreate().listFiles(new Path("wc/out"), false);
			while(ri.hasNext()) {
				LocatedFileStatus lfs = ri.next();
				if (lfs.getPath().getName().matches("^part-r-.*")) {
					FSDataInputStream fsis = fsUtil.getFs().open(lfs.getPath());
					java.nio.file.Path path = Paths.get(mapredout);
					if (!Files.exists(path)) {
						Files.createDirectories(path);
					}
					Date d = Date.from(Instant.now());
					File out = path.resolve(new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss").format(d) + ".txt").toFile();
					ByteStreams.copy(fsis, new FileOutputStream(out));
					fsis.close();
				}
			}
		}
	}

	public String getBaseFolder() {
		return baseFolder;
	}
	
	public JobParametersBuilder newJobParametersBuilder() {
		return new JobParametersBuilder();
	}
	
	
	public class JobParametersBuilder {
		private JobParameter mainClass;
		private JobParameter localFolder;
		private JobParameter jar;
		
		public JobParametersBuilder mainClass(String mainClass) {
			this.mainClass = new JobParameter(mainClass);
			return this;
		}
		
		public JobParametersBuilder localFolder(String localFolder) {
			this.localFolder = new JobParameter(localFolder);
			return this;
		}
		
		public JobParametersBuilder jar(String jar) {
			this.jar = new JobParameter(jar);
			return this;
		}
		
		public JobParameters build() {
			Map<String, JobParameter> jpmap = Maps.newHashMap();
			jpmap.put("localFolder", localFolder);
			jpmap.put("jar", jar);
			jpmap.put("inputFolder", new JobParameter(fsUtil.convertToFullUserPath("wc/in")));
			jpmap.put("outputFolder", new JobParameter(fsUtil.convertToFullUserPath("wc/out")));
			jpmap.put("mainClass", mainClass);
			jpmap.put("startTime", new JobParameter(Date.from(Instant.now()))); // distinguish jobs.
			return new JobParameters(jpmap);
		}

	}

}
