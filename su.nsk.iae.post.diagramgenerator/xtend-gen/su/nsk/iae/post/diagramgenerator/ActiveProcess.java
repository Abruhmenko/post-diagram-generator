package su.nsk.iae.post.diagramgenerator;

@SuppressWarnings("all")
public class ActiveProcess {
  private int idFrom;
  
  private int idTo;
  
  private String action;
  
  public ActiveProcess() {
    this.idFrom = 0;
    this.idTo = 0;
    this.action = "";
  }
  
  public void setAction(final String act) {
    this.action = act;
  }
  
  public void setIdFrom(final int id) {
    this.idFrom = id;
  }
  
  public void setIdTo(final int id) {
    this.idTo = id;
  }
  
  public String getAction() {
    return this.action;
  }
  
  public int getIdFrom() {
    return this.idFrom;
  }
  
  public int getIdTo() {
    return this.idTo;
  }
}
