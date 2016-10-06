package graph.node;

import graph.edge.Edge;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Node {

	private String name;
	private Color color, textColor;
	private int minX, minY;
	private List<Edge> incomingEdges, outgoingEdges;
	private static int nodeWidth = 10, nodeHeight = 6, nodeTextLength = 10;

	public Node(String name, Color color, int centerX, int centerY) {
		this.name = name;
		this.color = color;
		this.incomingEdges = new ArrayList<Edge>();
		this.outgoingEdges = new ArrayList<Edge>();
		this.minX = centerX;
		this.minY = centerY;
	}

	public Color getTextColor() {
		return textColor;
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	public int getMinX() {
		return minX;
	}

	public int getMinY() {
		return minY;
	}

	public void setMinX(int minX) {
		this.minX = minX;
	}

	public void setMinY(int minY) {
		this.minY = minY;
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

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getText() {
		return name;
	}

	public void setText(String text) {
		this.name = text;
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
		double midX = minX + a / 2;
		double midY = minY + b / 2;
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
		minX = (int) (minX + x);
		minY = (int) (minY + y);

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

}
