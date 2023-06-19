package su.nsk.iae.post.diagramgenerator

import java.util.ArrayList
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.nodemodel.util.NodeModelUtils
import su.nsk.iae.post.poST.Process
import su.nsk.iae.post.poST.Statement
import su.nsk.iae.post.poST.IfStatement
import su.nsk.iae.post.poST.Expression
import su.nsk.iae.post.poST.AssignmentStatement
import su.nsk.iae.post.poST.StatementList
import su.nsk.iae.post.poST.SetStateStatement
import su.nsk.iae.post.poST.CaseStatement
import su.nsk.iae.post.poST.CaseListElement

class StatechartDiagramGenerator extends ProcessDiagramGenerator {

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//Input: process, to which we will generate statechart diagram
//Output: string with statechart diagram node's for that process
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def /*String*/ getStatechartNodes(Process process)
	{
		//var String tempString = "";
		var DiagramNode newNode = new DiagramNode("   ", "circle")
		addElementToProcId(newNode)
		
		//tempString += gmlTextGenerator.generateOneProcessNode(count_id, "     ", newNode.getShape)
		//procId.add(count_id, "start_default") // запоминаем соответствие имени назначенному ему Id
	    //count_id ++
		for (state : process.states)//получаем список всех состояний процесса, и проходим по нему
		{ 
			var DiagramNode tmpNode = new DiagramNode(state.name)
	    	
	    	//tempString += gmlTextGenerator.generateOneProcessNode(count_id, state.name, "roundrectangle") 
	    	addElementToProcId(tmpNode)
	    	//procId.add(count_id, tmpNode) // запоминаем соответствие имени назначенному ему Id
	        //count_id ++
	    }
	    /*System.out.println("procId:")
	    for(e: procId.values())
	    {
	    	System.out.print(e.name + ":" + e.shape +", ")
	    }
	    System.out.println(" ");*/
	  //  return tempString
	}


//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
def generateStatechartDiagramModel(Resource resource, Process process)
{
	 getStatechartNodes(process)
	 generateStatechartModel(resource, process)
}

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Возвращает готовый текст statechart-диаграммы
// Input: process, to which we will generate statechart diagram
// Output: finished text of GML process statechart diagram
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def generateStatechartDiagram(Process process)
	'''«gmlTextGenerator.writeHeadGML(this)»
	«gmlTextGenerator.generateNodes(this/* , procId*/)»
	«gmlTextGenerator.generateAllEdges(procList)»
]'''	
	
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Build statechard diagram model as an ArrayList<ActiveProcess> (procList), where we save the relationships behind states of process
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
def generateStatechartModel(Resource resource, Process process)
	{
		var boolean flagFirstStateInProcess = true;
		for (state : process.states) 
	    {
	    	//var DiagramNode tmpNode = new DiagramNode(state.name)

	         for(statement: state.statement.statements) // go throw statements
	         {
	         	if(flagFirstStateInProcess) // we need to put start node to diagram (such circle with edge to first state on process)
	         	{
	         		//var DiagramNode stNode = new DiagramNode("   ", "circle")	
	         		var ActiveProcess startNode = new ActiveProcess()
	    			startNode.idFrom = getElementIndexProcId("   ")
	    			startNode.idTo = getElementIndexProcId(state.name)
	    			procList.add(startNode)
	         	}
	         	var ArrayList<ActiveProcess> tempProcList;
	         	tempProcList = statement.getStatechartList(getElementIndexProcId(state.name), "(", "")
	         	procList.addAll(tempProcList)
	         	flagFirstStateInProcess = false;
	         }
	       
	         if(state.timeout !== null) // go throw timeoutFunction, if it is exist for that state
	         {
	         	for(timeoutFunctionStatements: state.timeout.statement.statements)
	        	{
	        		var String contextLabel = "(Timeout [ time = " + NodeModelUtils.getNode(state.timeout.const !== null ? state.timeout.const : state.timeout.variable).text.trim + " ]"
	        		var ArrayList<ActiveProcess> tempProcList;
	        		tempProcList = timeoutFunctionStatements.getStatechartList(getElementIndexProcId(state.name), contextLabel, "")
	        		procList.addAll(tempProcList)
	        	}
	         }
	    }
	}

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Полиморфный метод для суперкласса Statement (заглушка)
//
// Polymorphic method for Statement (to avoid exception)
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
def dispatch ArrayList<ActiveProcess> getStatechartList(Statement statement, int contextStateId, String contextLabel, String expressionStatement)
	{
		return new ArrayList<ActiveProcess>;
	}


//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//Polymorphic method for IfElseStat, input: contextStateId which is current state id in the list procId
//Method calls getStatechartList for 'else' and 'then' fields of statement and collecting their returns to return it
//Output: ArrayList<ActiveProcess> which have information about relationships between states (like set next and etc)
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def dispatch ArrayList<ActiveProcess> getStatechartList(IfStatement statement, int contextStateId, String contextLabel, String expressionStatement)
	{
		var String newThenContextLabel
		var index = contextLabel.lastIndexOf("]")
		if( index != -1)
		{
			newThenContextLabel = contextLabel.substring(0, index) + " AND (" + statement.mainCond.translateExpr() + ") ]"//collecting conditions in contextLabel
		}
		else
		{
			newThenContextLabel = contextLabel + "[ (" + statement.mainCond.translateExpr() + ") ]"
		}
		var String newExprLabel = "";
		newExprLabel = statement.mainStatement.getContextLabel(contextStateId, newThenContextLabel, newExprLabel) //collecting expressions context for edge label (which after /)
		
		
		var ArrayList<ActiveProcess> procTempList = new ArrayList<ActiveProcess>
		var ArrayList<ActiveProcess> procTempThenList = statement.mainStatement.getStatechartList(contextStateId, newThenContextLabel, newExprLabel)
		
		var ArrayList<ActiveProcess> procTempElseList = new ArrayList<ActiveProcess>
		if(statement.elseStatement !== null) // 'if' may be without 'else'
		{
			var String newElseContextLabel
			if( index != -1)
			{
				newElseContextLabel = contextLabel.substring(0, index) + " AND NOT(" + statement.mainCond.translateExpr()  + ") ]"
			}
			else
			{
				newElseContextLabel = contextLabel + "[ NOT(" + statement.mainCond.translateExpr()  + ") ]"
			}
			newExprLabel =  ""; //needed only local expressions in that scope
			newExprLabel = statement.elseStatement.getContextLabel(contextStateId, newElseContextLabel, newExprLabel) //collecting expressions context for edge label (which after /)
			procTempElseList = statement.elseStatement.getStatechartList(contextStateId, newElseContextLabel, newExprLabel)
		}
		procTempList.addAll(procTempThenList)
		procTempList.addAll(procTempElseList)
		return (procTempList)
	}
	
	def dispatch ArrayList<ActiveProcess> getStatechartList(CaseStatement statement, int contextStateId, String contextLabel, String expressionStatement) {
		var ArrayList<ActiveProcess> procTempList = new ArrayList<ActiveProcess>
		
		for (caseElement : statement.caseElements) {
			var String newThenContextLabel
			var index = contextLabel.lastIndexOf("]")
			if (index != -1) {
				newThenContextLabel = contextLabel.substring(0, index) + "\n" + '''AND («statement.cond.translateExpr» = «caseElement.caseList.caseListElement.get(0).translateExpr») ]'''//collecting conditions in contextLabel
			}
			else {
				newThenContextLabel = contextLabel + '''[ («statement.cond.translateExpr» = «caseElement.caseList.caseListElement.get(0).translateExpr») ]'''
			}
			var String newExprLabel = "";
			newExprLabel = caseElement.statement.getContextLabel(contextStateId, newThenContextLabel, newExprLabel) //collecting expressions context for edge label (which after /)
			
			var ArrayList<ActiveProcess> procTempThenList = caseElement.statement.getStatechartList(contextStateId, newThenContextLabel, newExprLabel)
			
			procTempList.addAll(procTempThenList)
		}
		
		return procTempList
	}


//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//Polymorphic method for CompoundStatement, input: contextStateId which is current state id in the list procId
//Method calls getStatechartList for their statements and collecting their returns to return it
//Output: ArrayList<ActiveProcess> which have information about relationships between states (like set next and etc)
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def dispatch ArrayList<ActiveProcess> getStatechartList(StatementList statementList, int contextStateId, String contextLabel, String expressionStatement)
	{
		var ArrayList<ActiveProcess> procTempList = new ArrayList<ActiveProcess>;
		var String newExprLabel = expressionStatement;
		for(s : statementList.statements)
		{
			newExprLabel = s.getContextLabel(contextStateId, contextLabel, newExprLabel) //collecting expressions context for edge label (which is after /)
		}
		for(s : statementList.statements)
		{
			var ArrayList<ActiveProcess> subProcList = s.getStatechartList(contextStateId, contextLabel, newExprLabel);
			procTempList.addAll(subProcList)
		}
		return procTempList
	}

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//Polymorphic method for ExpressionStatement to collect expressions like (a = b) which have to be after '/' in edge label
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def dispatch String getContextLabel(StatementList statementList, int contextStateId, String contextLabel, String expressionStatement)
	{
		var String newExprContextLabel = "";
		if(expressionStatement.length > 0)//not first expression
		{
			newExprContextLabel = expressionStatement + "; " // + statementList.getExpression //collecting expressions like 'a; b; c' (will be put in edge labels after '/')
			for (s : statementList.statements) {
				var String str = s.getExpression
				if (str != "") {
					newExprContextLabel += str
				}
			}
		}
		else
		{
			// newExprContextLabel = expressionStatement + statementList.getExpression //collecting expressions like 'a; b; c' (will be put in edge labels after '/')
			for (s : statementList.statements) {
				var String str = s.getExpression
				if (str != "") {
					newExprContextLabel += str
				}
			}
		}
		return newExprContextLabel
	}

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//Polymorphic method for all other Statements - they not needed
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def dispatch String getContextLabel(Statement statement, int contextStateId, String contextLabel, String expressionStatement)
	{
		return expressionStatement
	}


//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//Polymorphic method for SetStateStat, input: contextStateId which is current state id in the list procId
//Method returns one element of ArrayList<ActiveProcess>, which have information about which state set which state (we have their id in the procId list)
//Output: ArrayList<ActiveProcess> which have information about relationships between states (like set next and etc)
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def dispatch ArrayList<ActiveProcess> getStatechartList(SetStateStatement statement, int contextStateId, String contextLabel, String expressionStatement)
	{
		var String finishEdgeLabel
		if(expressionStatement.length() > 0)
			finishEdgeLabel = contextLabel + " / " + expressionStatement + ")"
		else
			finishEdgeLabel = contextLabel + ")"
		
		var ActiveProcess tempElem = new ActiveProcess()
		var ArrayList<ActiveProcess> procTempList = new ArrayList<ActiveProcess>;
		tempElem.idFrom = contextStateId
		if(statement.next) // we gets id to states in the order we found states in process notification, so, next state will have number id which is currentId + 1
			tempElem.idTo = contextStateId + 1
		else
		{
			//var DiagramNode tmpNode = new DiagramNode(statement.stateId)
			tempElem.idTo = (getElementIndexProcId(statement.state.name))
		}
		tempElem.action = finishEdgeLabel
		procTempList.add(tempElem)
		return procTempList
	}

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Method returns expression text (used in edge labels)
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def dispatch String translateExpr(Expression expr) 
	{
		return '''« NodeModelUtils.getNode(expr).text.trim »'''
	}
	def dispatch String translateExpr(CaseListElement el) 
	{
		if (el.variable !== null) {
			return el.variable.name
		}
		else {
			return NodeModelUtils.getNode(el.num).text.trim
		}
	}
	
	def dispatch String getExpression(Statement s)
	{
		return ""
	}
	
	def dispatch String getExpression(AssignmentStatement s)
	{
		return '''« NodeModelUtils.getNode(s).text.trim »'''
	}
	
}