package mmd;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;

public class TestClone {

	
	@Test
	public void testModuleGetID() throws Exception {
    	MMD tmmd = new MMD("./test/goodExample/output");
    	tmmd.setDMAFmt(
    			"NAME\n"+
    			"METRIC CC\n"+
    			"METRIC IFS\n"+
    			"CATEGORY\n" +
    			"VULNERABILITY"
    			);
    	tmmd.setDataDMA(
    			"module1 1 1 sqli 1\n" +
    			"module2 1 1 sqli 1\n"
    	);
    	tmmd.doAnalysis();
    	assertTrue(tmmd.getModules().size() == 0);
    	assertTrue(tmmd.getClones().size() == 1);
    	HashMap<String, Clone> c =  tmmd.getClones();
    	
    	Clone cl = c.values().iterator().next();
    	assertTrue(cl.getModuleIDs().size() == 2);
    	assertTrue(cl.getModuleIDs().contains("m0"));
    	assertTrue(cl.getVulnerabilityTypes().contains("sqli"));
    	assertTrue(cl.isClone());
    	assertFalse(cl.isMixedTypeClone());
    	assertTrue(cl.size()==2);
	}
	
	@Test
	public void testMixedClone() throws Exception {
		MMD tmmd = new MMD("./test/goodExample/output");
    	tmmd.setDMAFmt(
    			"NAME\n"+
    			"METRIC CC\n"+
    			"METRIC IFS\n"+
    			"CATEGORY\n" +
    			"VULNERABILITY"
    			);
    	tmmd.setDataDMA(
    			"module1 1 1 ldapi 1\n" +
    			"module2 1 1 sqli 1\n"
    	);
    	tmmd.doAnalysis();
    	HashMap<String, Clone> c =  tmmd.getClones();
    	Clone cl = c.values().iterator().next();
    	assertTrue(cl.isMixedTypeClone());
	}
	
	@Test
	public void testMixedVulnerability() throws Exception {
		MMD tmmd = new MMD("./test/goodExample/output");
    	tmmd.setDMAFmt(
    			"NAME\n"+
    			"METRIC CC\n"+
    			"METRIC IFS\n"+
    			"CATEGORY\n" +
    			"VULNERABILITY"
    			);
    	tmmd.setDataDMA(
    			"module1 1 1 ldapi 1\n" +
    			"module2 1 1 sqli 1\n"
    	);
    	tmmd.doAnalysis();
    	HashMap<String, Clone> c =  tmmd.getClones();
    	Clone cl = c.values().iterator().next();
    	assertFalse(cl.isMixedVulnerabilityClone());
    	
    	MMD tmmd1 = new MMD("./test/goodExample/output");
    	tmmd1.setDMAFmt(
    			"NAME\n"+
    			"METRIC CC\n"+
    			"METRIC IFS\n"+
    			"CATEGORY\n" +
    			"VULNERABILITY"
    			);
    	tmmd1.setDataDMA(
    			"module1 1 1 ldapi 0\n" +
    			"module2 1 1 sqli 1\n"
    	);
    	
    	tmmd1.doAnalysis();
    	HashMap<String, Clone> c1 =  tmmd1.getClones();
    	assertTrue(c1.size()==1);
    	Clone cl1 = c1.values().iterator().next();
    	assertTrue(cl1.isMixedVulnerabilityClone());
	}
	
	@Test
	public void testGetModules() throws Exception {
		MMD tmmd = new MMD("./test/goodExample/output");
    	tmmd.setDMAFmt(
    			"NAME\n"+
    			"METRIC CC\n"+
    			"METRIC IFS\n"+
    			"CATEGORY\n" +
    			"VULNERABILITY"
    			);
    	tmmd.setDataDMA(
    			"module1 1 1 ldapi 1\n" +
    			"module2 1 1 sqli 1\n"
    	);
    	tmmd.doAnalysis();
    	HashMap<String, Clone> c =  tmmd.getClones();
    	Clone cl = c.values().iterator().next();
    	assertTrue(cl.getModules().size() == 2);
	}
	
	@Test
	public void testGetIDFields() throws Exception {
		MMD tmmd = new MMD("./test/goodExample/output");
    	tmmd.setDMAFmt(
    			"NAME\n"+
    			"ID\n"+
    			"METRIC CC\n"+
    			"METRIC IFS\n"+
    			"CATEGORY\n" +
    			"VULNERABILITY"
    			);
    	tmmd.setDataDMA(
    			"module1 m01 1 1 ldapi 1\n" +
    			"module2 m02 1 1 sqli 1\n"
    	);
    	tmmd.doAnalysis();
    	HashMap<String, Clone> c =  tmmd.getClones();
    	Clone cl = c.values().iterator().next();
    	System.out.println("FIELDS " + cl.getIdFields().size());
    	assertTrue(cl.getModules().size()==2);
    	assertTrue(cl.getIdFields().size()==2);
    	assertTrue(cl.getIdFields().containsKey("ID"));
    	assertTrue(cl.getIdFields().containsKey("NAME"));
    	tmmd.buildOutput();
	}
	
	@Test
	public void testIsVulnerable() throws Exception {
		MMD tmmd = new MMD("./test/goodExample/output");
    	tmmd.setDMAFmt(
    			"NAME\n"+
    			"METRIC CC\n"+
    			"METRIC IFS\n"+
    			"CATEGORY\n" +
    			"VULNERABILITY"
    			);
    	tmmd.setDataDMA(
    			"module1 1 1 ldapi 1\n" +
    			"module2 1 1 sqli 1\n"
    	);
    	tmmd.doAnalysis();
    	HashMap<String, Clone> c =  tmmd.getClones();
    	Clone cl = c.values().iterator().next();
    	assertTrue(cl.isVulnerable());
    	
    	MMD tmmd1 = new MMD("./test/goodExample/output");
    	tmmd1.setDMAFmt(
    			"NAME\n"+
    			"METRIC CC\n"+
    			"METRIC IFS\n"+
    			"CATEGORY\n" +
    			"VULNERABILITY"
    			);
    	tmmd1.setDataDMA(
    			"module1 1 1 ldapi 0\n" +
    			"module2 1 1 sqli 1\n"
    	);
    	
    	tmmd1.doAnalysis();
    	HashMap<String, Clone> c1 =  tmmd1.getClones();
    	assertTrue(c1.size()==1);
    	Clone cl1 = c1.values().iterator().next();
    	assertFalse(cl1.isVulnerable());
    	
    	MMD tmmd2 = new MMD("./test/goodExample/output");
    	tmmd2.setDMAFmt(
    			"NAME\n"+
    			"METRIC CC\n"+
    			"METRIC IFS\n"+
    			"CATEGORY\n" +
    			"VULNERABILITY"
    			);
    	tmmd2.setDataDMA(
    			"module1 1 1 ldapi 0\n" +
    			"module2 1 1 sqli 0\n"
    	);
    	
    	tmmd2.doAnalysis();
    	HashMap<String, Clone> c2 =  tmmd2.getClones();
    	assertTrue(c2.size()==1);
    	Clone cl2 = c2.values().iterator().next();
    	assertFalse(cl2.isVulnerable());
    	tmmd.buildOutput();
	}
	
	@Test
	public void testGetID() throws Exception {
		MMD tmmd = new MMD("./test/goodExample/output");
    	tmmd.setDMAFmt(
    			"NAME\n"+
    			"METRIC CC\n"+
    			"METRIC IFS\n"+
    			"CATEGORY\n" +
    			"VULNERABILITY"
    			);
    	tmmd.setDataDMA(
    			"module1 1 1 ldapi 1\n" +
    			"module2 1 1 sqli 1\n"
    	);
    	tmmd.doAnalysis();
    	HashMap<String, Clone> c =  tmmd.getClones();
    	Clone cl = c.values().iterator().next();
    	assertTrue(cl.getId().equals("c0"));
    	tmmd.buildOutput();
	}
	
	@Test
	public void testMultipleCloneGroups() throws Exception {
    	MMD tmmd = new MMD("./test/goodExample/output");
    	tmmd.setDMAFmt(
    			"NAME\n"+
    			"METRIC CC\n"+
    			"METRIC IFS\n"+
    			"CATEGORY\n" +
    			"VULNERABILITY"
    			);
    	tmmd.setDataDMA(
    			"module1 1 1 sqli 1\n" +
    			"module2 1 1 sqli 1\n" +
    			"module3 2 1 sqli 1\n" +
    			"module4 2 1 sqli 0\n" +
    			"module5 3 1 ldapi 0\n" +
    			"module6 3 1 ldapi 1\n"
    	);
    	tmmd.doAnalysis();
    	tmmd.buildOutput();
	}
}
