package hello.batch.nutch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import hello.util.DirectoryDeleter;

@Component
public class UnjarCleaner {
	
	private int maxUnjarNumber;
	
	private static final Logger log = LoggerFactory.getLogger(UnjarCleaner.class);
	
	@Scheduled(initialDelay=1000, fixedRate=5000)
	public void clean() {
		Path tmpBase = Paths.get(System.getProperty("java.io.tmpdir"), System.getProperty("user.name"));
		System.out.println(tmpBase.toAbsolutePath().normalize().toString());
	}
	
	public void clean(Path userUnjarPath) {
		getOutDatedDirectory(userUnjarPath).forEach(f -> {
			try {
				DirectoryDeleter.deleteRecursive(f);
			} catch (FileNotFoundException e) {
				log.error("Cann't delete directory: {}", f.getAbsolutePath());
			}
		});
	}
	
	protected List<File> getOutDatedDirectory(Path userUnjarPath) {
		if (Files.exists(userUnjarPath)) {
			List<File> files = Arrays.asList(userUnjarPath.toFile().listFiles());
			if (files.size() > maxUnjarNumber) {
				Collections.sort(files, new JavaIoFileCreateTimeComparator());
				return files.subList(0, files.size() - maxUnjarNumber);
			}
		}
		return new ArrayList<>();
	}
	
	
	@Value("${spring.nutch.maxUnjarNumber}")
	public void setMaxUnjarNumber(int maxUnjarNumber) {
		this.maxUnjarNumber = maxUnjarNumber;
		if (maxUnjarNumber < 3) {
			this.maxUnjarNumber = 3;
		}
	}
	
	public class JavaIoFileCreateTimeComparator implements Comparator<File> {
		@Override
		public int compare(File f1, File f2) {
			
			try {
				BasicFileAttributes attr1 = Files.readAttributes(f1.toPath(), BasicFileAttributes.class);
				BasicFileAttributes attr2 = Files.readAttributes(f2.toPath(), BasicFileAttributes.class);
				return attr1.creationTime().compareTo(attr2.creationTime());
			} catch (IOException e) {
				return 0;
			}
		}
		
	}

}
