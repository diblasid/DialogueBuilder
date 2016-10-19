import graph.GraphViewPanel;
import graph.graphInterface.InterfacePanel;
import graphwindow.GraphViewWindow;

import javax.swing.JFrame;
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

		GraphViewWindow window = new GraphViewWindow("GUI Test", DIM_PIXELS);
		window.setEnabled(true);
	}

}
