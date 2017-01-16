package hello.store;

import org.kitesdk.data.Formats;
import org.kitesdk.data.PartitionStrategy;
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
//    definition.setPartitionStrategy(new PartitionStrategy.Builder().year("modified").month("modified").build()); // /year=2015/month=01/u-u-i-d.avro style files will be created.
    definition.setPartitionStrategy(new PartitionStrategy.Builder().dateFormat("modified", "Y_M", "yyyyMM").build()); // /Y_M=201501/u-u-i-d.avro style directories will be created.
    return definition;
  }
  
  @Bean
  public DataStoreWriter<FileInfo> dataStoreWriter() {
    AvroPojoDatasetStoreWriter<FileInfo> ws = new AvroPojoDatasetStoreWriter<FileInfo>(FileInfo.class,
        datasetRepositoryFactory(), fileInfoDatasetDefinition());
    
    return ws;
  }
  
  
}