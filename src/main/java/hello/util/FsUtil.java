package hello.util;

import java.io.IOException;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.io.ByteStreams;

@Component
public class FsUtil {

	@Value("${spring.hadoop.fsUri}")
	private String fsUri;
	
	private FileSystem fs;

	public String getFileContent(Path hdpath) throws IOException {
		FSDataInputStream dis = fs.open(hdpath);
		String c = new String(ByteStreams.toByteArray(dis));
		dis.close();
		return c;

	}
	
	public String getFileContent(String p) throws IOException {
		return getFileContent(convertToUserPath(p));
	}
	
	public org.apache.hadoop.fs.Path convertToUserPath(String p) {
		if (p.startsWith("/")) {
			p = p.substring(1);
		}
		String s = String.format("/user/%s/%s", System.getProperty("user.name"), p);
		return new org.apache.hadoop.fs.Path(s);
	}
	
	public String convertToFullUserPath(String p) {
		String s = convertToUserPath(p).toString();
		if (!s.startsWith("hdfs://")) {
			s = fsUri + s;
		}
		return s;
	}

}
