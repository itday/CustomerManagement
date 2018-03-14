package application;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import application.objects.MyJTable;
import application.objects.MyColumnModel;
import application.objects.MyDataModel;

/**
 * Generates the customers list tab.
 * 
 * @author Sebastian Müller
 * @since 14.03.2018
 */

public class ListTab {

    private HeadSceneController controller;

    private JScrollPane jScrollPane;
    private MyJTable    jTable;
    private JPanel      buttonJPanel;
    private Box         verticalBox;

    public ListTab(HeadSceneController controller) {
        this.controller = controller;
    }

    public Component draw() {
        generateTable();
        generateButtons();
        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.add(jScrollPane, BorderLayout.CENTER);
        jPanel.add(buttonJPanel, BorderLayout.SOUTH);

        return jPanel;
    }

    public MyJTable getJTable() {
        return jTable;
    }

    private void generateTable() {
        String[] columnNames = { "Id", "Family name", "First name", "ZIP code", "City", "Street", "House Number" };
        jTable = new MyJTable(new MyDataModel(controller.getCustomers(), columnNames), new MyColumnModel(columnNames.length));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(jTable, BorderLayout.CENTER);
        panel.add(jTable.getTableHeader(), BorderLayout.NORTH);

        jScrollPane = new JScrollPane(panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                                      ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    private void generateButtons() {
        buttonJPanel = new JPanel(new BorderLayout());

        Button b1 = new Button("Add Customer");
        Button b2 = new Button("Modify Customer");
        Button b3 = new Button("Delete Customer");

        b1.addActionListener(controller.getHandleAdd());
        b2.addActionListener(controller.getHandleModify());
        b3.addActionListener(controller.getHandleDelete());

        verticalBox = Box.createVerticalBox();
        verticalBox.add(b1);
        verticalBox.add(b2);
        verticalBox.add(b3);

        buttonJPanel.add(verticalBox, BorderLayout.EAST);
    }
}