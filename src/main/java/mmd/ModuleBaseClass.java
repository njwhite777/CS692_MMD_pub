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
	
	.
	.
	.
}
