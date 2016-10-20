package graphInterface.graph.node;

import graphInterface.graph.edge.Edge;
import graphInterface.property.EditType;
import graphInterface.property.PropertyKey;
import graphInterface.property.Selectable;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Node implements Selectable {
	private List<Edge> incomingEdges, outgoingEdges;
	private static int nodeWidth = 10, nodeHeight = 6, nodeTextLength = 10;
	private Properties properties;
	private Color currentColor;

	public enum NodeEnum implements PropertyKey {

		NODE_NAME("Name", EditType.STRING), NODE_COLOR("Color", EditType.COLOR), X_POS(
				"X", EditType.INTEGER), Y_POS("Y", EditType.INTEGER), NODE_SELECTED_COLOR(
				"Selected Color", EditType.COLOR), TEXT_COLOR("Text Color",
				EditType.COLOR);

		private String propertyName;
		private EditType type;

		private NodeEnum(String propertyName, EditType type) {
			this.propertyName = propertyName;
			this.type = type;
		}

		public String getPropertyName() {
			return propertyName;
		}

		public EditType getType() {
			return type;
		}

	}

	public Node(String name, int centerX, int centerY, Color defaultColor,
			Color defaultSelectedColor, int nodeWidth, int nodeHeight) {
		this.incomingEdges = new ArrayList<Edge>();
		this.outgoingEdges = new ArrayList<Edge>();
		this.properties = new Properties();
		this.currentColor = defaultColor;
		properties.put(NodeEnum.NODE_NAME, name);
		properties.put(NodeEnum.NODE_COLOR, defaultColor);
		properties.put(NodeEnum.X_POS, centerX);
		properties.put(NodeEnum.Y_POS, centerY);
		properties.put(NodeEnum.NODE_SELECTED_COLOR, defaultSelectedColor);
		properties.put(NodeEnum.TEXT_COLOR, Color.WHITE);
		Node.nodeWidth = nodeWidth;
		Node.nodeHeight = nodeHeight;

	}

	public Color getTextColor() {
		return (Color) properties.get(NodeEnum.TEXT_COLOR);
	}

	public void setTextColor(Color textColor) {
		properties.replace(NodeEnum.TEXT_COLOR, textColor);
	}

	public int getMinX() {
		return (Integer) properties.get(NodeEnum.X_POS);
	}

	public int getMinY() {
		return (Integer) properties.get(NodeEnum.Y_POS);
	}

	public void setMinX(int minX) {
		properties.replace(NodeEnum.X_POS, minX);
	}

	public void setMinY(int minY) {
		properties.replace(NodeEnum.Y_POS, minY);
	}

	public void setSelectedColor(Color selColor) {
		properties.replace(NodeEnum.NODE_SELECTED_COLOR, selColor);
		currentColor = selColor;
	}

	public Color getSelectedColor() {
		return (Color) properties.get(NodeEnum.NODE_SELECTED_COLOR);
	}

	public void addIncomingEdge(Edge edge) {
		this.incomingEdges.add(edge);
		edge.setEndNode(this);
	}

	public void addOutgoingEdge(Edge edge) {
		this.outgoingEdges.add(edge);
		edge.setStartNode(this);
	}

	public void removeIncomingEdge(Edge edge) {
		if (this.incomingEdges.contains(edge)) {
			this.incomingEdges.remove(edge);
		}
	}

	public void removeOutgoingEdge(Edge edge) {
		if (this.outgoingEdges.contains(edge)) {
			this.outgoingEdges.remove(edge);
		}
	}

	public List<Edge> getIncomingEdges() {
		return this.incomingEdges;
	}

	public List<Edge> getOutgoingEdges() {
		return this.outgoingEdges;
	}

	public Color getUnselectedColor() {
		return (Color) properties.get(NodeEnum.NODE_COLOR);
	}

	public void setUnselectedColor(Color color) {
		properties.replace(NodeEnum.NODE_COLOR, color);
	}

	public Color getColor() {
		return currentColor;
	}

	public void setColor(Color color) {
		this.currentColor = color;
	}

	public String getText() {
		return (String) properties.get(NodeEnum.NODE_NAME);
	}

	public void setText(String text) {
		properties.replace(NodeEnum.NODE_NAME, text);
	}

	public static int getNodeWidth() {
		return nodeWidth;
	}

	public static void setNodeWidth(int nodeWidth) {
		Node.nodeWidth = nodeWidth;
	}

	public static int getNodeHeight() {
		return nodeHeight;
	}

	public static void setNodeHeight(int nodeHeight) {
		Node.nodeHeight = nodeHeight;
	}

	public static int getNodeTextLength() {
		return nodeTextLength;
	}

	public static void setNodeTextLength(int nodeTextLength) {
		Node.nodeTextLength = nodeTextLength;
	}

	public Point getNearestBound(double x, double y, int unitSize) {
		double a = nodeWidth * unitSize;
		double b = nodeHeight * unitSize;
		double midX = getMinX() + a / 2;
		double midY = getMinY() + b / 2;
		double angle = Math.atan2(-(y - midY), x - midX);
		double radius = 0.5
				* a
				* b
				/ Math.sqrt(Math.pow(a * Math.sin(angle), 2)
						+ Math.pow(b * Math.cos(angle), 2));
		double xResult = midX + radius * Math.cos(angle);
		double yResult = midY - radius * Math.sin(angle);
		Point p = new Point();
		p.setLocation((int) xResult, (int) yResult);
		return p;
	}

	public void move(double x, double y, int unitSize) {
		setMinX((int) (getMinX() + x));
		setMinY((int) (getMinY() + y));

		for (Edge edge : incomingEdges) {
			Point newEnd = getNearestBound(edge.getControlPoint().getX(), edge
					.getControlPoint().getY(), unitSize);
			edge.setEndPoint(newEnd);
		}

		for (Edge edge : outgoingEdges) {
			Point newStart = getNearestBound(edge.getControlPoint().getX(),
					edge.getControlPoint().getY(), unitSize);
			edge.setStartPoint(newStart);
		}

	}

	public Properties getProperties() {
		return this.properties;
	}

}
