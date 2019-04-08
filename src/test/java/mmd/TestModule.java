package mmd;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestModule {
	
	@Test
	public void testModuleMethods() throws Exception {
    	MMD tmmd = new MMD("./test/goodExample/output");
    	tmmd.setDMAFmt(
    			"NAME\n"+
    			"METRIC CC\n"+
    			"METRIC IFS\n"+
    			"CATEGORY\n" +
    			"VULNERABILITY"
    			);
    	tmmd.setDataDMA(
    			"module1 1 1 sqli 1\n"
    	);
    	tmmd.doAnalysis();
    	Module mod = tmmd.getModules().values().iterator().next();
    	assertTrue(mod.getClone()==null);
    	assertTrue(mod.getFileName().equals("TESTFILE.dma"));
    	tmmd.buildOutput();
	}
	
	
}
