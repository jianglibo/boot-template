package hello.util;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.google.common.io.ByteStreams;

@Component
public class FsUtil {

	@Value("${spring.hadoop.fsUri}")
	private String fsUri;
	
	@Autowired
	private FileSystem fs;
	
	@Autowired
	private ApplicationContext applicationContext;


	public String getFileContent(org.apache.hadoop.fs.Path hdpath) throws IOException {
		FSDataInputStream dis = fs.open(hdpath);
		String c = new String(ByteStreams.toByteArray(dis));
		dis.close();
		return c;
	}
	
	public String getFileContent(String hdpath) throws IOException {
		return getFileContent(new org.apache.hadoop.fs.Path(hdpath));
	}
	
	public org.apache.hadoop.fs.Path convertToUserPath(String p) {
		if (p.startsWith("/")) {
			p = p.substring(1);
		}
		String s = String.format("/user/%s/%s", System.getProperty("user.name"), p);
		return new org.apache.hadoop.fs.Path(s);
	}
	
	public String convertToFullUserPath(String string) {
		String s = convertToUserPath(string).toString();
		if (!s.startsWith("hdfs://")) {
			s = fsUri + s;
		}
		return s;
	}
	
	public Resource localFileAsResource(String localFile) {
		Path localPath = Paths.get(localFile);
		return localFileAsResource(localPath);
	}
	
	public Resource localFileAsResource(Path localPath) {
		return applicationContext.getResource("file:///" + localPath.toAbsolutePath().normalize().toString());
	}
	
	public String getRemoteFileURIString(String baseFolder, String localFile) {
		Path localJavaPath = Paths.get(localFile).normalize().toAbsolutePath();
		org.apache.hadoop.fs.Path remoteHadoopPath = new org.apache.hadoop.fs.Path(baseFolder, localJavaPath.getFileName().toString());
		return remoteHadoopPath.toString();
	}
	
	/**
	 * 
	 * @param baseFolder the remote folder to copy to.
	 * @param localFile localFile
	 * @throws IOException
	 */
	public void copyFromLocalFile(String baseFolder, String localFile) throws IOException {
		Path localJavaPath = Paths.get(localFile).normalize().toAbsolutePath();
		org.apache.hadoop.fs.Path localHadoopPath = new org.apache.hadoop.fs.Path(localJavaPath.toString());
		org.apache.hadoop.fs.Path remoteHadoopPath = new org.apache.hadoop.fs.Path(baseFolder, localJavaPath.getFileName().toString());
		fs.copyFromLocalFile(localHadoopPath, remoteHadoopPath);
	}




}
