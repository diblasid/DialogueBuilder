package graphInterface.graph;

import graphInterface.InterfacePanel;
import graphInterface.PropertyCellModel;
import graphInterface.graph.Graph.GraphEnum;
import graphInterface.graph.edge.Edge;
import graphInterface.graph.node.Node;
import graphInterface.graph.node.Node.NodeEnum;
import graphInterface.graph.node.NodeController;

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
	private double windowX = 0, windowY = 0;
	private double dx = 0, dy = 0, zx = 0, zy = 0;
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
			g.drawLine(k * graph.getUnitSize() + (int) dx, -adjustedDimension
					+ (int) dy, k * graph.getUnitSize() + (int) dx, 2
					* adjustedDimension + (int) dy);
		}

		for (int i = -adjustedDimension / graph.getUnitSize(); i < 2
				* adjustedDimension / graph.getUnitSize(); i++) {
			g.drawLine(-adjustedDimension + (int) dx, i * graph.getUnitSize()
					+ (int) dy, 2 * adjustedDimension + (int) dx,
					i * graph.getUnitSize() + (int) dy);
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

		if (selectedNode != null) {
			selectedNode.setColor(selectedNode.getSelectedColor());
		}
		if (selectedEdge != null) {
			selectedEdge.setCurrentColor(selectedEdge.getSelectedColor());
		}
		for (Node node : graph.getNodes()) {
			drawNode(node, g2, graph.getUnitSize());
		}

	}

	private void emptySelected() {
		if (selectedNode != null) {
			selectedNode.setColor(selectedNode.getUnselectedColor());
			selectedNode = null;
		}

		if (selectedEdge != null) {
			selectedEdge.setCurrentColor(selectedEdge.getUnselectedColor());
			selectedEdge = null;

		}

		model.setSelected(graph);
	}

	private void zoomOut(double x, double y) {
		if (graph.getZoomScale() > Math.pow(0.5, 3)) {
			zx = (x - graph.getZoomPointX()) / (graph.getZoomScale() * 0.5);
			zy = (y - graph.getZoomPointY()) / (0.5 * graph.getZoomScale());
			graph.setZoom(x, y, 0.5 * graph.getZoomScale());
			this.model.setValueAt(x, GraphEnum.ZOOM_X);
			this.model.setValueAt(y, GraphEnum.ZOOM_Y);
			this.model.setValueAt(graph.getZoomScale(), GraphEnum.ZOOM_SCALE);
		}
	}

	private void zoomIn(double x, double y) {
		if (graph.getZoomScale() < Math.pow(2, 2)) {
			zx = (x - graph.getZoomPointX()) / (2 * graph.getZoomScale());
			zy = (y - graph.getZoomPointY()) / (2 * graph.getZoomScale());
			graph.setZoom(x, y, 2.0 * graph.getZoomScale());
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
					graph.getDimPixels() / 2, graph.getDefaultNodeColor(),
					graph.getDefaultNodeSelectedColor(), graph.getNodeWidth(),
					graph.getNodeHeight());
			graph.addNode(temp);
			this.drawState = DrawState.DEFAULT_STATE;
		}
	}

	public void newEdgeClicked() {
		emptySelected();
		this.drawState = DrawState.CREATING_EDGE;
	}

	// #############################################

	// #### Mouse Input Listeners ##################

	public void mouseDragged(MouseEvent e) {
		double pX = (((double) e.getX() - graph.getZoomPointX() - dx)
				/ graph.getZoomScale() + graph.getZoomPointX());
		double pY = (((double) e.getY() - graph.getZoomPointY() - dy)
				/ graph.getZoomScale() + graph.getZoomPointY());
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
			dx += ((double) e.getX() - windowX);
			dy += ((double) e.getY() - windowY);
		}

		mouseX = pX;
		mouseY = pY;
		windowX = e.getX();
		windowY = e.getY();
	}

	public void mouseMoved(MouseEvent e) {
		mouseX = (((double) e.getX() - graph.getZoomPointX() - dx)
				/ graph.getZoomScale() + graph.getZoomPointX());
		mouseY = (((double) e.getY() - graph.getZoomPointY() - dy)
				/ graph.getZoomScale() + graph.getZoomPointY());
		if (drawState == DrawState.CREATING_EDGE && newEdge != null
				&& selectedNode != null) {
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
		windowX = e.getX();
		windowY = e.getY();

		mouseX = ((double) e.getX() - graph.getZoomPointX() - dx)
				/ graph.getZoomScale() + graph.getZoomPointX();
		mouseY = ((double) e.getY() - graph.getZoomPointY() - dy)
				/ graph.getZoomScale() + graph.getZoomPointY();

		for (Node node : graph.getNodes()) {
			for (Edge edge : node.getOutgoingEdges()) {
				if (Point.distance(mouseX, mouseY, edge.getSelectorPoint()
						.getX(), edge.getSelectorPoint().getY()) < edge
						.getSelectionRadius() * graph.getUnitSize()
						&& drawState == DrawState.DEFAULT_STATE) {
					drawState = DrawState.ACTION_RADIUS_CHANGE;
					selectedEdge = edge;
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
					}
				} else {
					emptySelected();
					drawState = DrawState.NODE_EDIT;
					selectedNode = node;
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

		mouseX = ((double) e.getX() - graph.getZoomPointX() - dx)
				/ graph.getZoomScale() + graph.getZoomPointX();
		mouseY = ((double) e.getY() - graph.getZoomPointY() - dy)
				/ graph.getZoomScale() + graph.getZoomPointY();
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
