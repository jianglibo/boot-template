package hello.util;

import org.apache.hadoop.fs.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * For nutch jobs we have a convention, under the baseFolder there have a folder named seeds.
 * @author Administrator
 *
 */

@Component
public class NutchFolderUtil {

	private String baseFolder;
	
	private String fsUri;

	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private org.apache.hadoop.conf.Configuration configuration;
	
	@Autowired
	private FileSystem fs;

	public String getBaseFolder() {
		return baseFolder;
	}

	@Value("${spring.nutchBaseFolder}")
	public void setBaseFolder(String baseFolder) {
		this.baseFolder = baseFolder;
	}

	public String getFsUri() {
		return fsUri;
	}

	@Value("${spring.hadoop.fsUri}")
	public void setFsUri(String fsUri) {
		this.fsUri = fsUri;
	}

}
