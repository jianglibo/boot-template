package hello.util;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import hello.TbatchBase;

public class TestNutchHdpFolder extends TbatchBase {
	
	@Autowired
	private HdpFolderUtil nutchFolderUtil;
	
	@Test
	public void t() {
		String s = nutchFolderUtil.getRemoteFileURIString("abc/xx.txt");
		assertThat(s, equalTo(nutchFolderUtil.getBaseFolder() + "/xx.txt"));
	}

}
