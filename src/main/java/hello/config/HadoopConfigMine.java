package hello.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.hadoop.config.annotation.EnableHadoop;
import org.springframework.data.hadoop.config.annotation.SpringHadoopConfigurerAdapter;

@Configuration
@ImportResource(locations={"classpath:hadoop-config.xml"})
@EnableHadoop
public class HadoopConfigMine extends SpringHadoopConfigurerAdapter {

}
