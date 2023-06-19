package su.nsk.iae.post.diagramgenerator

class DiagramNode {
	
	var int index
	var String name
	var String shape
	var int visited
	
	new(String name, String shape) {
		this.name = name
		this.shape = shape
		visited = -1
	}
	
	new(String name) {
		this.name = name
		this.shape = "roundrectangle"
		visited = -1
	}
	
	def setIndex(int ind) { index = ind }
	def setName(String newName) { name = newName }
	def setShape(String newShape) { shape = newShape }
	def void setVisited(int visit) { visited = visit }
	
	def int getIndex() { return index }
	def String getName() { return name }
	def String getShape() { return shape }
	def int getVisited() { return visited }
	
}