package hello.util;

import java.util.UUID;

/**
 * @author jianglibo@gmail.com
 *         
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
