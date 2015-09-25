package hello;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.WebApplicationContext;


/**
 * @author jianglibo@gmail.com
 *         2015年8月17日
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class Tbase {
    
    @Autowired
    protected WebApplicationContext context;
    
    public void printme(Object o) {
        System.out.println(o);
    }
}
