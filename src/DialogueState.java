import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

public class DialogueState {

	public static int ovalWidth = 10, ovalHeight = 6, stateTextLength = 10;

	public static Color selectedStateColor = new Color(100, 100, 200),
			unSelectedStateColor = new Color(150, 150, 240),
			stateTextColor = Color.WHITE;

	private String name;
	private Ellipse2D ellipse;
	private Color color, textColor;
	private List<ActionEdge> incomingActions, outgoingActions;

	public DialogueState(Ellipse2D ellipse, String name, Color textColor) {
		this.ellipse = ellipse;
		this.name = name;
		this.color = unSelectedStateColor;
		this.textColor = textColor;
		this.incomingActions = new ArrayList<ActionEdge>();
		this.outgoingActions = new ArrayList<ActionEdge>();

	}

	public int getCenterX() {
		return (int) this.ellipse.getCenterX();
	}

	public int getCenterY() {
		return (int) this.ellipse.getCenterY();
	}

	public Point getNearestBound(double x, double y) {
		double a = this.ellipse.getWidth();
		double b = this.ellipse.getHeight();
		double angle = Math.atan2(-(y - this.ellipse.getCenterY()), x
				- this.ellipse.getCenterX());
		double radius = 0.5
				* a
				* b
				/ Math.sqrt(Math.pow(a * Math.sin(angle), 2)
						+ Math.pow(b * Math.cos(angle), 2));
		double xResult = this.ellipse.getCenterX() + radius * Math.cos(angle);
		double yResult = this.ellipse.getCenterY() - radius * Math.sin(angle);
		Point p = new Point();
		p.setLocation((int) xResult, (int) yResult);
		return p;
	}

	public Point getPoint(double angle) {
		double a = this.ellipse.getWidth();
		double b = this.ellipse.getHeight();

		double radius = 0.5
				* a
				* b
				/ Math.sqrt(Math.pow(a * Math.sin(angle), 2)
						+ Math.pow(b * Math.cos(angle), 2));
		double xResult = this.ellipse.getCenterX() + radius * Math.cos(angle);
		double yResult = this.ellipse.getCenterY() - radius * Math.sin(angle);
		Point p = new Point();
		p.setLocation((int) xResult, (int) yResult);
		return p;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getText() {
		return name;
	}

	public void setText(String text) {
		this.name = text;
	}

	public Ellipse2D getEllipse() {
		return ellipse;
	}

	public void setEllipse(Ellipse2D ellipse) {
		this.ellipse = ellipse;
		for (ActionEdge action : incomingActions) {
			Point temp = action.getPointStart();
			action.setPointEnd(this.getNearestBound(temp.getX(), temp.getY()));
		}

		for (ActionEdge action : outgoingActions) {
			Point temp = action.getPointEnd();
			action.setPointStart(this.getNearestBound(temp.getX(), temp.getY()));
		}
	}

	public void addIncomingAction(ActionEdge action) {
		this.incomingActions.add(action);
	}

	public void addOutgoingAction(ActionEdge action) {
		this.outgoingActions.add(action);
	}

	public void removeIncomingAction(ActionEdge action) {
		if (this.incomingActions.contains(action)) {
			this.incomingActions.remove(action);
		}
	}

	public void removeOutgoingAction(ActionEdge action) {
		if (this.outgoingActions.contains(action)) {
			this.outgoingActions.remove(action);
		}
	}

	public List<ActionEdge> getIncomingActions() {
		return this.incomingActions;
	}

	public List<ActionEdge> getOutgoingActions() {
		return this.outgoingActions;
	}

	public void move(double x, double y, int unitSize) {
		this.ellipse.setFrame(this.ellipse.getMinX() + x,
				this.ellipse.getMinY() + y, ovalWidth * unitSize, ovalHeight
						* unitSize);
		for (ActionEdge action : this.incomingActions) {
			Point tempStart = action.getPointStart();
			DialogueState tempStartState = action.getStartState();
			Point newEnd = this.getNearestBound(tempStart.getX(),
					tempStart.getY());
			action.setPointEnd(newEnd);
			action.setPointStart(tempStartState.getNearestBound(newEnd.getX(),
					newEnd.getY()));
		}

		for (ActionEdge action : this.outgoingActions) {
			Point tempEnd = action.getPointEnd();
			DialogueState tempEndState = action.getFinishState();
			Point newStart = this.getNearestBound(tempEnd.getX(),
					tempEnd.getY());
			action.setPointStart(newStart);
			if (tempEndState != null) {
				action.setPointEnd(tempEndState.getNearestBound(
						newStart.getX(), newStart.getY()));
			}
		}
	}

	public void draw(Graphics g, int unitSize) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(this.color);
		this.ellipse.setFrame(this.ellipse.getX(), this.ellipse.getY(),
				ovalWidth * unitSize, ovalHeight * unitSize);
		g2d.fill(this.ellipse);
		g2d.setColor(this.textColor);
		if (this.name.length() > stateTextLength) {
			g2d.drawString(
					this.name.substring(0, stateTextLength - 3).concat("..."),
					(int) (this.ellipse.getX()) + unitSize / 2,
					(int) (this.ellipse.getY()) + 2 * unitSize);
		} else {
			g2d.drawString(this.name, (int) (this.ellipse.getX()) + unitSize
					* ovalWidth / 2 - this.name.length() * unitSize / 4,
					(int) (this.ellipse.getY()) + 2 * unitSize);
		}

	}
}
