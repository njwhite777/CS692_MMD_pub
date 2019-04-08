package mmd;
import java.util.Arrays;
import java.util.HashSet;

public final class Constants {
	
	public static final class Headers {
		
		private static final HashSet<String> ALL_HEADER_ITEMS = new HashSet<String>(Arrays.asList(
			new String[]{"METRIC","TYPE2","XMI_ID","INT","ENUM","CLONE_MIXED_TYPE","ENDING_LINENO","CWE","TEST_NUMBER","CLONE_TYPE","NAME","ID","FILENAME","CHANGE","ENUM2","CLONE_ID","CATEGORY","XMLTAG","SIGNATURE","TYPE","DMA_ID","CHAR","EFFORT","CLONE","BEGINNING_LINENO","VULNERABILITY","BENCHMARK_FILENAME","BENNCHMARK_VERSION"}
		));
			
		private static final HashSet<String> INT_TYPE_HEADER_ITEMS = new HashSet<String>(Arrays.asList(
			new String[] {"METRIC","INT","CLONE_MIXED_TYPE","ENDING_LINENO","CWE","CLONE_TYPE","CHANGE","XMLTAG","EFFORT","CLONE","BEGINNING_LINENO","VULNERABILITY"}
		));
		
		private static final HashSet<String> INT_TYPE_ZERO_OR_ONE_VALUE = new HashSet<String>(Arrays.asList(
			new String[] {"CLONE_MIXED_TYPE","CLONE_TYPE","CLONE","VULNERABILITY"}
		));
				
		private static HashSet<String> UNLIMITED_APPEAR_HEADER_ITEMS = new HashSet<String>(Arrays.asList(
			new String[] {"METRIC","INT","XMLTAG","CHAR"}
		));

		private static HashSet<String> IDENTIFICATION_FIELDS = new HashSet<String>(Arrays.asList(
			new String[] {"NAME","ID","DMA_ID","XML_ID","CLONE_ID","TEST_NUMBER"}
		));
			
		private static HashSet<String> TWO_PART_HEADER = new HashSet<String>(Arrays.asList(
			new String[] { "METRIC","XMLTAG"}
		));
			
		private static HashSet<String> NOT_ZERO_OR_ONE_APPEARANCE = new HashSet<String>(Arrays.asList(
			new String[] {"METRIC","INT","XMLTAG","CHAR"}
		));
		
		private static HashSet<String> PROCESS_DATA = new HashSet<String>(Arrays.asList(
				new String[] {"CWE","CATEGORY","VULNERABILITY"}
			));
			
		@SuppressWarnings("unlikely-arg-type")
		private static HashSet<String> getSubset(HashSet<String> superset, HashSet<String> subset) {
			HashSet<String> t = (HashSet<String>) superset.clone();
			t.removeAll(subset);
			return t;
		}
		
		public static HashSet<String> getProcessData(){
			return PROCESS_DATA;
		}
			
		public static HashSet<String> getIdentificationFields(){
			return IDENTIFICATION_FIELDS;				
		}
			
		public static HashSet<String> getOnePartHeaderItems(){
			return getSubset(ALL_HEADER_ITEMS,TWO_PART_HEADER);
		}
		
		public static HashSet<String> getTwoPartHeaderItems(){
			return TWO_PART_HEADER;
		}
		
		public static HashSet<String> getIntTypeHeaderItems(){
			return INT_TYPE_HEADER_ITEMS;
		}

//		public static HashSet<String> getStringTypeHeaderItems(){
//			return getSubset(ALL_HEADER_ITEMS,INT_TYPE_HEADER_ITEMS);
//		}
//		
//		public static HashSet<String> getZeroOrOneAppearance(){
//			return getSubset(ALL_HEADER_ITEMS, NOT_ZERO_OR_ONE_APPEARANCE);
//		}
//		
//		public static HashSet<String> getUnlimitedAppearanceHeaderItems(){
//			return UNLIMITED_APPEAR_HEADER_ITEMS;
//		}	
		
		public static HashSet<String> getLimitOneAppearanceHeaderItems(){
			return getSubset(ALL_HEADER_ITEMS, UNLIMITED_APPEAR_HEADER_ITEMS);
		}
	
		public static HashSet<String> getAllHeaderItems(){
			return ALL_HEADER_ITEMS;
		}

		public static HashSet<String> getIntTypeZeroOrOneValue(){
			return INT_TYPE_ZERO_OR_ONE_VALUE;
		}
	}
	
}
