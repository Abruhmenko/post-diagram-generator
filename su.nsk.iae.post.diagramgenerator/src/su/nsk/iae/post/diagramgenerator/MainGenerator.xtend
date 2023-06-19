package su.nsk.iae.post.diagramgenerator

import su.nsk.iae.post.generator.IPoSTGenerator
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext
import su.nsk.iae.post.poST.Model
import su.nsk.iae.post.poST.Program
class MainGenerator implements IPoSTGenerator {
	
	protected String WORKING_DIRECTORY = "generated-code"
	protected String STATECHART_DIAGRAM_FILE_NAME_TAIL = "_statechart_diagram.gml"
	
	protected Model model
	protected Program program
	protected StatechartDiagramGenerator statechartDiagramGenerator
	protected ActivityDiagramGenerator activityDiagramGenerator
	protected DataDiagramGenerator dataDiagramGenerator
	
	override void setModel(Model m) {
		model = m
		program = m.programs.get(0)
	}
	
	override void beforeGenerate(Resource resource, IFileSystemAccess2 fsa, IGeneratorContext context) {
		setModel(resource.allContents.toIterable.filter(Model).get(0))
		statechartDiagramGenerator = new StatechartDiagramGenerator()
		activityDiagramGenerator = new ActivityDiagramGenerator()
		dataDiagramGenerator = new DataDiagramGenerator()
	}
	
	override void doGenerate(Resource resource, IFileSystemAccess2 fsa, IGeneratorContext context) {
		
		// generate "statechart" diagram for each process
		for (process : program.processes) {
			statechartDiagramGenerator.generateStatechartDiagramModel(resource, process)
			fsa.generateFile('''«WORKING_DIRECTORY»/«process.name»«STATECHART_DIAGRAM_FILE_NAME_TAIL»''', statechartDiagramGenerator.generateStatechartDiagram(process))
			statechartDiagramGenerator.clear()
		}
		
		// generate "processes connection" diagram
		activityDiagramGenerator.generateActivityDiagramModel(resource)
		var ResultOfSeparation activityResult = activityDiagramGenerator.separateDiadram()
		for (var i = 0; i < activityResult.getDiagramComponents.size(); i ++) {
			fsa.generateFile('''«WORKING_DIRECTORY»/processes_connection_diagram_«i».gml''', activityDiagramGenerator.generateActivityDiagram(activityResult.getDiagramComponentNodes.get(i), activityResult.getDiagramComponents.get(i)));
			fsa.generateFile('''«WORKING_DIRECTORY»/processes_connection_diagram_«i».graphml''', activityDiagramGenerator.generateActivityGraphMLDiagram(activityResult.getDiagramComponentNodes.get(i), activityResult.getDiagramComponents.get(i), WORKING_DIRECTORY, STATECHART_DIAGRAM_FILE_NAME_TAIL));
     		activityDiagramGenerator.clear()
		}
		
		// generate "variables usage" diagram
		dataDiagramGenerator.generateDataDiagramModel(resource)
		var ResultOfSeparation dataResult = dataDiagramGenerator.separateDiadram()
		for (var j = 0; j < dataResult.getDiagramComponents.size(); j ++) {
			fsa.generateFile('''«WORKING_DIRECTORY»/variables_usage_diagram_«j».gml''', dataDiagramGenerator.generateDataDiagram(dataResult.getDiagramComponentNodes.get(j), dataResult.getDiagramComponents.get(j)));
			fsa.generateFile('''«WORKING_DIRECTORY»/variables_usage_diagram_«j».graphml''', dataDiagramGenerator.generateDataGraphMLDiagram(dataResult.getDiagramComponentNodes.get(j), dataResult.getDiagramComponents.get(j), WORKING_DIRECTORY, STATECHART_DIAGRAM_FILE_NAME_TAIL));
     		dataDiagramGenerator.clear()
		}
	}
	
	override void afterGenerate(Resource resource, IFileSystemAccess2 fsa, IGeneratorContext context) {
		
	}
	
}