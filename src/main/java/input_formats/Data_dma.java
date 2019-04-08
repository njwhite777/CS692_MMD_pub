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
	
	public static boolean checkNumber(String numStr){
		  if(numStr==null){
			  return false;
		  }		  
		  if(numStr==""){
			  return false;
		  }
		  
		  if(numStr.contains(".")){ //check float type
			  if(numStr.indexOf('.')==numStr.lastIndexOf('.')){
				  StringTokenizer st=new StringTokenizer(numStr,".");
				  while(st.hasMoreElements()){
					  String splitStr= st.nextToken();
					  for(int i=splitStr.length();--i>=0;){
						  if(!Character.isDigit(splitStr.charAt(i))){
							  return false;
						  }
					  }
				  }
			  }
		  }else{
			  for(int i=numStr.length();--i>=0;){ 
				  if(!Character.isDigit(numStr.charAt(i))){
					  return false;
				  }
			  }
		  }
		  return true;
	}
	
	private void readDataFile(BufferedReader br) throws Exception {
		try {
		    String line;
		    while ((line = br.readLine()) != null) {

		    	// Split the row on whitespace.
		    	String[] row = line.split("\\s+");
		    	
		    	if(this.previousRowLength == -1) {
		    		this.previousRowLength = row.length;
		    	}else if(this.previousRowLength != row.length) {
		    		throw new Exception("GENERIC EXCEPTION: Sorry, the data file must maintain a consistent row length. Examine row " + Integer.toString(this.length+1));		    		
		    	}
		    	this.addData_checkTypes(dma_fmt, row, length);
		    	this.length++;
		    }
		}catch(Exception e) {
			throw e;
		}
		
		if(this.length == 0) {
			throw new Exception("GENERIC EXCEPTION: problem reading from format file.");
		}		
	}
	
	//Note: this method has serious side effects.
	public void addData_checkTypes(Dma_fmt dma_fmt,String[] row,int length) throws Exception {
		Module t = new Module();
		t.setFileName(this.nameOfDataAndFormatFile);
		t.setModuleID(moduleNextID.getAndIncrement());
//		t.setConnectionIDObject(connectionNextID);
		
		if(row.length != dma_fmt.headers.size())
			throw new Exception("GENERIC EXCEPTION: the format file must have the same number of header items (has "+ dma_fmt.getHeaderLength() +") as the data file has row items (has " + row.length + ").");
		
		for(int i=0;i<dma_fmt.headers.size();i++) {
			String fullHeader =  dma_fmt.headers.get(i);
			String headerType = fullHeader.split("\\s+")[0];
			Object o = row[i];
					
			if(this.intTypeItems.contains(headerType)) {
				if(!checkNumber(row[i])) {
					throw new Exception("GENERIC EXCEPTION: the Header " + dma_fmt.headers.get(i).toString()+ " is INT type format. "+ "Please Check data file at line (" + length + "),column(" + i+ ")," + row[i] + " should be INT type.");
				}
				// If we get here we know we have a number, so convert string to number.				
				o = Integer.valueOf((String) o);
			
				
			}else {
				if(!(headerType.equals("ID") || headerType.equals("CLONE_ID") ) && checkNumber(row[i])) {
					throw new Exception("GENERIC EXCEPTION: the Header " + dma_fmt.headers.get(i).toString()+ " is String type format. "+ "Please Check data file at line (" + length + "), column (" + i +"), " + row[i] + " should be String type.");
				}				
			}
			

			if(this.intTypeTtemsRangeOneOrZero.contains(headerType)) {
				if(!(Integer.parseInt(row[i])==0||Integer.parseInt(row[i])==1)) {
					throw new Exception("GENERIC EXCEPTION: " + headerType.toString()+ " is INT type format and only have value 0 and 1. " + "Please Check data file at line (" + length + "),column(" + i+ ")" );
				}				
				
			}	
			
			
			if(this.comparableData.contains(headerType)){
				try {
					String metricType = fullHeader.split("\\s+")[1];
					t.addComparableItem(o);
					t.addComparableItemHeaders(metricType);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
				if(length == 0) metricLength++;
			}
			
			if(this.processData.contains(headerType)) {
				t.setProcessData(headerType.toLowerCase(),row[i]);
			}
			
			if(this.idTypeItem.contains(headerType)) {
				t.setIdField(headerType,o);
			}
			
			if(headerType.equals("CATEGORY")) {
				t.setVulnerabilityType((String) o);				
			}
			
			if(headerType.equals("VULNERABILITY")) {
				t.setVulnerabilityStatus((Integer) o);
			}
		}
		this.data.add(t);
	}
	
	// Returns the length of one row of data. This should match the length of the dma_fmt headers.	
	public int getRowLength() {
		return this.previousRowLength;		
	}
	
	public int getNumberOfModules() {
		return length;
	}
	
	public int getNumberOfMetrics() {
		return metricLength;
	}
}
