/**
 * 2016 jianglibo@gmail.com
 *
 */
package hello.di;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Co {
    
    @Bean(name="rom")
    @Qualifier(value="rom")
    public ObjectMapper om() {
        return new ObjectMapper();
    }

}
