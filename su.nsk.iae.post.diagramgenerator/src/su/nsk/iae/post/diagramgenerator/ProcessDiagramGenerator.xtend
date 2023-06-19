package su.nsk.iae.post.diagramgenerator

import java.util.ArrayList
import java.util.HashMap

class ProcessDiagramGenerator {
	
	protected var count_id = 0
	protected var ArrayList<ActiveProcess> procList = new ArrayList<ActiveProcess>
	protected var HashMap<String, DiagramNode> procId = new HashMap<String, DiagramNode>()
	protected var GMLTextGenerator gmlTextGenerator = new GMLTextGenerator()
	
	new() {
		count_id = 0
	}
	
	def addElementToProcList(ActiveProcess elem) {
		procList.add(elem)
	}
	 
	def addAllElementsToProcList(ArrayList<ActiveProcess> list) {
		procList.addAll(list)
	}
	
	def addElementToProcId(DiagramNode elem) {
		elem.index = count_id
		procId.put(elem.name, elem)
		count_id++
	}
	
	def addElementToProcId(int ind, DiagramNode elem) {
		elem.index = ind
		procId.put(elem.name, elem)
	}
	
	def DiagramNode getElementProcId(String key) {
		return procId.get(key)
	}
	
	def int getElementIndexProcId(String key) {
		return procId.get(key).index
	}
	
	def zeroCountId() {
		count_id = 0
	}
	
	def incrementCountId() {
		count_id ++;
	}
	
	def int getCountId() {
		return count_id
	}
	
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Очистка памяти, обнуление счетчиков
//
// garbage collector, make variables zero
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def void clear() {
	 	procList.clear()
      	count_id = 0
      	procId.clear()
	}
	
}