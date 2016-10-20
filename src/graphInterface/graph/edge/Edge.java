package graphInterface.graph.edge;

import graphInterface.graph.node.Node;
import graphInterface.property.EditType;
import graphInterface.property.PropertyKey;
import graphInterface.property.Selectable;

import java.awt.Color;
import java.awt.Point;
import java.util.Properties;

public class Edge implements Selectable {

	private Point startPoint, endPoint;
	private Node startNode, endNode;
	private Properties properties;
	private Color currentColor;

	private enum EdgeEnum implements PropertyKey {

		EDGE_SELECTED_COLOR("Selected Color", EditType.COLOR), EDGE_COLOR(
				"Color", EditType.COLOR), LINE_COLOR("Line Color",
				EditType.COLOR), SELECTION_RADIUS("Edge Selector Radius",
				EditType.INTEGER), CONTROL_X("Control Point X", EditType.DOUBLE), CONTROL_Y(
				"Control Point Y", EditType.DOUBLE), EDGE_WIDTH("Edge Width",
				EditType.DOUBLE), ARROW_HEAD_SIZE("Arrow Head Size",
				EditType.INTEGER), ARROW_HEAD_ANGLE("Arrow Head Angle",
				EditType.DOUBLE);

		private String propertyName;
		private EditType type;

		private EdgeEnum(String propertyName, EditType type) {
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

	public Edge(Point start) {
		this.startPoint = start;
		this.endPoint = start;
		currentColor = Color.blue;
		properties = new Properties();
		properties.put(EdgeEnum.EDGE_COLOR, Color.BLUE);
		properties.put(EdgeEnum.EDGE_SELECTED_COLOR, Color.GREEN);
		properties.put(EdgeEnum.LINE_COLOR, Color.BLACK);
		properties.put(EdgeEnum.SELECTION_RADIUS, 1);
		properties.put(EdgeEnum.CONTROL_X, start.getX());
		properties.put(EdgeEnum.CONTROL_Y, start.getY());
		properties.put(EdgeEnum.EDGE_WIDTH, 3.0);
		properties.put(EdgeEnum.ARROW_HEAD_SIZE, 1);
		properties.put(EdgeEnum.ARROW_HEAD_ANGLE, Math.PI / 8);

	}

	public double getEdgeWidth() {
		return (Double) properties.get(EdgeEnum.EDGE_WIDTH);
	}

	public void setEdgeWidth(double width) {
		properties.replace(EdgeEnum.EDGE_WIDTH, width);
	}

	public Color getCurrentColor() {
		return currentColor;
	}

	public void setCurrentColor(Color currentColor) {
		this.currentColor = currentColor;
	}

	public Node getStartNode() {
		return startNode;
	}

	public void setStartNode(Node startNode) {
		this.startNode = startNode;
	}

	public Node getEndNode() {
		return endNode;
	}

	public void setEndNode(Node endNode) {
		this.endNode = endNode;
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public Color getUnselectedColor() {
		return (Color) properties.get(EdgeEnum.EDGE_COLOR);
	}

	public Color getSelectedColor() {
		return (Color) properties.get(EdgeEnum.EDGE_SELECTED_COLOR);
	}

	public Color getLineColor() {
		return (Color) properties.get(EdgeEnum.LINE_COLOR);
	}

	public void setUnselectedColor(Color color) {
		properties.replace(EdgeEnum.EDGE_COLOR, color);
	}

	public void setSelectedColor(Color color) {
		properties.replace(EdgeEnum.EDGE_SELECTED_COLOR, color);
	}

	public void setLineColor(Color color) {
		properties.replace(EdgeEnum.LINE_COLOR, color);
	}

	public int getSelectionRadius() {
		return (Integer) properties.get(EdgeEnum.SELECTION_RADIUS);
	}

	public void setSelectionRadius(int radius) {
		properties.replace(EdgeEnum.SELECTION_RADIUS, radius);
	}

	public int getArrowHeadSize() {
		return (Integer) properties.get(EdgeEnum.ARROW_HEAD_SIZE);
	}

	public void setArrowHeadSize(int size) {
		properties.replace(EdgeEnum.ARROW_HEAD_SIZE, size);
	}

	public double getArrowHeadAngle() {
		return (Double) properties.get(EdgeEnum.ARROW_HEAD_ANGLE);
	}

	public void setArrowHeadAngle(double angle) {
		properties.replace(EdgeEnum.ARROW_HEAD_ANGLE, angle);
	}

	public Point getControlPoint() {
		Point temp = new Point();
		temp.setLocation((Double) properties.get(EdgeEnum.CONTROL_X),
				(Double) properties.get(EdgeEnum.CONTROL_Y));
		return temp;
	}

	public void setControlPoint(Point controlPoint) {
		properties.replace(EdgeEnum.CONTROL_X, controlPoint.getX());
		properties.replace(EdgeEnum.CONTROL_Y, controlPoint.getY());
	}

	public Point getMidPoint() {
		Point mid = new Point();
		mid.setLocation((startPoint.getX() + endPoint.getX()) / 2,
				(startPoint.getY() + endPoint.getY()) / 2);
		return mid;
	}

	public Point getSelectorPoint() {
		Point sel = new Point();
		sel.setLocation(
				((Double) properties.get(EdgeEnum.CONTROL_X) + getMidPoint()
						.getX()) / 2, ((Double) properties
						.get(EdgeEnum.CONTROL_Y) + getMidPoint().getY()) / 2);
		return sel;
	}

	public Properties getProperties() {
		return properties;
	}
}
