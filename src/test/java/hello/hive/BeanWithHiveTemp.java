package hello.hive;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hive.HiveClient;
import org.springframework.data.hadoop.hive.HiveClientCallback;
import org.springframework.data.hadoop.hive.HiveTemplate;
import org.springframework.stereotype.Component;


@Component
public class BeanWithHiveTemp {

	  private HiveTemplate template;

	  public List<String> getDbs() {
	      return template.execute(new HiveClientCallback<List<String>>() {
	         @Override
	         public List<String> doInHive(HiveClient hiveClient) throws Exception {
	            return hiveClient.execute("show databases");
	         }
	      });
	  }
	  
	  @Autowired
	  public void setHiveTemplate(HiveTemplate template) { this.template = template; }
}
