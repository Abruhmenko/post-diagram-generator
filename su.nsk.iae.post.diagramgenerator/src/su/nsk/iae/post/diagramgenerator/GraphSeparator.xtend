package su.nsk.iae.post.diagramgenerator

import java.util.LinkedList
import java.util.ArrayList
import java.util.HashMap
import java.util.LinkedHashSet
import java.util.Collections

class GraphSeparator {
	
	var ResultOfSeparation result = new ResultOfSeparation()
	var LinkedList<DiagramNode> nodesQuery = new LinkedList<DiagramNode>() //query
	var LinkedHashSet<Integer> deleteIndex = new LinkedHashSet<Integer>() 
	
	var ArrayList<Integer> deleteIndexNotUnique = new ArrayList<Integer>() 
	var ArrayList<ArrayList<ActiveProcess>> componentList = new ArrayList<ArrayList<ActiveProcess>>
	var nodeList = new ArrayList<HashMap<String, DiagramNode>>()
	var int currentComponentIndex = 0
	
	
	def ResultOfSeparation separateGraph(ArrayList<ActiveProcess> procList, HashMap<String, DiagramNode> nodeIdList) {
		/*nodeList.add(nodeIdList)
		componentList.add(procList)
		result.setDiagramComponents(componentList)
		result.setDiagramComponentNodes(nodeList)
		return result
		*/
		
		componentList.clear()
		deleteIndex.clear()
		nodesQuery.clear()
		nodeList.clear()
		currentComponentIndex = 0
		
		for (e: nodeIdList.keySet()) {
			var ArrayList<ActiveProcess> tempComponent = new ArrayList<ActiveProcess>
			var tempNodeList = new HashMap<String, DiagramNode>()
			if ((nodeIdList.get(e).getVisited() == -1)&&(nodeIdList.get(e).shape == "roundrectangle")) {
				nodesQuery.addLast(nodeIdList.get(e))
			}
			deleteIndex.clear()
			deleteIndexNotUnique.clear()
			
			while (nodesQuery.size() > 0) {
				var DiagramNode element = nodesQuery.pollFirst(); //pop
				System.out.println("pop: "+ element.name)
				System.out.println("nodeList:")
					for (var i = 0; i < nodeList.size(); i ++) {	
						System.out.print("i:" + i + " ")

						for (g: nodeList.get(i).values()) {
							System.out.print(" " + g.name  + "(" + g.visited + ") ")
						}
						System.out.println(" ")
					}
					
				if (element.getVisited() == -1) { //didn't visited
					element.setVisited(currentComponentIndex)
					System.out.print("didn't visited (" + element.name + "): " + currentComponentIndex+"  ")
					tempComponent.addAll(getActiveListByNode(procList, element))
					tempNodeList.put(element.name, element)//add node to that component
					
					var ArrayList <DiagramNode> childrenList = getElementList(procList, nodeIdList, element)
					for (ch: childrenList) {
						System.out.print(",  ch:  " + ch.name)
					}						
					System.out.println(" ")
					nodesQuery.addAll(childrenList)//get all of his children
					System.out.println(" Query:")
					for (var a = 0; a < nodesQuery.size(); a ++) {
						System.out.print("  " + nodesQuery.get(a).name + "(" + nodesQuery.get(a).visited + ") ")
					}
					System.out.println(" ")
				}
				else {
					System.out.println("element.getVisited() = " + element.visited + ", currentComponentIndex = " + currentComponentIndex)
					if (element.getVisited() != currentComponentIndex) { // we found a head of another component
						if (element.getVisited() < currentComponentIndex) { // we found a head of another component
							System.out.println("< currentIndex")
							/*System.out.println("nodeList:")
							for(var i = 0; i < nodeList.size(); i ++)
							{	
								System.out.print("i:" + i + " ")
								for(g: nodeList.get(i).values())
								{
									System.out.print(" " + g.name)
								}
								System.out.println(" ")
							}*/
							System.out.println("element.name = " + element.name + " element.visited = " + element.getVisited())
							setVisitedForList(tempNodeList, element.getVisited()) //rewrite new component number
							tempComponent.addAll(componentList.get(element.getVisited())) // save all nodes which was found before
							tempNodeList.putAll(nodeList.get(element.getVisited()))
							
							System.out.println("last component = " + currentComponentIndex)
							currentComponentIndex = element.getVisited()
							System.out.println("now component = " + currentComponentIndex)
							//componentList.remove(element.getVisited())
							//nodeList.remove(element.getVisited())
							deleteIndex.add(element.getVisited())
							deleteIndexNotUnique.add(element.getVisited())
							//deleteIndex.remove(currentComponentIndex)
						}
						else
						if (element.getVisited() > currentComponentIndex) { // we found a head of another component
							System.out.println("> currentIndex")
							/*System.out.println("nodeList:")
							for(var i = 0; i < nodeList.size(); i ++)
							{	
								System.out.print("i:" + i + " ")
								for(g: nodeList.get(i).values())
								{
									System.out.print(" " + g.name)
								}
								System.out.println(" ")
							}*/
							var int lastComponentIndex = element.getVisited()
							System.out.println("element.name = " + element.name + " element.visited = " + lastComponentIndex)
							setVisitedForList(nodeList.get(lastComponentIndex), currentComponentIndex) //rewrite new component number
							tempComponent.addAll(componentList.get(lastComponentIndex)) // save all nodes which was found before
							tempNodeList.putAll(nodeList.get(lastComponentIndex))
						
							System.out.println("now component = " + currentComponentIndex + ", last:" + lastComponentIndex)
							deleteIndex.add(lastComponentIndex)
							deleteIndexNotUnique.add(lastComponentIndex)
							//deleteIndex.remove(currentComponentIndex)
							//componentList.remove(lastComponentIndex)
							//nodeList.remove(lastComponentIndex)
						}
					}
				}
			}
			
			Collections.sort(deleteIndexNotUnique)
			for (var i = 0; i < deleteIndexNotUnique.size(); i ++) {
				if (deleteIndex.contains(deleteIndexNotUnique.get(i))) {
					deleteIndex.remove(deleteIndexNotUnique.get(i))
					componentList.remove(deleteIndexNotUnique.get(i) - i)
					nodeList.remove(deleteIndexNotUnique.get(i) - i)
				}
			}
			if (tempNodeList.size() > 0) {
				componentList.add(currentComponentIndex, tempComponent)
				nodeList.add(currentComponentIndex, tempNodeList)
			}
			for (var t = 0; t < nodeList.size(); t ++) {
				setVisitedForList(nodeList.get(t), t) //rewrite new component number
			}
			currentComponentIndex = componentList.size()
			System.out.println("size = " + currentComponentIndex)
		}
		//return componentList;
		result.setDiagramComponents(componentList)
		result.setDiagramComponentNodes(nodeList)
		return result 
	}
	
	
	def ArrayList <DiagramNode> getElementList(ArrayList<ActiveProcess> procList, HashMap<String, DiagramNode> nodeIdList, DiagramNode element) {
		var ArrayList <DiagramNode> tempList = new ArrayList <DiagramNode>
		var ArrayList<Integer> idList = new ArrayList<Integer>
		var int idElem = element.index
		for (e: procList) {
			if ((e.idFrom == idElem)) {
				idList.add(e.idTo)
			}
		}
		for (e: nodeIdList.values()) {
			for (in: idList) {
				if (e.index == in) {
					tempList.add(e)
				}
			}
		}
		return tempList
	}
	
	
	def ArrayList<ActiveProcess> getActiveListByNode(ArrayList<ActiveProcess> procList, DiagramNode element) {
		var ArrayList<ActiveProcess> tmpList = new ArrayList<ActiveProcess>()
		for (e: procList) {
			if (e.idFrom == element.index) {
				tmpList.add(e)
			}
		}
		return tmpList
	}
	
	
	def void setVisitedForList(HashMap<String, DiagramNode> tempNodeList, int component) {
		for (e: tempNodeList.values()) {
			for (nodeInQ: nodesQuery) {
				if (nodeInQ.name == e.name) {
					nodeInQ.setVisited(component)
				}
			}
			e.setVisited(component)
		}
	}
	
	
	def DiagramNode getElementNodeIdList(HashMap<String, DiagramNode> nodeIdList, int index) {
		for (e: nodeIdList.values()) {
			if (e.index == index)
				return e
		}
		return null
	}
	
}