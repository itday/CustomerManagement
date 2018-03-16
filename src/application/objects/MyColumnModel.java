package application.objects;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

/**
 * Own column model for {@link javax.swing.JTable} extends {@link DefaultTableColumnModel}.
 *
 * @author Sebastian Müller
 * @since 14.03.2018
 */

@SuppressWarnings("serial") public class MyColumnModel extends DefaultTableColumnModel {

    public MyColumnModel(int count) {
        super();

        for (int i = 0; i < count; ++i)
            addColumn(new TableColumn(i));
    }
}
