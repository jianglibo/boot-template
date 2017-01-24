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
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.Resource;
import org.springframework.data.hadoop.batch.mapreduce.JarTasklet;

import hello.batch.JobConfigurationBase;
import hello.hadoopwc.mrcode.WordCount2;

@Configuration
public class WcJob extends JobConfigurationBase {
	
	
	private static final Logger log = LoggerFactory.getLogger(WcJob.class);
	
	public static final String JOB_NAME = "wcjobXml";
	
	public static final String PARAM_FILE_TO_WC = "fileToWc";
	public static final String PARAM_JAR_TO_WC = "jarToWc";
	
	@Autowired
	private FileSystem fs;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Value("${spring.nutchSeeds}")
	private String nutchSeedsFolder;
	
//	@Autowired
//	@Qualifier("WcJobStep1")
//	private Step step1;
	
//    @Bean("WcJob")
//    public Job job() {
//        return jobBuilderFactory.get(JOB_NAME)
//                .incrementer(new RunIdIncrementer())
//                .listener(new JobExecutionListenerSupport(){
//                	@Override
//                	public void afterJob(JobExecution jobExecution) {
//                		if (jobExecution.getStatus() != BatchStatus.COMPLETED) {
//                			log.error("Job {} failed.", JOB_NAME);
//                		}
//                	}
//                })
//                .flow(step1())
//                .end()
//                .build();
//    }
	
    
    @Bean("WcJobJarTasklet")
    @StepScope
    public JarTasklet jarTasklet(@Value("#{jobParameters['jarToWc']}") String jarFile, @Value("#{jobParameters['fileToWc']}") String localFile) {
    	Path localJavaPath = Paths.get(localFile).normalize().toAbsolutePath();
    	org.apache.hadoop.fs.Path remoteHadoopPath = new org.apache.hadoop.fs.Path(nutchSeedsFolder, localJavaPath.getFileName().toString());
    	Resource jarRc = applicationContext.getResource("file:///" + jarFile);
    	JarTasklet jt = new JarTasklet();
    	jt.setJar(jarRc);
    	jt.setArguments();
    	jt.setMainClass(WordCount2.class.getName());
    	return jt;
    }
    
    @Bean("copyFileTasklet")
    @StepScope
    public Tasklet copyFileTasklet(@Value("#{jobParameters['fileToWc']}") String localFile) {
//    public Tasklet copyFileTasklet() {
    	System.out.println(localFile);
    	return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				Path localJavaPath = Paths.get(localFile).normalize().toAbsolutePath();
				org.apache.hadoop.fs.Path localHadoopPath = new org.apache.hadoop.fs.Path(localJavaPath.toString());
				org.apache.hadoop.fs.Path remoteHadoopPath = new org.apache.hadoop.fs.Path(nutchSeedsFolder, localJavaPath.getFileName().toString());
				fs.copyFromLocalFile(localHadoopPath, remoteHadoopPath);
				return RepeatStatus.FINISHED;
			}
		};
    }

//    @Bean("WcJobStep1")
//    @StepScope
//    public Step step1(/*@Qualifier("copyFileTasklet") Tasklet copyFileTasklet*/) {
////    public Step step1() {
//        return stepBuilderFactory.get("step1")
//        		.tasklet(copyFileTasklet())
//                .build();
//    }
}
