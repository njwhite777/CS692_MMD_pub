package input_formats;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;

import mmd.Constants;
import mmd.File_operations;
import mmd.Module;

public class Data_dma extends File_operations {

	public ArrayList<Module> data;
	private int length;
	private int previousRowLength = -1;
	private Dma_fmt dma_fmt;
	private HashSet<String> comparableData;
	private HashSet<String> intTypeItems;
	private HashSet<String> idTypeItem;
	private HashSet<String> intTypeTtemsRangeOneOrZero;
	private int metricLength;
	private AtomicInteger moduleNextID = new AtomicInteger();
	private AtomicInteger connectionNextID = new AtomicInteger();
	private String nameOfDataAndFormatFile;
	private HashSet<String> processData;



	public Data_dma(String pathToTest, String nameOfDataAndFormatFile, Dma_fmt dma_fmt) throws Exception {
		this.length = 0;
		this.metricLength = 0;
		this.data = new ArrayList<Module>();
		this.dma_fmt = dma_fmt;
		
		this.intTypeItems = Constants.Headers.getIntTypeHeaderItems();
		this.comparableData = Constants.Headers.getTwoPartHeaderItems();
		this.idTypeItem = Constants.Headers.getIdentificationFields();
		this.processData = Constants.Headers.getProcessData();
		this.intTypeTtemsRangeOneOrZero =Constants.Headers.getIntTypeZeroOrOneValue();
		this.nameOfDataAndFormatFile = nameOfDataAndFormatFile;
		
		// Verify that file exists by using getFileName		
		File f = this.getFileName(pathToTest, nameOfDataAndFormatFile, "dma");
		// Verify that the file is longer than 0B		
		this.checkFileSize(f);
		BufferedReader br = new BufferedReader(this.getFileReader(f));
		this.readDataFile(br);
	}
	

	public Data_dma(String data_dma,Dma_fmt dma_fmt) throws Exception {
		// Declare all necessary objects here.
		this.length = 0;
		this.metricLength = 0;
		this.data = new ArrayList<Module>();
		this.dma_fmt = dma_fmt;
		
		this.intTypeItems = Constants.Headers.getIntTypeHeaderItems();
		this.comparableData = Constants.Headers.getTwoPartHeaderItems();
		this.idTypeItem = Constants.Headers.getIdentificationFields();
		this.processData = Constants.Headers.getProcessData();
		this.intTypeTtemsRangeOneOrZero =Constants.Headers.getIntTypeZeroOrOneValue();
		this.nameOfDataAndFormatFile = "TESTFILE";
		
		// Verify that file exists by using getFileName		
		InputStream is = new ByteArrayInputStream(data_dma.getBytes());
		BufferedReader br = new BufferedReader(new InputStreamReader(is));		
		this.readDataFile(br);
	}
	
	.
	.
	.
}
