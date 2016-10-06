package graph;

import graph.node.Node;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Graph {

	private int gridLinesWidth = 1;
	private Color gridLinesColor = Color.LIGHT_GRAY;
	private int dimPixels;
	public static final int BASE_UNIT_SIZE = 32;
	private List<Node> nodes;
	private double zoomPointX = 0, zoomPointY = 0, zoomScale = 1.0;

	public Graph(int dimPixels) {

		this.dimPixels = dimPixels;
		this.nodes = new ArrayList<Node>();
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public void addNode(Node node) {
		nodes.add(node);
	}

	public double getZoomPointX() {
		return zoomPointX;
	}

	public double getZoomPointY() {
		return zoomPointY;
	}

	public double getZoomScale() {
		return zoomScale;
	}

	public void setZoom(double x, double y, double scale) {
		this.zoomPointX = x;
		this.zoomPointY = y;
		this.zoomScale = scale;
	}

	public int getGridLineWidth() {
		return gridLinesWidth;
	}

	public void setGridLineWidth(int gridLinesWidth) {
		this.gridLinesWidth = gridLinesWidth;
	}

	public Color getGridLinesColor() {
		return gridLinesColor;
	}

	public void setGridLinesColor(Color gridLinesColor) {
		this.gridLinesColor = gridLinesColor;
	}

	public int getDimPixels() {
		return dimPixels;
	}

	public int getUnitSize() {
		return BASE_UNIT_SIZE;
	}

}
