package hello.di;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.fasterxml.jackson.databind.ObjectMapper;

import hello.Tbase;

public class TestBean extends Tbase{
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    @Qualifier(value="romx")
    private ObjectMapper om2;
    
    @Resource(name="romx")
    private ObjectMapper om3;
    
    @Test
    public void tequal() {
        assertThat(objectMapper == om2, is(false));
        assertThat(om2 == om3, is(true));
    }

}
