package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ImportResource("classpath:repositories.xml")
@EnableSpringDataWebSupport
@EnableWebMvc   
public class Application {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

//        System.out.println("Let's inspect the beans provided by Spring Boot:");
//
//        String[] beanNames = ctx.getBeanDefinitionNames();
//        Arrays.sort(beanNames);
//        for (String beanName : beanNames) {
//            System.out.println(beanName);
//        }
//        System.out.println(beanNames.length);
    }
    
    @Bean
    public MessageSource messageSource() {
    	ResourceBundleMessageSource parent = new ResourceBundleMessageSource();
    	parent.setBasename("messages.shared");
    	ResourceBundleMessageSource rbm = new ResourceBundleMessageSource();
    	rbm.setParentMessageSource(parent);
    	rbm.setBasenames("messages.format");
    	return rbm;
    }
}
