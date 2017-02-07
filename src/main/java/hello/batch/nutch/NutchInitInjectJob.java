package hello.batch.nutch;

import java.io.IOException;

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
import hello.util.NutchFolderUtil;

@Configuration
public class NutchInitInjectJob  extends JobConfigurationBase {
	
	@Autowired
	private NutchFolderUtil nutchFolderUtil;
	
	/**
	 * @see org.apache.nutch.crawl.InjectorJob to find required parameters.run(String[] args)
	 * 
	 * @param crawlId
	 * @param inputFolder
	 * @param outputFolder
	 * @param mainClass
	 * @return
	 * @throws IOException 
	 */
    @Bean("nutchInjectToolTasklet")
    @StepScope
    public ToolTasklet toolTasklet(@Value("#{jobParameters['crawlId']}") String crawlId) throws IOException {
    	ToolTasklet tt = new ToolTasklet();
    	tt.setJar(fsUtil.localFileAsResource(nutchFolderUtil.getNutchJobJar(crawlId)));
    	tt.setArguments(nutchFolderUtil.getSeedDir(crawlId), "-crawlId", crawlId);
    	tt.setToolClass("org.apache.nutch.crawl.InjectorJob");
    	tt.setConfiguration(nutchFolderUtil.getNutchConfiguration(crawlId));
    	return tt;
    }
    
    @Bean("copySeedTasklet")
    @StepScope
    public Tasklet copyFileTasklet(@Value("#{jobParameters['crawlId']}") String crawlId) {
    	return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				nutchFolderUtil.copySeeds(crawlId);
				return RepeatStatus.FINISHED;
			}
		};
    }

}
