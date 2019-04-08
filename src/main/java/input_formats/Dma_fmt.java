package input_formats;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mmd.Constants;
import mmd.File_operations;

public class Dma_fmt extends File_operations {

	int length;
	public ArrayList<String> headers;

	 
	HashSet<String> validHeaderItems;
	HashSet<String> twoLengthHeaderItems;
	HashSet<String> oneLengthHeaderItems;
	HashSet<String> unlimitDuplicateHeader;
	HashSet<String> limitOneHeader;
	HashSet<String> identificationField;
	ArrayList<String> limitOneHeaderLists;
	HashSet<String> seenHeaderItem;
	boolean seenMetric=false;
	boolean seenTYPE2=false;
	boolean seenIdentificationField=false;
	
	public Dma_fmt (String pathToTest, String nameOfDataAndFormatFile) throws Exception {
		// Declare all necessary objects here.
		this.length = 0;
		this.headers = new ArrayList<String>();
		
		this.twoLengthHeaderItems = new HashSet<String>(Arrays.asList( 
				new String[]{ "METRIC","XMLTAG"}))  ;
			
		this.limitOneHeader = Constants.Headers.getLimitOneAppearanceHeaderItems();
		this.limitOneHeaderLists = new ArrayList<>(limitOneHeader);
		this.oneLengthHeaderItems = Constants.Headers.getOnePartHeaderItems();
		this.identificationField = Constants.Headers.getIdentificationFields();
	
		// Verify that file exists by using getFileName		

		File f = this.getFileName(pathToTest, nameOfDataAndFormatFile,"dma_fmt");
		// Verify that the file is longer than 0B		
		this.checkFileSize(f);
		BufferedReader br = new BufferedReader(this.getFileReader(f));
		this.readFormatFile(br);		
	}
	
	public Dma_fmt(String dma_fmt) throws Exception {
		// Declare all necessary objects here.
		this.length = 0;
		this.headers = new ArrayList<String>();
		
		this.twoLengthHeaderItems = new HashSet<String>(Arrays.asList( 
				new String[]{ "METRIC","XMLTAG"}))  ;
			
		this.limitOneHeader = Constants.Headers.getLimitOneAppearanceHeaderItems();
		this.limitOneHeaderLists = new ArrayList<>(limitOneHeader);
		this.oneLengthHeaderItems = Constants.Headers.getOnePartHeaderItems();
		this.identificationField = Constants.Headers.getIdentificationFields();
	
		// Verify that file exists by using getFileName
		InputStream is = new ByteArrayInputStream(dma_fmt.getBytes());
		BufferedReader br = new BufferedReader(new InputStreamReader(is));		
		this.readFormatFile(br);	
	}

	public void checkHeaderTypeTuple(String line,int lineNumber) throws Exception {
		int tline = lineNumber+1;
		
		String[] tString = line.split("\\s+");	
		
		
		
		if(line.length() == 0) {
			throw new Exception("GENERIC EXCEPTION: Sorry, files with extension .dma_fmt should not have blank lines. Check line " + tline +".");
		}else if(tString.length == 2) {
			if(this.oneLengthHeaderItems.contains(tString[0])) {
				throw new Exception("GENERIC EXCEPTION: Sorry, received a tuple header item for an item that does not have a second parameter. Check line " + tline + ".");
			}
			if(tString[0].equals("METRIC")) {
				
				seenMetric=true;
			}
			if(!Constants.Headers.getAllHeaderItems().contains(tString[0])) {		    		 
	   			 throw new Exception("GENERIC EXCEPTION: Sorry,"+tString[0].toString()+ " is an undefined Headers");	   		 
			}
		}else if(tString.length==1) {
			if( this.twoLengthHeaderItems.contains(tString[0])) {
				throw new Exception("GENERIC EXCEPTION: Sorry, received a single header item which requires a second parameter.");
			}
						
			if(tString[0].equals("TYPE2")) {
				seenTYPE2=true;
			}
			
			if(this.identificationField.contains(tString[0])) {
				seenIdentificationField=true;
			}
			
			if(!Constants.Headers.getAllHeaderItems().contains(tString[0])) {		    		 
   			 throw new Exception("GENERIC EXCEPTION: Sorry,"+tString[0].toString()+ " is an undefined header");   		 
			}
			
		}else{
			throw new Exception("GENERIC EXCEPTION: Sorry, your header items will never have more than two items. Check line " + tline + ".");
		} 
		
	}
	
	public int getHeaderLength() {
		return this.length;
	}

	private void readFormatFile(BufferedReader br) throws Exception {
		
		try {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	this.checkHeaderTypeTuple(line,length);
		    	
		    	this.headers.add(line);
		    	this.length++;
		    }
		    //use for loop check if limit one header appear twice
		    for(String element:headers) {
		    	if(limitOneHeader.contains(element)) {
		    		 if(limitOneHeaderLists.remove(element)==false) {
		    			 throw new Exception("GENERIC EXCEPTION: Sorry,"+element.toString()+ " header can only appear once.");
		    		 }
		    	}
		    }
		    
//		    for(String element:headers) {
//		    	if(!ALL_HEADER_ITEMS.contains(element)) {		    		 
//		    			 throw new Exception("GENERIC EXCEPTION: Sorry,"+element.toString()+ " is a undefined Headers");
//		    		 
//		    	}
//		    }
		    //check when type2 appear but type not
		    if(seenTYPE2==true && !headers.contains("TYPE")) {
		    	throw new Exception("GENERIC EXCEPTION: Sorry, the TYPE2 header cannot present without the TYPE.");
		    }
		    
		    if(seenIdentificationField==false) {
		    	throw new Exception("GENERIC EXCEPTION: Sorry, there must be at least one identification field((NAME,ID,DMA_ID,XML_ID,CLONE_ID,TEST_NUMBER)).");		    
		    }
		    
		    if(seenMetric==false) {
		    	System.out.println("HERE1");
		    	throw new Exception("GENERIC EXCEPTION: Sorry, the metric header must appear at least once");
		    }
		}catch(Exception e) {
			throw e;
		}
		
//		for(String i:headers) {
//			
//				System.out.println(i.toString());
//					
//		}
		// This should never actually happen.		
		if(this.length == 0) {
			throw new Exception("GENERIC EXCEPTION: problem reading from format file.");
		}
	}

	
}
