package graph.node;

import graph.edge.Edge;
import graph.edge.EdgeController;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class NodeController extends EdgeController {

	protected NodeController() {
	}

	protected void drawNode(Node node, Graphics2D g2d, int unitSize) {
		g2d.setColor(node.getColor());
		g2d.fill(new Ellipse2D.Double(node.getMinX(), node.getMinY(), Node
				.getNodeWidth() * unitSize, Node.getNodeHeight() * unitSize));
		g2d.setColor(node.getTextColor());
		g2d.setFont(new Font("Arial", Font.BOLD, 10));
		if (node.getText().length() > Node.getNodeTextLength()) {
			Rectangle rectangle = new Rectangle();
			rectangle.setBounds(node.getMinX(), node.getMinY(), 10 * unitSize,
					6 * unitSize);
			drawCenteredString(g2d,
					node.getText().substring(0, Node.getNodeTextLength() - 3)
							.concat("..."), rectangle, new Font("Arial",
							Font.BOLD, 50));
		} else {
			Rectangle rectangle = new Rectangle();
			rectangle.setBounds(node.getMinX(), node.getMinY(), 10 * unitSize,
					6 * unitSize);
			drawCenteredString(g2d, node.getText(), rectangle, new Font(
					"Arial", Font.BOLD, 50));
		}

		for (Edge edge : node.getOutgoingEdges()) {
			drawEdge(edge, g2d, unitSize);
		}

	}

	private void drawCenteredString(Graphics2D g, String text, Rectangle r,
			Font font) {
		FontRenderContext frc = new FontRenderContext(null, true, true);

		Rectangle2D r2D = font.getStringBounds(text, frc);
		int rWidth = (int) Math.round(r2D.getWidth());
		int rHeight = (int) Math.round(r2D.getHeight());
		int rX = (int) Math.round(r2D.getX());
		int rY = (int) Math.round(r2D.getY());

		int a = (r.width / 2) - (rWidth / 2) - rX;
		int b = (r.height / 2) - (rHeight / 2) - rY;

		g.setFont(font);
		g.drawString(text, r.x + a, r.y + b);

	}
}
