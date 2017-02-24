package hello.batch.nutch;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestFamilyQualifies {
	
	@Test
	public void t() {
		assertTrue(FamilyQualifies.FetchQualify.valueOf("BASE_URL") == FamilyQualifies.FetchQualify.BASE_URL);
		assertThat(FamilyQualifies.bytes2fetchQualifyEnum.size(), equalTo(13));
	}

}
