package su.nsk.iae.post.diagramgenerator

import java.util.ArrayList

class GMLTextGenerator {

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Метод возвращает строку, содержащую заголовок выходного GML файла. Также обнуляется счетчик вершин (count_id)
//
// Method returns string, which is a head of output GML file. Also nodes counter is made zero 
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	def writeHeadGML(ProcessDiagramGenerator generator) '''
	Creator	"tranlator"
	Version	"2.15"
	graph
	[
		hierarchic	1
		label	""
		directed	1«generator.zeroCountId()»
	'''
	
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//Метод получает на вход длину подписи вершины и ее тип (те форму), генерирует строку с настройками отобоажения ноды диаграммы (длина, высота, цвет границы, форма вершины, и тд)
//
// Method input: node's label length (nameLength) and its shape (typeOfNode)
// Output: generated string which have a node's properties like shape, height, weight, colors and etc 
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def generateNodeGraphics (int nameLength, String typeOfNode) '''
			graphics
			[
				w	«(nameLength * 10)».0
				h	48.0
				type	"«typeOfNode»"
				raisedBorder	0
				fill	"#FFFFFF"
				outline	"#000000"
			]
	'''
	
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Метод генерирует LabelGraphics, задающий настройки щрифта и текст подписи внутри вершины. На вход получает подпись для вершины
//
// input: label which will be in the graph's node
// Output: string which have a LabelGraphics info about label, like label text, size and font of them
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	def generateLabelGraphics(String label)
	'''
			LabelGraphics
			[
				text	"«label»"
				fontSize	12
				fontName	"Dialog"
			]
	'''
	
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// На вход поступает число, которое будет присвоено вершине в качестве Id, и текст, который будет внутри вершины  
// Метод возвращает строку, содержащую объявление одной вершины в gml графе,
// вызывая при этом generateNodeGraphics(), generateLabelGraphics() для генерации отдельных частей описания вершины
//
//Input: processId - will be put to gml like node's id
//		processName - label
//		typeOfNode - node's shape
// Output: string which will have a full defining of one node in the output GML diagram.
// Method calls generateNodeGraphics(), generateLabelGraphics() for generate some parts of node's notification
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def generateOneProcessNode(int processId, String processName, String typeOfNode)
	'''
	node
	[
		id	«processId»
		label	"«processName»"
	    «generateNodeGraphics(processName.length, typeOfNode)»
		«generateLabelGraphics(processName)»
	]
	'''
	
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Метод принимает fromId - Id вершины, из которой идет ребро. toId -  Id вершины, в которую идет ребро. label - подпись над ребром.
// Возвращает строку, содержащую описание ребра в формате gml с заданными в параметрах свойствами.
//
// Input: fromId - Id of node, from which will go that edge
//		  toId - Id of node to which will go that edge
//		  label - edge's label text
// Output: string with edge's notification
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def generateOneEdge(int fromId, int toId, String label)
	'''
	edge
	[
		source	«fromId»
		target	«toId»
		label	"«label»"
	]
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
			tempString += generateOneEdge(procList.get(i).idFrom, procList.get(i).idTo, procList.get(i).action) // конкатенация описаний ребер
		}
		return tempString
	}


//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Метод возвращает строку, содержащую в себе объявления всех вершин создаваемой диаграммы процессов. Запоминает соответствие имени процесса его Id в списке procId
// shape определяет форму вершин процессов
//
//Output: string which have a  notification of all process nodes. Also method is saving accord of process name and its id in ArrayList procId
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def String generateNodes(ProcessDiagramGenerator generator/* ,  HashMap<String, DiagramNode> nodeList*/)
	{
		var String tempString = "";
		for (e : generator.procId.values()) //получаем список всех node, и проходим по нему
		{ 
	         tempString += generateOneProcessNode(generator.getElementIndexProcId(e.getName()), e.getName(), e.getShape()) // для каждого процесса генерируем строковое описание вершины графа, и конкатенируем его к предыдущим
	         //generator.addElementToProcId(generator.getCountId(), e.name)// procId.add(count_id, e.name) // запоминаем соответствие имени процесса назначенному ему Id
	         //generator.incrementCountId()// инкрементируем счетчик процессов (это число будет Id для вершины следующего процесса)
	     }
	    return tempString
	}
	
}