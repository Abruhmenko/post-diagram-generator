package su.nsk.iae.post.diagramgenerator;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("all")
public class ProcessDiagramGenerator {
  protected int count_id = 0;
  
  protected ArrayList<ActiveProcess> procList = new ArrayList<ActiveProcess>();
  
  protected HashMap<String, DiagramNode> procId = new HashMap<String, DiagramNode>();
  
  protected GMLTextGenerator gmlTextGenerator = new GMLTextGenerator();
  
  public ProcessDiagramGenerator() {
    this.count_id = 0;
  }
  
  public boolean addElementToProcList(final ActiveProcess elem) {
    return this.procList.add(elem);
  }
  
  public boolean addAllElementsToProcList(final ArrayList<ActiveProcess> list) {
    return this.procList.addAll(list);
  }
  
  public int addElementToProcId(final DiagramNode elem) {
    int _xblockexpression = (int) 0;
    {
      elem.setIndex(this.count_id);
      this.procId.put(elem.getName(), elem);
      _xblockexpression = this.count_id++;
    }
    return _xblockexpression;
  }
  
  public DiagramNode addElementToProcId(final int ind, final DiagramNode elem) {
    DiagramNode _xblockexpression = null;
    {
      elem.setIndex(ind);
      _xblockexpression = this.procId.put(elem.getName(), elem);
    }
    return _xblockexpression;
  }
  
  public DiagramNode getElementProcId(final String key) {
    return this.procId.get(key);
  }
  
  public int getElementIndexProcId(final String key) {
    return this.procId.get(key).getIndex();
  }
  
  public int zeroCountId() {
    return this.count_id = 0;
  }
  
  public int incrementCountId() {
    return this.count_id++;
  }
  
  public int getCountId() {
    return this.count_id;
  }
  
  public void clear() {
    this.procList.clear();
    this.count_id = 0;
    this.procId.clear();
  }
}
