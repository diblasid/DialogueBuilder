package graph.node;

import graph.edge.Edge;
import graph.edge.EdgeController;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class NodeController extends EdgeController {

	protected NodeController() {
	}

	public void drawNode(Node node, Graphics2D g2d, int unitSize) {
		g2d.setColor(node.getColor());
		g2d.fill(new Ellipse2D.Double(node.getMinX(), node.getMinY(), Node
				.getNodeWidth() * unitSize, Node.getNodeHeight() * unitSize));
		g2d.setColor(node.getTextColor());
		g2d.setFont(new Font("Arial", Font.BOLD, 5));
		if (node.getText().length() > Node.getNodeTextLength()) {
			g2d.drawString(
					node.getText().substring(0, Node.getNodeTextLength() - 3)
							.concat("..."), (int) (node.getMinX()) + unitSize
							/ 2, (int) (node.getMinY()) + 2 * unitSize);
		} else {
			g2d.drawString(node.getText(), (int) (node.getMinX()) + unitSize
					* Node.getNodeWidth() / 2 - node.getText().length()
					* unitSize / 4, (int) (node.getMinY() + 2 * unitSize));
		}

		for (Edge edge : node.getOutgoingEdges()) {
			drawEdge(edge, g2d, unitSize);
		}

	}
}
