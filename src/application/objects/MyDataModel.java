package application.objects;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import application.Customer;

/**
 * Auführung von AbstractTableModel für Customer
 *
 * @author Sebastian Müller
 * @since 14.03.2018
 */

@SuppressWarnings("serial") public class MyDataModel extends AbstractTableModel {

    private ArrayList<Customer> customers;
    private String[]            columnNames;

    public MyDataModel(ArrayList<Customer> customers, String[] columnNames) {
        this.customers = customers;
        this.columnNames = columnNames;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (customers.size() < rowIndex)
            customers.add(rowIndex, new Customer());

        customers.get(rowIndex).getVector().set(columnIndex, "" + aValue);
        refresh();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return customers.get(rowIndex).getVector().get(columnIndex);
    }

    public Customer getCustomer(int rowIndex) {
        if (rowIndex >= customers.size() || rowIndex < 0)
            return null;
        return customers.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return customers.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0)
            return int.class;
        else
            return String.class;
    }

    public void refresh() {
        fireTableDataChanged();
    }
}