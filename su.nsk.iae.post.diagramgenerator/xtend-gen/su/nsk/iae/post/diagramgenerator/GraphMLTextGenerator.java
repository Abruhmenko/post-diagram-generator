package su.nsk.iae.post.diagramgenerator;

import java.util.ArrayList;
import java.util.Collection;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class GraphMLTextGenerator {
  public CharSequence headGraphMlGenerator(final ProcessDiagramGenerator generator) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
    _builder.newLine();
    _builder.append("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\" xmlns:java=\"http://www.yworks.com/xml/yfiles-common/1.0/java\" xmlns:sys=\"http://www.yworks.com/xml/yfiles-common/markup/primitives/2.0\" xmlns:x=\"http://www.yworks.com/xml/yfiles-common/markup/2.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:y=\"http://www.yworks.com/xml/graphml\" xmlns:yed=\"http://www.yworks.com/xml/yed/3\" xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns http://www.yworks.com/xml/schema/graphml/1.1/ygraphml.xsd\">");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<key for=\"port\" id=\"d0\" yfiles.type=\"portgraphics\"/>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<key for=\"port\" id=\"d1\" yfiles.type=\"portgeometry\"/>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<key for=\"port\" id=\"d2\" yfiles.type=\"portuserdata\"/>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<key attr.name=\"url\" attr.type=\"string\" for=\"node\" id=\"d3\"/>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<key for=\"node\" id=\"d5\" yfiles.type=\"nodegraphics\"/>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<key for=\"graphml\" id=\"d6\" yfiles.type=\"resources\"/>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<key for=\"edge\" id=\"d9\" yfiles.type=\"edgegraphics\"/>");
    int _zeroCountId = generator.zeroCountId();
    _builder.append(_zeroCountId, "  ");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence generateShapeNodeGraphML(final int nameLength, final String processName, final String shapetype) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<y:ShapeNode>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<y:Geometry height=\"48.0\" width=\"");
    _builder.append((nameLength * 10), "  ");
    _builder.append("\"/>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<y:Fill color=\"#FFFFFF\" transparent=\"false\"/>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<y:BorderStyle color=\"#000000\" raised=\"false\" type=\"line\" width=\"1.0\"/>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<y:NodeLabel alignment=\"center\" fontFamily=\"Dialog\" fontSize=\"12\" fontStyle=\"plain\" hasBackgroundColor=\"false\" hasLineColor=\"false\" height=\"18.701171875\" horizontalTextPosition=\"center\" iconTextGap=\"4\" modelName=\"internal\" modelPosition=\"c\" textColor=\"#000000\" verticalTextPosition=\"bottom\" visible=\"true\" width=\"26.6640625\">");
    _builder.append(processName, "  ");
    _builder.append("</y:NodeLabel>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<y:Shape type=\"");
    _builder.append(shapetype, "  ");
    _builder.append("\"/>");
    _builder.newLineIfNotEmpty();
    _builder.append("</y:ShapeNode>");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence dataKeysGraphMLGenerator(final int nameLength, final String processName, final String shapetype, final String urlStatechartDiagram) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<data key=\"d3\"><![CDATA[");
    _builder.append(urlStatechartDiagram);
    _builder.append("]]></data>");
    _builder.newLineIfNotEmpty();
    _builder.append("<data key=\"d5\">");
    _builder.newLine();
    _builder.append("  ");
    CharSequence _generateShapeNodeGraphML = this.generateShapeNodeGraphML(nameLength, processName, shapetype);
    _builder.append(_generateShapeNodeGraphML, "  ");
    _builder.newLineIfNotEmpty();
    _builder.append("</data>");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence nodeGraphMLGenerate(final int processId, final String processName, final String shapetype, final String urlStatechartDiagram) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<node id=\"n");
    _builder.append(processId);
    _builder.append("\">");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    CharSequence _dataKeysGraphMLGenerator = this.dataKeysGraphMLGenerator(processName.length(), processName, shapetype, urlStatechartDiagram);
    _builder.append(_dataKeysGraphMLGenerator, "  ");
    _builder.newLineIfNotEmpty();
    _builder.append("</node>");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence generateOneEdgeGraphML(final int edgeId, final int fromId, final int toId, final String edgeLabel) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<edge id=\"e");
    _builder.append(edgeId);
    _builder.append("\" source=\"n");
    _builder.append(fromId);
    _builder.append("\" target=\"n");
    _builder.append(toId);
    _builder.append("\">");
    _builder.newLineIfNotEmpty();
    _builder.append("      ");
    _builder.append("<data key=\"d9\">");
    _builder.newLine();
    _builder.append("        ");
    CharSequence _generatePoliLineEdgeGraphML = this.generatePoliLineEdgeGraphML(edgeLabel);
    _builder.append(_generatePoliLineEdgeGraphML, "        ");
    _builder.newLineIfNotEmpty();
    _builder.append("      ");
    _builder.append("</data>");
    _builder.newLine();
    _builder.append("</edge>");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence generatePoliLineEdgeGraphML(final String edgeLabel) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<y:PolyLineEdge>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<y:LineStyle color=\"#000000\" type=\"line\" width=\"1.0\"/>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<y:Arrows source=\"none\" target=\"standard\"/>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<y:EdgeLabel alignment=\"center\" fontFamily=\"Dialog\" fontSize=\"12\" fontStyle=\"plain\" hasBackgroundColor=\"false\" hasLineColor=\"false\" horizontalTextPosition=\"center\" iconTextGap=\"4\" preferredPlacement=\"anywhere\" textColor=\"#000000\" verticalTextPosition=\"bottom\" visible=\"true\">");
    _builder.append(edgeLabel, "  ");
    _builder.append("<y:PreferredPlacementDescriptor/>");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("</y:EdgeLabel>");
    _builder.newLine();
    _builder.append("</y:PolyLineEdge>");
    _builder.newLine();
    return _builder;
  }
  
  public String generateAllEdges(final ArrayList<ActiveProcess> procList) {
    String tempString = "";
    for (int i = 0; (i < procList.size()); i++) {
      String _tempString = tempString;
      CharSequence _generateOneEdgeGraphML = this.generateOneEdgeGraphML(i, procList.get(i).getIdFrom(), procList.get(i).getIdTo(), procList.get(i).getAction());
      tempString = (_tempString + _generateOneEdgeGraphML);
    }
    return tempString;
  }
  
  public String generateNodes(final ProcessDiagramGenerator generator, final String url, final String statechartFileNameTail) {
    String tempString = "";
    Collection<DiagramNode> _values = generator.procId.values();
    for (final DiagramNode e : _values) {
      String _tempString = tempString;
      int _elementIndexProcId = generator.getElementIndexProcId(e.getName());
      String _name = e.getName();
      String _shape = e.getShape();
      String _name_1 = e.getName();
      String _plus = ((url + "/") + _name_1);
      String _plus_1 = (_plus + statechartFileNameTail);
      CharSequence _nodeGraphMLGenerate = this.nodeGraphMLGenerate(_elementIndexProcId, _name, _shape, _plus_1);
      tempString = (_tempString + _nodeGraphMLGenerate);
    }
    return tempString;
  }
}
