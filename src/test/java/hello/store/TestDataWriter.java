package hello.store;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.Remote;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.RemoteIterator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.store.DataStoreWriter;
import org.springframework.data.hadoop.store.StoreException;
import org.springframework.data.hadoop.store.dataset.DatasetOperations;
import org.springframework.data.hadoop.store.dataset.RecordCallback;

import com.google.inject.name.Named;

import hello.Tbase;
import hello.config.ApplicationConfig;
import hello.store.dataset.FileInfo;

public class TestDataWriter extends Tbase {

	private DataStoreWriter<FileInfo> writer;
	
	private DatasetOperations datasetOperations;
	
	@Autowired
	private FileSystem fs;
	
	@Autowired
	private ApplicationConfig appCfg;
	
	@Before
	public void be() throws FileNotFoundException, IllegalArgumentException, IOException {
		org.apache.hadoop.fs.Path fileinofPath = new org.apache.hadoop.fs.Path("/user/" + System.getProperty("user.name") + "/default/fileinfo");
		fs.delete(fileinofPath, true);
//		RemoteIterator<LocatedFileStatus> it = fsForApi.listFiles(, true);
//		while (it.hasNext()) {
//			LocatedFileStatus lf = it.next();
//			fsForApi.delete(f, recursive)
//			lf.getPath()
//		}
	}

	@Test
	public void t() {
		AtomicInteger at = new AtomicInteger(0);
		Random r = new Random();
		try (Stream<Path> pathes = Files.walk(Paths.get(appCfg.getDataWriteSourcePath()))) {
			pathes.map(Path::toFile).forEach(file -> {
				FileInfo fileInfo = new FileInfo(file.getName(), file.getParent(), (int) file.length(),
						file.lastModified());
				try {
					writer.write(fileInfo);
					at.incrementAndGet();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("total number is " + at);
		try {
			writer.close();
		} catch (IOException e) {
			throw new StoreException("Error closing FileInfo", e);
		}
		
        final AtomicLong count = new AtomicLong();
        datasetOperations.read(FileInfo.class, new RecordCallback<FileInfo>() {
            @Override
            public void doInRecord(FileInfo record) {
                count.getAndIncrement();
            }
        });
        System.out.println("File count: " + count.get());
        
	}
	
	  @Autowired
	  public void setDatasetOperations(DatasetOperations datasetOperations) {
	      this.datasetOperations = datasetOperations;
	  }

	@Autowired
	public void setDataStoreWriter(DataStoreWriter dataStoreWriter) {
		this.writer = dataStoreWriter;
	}

}
