package graph;

import java.awt.Graphics;

import javax.swing.JPanel;

public class GraphViewPanel extends JPanel {

	private static final long serialVersionUID = 2676764807008166350L;

	private GraphController controller;

	public GraphViewPanel(GraphController controller) {
		this.controller = controller;
		this.addMouseListener(controller);
		this.addMouseMotionListener(controller);
		this.addMouseWheelListener(controller);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		controller.draw(g);
	}

}
