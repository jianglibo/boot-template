package hello.util;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.hadoop.fs.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

public class HdpFolderUtil {

	private String baseFolder;
	

	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private org.apache.hadoop.conf.Configuration configuration;
	
	@Autowired
	private FileSystem fs;
	
	public HdpFolderUtil(String baseFolder) {
		super();
		this.baseFolder = baseFolder;
	}

	public Resource localFileAsResource(String localFile) {
		return applicationContext.getResource("file:///" + localFile);
	}
	
	public String getRemoteFileURIString(String localFile) {
		Path localJavaPath = Paths.get(localFile).normalize().toAbsolutePath();
		org.apache.hadoop.fs.Path remoteHadoopPath = new org.apache.hadoop.fs.Path(baseFolder, localJavaPath.getFileName().toString());
		return remoteHadoopPath.toString();
	}
	
	
	public void copyFromLocalFile(String localFile) throws IOException {
		Path localJavaPath = Paths.get(localFile).normalize().toAbsolutePath();
		org.apache.hadoop.fs.Path localHadoopPath = new org.apache.hadoop.fs.Path(localJavaPath.toString());
		org.apache.hadoop.fs.Path remoteHadoopPath = new org.apache.hadoop.fs.Path(baseFolder, localJavaPath.getFileName().toString());
		fs.copyFromLocalFile(localHadoopPath, remoteHadoopPath);
	}

	public String getBaseFolder() {
		return baseFolder;
	}

	public void setBaseFolder(String baseFolder) {
		this.baseFolder = baseFolder;
	}

}
