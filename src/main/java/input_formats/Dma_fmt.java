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
	
	.
	.
	.
}
