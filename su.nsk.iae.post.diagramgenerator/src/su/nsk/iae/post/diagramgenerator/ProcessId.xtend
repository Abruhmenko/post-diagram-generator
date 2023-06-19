package su.nsk.iae.post.diagramgenerator

class ProcessId {
	
	var processId = 0
	var String processName
	
	def void setId(int id) { processId = id }
	def void setName(String name) { processName = name }
	
	def int getId() { return processId }
	def String getName() { return processName }
	
}