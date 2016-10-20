package graphInterface;

import graphInterface.property.PropertyKey;
import graphInterface.property.Selectable;

import java.awt.Color;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class PropertyCellModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1364419675640008237L;
	protected Vector<Vector<Object>> props;
	protected Vector<String> header;
	private Selectable selected;

	public PropertyCellModel(Selectable selected) {
		setSelected(selected);
	}

	public void setSelected(Selectable selected) {
		this.selected = selected;
		props = new Vector<Vector<Object>>();
		for (Entry<Object, Object> key : selected.getProperties().entrySet()) {
			Vector<Object> b = new Vector<Object>();
			b.add(key.getKey());
			b.add(key.getValue());
			props.add(b);
		}
		header = new Vector<String>();
		header.add("Property");
		header.add("Value");

		this.fireTableDataChanged();
	}

	public String getColumnName(int col) {
		return header.get(col);
	}

	public int getRowCount() {
		return props.size();
	}

	public int getColumnCount() {
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

			PropertyKey temp = (PropertyKey) props.get(row).get(0);
			switch (temp.getType()) {
			case INTEGER:
				this.selected.getProperties().replace(temp,
						Integer.parseInt(value.toString()));
				break;
			case DOUBLE:
				this.selected.getProperties().replace(temp,
						Double.parseDouble(value.toString()));
				break;
			case COLOR:
				this.selected.getProperties().replace(temp, (Color) value);
				break;
			case STRING:
				this.selected.getProperties().replace(temp, value.toString());
				break;
			default:
				break;
			}
			this.fireTableDataChanged();
		}
	}

	public void setValueAt(Object value, PropertyKey key) {
		for (Vector<Object> temp : props) {
			if (((PropertyKey) temp.get(0)).getPropertyName().equals(
					key.getPropertyName())) {
				temp.set(1, value);
				this.fireTableDataChanged();
				return;
			}
		}
		return;
	}

}
