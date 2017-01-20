package hello.batch.hive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.batch.JobConfigurationBase;
import hello.hive.HiveCommonCommandExecutor;
import hello.util.FsUtil;

@Configuration
public class InitHiveJob extends JobConfigurationBase {
	
	
	private static final Logger log = LoggerFactory.getLogger(InitHiveJob.class);
	
	private static final String HIVE_TABLE_NAME = "ahivetable";
	
	private static final String AVRO_SCHEMA_URL = "default/fileinfo/1.avro";
	
	@Autowired
	private HiveCommonCommandExecutor hexecutor;
	
	@Autowired
	private FsUtil fsUtil;

    @Bean("InitHiveJob")
    public Job job() {
        return jobBuilderFactory.get(HiveTaskNames.INIT_HIVE_TABLE_JOB)
                .incrementer(new RunIdIncrementer())
                .listener(new JobExecutionListenerSupport(){
                	@Override
                	public void afterJob(JobExecution jobExecution) {
                		if (jobExecution.getStatus() != BatchStatus.COMPLETED) {
                			log.error("Job {} failed.", HiveTaskNames.INIT_HIVE_TABLE_JOB);
                		}
                	}
                })
                .flow(step1())
                .end()
                .build();
    }
    
    @Bean("InitHiveJobTasklet")
    public Tasklet tl() {
    	return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				hexecutor.avroSchemaUrl(HIVE_TABLE_NAME, fsUtil.convertToFullUserPath(AVRO_SCHEMA_URL));
				return RepeatStatus.FINISHED;
			}
		};
    }

    @Bean("InitHiveJobStep1")
    public Step step1() {
        return stepBuilderFactory.get("step1")
        		.tasklet(tl())
                .build();
    }
}
