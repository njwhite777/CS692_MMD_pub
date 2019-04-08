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
		
		.
		.
		.
	}
	
}
