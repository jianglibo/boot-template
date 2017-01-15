package hello.store;

import org.kitesdk.data.Formats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.hadoop.store.DataStoreWriter;
import org.springframework.data.hadoop.store.dataset.AvroPojoDatasetStoreWriter;
import org.springframework.data.hadoop.store.dataset.DatasetDefinition;
import org.springframework.data.hadoop.store.dataset.DatasetOperations;
import org.springframework.data.hadoop.store.dataset.DatasetRepositoryFactory;
import org.springframework.data.hadoop.store.dataset.DatasetTemplate;

import hello.store.dataset.FileInfo;

@Configuration
@ImportResource("classpath:hadoop-config.xml")
public class DatasetConfig {

	private @Autowired org.apache.hadoop.conf.Configuration hadoopConfiguration;
	
	 @Bean
	  public DatasetOperations datasetOperations() {
	    DatasetTemplate datasetOperations = new DatasetTemplate();
	    datasetOperations.setDatasetRepositoryFactory(datasetRepositoryFactory());
	    return datasetOperations;
	  }

  @Bean
  public DatasetRepositoryFactory datasetRepositoryFactory() {
    DatasetRepositoryFactory datasetRepositoryFactory = new DatasetRepositoryFactory();
    datasetRepositoryFactory.setConf(hadoopConfiguration);
    datasetRepositoryFactory.setBasePath("/user/" + System.getProperty("user.name"));
    datasetRepositoryFactory.setNamespace("default");
    return datasetRepositoryFactory;
  }

  @Bean
  public DatasetDefinition fileInfoDatasetDefinition() {
    DatasetDefinition definition = new DatasetDefinition();
    definition.setFormat(Formats.AVRO.getName());
    definition.setTargetClass(FileInfo.class);
    definition.setAllowNullValues(false);
    return definition;
  }
  
  @Bean
  public DataStoreWriter<FileInfo> dataStoreWriter() {
    return new AvroPojoDatasetStoreWriter<FileInfo>(FileInfo.class,
        datasetRepositoryFactory(), fileInfoDatasetDefinition());
  }
  
  
}