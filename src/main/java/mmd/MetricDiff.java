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
				
		public HashMap<String, Object> getOM1() {
			return this.om1;
		}
		
		public HashMap<String, Object> getOM2() {
			return this.om2;
		}
		
		public void addDifference(Object m1,Object m2,String headerValue) {
			this.om1.put(headerValue, m1);
			this.om2.put(headerValue, m2);
			this.headerItems.add(headerValue);
			this.difference++;
		}
	}