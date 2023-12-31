package su.nsk.iae.post.diagramgenerator

import java.util.ArrayList
import org.eclipse.emf.ecore.resource.Resource
import java.util.HashMap
import su.nsk.iae.post.poST.Process
import su.nsk.iae.post.poST.Statement
import su.nsk.iae.post.poST.StartProcessStatement
import su.nsk.iae.post.poST.StopProcessStatement
import su.nsk.iae.post.poST.IfStatement
import su.nsk.iae.post.poST.StatementList
import su.nsk.iae.post.poST.CaseStatement

class ActivityDiagramGenerator extends ProcessDiagramGraphMLGenerator {
	
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// ����� ������� ������ ����� ��������� �� ���������� � ���� ������ ArrayList<ActiveProcess>
//
// Method construct the model of manage connecting behind processes like an ArrayList<ActiveProcess> 
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def generateActivityModel(Resource resource)
	{
		for (process : resource.allContents.toIterable.filter(Process)) 
		{
			//var DiagramNode tmpNode = new DiagramNode(process.name) 
	         for (state : process.states) 
	         {
	         	for(statement: state.statement.statements) // go throw statements
	         	{
	         		var ArrayList<ActiveProcess> tempProcList;
	         		tempProcList = statement.getActiveList(getElementIndexProcId(process.name))
	         		procList.addAll(tempProcList)
	         	}
	       
	         	if(state.timeout !== null) // go throw timeoutFunction, if it is exist for that state
	         	{
	         		for(timeoutFunctionStatements: state.timeout.statement.statements)
	         		{
	         			var ArrayList<ActiveProcess> tempProcList;
	         			tempProcList = timeoutFunctionStatements.getActiveList(getElementIndexProcId(process.name))
	         			procList.addAll(tempProcList)
	         		}
	         	}  
	         }    	 
	     }
	}
	
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// ����������� ����� ��� StartProcStat: ���������� 1 ������� ArrayList<ActiveProcess>, �����, ���������� ����� ����� �������� ����� � ���������, ������ �������� ���
// ���������
//
// Polymorphic method. For StartProcStat returns 1 element of ArrayList<ActiveProcess>. It express how connecting the process and statement "start" in them
// Input: contextProcessId - id of process, in which is that statement
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def dispatch ArrayList<ActiveProcess> getActiveList(StartProcessStatement statement, int contextProcessId)
	{
		var ArrayList<ActiveProcess> procTempList = new ArrayList<ActiveProcess>
		var ActiveProcess proc = new ActiveProcess()
		//var DiagramNode tmpNode = new DiagramNode(statement.procId) 
	    proc.setAction("start");
	    proc.setIdFrom(contextProcessId)
	    if (statement.process !== null)
	    {
	    	proc.setIdTo(getElementIndexProcId(statement.process.name))
	    }
	    else
	    {
	    	proc.setIdTo(contextProcessId)
	    }
	    procTempList.add(proc)
	    
		return procTempList
	}

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// ����������� ����� ��� StopProcStat: ���������� 1 ������� ArrayList<ActiveProcess>, �����, ���������� ����� ����� �������� ���� � ���������, ������ �������� ���
// ���������
//
// Polymorphic method. For StopProcStat returns 1 element of ArrayList<ActiveProcess>. It express how connecting the process and statement "stop" in them
// Input: contextProcessId - id of process, in which is that statement
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def dispatch ArrayList<ActiveProcess> getActiveList(StopProcessStatement statement, int contextProcessId)
	{
		var ArrayList<ActiveProcess> procTempList = new ArrayList<ActiveProcess>
		var ActiveProcess proc = new ActiveProcess()
		//var DiagramNode tmpNode = new DiagramNode(statement.procId) 
	    proc.setAction("stop");
	    proc.setIdFrom(contextProcessId)
	    if (statement.process !== null)
	    {
	    	proc.setIdTo(getElementIndexProcId(statement.process.name))
	    }
	    else
	    {
	    	proc.setIdTo(contextProcessId)
	    }
	    procTempList.add(proc)
	    
		return procTempList
	}


//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// ����������� ����� ��� IfElseStat: ���������� ArrayList<ActiveProcess>, �� ������ �������, ���������� ����� ����� ��������� �����/���� � ���������, ������ �������� ��� ���������
// ����� ��������������� �������� ������� getActiveList � ����� IfElseStat, ����� ���� �������� ������ ���������, � ���������� ���
//
// Polymorphic method. For IfElseStat returns ArrayList<ActiveProcess>. It express how connecting the process and statement "stop"/"start" in them
// Method calls getActiveList from IfElseStat statement's fields, then collect results and return them
// Input: contextProcessId - id of process, in which is that statement
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def dispatch ArrayList<ActiveProcess> getActiveList(IfStatement statement, int contextProcessId)
	{
		var ArrayList<ActiveProcess> procTempList = new ArrayList<ActiveProcess>
		var ArrayList<ActiveProcess> procTempThenList = statement.mainStatement.getActiveList(contextProcessId)
		var ArrayList<ActiveProcess> procTempElseList = new ArrayList<ActiveProcess>
		
		if(statement.elseStatement !== null)
			procTempElseList = statement.elseStatement.getActiveList(contextProcessId)
		procTempList.addAll(procTempThenList)
		procTempList.addAll(procTempElseList)
		return (procTempList)
	}
	
	
	def dispatch ArrayList<ActiveProcess> getActiveList(CaseStatement statement, int contextProcessId)
	{
		var ArrayList<ActiveProcess> procTempList = new ArrayList<ActiveProcess>
		for (caseElement : statement.caseElements) {
			var ArrayList<ActiveProcess> procTempCaseElementList = caseElement.statement.getActiveList(contextProcessId)
			procTempList.addAll(procTempCaseElementList)
		}
		
		var ArrayList<ActiveProcess> procTempElseList = new ArrayList<ActiveProcess>
		if(statement.elseStatement !== null)
			procTempElseList = statement.elseStatement.getActiveList(contextProcessId)
		//procTempList.addAll(procTempThenList)
		procTempList.addAll(procTempElseList)
		return (procTempList)
	}

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// ����������� ����� getActiveList ��� ��������� �������� (CompoundStatement) 
// ���������� ����� ������ �������� ActiveProcess �� ���� �������� ������ ��������� ��������
//
// Polymorphic method. For CompoundStatement returns ArrayList<ActiveProcess>. It express how connecting the process and statement "stop"/"start" in them
// Method calls getActiveList from statement's fields, then collect results and return them
// Input: contextProcessId - id of process, in which is that statement
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def dispatch ArrayList<ActiveProcess> getActiveList(StatementList statementList, int contextProcessId)
	{
		var ArrayList<ActiveProcess> procTempList = new ArrayList<ActiveProcess>;
		for(s : statementList.statements)
		{
			var ArrayList<ActiveProcess> subProcList = s.getActiveList(contextProcessId);
			procTempList.addAll(subProcList)
		}
		return procTempList
	}


//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// ����������� ����� ��� ����������� Statement (��������)
//
// Polymorphic method for Statement (to avoid exception)
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
def dispatch ArrayList<ActiveProcess> getActiveList(Statement statement, int contextProcessId)
	{
		return new ArrayList<ActiveProcess>;
	}


/*def dispatch ArrayList<ActiveProcess> getActiveList(Expression expression, int contextProcessId)
	{
		return new ArrayList<ActiveProcess>;
	}
	*/
	
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
def generateActivityDiagramModel(Resource resource)
{
	generateProcList(resource)
	generateActivityModel(resource)
}

	
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// ����� ��������� ������ ��������� gml �����, ������ ������ ��������� � ������ �����. ��� ����� ���������� ��������������� ������. ���������� ������� ����� activity-���������
//
// Method is collecting a GML head, nodes list and edge list of diagram by calling the same methods 
// Output: finished text of GML activity diagram
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def String generateActivityDiagram(HashMap<String, DiagramNode> nodesId, ArrayList<ActiveProcess> diagramModel)
	{
		var String result = ""
		System.out.print("Generate GML activity diagram...")
		result += '''�gmlTextGenerator.writeHeadGML(this)�
		'''
		procId = nodesId
		procList = diagramModel
		result += '''	�gmlTextGenerator.generateNodes(this/*, nodesId */)�
			�gmlTextGenerator.generateAllEdges(diagramModel)�
		]'''
		System.out.println("done.")
		return result
	}


//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// ����� ��������� ������ ��������� GraphML �����, ������ ������ ��������� � ������ �����. ��� ����� ���������� ��������������� ������. ���������� ������� ����� activity-��������� 
//
// Method is collecting a GML head, nodes list and edge list of diagram by calling the same methods 
// Output: finished text of GraphML activity diagram
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	def String generateActivityGraphMLDiagram(HashMap<String, DiagramNode> nodesId, ArrayList<ActiveProcess> diagramModel, String url, String statechartFileNameTail)
	{
		var String result = ""
		System.out.print("Generate GraphML activity diagram...")
		result += '''�graphMLTextGenerator.headGraphMlGenerator(this)�
		'''
		procId = nodesId
		procList = diagramModel
		result += '''<graph edgedefault="directed" id="G">
	�graphMLTextGenerator.generateNodes(this/*, nodesId */, url, statechartFileNameTail)�
	�graphMLTextGenerator.generateAllEdges(diagramModel)�
  </graph>
    <data key="d6">
	  <y:Resources/>
	</data>
</graphml>'''
		System.out.println("done.")
		return result
	}
	
}