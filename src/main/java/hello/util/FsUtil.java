package hello.util;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.permission.FsPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;

@Component
public class FsUtil {

	@Value("${spring.hadoop.fsUri}")
	private String fsUri;
	
	@Autowired
	private FileSystem fs;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private org.apache.hadoop.conf.Configuration configuration;
	
	public FileSystem getOrCreate() throws IOException {
		try {
			fs.listFiles(new org.apache.hadoop.fs.Path("/"), false);
		} catch (Exception e) {
			fs = FileSystem.newInstance(configuration);
		}
		return fs;
	}
	
	public FileSystem getFs() {
		return fs;
	}


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
	
	///tmp/hadoop-yarn/staging/admin/.staging
	
	public void grantStagingPermission(String user) throws IOException {
		if (Strings.isNullOrEmpty(user)) {
			user = System.getProperty("user.name");
		}
		fs.mkdirs(new org.apache.hadoop.fs.Path("/tmp"), FsPermission.getDirDefault());
//		FsPermission fp = new FsPermission(FsAction.READ_WRITE, FsAction.READ_WRITE, FsAction.READ_WRITE);
//		fs.setPermission(new org.apache.hadoop.fs.Path("/tmp"), FsPermission.getDirDefault());
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
