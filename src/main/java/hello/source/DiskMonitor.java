package hello.source;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

//import com.sun.nio.file.SensitivityWatchEventModifier;

import static java.nio.file.StandardWatchEventKinds.*;

@Configuration
public class DiskMonitor {
	
	private static final Logger LOG = LoggerFactory.getLogger(DiskMonitor.class);
	
	@Bean
	public ThreadPoolTaskExecutor watcherExecutor() {
		ThreadPoolTaskExecutor tple = new ThreadPoolTaskExecutor();
		return tple;
	}
	
	@Bean
	public ThreadPoolTaskExecutor watcherProcessExecutor() {
		ThreadPoolTaskExecutor tple = new ThreadPoolTaskExecutor();
		return tple;
	}
	
	@Autowired
	private ThreadPoolTaskExecutor watcherExecutor;
	
//    public void startRecursiveWatcher() throws IOException {
//        LOG.info("Starting Recursive Watcher");
//
//		WatchService watcher = FileSystems.getDefault().newWatchService();
//        final Map<WatchKey, Path> keys = new HashMap<>();
//
//        Consumer<Path> register = p -> {
//            if (!p.toFile().exists() || !p.toFile().isDirectory()) {
//                throw new RuntimeException("folder " + p + " does not exist or is not a directory");
//            }
//            try {
//                Files.walkFileTree(p, new SimpleFileVisitor<Path>() {
//                    @Override
//                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
//                        LOG.info("registering " + dir + " in watcher service");
//                        WatchKey watchKey = dir.register(watcher, new WatchEvent.Kind[]{ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY}, SensitivityWatchEventModifier.HIGH);
//                        keys.put(watchKey, dir);
//                        return FileVisitResult.CONTINUE;
//                    }
//                });
//            } catch (IOException e) {
//                throw new RuntimeException("Error registering path " + p);
//            }
//        };
//        
//        register.accept(Paths.get("c:"));
//        
//        watcherExecutor.submit(() -> {
//            while (true) {
//                final WatchKey key;
//                try {
//                    key = watcher.take(); // wait for a key to be available
//                } catch (InterruptedException ex) {
//                    return;
//                }
//
//                final Path dir = keys.get(key);
//                if (dir == null) {
//                    System.err.println("WatchKey " + key + " not recognized!");
//                    continue;
//                }
//
//                key.pollEvents().stream()
//                        .filter(e -> (e.kind() != OVERFLOW))
//                        .map(e -> ((WatchEvent<Path>) e).context())
//                        .forEach(p -> {
//                            final Path absPath = dir.resolve(p);
//                            if (absPath.toFile().isDirectory()) {
//                                register.accept(absPath);
//                            } else {
//                                final File f = absPath.toFile();
//                                LOG.info("Detected new file " + f.getAbsolutePath());
//                            }
//                        });
//
//                boolean valid = key.reset(); // IMPORTANT: The key must be reset after processed
//                if (!valid) {
//                    break;
//                }
//            }
//        });
//    }
	
	public void d() throws IOException {
		WatchService watcher = FileSystems.getDefault().newWatchService();
		
		Path dir = Paths.get("c:");
		try {
		    WatchKey key = dir.register(watcher,
		                           ENTRY_CREATE,
		                           ENTRY_DELETE,
		                           ENTRY_MODIFY,
		                           OVERFLOW);
		    
		    while (true) {
		    	try {
		    		// wait for a key to be available
		    		key = watcher.take();
		    	} catch (InterruptedException ex) {
		    		return;
		    	}

		    	for (WatchEvent<?> event : key.pollEvents()) {
		    		// get event type
		    		WatchEvent.Kind<?> kind = event.kind();

		    		// get file name
		    		@SuppressWarnings("unchecked")
		    		WatchEvent<Path> ev = (WatchEvent<Path>) event;
		    		
		    		Path fileName = ev.context();
		    		System.out.println(kind.name() + ": " + fileName + ev.count());

		    		if (kind == OVERFLOW) {
		    			continue;
		    		} else if (kind == ENTRY_CREATE) {

		    		} else if (kind == ENTRY_DELETE) {

		    		} else if (kind == ENTRY_MODIFY) {

		    		}
		    	}
		    	// IMPORTANT: The key must be reset after processed
		    	boolean valid = key.reset();
		    	if (!valid) {
		    		break;
		    	}
		    }
		} catch (IOException x) {
		    System.err.println(x);
		}
	}

}
