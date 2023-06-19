package su.nsk.iae.post.diagramgenerator;

@SuppressWarnings("all")
public class ProcessId {
  private int processId = 0;
  
  private String processName;
  
  public void setId(final int id) {
    this.processId = id;
  }
  
  public void setName(final String name) {
    this.processName = name;
  }
  
  public int getId() {
    return this.processId;
  }
  
  public String getName() {
    return this.processName;
  }
}
