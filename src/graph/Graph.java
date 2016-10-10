package graph;

import graph.node.Node;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Graph {

	public enum EditType {
		DOUBLE, INTEGER, COLOR, STRING;
	}

	public enum GraphEnum {

		GRID_LINE_WIDTH("Grid Line Width", 1, EditType.INTEGER), GRID_LINE_COLOR(
				"Grid Line Color", Color.LIGHT_GRAY, EditType.COLOR), ZOOM_X(
				"Zoom X", 0.0, EditType.DOUBLE), ZOOM_Y("Zoom Y", 0.0,
				EditType.DOUBLE), ZOOM_SCALE("Zoom Scale", 1.0, EditType.DOUBLE);
		private String name;
		private Object value;
		private EditType type;

		private GraphEnum(String name, Object value, EditType type) {
			this.name = name;
			this.value = value;
			this.type = type;
		}

		private void setValue(Object value) {

			this.value = value;
		}

		public Object getValue() {
			return this.value;
		}

		public String getName() {
			return this.name;
		}

		public EditType getType() {
			return this.type;
		}
	}

	private int dimPixels;
	public static final int BASE_UNIT_SIZE = 32;
	private List<Node> nodes;

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
		return (Double) GraphEnum.ZOOM_X.getValue();
	}

	public double getZoomPointY() {
		return (Double) GraphEnum.ZOOM_Y.getValue();
	}

	public double getZoomScale() {
		return (Double) GraphEnum.ZOOM_SCALE.getValue();
	}

	public void setZoom(double x, double y, double scale) {
		GraphEnum.ZOOM_X.setValue(x);
		GraphEnum.ZOOM_Y.setValue(y);
		GraphEnum.ZOOM_SCALE.setValue(scale);
	}

	public int getGridLineWidth() {
		return (Integer) GraphEnum.GRID_LINE_WIDTH.getValue();
	}

	public void setGridLineWidth(int gridLinesWidth) {
		GraphEnum.GRID_LINE_WIDTH.setValue(gridLinesWidth);
	}

	public Color getGridLinesColor() {
		return (Color) GraphEnum.GRID_LINE_COLOR.getValue();
	}

	public void setGridLinesColor(Color gridLinesColor) {
		GraphEnum.GRID_LINE_COLOR.setValue(gridLinesColor);
	}

	public int getDimPixels() {
		return dimPixels;
	}

	public int getUnitSize() {
		return BASE_UNIT_SIZE;
	}

}
