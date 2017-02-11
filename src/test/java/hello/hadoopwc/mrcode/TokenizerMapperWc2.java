package hello.hadoopwc.mrcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.StringUtils;

public class TokenizerMapperWc2  extends Mapper<Object, Text, Text, IntWritable> {

	static enum CountersEnum {
		INPUT_WORDS
	}
	
	  
	public static Path logDir =  new Path("yarnlog");

	private final static IntWritable one = new IntWritable(1);
	private Text word = new Text();

	private boolean caseSensitive;
	private Set<String> patternsToSkip = new HashSet<String>();

	private Configuration conf;
	private BufferedReader fis;
	
	public String printClassPath() throws IOException {
		ClassLoader sysClassLoader = this.getClass().getClassLoader();
		
        URL[] urls = ((URLClassLoader)sysClassLoader).getURLs();
        List<String> lines = new ArrayList<>();
        FileSystem fs = FileSystem.get(conf);
        Path  p= new Path(logDir, new SimpleDateFormat("HHmmss").format(Date.from(Instant.now())));
		FSDataOutputStream fd0 = fs.create(p);

		
        for(int i=0; i< urls.length; i++)
        {
    		fd0.writeBytes(urls[i].getFile() + "\n");
        }
		fd0.close();
        return String.join(",", lines);
	}

	@Override
	public void setup(Context context) throws IOException, InterruptedException {
		conf = context.getConfiguration();
		printClassPath();
		caseSensitive = conf.getBoolean("wordcount.case.sensitive", false); //true
		if (conf.getBoolean("wordcount.skip.patterns", false)) { //true
			URI[] patternsURIs = Job.getInstance(conf).getCacheFiles();
			for (URI patternsURI : patternsURIs) {
				Path patternsPath = new Path(patternsURI.getPath());
				String patternsFileName = patternsPath.getName().toString();
				parseSkipFile(patternsFileName);
			}
		}
	}

	private void parseSkipFile(String fileName) {
		try {
			fis = new BufferedReader(new FileReader(fileName));
			String pattern = null;
			while ((pattern = fis.readLine()) != null) {
				patternsToSkip.add(pattern);
			}
		} catch (IOException ioe) {
			System.err.println(
					"Caught exception while parsing the cached file '" + StringUtils.stringifyException(ioe));
		}
	}

	@Override
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		String line = (caseSensitive) ? value.toString() : value.toString().toLowerCase();
		for (String pattern : patternsToSkip) {
			line = line.replaceAll(pattern, "");
		}
		StringTokenizer itr = new StringTokenizer(line);
		while (itr.hasMoreTokens()) {
			word.set(itr.nextToken());
			context.write(word, one);
			Counter counter = context.getCounter(CountersEnum.class.getName(), CountersEnum.INPUT_WORDS.toString());
			counter.increment(1);
		}
	}

}
