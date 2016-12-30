/**
 * 2016 jianglibo@gmail.com
 *
 */
package hello.ylbx;

import java.io.IOException;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

/**
 * @author jianglibo@gmail.com
 *         2015年9月24日
 *
 */
public class TestUseCookie {
    
    private static String domain = "115.236.191.154";
    
    private static String url = "http://" + domain +  "/gioidata/personInfoAction.do?method=queryPersonInfoList";
    
    private void printme(Object o) {
        System.out.println(o);
    }

    @Test
    public void t() throws ClientProtocolException, IOException {
        Request r = Request.Post(url).bodyForm(new BasicNameValuePair("limit", "20"), new BasicNameValuePair("_queryid", "new"));
        String c = e().execute(r).returnContent().asString();
        printme(c);
    }
    
    private Executor e(){
        CookieStore cs = cs();
        Executor executor = Executor.newInstance().use(cs);
        return executor;
    }
    
    private CookieStore cs() {
        CookieStore cs = new BasicCookieStore();
        Cookie co =new MyBcCookie("JSESSIONID", "cDlRWDpJmB37kyPrWNNlGd92TgsL9YG7ql11M8sjFQj1hDGvSR2M");
        cs.addCookie(co);
        return cs;
    }
    
    public static class MyBcCookie extends BasicClientCookie {
        
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        /**
         * @param name
         * @param value
         */
        public MyBcCookie(String name, String value) {
            super(name, value);
        }
        
        @Override
        public String getDomain() {
            return "115.236.191.154";
        }
    }
    
}
