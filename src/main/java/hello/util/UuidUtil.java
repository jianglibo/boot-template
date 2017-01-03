package hello.util;

import java.util.UUID;

/**
 * @author jianglibo@gmail.com
 *         2015å¹?8æœ?12æ—?
 *
 */
public class UuidUtil {
    
    public static String uuidNoDash() {
        return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
    }
    
    public static String uuidNoDashUpcase() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

}
