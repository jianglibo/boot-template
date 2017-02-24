package hello.batch.nutch;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.avro.io.DatumReader;
import org.apache.hadoop.hbase.util.Bytes;

public class FamilyQualifies {
	
	public static final Map<ByteBuffer, FetchQualify> bytes2fetchQualifyEnum = new HashMap<ByteBuffer, FamilyQualifies.FetchQualify>();
	
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
					return Bytes.toString(entry.getValue());
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
