package graphwindow;

import graph.GraphController;
import graph.GraphInterfaceEditor;
import graph.GraphViewPanel;
import graph.graphInterface.InterfacePanel;
import graphwindow.WindowController.DataChangeListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GraphViewWindow extends JFrame implements DataChangeListener {

	private GraphViewPanel display;
	private InterfacePanel menu;

	public GraphViewWindow(String title, int DIM_PIXELS) {
		super(title);
		JPanel container = new JPanel();

		container.setBounds(0, 0, (int) (1.5 * DIM_PIXELS), DIM_PIXELS);
		container.setLayout(new GridBagLayout());
		GraphInterfaceEditor graph = new GraphInterfaceEditor(DIM_PIXELS);
		GraphController controller = new GraphController(graph);
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

		setSize((int) (1.5 * DIM_PIXELS), DIM_PIXELS);
		setLocation(100, 100);
		setVisible(true);
		setContentPane(container);

		new Thread(new Runnable() {

			public void run() {
				while (true) {
					display.repaint();
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}).start();

	}

	public void onDataChanged() {
		menu.repaint();
		display.repaint();
	}

}
