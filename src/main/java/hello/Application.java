package hello;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.sql.DataSource;

import org.apache.hadoop.fs.FileSystem;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.data.hadoop.fs.DistCp;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import hello.util.SecurityUtil;

@SpringBootApplication
@ImportResource({"classpath:repositories.xml", "classpath:hadoop-config.xml"})
@EnableSpringDataWebSupport
@EnableJpaRepositories(basePackages="hello.repository")
@EnableWebMvc
@EnableBatchProcessing
public class Application {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(ApplicationForT.class, args);

//        System.out.println("Let's inspect the beans provided by Spring Boot:");
//
//        String[] beanNames = ctx.getBeanDefinitionNames();
//        Arrays.sort(beanNames);
//        for (String beanName : beanNames) {
//            System.out.println(beanName);
//        }
//        System.out.println(beanNames.length);
    }
    
//    see: MessageSourceAutoConfiguration
    
//    @Bean
//    public MessageSource messageSource() {
//    	ResourceBundleMessageSource parent = new ResourceBundleMessageSource();
//    	parent.setBasename("messages.shared");
//    	ResourceBundleMessageSource rbm = new ResourceBundleMessageSource();
//    	rbm.setParentMessageSource(parent);
//    	rbm.setBasenames("messages.children.format", "messages.children.validate");
//    	return rbm;
//    }

	@Autowired
	private org.apache.hadoop.conf.Configuration configuration;
    
    @Bean
    @Primary
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }
    
    @Bean("fsForApi")
    public FileSystem hdFileSystem() throws IOException {
    	FileSystem fs = FileSystem.get(configuration);
    	return fs;
    }
    
    /**
     * distCp is for inter/intra cluster copy. In other words, It's not for local to cluster copy.
     * @return
     */
    @Bean
    public DistCp distCp() {
    	return new DistCp(configuration, System.getProperty("user.name"));
//    	return new DistCp(configuration);
    }
    
    @Bean("asyncJobLauncher")
    public JobLauncher asyncJl(JobRepository jobRepository) {
    	SimpleJobLauncher jl = new SimpleJobLauncher();
    	jl.setJobRepository(jobRepository);
    	ThreadPoolTaskExecutor tpte = new ThreadPoolTaskExecutor();
    	jl.setTaskExecutor(new DelegatingSecurityContextAsyncTaskExecutor(tpte, SecurityContextHolder.getContext()));
    	return jl;
    }
    
    @Bean("syncJobLauncher")
    public JobLauncher syncJl(JobRepository jobRepository) {
    	SimpleJobLauncher jl = new SimpleJobLauncher();
    	jl.setJobRepository(jobRepository);
    	return jl;
    }

//    @Bean
//    @ConfigurationProperties(prefix="spring.secondarydatasource")
//    public DataSource secondaryDataSource() {
//        return DataSourceBuilder.create().build();
//    }
}
