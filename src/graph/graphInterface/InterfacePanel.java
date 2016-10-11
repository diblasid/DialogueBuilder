package graph.graphInterface;

import graph.Graph.GraphEnum;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Vector;

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
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class InterfacePanel extends JPanel {

	private static final long serialVersionUID = -5100592217993772648L;

	private Dimension preferred;

	public interface ControlCallback {
		void newNodeClicked();

		void newEdgeClicked();

		void propertyChanged(GraphEnum e, String value);
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

		JTable properties = new JTable();
		properties.setModel(new PropertyCellModel());
		properties.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		properties.setCellEditor(new PropertyTableCellEditor());
		properties.setDefaultRenderer(Object.class, new PropertyCellRenderer(
				true));
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
				properties.getModel());
		sorter.setSortable(0, false);
		Comparator<Object> comparator = new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				return ((GraphEnum) o1).getName().compareTo(
						((GraphEnum) o2).getName());
			}
		};
		sorter.setComparator(0, comparator);
		properties.setRowSorter(sorter);
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
				((JTextField) component).setText(((GraphEnum) value).getValue()
						.toString());
				return component;
			} else {
				((JLabel) name).setText(((GraphEnum) value).getName()
						.toString());
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

	class PropertyCellModel extends AbstractTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = -1364419675640008237L;
		private Vector<Vector<Object>> props;
		private Vector<GraphEnum> values;
		private Vector<String> header;

		public PropertyCellModel() {
			props = new Vector<Vector<Object>>();
			values = new Vector<GraphEnum>();
			for (GraphEnum key : GraphEnum.values()) {
				Vector<Object> b = new Vector<Object>();
				b.add(key.getName());
				b.add(key.getValue());
				values.add(key);
				props.add(b);
			}
			header = new Vector<String>();
			header.add("Property");
			header.add("Value");

		}

		public String getColumnName(int col) {
			return header.get(col);
		}

		public int getRowCount() {
			return props.size();
		}

		public int getColumnCount() {
			// TODO Auto-generated method stub
			return header.size();
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			return props.get(rowIndex).get(columnIndex);
		}

		public boolean isCellEditable(int row, int col) {

			if (col < 1) {
				return false;
			} else {
				return true;
			}
		}

		public void setValueAt(Object value, int row, int col) {
			if (col == 1) {
				props.get(row).set(col, value);
				Object val = (Object) getValueAt(row, col);
				mListener.propertyChanged(values.get(row), (String) val);
				fireTableCellUpdated(row, col);
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
			String val;
			if (value instanceof Integer) {
				val = Integer.toString((Integer) value);
				this.value = new JFormattedTextField(NumberFormat.INTEGER_FIELD);
			} else if (value instanceof Double) {
				val = Double.toString((Double) value);
				this.value = new JFormattedTextField(
						NumberFormat.FRACTION_FIELD);
			} else if (value instanceof Color) {
				Color color = ((Color) value);
				val = Integer.toString(color.getRGB());
			} else {
				val = (String) value;
			}
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
				((JTextField) this.value).setText(val);
				return this.value;
			} else {
				((JLabel) name).setText(val);
				((JLabel) name).setEnabled(false);
				return name;
			}
		}

	}
}
