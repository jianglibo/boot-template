package hello.batch.nutch;

import java.io.IOException;
import java.util.Properties;

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

//https://wiki.apache.org/nutch/Nutch2Crawling
	
@Configuration
public class NutchTasklets  extends JobConfigurationBase {
	
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
    public ToolTasklet nutchInjectToolTasklet(@Value("#{jobParameters['crawlId']}") String crawlId
    		,@Value("#{jobParameters['debug']}") long debug
    		) throws IOException {
    	ToolTasklet tt = new ToolTasklet();
    	tt.setJar(fsUtil.localFileAsResource(nutchFolderUtil.getNutchJobJar(crawlId)));
    	tt.setArguments(nutchFolderUtil.getSeedDir(crawlId), "-crawlId", crawlId);
    	tt.setToolClass("org.apache.nutch.crawl.InjectorJob");
    	tt.setConfiguration(nutchFolderUtil.getNutchConfiguration(crawlId));
//    	tt.setLibs(nutchFolderUtil.getLibsWithJobSelf(crawlId));
//    	tt.setLibs(nutchFolderUtil.getLibs());
//    	tt.setArchives(nutchFolderUtil.getJobArchive(crawlId));
    	if (debug == 1) {
        	Properties properties = new Properties();
        	properties.setProperty("yarn.app.mapreduce.am.log.level", "DEBUG");
        	properties.setProperty("mapreduce.map.log.level", "DEBUG");
        	properties.setProperty("mapreduce.reduce.log.level", "DEBUG");
        	tt.setProperties(properties);
    	}
    	return tt;
    }
    
//  commonOptions="-D mapred.reduce.tasks=$numTasks -D mapred.child.java.opts=-Xmx1000m -D mapred.reduce.tasks.speculative.execution=false -D mapred.map.tasks.speculative.execution=false -D mapred.compress.map.output=true"
    /**
     * If the job returns 1, means no new segments created. If return 0, that's ok.
     * @param crawlId
     * @param numSlaves
     * @param addDays
     * @param debug
     * @param batchId
     * @return
     * @throws IOException
     */
    @Bean("nutchGenerateToolTasklet")
    @StepScope
    public ToolTasklet nutchGenerateToolTasklet(@Value("#{jobParameters['crawlId']}") String crawlId
    		,@Value("#{jobParameters['numSlaves']}") long numSlaves
    		,@Value("#{jobParameters['addDays']}") long addDays
    		,@Value("#{jobParameters['debug']}") long debug
    		,@Value("#{jobParameters['batchId']}") String batchId) throws IOException {
    	long numTasks = numSlaves * 2;
    	long sizeFetchlist = numSlaves * 50000;
    	ToolTasklet tt = new ToolTasklet();
    	tt.setJar(fsUtil.localFileAsResource(nutchFolderUtil.getNutchJobJar(crawlId)));
    	tt.setArguments("-topN", String.valueOf(sizeFetchlist),"-noNorm", "-noFilter", "-adddays", String.valueOf(addDays), "-crawlId", crawlId, "-batchId", batchId);
    	tt.setToolClass("org.apache.nutch.crawl.GeneratorJob");
    	tt.setConfiguration(nutchFolderUtil.getNutchConfiguration(crawlId));
    	tt.setProperties(commonProperties(numTasks, debug));
    	return tt;
    }
    
    
    @Bean("nutchFetchToolTasklet")
    @StepScope
    public ToolTasklet nutchFetchToolTasklet(@Value("#{jobParameters['crawlId']}") String crawlId
    		,@Value("#{jobParameters['numSlaves']}") long numSlaves
    		,@Value("#{jobParameters['timeLimitFetch']}") long timeLimitFetch
    		,@Value("#{jobParameters['threads']}") long threads
    		,@Value("#{jobParameters['debug']}") long debug
    		,@Value("#{jobParameters['batchId']}") String batchId) throws IOException {
    	long numTasks = numSlaves * 2;
    	ToolTasklet tt = new ToolTasklet();
    	tt.setJar(fsUtil.localFileAsResource(nutchFolderUtil.getNutchJobJar(crawlId)));
    	tt.setArguments(batchId, "-crawlId", crawlId, "-threads", String.valueOf(threads));
    	tt.setToolClass("org.apache.nutch.fetcher.FetcherJob");
    	tt.setConfiguration(nutchFolderUtil.getNutchConfiguration(crawlId));
    	Properties properties = commonProperties(numTasks, debug);
    	properties.put("fetcher.timelimit.mins",String.valueOf(timeLimitFetch));
    	tt.setProperties(properties);
    	return tt;
    }
    
    @Bean("nutchParseToolTasklet")
    @StepScope
    public ToolTasklet nutchParseToolTasklet(@Value("#{jobParameters['crawlId']}") String crawlId
    		,@Value("#{jobParameters['numSlaves']}") long numSlaves
    		,@Value("#{jobParameters['startSkipping']}") long startSkipping
    		,@Value("#{jobParameters['skipRecords']}") long skipRecords
    		,@Value("#{jobParameters['debug']}") long debug
    		,@Value("#{jobParameters['batchId']}") String batchId) throws IOException {
    	long numTasks = numSlaves * 2;
    	ToolTasklet tt = new ToolTasklet();
    	tt.setJar(fsUtil.localFileAsResource(nutchFolderUtil.getNutchJobJar(crawlId)));
    	tt.setArguments(batchId, "-crawlId", crawlId);
    	tt.setToolClass("org.apache.nutch.parse.ParserJob");
    	tt.setConfiguration(nutchFolderUtil.getNutchConfiguration(crawlId));
    	Properties properties = commonProperties(numTasks, debug);
    	properties.put("mapred.skip.attempts.to.start.skipping",String.valueOf(startSkipping));
    	properties.put("mapred.skip.map.max.skip.records",String.valueOf(skipRecords));
    	tt.setProperties(properties);
    	return tt;
    }
    
    @Bean("nutchUpdateDbToolTasklet")
    @StepScope
    public ToolTasklet nutchUpdateDbToolTasklet(@Value("#{jobParameters['crawlId']}") String crawlId
    		,@Value("#{jobParameters['numSlaves']}") long numSlaves
    		,@Value("#{jobParameters['debug']}") long debug
    		,@Value("#{jobParameters['batchId']}") String batchId) throws IOException {
    	long numTasks = numSlaves * 2;
    	ToolTasklet tt = new ToolTasklet();
    	tt.setJar(fsUtil.localFileAsResource(nutchFolderUtil.getNutchJobJar(crawlId)));
    	tt.setArguments(batchId, "-crawlId", crawlId);
    	tt.setToolClass("org.apache.nutch.crawl.DbUpdaterJob");
    	tt.setConfiguration(nutchFolderUtil.getNutchConfiguration(crawlId));
    	Properties properties = commonProperties(numTasks, debug);
    	tt.setProperties(properties);
    	return tt;
    }
    
    private Properties commonProperties(long numTasks, long debug) {
    	Properties properties = new Properties();
    	if (debug == 1) {
        	properties.setProperty("yarn.app.mapreduce.am.log.level", "DEBUG");
        	properties.setProperty("mapreduce.map.log.level", "DEBUG");
        	properties.setProperty("mapreduce.reduce.log.level", "DEBUG");
    	}
    	properties.setProperty("mapred.reduce.tasks", String.valueOf(numTasks));
    	properties.setProperty("mapred.reduce.tasks.speculative.execution", "false");
    	properties.setProperty("mapred.map.tasks.speculative.execution", "false");
    	properties.setProperty("mapred.compress.map.output", "true");
    	return properties;
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
