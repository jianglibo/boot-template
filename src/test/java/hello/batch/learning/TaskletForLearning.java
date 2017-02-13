package hello.batch.learning;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import com.google.common.base.Predicate;

@Configuration
public class TaskletForLearning {

	private static Logger log = LoggerFactory.getLogger(TaskletForLearning.class);
	
	public static String JOB_NAME = "batch-learning";
	
    @Bean("learningStepExecutionListener")
    @StepScope
    public StepExecutionListener generateStepExecutionListener() {
    	return new StepExecutionListener(){
			@Override
			public void beforeStep(StepExecution stepExecution) {
			}
			@Override
			public ExitStatus afterStep(StepExecution stepExecution) {
				log.info("Step {} got exitCode with: {}", stepExecution.getStepName(), stepExecution.getExitStatus().getExitCode());
				Date cc = stepExecution.getJobParameters().getDate("currentTime");
				Long forceFetch = stepExecution.getJobParameters().getLong("forceFetch");
				Assert.isTrue(forceFetch == 1L);
//				String cc = stepExecution.getExecutionContext().getString("currentTime");
				log.info("currentTime is {}", cc);
				// the returned ExistStatus is combined with original existStatus, If custom existStatus provided, custom exist code will win.
				return stepExecution.getExitStatus();
			}};
    }
	
	@Bean("stepOneTaskletSuccess")
	public Tasklet step1() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				// If set exitstatus to "Imeanit", job should be success.
//				contribution.setExitStatus(new ExitStatus("Imeanit"));
				throw new Exception("ya");
			}
		};
	}
	
	@Bean("stepTwoTasklet")
	public Tasklet step2() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				return RepeatStatus.FINISHED;
			}
		};
	}
	
	@Bean("stepThreeTasklet")
	public Tasklet step3() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				return RepeatStatus.FINISHED;
			}
		};
	}
}
