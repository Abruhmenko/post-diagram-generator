package su.nsk.iae.post.diagramgenerator;

import com.google.common.collect.Iterables;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

@SuppressWarnings("all")
public class ProcessDiagramGraphMLGenerator extends ProcessDiagramGenerator {
  protected GraphMLTextGenerator graphMLTextGenerator = new GraphMLTextGenerator();
  
  protected GraphSeparator graphSeparator = new GraphSeparator();
  
  public void generateProcList(final Resource resource) {
    Iterable<su.nsk.iae.post.poST.Process> _filter = Iterables.<su.nsk.iae.post.poST.Process>filter(IteratorExtensions.<EObject>toIterable(resource.getAllContents()), su.nsk.iae.post.poST.Process.class);
    for (final su.nsk.iae.post.poST.Process e : _filter) {
      {
        String _name = e.getName();
        DiagramNode newNode = new DiagramNode(_name);
        this.addElementToProcId(newNode);
      }
    }
    this.zeroCountId();
  }
  
  public ResultOfSeparation separateDiadram() {
    return this.graphSeparator.separateGraph(this.procList, this.procId);
  }
}
