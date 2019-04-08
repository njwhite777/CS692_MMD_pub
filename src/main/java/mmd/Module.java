package mmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NavigableMap;

import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.model.Factory;

public class Module extends ModuleBaseClass {
	
	public String id;
	public ArrayList<Object> comparableItems;
	private ArrayList<String> comparableItemHeaders;
	private String fileName;
	private String vulnerabilityCategory;
	private int vulnerabilityStatus;

	public HashMap<String, MetricDiff> connections;
	public MutableNode node;

	private Clone clone;
	
	public Module() {
		this._id = "";
		this._clone_id = "";
		this.id = "";
		this.vulnerabilityCategory = "";
		this.vulnerabilityStatus = -1;
		this.comparableItems = new ArrayList<Object>();
		this.comparableItemHeaders = new ArrayList<String>();
		this.connections = new  HashMap<String, MetricDiff>();
		this.fileName = "";
		this.node = Factory.mutNode("").add(Shape.RECTANGLE);
	}
	
	public void addComparableItem(Object item) {
		this.comparableItems.add(item);
	}
	
	public void addComparableItemHeaders(String header) {
		this.comparableItemHeaders.add(header);
	}
	
	public String getComparableItemHeader(Integer i) {
		return this.comparableItemHeaders.get(i);
	}
		
	public void setVulnerabilityType(String vulnerabilityCategory) {
		this.vulnerabilityCategory = vulnerabilityCategory;
	}

	public String getVulnerabilityType() {
		return this.vulnerabilityCategory;
	}
	
	public void setVulnerabilityStatus(int status) {
		this.vulnerabilityStatus = status;		
	}
	
	public int getVulnerabilityStatus() {
		return this.vulnerabilityStatus;
	}
	
	public int getNumberOfComparableMetrics() {
		return this.comparableItems.size();		
	}
	
	public String getId() {
		return this._id;
	}

	public void setModuleID(int id) {
		this._id = "m" + id;
		this.node.setName(this._id);
	}
			
	public void setFileName(String nameOfDataAndFormatFile) {
		this.fileName = nameOfDataAndFormatFile + ".dma";		
	}
	
	public String getFileName() {
		return this.fileName;
	}

	public void setClone(Clone c) {
		this.clone = c;		
	}

	public Clone getClone() {
		return this.clone;
	}
	
	public String getCloneId() {
		return this.clone.getId();
	}

	public boolean isClone() {
		return !(this.clone == null);
	}
	
	public ModuleBaseClass getSelf() {
		if(this.isClone()) return this.clone;
		return this;
	}
	
	public boolean isVulnerable() {
		return this.vulnerabilityStatus == 1;
	}

	public void updateConnections(Clone clone) {
		NavigableMap<Integer, HashMap<String, Connection>> distanceMap = this.getDistanceMapToNearestNeigbors();
		for(Integer distance : distanceMap.keySet()) {
			if(distance == 0) continue;
			HashMap<String, Connection> neigborMap = distanceMap.get(distance);
			for(String neigborID : neigborMap.keySet()) {
				Connection c = neigborMap.get(neigborID);
				ModuleBaseClass neigborNode = c.getOtherNode(this);
				
				// Remove all references to this module from neigbors.
				neigborNode.updateConnectionHelper(this,c);
				c.setModules(clone,neigborNode);
				neigborNode.putConnection(clone, c);					
			}
		}	
	}

//	public String getCWE() {
//		return null;
//	}

}
