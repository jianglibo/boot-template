package hello.store;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.store.DataStoreWriter;
import org.springframework.data.hadoop.store.StoreException;
import org.springframework.data.hadoop.store.dataset.DatasetOperations;
import org.springframework.data.hadoop.store.dataset.RecordCallback;

import hello.Tbase;
import hello.store.dataset.FileInfo;

public class TestDataWriter extends Tbase {

	private DataStoreWriter<FileInfo> writer;
	
	private DatasetOperations datasetOperations;

	@Test
	public void t() {
		try (Stream<Path> pathes = Files.walk(Paths.get("e:/hadoop-2.7.3"))) {
			pathes.map(Path::toFile).forEach(file -> {
				FileInfo fileInfo = new FileInfo(file.getName(), file.getParent(), (int) file.length(),
						file.lastModified());
				try {
					writer.write(fileInfo);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

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
