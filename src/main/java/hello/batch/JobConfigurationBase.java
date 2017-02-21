package hello.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import hello.util.FsUtil;

public class JobConfigurationBase {

    @Autowired
    protected JobBuilderFactory jobBuilderFactory;

    @Autowired
    protected StepBuilderFactory stepBuilderFactory;
    
    @Autowired
    protected JobRegistry jobRegistry;

    @Autowired
    protected DataSource dataSource;
    
    @Autowired
    protected FsUtil fsUtil;
    
	@Autowired
	protected org.apache.hadoop.conf.Configuration configuration;

	@Autowired
	protected ApplicationContext applicationcontext;
}
