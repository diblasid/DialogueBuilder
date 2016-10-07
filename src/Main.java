import graph.Graph;
import graph.GraphController;
import graph.GraphViewPanel;
import graphInterface.InterfacePanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {

	/**
	 * @param args
	 */
	public static GraphViewPanel display;
	public static InterfacePanel menu;
	public static JFrame window;
	public static final int DIM_PIXELS = 1024;

	public static void main(String[] args) {

		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}

		JPanel container = new JPanel();

		container.setBounds(0, 0, (int) (1.5 * DIM_PIXELS), DIM_PIXELS);
		container.setLayout(new GridBagLayout());

		GraphController controller = new GraphController(new Graph(DIM_PIXELS));
		display = new GraphViewPanel(controller);
		display.setMaximumSize(new Dimension(java.awt.Toolkit
				.getDefaultToolkit().getScreenSize()));
		display.setMinimumSize(new Dimension(128, 128));
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.ipadx = 0;
		c.ipady = 0;
		c.gridx = 0;
		c.gridy = 0;
		container.add(display, c);

		menu = new InterfacePanel(DIM_PIXELS / 2, DIM_PIXELS, controller,
				new Dimension(DIM_PIXELS / 2, DIM_PIXELS));
		menu.setMaximumSize(new Dimension(DIM_PIXELS / 2,
				(int) java.awt.Toolkit.getDefaultToolkit().getScreenSize()
						.getHeight()));
		menu.setMinimumSize(new Dimension(DIM_PIXELS / 2, DIM_PIXELS / 4));
		menu.setBackground(Color.GRAY);
		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 2;
		c.gridwidth = 1;
		c.ipadx = 0;
		c.ipady = 0;
		c.gridheight = 1;
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridy = 0;

		container.add(menu, c);

		window = new JFrame("GUI Test");

		window.setSize((int) (1.5 * DIM_PIXELS), DIM_PIXELS);
		window.setLocation(100, 100);
		window.setVisible(true);
		window.setContentPane(container);

		new Thread(new Runnable() {

			public void run() {
				while (true) {
					display.repaint();
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}).start();

	}

}
