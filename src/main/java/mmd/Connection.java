package mmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Connection {
	
	private ModuleBaseClass currentNode;
	private ModuleBaseClass neigborNode;
	
	private int id;
	private String _id;
	private int difference;
	private HashSet<String> headerItems;
	private HashMap<String, HashMap<String, Object>> moduleDifferences;
	private HashMap<String, Object> h1;
	private HashMap<String, Object> h2;

	
	public Connection(ModuleBaseClass currentNode,ModuleBaseClass neigborNode,int connID){
		this.currentNode = currentNode.getSelf();
		this.neigborNode = neigborNode.getSelf();
		
		this.id = connID;
		this.headerItems = new HashSet<String>();
		this.moduleDifferences = new HashMap<String,HashMap<String,Object>>();
	}
	
	public ArrayList<ModuleBaseClass> getModules() {
		ArrayList<ModuleBaseClass> ms = new ArrayList<ModuleBaseClass>();
		ms.add(this.currentNode);
		ms.add(this.neigborNode);
		return ms;
	}
	
	public void setModules(ModuleBaseClass currentNode, ModuleBaseClass neigborNode) {
		this.currentNode = currentNode.getSelf();
		this.neigborNode = neigborNode.getSelf();
	}
	
	public void cannibalizeMD(MetricDiff md) {
		this.moduleDifferences = md.moduleDifferences;
		this.headerItems = md.headerItems;
		this.difference = md.difference;
		this.h1 = md.getOM1();
		this.h2 = md.getOM2();
	}
	
	public int getDistance() {
		return this.difference;
	}
	
	public ModuleBaseClass getOtherNode(ModuleBaseClass m) {
		if(m.getSelf().getId().equals(this.currentNode.getSelf().getId())) return this.neigborNode.getSelf();
		else return this.currentNode.getSelf();
	}
	
	public String getId() {
		return this._id;
	}

	public void setID(int andIncrement) {
		this._id = "e"+andIncrement;
	}

	public  HashSet<String> getHeaderItems() {
		return this.headerItems;
	}

	public Object getHeadertoValue(String header,int i) {
		if(i==1) return  this.h1.get(header);
		else return this.h2.get(header);
	}
}
