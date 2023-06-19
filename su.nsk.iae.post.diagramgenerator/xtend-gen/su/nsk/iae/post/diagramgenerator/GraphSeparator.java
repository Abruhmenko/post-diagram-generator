package su.nsk.iae.post.diagramgenerator;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

@SuppressWarnings("all")
public class GraphSeparator {
  private ResultOfSeparation result = new ResultOfSeparation();
  
  private LinkedList<DiagramNode> nodesQuery = new LinkedList<DiagramNode>();
  
  private LinkedHashSet<Integer> deleteIndex = new LinkedHashSet<Integer>();
  
  private ArrayList<Integer> deleteIndexNotUnique = new ArrayList<Integer>();
  
  private ArrayList<ArrayList<ActiveProcess>> componentList = new ArrayList<ArrayList<ActiveProcess>>();
  
  private ArrayList<HashMap<String, DiagramNode>> nodeList = new ArrayList<HashMap<String, DiagramNode>>();
  
  private int currentComponentIndex = 0;
  
  public ResultOfSeparation separateGraph(final ArrayList<ActiveProcess> procList, final HashMap<String, DiagramNode> nodeIdList) {
    this.componentList.clear();
    this.deleteIndex.clear();
    this.nodesQuery.clear();
    this.nodeList.clear();
    this.currentComponentIndex = 0;
    Set<String> _keySet = nodeIdList.keySet();
    for (final String e : _keySet) {
      {
        ArrayList<ActiveProcess> tempComponent = new ArrayList<ActiveProcess>();
        HashMap<String, DiagramNode> tempNodeList = new HashMap<String, DiagramNode>();
        if (((nodeIdList.get(e).getVisited() == (-1)) && Objects.equal(nodeIdList.get(e).getShape(), "roundrectangle"))) {
          this.nodesQuery.addLast(nodeIdList.get(e));
        }
        this.deleteIndex.clear();
        this.deleteIndexNotUnique.clear();
        while ((this.nodesQuery.size() > 0)) {
          {
            DiagramNode element = this.nodesQuery.pollFirst();
            String _name = element.getName();
            String _plus = ("pop: " + _name);
            System.out.println(_plus);
            System.out.println("nodeList:");
            for (int i = 0; (i < this.nodeList.size()); i++) {
              {
                System.out.print((("i:" + Integer.valueOf(i)) + " "));
                Collection<DiagramNode> _values = this.nodeList.get(i).values();
                for (final DiagramNode g : _values) {
                  String _name_1 = g.getName();
                  String _plus_1 = (" " + _name_1);
                  String _plus_2 = (_plus_1 + "(");
                  int _visited = g.getVisited();
                  String _plus_3 = (_plus_2 + Integer.valueOf(_visited));
                  String _plus_4 = (_plus_3 + ") ");
                  System.out.print(_plus_4);
                }
                System.out.println(" ");
              }
            }
            int _visited = element.getVisited();
            boolean _equals = (_visited == (-1));
            if (_equals) {
              element.setVisited(this.currentComponentIndex);
              String _name_1 = element.getName();
              String _plus_1 = ("didn\'t visited (" + _name_1);
              String _plus_2 = (_plus_1 + "): ");
              String _plus_3 = (_plus_2 + Integer.valueOf(this.currentComponentIndex));
              String _plus_4 = (_plus_3 + "  ");
              System.out.print(_plus_4);
              tempComponent.addAll(this.getActiveListByNode(procList, element));
              tempNodeList.put(element.getName(), element);
              ArrayList<DiagramNode> childrenList = this.getElementList(procList, nodeIdList, element);
              for (final DiagramNode ch : childrenList) {
                String _name_2 = ch.getName();
                String _plus_5 = (",  ch:  " + _name_2);
                System.out.print(_plus_5);
              }
              System.out.println(" ");
              this.nodesQuery.addAll(childrenList);
              System.out.println(" Query:");
              for (int a = 0; (a < this.nodesQuery.size()); a++) {
                String _name_3 = this.nodesQuery.get(a).getName();
                String _plus_6 = ("  " + _name_3);
                String _plus_7 = (_plus_6 + "(");
                int _visited_1 = this.nodesQuery.get(a).getVisited();
                String _plus_8 = (_plus_7 + Integer.valueOf(_visited_1));
                String _plus_9 = (_plus_8 + ") ");
                System.out.print(_plus_9);
              }
              System.out.println(" ");
            } else {
              int _visited_1 = element.getVisited();
              String _plus_6 = ("element.getVisited() = " + Integer.valueOf(_visited_1));
              String _plus_7 = (_plus_6 + ", currentComponentIndex = ");
              String _plus_8 = (_plus_7 + Integer.valueOf(this.currentComponentIndex));
              System.out.println(_plus_8);
              int _visited_2 = element.getVisited();
              boolean _notEquals = (_visited_2 != this.currentComponentIndex);
              if (_notEquals) {
                int _visited_3 = element.getVisited();
                boolean _lessThan = (_visited_3 < this.currentComponentIndex);
                if (_lessThan) {
                  System.out.println("< currentIndex");
                  String _name_3 = element.getName();
                  String _plus_9 = ("element.name = " + _name_3);
                  String _plus_10 = (_plus_9 + " element.visited = ");
                  int _visited_4 = element.getVisited();
                  String _plus_11 = (_plus_10 + Integer.valueOf(_visited_4));
                  System.out.println(_plus_11);
                  this.setVisitedForList(tempNodeList, element.getVisited());
                  tempComponent.addAll(this.componentList.get(element.getVisited()));
                  tempNodeList.putAll(this.nodeList.get(element.getVisited()));
                  System.out.println(("last component = " + Integer.valueOf(this.currentComponentIndex)));
                  this.currentComponentIndex = element.getVisited();
                  System.out.println(("now component = " + Integer.valueOf(this.currentComponentIndex)));
                  this.deleteIndex.add(Integer.valueOf(element.getVisited()));
                  this.deleteIndexNotUnique.add(Integer.valueOf(element.getVisited()));
                } else {
                  int _visited_5 = element.getVisited();
                  boolean _greaterThan = (_visited_5 > this.currentComponentIndex);
                  if (_greaterThan) {
                    System.out.println("> currentIndex");
                    int lastComponentIndex = element.getVisited();
                    String _name_4 = element.getName();
                    String _plus_12 = ("element.name = " + _name_4);
                    String _plus_13 = (_plus_12 + " element.visited = ");
                    String _plus_14 = (_plus_13 + Integer.valueOf(lastComponentIndex));
                    System.out.println(_plus_14);
                    this.setVisitedForList(this.nodeList.get(lastComponentIndex), this.currentComponentIndex);
                    tempComponent.addAll(this.componentList.get(lastComponentIndex));
                    tempNodeList.putAll(this.nodeList.get(lastComponentIndex));
                    System.out.println(((("now component = " + Integer.valueOf(this.currentComponentIndex)) + ", last:") + Integer.valueOf(lastComponentIndex)));
                    this.deleteIndex.add(Integer.valueOf(lastComponentIndex));
                    this.deleteIndexNotUnique.add(Integer.valueOf(lastComponentIndex));
                  }
                }
              }
            }
          }
        }
        Collections.<Integer>sort(this.deleteIndexNotUnique);
        for (int i = 0; (i < this.deleteIndexNotUnique.size()); i++) {
          boolean _contains = this.deleteIndex.contains(this.deleteIndexNotUnique.get(i));
          if (_contains) {
            this.deleteIndex.remove(this.deleteIndexNotUnique.get(i));
            Integer _get = this.deleteIndexNotUnique.get(i);
            int _minus = ((_get).intValue() - i);
            this.componentList.remove(_minus);
            Integer _get_1 = this.deleteIndexNotUnique.get(i);
            int _minus_1 = ((_get_1).intValue() - i);
            this.nodeList.remove(_minus_1);
          }
        }
        int _size = tempNodeList.size();
        boolean _greaterThan = (_size > 0);
        if (_greaterThan) {
          this.componentList.add(this.currentComponentIndex, tempComponent);
          this.nodeList.add(this.currentComponentIndex, tempNodeList);
        }
        for (int t = 0; (t < this.nodeList.size()); t++) {
          this.setVisitedForList(this.nodeList.get(t), t);
        }
        this.currentComponentIndex = this.componentList.size();
        System.out.println(("size = " + Integer.valueOf(this.currentComponentIndex)));
      }
    }
    this.result.setDiagramComponents(this.componentList);
    this.result.setDiagramComponentNodes(this.nodeList);
    return this.result;
  }
  
  public ArrayList<DiagramNode> getElementList(final ArrayList<ActiveProcess> procList, final HashMap<String, DiagramNode> nodeIdList, final DiagramNode element) {
    ArrayList<DiagramNode> tempList = new ArrayList<DiagramNode>();
    ArrayList<Integer> idList = new ArrayList<Integer>();
    int idElem = element.getIndex();
    for (final ActiveProcess e : procList) {
      int _idFrom = e.getIdFrom();
      boolean _equals = (_idFrom == idElem);
      if (_equals) {
        idList.add(Integer.valueOf(e.getIdTo()));
      }
    }
    Collection<DiagramNode> _values = nodeIdList.values();
    for (final DiagramNode e_1 : _values) {
      for (final Integer in : idList) {
        int _index = e_1.getIndex();
        boolean _equals_1 = (_index == (in).intValue());
        if (_equals_1) {
          tempList.add(e_1);
        }
      }
    }
    return tempList;
  }
  
  public ArrayList<ActiveProcess> getActiveListByNode(final ArrayList<ActiveProcess> procList, final DiagramNode element) {
    ArrayList<ActiveProcess> tmpList = new ArrayList<ActiveProcess>();
    for (final ActiveProcess e : procList) {
      int _idFrom = e.getIdFrom();
      int _index = element.getIndex();
      boolean _equals = (_idFrom == _index);
      if (_equals) {
        tmpList.add(e);
      }
    }
    return tmpList;
  }
  
  public void setVisitedForList(final HashMap<String, DiagramNode> tempNodeList, final int component) {
    Collection<DiagramNode> _values = tempNodeList.values();
    for (final DiagramNode e : _values) {
      {
        for (final DiagramNode nodeInQ : this.nodesQuery) {
          String _name = nodeInQ.getName();
          String _name_1 = e.getName();
          boolean _equals = Objects.equal(_name, _name_1);
          if (_equals) {
            nodeInQ.setVisited(component);
          }
        }
        e.setVisited(component);
      }
    }
  }
  
  public DiagramNode getElementNodeIdList(final HashMap<String, DiagramNode> nodeIdList, final int index) {
    Collection<DiagramNode> _values = nodeIdList.values();
    for (final DiagramNode e : _values) {
      int _index = e.getIndex();
      boolean _equals = (_index == index);
      if (_equals) {
        return e;
      }
    }
    return null;
  }
}
