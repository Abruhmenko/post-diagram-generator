package su.nsk.iae.post.diagramgenerator

import java.util.ArrayList
import java.util.HashMap

class ResultOfSeparation {
	
	var ArrayList<ArrayList<ActiveProcess>> diagramComponents
	var ArrayList<HashMap<String, DiagramNode>> diagramComponentNodes
	
	def setDiagramComponents(ArrayList<ArrayList<ActiveProcess>> components) {
		diagramComponents = components
	}
	
	def setDiagramComponentNodes(ArrayList<HashMap<String, DiagramNode>> compNodes) {
		diagramComponentNodes = compNodes
	}
	
	def ArrayList<ArrayList<ActiveProcess>> getDiagramComponents() {
		return diagramComponents
	}
	
	def ArrayList<HashMap<String, DiagramNode>> getDiagramComponentNodes() {
		return diagramComponentNodes
	}
	
}