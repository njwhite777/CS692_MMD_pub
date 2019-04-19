package mmd;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.parsers.ParserConfigurationException;

import input_formats.Data_dma;
import input_formats.Dma_fmt;
import output_formats.Dot_builder;
import output_formats.Xml_builder;

public class MMD {
	
	private Dma_fmt dma_fmt;
	private Data_dma data_dma;
	private String pathToOutputFiles;
	private HashMap<String,Module> modules;
	private HashMap<String,Clone> clones;
	private Xml_builder xmlOutputWriter;
	
	private HashMap<String,Connection> connections;
	
	private AtomicInteger cloneNextID = new AtomicInteger();
	private AtomicInteger connectionNextID = new AtomicInteger();
	private Integer connID = 0;	

	private Dot_builder dotOutputWriter;
	private String filename;

	public MMD(String pathToTest, String nameOfDataAndFormatFile,String pathToOutputFiles) throws Exception {	
		this.dma_fmt = new Dma_fmt(pathToTest,nameOfDataAndFormatFile);
		this.data_dma = new Data_dma(pathToTest,nameOfDataAndFormatFile,dma_fmt);
		this.modules = new HashMap<String,Module>();
		this.connections = new HashMap<String,Connection>();
		this.filename =  nameOfDataAndFormatFile+".dma";

		this.pathToOutputFiles = pathToOutputFiles;
		this.clones = new HashMap<String,Clone>();
		File f = new File(pathToOutputFiles);
		if(!f.exists())
			f.mkdirs();
	}

	public MMD(String absolutePath) {
		this.modules = new HashMap<String,Module>();
		this.connections = new HashMap<String,Connection>();
		this.filename =  "TEST.dma";

		this.pathToOutputFiles = absolutePath;
		this.clones = new HashMap<String,Clone>();
		File f = new File(pathToOutputFiles);
		if(!f.exists())
			f.mkdirs();
	}
	
	public void setDMAFmt(String dma_fmt) throws Exception {
		this.dma_fmt = new Dma_fmt(dma_fmt); 
	}
	
	public void setDataDMA(String data_dma) throws Exception {
		this.data_dma = new Data_dma(data_dma,this.dma_fmt); 	
	}

	void buildOutput() throws ParserConfigurationException {
		ArrayList<ModuleBaseClass> mbc = new ArrayList<ModuleBaseClass>();
		mbc.addAll(this.clones.values());
		mbc.addAll(this.modules.values());
		
		this.dotOutputWriter = new Dot_builder(mbc,this.pathToOutputFiles);
		this.dotOutputWriter.run();
		this.dotOutputWriter.write();
		
		this.xmlOutputWriter = new Xml_builder(this.modules,this.clones,this.connections,this.pathToOutputFiles);
		this.xmlOutputWriter.run(this.filename);
		this.xmlOutputWriter.write();
	}

	// TODO: Test to make sure the values of CLONE_MIXED_TYPE, CLONE_TYPE, CLONE, and VULNERABILITY are all 0 or 1.
	// TODO: Make sure the type is one of BASE_CLASS, MEMBER_FUNCTION, SOURCE_FILE, etc. (Ask what is the etc?).		
	void doAnalysis() {	
		
		// Single module case. 
		if(this.data_dma.getNumberOfModules()==1) {
			Module m1 = data_dma.data.get(0);
			this.modules.put(m1.getId(),m1);
		}
		
		for(int indexFirst = 0; indexFirst<this.data_dma.getNumberOfModules(); indexFirst++) {
			for(int i = indexFirst+1;i<this.data_dma.getNumberOfModules();i++) {
				Module m1 = data_dma.data.get(indexFirst);
				Module m2 = data_dma.data.get(i);
				this.checkAndAddNeigbor(m1,m2);
			}
		}
				
		HashSet<String> seenModule = new HashSet<String>();
		
		ArrayList<ModuleBaseClass> mbc = new ArrayList<ModuleBaseClass>();
		mbc.addAll(this.clones.values());
		mbc.addAll(this.modules.values());
		
		for(ModuleBaseClass startNode : mbc) {
			this.traverseGraph(startNode,seenModule);
		}	
	}
	
	public void traverseGraph(ModuleBaseClass startNode,HashSet<String> seenConnection) {
		Stack<Connection> ns = new Stack<Connection>();
		for(Integer i : startNode.getDistanceMapToNearestNeigbors().keySet()) {
			for( Connection ctn : startNode.getAllNearestNeigbors().get(i).values()) {
				ns.push(ctn);
			}
		}
		
		while(!ns.isEmpty()) {
			Connection c = ns.pop();
			ModuleBaseClass neigborNode = c.getOtherNode(startNode);
			
			String connection = startNode.getSelf().getId()+"-"+neigborNode.getId();
			String reverseCon = neigborNode.getId()+"-"+startNode.getSelf().getId();
						
			if(!(seenConnection.contains(connection))) {
				seenConnection.add(connection);
				seenConnection.add(reverseCon);
				this.traverseGraph(neigborNode,seenConnection);		
				c.setID(this.connectionNextID.getAndIncrement());
				this.connections.put(c.getId(),c);
			}
		}
		return;
	}
	
	public void checkAndAddNeigbor(Module m1, Module m2) {

		MetricDiff md = new MetricDiff(m1,m2);				
		for(int j=0;j<m1.getNumberOfComparableMetrics();j++) {
			if(!m1.comparableItems.get(j).equals(m2.comparableItems.get(j))) {
				md.addDifference(m1.comparableItems.get(j),m2.comparableItems.get(j),m1.getComparableItemHeader(j));
			}
		}

		// The modules are clones
		if(md.difference == 0) {
			.
			.
			.
		}
		// They are not clones of one another.
		else {
			.
			.
			.
		}
	}
	

	public  HashMap<String, Module> getModules() {
		return this.modules;
	}

	public HashMap<String, Clone> getClones() {
		return this.clones;
	}

	public HashMap<String, Connection> getConnections() {
		return this.connections;
	}

	public static void main(String[] args) throws Exception {

		if(args.length < 2 || args.length > 3) {
			System.out.println("Please call file as follows: xxxx.jar  pathToTestDir nameOfDataAndFormatFile <path_to_output_dir>");
		}else {
			String pathToTest = args[0];
			String nameOfDataAndFormatFile = args[1];
			String pathToOutputFile = args[2];
			
			MMD mmd = new MMD(pathToTest,nameOfDataAndFormatFile,pathToOutputFile);
			mmd.doAnalysis();
			mmd.buildOutput();
		}	
	}
}
