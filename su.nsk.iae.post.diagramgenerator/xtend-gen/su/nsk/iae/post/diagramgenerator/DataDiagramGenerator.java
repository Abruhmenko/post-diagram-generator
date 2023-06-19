package su.nsk.iae.post.diagramgenerator;

import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import su.nsk.iae.post.poST.AddExpression;
import su.nsk.iae.post.poST.AndExpression;
import su.nsk.iae.post.poST.ArrayVariable;
import su.nsk.iae.post.poST.AssignmentStatement;
import su.nsk.iae.post.poST.CaseElement;
import su.nsk.iae.post.poST.CaseStatement;
import su.nsk.iae.post.poST.CompExpression;
import su.nsk.iae.post.poST.EquExpression;
import su.nsk.iae.post.poST.Expression;
import su.nsk.iae.post.poST.ForStatement;
import su.nsk.iae.post.poST.IfStatement;
import su.nsk.iae.post.poST.InputVarDeclaration;
import su.nsk.iae.post.poST.MulExpression;
import su.nsk.iae.post.poST.OutputVarDeclaration;
import su.nsk.iae.post.poST.ParamAssignment;
import su.nsk.iae.post.poST.PowerExpression;
import su.nsk.iae.post.poST.PrimaryExpression;
import su.nsk.iae.post.poST.RepeatStatement;
import su.nsk.iae.post.poST.State;
import su.nsk.iae.post.poST.Statement;
import su.nsk.iae.post.poST.StatementList;
import su.nsk.iae.post.poST.SymbolicVariable;
import su.nsk.iae.post.poST.TimeoutStatement;
import su.nsk.iae.post.poST.UnaryExpression;
import su.nsk.iae.post.poST.VarDeclaration;
import su.nsk.iae.post.poST.VarInitDeclaration;
import su.nsk.iae.post.poST.WhileStatement;
import su.nsk.iae.post.poST.XorExpression;

@SuppressWarnings("all")
public class DataDiagramGenerator extends ProcessDiagramGraphMLGenerator {
  private HashMap<String, Integer> variableId = new HashMap<String, Integer>();
  
  public int getVariablesNodes(final Resource resource) {
    int _xblockexpression = (int) 0;
    {
      this.count_id = this.procId.size();
      Iterable<VarDeclaration> _filter = Iterables.<VarDeclaration>filter(IteratorExtensions.<EObject>toIterable(resource.getAllContents()), VarDeclaration.class);
      for (final VarDeclaration varBlock : _filter) {
        EList<VarInitDeclaration> _vars = varBlock.getVars();
        for (final VarInitDeclaration variablesList : _vars) {
          {
            String varType = variablesList.getSpec().getType();
            EList<SymbolicVariable> _vars_1 = variablesList.getVarList().getVars();
            for (final SymbolicVariable variable : _vars_1) {
              {
                String _name = variable.getName();
                String _plus = ("VAR\n" + _name);
                String _plus_1 = (_plus + " : ");
                String _plus_2 = (_plus_1 + varType);
                DiagramNode newNode = new DiagramNode(_plus_2, "ellipse");
                this.addElementToProcId(newNode);
                this.variableId.put(variable.getName(), Integer.valueOf((this.count_id - 1)));
              }
            }
          }
        }
      }
      Iterable<InputVarDeclaration> _filter_1 = Iterables.<InputVarDeclaration>filter(IteratorExtensions.<EObject>toIterable(resource.getAllContents()), InputVarDeclaration.class);
      for (final InputVarDeclaration varBlock_1 : _filter_1) {
        EList<VarInitDeclaration> _vars_1 = varBlock_1.getVars();
        for (final VarInitDeclaration variablesList_1 : _vars_1) {
          {
            String varType = variablesList_1.getSpec().getType();
            EList<SymbolicVariable> _vars_2 = variablesList_1.getVarList().getVars();
            for (final SymbolicVariable variable : _vars_2) {
              {
                String _name = variable.getName();
                String _plus = ("VAR_INPUT\n" + _name);
                String _plus_1 = (_plus + " : ");
                String _plus_2 = (_plus_1 + varType);
                DiagramNode newNode = new DiagramNode(_plus_2, "ellipse");
                this.addElementToProcId(newNode);
                this.variableId.put(variable.getName(), Integer.valueOf((this.count_id - 1)));
              }
            }
          }
        }
      }
      Iterable<OutputVarDeclaration> _filter_2 = Iterables.<OutputVarDeclaration>filter(IteratorExtensions.<EObject>toIterable(resource.getAllContents()), OutputVarDeclaration.class);
      for (final OutputVarDeclaration varBlock_2 : _filter_2) {
        EList<VarInitDeclaration> _vars_2 = varBlock_2.getVars();
        for (final VarInitDeclaration variablesList_2 : _vars_2) {
          {
            String varType = variablesList_2.getSpec().getType();
            EList<SymbolicVariable> _vars_3 = variablesList_2.getVarList().getVars();
            for (final SymbolicVariable variable : _vars_3) {
              {
                String _name = variable.getName();
                String _plus = ("VAR_OUTPUT\n" + _name);
                String _plus_1 = (_plus + " : ");
                String _plus_2 = (_plus_1 + varType);
                DiagramNode newNode = new DiagramNode(_plus_2, "ellipse");
                this.addElementToProcId(newNode);
                this.variableId.put(variable.getName(), Integer.valueOf((this.count_id - 1)));
              }
            }
          }
        }
      }
      _xblockexpression = this.zeroCountId();
    }
    return _xblockexpression;
  }
  
  public HashSet<String> findVarRefsInExpr(final EObject expression) {
    HashSet<String> resultSet = new HashSet<String>();
    boolean _matched = false;
    if (expression instanceof PrimaryExpression) {
      _matched=true;
      Expression _nestExpr = ((PrimaryExpression)expression).getNestExpr();
      boolean _tripleNotEquals = (_nestExpr != null);
      if (_tripleNotEquals) {
        resultSet.addAll(this.findVarRefsInExpr(((PrimaryExpression)expression).getNestExpr()));
        return resultSet;
      }
      SymbolicVariable _variable = ((PrimaryExpression)expression).getVariable();
      boolean _tripleNotEquals_1 = (_variable != null);
      if (_tripleNotEquals_1) {
        resultSet.add(((PrimaryExpression)expression).getVariable().getName());
        return resultSet;
      }
      ArrayVariable _array = ((PrimaryExpression)expression).getArray();
      boolean _tripleNotEquals_2 = (_array != null);
      if (_tripleNotEquals_2) {
        resultSet.add(((PrimaryExpression)expression).getArray().getVariable().getName());
        resultSet.addAll(this.findVarRefsInExpr(((PrimaryExpression)expression).getArray().getIndex()));
        return resultSet;
      }
      if (((((PrimaryExpression)expression).getFunCall() != null) && (((PrimaryExpression)expression).getFunCall().getArgs() != null))) {
        EList<ParamAssignment> _elements = ((PrimaryExpression)expression).getFunCall().getArgs().getElements();
        for (final ParamAssignment arg : _elements) {
          {
            resultSet.add(arg.getVariable().getName());
            resultSet.addAll(this.findVarRefsInExpr(arg.getValue()));
          }
        }
        return resultSet;
      }
      return resultSet;
    }
    if (!_matched) {
      if (expression instanceof UnaryExpression) {
        _matched=true;
        XorExpression _right = ((UnaryExpression)expression).getRight();
        boolean _tripleNotEquals = (_right != null);
        if (_tripleNotEquals) {
          resultSet.addAll(this.findVarRefsInExpr(((UnaryExpression)expression).getRight()));
        } else {
          resultSet.addAll(this.findVarRefsInExpr(((PrimaryExpression) expression)));
        }
        return resultSet;
      }
    }
    if (!_matched) {
      if (expression instanceof PowerExpression) {
        _matched=true;
        resultSet.addAll(this.findVarRefsInExpr(((PowerExpression)expression).getLeft()));
        resultSet.addAll(this.findVarRefsInExpr(((PowerExpression)expression).getRight()));
        return resultSet;
      }
    }
    if (!_matched) {
      if (expression instanceof MulExpression) {
        _matched=true;
        resultSet.addAll(this.findVarRefsInExpr(((MulExpression)expression).getLeft()));
        resultSet.addAll(this.findVarRefsInExpr(((MulExpression)expression).getRight()));
        return resultSet;
      }
    }
    if (!_matched) {
      if (expression instanceof AddExpression) {
        _matched=true;
        resultSet.addAll(this.findVarRefsInExpr(((AddExpression)expression).getLeft()));
        resultSet.addAll(this.findVarRefsInExpr(((AddExpression)expression).getRight()));
        return resultSet;
      }
    }
    if (!_matched) {
      if (expression instanceof EquExpression) {
        _matched=true;
        resultSet.addAll(this.findVarRefsInExpr(((EquExpression)expression).getLeft()));
        resultSet.addAll(this.findVarRefsInExpr(((EquExpression)expression).getRight()));
        return resultSet;
      }
    }
    if (!_matched) {
      if (expression instanceof CompExpression) {
        _matched=true;
        resultSet.addAll(this.findVarRefsInExpr(((CompExpression)expression).getLeft()));
        resultSet.addAll(this.findVarRefsInExpr(((CompExpression)expression).getRight()));
        return resultSet;
      }
    }
    if (!_matched) {
      if (expression instanceof AndExpression) {
        _matched=true;
        resultSet.addAll(this.findVarRefsInExpr(((AndExpression)expression).getLeft()));
        resultSet.addAll(this.findVarRefsInExpr(((AndExpression)expression).getRight()));
        return resultSet;
      }
    }
    if (!_matched) {
      if (expression instanceof XorExpression) {
        _matched=true;
        resultSet.addAll(this.findVarRefsInExpr(((XorExpression)expression).getLeft()));
        resultSet.addAll(this.findVarRefsInExpr(((XorExpression)expression).getRight()));
        return resultSet;
      }
    }
    return resultSet;
  }
  
  public HashSet<String> findVarRefsInStatement(final EObject statement) {
    HashSet<String> resultSet = new HashSet<String>();
    boolean _matched = false;
    if (statement instanceof Expression) {
      _matched=true;
      resultSet.addAll(this.findVarRefsInExpr(statement));
      return resultSet;
    }
    if (!_matched) {
      if (statement instanceof StatementList) {
        _matched=true;
        EList<Statement> _statements = ((StatementList)statement).getStatements();
        for (final Statement s : _statements) {
          resultSet.addAll(this.findVarRefsInStatement(s));
        }
        return resultSet;
      }
    }
    if (!_matched) {
      if (statement instanceof TimeoutStatement) {
        _matched=true;
        SymbolicVariable _variable = ((TimeoutStatement)statement).getVariable();
        boolean _tripleNotEquals = (_variable != null);
        if (_tripleNotEquals) {
          resultSet.add(((TimeoutStatement)statement).getVariable().getName());
        }
        resultSet.addAll(this.findVarRefsInStatement(((TimeoutStatement)statement).getStatement()));
        return resultSet;
      }
    }
    if (!_matched) {
      if (statement instanceof AssignmentStatement) {
        _matched=true;
        SymbolicVariable _variable = ((AssignmentStatement)statement).getVariable();
        boolean _tripleNotEquals = (_variable != null);
        if (_tripleNotEquals) {
          resultSet.add(((AssignmentStatement)statement).getVariable().getName());
        }
        resultSet.addAll(this.findVarRefsInStatement(((AssignmentStatement)statement).getValue()));
        return resultSet;
      }
    }
    if (!_matched) {
      if (statement instanceof IfStatement) {
        _matched=true;
        resultSet.addAll(this.findVarRefsInStatement(((IfStatement)statement).getMainCond()));
        resultSet.addAll(this.findVarRefsInStatement(((IfStatement)statement).getMainStatement()));
        EList<Expression> _elseIfCond = ((IfStatement)statement).getElseIfCond();
        boolean _tripleNotEquals = (_elseIfCond != null);
        if (_tripleNotEquals) {
          EList<Expression> _elseIfCond_1 = ((IfStatement)statement).getElseIfCond();
          for (final Expression cond : _elseIfCond_1) {
            resultSet.addAll(this.findVarRefsInStatement(cond));
          }
          EList<StatementList> _elseIfStatements = ((IfStatement)statement).getElseIfStatements();
          for (final StatementList condStatement : _elseIfStatements) {
            resultSet.addAll(this.findVarRefsInStatement(condStatement));
          }
        }
        StatementList _elseStatement = ((IfStatement)statement).getElseStatement();
        boolean _tripleNotEquals_1 = (_elseStatement != null);
        if (_tripleNotEquals_1) {
          resultSet.addAll(this.findVarRefsInStatement(((IfStatement)statement).getElseStatement()));
        }
        return resultSet;
      }
    }
    if (!_matched) {
      if (statement instanceof CaseStatement) {
        _matched=true;
        resultSet.addAll(this.findVarRefsInStatement(((CaseStatement)statement).getCond()));
        EList<CaseElement> _caseElements = ((CaseStatement)statement).getCaseElements();
        for (final CaseElement caseElement : _caseElements) {
          resultSet.addAll(this.findVarRefsInStatement(caseElement.getStatement()));
        }
        StatementList _elseStatement = ((CaseStatement)statement).getElseStatement();
        boolean _tripleNotEquals = (_elseStatement != null);
        if (_tripleNotEquals) {
          resultSet.addAll(this.findVarRefsInStatement(((CaseStatement)statement).getElseStatement()));
        }
        return resultSet;
      }
    }
    if (!_matched) {
      if (statement instanceof ForStatement) {
        _matched=true;
        resultSet.add(((ForStatement)statement).getVariable().getName());
        resultSet.addAll(this.findVarRefsInStatement(((ForStatement)statement).getForList().getStart()));
        resultSet.addAll(this.findVarRefsInStatement(((ForStatement)statement).getForList().getEnd()));
        Expression _step = ((ForStatement)statement).getForList().getStep();
        boolean _tripleNotEquals = (_step != null);
        if (_tripleNotEquals) {
          resultSet.addAll(this.findVarRefsInStatement(((ForStatement)statement).getForList().getStep()));
        }
        resultSet.addAll(this.findVarRefsInStatement(((ForStatement)statement).getStatement()));
        return resultSet;
      }
    }
    if (!_matched) {
      if (statement instanceof WhileStatement) {
        _matched=true;
        resultSet.addAll(this.findVarRefsInStatement(((WhileStatement)statement).getCond()));
        resultSet.addAll(this.findVarRefsInStatement(((WhileStatement)statement).getStatement()));
        return resultSet;
      }
    }
    if (!_matched) {
      if (statement instanceof RepeatStatement) {
        _matched=true;
        resultSet.addAll(this.findVarRefsInStatement(((RepeatStatement)statement).getStatement()));
        resultSet.addAll(this.findVarRefsInStatement(((RepeatStatement)statement).getCond()));
        return resultSet;
      }
    }
    return resultSet;
  }
  
  public void generateDataModel(final Resource resource) {
    Iterable<su.nsk.iae.post.poST.Process> _filter = Iterables.<su.nsk.iae.post.poST.Process>filter(IteratorExtensions.<EObject>toIterable(resource.getAllContents()), su.nsk.iae.post.poST.Process.class);
    for (final su.nsk.iae.post.poST.Process process : _filter) {
      {
        EList<VarDeclaration> _procVars = process.getProcVars();
        for (final VarDeclaration varBlock : _procVars) {
          EList<VarInitDeclaration> _vars = varBlock.getVars();
          for (final VarInitDeclaration variablesList : _vars) {
            EList<SymbolicVariable> _vars_1 = variablesList.getVarList().getVars();
            for (final SymbolicVariable variable : _vars_1) {
              {
                ActiveProcess node = new ActiveProcess();
                node.setIdFrom(this.getElementIndexProcId(process.getName()));
                node.setIdTo((this.variableId.get(variable.getName())).intValue());
                node.setAction("declare");
                this.procList.add(node);
              }
            }
          }
        }
        HashSet<String> procVarRefs = new HashSet<String>();
        EList<State> _states = process.getStates();
        for (final State state : _states) {
          EList<Statement> _statements = state.getStatement().getStatements();
          for (final Statement statement : _statements) {
            procVarRefs.addAll(this.findVarRefsInStatement(statement));
          }
        }
        for (final String varName : procVarRefs) {
          {
            ActiveProcess node = new ActiveProcess();
            node.setIdFrom(this.getElementIndexProcId(process.getName()));
            node.setIdTo((this.variableId.get(varName)).intValue());
            node.setAction("use");
            this.procList.add(node);
          }
        }
      }
    }
  }
  
  public void generateDataDiagramModel(final Resource resource) {
    this.generateProcList(resource);
    this.getVariablesNodes(resource);
    this.generateDataModel(resource);
  }
  
  public String generateDataDiagram(final HashMap<String, DiagramNode> nodesId, final ArrayList<ActiveProcess> diagramModel) {
    String result = "";
    System.out.print("Generate GML data diagram...");
    this.procId = nodesId;
    this.procList = diagramModel;
    String _result = result;
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _writeHeadGML = this.gmlTextGenerator.writeHeadGML(this);
    _builder.append(_writeHeadGML);
    _builder.newLineIfNotEmpty();
    result = (_result + _builder);
    String _result_1 = result;
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("\t");
    String _generateNodes = this.gmlTextGenerator.generateNodes(this);
    _builder_1.append(_generateNodes, "\t");
    _builder_1.newLineIfNotEmpty();
    result = (_result_1 + _builder_1);
    String _result_2 = result;
    StringConcatenation _builder_2 = new StringConcatenation();
    _builder_2.append("\t");
    String _generateAllEdges = this.gmlTextGenerator.generateAllEdges(diagramModel);
    _builder_2.append(_generateAllEdges, "\t");
    _builder_2.newLineIfNotEmpty();
    result = (_result_2 + _builder_2);
    String _result_3 = result;
    result = (_result_3 + "]");
    System.out.println("done.");
    return result;
  }
  
  public String generateDataGraphMLDiagram(final HashMap<String, DiagramNode> nodesId, final ArrayList<ActiveProcess> diagramModel, final String url, final String statechartFileNameTail) {
    String result = "";
    System.out.print("Generate GraphML data diagram...");
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
}
