import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
import java.awt.geom.QuadCurve2D;

public class ActionEdge {
	private DialogueState startState, finishState;
	private Point pointStart, pointEnd;

	private static final Color SELECTION_COLOR = Color.green;

	public static final double ARROW_ANGLE_RAD = Math.PI / 8,
			ARROW_WIDTH = 0.1;

	private static final int COLOR = 1, SELECTION_RADIUS = 1;
	public static final int ARROW_HEAD_SIZE = 1;

	private Ellipse2D selection;
	private PropertyMap properties;

	public ActionEdge(DialogueState start) {
		this.properties = new PropertyMap();
		this.startState = start;
		this.pointStart = new Point();
		this.pointEnd = new Point();
		Point temp = start.getNearestBound(start.getCenterX(),
				start.getCenterY());
		this.pointStart.setLocation(temp.getX(), temp.getY());
		this.pointEnd.setLocation(temp.getX(), temp.getY());
		this.selection = new Ellipse2D.Double();
		double midX = (this.pointStart.getX() + this.pointEnd.getX()) / 2, midY = (this.pointStart
				.getY() + this.pointEnd.getY()) / 2;
		this.setSelection(midX, midY);
		this.properties.put(COLOR, Color.BLACK);
	}

	public double getAngle() {
		return Math.atan2(-(this.pointEnd.getY() - this.pointStart.getY()),
				this.pointEnd.getX() - this.pointStart.getX());
	}

	public Point getPointStart() {
		return pointStart;
	}

	public void setPointStart(Point pointStart) {
		this.pointStart = pointStart;
	}

	public Point getPointEnd() {
		return pointEnd;
	}

	public void setPointEnd(Point pointEnd) {
		this.pointEnd = pointEnd;
	}

	public DialogueState getStartState() {
		return this.startState;
	}

	public void setFinishState(DialogueState finish) {
		this.finishState = finish;
	}

	public DialogueState getFinishState() {
		return finishState;
	}

	public Ellipse2D getSelection() {
		return this.selection;
	}

	public void setSelection(double midX, double midY) {
		this.selection.setFrameFromCenter(midX, midY, midX + SELECTION_RADIUS,
				midY + SELECTION_RADIUS);
		this.setPointStart(this.startState.getNearestBound(midX, midY));
		if (this.finishState != null) {
			this.setPointEnd(this.finishState.getNearestBound(midX, midY));
		}
	}

	public void setSelection(double x1, double x2, double y1, double y2) {
		double midX = (x1 + x2) / 2, midY = (y1 + y2) / 2;

		this.selection.setFrameFromCenter(midX, midY, midX + SELECTION_RADIUS,
				midY + SELECTION_RADIUS);
		this.setPointStart(this.startState.getNearestBound(midX, midY));
		if (this.finishState != null) {
			this.setPointEnd(this.finishState.getNearestBound(midX, midY));
		} else {
			Point temp = new Point();
			temp.setLocation(x2, y2);
			this.setPointEnd(temp);
		}
	}

	public void draw(Graphics g, int unitSize) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor((Color) this.properties.get(COLOR));
		g2d.setStroke(new BasicStroke((int) ARROW_WIDTH * unitSize));
		Point start = this.pointStart;
		Point end = this.pointEnd;
		DialogueState begin = this.startState;
		DialogueState finish = this.finishState;
		if (finish != null) {
			QuadCurve2D q = new QuadCurve2D.Float();
			Point newStart = begin.getNearestBound(this.selection.getCenterX(),
					this.selection.getCenterY());
			Point newEnd = finish.getNearestBound(this.selection.getCenterX(),
					this.selection.getCenterY());
			double midX = (newStart.getX() + newEnd.getX()) / 2, midY = (newStart
					.getY() + newEnd.getY()) / 2;

			q.setCurve((float) newStart.getX(), (float) newStart.getY(),
					(float) (2 * this.selection.getCenterX() - midX),
					(float) (2 * this.selection.getCenterY() - midY),
					(float) newEnd.getX(), (float) newEnd.getY());
			g2d.draw(q);
			this.drawCurvedArrowHead(g2d, unitSize,
					this.selection.getCenterX(), this.selection.getCenterY());
			g2d.setColor(SELECTION_COLOR);
			this.selection.setFrameFromCenter(this.selection.getCenterX(),
					this.selection.getCenterY(), this.selection.getCenterX()
							+ unitSize * SELECTION_RADIUS,
					this.selection.getCenterY() + unitSize * SELECTION_RADIUS);
			g2d.fill(this.selection);
			return;

		}
		g2d.drawLine((int) start.getX(), (int) start.getY(), (int) end.getX(),
				(int) end.getY());
		this.drawArrowHead(g2d, unitSize);
		g2d.setColor(SELECTION_COLOR);
		this.selection.setFrameFromCenter(this.selection.getCenterX(),
				this.selection.getCenterY(), this.selection.getCenterX()
						+ unitSize * SELECTION_RADIUS,
				this.selection.getCenterY() + unitSize * SELECTION_RADIUS);
		g2d.fill(this.selection);

	}

	public void drawArrowHead(Graphics2D g2d, int unitSize) {
		Point newPoint = this.pointEnd;
		double arrowLeftX = newPoint.getX()
				+ ARROW_HEAD_SIZE
				* unitSize
				* Math.cos((this.getAngle() + Math.PI - ARROW_ANGLE_RAD)
						% (2 * Math.PI));
		double arrowRightX = newPoint.getX()
				+ ARROW_HEAD_SIZE
				* unitSize
				* Math.cos((this.getAngle() + Math.PI + ARROW_ANGLE_RAD)
						% (2 * Math.PI));
		double arrowLeftY = newPoint.getY()
				- ARROW_HEAD_SIZE
				* unitSize
				* Math.sin((this.getAngle() + Math.PI - ARROW_ANGLE_RAD)
						% (2 * Math.PI));
		double arrowRightY = newPoint.getY()
				- ARROW_HEAD_SIZE
				* unitSize
				* Math.sin((this.getAngle() + Math.PI + ARROW_ANGLE_RAD)
						% (2 * Math.PI));
		int[] x = new int[] { (int) (newPoint.getX()), (int) arrowLeftX,
				(int) arrowRightX };
		int[] y = new int[] { (int) (newPoint.getY()), (int) arrowLeftY,
				(int) arrowRightY };
		int n = 3;
		g2d.fillPolygon(new Polygon(x, y, n));
	}

	public void drawCurvedArrowHead(Graphics2D g2d, int unitSize,
			double controlX, double controlY) {
		Point newPoint = this.finishState.getNearestBound(controlX, controlY);

		double arrowLeftX = newPoint.getX()
				+ ARROW_HEAD_SIZE
				* unitSize
				* Math.cos((Math.atan2(newPoint.getY() - controlY,
						newPoint.getX() - controlX)
						+ Math.PI + ARROW_ANGLE_RAD)
						% (2 * Math.PI));
		double arrowRightX = newPoint.getX()
				+ ARROW_HEAD_SIZE
				* unitSize
				* Math.cos((Math.atan2(newPoint.getY() - controlY,
						newPoint.getX() - controlX)
						+ Math.PI - ARROW_ANGLE_RAD)
						% (2 * Math.PI));
		double arrowLeftY = newPoint.getY()
				+ ARROW_HEAD_SIZE
				* unitSize
				* Math.sin((Math.atan2(newPoint.getY() - controlY,
						newPoint.getX() - controlX)
						+ Math.PI + ARROW_ANGLE_RAD)
						% (2 * Math.PI));
		double arrowRightY = newPoint.getY()
				+ ARROW_HEAD_SIZE
				* unitSize
				* Math.sin((Math.atan2(newPoint.getY() - controlY,
						newPoint.getX() - controlX)
						+ Math.PI - ARROW_ANGLE_RAD)
						% (2 * Math.PI));
		int[] x = new int[] { (int) (newPoint.getX()), (int) arrowLeftX,
				(int) arrowRightX };
		int[] y = new int[] { (int) (newPoint.getY()), (int) arrowLeftY,
				(int) arrowRightY };
		int n = 3;
		g2d.fillPolygon(new Polygon(x, y, n));
	}

}
