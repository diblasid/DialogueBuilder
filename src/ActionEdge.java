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

	public static final double ARROW_ANGLE_RAD = Math.PI / 8, ANGLE_OFFSET = 0,
			ARROW_WIDTH = 0.1;

	private static final int COLOR = 1, RADIUS = 2, SELECTION_RADIUS = 20;
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
		this.selection.setFrameFromCenter(temp.getX(), temp.getY(), temp.getX() + SELECTION_RADIUS, temp.getY() + SELECTION_RADIUS);
		this.properties.put(COLOR, Color.BLACK);
		this.properties.put(RADIUS, 0f);

	}

	public void refreshSelector() {
		int tempX = (int) ((this.pointStart.getX() + this.pointEnd.getX()) / 2);
		int tempY = (int) ((this.pointEnd.getY() + this.pointStart.getY()) / 2);
		
		double normal = this.getAngle() + Math.PI / 2;
		
		double controlX = tempX + this.properties.getFloatValue(RADIUS)
		* Math.cos(normal);
		double controlY = tempY - this.properties.getFloatValue(RADIUS)
		* Math.sin(normal);
		this.selection.setFrameFromCenter(controlX, controlY, controlX + SELECTION_RADIUS, controlY + SELECTION_RADIUS);

	}

	public void setRadius(float radius) {
		this.properties.replace(RADIUS, radius);
		this.refreshSelector();
		
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
		this.refreshSelector();
	}

	public Point getPointEnd() {
		return pointEnd;
	}

	public void setPointEnd(Point pointEnd) {
		this.pointEnd = pointEnd;
		this.refreshSelector();
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
			Point newStart = begin.getPoint(this.getAngle() + ANGLE_OFFSET);
			Point newEnd = finish.getPoint(this.getAngle() + Math.PI
					- ANGLE_OFFSET);
			double normal = this.getAngle() + Math.PI / 2;
			float newX = (float) (start.getX() + end.getX()) / 2;
			float newY = (float) (start.getY() + end.getY()) / 2;
			double controlX = newX + 2*this.properties.getFloatValue(RADIUS)
					* Math.cos(normal);
			double controlY = newY - 2*this.properties.getFloatValue(RADIUS)
					* Math.sin(normal);
			q.setCurve((float) newStart.getX(), (float) newStart.getY(),
					(float) (controlX), (float) (controlY),
					(float) newEnd.getX(), (float) newEnd.getY());
			g2d.draw(q);
			this.drawCurvedArrowHead(g2d, unitSize, ANGLE_OFFSET, controlX,
					controlY);
			g2d.setColor(SELECTION_COLOR);
			g2d.fill(this.selection);
			return;

		}
		g2d.drawLine((int) start.getX(), (int) start.getY(), (int) end.getX(),
				(int) end.getY());
		this.drawArrowHead(g2d, unitSize);
		g2d.setColor(SELECTION_COLOR);
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
			double angleOffset, double controlX, double controlY) {
		Point newPoint = this.finishState.getPoint(this.getAngle() + Math.PI
				- angleOffset);

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
