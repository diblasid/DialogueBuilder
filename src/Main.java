import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {

	/**
	 * @param args
	 */
	public static DrawPanel display;
	public static InterfacePanel menu;
	public static JFrame window;
	public static final int DIM_PIXELS = 1024;
	public static final int DIM = DIM_PIXELS / 4;

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
		container.setBounds(0, 0, 2 * DIM_PIXELS, DIM_PIXELS);
		container.setLayout(new GridLayout(1, 2));

		display = new DrawPanel(DIM_PIXELS, new Dialogue(DIM_PIXELS / DIM,
				DIM_PIXELS, new ArrayList<DialogueState>(),
				new ArrayList<ActionEdge>()));
		display.setBackground(Color.black);

		menu = new InterfacePanel(DIM_PIXELS, DIM_PIXELS, display);
		menu.setBackground(Color.GRAY);

		container.add(display);
		container.add(menu);

		window = new JFrame("GUI Test");
		// glass.setBackground(new Color(0,0,0,0));
		// glass.setOpaque(false);

		window.setSize(2 * DIM_PIXELS, DIM_PIXELS);
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
