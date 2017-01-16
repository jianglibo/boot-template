package hello.util;

import org.apache.hadoop.fs.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HiveCreateTable {
	
	public static String ROW_FORMAT_SERDE = "'org.apache.hadoop.hive.serde2.avro.AvroSerDe' ";
	public static String INPUTFORMAT = "'org.apache.hadoop.hive.ql.io.avro.AvroContainerInputFormat' ";
	public static String OUTPUTFORMAT = "'org.apache.hadoop.hive.ql.io.avro.AvroContainerOutputFormat' ";
	
	@Autowired
	private FileSystem fs;
	
	@Autowired
	private FsUtil fsUti;
	
	public String avroSchemaLiteral(String tableName, String avscUrl) {
		StringBuffer sb = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
		sb.append("ROW FORMAT SERDE ").append(ROW_FORMAT_SERDE);
		sb.append("STORED as INPUTFORMAT ").append(INPUTFORMAT);
		sb.append("OUTPUTFORMAT ").append(OUTPUTFORMAT);
		sb.append("TBLPROPERTIES").append(avscUrl);
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
