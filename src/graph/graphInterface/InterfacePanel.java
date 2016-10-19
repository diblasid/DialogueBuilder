package graph.graphInterface;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import property.PropertyEnum;

public class InterfacePanel extends JPanel {

	private static final long serialVersionUID = -5100592217993772648L;

	private Dimension preferred;

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
				mListener.newNodeClicked();
			}

		});

		JButton createActionButton = new JButton();
		createActionButton.setSize(width / 2, 20);
		createActionButton.setText("Create an Action");
		createActionButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				mListener.newEdgeClicked();
			}

		});
		buttons.add(newStateButton, c);
		c.gridx = 2;
		buttons.add(createActionButton, c);

		this.add(buttons);

		properties = new JTable(this.mListener.getCellModel()) {
			private static final long serialVersionUID = 1L;
			private Class<?> editingClass;

			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				editingClass = null;
				int modelColumn = convertColumnIndexToModel(column);
				if (modelColumn == 1) {
					Class<?> rowClass = getModel().getValueAt(row, modelColumn)
							.getClass();
					if (rowClass == Color.class) {

						return new TableCellRenderer() {

							public Component getTableCellRendererComponent(
									JTable table, Object value,
									boolean isSelected, boolean hasFocus,
									int row, int column) {
								Color color = (Color) value;
								JPanel colorPanel = new JPanel();
								colorPanel.setBackground(color);
								return colorPanel;
							}

						};
					}
					return getDefaultRenderer(rowClass);
				} else {
					return new TableCellRenderer() {

						public Component getTableCellRendererComponent(
								JTable table, Object value, boolean isSelected,
								boolean hasFocus, int row, int column) {

							return new JLabel(
									((PropertyEnum) value).getPropertyName());
						}

					};
				}
			}

			@Override
			public TableCellEditor getCellEditor(int row, int column) {
				editingClass = null;
				int modelColumn = convertColumnIndexToModel(column);
				if (modelColumn == 1) {
					editingClass = getModel().getValueAt(row, modelColumn)
							.getClass();
					if (editingClass == Color.class) {
						return new ColorCellEditor();
					}
					return getDefaultEditor(editingClass);
				} else {
					return super.getCellEditor(row, column);
				}
			}

			// This method is also invoked by the editor when the value in the
			// editor
			// component is saved in the TableModel. The class was saved when
			// the
			// editor was invoked so the proper class can be created.

			@Override
			public Class<?> getColumnClass(int column) {
				return editingClass != null ? editingClass : super
						.getColumnClass(column);
			}
		};

		properties.setModel(this.mListener.getCellModel());
		properties.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// properties.setCellEditor(new PropertyTableCellEditor());
		// properties.setDefaultRenderer(Object.class, new
		// PropertyCellRenderer(true));

		properties.setSize(width, height / 2);
		JScrollPane scroller = new JScrollPane(properties);

		this.add(scroller);
	}

	@Override
	public Dimension getPreferredSize() {
		return this.preferred;
	}

	class ColorCellEditor extends AbstractCellEditor implements
			TableCellEditor, ActionListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1757164937657693786L;
		Color currentColor;
		JButton button;
		JColorChooser colorChooser;
		JDialog dialog;
		protected static final String EDIT = "edit";

		public ColorCellEditor() {
			button = new JButton();
			button.setActionCommand(EDIT);
			button.addActionListener(this);
			button.setBorderPainted(false);

			// Set up the dialog that the button brings up.
			colorChooser = new JColorChooser();
			dialog = JColorChooser.createDialog(button, "Pick a Color", true, // modal
					colorChooser, this, // OK button handler
					null); // no CANCEL button handler
		}

		public Object getCellEditorValue() {
			return currentColor;
		}

		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			currentColor = (Color) value;
			return button;
		}

		public void actionPerformed(ActionEvent e) {
			if (EDIT.equals(e.getActionCommand())) {
				// The user has clicked the cell, so
				// bring up the dialog.
				button.setBackground(currentColor);
				colorChooser.setColor(currentColor);
				dialog.setVisible(true);

				fireEditingStopped(); // Make the renderer reappear.

			} else { // User pressed dialog's "OK" button.
				currentColor = colorChooser.getColor();
			}
		}

	}

}
