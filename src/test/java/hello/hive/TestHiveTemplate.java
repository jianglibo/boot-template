package hello.hive;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import hello.batch.TbatchBase;
import hello.util.FsUtil;

public class TestHiveTemplate extends TbatchBase {

	@Autowired
	private HiveExecutor bwht;
	
	@Autowired
	private HiveCreateTable hct;
	
	@Autowired
	private FsUtil fsUtil;
	
	public TestHiveTemplate() {
		super(null);
	}
	
	@Before
	public void be() {
		bwht.dropTable("createWithLiteral");
	}
	
	@After
	public void af() {
		bwht.dropTable("createWithLiteral");
	}

	@Test
	public void t() {
		
	}
	
	@Test
	public void tCreateTable() throws IOException {
		List<String> tbs = bwht.showTable();
		String script = hct.avroSchemaLiteral("createWithLiteral", "hdfs:" + fsUtil.convertToUserPath("default/fileinfo/.metadata/schema.avsc").toString());
		List<String> ss = bwht.execute(script);
		ss.forEach(System.out::println);
		assertThat(bwht.showTable().size() - tbs.size(), equalTo(1));
		
	}
}
