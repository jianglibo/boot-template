package hello.batch.nutch;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import hello.util.DirectoryDeleter;

//@Component
public class UnjarCleanerWatcher implements InitializingBean  {
	
	private int maxUnjarNumber;
	
	private Path unjarFolder;
	
	private static final Logger log = LoggerFactory.getLogger(UnjarCleanerWatcher.class);
	
	private ArrayBlockingQueue<Path> queue = new ArrayBlockingQueue<>(50, true);
	
	private Thread watcherThread;
	
	private static Pattern unjarPattern = Pattern.compile(".*unjar\\d+$");
	
	@Scheduled(initialDelay=30000, fixedDelay=30000)
	public void clean() {
		cleanWork();
		if (! watcherThread.isAlive()) {
			startWatcherThread();
		}
	}
	
	private synchronized void cleanWork() {
		while (queue.size() > maxUnjarNumber) {
			try {
				Path p = queue.take();
				if (unjarPattern.matcher(p.getFileName().toString()).matches()) {
					boolean success = DirectoryDeleter.deleteRecursiveIgnoreFailed(p.toFile());
					if (!success) {
						log.error("unjarDeleter delete {} failed.", p.normalize().toAbsolutePath().toString());
					}
				}
			} catch (InterruptedException e) {
				log.error("unjarDeleter queue interupted");
			} catch (FileNotFoundException e) {
				log.error("unjarDeleter filenotfound.");
			}
		}
	}
	
	protected class Watcher implements Runnable {
		@Override
		public void run() {
			try {
				startWatcher();
			} catch (IOException e) {
				log.error("Cannot create unjarFolder Watcher");
			}
		}
		
	    private void startWatcher() throws IOException {
	        final WatchService watchService = FileSystems.getDefault()
	                .newWatchService();
	        unjarFolder.register(watchService, ENTRY_CREATE);
	 
	        Runtime.getRuntime().addShutdownHook(new Thread() {
	            public void run() {
	                try {
	                    watchService.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        });
	        WatchKey key = null;
	        
	        while (true) {
	            try {
	                key = watchService.take();
	                for (WatchEvent<?> event : key.pollEvents()) {
	                	Path createdFile = unjarFolder.resolve((Path) event.context());
	                	if (!queue.contains(createdFile)) {
	                		queue.put(createdFile);
	                	}
	                }
	                boolean reset = key.reset();
	                if (!reset) {
	                    System.out.println("Could not reset the watch key.");
	                    break;
	                }
	            } catch (Exception e) {
	                System.out.println("InterruptedException: " + e.getMessage());
	            }
	        }
	    }
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

	
	private void startWatcherThread() {
		watcherThread = new Thread(new Watcher());
		watcherThread.start();
		log.debug("watcherThread started.");
		// delete all existing unjarFolders.
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Files.list(unjarFolder).distinct().filter(p -> unjarPattern.matcher(p.getFileName().toString()).matches()).forEach( pa -> {
						try {
							DirectoryDeleter.deleteRecursiveIgnoreFailed(pa.toFile());
						} catch (FileNotFoundException e) {
							log.error("remove unjarFolder {} failed.", pa.normalize().toAbsolutePath().toString());
						}
					});
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	 
	@Override
	public void afterPropertiesSet() throws Exception {
		startWatcherThread();
	}

}
