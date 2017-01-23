package hello.util;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;

import hello.TbatchBase;

public class TestDistCp extends TbatchBase {
	
	private static final Path tpath = new Path("distCp/distCp.txt");
	
	@Autowired
	private FileSystem fs;
	
	@Before
	public void b() throws IOException {
		if (fs.exists(tpath)) {
			fs.delete(tpath, false);
		} else {
			if (!fs.exists(tpath.getParent())) {
				fs.mkdirs(tpath.getParent());
			}
		}
	}
	
	@Test
	public void tApiCopy() throws IllegalArgumentException, IOException {
		String localFile = Paths.get(BATCH_FIXTURE_BASE, "distCp.txt").normalize().toAbsolutePath().toString();
		fs.copyFromLocalFile(new Path(localFile), tpath);
		
		RemoteIterator<LocatedFileStatus> rfs = fs.listFiles(tpath.getParent(), false);
		List<LocatedFileStatus> rfslist = Lists.newArrayList();
		while(rfs.hasNext()) {
			rfslist.add(rfs.next());
		}
		assertThat(rfslist.size(), equalTo(1));
		
		FSDataInputStream dis = fs.open(tpath);
		
		String s = new String(ByteStreams.toByteArray(dis));
		
		assertThat(s, equalTo("hello distcp."));
	}
}
