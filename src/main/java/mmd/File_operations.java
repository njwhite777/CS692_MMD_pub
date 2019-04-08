package mmd;

import java.io.File;
import java.io.FileReader;

public abstract class File_operations {
	
	// Creates the file. Checks to see if the file at that path exists (hint, it should).	
	public File getFileName(String pathToTest,String nameOfDataAndFormatFile,String ext) throws Exception {
		File f = new File(pathToTest + "/" + nameOfDataAndFormatFile + "." + ext);
		if(!f.exists()) throw new Exception("GENERIC ERROR: the file "+ nameOfDataAndFormatFile + "." + ext +" does not exist.");
		return f;
	}

	// Checks the size of the files in the test directory	
	public void checkFileSize(File f) throws Exception {
		if( f.length() == 0 ) throw new Exception("GENERIC ERROR: the file " + f.getName() + " must not be empty (0B size).");		
	}
	
	// Creates reader for file
	public FileReader getFileReader(File f) throws Exception {
		return new FileReader(f);		
	}
}
