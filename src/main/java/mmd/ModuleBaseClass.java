package mmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;

import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.MutableNode;

public abstract class ModuleBaseClass {
		
	protected String _id;
	protected String _clone_id;
		
	private Integer keepNearestNeigbors = 1;
	private NavigableMap<Integer,HashMap<String,Connection>> nearestNeighbors;
	private HashMap<String, Object> idFields;
	HashMap<String, String> processData;

	public MutableNode node;	
	
	abstract public String getId();
		
	public abstract ModuleBaseClass getSelf();
	public abstract boolean isVulnerable();
	public abstract boolean isClone();
	
	public ModuleBaseClass() {
		this.nearestNeighbors = new TreeMap<Integer,HashMap<String,Connection>>();
		this.idFields = new HashMap<String,Object>();
		this.processData = new HashMap<String, String>();
		this.node = Factory.mutNode("").add(Shape.RECTANGLE);
	}
	
	public Object getIdField(String s) {
		return this.idFields.get(s);
	}
	
	public void setIdField(String s,Object o) {
		this.idFields.put(s, o);
	} 
	
	
	public HashMap<String,Object> getIdFields() {
		return this.idFields;
	}
	
	public String getName() {
		String name  = "";
		for(String header : Constants.Headers.getIdentificationFields()) {
			if(this.idFields.containsKey(header)) {
				name = (String) this.idFields.get(header);
				break;
			}
		}
		return name;
	}
	
	public void addNeigbor(Connection c) {
		ModuleBaseClass otherNode = c.getOtherNode(this);
		String neigborID = otherNode.getId();
						
		int keepNN=this.keepNearestNeigbors;
		
		if(!this.nearestNeighbors.containsKey(c.getDistance())) {
			this.nearestNeighbors.put(c.getDistance(),new HashMap<String,Connection>());
			this.nearestNeighbors.get(c.getDistance()).put(neigborID,c);
		}else {
			if(!this.nearestNeighbors.get(c.getDistance()).containsKey(neigborID)) {
				this.nearestNeighbors.get(c.getDistance()).put(neigborID,c);
			}
		}
		
		if(this.nearestNeighbors.size() > keepNN) {
			this.nearestNeighbors.remove(this.nearestNeighbors.lastKey());
		}
	}
	
	public void addNeigbor(ModuleBaseClass neigbor,Connection c) {
		this.addNeigbor(c);
	}

	public SortedMap<Integer,HashMap<String,Connection>> getAllNearestNeigbors() {
		return (SortedMap<Integer, HashMap<String,Connection>>) this.nearestNeighbors;
	}
	
	public ArrayList<Connection> getNeigborArrayList(){
		ArrayList<Connection> ctnStack = new ArrayList<Connection>();
		for(Integer k : this.nearestNeighbors.keySet()) {
			for(Connection ctn : this.nearestNeighbors.get(k).values()) {
				ctnStack.add(ctn);
			}
		}
		return ctnStack;
	}

	public NavigableMap<Integer, HashMap<String,Connection>> getDistanceMapToNearestNeigbors() {
		ArrayList<Integer> ks = new ArrayList<Integer>(this.nearestNeighbors.keySet());
		if(ks.size() == 0) return new TreeMap<Integer, HashMap<String,Connection>>();
		int firstKey = 0;
		for(Integer v : ks) {
			if(!(v == 0)) {
				firstKey = v;
				break;
			}
		}
		return (NavigableMap<Integer, HashMap<String,Connection>>) this.nearestNeighbors.subMap(firstKey,true,this.nearestNeighbors.lastKey(),true);
	}
	
	public void putConnection(ModuleBaseClass module,Connection c) {
		int distance = c.getDistance();
		String putID = module.getSelf().getId();
		if(this.nearestNeighbors.containsKey(distance)) {
			HashMap<String,Connection> neigborConnectionMap = this.nearestNeighbors.get(distance);
			neigborConnectionMap.put(putID, c);
		}
	}

	public void updateConnectionHelper(Module module, Connection c) {
		int distance = c.getDistance();
		String oldID = module.getId();
		if(this.nearestNeighbors.containsKey(distance)) {
			HashMap<String,Connection> neigborConnectionMap = this.nearestNeighbors.get(distance);
			if(neigborConnectionMap.containsKey(oldID)) {
				neigborConnectionMap.remove(module.getId());
			}
		}
	}
	
	public void setProcessData(String xmlTagName,String value) {
		this.processData.put(xmlTagName, value);		
	}
	
	public String getProcessData(String xmlTagName) {
		return this.processData.get(xmlTagName);
	}

	public HashMap<String, String> getAllProcessData() {
		return this.processData;
	}
	
	public boolean hasProcessData() {
		return this.processData.size() > 0;
	}
}
