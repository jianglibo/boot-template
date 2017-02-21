package hello.batch.nutch;

import java.io.File;
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import hello.util.DirectoryDeleter;

//@Component
public class UnjarCleanerPolling implements InitializingBean  {
	
	private int maxUnjarNumber;
	
	private Path unjarFolder;
	
	private static final Logger log = LoggerFactory.getLogger(UnjarCleanerPolling.class);
	
	private AtomicBoolean running = new AtomicBoolean(false);
	
	@Scheduled(initialDelay=30000, fixedDelay=30000)
	public void clean() {
		if (!running.get()) {
			running.set(true);
			clean(unjarFolder);
			running.set(false);
		} else {
			log.info("Unjarcleaner is running. skipping.");
		}
	}
	
	public void clean(Path userUnjarPath) {
		getOutDatedDirectory(userUnjarPath).forEach(f -> {
			try {
				DirectoryDeleter.deleteRecursiveIgnoreFailed(f);
				log.info("delete outdated unjar directory: {}", f.getAbsolutePath());
			} catch (Exception e) {
				log.error("Cann't delete directory: {}", f.getAbsolutePath());
			}
		});
	}
	
	protected List<File> getOutDatedDirectory(Path userUnjarPath) {
		if (Files.exists(userUnjarPath)) {
			List<File> files = Arrays.asList(userUnjarPath.toFile().listFiles());
			files = files.stream().filter(f -> !"mapred".equals(f.getName())).collect(Collectors.toList());
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
	
	@Value("${spring.nutch.unjarFolder}")
	public void setUnjarFolder(String unjarFolder) {
		this.unjarFolder = Paths.get(unjarFolder);
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
	

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
