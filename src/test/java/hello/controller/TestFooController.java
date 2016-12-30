/**
 * 2016 jianglibo@gmail.com
 *
 */
package hello.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

import hello.Tbase;

/**
 * @author jianglibo@gmail.com
 *         2015年9月28日
 *
 */
public class TestFooController extends Tbase {

    @Before
    public void b() {
        fooRepo.deleteAll();
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetTaskItemsAllowed() throws Exception {

        long unpcount = fooRepo.count();
        
        createFoo("abc");
        
        String url = getFullUri("/foos");
        // @formatter: off
        mvc.perform(get(url)).andExpect(status().is2xxSuccessful()).andDo(new ResultHandler() {
            @Override
            public void handle(MvcResult result) throws Exception {
                String c = result.getResponse().getContentAsString();
                printme(c);
            }
        });
        
        mvc.perform(get(url).param("projection", "foo-simple")).andExpect(status().is2xxSuccessful()).andDo(new ResultHandler() {
            @Override
            public void handle(MvcResult result) throws Exception {
                String c = result.getResponse().getContentAsString();
                printme(c);
            }
        });
        // @formatter: on
    }
}
