package su.nsk.iae.post.diagramgenerator;

@SuppressWarnings("all")
public class DiagramNode {
  private int index;
  
  private String name;
  
  private String shape;
  
  private int visited;
  
  public DiagramNode(final String name, final String shape) {
    this.name = name;
    this.shape = shape;
    this.visited = (-1);
  }
  
  public DiagramNode(final String name) {
    this.name = name;
    this.shape = "roundrectangle";
    this.visited = (-1);
  }
  
  public int setIndex(final int ind) {
    return this.index = ind;
  }
  
  public String setName(final String newName) {
    return this.name = newName;
  }
  
  public String setShape(final String newShape) {
    return this.shape = newShape;
  }
  
  public void setVisited(final int visit) {
    this.visited = visit;
  }
  
  public int getIndex() {
    return this.index;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getShape() {
    return this.shape;
  }
  
  public int getVisited() {
    return this.visited;
  }
}
