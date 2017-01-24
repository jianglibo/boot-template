package hello.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	
	@Autowired
	private FileSystem fs;
	
	public void initFolder() throws IOException {
		fs.mkdirs(new Path(baseFolder, "in"));
		fs.mkdirs(new Path(baseFolder, "out"));
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
			jpmap.put("inputFolder", new JobParameter("wc/in"));
			jpmap.put("outputFolder", new JobParameter("wc/out"));
			jpmap.put("mainClass", mainClass);
			return new JobParameters(jpmap);
		}

	}

}
