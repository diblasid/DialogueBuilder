package graph.graphInterface;

import graph.Graph.GraphEnum;
import graph.graphInterface.InterfacePanel.ControlCallback;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class PropertyCell implements ListCellRenderer<GraphEnum> {

	// This is the only method defined by ListCellRenderer.
	// We just reconfigure the JLabel each time we're called.
	private ControlCallback mListener;

	public PropertyCell(ControlCallback listener) {
		this.mListener = listener;
	}

	public Component getListCellRendererComponent(
			JList<? extends GraphEnum> list, // the list
			final GraphEnum value, // value to display
			int index, // cell index
			boolean isSelected, // is the cell selected
			boolean cellHasFocus) // does the cell have focus
	{
		JPanel cell = new JPanel(new GridLayout(1, 2));
		String s = value.getName();
		JLabel name = new JLabel(s);
		cell.add(name);

		switch (value.getType()) {
		case DOUBLE:
			final JFormattedTextField doubleEditor = new JFormattedTextField(
					NumberFormat.getNumberInstance());
			doubleEditor.setValue(value.getValue());
			doubleEditor.setEditable(true);
			doubleEditor.addKeyListener(new KeyListener() {

				public void keyTyped(KeyEvent e) {
					mListener.propertyChanged(value, doubleEditor.getText());
				}

				public void keyPressed(KeyEvent e) {

				}

				public void keyReleased(KeyEvent e) {

				}

			});
			cell.add(doubleEditor);

			break;
		case INTEGER:
			final JFormattedTextField intEditor = new JFormattedTextField(
					NumberFormat.getNumberInstance());
			intEditor.setValue(value.getValue());
			intEditor.setEditable(true);
			intEditor.addKeyListener(new KeyListener() {

				public void keyTyped(KeyEvent e) {
					mListener.propertyChanged(value, intEditor.getText());
				}

				public void keyPressed(KeyEvent e) {

				}

				public void keyReleased(KeyEvent e) {

				}

			});
			cell.add(intEditor);

			break;
		case COLOR:
			break;
		case STRING:
			break;
		}

		if (isSelected) {
			cell.setBackground(list.getSelectionBackground());
			cell.setForeground(list.getSelectionForeground());
		} else {
			cell.setBackground(list.getBackground());
			cell.setForeground(list.getForeground());
		}
		cell.setEnabled(list.isEnabled());
		cell.setFont(list.getFont());
		cell.setOpaque(true);
		return cell;
	}
}
