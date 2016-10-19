package graph.edge;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
import java.awt.geom.QuadCurve2D;

public class EdgeController {

	public static final double ARROW_ANGLE_RAD = Math.PI / 8,
			ARROW_WIDTH = 0.1;

	public static final int ARROW_HEAD_SIZE = 1;

	protected EdgeController() {
	}

	protected void drawEdge(Edge edge, Graphics2D g2d, int unitSize) {

		g2d.setColor(edge.getLineColor());
		g2d.setStroke(new BasicStroke((int) ARROW_WIDTH * unitSize));
		QuadCurve2D q = new QuadCurve2D.Float();

		q.setCurve((float) edge.getStartPoint().getX(), (float) edge
				.getStartPoint().getY(), (float) edge.getControlPoint().getX(),
				(float) edge.getControlPoint().getY(), (float) edge
						.getEndPoint().getX(), (float) edge.getEndPoint()
						.getY());
		g2d.draw(q);
		this.drawCurvedArrowHead(edge, g2d, unitSize);
		g2d.setColor(edge.getCurrentColor());

		Ellipse2D ellipse = new Ellipse2D.Double(edge.getSelectorPoint().getX()
				- unitSize * edge.getSelectionRadius() / 2, edge
				.getSelectorPoint().getY()
				- unitSize
				* edge.getSelectionRadius() / 2, edge.getSelectionRadius()
				* unitSize, edge.getSelectionRadius() * unitSize);
		g2d.fill(ellipse);

	}

	private void drawCurvedArrowHead(Edge edge, Graphics2D g2d, int unitSize) {

		double arrowLeftX = edge.getEndPoint().getX()
				+ ARROW_HEAD_SIZE
				* unitSize
				* Math.cos((Math.atan2(edge.getEndPoint().getY()
						- edge.getControlPoint().getY(), edge.getEndPoint()
						.getX() - edge.getControlPoint().getX())
						+ Math.PI + ARROW_ANGLE_RAD)
						% (2 * Math.PI));
		double arrowRightX = edge.getEndPoint().getX()
				+ ARROW_HEAD_SIZE
				* unitSize
				* Math.cos((Math.atan2(edge.getEndPoint().getY()
						- edge.getControlPoint().getY(), edge.getEndPoint()
						.getX() - edge.getControlPoint().getX())
						+ Math.PI - ARROW_ANGLE_RAD)
						% (2 * Math.PI));
		double arrowLeftY = edge.getEndPoint().getY()
				+ ARROW_HEAD_SIZE
				* unitSize
				* Math.sin((Math.atan2(edge.getEndPoint().getY()
						- edge.getControlPoint().getY(), edge.getEndPoint()
						.getX() - edge.getControlPoint().getX())
						+ Math.PI + ARROW_ANGLE_RAD)
						% (2 * Math.PI));
		double arrowRightY = edge.getEndPoint().getY()
				+ ARROW_HEAD_SIZE
				* unitSize
				* Math.sin((Math.atan2(edge.getEndPoint().getY()
						- edge.getControlPoint().getY(), edge.getEndPoint()
						.getX() - edge.getControlPoint().getX())
						+ Math.PI - ARROW_ANGLE_RAD)
						% (2 * Math.PI));
		int[] x = new int[] { (int) (edge.getEndPoint().getX()),
				(int) arrowLeftX, (int) arrowRightX };
		int[] y = new int[] { (int) (edge.getEndPoint().getY()),
				(int) arrowLeftY, (int) arrowRightY };
		int n = 3;
		g2d.fillPolygon(new Polygon(x, y, n));
	}

}
