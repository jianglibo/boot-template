package hello.batch.wc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.batch.mapreduce.ToolTasklet;

import hello.batch.JobConfigurationBase;
import hello.util.FsUtil;
import hello.util.WordCountFolderUtil;

@Configuration
public class WcJob extends JobConfigurationBase {
	
	
	private static final Logger log = LoggerFactory.getLogger(WcJob.class);
	
	public static final String JOB_NAME = "wcjobXml";
	
	/**
	 * This utility will prepend the configured ${spring.nutchBaseFolder} to the relative path.  
	 */
	@Autowired
	private WordCountFolderUtil wordCountFolderUtil;
	
	@Autowired
	private org.apache.hadoop.conf.Configuration configuration;
	
	@Autowired
	private FsUtil fsUtil;
	
    @Bean("WcJobToolTasklet")
    @StepScope
    public ToolTasklet toolTasklet(@Value("#{jobParameters['jar']}") String jarFile, @Value("#{jobParameters['inputFolder']}") String inputFolder, @Value("#{jobParameters['outputFolder']}") String outputFolder, @Value("#{jobParameters['mainClass']}") String mainClass) {
    	ToolTasklet tt = new ToolTasklet();
    	tt.setJar(fsUtil.localFileAsResource(jarFile));
    	tt.setArguments(inputFolder, outputFolder);
    	tt.setToolClass(mainClass);
    	tt.setConfiguration(configuration);
    	return tt;
    }
    
    @Bean("copyFileTasklet")
    @StepScope
    public Tasklet copyFileTasklet(@Value("#{jobParameters['localFolder']}") String localFolder) {
    	return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				wordCountFolderUtil.copyFilesToWc(localFolder);
				return RepeatStatus.FINISHED;
			}
		};
    }
}

//@Bean("WcJob")
//public Job job() {
//  return jobBuilderFactory.get(JOB_NAME)
//          .incrementer(new RunIdIncrementer())
//          .listener(new JobExecutionListenerSupport(){
//          	@Override
//          	public void afterJob(JobExecution jobExecution) {
//          		if (jobExecution.getStatus() != BatchStatus.COMPLETED) {
//          			log.error("Job {} failed.", JOB_NAME);
//          		}
//          	}
//          })
//          .flow(step1())
//          .end()
//          .build();
//}

//@Bean("WcJobStep1")
//@StepScope
//public Step step1(/*@Qualifier("copyFileTasklet") Tasklet copyFileTasklet*/) {
////public Step step1() {
//  return stepBuilderFactory.get("step1")
//  		.tasklet(copyFileTasklet())
//          .build();
//}
