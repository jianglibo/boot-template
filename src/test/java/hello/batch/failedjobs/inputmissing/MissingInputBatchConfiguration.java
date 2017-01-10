package hello.batch.failedjobs.inputmissing;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.batch.Person;
import hello.batch.PersonItemProcessor;

@Configuration
public class MissingInputBatchConfiguration implements ApplicationContextAware {
	
	public static final String JOB_NAME = "missingInputJob";
	
	public static final String FIXTURE_SOURCE = "fixtnotingit/sample-data-notexists.csv";
	
	private ApplicationContext applicationContext;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    
    @Autowired
    public JobRegistry jobRegistry;

    @Autowired
    public DataSource dataSource;
    
    @Bean
    public FlatFileItemReader<Person> missingInputJobItemReader() {
        FlatFileItemReader<Person> reader = new FlatFileItemReader<Person>();
        reader.setResource(applicationContext.getResource("file://" + FIXTURE_SOURCE));
        reader.setLineMapper(new DefaultLineMapper<Person>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "firstName", "lastName" });
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
                setTargetType(Person.class);
            }});
        }});
        return reader;
    }
    
    @Bean
    public JdbcBatchItemWriter<Person> missingInputJobItemWriter() {
        JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<Person>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>());
        writer.setSql("INSERT INTO people1 (first_name, last_name) VALUES (:firstName, :lastName)");
        writer.setDataSource(dataSource);
        return writer;
    }
    
    @Bean
    public PersonItemProcessor missingInputJobProcessor() {
        return new PersonItemProcessor();
    }
    
    @Bean
    public Job missingInputJobJob(MissingInputJobCompletionNotificationListener listener) {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(missingInputJobStep1())
                .end()
                .build();
    }

    @Bean
    public Step missingInputJobStep1() {
        return stepBuilderFactory.get("step1")
                .<Person, Person> chunk(10)
                .reader(missingInputJobItemReader())
                .processor(missingInputJobProcessor())
                .writer(missingInputJobItemWriter())
                .allowStartIfComplete(true)
                .build();
    }


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}