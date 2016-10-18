package graph;

import graph.graphInterface.PropertyCellModel;

import java.awt.Color;

public class GraphInterfaceEditor extends Graph {

	private PropertyCellModel model;

	public GraphInterfaceEditor(int dimPixels) {
		super(dimPixels);
		this.model = new PropertyCellModel(this);
	}

	public void setZoom(double x, double y, double scale) {
		super.setZoom(x, y, scale);
		this.model.setValueAt(x, GraphEnum.ZOOM_X);
		this.model.setValueAt(y, GraphEnum.ZOOM_Y);
		this.model.setValueAt(scale, GraphEnum.ZOOM_SCALE);

	}

	public void setGridLineWidth(int gridLinesWidth) {
		super.setGridLineWidth(gridLinesWidth);
		this.model.setValueAt(gridLinesWidth, GraphEnum.GRID_LINE_WIDTH);

	}

	public void setGridLinesColor(Color gridLinesColor) {
		super.setGridLinesColor(gridLinesColor);
		this.model.setValueAt(gridLinesColor, GraphEnum.GRID_LINE_COLOR);

	}

	public PropertyCellModel getModel() {
		return this.model;
	}

}
