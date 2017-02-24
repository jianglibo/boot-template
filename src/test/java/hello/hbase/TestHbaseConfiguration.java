package hello.hbase;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;

//import org.apache.gora.hbase.store.HBaseStore;
//import org.apache.gora.query.Query;
//import org.apache.gora.query.Result;
//import org.apache.gora.store.DataStore;
//import org.apache.gora.store.DataStoreFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
//import org.apache.nutch.storage.WebPage;
//import org.apache.nutch.util.NutchConfiguration;
//import org.apache.nutch.util.TableUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;

import com.beust.jcommander.internal.Maps;
import com.google.common.collect.Lists;
import com.google.inject.name.Named;

import hello.Tbase;
import hello.batch.nutch.FamilyQualifies;

public class TestHbaseConfiguration extends Tbase {
	
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	private static final String FHGOV_WEBPAGE = "fhgov_webpage";
	
	@Autowired
	private HbaseTemplate template;
	
	@Autowired
	private org.apache.hadoop.conf.Configuration conf;
	
//	/opt/hbase/hbase-1.2.4/bin/hbase shell
//	scan 'fhgov_webpage'
//	count 'fhgov_webpage'
//	get  '/user/user01/customer', 'jsmith'
//	get  '/user/user01/customer', 'jsmith', {COLUMNS=>['addr']} 
//	get  '/user/user01/customer', 'jsmith', {COLUMNS=>['order:numb']}

//	@Test
//	public void t() {
//		List<String[]> rows = template.find("fhgov_webpage", "f", new RowMapper<String[]>() {
//			  @Override
//			  public String[] mapRow(org.apache.hadoop.hbase.client.Result result, int rowNum) throws Exception {
//			      NavigableMap<byte[], byte[]> familyMap = result.getFamilyMap("f".getBytes());
//			      String[] Quantifers = new String[familyMap.size()];
//
//			      int counter = 0;
//			      for(byte[] bQunitifer : familyMap.keySet())
//			      {
//			          Quantifers[counter++] = Bytes.toString(bQunitifer);
//
//			      }
//			      return Quantifers;
//			  }
//			});
//		rows.forEach(r -> System.out.println(r));
//		
//	}
	
	@Test
	public void listTable() throws MasterNotRunningException, ZooKeeperConnectionException, IOException {
		Admin admin = ConnectionFactory.createConnection(conf).getAdmin();
		HTableDescriptor[] tableDescriptor = admin.listTables();
		List<String> tns = Lists.newArrayList();
		Map<String, HColumnDescriptor[]> familiesMap = Maps.newHashMap();
		
	    for (int i=0; i<tableDescriptor.length;i++ ){
	       String tn = tableDescriptor[i].getNameAsString();
	       familiesMap.put(tn, tableDescriptor[i].getColumnFamilies());
	       tns.add(tn);
	       System.out.println(tn);
	       for( HColumnDescriptor hd : familiesMap.get(tn)) {
	    	   System.out.println(hd.getNameAsString());
	    	   
	       }
	    }
	    assertTrue("table should contains fhogv_webpage.", tns.contains(FHGOV_WEBPAGE));
	}
	
	@Test
	public void scanFetchFamily() throws MasterNotRunningException, ZooKeeperConnectionException, IOException {
		Table table = ConnectionFactory.createConnection(conf).getTable(TableName.valueOf(FHGOV_WEBPAGE));
		Scan scan = new Scan();
		scan.setMaxResultSize(1L);
		ResultScanner resultScanner =  table.getScanner(scan);
		Result result = resultScanner.iterator().next();
		NavigableMap<byte[], byte[]> qualifier2Value =  result.getFamilyMap(FamilyQualifies.FetchQualify.FAMILY_NAME);
		assertThat("column number of 'f' family shoud be" ,qualifier2Value.keySet().size() , equalTo(13));
		
		
		for(Entry<byte[], byte[]> entry: qualifier2Value.entrySet()) {
			System.out.println(FamilyQualifies.FetchQualify.getFieldNameByQualifyBytes(entry) + ":");
			System.out.println(FamilyQualifies.FetchQualify.getValue(entry));
		}
	}
	
	

//	f
//	h
//	il
//	mk
//	mtdt
//	ol
//	p
//	s
	
//	@Test
//	public void tGora() {
//		Configuration c = new Configuration(NutchConfiguration.create());
//		try {
//			DataStore<String, WebPage> store = DataStoreFactory.createDataStore(HBaseStore.class, String.class, WebPage.class, c, "fhgov_webpage");
//			Query<String, WebPage> q = store.newQuery();
//			
////			String key = "http://gtob.ningbo.gov.cn/art/2016/6/1/art_249_371742.html";
////			key = TableUtil.reverseUrl(key);
////			q.setLimit(1);
////			q.setStartKey(key);
//			
//			Result<String, WebPage> result = q.execute();
//			
//			while (result.next()) {
//				result.getKey();
//				WebPage wp = result.get();
//				LOGGER.info("\n fetch time: {},\n batchId: {},\n fetch interval: {},\n",
//						Instant.ofEpochMilli(wp.getFetchTime()).toString(), //
//						wp.getBatchId(), //
//						wp.getFetchInterval()//
//				);
//			}
//			
//		} catch (Exception e) {
//			throw new IllegalStateException("Cannot create webstore!", e);
//		}
//	}
}
