package hello.batch.nutch;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

public class BytemanNutchHelper {
	
	private static Logger log = LoggerFactory.getLogger(BytemanNutchHelper.class);
	
	private static final ConcurrentHashMap<String, File> jar2Unjar = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String, Boolean> unjared = new ConcurrentHashMap<>();

	public File calculateUnjarFolder(Resource jar, File targetUnjarFolder) throws IOException {
		String key = jar.contentLength() + jar.getURI().toString();
		if (jar2Unjar.containsKey(key)) {
			log.info("jar key {} was founded. returning it.", key);
			try {
				log.info("remove unused empty folder {}.", targetUnjarFolder.toString());
				targetUnjarFolder.delete();
			} catch (Exception e) {
			}
		} else {
			log.info("jar key {} not found. putting it into map.", key);
			jar2Unjar.put(key, targetUnjarFolder);
		}
		return jar2Unjar.get(key);
	}
	
	public boolean isUnjared(Resource jar) throws IOException {
		String key = jar.contentLength() + jar.getURI().toString();
		if (unjared.containsKey(key)) {
			log.info("{} has already unjared.", key);
			return true;
		} else {
			log.info("{} hasn't unjared. continue to unjar.", key);
			unjared.put(key, true);
			return false;
		}
	}
	
	public boolean traceln(Object o) {
		log.info(o.toString());
		return true;
	}
	
	public boolean debug(Object o) {
		log.info(o.toString());
		return true;
	}
}
