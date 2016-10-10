package graph.graphInterface;

import graph.Graph.GraphEnum;
import graph.graphInterface.InterfacePanel.ControlCallback;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.beans.PropertyEditor;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PropertyCellEditor extends JPanel implements PropertyEditor {
	private GraphEnum property;
	private JFormattedTextField editor;
	private JLabel name;
	private ControlCallback mListener;

	public PropertyCellEditor(ControlCallback listener, GraphEnum property) {
		this.property = property;
		this.mListener = listener;
		editor = new JFormattedTextField(NumberFormat.getNumberInstance());
		editor.setText(property.getValue().toString());
		name = new JLabel(property.getName());
		this.setLayout(new GridLayout(1, 2));
		this.add(name);
		this.add(editor);
	}

	public void setValue(Object value) {
		editor.setText(value.toString());
		mListener.propertyChanged(property, value.toString());
	}

	public Object getValue() {
		return editor.getText();
	}

	public boolean isPaintable() {
		return false;
	}

	public void paintValue(Graphics gfx, Rectangle box) {

	}

	public String getJavaInitializationString() {
		return null;
	}

	public String getAsText() {
		return property.getValue().toString();
	}

	public void setAsText(String text) throws IllegalArgumentException {
		this.editor.setText(text);
		mListener.propertyChanged(property, text);
	}

	public String[] getTags() {
		return null;
	}

	public Component getCustomEditor() {
		return null;
	}

	public boolean supportsCustomEditor() {
		return true;
	}
}