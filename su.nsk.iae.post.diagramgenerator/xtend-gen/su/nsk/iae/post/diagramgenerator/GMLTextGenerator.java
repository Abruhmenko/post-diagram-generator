package su.nsk.iae.post.diagramgenerator;

import java.util.ArrayList;
import java.util.Collection;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class GMLTextGenerator {
  public CharSequence writeHeadGML(final ProcessDiagramGenerator generator) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Creator\t\"tranlator\"");
    _builder.newLine();
    _builder.append("Version\t\"2.15\"");
    _builder.newLine();
    _builder.append("graph");
    _builder.newLine();
    _builder.append("[");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("hierarchic\t1");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("label\t\"\"");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("directed\t1");
    int _zeroCountId = generator.zeroCountId();
    _builder.append(_zeroCountId, "\t");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence generateNodeGraphics(final int nameLength, final String typeOfNode) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("graphics");
    _builder.newLine();
    _builder.append("[");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("w\t");
    _builder.append((nameLength * 10), "\t");
    _builder.append(".0");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("h\t48.0");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("type\t\"");
    _builder.append(typeOfNode, "\t");
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("raisedBorder\t0");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("fill\t\"#FFFFFF\"");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("outline\t\"#000000\"");
    _builder.newLine();
    _builder.append("]");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence generateLabelGraphics(final String label) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("LabelGraphics");
    _builder.newLine();
    _builder.append("[");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("text\t\"");
    _builder.append(label, "\t");
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("fontSize\t12");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("fontName\t\"Dialog\"");
    _builder.newLine();
    _builder.append("]");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence generateOneProcessNode(final int processId, final String processName, final String typeOfNode) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("node");
    _builder.newLine();
    _builder.append("[");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("id\t");
    _builder.append(processId, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("label\t\"");
    _builder.append(processName, "\t");
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    CharSequence _generateNodeGraphics = this.generateNodeGraphics(processName.length(), typeOfNode);
    _builder.append(_generateNodeGraphics, "    ");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    CharSequence _generateLabelGraphics = this.generateLabelGraphics(processName);
    _builder.append(_generateLabelGraphics, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("]");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence generateOneEdge(final int fromId, final int toId, final String label) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("edge");
    _builder.newLine();
    _builder.append("[");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("source\t");
    _builder.append(fromId, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("target\t");
    _builder.append(toId, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("label\t\"");
    _builder.append(label, "\t");
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    _builder.append("]");
    _builder.newLine();
    return _builder;
  }
  
  public String generateAllEdges(final ArrayList<ActiveProcess> procList) {
    String tempString = "";
    for (int i = 0; (i < procList.size()); i++) {
      String _tempString = tempString;
      CharSequence _generateOneEdge = this.generateOneEdge(procList.get(i).getIdFrom(), procList.get(i).getIdTo(), procList.get(i).getAction());
      tempString = (_tempString + _generateOneEdge);
    }
    return tempString;
  }
  
  public String generateNodes(final ProcessDiagramGenerator generator) {
    String tempString = "";
    Collection<DiagramNode> _values = generator.procId.values();
    for (final DiagramNode e : _values) {
      String _tempString = tempString;
      CharSequence _generateOneProcessNode = this.generateOneProcessNode(generator.getElementIndexProcId(e.getName()), e.getName(), e.getShape());
      tempString = (_tempString + _generateOneProcessNode);
    }
    return tempString;
  }
}
