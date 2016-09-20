import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Ellipse2D;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class DrawPanel extends JPanel implements InterfacePanel.ControlCallback {

	private static final long serialVersionUID = 2676764807008166350L;

	private Dialogue dialogue;

	private Color gridLinesColor = Color.LIGHT_GRAY;

	private int dimPixels;
	public static final int GRID_LINE_WIDTH = 1;
	private int unitSize = 32;

	private DialogueState selectedState;
	private ActionEdge newAction, selectedAction;
	private Dimension preferred;

	private long mouseX, mouseY;
	public static final int DEFAULT_STATE = 0, CREATING_ACTION = 1,
			ACTION_RADIUS_CHANGE = 2;

	private int drawState = DEFAULT_STATE;

	public DrawPanel(int dimPixels, Dialogue dialogue, Dimension preferred) {
		this.dialogue = dialogue;
		this.dimPixels = dimPixels;
		this.preferred = preferred;
		this.addMouseListener(new MyMouseListener());
		this.addMouseMotionListener(new MyMouseMotionListener());
		this.addMouseWheelListener(new MyMouseListener());
	}

	private void drawGrid(Graphics g) {
		g.setColor(this.gridLinesColor);
		for (int k = -this.dimPixels / this.unitSize; k < 2 * this.dimPixels
				/ this.unitSize; k++) {
			g.drawLine(k * this.unitSize, 0, k * this.unitSize,
					this.getHeight());
		}

		for (int i = -this.dimPixels / this.unitSize; i < 2 * this.dimPixels
				/ this.unitSize; i++) {
			g.drawLine(0, i * this.unitSize, this.getWidth(), i * this.unitSize);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.clearRect(-this.dimPixels, -this.dimPixels, 2 * this.dimPixels,
				2 * this.dimPixels);
		drawGrid(g);

		dialogue.drawStates(g, this.unitSize);

		dialogue.drawActions(g, this.unitSize);

		if (newAction != null) {
			newAction.draw(g, this.unitSize);
		}
	}

	class MyMouseListener extends MouseAdapter {

		private int scrollAmount = 0;
		private double scale = 0.5;
		public static final int SCROLL_THRESHOLD = 5;

		public void mouseWheelMoved(MouseWheelEvent e) {
			this.scrollAmount += e.getWheelRotation();
			if (this.scrollAmount >= SCROLL_THRESHOLD) {
				if (unitSize < 64) {
					unitSize = unitSize * 2;
					notifyZoom(0.5, e.getX(), e.getY());
					this.scale = this.scale * 0.5;
				}
				this.scrollAmount = 0;
			} else if (this.scrollAmount <= -SCROLL_THRESHOLD) {
				if (unitSize > 4) {
					unitSize = unitSize / 2;
					notifyZoom(2.0, e.getX(), e.getY());
					this.scale = 2.0 * this.scale;
				}
				this.scrollAmount = 0;

			}
		}

		public void mousePressed(MouseEvent e) {

			mouseX = e.getX();
			mouseY = e.getY();

			for (ActionEdge action : dialogue.getActions()) {
				if (action.getSelection().contains(mouseX, mouseY)
						&& drawState == DEFAULT_STATE) {
					drawState = ACTION_RADIUS_CHANGE;
					selectedAction = action;
					return;
				}
			}

			for (DialogueState state : dialogue.getStates()) {
				Ellipse2D ellipse = state.getEllipse();
				if (ellipse.contains(e.getX(), e.getY())) {
					if (drawState == CREATING_ACTION) {
						if (newAction != null
								&& !newAction.getStartState().equals(state)) {
							newAction.getStartState().addOutgoingAction(
									newAction);
							state.addIncomingAction(newAction);
							newAction.setFinishState(state);
							newAction.setPointEnd(state.getNearestBound(
									newAction.getSelection().getCenterX(),
									newAction.getSelection().getCenterY()));
							dialogue.addAction(newAction);
							newAction = null;
							emptySelected();
							drawState = DEFAULT_STATE;

						} else {
							emptySelected();
							selectedState = state;
							newAction = new ActionEdge(selectedState);
							selectedState.addOutgoingAction(newAction);
							selectedState
									.setColor(DialogueState.selectedStateColor);
						}
					} else {
						emptySelected();
						selectedState = state;
						selectedState
								.setColor(DialogueState.selectedStateColor);
					}
					return;
				}
			}
			emptySelected();
		}

		public void mouseReleased(MouseEvent e) {
			emptySelected();
			if (drawState == ACTION_RADIUS_CHANGE) {
				drawState = DEFAULT_STATE;
			}
		}

		public void mouseClicked(MouseEvent e) {

			for (DialogueState state : dialogue.getStates()) {

				Ellipse2D ellipse = state.getEllipse();
				if (ellipse.contains(e.getX(), e.getY())) {

				}
			}
		}
	}

	private void emptySelected() {
		if (selectedState != null) {
			selectedState.setColor(DialogueState.unSelectedStateColor);
			selectedState = null;
		}

		if (selectedAction != null) {
			selectedAction = null;
		}

	}

	private void notifyZoom(double ratio, int x, int y) {
		for (DialogueState ds : this.dialogue.getStates()) {
			ds.move((x - ds.getCenterX()) * ratio, (y - ds.getCenterY())
					* ratio, this.unitSize);
		}
		for (ActionEdge action : this.dialogue.getActions()) {
			double ax = action.getSelection().getCenterX(), ay = action
					.getSelection().getCenterY();
			action.setSelection((x - ax) * ratio + ax, (y - ay) * ratio + ay);
		}
	}

	class MyMouseMotionListener extends MouseMotionAdapter {
		public void mouseDragged(MouseEvent e) {

			if (selectedAction != null && drawState == ACTION_RADIUS_CHANGE) {
				selectedAction.setSelection(e.getX(), e.getY());
			} else if (selectedState != null) {
				selectedState.move(e.getX() - mouseX, e.getY() - mouseY,
						unitSize);
				mouseX = e.getX();
				mouseY = e.getY();
			}
		}

		public void mouseMoved(MouseEvent e) {
			if (drawState == CREATING_ACTION && newAction != null) {
				Point temp = newAction.getStartState().getNearestBound(
						e.getX(), e.getY());
				Point temp2 = new Point();
				temp2.setLocation((int) e.getX(), (int) e.getY());
				newAction.setSelection(temp.getX(), temp2.getX(), temp.getY(),
						temp2.getY());
			}
		}
	}

	public void newStateClicked() {
		JFrame frame = new JFrame("Name Your New State");
		String name = (String) JOptionPane.showInputDialog(frame,
				"What would you like to name the new state?", "New State",
				JOptionPane.QUESTION_MESSAGE);
		if (name != null) {
			dialogue.createNewState(name, this.dimPixels / 2
					- DialogueState.ovalWidth * this.unitSize / 2,
					this.dimPixels / 2 - DialogueState.ovalHeight
							* this.unitSize / 2, this.unitSize);
			this.drawState = DEFAULT_STATE;
		}
	}

	public void newActionClicked() {
		emptySelected();
		this.drawState = CREATING_ACTION;
	}

	@Override
	public Dimension getPreferredSize() {
		return this.preferred;
	}
}
