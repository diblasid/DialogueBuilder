package graph.graphInterface;

import graph.Graph.GraphEnum;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

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

		JTable properties = new JTable(GraphEnum.values().length, 2);
		for (GraphEnum key : GraphEnum.values()) {
			JPanel property = new JPanel();
			property.setLayout(new GridLayout(1, 2));
			JLabel name = new JLabel(key.getName());
			name.setEnabled(false);
			JFormattedTextField value = new JFormattedTextField(
					NumberFormat.getNumberInstance());
			value.setText(key.getValue().toString());
			value.setEditable(true);
			property.add(name);
			property.add(value);
			properties.add(property);
		}
		properties.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		properties.setSize(width, height / 2);
		JScrollPane scroller = new JScrollPane(properties);

		this.add(scroller);

	}

	@Override
	public Dimension getPreferredSize() {
		return this.preferred;
	}

}
