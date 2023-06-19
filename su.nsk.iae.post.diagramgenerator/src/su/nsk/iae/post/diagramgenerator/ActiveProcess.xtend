package su.nsk.iae.post.diagramgenerator

class ActiveProcess {
	
	var int idFrom
	var int idTo
	var String action
	
	new () {
		idFrom = 0
		idTo = 0
		action = ""
	}
	
	def void setAction(String act) { action = act }
	def void setIdFrom(int id) { idFrom = id }
	def void setIdTo(int id) { idTo = id }
	
	def String getAction() { return action }
	def int getIdFrom() { return idFrom }
	def int getIdTo() { return idTo }
	
}