package su.nsk.iae.post.diagramgenerator;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.Arrays;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import su.nsk.iae.post.poST.AssignmentStatement;
import su.nsk.iae.post.poST.CaseElement;
import su.nsk.iae.post.poST.CaseListElement;
import su.nsk.iae.post.poST.CaseStatement;
import su.nsk.iae.post.poST.Constant;
import su.nsk.iae.post.poST.Expression;
import su.nsk.iae.post.poST.IfStatement;
import su.nsk.iae.post.poST.SetStateStatement;
import su.nsk.iae.post.poST.State;
import su.nsk.iae.post.poST.Statement;
import su.nsk.iae.post.poST.StatementList;
import su.nsk.iae.post.poST.SymbolicVariable;
import su.nsk.iae.post.poST.TimeoutStatement;

@SuppressWarnings("all")
public class StatechartDiagramGenerator extends ProcessDiagramGenerator {
  public void getStatechartNodes(final su.nsk.iae.post.poST.Process process) {
    DiagramNode newNode = new DiagramNode("   ", "circle");
    this.addElementToProcId(newNode);
    EList<State> _states = process.getStates();
    for (final State state : _states) {
      {
        String _name = state.getName();
        DiagramNode tmpNode = new DiagramNode(_name);
        this.addElementToProcId(tmpNode);
      }
    }
  }
  
  public void generateStatechartDiagramModel(final Resource resource, final su.nsk.iae.post.poST.Process process) {
    this.getStatechartNodes(process);
    this.generateStatechartModel(resource, process);
  }
  
  public CharSequence generateStatechartDiagram(final su.nsk.iae.post.poST.Process process) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _writeHeadGML = this.gmlTextGenerator.writeHeadGML(this);
    _builder.append(_writeHeadGML);
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    String _generateNodes = this.gmlTextGenerator.generateNodes(this);
    _builder.append(_generateNodes, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    String _generateAllEdges = this.gmlTextGenerator.generateAllEdges(this.procList);
    _builder.append(_generateAllEdges, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("]");
    return _builder;
  }
  
  public void generateStatechartModel(final Resource resource, final su.nsk.iae.post.poST.Process process) {
    boolean flagFirstStateInProcess = true;
    EList<State> _states = process.getStates();
    for (final State state : _states) {
      {
        EList<Statement> _statements = state.getStatement().getStatements();
        for (final Statement statement : _statements) {
          {
            if (flagFirstStateInProcess) {
              ActiveProcess startNode = new ActiveProcess();
              startNode.setIdFrom(this.getElementIndexProcId("   "));
              startNode.setIdTo(this.getElementIndexProcId(state.getName()));
              this.procList.add(startNode);
            }
            ArrayList<ActiveProcess> tempProcList = null;
            tempProcList = this.getStatechartList(statement, this.getElementIndexProcId(state.getName()), "(", "");
            this.procList.addAll(tempProcList);
            flagFirstStateInProcess = false;
          }
        }
        TimeoutStatement _timeout = state.getTimeout();
        boolean _tripleNotEquals = (_timeout != null);
        if (_tripleNotEquals) {
          EList<Statement> _statements_1 = state.getTimeout().getStatement().getStatements();
          for (final Statement timeoutFunctionStatements : _statements_1) {
            {
              EObject _xifexpression = null;
              Constant _const = state.getTimeout().getConst();
              boolean _tripleNotEquals_1 = (_const != null);
              if (_tripleNotEquals_1) {
                _xifexpression = state.getTimeout().getConst();
              } else {
                _xifexpression = state.getTimeout().getVariable();
              }
              String _trim = NodeModelUtils.getNode(_xifexpression).getText().trim();
              String _plus = ("(Timeout [ time = " + _trim);
              String contextLabel = (_plus + " ]");
              ArrayList<ActiveProcess> tempProcList = null;
              tempProcList = this.getStatechartList(timeoutFunctionStatements, this.getElementIndexProcId(state.getName()), contextLabel, "");
              this.procList.addAll(tempProcList);
            }
          }
        }
      }
    }
  }
  
  protected ArrayList<ActiveProcess> _getStatechartList(final Statement statement, final int contextStateId, final String contextLabel, final String expressionStatement) {
    return new ArrayList<ActiveProcess>();
  }
  
  protected ArrayList<ActiveProcess> _getStatechartList(final IfStatement statement, final int contextStateId, final String contextLabel, final String expressionStatement) {
    String newThenContextLabel = null;
    int index = contextLabel.lastIndexOf("]");
    if ((index != (-1))) {
      String _substring = contextLabel.substring(0, index);
      String _plus = (_substring + " AND (");
      String _translateExpr = this.translateExpr(statement.getMainCond());
      String _plus_1 = (_plus + _translateExpr);
      String _plus_2 = (_plus_1 + ") ]");
      newThenContextLabel = _plus_2;
    } else {
      String _translateExpr_1 = this.translateExpr(statement.getMainCond());
      String _plus_3 = ((contextLabel + "[ (") + _translateExpr_1);
      String _plus_4 = (_plus_3 + ") ]");
      newThenContextLabel = _plus_4;
    }
    String newExprLabel = "";
    newExprLabel = this.getContextLabel(statement.getMainStatement(), contextStateId, newThenContextLabel, newExprLabel);
    ArrayList<ActiveProcess> procTempList = new ArrayList<ActiveProcess>();
    ArrayList<ActiveProcess> procTempThenList = this.getStatechartList(statement.getMainStatement(), contextStateId, newThenContextLabel, newExprLabel);
    ArrayList<ActiveProcess> procTempElseList = new ArrayList<ActiveProcess>();
    StatementList _elseStatement = statement.getElseStatement();
    boolean _tripleNotEquals = (_elseStatement != null);
    if (_tripleNotEquals) {
      String newElseContextLabel = null;
      if ((index != (-1))) {
        String _substring_1 = contextLabel.substring(0, index);
        String _plus_5 = (_substring_1 + " AND NOT(");
        String _translateExpr_2 = this.translateExpr(statement.getMainCond());
        String _plus_6 = (_plus_5 + _translateExpr_2);
        String _plus_7 = (_plus_6 + ") ]");
        newElseContextLabel = _plus_7;
      } else {
        String _translateExpr_3 = this.translateExpr(statement.getMainCond());
        String _plus_8 = ((contextLabel + "[ NOT(") + _translateExpr_3);
        String _plus_9 = (_plus_8 + ") ]");
        newElseContextLabel = _plus_9;
      }
      newExprLabel = "";
      newExprLabel = this.getContextLabel(statement.getElseStatement(), contextStateId, newElseContextLabel, newExprLabel);
      procTempElseList = this.getStatechartList(statement.getElseStatement(), contextStateId, newElseContextLabel, newExprLabel);
    }
    procTempList.addAll(procTempThenList);
    procTempList.addAll(procTempElseList);
    return procTempList;
  }
  
  protected ArrayList<ActiveProcess> _getStatechartList(final CaseStatement statement, final int contextStateId, final String contextLabel, final String expressionStatement) {
    ArrayList<ActiveProcess> procTempList = new ArrayList<ActiveProcess>();
    EList<CaseElement> _caseElements = statement.getCaseElements();
    for (final CaseElement caseElement : _caseElements) {
      {
        String newThenContextLabel = null;
        int index = contextLabel.lastIndexOf("]");
        if ((index != (-1))) {
          String _substring = contextLabel.substring(0, index);
          String _plus = (_substring + "\n");
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("AND (");
          String _translateExpr = this.translateExpr(statement.getCond());
          _builder.append(_translateExpr);
          _builder.append(" = ");
          String _translateExpr_1 = this.translateExpr(caseElement.getCaseList().getCaseListElement().get(0));
          _builder.append(_translateExpr_1);
          _builder.append(") ]");
          String _plus_1 = (_plus + _builder);
          newThenContextLabel = _plus_1;
        } else {
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("[ (");
          String _translateExpr_2 = this.translateExpr(statement.getCond());
          _builder_1.append(_translateExpr_2);
          _builder_1.append(" = ");
          String _translateExpr_3 = this.translateExpr(caseElement.getCaseList().getCaseListElement().get(0));
          _builder_1.append(_translateExpr_3);
          _builder_1.append(") ]");
          String _plus_2 = (contextLabel + _builder_1);
          newThenContextLabel = _plus_2;
        }
        String newExprLabel = "";
        newExprLabel = this.getContextLabel(caseElement.getStatement(), contextStateId, newThenContextLabel, newExprLabel);
        ArrayList<ActiveProcess> procTempThenList = this.getStatechartList(caseElement.getStatement(), contextStateId, newThenContextLabel, newExprLabel);
        procTempList.addAll(procTempThenList);
      }
    }
    return procTempList;
  }
  
  protected ArrayList<ActiveProcess> _getStatechartList(final StatementList statementList, final int contextStateId, final String contextLabel, final String expressionStatement) {
    ArrayList<ActiveProcess> procTempList = new ArrayList<ActiveProcess>();
    String newExprLabel = expressionStatement;
    EList<Statement> _statements = statementList.getStatements();
    for (final Statement s : _statements) {
      newExprLabel = this.getContextLabel(s, contextStateId, contextLabel, newExprLabel);
    }
    EList<Statement> _statements_1 = statementList.getStatements();
    for (final Statement s_1 : _statements_1) {
      {
        ArrayList<ActiveProcess> subProcList = this.getStatechartList(s_1, contextStateId, contextLabel, newExprLabel);
        procTempList.addAll(subProcList);
      }
    }
    return procTempList;
  }
  
  protected String _getContextLabel(final StatementList statementList, final int contextStateId, final String contextLabel, final String expressionStatement) {
    String newExprContextLabel = "";
    int _length = expressionStatement.length();
    boolean _greaterThan = (_length > 0);
    if (_greaterThan) {
      newExprContextLabel = (expressionStatement + "; ");
      EList<Statement> _statements = statementList.getStatements();
      for (final Statement s : _statements) {
        {
          String str = this.getExpression(s);
          boolean _notEquals = (!Objects.equal(str, ""));
          if (_notEquals) {
            String _newExprContextLabel = newExprContextLabel;
            newExprContextLabel = (_newExprContextLabel + str);
          }
        }
      }
    } else {
      EList<Statement> _statements_1 = statementList.getStatements();
      for (final Statement s_1 : _statements_1) {
        {
          String str = this.getExpression(s_1);
          boolean _notEquals = (!Objects.equal(str, ""));
          if (_notEquals) {
            String _newExprContextLabel = newExprContextLabel;
            newExprContextLabel = (_newExprContextLabel + str);
          }
        }
      }
    }
    return newExprContextLabel;
  }
  
  protected String _getContextLabel(final Statement statement, final int contextStateId, final String contextLabel, final String expressionStatement) {
    return expressionStatement;
  }
  
  protected ArrayList<ActiveProcess> _getStatechartList(final SetStateStatement statement, final int contextStateId, final String contextLabel, final String expressionStatement) {
    String finishEdgeLabel = null;
    int _length = expressionStatement.length();
    boolean _greaterThan = (_length > 0);
    if (_greaterThan) {
      finishEdgeLabel = (((contextLabel + " / ") + expressionStatement) + ")");
    } else {
      finishEdgeLabel = (contextLabel + ")");
    }
    ActiveProcess tempElem = new ActiveProcess();
    ArrayList<ActiveProcess> procTempList = new ArrayList<ActiveProcess>();
    tempElem.setIdFrom(contextStateId);
    boolean _isNext = statement.isNext();
    if (_isNext) {
      tempElem.setIdTo((contextStateId + 1));
    } else {
      tempElem.setIdTo(this.getElementIndexProcId(statement.getState().getName()));
    }
    tempElem.setAction(finishEdgeLabel);
    procTempList.add(tempElem);
    return procTempList;
  }
  
  protected String _translateExpr(final Expression expr) {
    StringConcatenation _builder = new StringConcatenation();
    String _trim = NodeModelUtils.getNode(expr).getText().trim();
    _builder.append(_trim);
    return _builder.toString();
  }
  
  protected String _translateExpr(final CaseListElement el) {
    SymbolicVariable _variable = el.getVariable();
    boolean _tripleNotEquals = (_variable != null);
    if (_tripleNotEquals) {
      return el.getVariable().getName();
    } else {
      return NodeModelUtils.getNode(el.getNum()).getText().trim();
    }
  }
  
  protected String _getExpression(final Statement s) {
    return "";
  }
  
  protected String _getExpression(final AssignmentStatement s) {
    StringConcatenation _builder = new StringConcatenation();
    String _trim = NodeModelUtils.getNode(s).getText().trim();
    _builder.append(_trim);
    return _builder.toString();
  }
  
  public ArrayList<ActiveProcess> getStatechartList(final EObject statement, final int contextStateId, final String contextLabel, final String expressionStatement) {
    if (statement instanceof CaseStatement) {
      return _getStatechartList((CaseStatement)statement, contextStateId, contextLabel, expressionStatement);
    } else if (statement instanceof IfStatement) {
      return _getStatechartList((IfStatement)statement, contextStateId, contextLabel, expressionStatement);
    } else if (statement instanceof SetStateStatement) {
      return _getStatechartList((SetStateStatement)statement, contextStateId, contextLabel, expressionStatement);
    } else if (statement instanceof Statement) {
      return _getStatechartList((Statement)statement, contextStateId, contextLabel, expressionStatement);
    } else if (statement instanceof StatementList) {
      return _getStatechartList((StatementList)statement, contextStateId, contextLabel, expressionStatement);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(statement, contextStateId, contextLabel, expressionStatement).toString());
    }
  }
  
  public String getContextLabel(final EObject statement, final int contextStateId, final String contextLabel, final String expressionStatement) {
    if (statement instanceof Statement) {
      return _getContextLabel((Statement)statement, contextStateId, contextLabel, expressionStatement);
    } else if (statement instanceof StatementList) {
      return _getContextLabel((StatementList)statement, contextStateId, contextLabel, expressionStatement);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(statement, contextStateId, contextLabel, expressionStatement).toString());
    }
  }
  
  public String translateExpr(final EObject el) {
    if (el instanceof CaseListElement) {
      return _translateExpr((CaseListElement)el);
    } else if (el instanceof Expression) {
      return _translateExpr((Expression)el);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(el).toString());
    }
  }
  
  public String getExpression(final Statement s) {
    if (s instanceof AssignmentStatement) {
      return _getExpression((AssignmentStatement)s);
    } else if (s != null) {
      return _getExpression(s);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(s).toString());
    }
  }
}
