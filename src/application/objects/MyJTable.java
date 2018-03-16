package application.objects;

import java.awt.FontMetrics;
import java.util.Enumeration;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * Wrapper für {@link JTable}
 *
 * @author Sebastian Müller
 * @since 14.03.2018
 */

@SuppressWarnings("serial") public class MyJTable extends JTable {

    private FontMetrics fontMetrics;

    public MyJTable(TableModel dataModel, TableColumnModel columnModel) {
        super(dataModel, columnModel);

    }

    public void init(FontMetrics fontMetrics) {
        this.fontMetrics = fontMetrics;
        updateSize();
    }

    public void updateSize() {
        int i = 0;
        String header;
        Enumeration<TableColumn> columns = columnModel.getColumns();
        while (columns.hasMoreElements()) {
            TableColumn tc = columns.nextElement();
            header = dataModel.getColumnName(i);
            tc.setHeaderValue(header);

            int width = fontMetrics.stringWidth(header);
            int tmp;
            for (int y = 0; y < getRowCount(); ++y)
                width = width < (tmp = fontMetrics.stringWidth("" + getValueAt(y, i))) ? tmp : width;

            tc.setPreferredWidth(width);
            ++i;
        }
    }

    @Override
    public MyDataModel getModel() {
        return (MyDataModel) super.getModel();
    }
}