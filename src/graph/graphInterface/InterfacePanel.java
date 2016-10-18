package graph.graphInterface;

import graphwindow.WindowController.DataChangeListener;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import property.PropertyEnum;

public class InterfacePanel extends JPanel {

	private static final long serialVersionUID = -5100592217993772648L;

	private Dimension preferred;

	private DataChangeListener changeListener;
	private JTable properties;

	public interface ControlCallback {
		void newNodeClicked();

		void newEdgeClicked();

		PropertyCellModel getCellModel();

		// void propertyChanged(GraphEnum e, String value);
	}

	private ControlCallback mListener;

	public InterfacePanel(int width, int height, ControlCallback listener,
			Dimension preferred) {
		this.setSize(width, height);
		this.mListener = listener;
		this.preferred = preferred;
		this.setLayout(new GridLayout(2, 1));

		JPanel buttons = new JPanel();
		buttons.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 2;
		c.gridheight = 1;
		JButton newStateButton = new JButton();
		newStateButton.setSize(width / 2, height / 2);
		newStateButton.setText("New State");
		newStateButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.out.println("New State Created");
				mListener.newNodeClicked();
			}

		});

		JButton createActionButton = new JButton();
		createActionButton.setSize(width / 2, 20);
		createActionButton.setText("Create an Action");
		createActionButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.out.println("New Action Created");
				mListener.newEdgeClicked();
			}

		});
		buttons.add(newStateButton, c);
		c.gridx = 2;
		buttons.add(createActionButton, c);

		this.add(buttons);

		properties = new JTable();
		properties.setModel(this.mListener.getCellModel());
		properties.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		properties.setCellEditor(new PropertyTableCellEditor());
		properties.setDefaultRenderer(Object.class, new PropertyCellRenderer(
				true));

		properties.setSize(width, height / 2);
		JScrollPane scroller = new JScrollPane(properties);

		this.add(scroller);
	}

	@Override
	public Dimension getPreferredSize() {
		return this.preferred;
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

	class PropertyCellRenderer extends JComponent implements TableCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 127675220061409926L;
		private boolean isBordered;
		private JLabel name;
		private JTextField value;

		public PropertyCellRenderer(boolean isBordered) {
			this.isBordered = isBordered;
			this.name = new JLabel();
			this.value = new JTextField();
			setOpaque(true);
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {

			if (isBordered) {
				if (isSelected) {
					setBorder(table.getBorder());
					setBackground(table.getSelectionBackground());
				} else {
					setBorder(table.getBorder());
					setBackground(table.getBackground());
				}
			}
			if (column == 1) {

				String val;
				if (value instanceof Integer) {
					val = Integer.toString((Integer) value);
					this.value = new JFormattedTextField(
							NumberFormat.INTEGER_FIELD);
				} else if (value instanceof Double) {
					val = Double.toString((Double) value);
					this.value = new JFormattedTextField(
							NumberFormat.FRACTION_FIELD);
				} else if (value instanceof Color) {
					Color color = ((Color) value);
					val = Integer.toString(color.getRGB());
				} else {
					val = value.toString();
				}
				((JTextField) this.value).setText(val);
				return this.value;
			} else {
				if (value instanceof PropertyEnum) {
					String val = ((PropertyEnum) value).getPropertyName();
					((JLabel) name).setText(val);
					((JLabel) name).setEnabled(false);
				}

				return name;
			}
		}

	}

}
