package hello;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import hello.domain.Foo;
import hello.repository.FooRepository;


/**
 * @author jianglibo@gmail.com
 *         2015年8月17日
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class Tbase {
    
    @Autowired
    protected WebApplicationContext context;
    
    protected MockMvc mvc;

    @Autowired
    protected FooRepository fooRepo;
    
    @Before
    public void before() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }
    
    public void printme(Object o) {
        System.out.println(o);
    }
    
    
    public void createFoo(String name) {
        Foo foo = new Foo(name);
        fooRepo.save(foo);
    }
    
    public String getFullUri(String uri) {
        return getApiPrefix() + uri;
    }
    
    public String getApiPrefix() {
        return "/api/v1";
    }
    
}
