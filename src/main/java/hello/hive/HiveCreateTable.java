package hello.hive;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;

@Component
public class HiveCreateTable {
	
	public static String ROW_FORMAT_SERDE = "'org.apache.hadoop.hive.serde2.avro.AvroSerDe' ";
	public static String INPUTFORMAT = "'org.apache.hadoop.hive.ql.io.avro.AvroContainerInputFormat' ";
	public static String OUTPUTFORMAT = "'org.apache.hadoop.hive.ql.io.avro.AvroContainerOutputFormat' ";
	
	private  Pattern ptn = Pattern.compile(".*?'");
	
	@Autowired
	private ApplicationContext applicationContext;
	
	// s.replaceAll("\\\\?'", "\\\\'") can archive this function.
	public String escapeSingleQuota(String origin) {
		Matcher m = ptn.matcher(origin);
		StringBuffer sb = new StringBuffer();
		int end = 0;
		while(m.find()) {
			end = m.end();
			String seg = origin.substring(m.start(), end);
			int len = seg.length();
			if (len == 1) {
				if ('\'' == seg.charAt(0)) {
					sb.append("\\'");
				} else {
					sb.append(seg);
				}
			} else { // len > 1
				sb.append(seg.substring(0, seg.length() - 2));
				char lastcSec = seg.charAt(seg.length() - 2);
				if (lastcSec == '\\') {
					sb.append("\\'");
				} else {
					sb.append(lastcSec);
					sb.append("\\'");
				}
			}
		}
		
		if (end < origin.length()) {
			sb.append(origin.substring(end));
		}
		return sb.toString();
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
	
	
//	CREATE TABLE as_avro
//	  ROW FORMAT SERDE
//	  'org.apache.hadoop.hive.serde2.avro.AvroSerDe'
//	  STORED as INPUTFORMAT
//	  'org.apache.hadoop.hive.ql.io.avro.AvroContainerInputFormat'
//	  OUTPUTFORMAT
//	  'org.apache.hadoop.hive.ql.io.avro.AvroContainerOutputFormat'
//	  TBLPROPERTIES (
//	    'avro.schema.url'='file:///path/to/the/schema/test_serializer.avsc');
}
