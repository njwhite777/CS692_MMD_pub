package mmd;
import java.util.HashMap;
import java.util.HashSet;

public class MetricDiff{
		
		String id;
		public int difference;
		HashSet<String> headerItems;
		HashMap<String,HashMap<String,Object>> moduleDifferences;
		public HashMap<String,Module> modules;
		
		private HashMap<String, Object> om1;
		private HashMap<String, Object> om2;
		
		public MetricDiff(Module m1,Module m2) {
			this.id = "";
			this.headerItems = new HashSet<String>();
			this.moduleDifferences = new HashMap<String,HashMap<String,Object>>();
			this.om1 =new HashMap<String,Object>();
			this.om2 =new HashMap<String,Object>();
			this.moduleDifferences.put(m1.getId(),om1);
			this.moduleDifferences.put(m2.getId(),om2);
			this.modules = new HashMap<String,Module>();
			this.modules.put(m1.getId(),m1);
			this.modules.put(m2.getId(),m2);
		}
				
		.
		.
		.
	}