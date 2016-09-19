import java.awt.Graphics;
import java.awt.geom.Ellipse2D;
import java.util.List;

public class Dialogue {
	private List<DialogueState> states;
	private List<ActionEdge> actions;

	public Dialogue(int unitSize, int dimPixels, List<DialogueState> states,
			List<ActionEdge> actions) {

		this.actions = actions;
		this.states = states;
		if (this.states.size() == 0) {
			createNewState("Start", dimPixels / 2 - DialogueState.ovalWidth
					* unitSize / 2, dimPixels / 2 - DialogueState.ovalHeight
					* unitSize / 2, unitSize);
		}
	}

	public void drawStates(Graphics g, int unitSize) {
		for (DialogueState state : this.states) {
			state.draw(g, unitSize);
		}
	}

	public void drawActions(Graphics g, int unitSize) {
		for (ActionEdge action : this.actions) {
			action.draw(g, unitSize);
		}
	}

	public List<DialogueState> getStates() {
		return states;
	}

	public void addState(DialogueState state) {
		this.states.add(state);
	}

	public List<ActionEdge> getActions() {
		return actions;
	}

	public void addAction(ActionEdge action) {
		this.actions.add(action);
	}

	public void createNewState(String name, double x, double y, int unitSize) {
		Ellipse2D ellipse = new Ellipse2D.Double(x, y, DialogueState.ovalWidth
				* unitSize, DialogueState.ovalHeight * unitSize);
		this.states.add(new DialogueState(ellipse, name, DialogueState.stateTextColor));
	}

}
