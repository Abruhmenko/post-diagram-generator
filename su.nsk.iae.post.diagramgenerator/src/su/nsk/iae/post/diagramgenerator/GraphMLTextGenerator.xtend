package su.nsk.iae.post.diagramgenerator

import java.util.ArrayList

class GraphMLTextGenerator {
	
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Метод возвращает строку, содержащую заголовок выходного GraphML файла. Также обнуляется счетчик вершин (count_id)
//
// Method returns string, which is a head of output GraphML file. Also nodes counter is made zero 
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------		
	def headGraphMlGenerator(ProcessDiagramGenerator generator)
	'''
	<?xml version="1.0" encoding="UTF-8" standalone="no"?>
	<graphml xmlns="http://graphml.graphdrawing.org/xmlns" xmlns:java="http://www.yworks.com/xml/yfiles-common/1.0/java" xmlns:sys="http://www.yworks.com/xml/yfiles-common/markup/primitives/2.0" xmlns:x="http://www.yworks.com/xml/yfiles-common/markup/2.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:y="http://www.yworks.com/xml/graphml" xmlns:yed="http://www.yworks.com/xml/yed/3" xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns http://www.yworks.com/xml/schema/graphml/1.1/ygraphml.xsd">
	  <key for="port" id="d0" yfiles.type="portgraphics"/>
	  <key for="port" id="d1" yfiles.type="portgeometry"/>
	  <key for="port" id="d2" yfiles.type="portuserdata"/>
	  <key attr.name="url" attr.type="string" for="node" id="d3"/>
	  <key for="node" id="d5" yfiles.type="nodegraphics"/>
	  <key for="graphml" id="d6" yfiles.type="resources"/>
	  <key for="edge" id="d9" yfiles.type="edgegraphics"/>«generator.zeroCountId()»
	'''
	
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Method returns string, which declare the nodes properties like shape and label 
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def generateShapeNodeGraphML(int nameLength, String processName, String shapetype) '''
	        <y:ShapeNode>
	          <y:Geometry height="48.0" width="«(nameLength * 10)»"/>
	          <y:Fill color="#FFFFFF" transparent="false"/>
	          <y:BorderStyle color="#000000" raised="false" type="line" width="1.0"/>
	          <y:NodeLabel alignment="center" fontFamily="Dialog" fontSize="12" fontStyle="plain" hasBackgroundColor="false" hasLineColor="false" height="18.701171875" horizontalTextPosition="center" iconTextGap="4" modelName="internal" modelPosition="c" textColor="#000000" verticalTextPosition="bottom" visible="true" width="26.6640625">«processName»</y:NodeLabel>
	          <y:Shape type="«shapetype»"/>
	        </y:ShapeNode>
	'''
	
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Method returns string, which declare the nodes properties and other data keys like url 
//urlStatechartDiagram - is a link in file system to statechart diagram for that process
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def dataKeysGraphMLGenerator(int nameLength, String processName, String shapetype, String urlStatechartDiagram)'''
	      <data key="d3"><![CDATA[«urlStatechartDiagram»]]></data>
	      <data key="d5">
	        «generateShapeNodeGraphML(nameLength, processName, shapetype)»
	      </data>
	'''
	
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Method returns string, which declare one node in GraphML format 
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def nodeGraphMLGenerate(int processId, String processName, String shapetype, String urlStatechartDiagram)'''
	    <node id="n«processId»">
	      «dataKeysGraphMLGenerator(processName.length(), processName, shapetype, urlStatechartDiagram)»
	    </node>
	'''
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Method returns string, which declare one edge in GraphML format
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	def generateOneEdgeGraphML(int edgeId, int fromId, int toId, String edgeLabel)
	'''
	<edge id="e«edgeId»" source="n«fromId»" target="n«toId»">
	      <data key="d9">
	        «generatePoliLineEdgeGraphML(edgeLabel)»
	      </data>
	</edge>
	'''
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Method returns string, which declare one edge's properties in GraphML format (like edge label)
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	def generatePoliLineEdgeGraphML(String edgeLabel)'''
		        <y:PolyLineEdge>
		          <y:LineStyle color="#000000" type="line" width="1.0"/>
		          <y:Arrows source="none" target="standard"/>
		          <y:EdgeLabel alignment="center" fontFamily="Dialog" fontSize="12" fontStyle="plain" hasBackgroundColor="false" hasLineColor="false" horizontalTextPosition="center" iconTextGap="4" preferredPlacement="anywhere" textColor="#000000" verticalTextPosition="bottom" visible="true">«edgeLabel»<y:PreferredPlacementDescriptor/>
		          </y:EdgeLabel>
		        </y:PolyLineEdge>
	'''
    
    
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Возвращает строку, содержащую описание всех ребер графа диаграммы процессов
//
//Output: string with notification of all edges in graph
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def String generateAllEdges(ArrayList<ActiveProcess> procList)
	{
		var String tempString = ""
		for (var int i; i < procList.size; i ++) // идем по созданной заранее модели
		{
			tempString += generateOneEdgeGraphML(i, procList.get(i).idFrom, procList.get(i).idTo, procList.get(i).action)// конкатенация описаний ребер
		}
		return tempString
	}


//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Метод возвращает строку, содержащую в себе объявления всех вершин создаваемой диаграммы процессов. Запоминает соответствие имени процесса его Id в списке procId
// shape определяет форму вершин процессов
//
//Output: string which have a  notification of all process nodes. Also method is saving accord of process name and its id in ArrayList procId
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def String generateNodes(ProcessDiagramGenerator generator/*, HashMap<String, DiagramNode> nodeList */, String url, String statechartFileNameTail)
	{
		var String tempString = "";
		for (e : generator.procId.values()) //получаем список всех процессов, и проходим по нему
		{ 
	         tempString += nodeGraphMLGenerate(generator.getElementIndexProcId(e.getName()), e.getName(), e.getShape(), url + "/" + e.name + statechartFileNameTail)// для каждого процесса генерируем строковое описание вершины графа, и конкатенируем его к предыдущим
	         //generator.addElementToProcId(generator.getCountId(), e.name)// procId.add(count_id, e.name) // этот список создается при генерации этой диаграммы в GML
	         //generator.incrementCountId()// инкрементируем счетчик процессов (это число будет Id для вершины следующего процесса)
	     }
	    return tempString
	}
	
}