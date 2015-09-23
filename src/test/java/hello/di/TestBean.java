package hello.di;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import hello.Tbase;

public class TestBean extends Tbase{
    
    @Autowired
    private ObjectMapper om1;
    
    @Autowired
    private ObjectMapper om2;
    
    @Test
    public void tequal() {
        assertThat(om1 == om2, is(true));
    }

}
