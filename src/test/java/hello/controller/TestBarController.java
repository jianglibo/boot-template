/**
 * 2016 jianglibo@gmail.com
 *
 */
package hello.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

import hello.Tbase;
import hello.repository.BarRepository;

/**
 * 
 * @author jianglibo@gmail.com
 *
 */
public class TestBarController extends Tbase {
	
    @Autowired
    protected BarRepository barRepo;

    @Before
    public void b() {
//        fooRepo.deleteAll();
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetTaskItemsAllowed() throws Exception {

        createFoo("abc");
        
        String url = "/bars";
        // @formatter: off
        mvc.perform(get(url)).andExpect(status().is2xxSuccessful()).andDo(new ResultHandler() {
            @Override
            public void handle(MvcResult result) throws Exception {
                String c = result.getResponse().getContentAsString();
                printme(c);
            }
        });
        // @formatter: on
    }
}
