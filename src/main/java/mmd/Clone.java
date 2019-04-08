package mmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.SortedMap;

import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.model.Factory;

public class Clone extends ModuleBaseClass {
	
	
	private ArrayList<ModuleBaseClass> modules;
	private HashSet<String> moduleIDs;
	private HashSet<String> vulnerabilityTypes;
	private String _id;
	public HashSet<Integer> vulnerabilityStatus;
	
	public Clone(Integer cloneNextID) {
		this._id = "c" + cloneNextID; 
		this.modules = new ArrayList<ModuleBaseClass>();
		this.vulnerabilityTypes = new HashSet<String>();
		this.moduleIDs = new HashSet<String>();
		this.node = Factory.mutNode(this._id).add(Shape.DOUBLE_CIRCLE);
		this.vulnerabilityStatus = new HashSet<Integer>();
	}
	
	public HashSet<String> getModuleIDs() {
		return moduleIDs;
	}
	
	.
	.
	.
}
