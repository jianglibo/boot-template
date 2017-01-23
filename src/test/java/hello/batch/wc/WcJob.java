package hello.batch.wc;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.hadoop.fs.FileSystem;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.batch.JobConfigurationBase;

@Configuration
public class WcJob extends JobConfigurationBase {
	
	
	private static final Logger log = LoggerFactory.getLogger(WcJob.class);
	
	public static final String JOB_NAME = "wcjob";
	
	public static final String PARAM_FILE_TO_WC = "fileToWc";
	
	@Autowired
	private FileSystem fs;
	
	@Value("${spring.nutchSeeds}")
	private String nutchSeedsFolder;
	
    @Bean("WcJob")
    public Job job() {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .listener(new JobExecutionListenerSupport(){
                	@Override
                	public void afterJob(JobExecution jobExecution) {
                		if (jobExecution.getStatus() != BatchStatus.COMPLETED) {
                			log.error("Job {} failed.", JOB_NAME);
                		}
                	}
                })
                .flow(step1())
                .end()
                .build();
    }
    
    @Bean("copyFileTasklet")
    public Tasklet copyFileTasklet() {
    	return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				String fileToWc = (String) chunkContext.getStepContext().getJobParameters().get(PARAM_FILE_TO_WC);
				Path localJavaPath = Paths.get(fileToWc).normalize().toAbsolutePath();
				org.apache.hadoop.fs.Path localHadoopPath = new org.apache.hadoop.fs.Path(localJavaPath.toString());
				org.apache.hadoop.fs.Path remoteHadoopPath = new org.apache.hadoop.fs.Path(nutchSeedsFolder, localJavaPath.getFileName().toString());
				fs.copyFromLocalFile(localHadoopPath, remoteHadoopPath);
				return RepeatStatus.FINISHED;
			}
		};
    }

    @Bean("WcJobStep1")
    public Step step1() {
        return stepBuilderFactory.get("step1")
        		.tasklet(copyFileTasklet())
                .build();
    }
}
