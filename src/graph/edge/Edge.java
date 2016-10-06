package graph.edge;

import graph.node.Node;

import java.awt.Point;

public class Edge {

	public static final int SELECTION_RADIUS = 1, ARROW_HEAD_SIZE = 1;
	private Point startPoint, endPoint, controlPoint;
	private Node startNode, endNode;

	public Edge(Point start) {
		this.startPoint = start;
		this.controlPoint = start;
		this.endPoint = start;
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

	public Point getControlPoint() {
		return controlPoint;
	}

	public void setControlPoint(Point controlPoint) {
		this.controlPoint = controlPoint;
	}

	public Point getMidPoint() {
		Point mid = new Point();
		mid.setLocation((startPoint.getX() + endPoint.getX()) / 2,
				(startPoint.getY() + endPoint.getY()) / 2);
		return mid;
	}

	public Point getSelectorPoint() {
		Point sel = new Point();
		sel.setLocation((controlPoint.getX() + getMidPoint().getX()) / 2,
				(controlPoint.getY() + getMidPoint().getY()) / 2);
		return sel;
	}
}
