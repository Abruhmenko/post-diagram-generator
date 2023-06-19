package su.nsk.iae.post.diagramgenerator;

import com.google.common.collect.Iterables;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess2;
import org.eclipse.xtext.generator.IGeneratorContext;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import su.nsk.iae.post.generator.IPoSTGenerator;
import su.nsk.iae.post.poST.Model;
import su.nsk.iae.post.poST.Program;

@SuppressWarnings("all")
public class MainGenerator implements IPoSTGenerator {
  protected String WORKING_DIRECTORY = "generated-code";
  
  protected String STATECHART_DIAGRAM_FILE_NAME_TAIL = "_statechart_diagram.gml";
  
  protected Model model;
  
  protected Program program;
  
  protected StatechartDiagramGenerator statechartDiagramGenerator;
  
  protected ActivityDiagramGenerator activityDiagramGenerator;
  
  protected DataDiagramGenerator dataDiagramGenerator;
  
  @Override
  public void setModel(final Model m) {
    this.model = m;
    this.program = m.getPrograms().get(0);
  }
  
  @Override
  public void beforeGenerate(final Resource resource, final IFileSystemAccess2 fsa, final IGeneratorContext context) {
    this.setModel(((Model[])Conversions.unwrapArray((Iterables.<Model>filter(IteratorExtensions.<EObject>toIterable(resource.getAllContents()), Model.class)), Model.class))[0]);
    StatechartDiagramGenerator _statechartDiagramGenerator = new StatechartDiagramGenerator();
    this.statechartDiagramGenerator = _statechartDiagramGenerator;
    ActivityDiagramGenerator _activityDiagramGenerator = new ActivityDiagramGenerator();
    this.activityDiagramGenerator = _activityDiagramGenerator;
    DataDiagramGenerator _dataDiagramGenerator = new DataDiagramGenerator();
    this.dataDiagramGenerator = _dataDiagramGenerator;
  }
  
  @Override
  public void doGenerate(final Resource resource, final IFileSystemAccess2 fsa, final IGeneratorContext context) {
    EList<su.nsk.iae.post.poST.Process> _processes = this.program.getProcesses();
    for (final su.nsk.iae.post.poST.Process process : _processes) {
      {
        this.statechartDiagramGenerator.generateStatechartDiagramModel(resource, process);
        StringConcatenation _builder = new StringConcatenation();
        _builder.append(this.WORKING_DIRECTORY);
        _builder.append("/");
        String _name = process.getName();
        _builder.append(_name);
        _builder.append(this.STATECHART_DIAGRAM_FILE_NAME_TAIL);
        fsa.generateFile(_builder.toString(), this.statechartDiagramGenerator.generateStatechartDiagram(process));
        this.statechartDiagramGenerator.clear();
      }
    }
    this.activityDiagramGenerator.generateActivityDiagramModel(resource);
    ResultOfSeparation activityResult = this.activityDiagramGenerator.separateDiadram();
    for (int i = 0; (i < activityResult.getDiagramComponents().size()); i++) {
      {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append(this.WORKING_DIRECTORY);
        _builder.append("/processes_connection_diagram_");
        _builder.append(i);
        _builder.append(".gml");
        fsa.generateFile(_builder.toString(), this.activityDiagramGenerator.generateActivityDiagram(activityResult.getDiagramComponentNodes().get(i), activityResult.getDiagramComponents().get(i)));
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append(this.WORKING_DIRECTORY);
        _builder_1.append("/processes_connection_diagram_");
        _builder_1.append(i);
        _builder_1.append(".graphml");
        fsa.generateFile(_builder_1.toString(), this.activityDiagramGenerator.generateActivityGraphMLDiagram(activityResult.getDiagramComponentNodes().get(i), activityResult.getDiagramComponents().get(i), this.WORKING_DIRECTORY, this.STATECHART_DIAGRAM_FILE_NAME_TAIL));
        this.activityDiagramGenerator.clear();
      }
    }
    this.dataDiagramGenerator.generateDataDiagramModel(resource);
    ResultOfSeparation dataResult = this.dataDiagramGenerator.separateDiadram();
    for (int j = 0; (j < dataResult.getDiagramComponents().size()); j++) {
      {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append(this.WORKING_DIRECTORY);
        _builder.append("/variables_usage_diagram_");
        _builder.append(j);
        _builder.append(".gml");
        fsa.generateFile(_builder.toString(), this.dataDiagramGenerator.generateDataDiagram(dataResult.getDiagramComponentNodes().get(j), dataResult.getDiagramComponents().get(j)));
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append(this.WORKING_DIRECTORY);
        _builder_1.append("/variables_usage_diagram_");
        _builder_1.append(j);
        _builder_1.append(".graphml");
        fsa.generateFile(_builder_1.toString(), this.dataDiagramGenerator.generateDataGraphMLDiagram(dataResult.getDiagramComponentNodes().get(j), dataResult.getDiagramComponents().get(j), this.WORKING_DIRECTORY, this.STATECHART_DIAGRAM_FILE_NAME_TAIL));
        this.dataDiagramGenerator.clear();
      }
    }
  }
  
  @Override
  public void afterGenerate(final Resource resource, final IFileSystemAccess2 fsa, final IGeneratorContext context) {
  }
}
