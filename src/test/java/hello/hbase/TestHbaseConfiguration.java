package hello.hbase;

import java.util.List;

import org.apache.hadoop.hbase.client.Result;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;

import hello.Tbase;

public class TestHbaseConfiguration extends Tbase {
	
	@Autowired
	private HbaseTemplate template;

	@Test
	public void t() {
		List<String> rows = template.find("MyTable", "SomeColumn", new RowMapper<String>() {
			  @Override
			  public String mapRow(Result result, int rowNum) throws Exception {
			    return result.toString();
			  }
			});
	}
}
