package hello.hive;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import hello.batch.TbatchBase;

public class TestHiveTemplate extends TbatchBase {

	@Autowired
	private BeanWithHiveTemp bwht;
	
	public TestHiveTemplate() {
		super(null);
	}

	@Test
	public void t() {
		List<String> dbs = bwht.getDbs();
		assertThat("there should 2", dbs.size(), equalTo(2));
	}
}
