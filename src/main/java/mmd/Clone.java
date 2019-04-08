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
	
	private void setVulnerabilityStatus(Integer status) {
		this.vulnerabilityStatus.add(status);
	}
	
	public HashSet<Integer> getVulnerabilityStatus(){
		return this.vulnerabilityStatus;
	}
	
	public HashSet<String> getVulnerabilityTypes() {
		return this.vulnerabilityTypes;
	}

	public void setVulnerabilityType(String vulnType) {
		this.vulnerabilityTypes.add(vulnType);
	}

	public void addModule(Module m) {
		if(!this.moduleIDs.contains(m.getSelf().getId())) {
			this.moduleIDs.add(m.getId());
			this.modules.add(m);
			this.addModuleProcessData(m);
			if(m.getVulnerabilityType() != "") this.setVulnerabilityType(m.getVulnerabilityType());
			if(m.getVulnerabilityStatus() != -1)this.setVulnerabilityStatus(m.getVulnerabilityStatus());
			m.updateConnections(this);
			this.migrateConnections(m);
		}
	}
	
	private void addModuleProcessData(Module m) {
		this.processData = m.getAllProcessData();
	}

	public ArrayList<ModuleBaseClass> getModules(){
		return this.modules;
	}
	
	public boolean isMixedVulnerabilityClone() {
		return this.vulnerabilityStatus.size() > 1; 
	}
	
	public boolean isVulnerable() {
		return (this.vulnerabilityStatus.size() == 1 && this.vulnerabilityStatus.contains(1));
	}
	
	public boolean isMixedTypeClone() {
		return this.vulnerabilityTypes.size() > 1;
	}

	public String getId() {
		return this._id;
	}

	public int size() {
		return this.modules.size();
	}
 	
	public boolean isClone() {
		return true;
	}
	
	public ModuleBaseClass getSelf() {
		return this;
	}
	
	public void migrateConnections(Module fromMod) {
		SortedMap<Integer,HashMap<String,Connection>> nn = fromMod.getAllNearestNeigbors();
		for(Integer i : nn.keySet()) {
			if(i==0) continue;
			for(String ks : nn.get(i).keySet()) {
				Connection c = nn.get(i).get(ks);
				this.addNeigbor(c);
			}
		}	
	}
	
	@Override
	public HashMap<String, Object> getIdFields() {
		return this.modules.get(0).getIdFields();
	}
	
	@Override
	public String getName() {
		return this.getId();
	}
}
