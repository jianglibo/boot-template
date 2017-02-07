package hello.batch.nutch;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.batch.JobConfigurationBase;

/**
 * only run once.
 * @author admin
 *
 */

@Configuration
public class MKNutchBaseFolderJob extends JobConfigurationBase {
	
	private static final Logger log = LoggerFactory.getLogger(MKNutchBaseFolderJob.class);
	

	@Autowired
	private FileSystem fs;
	
	@Value("${spring.nutch.baseFolder}")
	private String nutchBaseFolder;

//    @Bean("NutchMkSeedFolderJob")
//    public Job job() {
//        return jobBuilderFactory.get(NutchTaskNames.MK_SEED_FOLDER_JOB)
//                .incrementer(new RunIdIncrementer())
//                .listener(new JobExecutionListenerSupport(){
//                	@Override
//                	public void afterJob(JobExecution jobExecution) {
//                		if (jobExecution.getStatus() != BatchStatus.COMPLETED) {
//                			log.error("Job {} failed.", NutchTaskNames.MK_SEED_FOLDER_JOB);
//                		}
//                	}
//                })
//                .flow(step1())
//                .end()
//                .build();
//    }
    
    @Bean("NutchMkSeedFolderJobTasklet")
    public Tasklet tl() {
    	return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				Path p = new Path(nutchBaseFolder);
				if (!fs.exists(p)) {
					fs.mkdirs(new Path(nutchBaseFolder));
				}
				return RepeatStatus.FINISHED;
			}
		};
    }

//    @Bean("NutchMkSeedFolderJobStep1")
//    public Step step1() {
//        return stepBuilderFactory.get("step1")
//        		.tasklet(tl())
//                .build();
//    }
}
