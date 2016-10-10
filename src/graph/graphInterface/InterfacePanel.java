package graph.graphInterface;

import graph.Graph.GraphEnum;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

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
		DefaultTableModel model = new DefaultTableModel();

		Vector<Vector<Object>> props = new Vector<Vector<Object>>();
		for (GraphEnum key : GraphEnum.values()) {
			Vector<Object> b = new Vector<Object>();
			b.add(key.getName());
			b.add(key.getValue());
			props.add(b);
		}
		Vector<String> header = new Vector<String>();
		header.add("Property");
		header.add("Value");

		model.setDataVector(props, header);

		JTable properties = new JTable(model);
		properties.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		properties.setCellEditor(new PropertyTableCellEditor());

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

		public Object getCellEditorValue() {
			if (this.column == 1) {
				return ((JTextField) component).getText();
			} else {
				return null;
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

	}

}
