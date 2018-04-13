package application;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import application.objects.Customer;
import application.objects.MyDialog;
import application.objects.MyJTable;

/**
 * Builds the GUI.
 *
 * @author Sebastian Müller
 * @since 16.03.2018
 */

public class Controller {

    private JTabbedPane jTabbedPane;

    /*
     * listTab
     */
    private Component listTab;
    private MyJTable  jTable;

    /*
     * detailsTab
     */
    private Component    detailsTab;
    private JTextField[] fields;

    /*
     * data
     */
    private ArrayList<Customer> customers;
    private int                 entryCounter = 0;
    private Customer            activeCustomer;

    // Message string for errors
    private String msg;

    /*
     * GUI actions
     */

    public void initialize(JFrame jFrame) {
        customers = new ArrayList<>();

        /*
         * Build GUI
         */
        jTabbedPane = new JTabbedPane();
        jFrame.add(jTabbedPane);

        ListTab iLT = new ListTab(this); // Input list tab
        listTab = iLT.draw();
        jTable = iLT.getJTable();

        DetailsTab iDT = new DetailsTab(this); // Input details tab
        detailsTab = iDT.draw();
        fields = iDT.getFields();

        jTabbedPane.add("All Customers", listTab);
        jTabbedPane.add("Customer Details", detailsTab);

        /*
         * Prepare data
         */
        loadData();
        jFrame.setVisible(true);
        jTable.init(jFrame.getGraphics().getFontMetrics());
    }

    /*
     * Customer actions
     */

    public void loadCustomer() {
        fields[0].requestFocus();
        if (activeCustomer == null) {
            fields[0].setText("");
            fields[1].setText("");
            fields[2].setText("");
            fields[3].setText("");
            fields[4].setText("");
            fields[5].setText("");
        } else {
            fields[0].setText(activeCustomer.getFamilyName());
            fields[1].setText(activeCustomer.getStreet());
            fields[2].setText(activeCustomer.getCity());
            fields[3].setText(activeCustomer.getFirstName());
            fields[4].setText(activeCustomer.getHouseNumber());
            fields[5].setText(activeCustomer.getZipCode());
        }
    }

    public boolean saveCustomer() {
        if (activeCustomer == null)
            activeCustomer = new Customer();

        /*
         * Has something changed?
         */
        boolean equal = true;

        if (!getField(0).equals(activeCustomer.getFamilyName()))
            equal = false;
        else if (!getField(1).equals(activeCustomer.getStreet()))
            equal = false;
        else if (!getField(2).equals(activeCustomer.getCity()))
            equal = false;
        else if (!getField(3).equals(activeCustomer.getFirstName()))
            equal = false;
        else if (!getField(4).equals(activeCustomer.getHouseNumber()))
            equal = false;
        else if (!getField(5).equals(activeCustomer.getZipCode()))
            equal = false;

        if (equal) {
            msg = "Nothing changed!";
            return false;
        }

        /*
         * Exists this entry in the table twice?
         */
        Customer customer = new Customer(activeCustomer.getId(), getField(0),getField(3),getField(5),getField(2),getField(1),getField(4));

        if (!Main.enableDefects) {
            String simpleString = customer.toSimpleStringWithoutId();
            for (Customer c : customers)
                if (c.toSimpleStringWithoutId().equals(simpleString)) {
                    msg = "Customer already exists with id " + c.getId() + " in database!";
                    return false;
                }
        }

        // The customer passed every check -> update data
        activeCustomer.setVektor(customer.getVector());

        /*
         * Update tableView
         */
        if (!customers.contains(activeCustomer)) { // a new entry
            activeCustomer.setId(++entryCounter);
            customers.add(activeCustomer);
        }

        jTable.getModel().refresh();
        jTable.updateSize();

        msg = "Customer was saved successfully.";
        return true; // !equal
    }

    private String getField(int index) {
        return fields[index].getText().trim();
    }

    public boolean checkFields() {

        msg = "";

        msg += Check.name(fields[0].getText(), "Family name");
        msg += Check.street(fields[1].getText(), "Street name");
        msg += Check.name(fields[2].getText(), "City name");
        msg += Check.name(fields[3].getText(), "First name");
        msg += Check.houseNumber(fields[4].getText(), "House number");
        msg += Check.zipCode(fields[5].getText(), "ZIP code");

        if (!msg.isEmpty())
            msg = "Error while checking text fields:\n" + msg;

        return msg.isEmpty();
    }

    private void loadData() {
        try (Scanner scanner = new Scanner(Main.class.getResourceAsStream("/resources/Customers.csv"), StandardCharsets.UTF_8.name())) {
            start: while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty() || line.startsWith("#") || line.startsWith("//"))
                    continue start;
                line = line.replace(',', ';');

                try {
                    String[] cells = line.split(";");
                    if (cells.length >= 7) {
                        int id;

                        if ("".equals(cells[0]))
                            id = ++entryCounter; // Set id (if not set)
                        else {
                            try {
                                id = Integer.parseInt(cells[0]); // Parse id
                            } catch (NumberFormatException e) {
                                MyDialog.error("Error in customers input file: Malformed id value", "Line: " + line);
                                continue start;
                            }

                            if (id <= 0) {
                                MyDialog.error("Error in customers input file: Malformed id value", "Line: " + line);
                                continue start;
                            }

                            entryCounter = entryCounter < id ? id : entryCounter; // Update
                                                                                  // entryCounter
                            for (Customer c : customers)
                                if (c.getId() == id) {
                                    MyDialog.error("Error in customers input file: Double id value", "Line: " + line);
                                    continue start;
                                }
                        }

                        activeCustomer = new Customer(id, cells[1].trim(), cells[2].trim(), cells[3].trim(), cells[4].trim(),
                                                      cells[5].trim(), cells[6].trim());
                        // At this point you could check the inputs and check
                        // for duplicates.
                        customers.add(activeCustomer);
                    } else
                        MyDialog.error("Error in customers input file: Too few columns", "Line: " + line); // continue:
                                                                                                           // start
                } catch (Exception e) {
                    e.printStackTrace();
                    MyDialog.error("Error in customers input file", "Line: " + line);
                } finally {
                    activeCustomer = null;
                }
            }
            scanner.close(); // Scanner could theoretically remain open
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Sorts the data
        customers.sort((o1, o2) -> o1.compareTo(o2));

        // refresh
        jTable.getModel().refresh();
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    /*
     * Handler for actions
     */

    public ActionListener getHandleAdd() {
        return (e) -> {
            activeCustomer = null;
            loadCustomer();
            jTabbedPane.setSelectedComponent(detailsTab);
        };
    }

    public ActionListener getHandleModify() {
        return (e) -> {
            activeCustomer = jTable.getModel().getCustomer(jTable.getSelectedRow());
            if (activeCustomer == null) {
                MyDialog.error(null, "No line selected!");
            } else {
                loadCustomer();
                jTabbedPane.setSelectedComponent(detailsTab);
            }
        };
    }

    public ActionListener getHandleDelete() {
        return (e) -> {
            activeCustomer = jTable.getModel().getCustomer(jTable.getSelectedRow());
            if (activeCustomer == null) {
                MyDialog.error(null, "No line selected!");
            } else {
                customers.remove(activeCustomer);
                activeCustomer = null;
                loadCustomer();
                jTable.getModel().refresh();
            }
        };
    }

    public ActionListener getHandleCancle() {
        return (e) -> {
            jTabbedPane.setSelectedComponent(listTab);
            activeCustomer = null;
            loadCustomer();
        };
    }

    public ActionListener getHandleReset() {
        return (e) -> {
            loadCustomer();
        };
    }

    public ActionListener getHandleSave() {
        return (e) -> {
            handleSave();
        };
    }

    private void handleSave() {
        if (checkFields()) {
            if (saveCustomer()) {
                MyDialog.info("Information dialog", msg);
                activeCustomer = null;
                loadCustomer();
                jTabbedPane.setSelectedComponent(listTab);
            } else
                MyDialog.error("Error dialog", msg);
        } else
            MyDialog.error("Error dialog", msg);
    }

    public KeyListener getHandleEnterSave() {
        return new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    handleSave();
            }

            @Override
            public void keyPressed(KeyEvent e) {}
        };
    }
}