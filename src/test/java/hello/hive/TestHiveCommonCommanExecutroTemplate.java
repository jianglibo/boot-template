package hello.hive;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import hello.TbatchBase;
import hello.util.FsUtil;

public class TestHiveCommonCommanExecutroTemplate extends TbatchBase {

	@Autowired
	private HiveCommonCommandExecutor bwht;
	
	@Autowired
	private HiveCommonCommandExecutor hct;
	
	@Autowired
	private FsUtil fsUtil;
	
	public TestHiveCommonCommanExecutroTemplate() {
		super(null);
	}
	
	@Before
	public void be() {
		bwht.dropTable("createWithLiteral");
		bwht.dropTable("createWithUrl");
	}
	
	@After
	public void af() {
//		bwht.dropTable("createWithLiteral");
//		bwht.dropTable("createWithUrl");
	}

	
	@Test
	public void tCreateTable() throws IOException {
		List<String> tbs = bwht.showTable();
		String script = hct.avroSchemaLiteral("createWithLiteral", "hdfs:" + fsUtil.convertToUserPath("default/fileinfo/.metadata/schema.avsc").toString());
		bwht.execute(script);
		assertThat(bwht.showTable().size() - tbs.size(), equalTo(1));
		
		tbs = bwht.showTable();
		hct.avroSchemaUrl("createWithUrl", fsUtil.convertToFullUserPath("default/fileinfo/.metadata/schema.avsc"));
		bwht.execute(script);
		assertThat(bwht.showTable().size() - tbs.size(), equalTo(1));
		
	}
}
