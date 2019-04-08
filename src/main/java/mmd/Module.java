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
	
	.
	.
	.
}
