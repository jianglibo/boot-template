package hello.hive;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.hadoop.hive.HiveClient;
import org.springframework.data.hadoop.hive.HiveClientCallback;
import org.springframework.data.hadoop.hive.HiveTemplate;
import org.springframework.stereotype.Component;

import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;


@Component
public class HiveCommonCommandExecutor {

	  private HiveTemplate template;
	  
	  public static String ROW_FORMAT_SERDE = "'org.apache.hadoop.hive.serde2.avro.AvroSerDe' ";
	  public static String INPUTFORMAT = "'org.apache.hadoop.hive.ql.io.avro.AvroContainerInputFormat' ";
	  public static String OUTPUTFORMAT = "'org.apache.hadoop.hive.ql.io.avro.AvroContainerOutputFormat' ";

	  @Autowired
	  public void setHiveTemplate(HiveTemplate template) { this.template = template; }
	  
	  
	  @Autowired
	  private ApplicationContext applicationContext;

	  public List<String> showTable() {
		  return showTable("default");
	  }
	  
	  public List<String> showTable(String dbName) {
		  return execute(String.format("SHOW TABLES IN %s", dbName));
	  }
	  
	  public List<String> dropTable(String tableName) {
		  return execute("DROP TABLE IF EXISTS " + tableName);
	  }
	  
	  public List<String> execute(String script) {
		  return template.execute(new HiveClientCallback<List<String>>() {
			@Override
			public List<String> doInHive(HiveClient hiveClient) throws Exception {
				return hiveClient.execute(script);
			}
		});
	  }
	  
		private StringBuffer avroSchema(String tableName) {
			StringBuffer sb = new StringBuffer("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" ");
			sb.append("ROW FORMAT SERDE ").append(ROW_FORMAT_SERDE);
			sb.append("STORED as INPUTFORMAT ").append(INPUTFORMAT);
			sb.append("OUTPUTFORMAT ").append(OUTPUTFORMAT);
			return sb;
		}
		
		public String avroSchemaLiteral(String tableName, String resourceStr) throws IOException {
			StringBuffer sb = avroSchema(tableName);
			InputStream is = applicationContext.getResource(resourceStr).getInputStream();
			String c = new String(ByteStreams.toByteArray(is));
			is.close();
			c = String.join("", CharStreams.readLines(new StringReader(c)));
			sb.append("TBLPROPERTIES ('avro.schema.literal'='").append(c.replaceAll("\\\\?'", "\\\\'")).append("')"); // ending witch ";" will cause error.
			return sb.toString();
		}
		
		public String avroSchemaUrl(String tableName, String url) throws IOException {
			StringBuffer sb = avroSchema(tableName);
			sb.append("TBLPROPERTIES ('avro.schema.url'='").append(url).append("')"); // ending witch ";" will cause error.
			return sb.toString();
		}
	  
}
