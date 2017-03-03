package hello.hbase;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.junit.Test;

public class TestAvroDeserialize {
	
	private static final Path schemaFile = Paths.get("./src/test/java/hello/hbase/protocolStatus.json");
	private static final Path datumFile = Paths.get("./src/test/java/hello/hbase/protocolExample.avro");

	@Test
	public void t() throws IOException {
		Schema schema = new Schema.Parser().parse(schemaFile.toFile());
		
		// Deserialize users from disk
		DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>(schema);
		DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(datumFile.toFile(), datumReader);
		GenericRecord user = null;
		while (dataFileReader.hasNext()) {
		// Reuse user object by passing it to next(). This saves us from
		// allocating and garbage collecting many objects for files with
		// many items.
		user = dataFileReader.next(user);
		System.out.println(user);
		}
	}
}
