package graph.graphInterface;

import graph.Selectable;

import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import property.PropertyEnum;

public class PropertyCellModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1364419675640008237L;
	protected Vector<Vector<Object>> props;
	protected Vector<String> header;
	private Selectable selected;

	public PropertyCellModel(Selectable selected) {
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

			PropertyEnum temp = (PropertyEnum) props.get(row).get(0);
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

				break;
			default:
				break;
			}
			this.fireTableDataChanged();
		}
	}

}
