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
		this.model.setValueAt(x, 2, 1);
		this.model.setValueAt(y, 3, 1);
		this.model.setValueAt(scale, 4, 1);

	}

	public void setGridLineWidth(int gridLinesWidth) {
		super.setGridLineWidth(gridLinesWidth);
		this.model.setValueAt(gridLinesWidth, 0, 1);

	}

	public void setGridLinesColor(Color gridLinesColor) {
		super.setGridLinesColor(gridLinesColor);
		this.model.setValueAt(gridLinesColor, 1, 1);

	}

	public PropertyCellModel getModel() {
		return this.model;
	}

}
