package mmd;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;

public class TestModuleBaseClass {
	
	
	@Test
	public void testModuleGetID() throws Exception {
    	MMD tmmd = new MMD("./test/goodExample/output");
    	tmmd.setDMAFmt(
    			"NAME\n"+
    			"METRIC CC\n"+
    			"METRIC IFS\n"+
    			"VULNERABILITY"
    			);
    	tmmd.setDataDMA("module1 1 1 1");
    	tmmd.doAnalysis();
    	assertTrue(tmmd.getModules().size() == 1);
    	HashMap<String, Module> mbc =  tmmd.getModules();
    	ModuleBaseClass m = mbc.values().iterator().next();
   	
   		assertTrue(m.getSelf().getName().equals("module1"));
   		assertTrue(m.getIdField("NAME").equals("module1"));
   		try {
   			m.getIdField("DERP");
   		}catch(Exception e) {}
	}
}
