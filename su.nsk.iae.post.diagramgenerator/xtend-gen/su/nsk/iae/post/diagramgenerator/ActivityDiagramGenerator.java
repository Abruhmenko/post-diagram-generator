package su.nsk.iae.post.diagramgenerator;

import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import su.nsk.iae.post.poST.CaseElement;
import su.nsk.iae.post.poST.CaseStatement;
import su.nsk.iae.post.poST.IfStatement;
import su.nsk.iae.post.poST.StartProcessStatement;
import su.nsk.iae.post.poST.State;
import su.nsk.iae.post.poST.Statement;
import su.nsk.iae.post.poST.StatementList;
import su.nsk.iae.post.poST.StopProcessStatement;
import su.nsk.iae.post.poST.TimeoutStatement;
import su.nsk.iae.post.poST.Variable;

@SuppressWarnings("all")
public class ActivityDiagramGenerator extends ProcessDiagramGraphMLGenerator {
  public void generateActivityModel(final Resource resource) {
    Iterable<su.nsk.iae.post.poST.Process> _filter = Iterables.<su.nsk.iae.post.poST.Process>filter(IteratorExtensions.<EObject>toIterable(resource.getAllContents()), su.nsk.iae.post.poST.Process.class);
    for (final su.nsk.iae.post.poST.Process process : _filter) {
      EList<State> _states = process.getStates();
      for (final State state : _states) {
        {
          EList<Statement> _statements = state.getStatement().getStatements();
          for (final Statement statement : _statements) {
            {
              ArrayList<ActiveProcess> tempProcList = null;
              tempProcList = this.getActiveList(statement, this.getElementIndexProcId(process.getName()));
              this.procList.addAll(tempProcList);
            }
          }
          TimeoutStatement _timeout = state.getTimeout();
          boolean _tripleNotEquals = (_timeout != null);
          if (_tripleNotEquals) {
            EList<Statement> _statements_1 = state.getTimeout().getStatement().getStatements();
            for (final Statement timeoutFunctionStatements : _statements_1) {
              {
                ArrayList<ActiveProcess> tempProcList = null;
                tempProcList = this.getActiveList(timeoutFunctionStatements, this.getElementIndexProcId(process.getName()));
                this.procList.addAll(tempProcList);
              }
            }
          }
        }
      }
    }
  }
  
  protected ArrayList<ActiveProcess> _getActiveList(final StartProcessStatement statement, final int contextProcessId) {
    ArrayList<ActiveProcess> procTempList = new ArrayList<ActiveProcess>();
    ActiveProcess proc = new ActiveProcess();
    proc.setAction("start");
    proc.setIdFrom(contextProcessId);
    Variable _process = statement.getProcess();
    boolean _tripleNotEquals = (_process != null);
    if (_tripleNotEquals) {
      proc.setIdTo(this.getElementIndexProcId(statement.getProcess().getName()));
    } else {
      proc.setIdTo(contextProcessId);
    }
    procTempList.add(proc);
    return procTempList;
  }
  
  protected ArrayList<ActiveProcess> _getActiveList(final StopProcessStatement statement, final int contextProcessId) {
    ArrayList<ActiveProcess> procTempList = new ArrayList<ActiveProcess>();
    ActiveProcess proc = new ActiveProcess();
    proc.setAction("stop");
    proc.setIdFrom(contextProcessId);
    Variable _process = statement.getProcess();
    boolean _tripleNotEquals = (_process != null);
    if (_tripleNotEquals) {
      proc.setIdTo(this.getElementIndexProcId(statement.getProcess().getName()));
    } else {
      proc.setIdTo(contextProcessId);
    }
    procTempList.add(proc);
    return procTempList;
  }
  
  protected ArrayList<ActiveProcess> _getActiveList(final IfStatement statement, final int contextProcessId) {
    ArrayList<ActiveProcess> procTempList = new ArrayList<ActiveProcess>();
    ArrayList<ActiveProcess> procTempThenList = this.getActiveList(statement.getMainStatement(), contextProcessId);
    ArrayList<ActiveProcess> procTempElseList = new ArrayList<ActiveProcess>();
    StatementList _elseStatement = statement.getElseStatement();
    boolean _tripleNotEquals = (_elseStatement != null);
    if (_tripleNotEquals) {
      procTempElseList = this.getActiveList(statement.getElseStatement(), contextProcessId);
    }
    procTempList.addAll(procTempThenList);
    procTempList.addAll(procTempElseList);
    return procTempList;
  }
  
  protected ArrayList<ActiveProcess> _getActiveList(final CaseStatement statement, final int contextProcessId) {
    ArrayList<ActiveProcess> procTempList = new ArrayList<ActiveProcess>();
    EList<CaseElement> _caseElements = statement.getCaseElements();
    for (final CaseElement caseElement : _caseElements) {
      {
        ArrayList<ActiveProcess> procTempCaseElementList = this.getActiveList(caseElement.getStatement(), contextProcessId);
        procTempList.addAll(procTempCaseElementList);
      }
    }
    ArrayList<ActiveProcess> procTempElseList = new ArrayList<ActiveProcess>();
    StatementList _elseStatement = statement.getElseStatement();
    boolean _tripleNotEquals = (_elseStatement != null);
    if (_tripleNotEquals) {
      procTempElseList = this.getActiveList(statement.getElseStatement(), contextProcessId);
    }
    procTempList.addAll(procTempElseList);
    return procTempList;
  }
  
  protected ArrayList<ActiveProcess> _getActiveList(final StatementList statementList, final int contextProcessId) {
    ArrayList<ActiveProcess> procTempList = new ArrayList<ActiveProcess>();
    EList<Statement> _statements = statementList.getStatements();
    for (final Statement s : _statements) {
      {
        ArrayList<ActiveProcess> subProcList = this.getActiveList(s, contextProcessId);
        procTempList.addAll(subProcList);
      }
    }
    return procTempList;
  }
  
  protected ArrayList<ActiveProcess> _getActiveList(final Statement statement, final int contextProcessId) {
    return new ArrayList<ActiveProcess>();
  }
  
  /**
   * def dispatch ArrayList<ActiveProcess> getActiveList(Expression expression, int contextProcessId)
   * {
   * return new ArrayList<ActiveProcess>;
   * }
   */
  public void generateActivityDiagramModel(final Resource resource) {
    this.generateProcList(resource);
    this.generateActivityModel(resource);
  }
  
  public String generateActivityDiagram(final HashMap<String, DiagramNode> nodesId, final ArrayList<ActiveProcess> diagramModel) {
    String result = "";
    System.out.print("Generate GML activity diagram...");
    String _result = result;
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _writeHeadGML = this.gmlTextGenerator.writeHeadGML(this);
    _builder.append(_writeHeadGML);
    _builder.newLineIfNotEmpty();
    result = (_result + _builder);
    this.procId = nodesId;
    this.procList = diagramModel;
    String _result_1 = result;
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("\t");
    String _generateNodes = this.gmlTextGenerator.generateNodes(this);
    _builder_1.append(_generateNodes, "\t");
    _builder_1.newLineIfNotEmpty();
    _builder_1.append("\t\t\t");
    String _generateAllEdges = this.gmlTextGenerator.generateAllEdges(diagramModel);
    _builder_1.append(_generateAllEdges, "\t\t\t");
    _builder_1.newLineIfNotEmpty();
    _builder_1.append("\t\t");
    _builder_1.append("]");
    result = (_result_1 + _builder_1);
    System.out.println("done.");
    return result;
  }
  
  public String generateActivityGraphMLDiagram(final HashMap<String, DiagramNode> nodesId, final ArrayList<ActiveProcess> diagramModel, final String url, final String statechartFileNameTail) {
    String result = "";
    System.out.print("Generate GraphML activity diagram...");
    String _result = result;
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _headGraphMlGenerator = this.graphMLTextGenerator.headGraphMlGenerator(this);
    _builder.append(_headGraphMlGenerator);
    _builder.newLineIfNotEmpty();
    result = (_result + _builder);
    this.procId = nodesId;
    this.procList = diagramModel;
    String _result_1 = result;
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("<graph edgedefault=\"directed\" id=\"G\">");
    _builder_1.newLine();
    _builder_1.append("\t");
    String _generateNodes = this.graphMLTextGenerator.generateNodes(this, url, statechartFileNameTail);
    _builder_1.append(_generateNodes, "\t");
    _builder_1.newLineIfNotEmpty();
    _builder_1.append("\t");
    String _generateAllEdges = this.graphMLTextGenerator.generateAllEdges(diagramModel);
    _builder_1.append(_generateAllEdges, "\t");
    _builder_1.newLineIfNotEmpty();
    _builder_1.append("  ");
    _builder_1.append("</graph>");
    _builder_1.newLine();
    _builder_1.append("    ");
    _builder_1.append("<data key=\"d6\">");
    _builder_1.newLine();
    _builder_1.append("\t  ");
    _builder_1.append("<y:Resources/>");
    _builder_1.newLine();
    _builder_1.append("\t");
    _builder_1.append("</data>");
    _builder_1.newLine();
    _builder_1.append("</graphml>");
    result = (_result_1 + _builder_1);
    System.out.println("done.");
    return result;
  }
  
  public ArrayList<ActiveProcess> getActiveList(final EObject statement, final int contextProcessId) {
    if (statement instanceof CaseStatement) {
      return _getActiveList((CaseStatement)statement, contextProcessId);
    } else if (statement instanceof IfStatement) {
      return _getActiveList((IfStatement)statement, contextProcessId);
    } else if (statement instanceof StartProcessStatement) {
      return _getActiveList((StartProcessStatement)statement, contextProcessId);
    } else if (statement instanceof StopProcessStatement) {
      return _getActiveList((StopProcessStatement)statement, contextProcessId);
    } else if (statement instanceof Statement) {
      return _getActiveList((Statement)statement, contextProcessId);
    } else if (statement instanceof StatementList) {
      return _getActiveList((StatementList)statement, contextProcessId);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(statement, contextProcessId).toString());
    }
  }
}
