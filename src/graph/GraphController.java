package graph;

import graph.Graph.GraphEnum;
import graph.edge.Edge;
import graph.graphInterface.InterfacePanel;
import graph.graphInterface.PropertyCellModel;
import graph.node.Node;
import graph.node.Node.NodeEnum;
import graph.node.NodeController;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GraphController extends NodeController implements
		InterfacePanel.ControlCallback, MouseMotionListener, MouseListener,
		MouseWheelListener {

	private enum DrawState {
		DEFAULT_STATE, CREATING_EDGE, ACTION_RADIUS_CHANGE, TRANSLATE, NODE_EDIT;
	}

	private Graph graph;
	private Node selectedNode;
	private Edge newEdge, selectedEdge;
	private double mouseX = 0, mouseY = 0;
	private double pDragX = 0, pDragY = 0;
	private double dx = 0, dy = 0;
	private DrawState drawState = DrawState.DEFAULT_STATE;
	private PropertyCellModel model;

	private int scrollAmount = 0;
	public static final int SCROLL_THRESHOLD = 5;

	public GraphController(Graph graph) {
		this.graph = graph;
		model = new PropertyCellModel(graph);
	}

	private void drawGrid(Graphics2D g) {
		g.setColor(graph.getGridLinesColor());
		g.setStroke(new BasicStroke(graph.getGridLineWidth()));
		int adjustedDimension = (int) (graph.getDimPixels() / (graph
				.getZoomScale()));
		for (int k = -adjustedDimension / graph.getUnitSize(); k < 2
				* adjustedDimension / graph.getUnitSize(); k++) {
			g.drawLine(k * graph.getUnitSize(), -adjustedDimension,
					k * graph.getUnitSize(), 2 * adjustedDimension);
		}

		for (int i = -adjustedDimension / graph.getUnitSize(); i < 2
				* adjustedDimension / graph.getUnitSize(); i++) {
			g.drawLine(-adjustedDimension, i * graph.getUnitSize(),
					2 * adjustedDimension, i * graph.getUnitSize());
		}
	}

	void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.clearRect(-graph.getDimPixels(), -graph.getDimPixels(),
				2 * graph.getDimPixels(), 2 * graph.getDimPixels());

		g2.translate(graph.getZoomPointX() + dx, graph.getZoomPointY() + dy);
		g2.scale(graph.getZoomScale(), graph.getZoomScale());
		g2.translate(-graph.getZoomPointX(), -graph.getZoomPointY());
		drawGrid(g2);

		for (Node node : graph.getNodes()) {
			drawNode(node, g2, graph.getUnitSize());
		}

	}

	private void emptySelected() {
		if (selectedNode != null) {
			selectedNode = null;
		}

		if (selectedEdge != null) {
			selectedEdge = null;
		}

		model.setSelected(graph);
	}

	private void zoomOut(double x, double y) {
		if (graph.getZoomScale() > Math.pow(0.5, 3)) {
			graph.setZoom(x, y, 0.5 * graph.getZoomScale());
			this.model.setValueAt(x, GraphEnum.ZOOM_X);
			this.model.setValueAt(y, GraphEnum.ZOOM_Y);
			this.model.setValueAt(graph.getZoomScale(), GraphEnum.ZOOM_SCALE);
		}
	}

	private void zoomIn(double x, double y) {
		if (graph.getZoomScale() < Math.pow(2, 2)) {
			graph.setZoom(x, y, 2 * graph.getZoomScale());
			this.model.setValueAt(x, GraphEnum.ZOOM_X);
			this.model.setValueAt(y, GraphEnum.ZOOM_Y);
			this.model.setValueAt(graph.getZoomScale(), GraphEnum.ZOOM_SCALE);
		}
	}

	// #### From InterfacePanel.ControlCallback #####
	public void newNodeClicked() {
		JFrame frame = new JFrame("Name Your New State");
		String name = (String) JOptionPane.showInputDialog(frame,
				"What would you like to name the new state?", "New State",
				JOptionPane.QUESTION_MESSAGE);
		if (name != null) {
			Node temp = new Node(name, graph.getDimPixels() / 2,
					graph.getDimPixels() / 2);
			graph.addNode(temp);
			model.setSelected(temp);
			this.drawState = DrawState.DEFAULT_STATE;
		}
	}

	public void newEdgeClicked() {
		emptySelected();
		this.drawState = DrawState.CREATING_EDGE;
	}

	/*
	 * public void propertyChanged(GraphEnum e, String value) { switch (e) {
	 * case GRID_LINE_WIDTH: graph.setGridLineWidth(Integer.parseInt(value));
	 * break; case GRID_LINE_COLOR:
	 * graph.setGridLinesColor(Color.decode(value)); break; case ZOOM_X:
	 * graph.setZoom(Double.parseDouble(value), graph.getZoomPointY(),
	 * graph.getZoomScale()); break; case ZOOM_Y:
	 * graph.setZoom(graph.getZoomPointX(), Double.parseDouble(value),
	 * graph.getZoomScale()); break; case ZOOM_SCALE:
	 * graph.setZoom(graph.getZoomPointX(), graph.getZoomPointY(),
	 * Double.parseDouble(value)); break; } }
	 */

	// #############################################

	// #### Mouse Input Listeners ##################

	public void mouseDragged(MouseEvent e) {
		double dragX = (((double) e.getX() - graph.getZoomPointX())
				/ graph.getZoomScale() + graph.getZoomPointX());
		double dragY = (((double) e.getY() - graph.getZoomPointY())
				/ graph.getZoomScale() + graph.getZoomPointY());
		double pX = dragX - dx;
		double pY = dragY - dy;
		if (selectedEdge != null && drawState == DrawState.ACTION_RADIUS_CHANGE) {
			Point control = new Point();
			control.setLocation(2 * pX - selectedEdge.getMidPoint().getX(), 2
					* pY - selectedEdge.getMidPoint().getY());
			selectedEdge.setControlPoint(control);

			Point start = selectedEdge.getStartNode().getNearestBound(
					selectedEdge.getControlPoint().getX(),
					selectedEdge.getControlPoint().getY(), graph.getUnitSize());
			selectedEdge.setStartPoint(start);
			Point end = selectedEdge.getEndNode().getNearestBound(
					selectedEdge.getControlPoint().getX(),
					selectedEdge.getControlPoint().getY(), graph.getUnitSize());
			selectedEdge.setEndPoint(end);

		} else if (selectedNode != null && drawState == DrawState.NODE_EDIT) {
			selectedNode.move(pX - mouseX, pY - mouseY, graph.getUnitSize());
			model.setValueAt(selectedNode.getMinX(), NodeEnum.X_POS);
			model.setValueAt(selectedNode.getMinY(), NodeEnum.Y_POS);

		} else if (drawState == DrawState.TRANSLATE) {
			dx += (dragX - pDragX) * graph.getZoomScale();
			dy += (dragY - pDragY) * graph.getZoomScale();
		}

		mouseX = pX;
		mouseY = pY;
		pDragX = dragX;
		pDragY = dragY;
	}

	public void mouseMoved(MouseEvent e) {
		mouseX = ((e.getX() - graph.getZoomPointX()) / graph.getZoomScale()
				+ graph.getZoomPointX() - dx);
		mouseY = ((e.getY() - graph.getZoomPointY()) / graph.getZoomScale()
				+ graph.getZoomPointY() - dy);
		if (drawState == DrawState.CREATING_EDGE && newEdge != null) {
			Point control = new Point();
			control.setLocation(newEdge.getMidPoint().getX(), newEdge
					.getMidPoint().getY());
			newEdge.setControlPoint(control);
			Point start = selectedNode.getNearestBound(newEdge
					.getControlPoint().getX(),
					newEdge.getControlPoint().getY(), graph.getUnitSize());
			newEdge.setStartPoint(start);
			Point end = new Point();
			end.setLocation(mouseX, mouseY);
			newEdge.setEndPoint(end);
			model.setSelected(newEdge);

		}
	}

	public void mouseClicked(MouseEvent e) {
		/*
		 * mouseX = e.getX(); mouseY = e.getY(); for (Node node :
		 * graph.getNodes()) { Ellipse2D ellipse = new
		 * Ellipse2D.Double(node.getMinX(), node.getMinY(), Node.getNodeWidth()
		 * * graph.getUnitSize(), Node.getNodeHeight() * graph.getUnitSize());
		 * if (ellipse.contains(e.getX(), e.getY())) {
		 * 
		 * } }
		 */

	}

	public void mousePressed(MouseEvent e) {
		pDragX = ((e.getX() - graph.getZoomPointX()) / graph.getZoomScale() + graph
				.getZoomPointX());
		pDragY = ((e.getY() - graph.getZoomPointY()) / graph.getZoomScale() + graph
				.getZoomPointY());

		mouseX = pDragX - dx;
		mouseY = pDragY - dy;

		for (Node node : graph.getNodes()) {
			for (Edge edge : node.getOutgoingEdges()) {
				if (Point.distance(mouseX, mouseY, edge.getSelectorPoint()
						.getX(), edge.getSelectorPoint().getY()) < edge
						.getSelectionRadius() * graph.getUnitSize()
						&& drawState == DrawState.DEFAULT_STATE) {
					drawState = DrawState.ACTION_RADIUS_CHANGE;
					selectedEdge = edge;
					selectedEdge.setCurrentColor(selectedEdge
							.getSelectedColor());
					model.setSelected(selectedEdge);
					return;
				}
			}
		}
		for (Node node : graph.getNodes()) {
			Ellipse2D ellipse = new Ellipse2D.Double(node.getMinX(),
					node.getMinY(), Node.getNodeWidth() * graph.getUnitSize(),
					Node.getNodeHeight() * graph.getUnitSize());

			if (ellipse.contains(mouseX, mouseY)) {

				if (drawState == DrawState.CREATING_EDGE) {
					if (newEdge != null
							&& !node.getIncomingEdges().contains(newEdge)) {
						node.addIncomingEdge(newEdge);
						newEdge.setEndPoint(node.getNearestBound(newEdge
								.getControlPoint().getX(), newEdge
								.getControlPoint().getY(), graph.getUnitSize()));
						newEdge.setCurrentColor(newEdge.getUnselectedColor());
						newEdge = null;
						emptySelected();
						drawState = DrawState.DEFAULT_STATE;

					} else {
						emptySelected();
						selectedNode = node;
						Point tempStart = new Point();
						tempStart.setLocation(ellipse.getCenterX(),
								ellipse.getCenterY());
						newEdge = new Edge(tempStart);
						newEdge.setCurrentColor(newEdge.getSelectedColor());
						model.setSelected(newEdge);
						selectedNode.addOutgoingEdge(newEdge);
						selectedNode.setColor(selectedNode.getSelectedColor());
					}
				} else {
					emptySelected();
					drawState = DrawState.NODE_EDIT;
					selectedNode = node;
					selectedNode.setColor(selectedNode.getSelectedColor());
					model.setSelected(selectedNode);
				}
				return;
			}
		}
		emptySelected();
		drawState = DrawState.TRANSLATE;
	}

	public void mouseReleased(MouseEvent e) {
		if (drawState != DrawState.CREATING_EDGE) {
			if (selectedNode != null) {
				drawState = DrawState.DEFAULT_STATE;
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseWheelMoved(MouseWheelEvent e) {

		mouseX = ((e.getX() - graph.getZoomPointX()) / graph.getZoomScale()
				+ graph.getZoomPointX() - dx);
		mouseY = ((e.getY() - graph.getZoomPointY()) / graph.getZoomScale()
				+ graph.getZoomPointY() - dy);
		this.scrollAmount += e.getWheelRotation();
		if (this.scrollAmount >= SCROLL_THRESHOLD) {
			zoomOut(e.getX(), e.getY());
			this.scrollAmount = 0;
		} else if (this.scrollAmount <= -SCROLL_THRESHOLD) {
			zoomIn(e.getX(), e.getY());
			this.scrollAmount = 0;

		}
	}

	public PropertyCellModel getCellModel() {
		return this.model;
	}

}
