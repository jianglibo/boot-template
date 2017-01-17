package hello.hive;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hive.HiveClient;
import org.springframework.data.hadoop.hive.HiveClientCallback;
import org.springframework.data.hadoop.hive.HiveTemplate;
import org.springframework.stereotype.Component;


@Component
public class HiveExecutor {

	  private HiveTemplate template;

	  @Autowired
	  public void setHiveTemplate(HiveTemplate template) { this.template = template; }

	  public List<String> showTable() {
		  return showTable("default");
	  }
	  
	  public List<String> showTable(String dbName) {
		  return execute(String.format("SHOW TABLES IN %s", dbName));
	  }
	  
	  public List<String> dropTable(String dbName) {
		  return execute("DROP TABLE IF EXISTS " + dbName);
	  }
	  
	  public List<String> execute(String script) {
		  return template.execute(new HiveClientCallback<List<String>>() {
			@Override
			public List<String> doInHive(HiveClient hiveClient) throws Exception {
				return hiveClient.execute(script);
			}
		});
	  }
	  
}
