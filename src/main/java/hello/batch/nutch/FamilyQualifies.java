package hello.batch.nutch;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.SeekableByteArrayInput;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.hadoop.hbase.util.Bytes;

public class FamilyQualifies {
	
	public static final Map<ByteBuffer, FetchQualify> bytes2fetchQualifyEnum = new HashMap<ByteBuffer, FamilyQualifies.FetchQualify>();
	
	public static final Schema protocolStatusSchema = new org.apache.avro.Schema.Parser()
		      .parse("{\"type\":\"record\",\"name\":\"ProtocolStatus\",\"namespace\":\"org.apache.nutch.storage\",\"doc\":\"A nested container representing data captured from web server responses.\",\"fields\":[{\"name\":\"code\",\"type\":\"int\",\"doc\":\"A protocol response code which can be one of SUCCESS - content was retrieved without errors, FAILED - Content was not retrieved. Any further errors may be indicated in args, PROTO_NOT_FOUND - This protocol was not found. Application may attempt to retry later, GONE - Resource is gone, MOVED - Resource has moved permanently. New url should be found in args, TEMP_MOVED - Resource has moved temporarily. New url should be found in args., NOTFOUND - Resource was not found, RETRY - Temporary failure. Application may retry immediately., EXCEPTION - Unspecified exception occured. Further information may be provided in args., ACCESS_DENIED - Access denied - authorization required, but missing/incorrect., ROBOTS_DENIED - Access denied by robots.txt rules., REDIR_EXCEEDED - Too many redirects., NOTFETCHING - Not fetching., NOTMODIFIED - Unchanged since the last fetch., WOULDBLOCK - Request was refused by protocol plugins, because it would block. The expected number of milliseconds to wait before retry may be provided in args., BLOCKED - Thread was blocked http.max.delays times during fetching.\",\"default\":0},{\"name\":\"args\",\"type\":{\"type\":\"array\",\"items\":\"string\"},\"doc\":\"Optional arguments supplied to compliment and/or justify the response code.\",\"default\":[]},{\"name\":\"lastModified\",\"type\":\"long\",\"doc\":\"A server reponse indicating when this page was last modified, this can be unreliable at times hence this is used as a default fall back value for the preferred 'modifiedTime' and 'preModifiedTime' obtained from the WebPage itself.\",\"default\":0}]}");
	
	public static final String PS_FIELD_CODE = "code";
	public static final String PS_FIELD_ARGS = "args";
	public static final String PS_FIELD_LASTMODIFIED = "lastModified";
	
	public static class ProtocalStatus {
		private int code;
		private java.util.List<java.lang.CharSequence> args;
		private long lastModified;
		public int getCode() {
			return code;
		}
		public void setCode(int code) {
			this.code = code;
		}
		public java.util.List<java.lang.CharSequence> getArgs() {
			return args;
		}
		public void setArgs(java.util.List<java.lang.CharSequence> args) {
			this.args = args;
		}
		public long getLastModified() {
			return lastModified;
		}
		public void setLastModified(long lastModified) {
			this.lastModified = lastModified;
		}
	}
	
	// only for test purpose.
	private static void writeBytesToFile(byte[] bytes) {
		Path p = Paths.get("./src/test/java/hello/hbase/protocolExample.avro");
		try {
			Files.write(p, bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// it doesn't work.
	protected static ProtocalStatus parseProtocolStatus(byte[] data) {
//		writeBytesToFile(data);
		GenericRecord gr = null;
		try {
			DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>(protocolStatusSchema);
			DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(new SeekableByteArrayInput(data), datumReader);
			if (dataFileReader.hasNext()) {
			// Reuse user object by passing it to next(). This saves us from
			// allocating and garbage collecting many objects for files with
			// many items.
			 gr = dataFileReader.next(gr);
			}
			dataFileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ProtocalStatus ps = null;
		if (gr != null) {
			ps = new ProtocalStatus();
			ps.setArgs((List<CharSequence>) gr.get(PS_FIELD_ARGS));
			ps.setCode((int) gr.get(PS_FIELD_CODE));
			ps.setLastModified((long) gr.get(PS_FIELD_LASTMODIFIED));
		}
		return ps;
	}
	
	public static enum FetchQualify {
	    BASE_URL("baseUrl", "bas")
	    , STATUS("status", "st")
	    , PREV_FETCH_TIME("prevFetchTime", "pts")
	    , FETCH_TIME("fetchTime", "ts")
	    , FETCH_INTERVAL("fetchInterval", "fi")
	    , RETRIES_SINCE_FETCH("retriesSinceFetch", "rsf")
	    , REPR_URL("reprUrl","rpr")
	    , CONTENT("content","cnt")
	    , CONTENT_TYPE("contentType", "typ")
	    , PROTOCOL_STATUS("protocolStatus", "prot")
	    , MODIFIED_TIME("modifiedTime", "mod")
	    , PREV_MODIFIED_TIME("prevModifiedTime", "pmod")
	    , BATCH_ID("batchId", "bid")
	    ,;
		
		public static final byte[] FAMILY_NAME = Bytes.toBytes("f");
		
		private String fieldName;
		private byte[] qualify;
		
		FetchQualify(String fieldName, String qualify) {
			this.fieldName = fieldName;
			this.qualify = Bytes.toBytes(qualify);
			bytes2fetchQualifyEnum.put(ByteBuffer.wrap(this.qualify), this);
		}
		
		public static String getFieldNameByQualifyBytes(Entry<byte[], byte[]> entry) {
			return bytes2fetchQualifyEnum.get(ByteBuffer.wrap(entry.getKey())).getFieldName();
		}

		public String getFieldName() {
			return fieldName;
		}
		
		public static Object getValue(Entry<byte[], byte[]> entry) {
			FetchQualify fq = bytes2fetchQualifyEnum.get(ByteBuffer.wrap(entry.getKey()));
			if (fq == null) {
				return null;
			} else {
				switch (fq) {
				case BASE_URL:
					return Bytes.toString(entry.getValue());
				case BATCH_ID:
					return Bytes.toString(entry.getValue());
				case CONTENT:
					return Bytes.toString(entry.getValue());
				case CONTENT_TYPE:
					return Bytes.toString(entry.getValue());
				case FETCH_INTERVAL:
					return Bytes.toInt(entry.getValue());
				case FETCH_TIME:
					return new Date(Bytes.toLong(entry.getValue()));
				case MODIFIED_TIME:
					return new Date(Bytes.toLong(entry.getValue()));
				case PREV_FETCH_TIME:
					return new Date(Bytes.toLong(entry.getValue()));
				case PREV_MODIFIED_TIME:
					return new Date(Bytes.toLong(entry.getValue()));
				case PROTOCOL_STATUS:
					return parseProtocolStatus(entry.getValue());
//					return Bytes.toString(entry.getValue());
				case REPR_URL:
					return Bytes.toString(entry.getValue());
				case RETRIES_SINCE_FETCH:
					return Bytes.toInt(entry.getValue());
				case STATUS:
					return Bytes.toInt(entry.getValue());
				default:
					return null;
				}
			}
		}
		
//		private

		public byte[] getQualify() {
			return qualify;
		}
	}
	
	public static enum ParseQualify {
		TITLE("title", "t")
		, TEXT("text", "c")
		, PARSE_STATUS("parseStatus", "st")
		, SIGNATURE("signature", "sig")
	    , PREV_SIGNATURE("prevSignature", "psig")
	    ,;
		public static final byte[] FAMILY_NAME = Bytes.toBytes("p");
		private String fieldName;
		private byte[] qualify;
		
		ParseQualify(String fieldName, String qualify) {
			this.fieldName = fieldName;
			this.qualify = Bytes.toBytes(qualify);
		}

		public String getFieldName() {
			return fieldName;
		}

		public byte[] getQualify() {
			return qualify;
		}
	}
	
	public static enum ScoreQualify {
	    SCORE("score", "s")
	    ,;

		public static final byte[] FAMILY_NAME = Bytes.toBytes("s");
		private String fieldName;
		private byte[] qualify;
		
		ScoreQualify(String fieldName, String qualify) {
			this.fieldName = fieldName;
			this.qualify = Bytes.toBytes(qualify);
		}

		public String getFieldName() {
			return fieldName;
		}

		public byte[] getQualify() {
			return qualify;
		}
	}
	
	public static class OtherFamilies {
		public static final byte[] HEADERS = Bytes.toBytes("h");
		public static final byte[] IN_LINKS = Bytes.toBytes("il");
		public static final byte[] OUT_LINKS = Bytes.toBytes("ol");
		public static final byte[] META_DATA = Bytes.toBytes("mtdt");
		public static final byte[] MARKS = Bytes.toBytes("mk");
	}
}
