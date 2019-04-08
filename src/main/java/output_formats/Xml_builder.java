package output_formats;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import mmd.Clone;
import mmd.Connection;
import mmd.Module;
import mmd.ModuleBaseClass;

public class Xml_builder {
	
	private File txtOutFile;
	private HashMap<String, Module> modules;
    private Document dom;
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db;
	private HashMap<String, Connection> connections;
	private HashMap<String, Clone> clones;
	
	public Xml_builder(HashMap<String, Module> modules,HashMap<String, Clone> clones, HashMap<String, Connection> connections, String pathToOutputFiles) throws ParserConfigurationException {
		this.txtOutFile = new File(pathToOutputFiles+"moduleData.xml");
		this.clones = clones;
		this.connections = connections;
		this.modules = modules;
		this.db = dbf.newDocumentBuilder();
		this.dom = this.db.newDocument();

	}

	public void run(String filename) {
		boolean fileNameSet = false;
		Element rootElement = this.dom.createElement("mmd");
		Element e;
		
		e = this.dom.createElement("filename");
		e.setTextContent(filename);
		rootElement.appendChild(e);

		//Printing clones first
		for(ModuleBaseClass tMod : this.clones.values()) {
			Clone c = (Clone) tMod;

			ArrayList<ModuleBaseClass> modules = c.getModules();
			HashSet<String> seenFlags = new HashSet<String>();
			
			Element cg = this.dom.createElement("clone_group");
			Element cd = this.dom.createElement("clone_data");
			Element cid = this.dom.createElement("clone_group_id");
			Element cNum = this.dom.createElement("count_of_clone_group");
			if(c.hasProcessData()) {
				
				// TODO: break this up into the different types of process data.
				Element pData = this.dom.createElement("clone_group_process_data");
				Element cgvsElement = this.dom.createElement("clone_group_vulnerablility_status");
				
				if(c.isMixedVulnerabilityClone()) cgvsElement.setTextContent("MIXED");
				else cgvsElement.setTextContent("" + c.getVulnerabilityStatus().toArray()[0]);
				
				Element cgvcElement = this.dom.createElement("clone_group_vulnerablility_category");
				if(c.isMixedTypeClone()) cgvcElement.setTextContent("MIXED");
				else cgvcElement.setTextContent("" + c.getVulnerabilityStatus().toArray()[0] );
				
				pData.appendChild(cgvsElement);
				pData.appendChild(cgvcElement);
				cd.appendChild(pData);
			}
			
			
			cid.setNodeValue(c.getId());
			
			cNum.setTextContent(""+c.size());
			
			cg.appendChild(cd);
			cd.appendChild(cid);
			cd.appendChild(cNum);
			
			for(ModuleBaseClass cm : modules) {
				Module cloneModule = (Module) cm;
				
				if(!fileNameSet) {
					fileNameSet = true;
				}
				if(!seenFlags.contains("cidSet")) {
					cid.setTextContent(tMod.getSelf().getId());
					seenFlags.add("cidSet");
				}
				
				// Print data on this clone group
				Element cModule = this.dom.createElement("clone_module");
				HashMap<String,Object> idFields = cloneModule.getIdFields();
				for(String field : idFields.keySet()) {
					Element cModIDElementOuter = this.dom.createElement("module_id");
					Element cModIDElement = this.dom.createElement(field.toLowerCase());
					cModIDElement.setTextContent((String) idFields.get(field));
					cModIDElementOuter.appendChild(cModIDElement);		
					cModule.appendChild(cModIDElementOuter);
				}
				
				HashMap<String,String> processData = cloneModule.getAllProcessData();
				Element cmpdElement = this.dom.createElement("module_process_data");
				for(String processTag : processData.keySet()) {			
					Element processTagElement = this.dom.createElement(processTag);
					processTagElement.setTextContent(processData.get(processTag));
					cmpdElement.appendChild(processTagElement);
				}
				cModule.appendChild(cmpdElement);
				cg.appendChild(cModule);
			}
			// Collect and print data about connections
			ArrayList<Connection> neigbors = c.getNeigborArrayList();	
			if(!neigbors.isEmpty()) {
				Element distConnElement = this.dom.createElement("distance_connections");
				for(Connection connectionToNeigbor : neigbors) {
					Element connectionIDElement = this.dom.createElement("connection_id");
					connectionIDElement.setTextContent(connectionToNeigbor.getId());
					distConnElement.appendChild(connectionIDElement);				
				}
				cg.appendChild(distConnElement);
			}
			rootElement.appendChild(cg);
		}
			// We are dealing with normal modules.
		for(Module tMod : this.modules.values()) {
			Element modElement = this.dom.createElement("module");

			HashMap<String,Object> idFields = tMod.getIdFields();
			for(String field : idFields.keySet()) {
				Element cModIDElementOuter = this.dom.createElement("module_id");
				Element cModIDElement = this.dom.createElement(field.toLowerCase());
				cModIDElement.setTextContent((String) idFields.get(field));
				cModIDElementOuter.appendChild(cModIDElement);		
				modElement.appendChild(cModIDElementOuter);
			}
			
			HashMap<String,String> processData = tMod.getAllProcessData();
			Element procDataElement = this.dom.createElement("process_data");		
			for(String processTag : processData.keySet()) {			
				Element processTagElement = this.dom.createElement(processTag);
				processTagElement.setTextContent(processData.get(processTag));
				procDataElement.appendChild(processTagElement);
			}
			modElement.appendChild(procDataElement);
			
			ArrayList<Connection> neigbors = tMod.getNeigborArrayList();
			Element distConnElement = this.dom.createElement("distance_connections");
			for(Connection connection : neigbors) {
				Element connectionIDElement = this.dom.createElement("connection_id");
				connectionIDElement.setTextContent(connection.getId());
				distConnElement.appendChild(connectionIDElement);
				modElement.appendChild(distConnElement);
			}
			

			
			rootElement.appendChild(modElement);
		}				
		// For edges in the graph.
		for(Connection connection : this.connections.values()) {
			Element connectionElement = this.dom.createElement("connection");
			Element connectionIDElement = this.dom.createElement("connection_id");		
			connectionIDElement.setTextContent(connection.getId());
			connectionElement.appendChild(connectionIDElement);
			
			String m1Name = connection.getModules().get(0).getSelf().getId();
			String m2Name = connection.getModules().get(1).getSelf().getId();
			
			Element listOfMetricsElement = this.dom.createElement("list_of_metrics");
			for(String header : connection.getHeaderItems()) {
				Element metricElement = this.dom.createElement("metric");
				
				Element metricNameElement = this.dom.createElement("metric_name");
				metricNameElement.setTextContent(header);
				
				Element m1ValueElement = this.dom.createElement("value");
				m1ValueElement.setAttribute("module",m1Name);
				m1ValueElement.setTextContent("" + connection.getHeadertoValue(header,0));
				
				Element m2ValueElement = this.dom.createElement("value");
				m2ValueElement.setAttribute("module",m2Name);
				m2ValueElement.setTextContent("" + connection.getHeadertoValue(header,1));
				
				metricElement.appendChild(metricNameElement);
				metricElement.appendChild(m1ValueElement);
				metricElement.appendChild(m2ValueElement);
				
				listOfMetricsElement.appendChild(metricElement);
			}
					
			connectionElement.appendChild(listOfMetricsElement);
			
			rootElement.appendChild(connectionElement);			
		}
		
		this.dom.appendChild((Node) rootElement);
	}
	
	public void write() {
		 try {
	            Transformer tr = TransformerFactory.newInstance().newTransformer();
	            tr.setOutputProperty(OutputKeys.INDENT, "yes");
	            tr.setOutputProperty(OutputKeys.METHOD, "xml");
	            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	            tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
	            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

	            // send DOM to file
				tr.transform(new DOMSource(this.dom), 
	                                 new StreamResult(new FileOutputStream(this.txtOutFile)));

	        } catch (TransformerException te) {
	            System.out.println(te.getMessage());
	        } catch (IOException ioe) {
	            System.out.println(ioe.getMessage());
	        }
	}
}
