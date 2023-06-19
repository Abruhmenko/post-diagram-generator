package su.nsk.iae.post.diagramgenerator

import java.util.HashMap
import org.eclipse.emf.ecore.resource.Resource
import java.util.ArrayList
import su.nsk.iae.post.poST.Process
import su.nsk.iae.post.poST.VarDeclaration
import su.nsk.iae.post.poST.InputVarDeclaration
import su.nsk.iae.post.poST.OutputVarDeclaration
import su.nsk.iae.post.poST.IfStatement
import su.nsk.iae.post.poST.CaseStatement
import su.nsk.iae.post.poST.StatementList
import su.nsk.iae.post.poST.TimeoutStatement
import su.nsk.iae.post.poST.AssignmentStatement
import java.util.HashSet
import org.eclipse.emf.ecore.EObject
import su.nsk.iae.post.poST.ForStatement
import su.nsk.iae.post.poST.WhileStatement
import su.nsk.iae.post.poST.RepeatStatement
import su.nsk.iae.post.poST.Expression
import su.nsk.iae.post.poST.XorExpression
import su.nsk.iae.post.poST.AndExpression
import su.nsk.iae.post.poST.CompExpression
import su.nsk.iae.post.poST.PrimaryExpression
import su.nsk.iae.post.poST.UnaryExpression
import su.nsk.iae.post.poST.PowerExpression
import su.nsk.iae.post.poST.MulExpression
import su.nsk.iae.post.poST.AddExpression
import su.nsk.iae.post.poST.EquExpression

class DataDiagramGenerator extends ProcessDiagramGraphMLGenerator {
	
	var HashMap<String, Integer> variableId = new HashMap<String, Integer>();

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Метод возвращает строку, содержащую описание вершин переменных в формате gml для диаграммы связи по данным
//
// Output: string with data nodes notification in the GML format
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def getVariablesNodes(Resource resource)
	{
		count_id = procId.size()
		for (varBlock : resource.allContents.toIterable.filter(VarDeclaration)) // идем по объявленным переменным
		{
			for (variablesList : varBlock.vars) {
				var varType = variablesList.spec.type
				for (variable : variablesList.varList.vars) {
					var DiagramNode newNode = new DiagramNode("VAR\n" + variable.name + " : " + varType, "ellipse")
					addElementToProcId(newNode)
		 			variableId.put(variable.name, count_id - 1) // запоминаем соответствие имени переменной назначенной ее вершине Id
				}
			}
	    }
	    for (varBlock : resource.allContents.toIterable.filter(InputVarDeclaration)) {
			for (variablesList : varBlock.vars) {
				var varType = variablesList.spec.type
				for (variable : variablesList.varList.vars) {
					var DiagramNode newNode = new DiagramNode("VAR_INPUT\n" + variable.name + " : " + varType, "ellipse")
					addElementToProcId(newNode)
		 			variableId.put(variable.name, count_id - 1)
				}
			}
	    }
	    for (varBlock : resource.allContents.toIterable.filter(OutputVarDeclaration)) {
			for (variablesList : varBlock.vars) {
				var varType = variablesList.spec.type
				for (variable : variablesList.varList.vars) {
					var DiagramNode newNode = new DiagramNode("VAR_OUTPUT\n" + variable.name + " : " + varType, "ellipse")
					addElementToProcId(newNode)
		 			variableId.put(variable.name, count_id - 1)
				}
			}
	    }
	    
	    zeroCountId()
	}

 
 /// Рекурсивный метод поиска обращений к переменным (возвращает множество имён переменных, без повторений)
 def HashSet<String> findVarRefsInExpr(EObject expression) {
 	var HashSet<String> resultSet = new HashSet<String>
 	switch expression {
 		PrimaryExpression: {
 			if (expression.nestExpr !== null) {
 				resultSet.addAll(findVarRefsInExpr(expression.nestExpr))
 				return resultSet
 			}
 			if (expression.variable !== null) {
 				resultSet.add(expression.variable.name)
 				return resultSet
 			}
 			if (expression.array !== null) {
 				resultSet.add(expression.array.variable.name)
 				resultSet.addAll(findVarRefsInExpr(expression.array.index))
 				return resultSet
 			}
 			if (expression.funCall !== null && expression.funCall.args !== null) {
 				for (arg : expression.funCall.args.elements) {
 					resultSet.add(arg.variable.name)
 					resultSet.addAll(findVarRefsInExpr(arg.value))
 				}
 				return resultSet
 			}
 			return resultSet
 		}
 		UnaryExpression: {
 			if (expression.right !== null) {
 				resultSet.addAll(findVarRefsInExpr(expression.right))
 			}
 			else {
 				resultSet.addAll(findVarRefsInExpr(expression as PrimaryExpression))
 			}
 			return resultSet
 		}
 		PowerExpression: {
 			resultSet.addAll(findVarRefsInExpr(expression.left))
 			resultSet.addAll(findVarRefsInExpr(expression.right))
 			return resultSet
 		}
 		MulExpression: {
 			resultSet.addAll(findVarRefsInExpr(expression.left))
 			resultSet.addAll(findVarRefsInExpr(expression.right))
 			return resultSet
 		}
 		AddExpression: {
 			resultSet.addAll(findVarRefsInExpr(expression.left))
 			resultSet.addAll(findVarRefsInExpr(expression.right))
 			return resultSet
 		}
 		EquExpression: {
 			resultSet.addAll(findVarRefsInExpr(expression.left))
 			resultSet.addAll(findVarRefsInExpr(expression.right))
 			return resultSet
 		}
 		CompExpression: {
 			resultSet.addAll(findVarRefsInExpr(expression.left))
 			resultSet.addAll(findVarRefsInExpr(expression.right))
 			return resultSet
 		}
 		AndExpression: {
 			resultSet.addAll(findVarRefsInExpr(expression.left))
 			resultSet.addAll(findVarRefsInExpr(expression.right))
 			return resultSet
 		}
 		XorExpression: {
 			resultSet.addAll(findVarRefsInExpr(expression.left))
 			resultSet.addAll(findVarRefsInExpr(expression.right))
 			return resultSet
 		}
 		default: {
 			return resultSet
 		}
 	}
 }
 
 /// Рекурсивный метод поиска обращений к переменным (возвращает множество имён переменных, без повторений)
 def HashSet<String> findVarRefsInStatement(EObject statement) {
 	var HashSet<String> resultSet = new HashSet<String>
 	switch statement {
 		Expression: {
 			resultSet.addAll(findVarRefsInExpr(statement))
 			return resultSet
 		}
 		StatementList: {
 			for (s : statement.statements) {
 				resultSet.addAll(findVarRefsInStatement(s))
 			}
 			return resultSet
 		}
 		TimeoutStatement: {
 			if (statement.variable !== null) {
 				resultSet.add(statement.variable.name)
 			}
 			resultSet.addAll(findVarRefsInStatement(statement.statement))
 			return resultSet
 		}
 		AssignmentStatement: {
 			if (statement.variable !== null) {
 				resultSet.add(statement.variable.name)
 			}
 			resultSet.addAll(findVarRefsInStatement(statement.value))
 			return resultSet
 		}
 		IfStatement: {
 			resultSet.addAll(findVarRefsInStatement(statement.mainCond))
 			resultSet.addAll(findVarRefsInStatement(statement.mainStatement))
 			if (statement.elseIfCond !== null) {
 				for (cond : statement.elseIfCond) {
 					resultSet.addAll(findVarRefsInStatement(cond))
 				}
 				for (condStatement : statement.elseIfStatements) {
 					resultSet.addAll(findVarRefsInStatement(condStatement))
 				}
 			}
 			if (statement.elseStatement !== null) {
 				resultSet.addAll(findVarRefsInStatement(statement.elseStatement))
 			}
 			return resultSet
 		}
 		CaseStatement: {
 			resultSet.addAll(findVarRefsInStatement(statement.cond))
 			for (caseElement : statement.caseElements) {
 				resultSet.addAll(findVarRefsInStatement(caseElement.statement))
 			}
 			if (statement.elseStatement !== null) {
 				resultSet.addAll(findVarRefsInStatement(statement.elseStatement))
 			}
 			return resultSet
 		}
 		ForStatement: {
 			resultSet.add(statement.variable.name)
 			resultSet.addAll(findVarRefsInStatement(statement.forList.start))
 			resultSet.addAll(findVarRefsInStatement(statement.forList.end))
 			if (statement.forList.step !== null) {
 				resultSet.addAll(findVarRefsInStatement(statement.forList.step))
 			}
 			resultSet.addAll(findVarRefsInStatement(statement.statement))
 			return resultSet
 		}
 		WhileStatement: {
 			resultSet.addAll(findVarRefsInStatement(statement.cond))
 			resultSet.addAll(findVarRefsInStatement(statement.statement))
 			return resultSet
 		}
 		RepeatStatement: {
 			resultSet.addAll(findVarRefsInStatement(statement.statement))
 			resultSet.addAll(findVarRefsInStatement(statement.cond))
 			return resultSet
 		}
 		default: { // Остальные типы не могут содержать обращений к переменным
 			return resultSet
 		}
 	}
 }
 
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Метод создает модель связи процессов и переменных в формате ArrayList<ActiveProcess> (procList)
//
//Method construct the model of connecting behind processes and variables like an ArrayList<ActiveProcess> (procList)
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def generateDataModel(Resource resource)
	{
		for (process : resource.allContents.toIterable.filter(Process)) 
		{
			for (varBlock : process.procVars) {
				for (variablesList : varBlock.vars) {
					for (variable : variablesList.varList.vars) {
						var ActiveProcess node = new ActiveProcess
						node.idFrom = getElementIndexProcId(process.name)
						node.idTo = variableId.get(variable.name)
						node.action = "declare"
						procList.add(node)
					}
				}
			}
			
			/// проходим по всем упоминаниям переменных, и сохраняем их имена 
			var HashSet<String> procVarRefs = new HashSet<String>
			for (state : process.states) {
				for (statement: state.statement.statements) {
	         		procVarRefs.addAll(findVarRefsInStatement(statement))
				}
			}
			
			/// проходим по созданному выше множеству, и для каждого имени переменной создаём ребро от него к данному процессу
			for (varName : procVarRefs) {
				var ActiveProcess node = new ActiveProcess
				node.idFrom = getElementIndexProcId(process.name)
				node.idTo = variableId.get(varName)
				node.action = "use" 
				procList.add(node)
			}
		}
	}
 
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
def generateDataDiagramModel(Resource resource)
{
	generateProcList(resource)
	getVariablesNodes(resource)
	generateDataModel(resource)
}
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Метод соединяет вместе заголовок gml файла, список вершин диаграммы и список ребер. Для этого вызываются соответствующие методы. Возвращает готовый текст data-диаграммы
//
// Method is collecting a GML head, nodes list (process nodes and data nodes) and edge list of diagram by calling the same methods 
// Output: finished text of GML data diagram
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def String generateDataDiagram(HashMap<String, DiagramNode> nodesId, ArrayList<ActiveProcess> diagramModel)
	{
		var String result = ""
		System.out.print("Generate GML data diagram...")
		procId = nodesId
		procList = diagramModel
		result += '''«gmlTextGenerator.writeHeadGML(this)»
		'''
		result += '''	«gmlTextGenerator.generateNodes(this/*, nodesId */)»
		'''
		result += '''	«gmlTextGenerator.generateAllEdges(diagramModel)»
		'''
		result += "]"
		System.out.println("done.")
		return result
	}

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Метод соединяет вместе заголовок GraphML файла, список вершин диаграммы и список ребер. Для этого вызываются соответствующие методы. Возвращает готовый текст data-диаграммы
//
// Method is collecting a GraphML head, nodes list (process nodes and data nodes) and edge list of diagram by calling the same methods 
// Output: finished text of GraphML data diagram
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def String generateDataGraphMLDiagram(HashMap<String, DiagramNode> nodesId, ArrayList<ActiveProcess> diagramModel, String url, String statechartFileNameTail)
	{
		var String result = ""
		System.out.print("Generate GraphML data diagram...")
		result += '''«graphMLTextGenerator.headGraphMlGenerator(this)»
		'''
		procId = nodesId
		procList = diagramModel
		result +='''<graph edgedefault="directed" id="G">
	«graphMLTextGenerator.generateNodes(this/*, nodesId */, url, statechartFileNameTail)»
	«graphMLTextGenerator.generateAllEdges(diagramModel)»
  </graph>
    <data key="d6">
	  <y:Resources/>
	</data>
</graphml>'''
		System.out.println("done.")
		return result
	}
	
}