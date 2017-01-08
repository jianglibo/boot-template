package hello.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.hadoop.batch.hive.HiveTasklet;

@Configuration
public class HiveTaskletForBatch {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;
    
    
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job runHiveJob(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("runHiveJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(stephive1())
                .end()
                .build();
    }

    @Bean
    public Step stephive1() {
    	HiveTasklet ht = new HiveTasklet();
        return stepBuilderFactory.get("hiveScript").tasklet(ht).build();
    }
    // end::jobstep[]
}