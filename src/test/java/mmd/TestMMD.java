package mmd;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestMMD {

	MMD mmd;
	File outDir1 = new File("./test/goodExample/output_dir1");
	File outDir2 = new File("./test/goodExample/output_dir2");
	
	String dma_fmt1 = "NAME\n"
			+ "METRIC WHILE\n"
			+ "METRIC IFS\n"
			+ "METRIC CC\n"
			+ "VULNERABILITY";
	
	String dma_data1 = "module1 1 1 1 0\n"
			+ "module2 1 0 1 0";
			
	
	// pathToTest, nameOfDataAndFormatFile, pathToOutputFiles
	
    @Before
    public void init() {
    }
    
    @Test
    public void buildsOutputDir() {
    	try {
			new MMD("./test/goodExample/input","goodExample",outDir1.getAbsolutePath());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	// Outdir exists.
    	assertTrue(outDir1.exists());
    	outDir1.delete();
    	assertFalse(outDir1.exists());
    }
    
    @Test
    public void doesNotBuildOutDir() {
    	if(!outDir1.exists()) outDir1.mkdirs();
    	try {
			new MMD("./test/goodExample/input","goodExample",outDir1.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
    	outDir1.delete();
    }
    
    @Test
    public void doesAnalysis() throws Exception {
    	MMD tmmd = new MMD(outDir1.getAbsolutePath());
    	tmmd.setDMAFmt(this.dma_fmt1);
    	tmmd.setDataDMA(this.dma_data1);
    	
    	tmmd.doAnalysis();
    	assertTrue(tmmd.getModules().size() == 2);
    	assertTrue(tmmd.getClones().size() == 0);
    	assertTrue(tmmd.getConnections().size() == 1);
    }
    
    @Test
    public void analysisSingleCase() throws Exception{
    	MMD tmmd = new MMD(outDir1.getAbsolutePath());
    	tmmd.setDMAFmt(
    			"NAME\n"+
    			"METRIC CC\n"+
    			"METRIC IFS\n"+
    			"VULNERABILITY"
    			);
    	tmmd.setDataDMA("module1 1 1 1");
    	tmmd.doAnalysis();
    }
    
    @Test
    public void analysisWithClonesCase() throws Exception{
    	MMD tmmd = new MMD(outDir1.getAbsolutePath());
    	tmmd.setDMAFmt(
    			"NAME\n"+
    			"METRIC CC\n"+
    			"METRIC IFS\n"+
    			"VULNERABILITY"
    			);
    	tmmd.setDataDMA("module1 1 1 1\n" 
    			+ "module2 1 1 1\n"
    			+ "module3 1 1 0\n"
    			+ "module4 1 0 0\n"
    			+ "module5 2 0 0\n"
    			+ "module6 2 1 0\n"
    			+ "module7 2 1 0\n"
    			+ "module8 1 1 0"
    			);
    	tmmd.doAnalysis();
    }
    
    @Test
    public void doesOutput() throws Exception {
    	MMD tmmd = new MMD(outDir1.getAbsolutePath());
    	tmmd.setDMAFmt(this.dma_fmt1);
    	tmmd.setDataDMA(this.dma_data1);
    	
    	tmmd.doAnalysis();
    	tmmd.buildOutput();
    }
    
    @Test
    public void callMain() {
    	String[] args = {"./test/goodExample/input","goodExample","./test/goodExample/output"};
    	try {
			MMD.main(args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Test
    public void callMainTooManyArgs() {
    	String[] args = {"./test/goodExample/input","goodExample","./test/goodExample/output",""};
    	try {
			MMD.main(args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Test
    public void callMainTooFewArgs() {
    	String[] args = {"./test/goodExample/input"};
    	try {
			MMD.main(args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
 
    @After
    public void finalize() {
    	outDir1.delete();
    }
}

