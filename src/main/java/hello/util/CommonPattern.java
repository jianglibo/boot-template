/**
 * Copyright 2015 jianglibo@gmail.com
 *
 */
package hello.util;

import java.util.regex.Pattern;

/**
 * @author jianglibo@gmail.com
 *         2015å¹?12æœ?25æ—?
 *
 */
public class CommonPattern {

    public static final Pattern EMAIL_PTN = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    public static final Pattern CHINA_MOBILE = Pattern.compile("^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$");
    public static final Pattern USER_NAME = Pattern.compile("^[a-zA-Z][a-zA-Z0-9\\-_.]*[a-zA-Z0-9]$");
    public static final Pattern DUPLICATED_DOT = Pattern.compile(".*(\\.)\\1.*");
    
}
