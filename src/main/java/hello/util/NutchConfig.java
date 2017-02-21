package hello.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class NutchConfig {
	
	private static Map<String, String> nutchDefault = new HashMap<>();
	
	static {
		nutchDefault.put("store.ip.address","false");
		nutchDefault.put("file.content.limit","65536");
		nutchDefault.put("file.crawl.redirect_noncanonical","true");
		nutchDefault.put("file.content.ignored","true");
		nutchDefault.put("file.crawl.parent","true");
		nutchDefault.put("http.agent.name","");
		nutchDefault.put("http.robots.agents","");
		nutchDefault.put("http.robots.403.allow","true");
		nutchDefault.put("http.agent.description","");
		nutchDefault.put("http.agent.url","");
		nutchDefault.put("http.agent.email","");
		nutchDefault.put("http.agent.version","Nutch-2.3.1");
		nutchDefault.put("http.agent.rotate","false");
		nutchDefault.put("http.agent.rotate.file","agents.txt");
		nutchDefault.put("http.agent.host","");
		nutchDefault.put("http.timeout","10000");
		nutchDefault.put("http.max.delays","100");
		nutchDefault.put("http.content.limit","65536");
		nutchDefault.put("http.proxy.host","");
		nutchDefault.put("http.proxy.port","");
		nutchDefault.put("http.proxy.username","");
		nutchDefault.put("http.proxy.password","");
		nutchDefault.put("http.proxy.realm","");
		nutchDefault.put("http.auth.file","httpclient-auth.xml");
		nutchDefault.put("http.verbose","false");
		nutchDefault.put("http.useHttp11","false");
		nutchDefault.put("http.accept.language","en-us,en-gb,en;q=0.7,*;q=0.3");
		nutchDefault.put("http.accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		nutchDefault.put("http.store.responsetime","true");
		nutchDefault.put("ftp.username","anonymous");
		nutchDefault.put("ftp.password","anonymous@example.com");
		nutchDefault.put("ftp.content.limit","65536");
		nutchDefault.put("ftp.timeout","60000");
		nutchDefault.put("ftp.server.timeout","100000");
		nutchDefault.put("ftp.keep.connection","false");
		nutchDefault.put("ftp.follow.talk","false");
		nutchDefault.put("db.fetch.interval.default","2592000");
		nutchDefault.put("db.fetch.interval.max","7776000");
		nutchDefault.put("db.fetch.schedule.class","org.apache.nutch.crawl.DefaultFetchSchedule");
		nutchDefault.put("db.fetch.schedule.adaptive.inc_rate","0.4");
		nutchDefault.put("db.fetch.schedule.adaptive.dec_rate","0.2");
		nutchDefault.put("db.fetch.schedule.adaptive.min_interval","60");
		nutchDefault.put("db.fetch.schedule.adaptive.max_interval","31536000");
		nutchDefault.put("db.fetch.schedule.adaptive.sync_delta","true");
		nutchDefault.put("db.fetch.schedule.adaptive.sync_delta_rate","0.3");
		nutchDefault.put("db.update.additions.allowed","true");
		nutchDefault.put("db.update.max.inlinks","10000");
		nutchDefault.put("db.ignore.internal.links","true");
		nutchDefault.put("db.ignore.external.links","false");
		nutchDefault.put("db.score.injected","1.0");
		nutchDefault.put("db.score.link.external","1.0");
		nutchDefault.put("db.score.link.internal","1.0");
		nutchDefault.put("db.score.count.filtered","false");
		nutchDefault.put("db.max.outlinks.per.page","100");
		nutchDefault.put("db.max.anchor.length","100");
		nutchDefault.put("db.parsemeta.to.crawldb","");
		nutchDefault.put("db.fetch.retry.max","3");
		nutchDefault.put("db.signature.class","org.apache.nutch.crawl.MD5Signature");
		nutchDefault.put("db.signature.text_profile.min_token_len","2");
		nutchDefault.put("db.signature.text_profile.quant_rate","0.01");
		nutchDefault.put("generate.max.count","-1");
		nutchDefault.put("generate.max.distance","-1");
		nutchDefault.put("generate.count.mode","host");
		nutchDefault.put("generate.update.crawldb","false");
		nutchDefault.put("partition.url.mode","byHost");
		nutchDefault.put("crawl.gen.delay","604800000");
		nutchDefault.put("fetcher.server.delay","5.0");
		nutchDefault.put("fetcher.server.min.delay","0.0");
		nutchDefault.put("fetcher.max.crawl.delay","30");
		nutchDefault.put("fetcher.threads.fetch","10");
		nutchDefault.put("fetcher.threads.per.queue","1");
		nutchDefault.put("fetcher.queue.mode","byHost");
		nutchDefault.put("fetcher.queue.use.host.settings","false");
		nutchDefault.put("fetcher.verbose","false");
		nutchDefault.put("fetcher.parse","false");
		nutchDefault.put("fetcher.store.content","true");
		nutchDefault.put("fetcher.timelimit.mins","-1");
		nutchDefault.put("fetcher.max.exceptions.per.queue","-1");
		nutchDefault.put("fetcher.throughput.threshold.pages","-1");
		nutchDefault.put("fetcher.throughput.threshold.sequence","5");
		nutchDefault.put("fetcher.throughput.threshold.check.after","5");
		nutchDefault.put("fetcher.queue.depth.multiplier","50");
		nutchDefault.put("indexingfilter.order","");
		nutchDefault.put("indexer.score.power","0.5");
		nutchDefault.put("indexer.max.title.length","100");
		nutchDefault.put("moreIndexingFilter.indexMimeTypeParts","true");
		nutchDefault.put("anchorIndexingFilter.deduplicate","false");
		nutchDefault.put("urlnormalizer.order","org.apache.nutch.net.urlnormalizer.basic.BasicURLNormalizer org.apache.nutch.net.urlnormalizer.regex.RegexURLNormalizer");
		nutchDefault.put("urlnormalizer.regex.file","regex-normalize.xml");
		nutchDefault.put("urlnormalizer.loop.count","1");
		nutchDefault.put("mime.type.magic","true");
		nutchDefault.put("plugin.folders","plugins");
		nutchDefault.put("plugin.auto-activation","true");
		nutchDefault.put("plugin.includes","protocol-http|urlfilter-regex|parse-(html|tika)|index-(basic|anchor)|urlnormalizer-(pass|regex|basic)|scoring-opic");
		nutchDefault.put("plugin.excludes","");
		nutchDefault.put("parse.plugin.file","parse-plugins.xml");
		nutchDefault.put("parser.character.encoding.default","windows-1252");
		nutchDefault.put("encodingdetector.charset.min.confidence","-1");
		nutchDefault.put("parser.caching.forbidden.policy","content");
		nutchDefault.put("parser.html.impl","neko");
		nutchDefault.put("parser.html.form.use_action","false");
		nutchDefault.put("parser.html.outlinks.ignore_tags","");
		nutchDefault.put("htmlparsefilter.order","");
		nutchDefault.put("parser.timeout","30");
		nutchDefault.put("parser.skip.truncated","true");
		nutchDefault.put("urlfilter.tld.length","");
		nutchDefault.put("urlfilter.domain.file","domain-urlfilter.txt");
		nutchDefault.put("urlfilter.regex.file","regex-urlfilter.txt");
		nutchDefault.put("urlfilter.automaton.file","automaton-urlfilter.txt");
		nutchDefault.put("urlfilter.prefix.file","prefix-urlfilter.txt");
		nutchDefault.put("urlfilter.suffix.file","suffix-urlfilter.txt");
		nutchDefault.put("urlfilter.order","");
		nutchDefault.put("scoring.filter.order","");
		nutchDefault.put("lang.ngram.min.length","1");
		nutchDefault.put("lang.ngram.max.length","4");
		nutchDefault.put("lang.analyze.max.length","2048");
		nutchDefault.put("lang.extraction.policy","detect,identify");
		nutchDefault.put("lang.identification.only.certain","false");
		nutchDefault.put("index.metadata","description,keywords");
		nutchDefault.put("metatags.names","*");
		nutchDefault.put("hadoop.job.history.user.location","${hadoop.log.dir}/history/user");
		nutchDefault.put("io.serializations","org.apache.hadoop.io.serializer.WritableSerialization,org.apache.hadoop.io.serializer.JavaSerialization");
		nutchDefault.put("solr.mapping.file","solrindex-mapping.xml");
		nutchDefault.put("solr.commit.size","250");
		nutchDefault.put("solr.commit.index","true");
		nutchDefault.put("solr.auth","false");
		nutchDefault.put("elastic.host","");
		nutchDefault.put("elastic.port","9300");
		nutchDefault.put("elastic.cluster","");
		nutchDefault.put("elastic.index","nutch");
		nutchDefault.put("elastic.max.bulk.docs","250");
		nutchDefault.put("elastic.max.bulk.size","2500500");
		nutchDefault.put("storage.data.store.class","org.apache.gora.memory.store.MemStore");
		nutchDefault.put("storage.schema.webpage","webpage");
		nutchDefault.put("storage.schema.host","host");
		nutchDefault.put("storage.crawl.id","");
		nutchDefault.put("gora.buffer.read.limit","10000");
		nutchDefault.put("gora.buffer.write.limit","10000");
	}
	
	private Properties properties;
	
	public NutchConfig() {
		this.properties = new Properties();
//		for(Entry<String, String> entry: nutchDefault.entrySet()) {
//			this.properties.put(entry.getKey(), entry.getValue());
//		}
	}
	
	public void addNutchCommonProperties(long numTasks, long debug) {
    	if (debug == 1) {
        	properties.setProperty("yarn.app.mapreduce.am.log.level", "DEBUG");
        	properties.setProperty("mapreduce.map.log.level", "DEBUG");
        	properties.setProperty("mapreduce.reduce.log.level", "DEBUG");
    	}
    	properties.setProperty("mapred.reduce.tasks", String.valueOf(numTasks));
    	properties.setProperty("mapred.reduce.tasks.speculative.execution", "false");
    	properties.setProperty("mapred.map.tasks.speculative.execution", "false");
    	properties.setProperty("mapred.compress.map.output", "true");
	}
	
	public void setNutchStoreHbase() {
		this.properties.put("storage.data.store.class", "org.apache.gora.hbase.store.HBaseStore");
	}
	
	public void setLog4jPropertiesFile(String log4jPropertiesFile) {
		if (log4jPropertiesFile == null || log4jPropertiesFile.trim().isEmpty()) {
			log4jPropertiesFile = "my-container-log4j.properties";
		}
		this.properties.put("mapreduce.job.log4j-properties-file", log4jPropertiesFile);
		setLog4jJavaOpts(log4jPropertiesFile);
	}
	
	public void setMapredChildJavaOpts(String opts) {
		this.properties.put("mapred.child.java.opts", opts);
	}

	private void setLog4jJavaOpts(String log4jPropertiesFile) {
		String opts = this.properties.getProperty("mapred.child.java.opts", "");
		opts += " -Dlog4j.configuration=" + log4jPropertiesFile;
		setMapredChildJavaOpts(opts);
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}
