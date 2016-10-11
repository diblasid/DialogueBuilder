package graph;

import graph.Graph.GraphEnum;
import graph.edge.Edge;
import graph.edge.EdgeController;
import graph.graphInterface.InterfacePanel;
import graph.node.Node;
import graph.node.NodeController;

import java.awt.BasicStroke;
import java.awt.Color;
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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class GraphController extends NodeController implements
		InterfacePanel.ControlCallback, MouseMotionListener, MouseListener,
		MouseWheelListener, TableModelListener {

	public static Color selectedNodeColor = new Color(100, 100, 200),
			unselectedNodeColor = new Color(150, 150, 240),
			stateTextColor = Color.WHITE;
	private Graph graph;
	private Node selectedNode;
	private Edge newEdge, selectedEdge;
	private double mouseX = 0, mouseY = 0;
	private double dx = 0, dy = 0;
	public static final int DEFAULT_STATE = 0, CREATING_EDGE = 1,
			ACTION_RADIUS_CHANGE = 2;

	private int scrollAmount = 0;
	public static final int SCROLL_THRESHOLD = 5;

	private int drawState = DEFAULT_STATE;

	public GraphController(Graph graph) {
		this.graph = graph;
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

		g2.translate(graph.getZoomPointX(), graph.getZoomPointY());
		g2.scale(graph.getZoomScale(), graph.getZoomScale());
		g2.translate(-graph.getZoomPointX() + dx, -graph.getZoomPointY() + dy);
		drawGrid(g2);

		for (Node node : graph.getNodes()) {
			drawNode(node, g2, graph.getUnitSize());
		}

	}

	private void emptySelected() {
		if (selectedNode != null) {
			selectedNode.setColor(unselectedNodeColor);
			selectedNode = null;
		}

		if (selectedEdge != null) {
			selectedEdge = null;
		}

	}

	private void zoomOut(double x, double y) {
		if (graph.getZoomScale() > Math.pow(0.5, 3)) {
			dx += -(x - graph.getZoomPointX()) / graph.getZoomScale();
			dy += -(y - graph.getZoomPointY()) / graph.getZoomScale();
			graph.setZoom(x, y, 0.5 * graph.getZoomScale());

		}
	}

	private void zoomIn(double x, double y) {
		if (graph.getZoomScale() < Math.pow(2, 2)) {
			dx += -(x - graph.getZoomPointX()) / graph.getZoomScale();
			dy += -(y - graph.getZoomPointY()) / graph.getZoomScale();
			graph.setZoom(x, y, 2 * graph.getZoomScale());

		}
	}

	// #### From InterfacePanel.ControlCallback #####
	public void newNodeClicked() {
		JFrame frame = new JFrame("Name Your New State");
		String name = (String) JOptionPane.showInputDialog(frame,
				"What would you like to name the new state?", "New State",
				JOptionPane.QUESTION_MESSAGE);
		if (name != null) {
			graph.addNode(new Node(name, unselectedNodeColor, graph
					.getDimPixels() / 2, graph.getDimPixels() / 2));
			this.drawState = DEFAULT_STATE;
		}
	}

	public void newEdgeClicked() {
		emptySelected();
		this.drawState = CREATING_EDGE;
	}

	public void propertyChanged(GraphEnum e, String value) {
		switch (e) {
		case GRID_LINE_WIDTH:
			graph.setGridLineWidth(Integer.parseInt(value));
			break;
		case GRID_LINE_COLOR:
			graph.setGridLinesColor(Color.decode(value));
			break;
		case ZOOM_X:
			graph.setZoom(Double.parseDouble(value), graph.getZoomPointY(),
					graph.getZoomScale());
			break;
		case ZOOM_Y:
			graph.setZoom(graph.getZoomPointX(), Double.parseDouble(value),
					graph.getZoomScale());
			break;
		case ZOOM_SCALE:
			graph.setZoom(graph.getZoomPointX(), graph.getZoomPointY(),
					Double.parseDouble(value));
			break;
		}
	}

	// #############################################

	// #### Mouse Input Listeners ##################

	public void mouseDragged(MouseEvent e) {
		double dragX = ((e.getX() - graph.getZoomPointX())
				/ graph.getZoomScale() + graph.getZoomPointX() - dx);
		double dragY = ((e.getY() - graph.getZoomPointY())
				/ graph.getZoomScale() + graph.getZoomPointY() - dy);
		if (selectedEdge != null && drawState == ACTION_RADIUS_CHANGE) {
			Point control = new Point();
			control.setLocation(2 * dragX - selectedEdge.getMidPoint().getX(),
					2 * dragY - selectedEdge.getMidPoint().getY());
			selectedEdge.setControlPoint(control);

			Point start = selectedEdge.getStartNode().getNearestBound(
					selectedEdge.getControlPoint().getX(),
					selectedEdge.getControlPoint().getY(), graph.getUnitSize());
			selectedEdge.setStartPoint(start);
			Point end = selectedEdge.getEndNode().getNearestBound(
					selectedEdge.getControlPoint().getX(),
					selectedEdge.getControlPoint().getY(), graph.getUnitSize());
			selectedEdge.setEndPoint(end);

		} else if (selectedNode != null) {
			selectedNode.move(dragX - mouseX, dragY - mouseY,
					graph.getUnitSize());
		} else {
			dx += dragX - mouseX;
			dy += dragY - mouseY;
		}

		mouseX = dragX;
		mouseY = dragY;
	}

	public void mouseMoved(MouseEvent e) {
		mouseX = ((e.getX() - graph.getZoomPointX()) / graph.getZoomScale()
				+ graph.getZoomPointX() - dx);
		mouseY = ((e.getY() - graph.getZoomPointY()) / graph.getZoomScale()
				+ graph.getZoomPointY() - dy);
		if (drawState == CREATING_EDGE && newEdge != null) {
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
		mouseX = ((e.getX() - graph.getZoomPointX()) / graph.getZoomScale()
				+ graph.getZoomPointX() - dx);
		mouseY = ((e.getY() - graph.getZoomPointY()) / graph.getZoomScale()
				+ graph.getZoomPointY() - dy);

		for (Node node : graph.getNodes()) {
			for (Edge edge : node.getOutgoingEdges()) {
				if (Point.distance(mouseX, mouseY, edge.getSelectorPoint()
						.getX(), edge.getSelectorPoint().getY()) < EdgeController.SELECTION_RADIUS
						* graph.getUnitSize()
						&& drawState == DEFAULT_STATE) {
					drawState = ACTION_RADIUS_CHANGE;
					selectedEdge = edge;
					return;
				}
			}
		}
		for (Node node : graph.getNodes()) {
			Ellipse2D ellipse = new Ellipse2D.Double(node.getMinX(),
					node.getMinY(), Node.getNodeWidth() * graph.getUnitSize(),
					Node.getNodeHeight() * graph.getUnitSize());

			if (ellipse.contains(mouseX, mouseY)) {

				if (drawState == CREATING_EDGE) {
					if (newEdge != null
							&& !node.getIncomingEdges().contains(newEdge)) {
						node.addIncomingEdge(newEdge);
						newEdge.setEndPoint(node.getNearestBound(newEdge
								.getControlPoint().getX(), newEdge
								.getControlPoint().getY(), graph.getUnitSize()));
						newEdge = null;
						emptySelected();
						drawState = DEFAULT_STATE;

					} else {
						emptySelected();
						selectedNode = node;
						Point tempStart = new Point();
						tempStart.setLocation(ellipse.getCenterX(),
								ellipse.getCenterY());
						newEdge = new Edge(tempStart);
						selectedNode.addOutgoingEdge(newEdge);
						selectedNode.setColor(selectedNodeColor);
					}
				} else {
					emptySelected();
					selectedNode = node;
					selectedNode.setColor(selectedNodeColor);
				}
				return;
			}
		}
		emptySelected();
	}

	public void mouseReleased(MouseEvent e) {
		if (drawState != CREATING_EDGE || selectedNode != null) {
			emptySelected();
			drawState = DEFAULT_STATE;
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

	public void tableChanged(TableModelEvent e) {
		/*
		 * int row = e.getFirstRow(); int column = e.getColumn(); TableModel
		 * model = (TableModel) e.getSource(); String columnName =
		 * model.getColumnName(column); String data = (String)
		 * model.getValueAt(row, column);
		 */
	}

}
