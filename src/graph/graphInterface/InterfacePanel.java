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
import java.util.EventObject;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

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
		TableModel model = new PropertyTableModel();
		PropertyTableCellRenderer renderer = new PropertyTableCellRenderer();

		JTable properties = new JTable(model);
		properties.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		properties.setCellEditor(new PropertyTableCellEditor());
		properties.setDefaultRenderer(GraphEnum.class, renderer);

		properties.setSize(width, height / 2);
		JScrollPane scroller = new JScrollPane(properties);

		this.add(scroller);

	}

	@Override
	public Dimension getPreferredSize() {
		return this.preferred;
	}

	class PropertyTableCellEditor extends AbstractCellEditor implements
			TableCellEditor {

		JComponent component = new JTextField(), name = new JLabel();
		int row = 0, column = 0;
		
		public PropertyTableCellEditor(){
			ActionListener actionListener = new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					
				}		
			};
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
		
		@Override
		public boolean isCellEditable(EventObject e){
				return true;
		}

	}
	
	class PropertyTableCellRenderer implements TableCellRenderer {
		  DefaultListCellRenderer listRenderer = new DefaultListCellRenderer();

		  DefaultTableCellRenderer tableRenderer = new DefaultTableCellRenderer();

		  private void configureRenderer(JLabel renderer, Object value) {
		      renderer.setBackground((Color)value);
		  }


		  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
		      boolean hasFocus, int row, int column) {
		    tableRenderer = (DefaultTableCellRenderer) tableRenderer.getTableCellRendererComponent(table,
		        value, isSelected, hasFocus, row, column);
		    configureRenderer(tableRenderer, value);
		    return tableRenderer;
		  }
		}
	
	class PropertyTableModel extends AbstractTableModel {
		private Vector<Vector<Object>> props;
		Vector<String> header;
		public PropertyTableModel(){
			props = new Vector<Vector<Object>>();
			for (GraphEnum key : GraphEnum.values()) {
				Vector<Object> b = new Vector<Object>();
				b.add(key.getName());
				b.add(key.getValue());
				props.add(b);
			}
			header = new Vector<String>();
			header.add("Property");
			header.add("Value");
		}
		  
		  public int getColumnCount() {
		    return header.size();
		  }

		  public String getColumnName(int column) {
		    return header.elementAt(column);
		  }

		  public int getRowCount() {
		    return props.size();
		  }

		  public Object getValueAt(int row, int column) {
		    return props.elementAt(row).elementAt(column);
		  }

		  public Class getColumnClass(int column) {
		    return (props.elementAt(0).elementAt(column).getClass());
		  }

		  public void setValueAt(Object value, int row, int column) {
		    props.elementAt(row).set(column, value);
		  }

		  public boolean isCellEditable(int row, int column) {
		    return (column != 0);
		  }
	}

}
