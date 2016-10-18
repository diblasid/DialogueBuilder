package graph.graphInterface;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

public class InterfaceController {

	private PropertyCellModel model;

	public InterfaceController(PropertyCellModel model) {
		this.model = model;
	}

	class PropertyTableCellEditor extends AbstractCellEditor implements
			TableCellEditor, ActionListener {

		/**
 * 
 */
		private static final long serialVersionUID = -1757164937657693786L;
		JComponent component, name;
		int row = 0, column = 0;

		public PropertyTableCellEditor() {
			component = new JTextField();
			name = new JLabel();
		}

		public Object getCellEditorValue() {
			if (this.column == 1) {
				return ((JTextField) component).getText();
			} else {
				return ((JLabel) name).getText();
			}
		}

		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			if (column == 1) {
				this.row = row;
				this.column = column;
				((JTextField) component).setText(value.toString());
				return component;
			} else {
				((JLabel) name).setText(value.toString());
				((JLabel) name).setEnabled(false);
				return name;
			}
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("edit")) {
				fireEditingStopped();
			}
		}

	}
}
