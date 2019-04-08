package output_formats;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import static guru.nidi.graphviz.model.Factory.*;

import mmd.Clone;
import mmd.Connection;
import mmd.ModuleBaseClass;

public class Dot_builder {
	
	private ArrayList<ModuleBaseClass> modules;
	private MutableGraph graph;
	
	private String fileExtension;
	
	private File imageOutFile ;
	private File txtOutFile;
	private boolean showMetricNames;
	
	
	public Dot_builder(ArrayList<ModuleBaseClass> modulesAndClones,String nameOfOutFile) {	
		this.modules = modulesAndClones;
		this.graph = mutGraph(nameOfOutFile).setDirected(false);
		this.fileExtension = "svg";
		this.imageOutFile = new File(nameOfOutFile+"moduleData."+this.fileExtension);
		this.txtOutFile = new File(nameOfOutFile+"moduleData.dot");
		this.showMetricNames = true;
	}
	
	public void run() {
		HashSet<String> seenModule = new HashSet<String>();
		HashSet<String> seenEdges = new HashSet<String>();
		for(ModuleBaseClass startNode : this.modules) {
			this.processModules(startNode,seenModule,seenEdges);
		}
	}
	
	public void write() {
		
		try {
			// Need to add switching for several different formats
			if(this.fileExtension.equals("svg")) { // Currently the only option is svg.
				try {
					PrintWriter out = new PrintWriter(this.txtOutFile);
					out.print(this.graph.toString());
					out.close();
				}catch(Exception e){
					e.printStackTrace();					
				}
				Graphviz.fromGraph(this.graph).render(Format.SVG).toFile(this.imageOutFile);				
			}
				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void processModules(ModuleBaseClass startNode,HashSet<String> seenModule, HashSet<String> seenEdges) {

		Stack<Connection> ns = new Stack<Connection>();
		
		String name = startNode.getName();

		if(startNode.isVulnerable()) {
			startNode.node.add(Color.RED).add(Style.FILLED);
		}else if(startNode.isClone() && ((Clone) startNode).isMixedVulnerabilityClone()) {
			startNode.node.add(Color.ORANGE).add(Style.FILLED);
		}
		
		this.graph.add(startNode.node.setName(name));
		
		for(Integer i : startNode.getDistanceMapToNearestNeigbors().keySet()) {
			for( Connection ctn : startNode.getAllNearestNeigbors().get(i).values()) {
				ns.push(ctn);
			}
		}		
		
		while(!ns.isEmpty()) {	
			Connection connToNeigbor = ns.pop();
			ModuleBaseClass neighborNode = connToNeigbor.getOtherNode(startNode);
			
			String connection = startNode.getSelf().getId()+"-"+neighborNode.getId();
			String reverseCon = neighborNode.getId()+"-"+startNode.getSelf().getId();
						
			if(!(seenEdges.contains(connection))) {
				seenEdges.add(connection);
				seenEdges.add(reverseCon);
				
				String label = ""+ connToNeigbor.getDistance();
				if(this.showMetricNames) {
					label = String.join(",", connToNeigbor.getHeaderItems());
				}
				
				//This is where we add edges to the graph.
				startNode.node.links().add(
						startNode.node.linkTo(
									neighborNode.node
								).with(Label.of(label)
						)
				);

				this.processModules(neighborNode,seenModule,seenEdges);		
			}
		}
		return;
	}

}
