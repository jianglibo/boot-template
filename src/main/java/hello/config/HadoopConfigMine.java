package hello.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.config.annotation.EnableHadoop;
import org.springframework.data.hadoop.config.annotation.SpringHadoopConfigurerAdapter;

@Configuration
@EnableHadoop
public class HadoopConfigMine extends SpringHadoopConfigurerAdapter {

}
