package hello.util;

import java.io.File;
import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DirectoryDeleter {
	
	private static Logger log = LoggerFactory.getLogger(DirectoryDeleter.class);

    public static boolean deleteRecursiveIgnoreFailed(File path) throws FileNotFoundException{
        if (!path.exists()) {
        	return true;
        }
        boolean ret = true;
        if (path.isDirectory()) {
            for (File f : path.listFiles()){
                ret = DirectoryDeleter.deleteRecursiveIgnoreFailed(f);
            }
        }
        ret =  path.delete(); // even if path is a directory, it's empty now, so we can delete it.
    	if (!ret) {
    		log.error("cannot delete file {} in unjarfolder.", path.getAbsolutePath());
    	}
    	return ret;
    }
}

