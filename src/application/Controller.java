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
    private String header;
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
        if (activeCustomer == null) {
            fields[0].requestFocus();
            fields[0].setText("");
            fields[1].setText("");
            fields[2].setText("");
            fields[3].setText("");
            fields[4].setText("");
            fields[5].setText("");
        } else {
            fields[0].requestFocus();
            fields[0].setText(activeCustomer.getFamilyName());
            fields[1].setText(activeCustomer.getStreet());
            fields[2].setText(activeCustomer.getCity());
            fields[3].setText(activeCustomer.getFirstName());
            fields[4].setText(activeCustomer.getHouseNumber());
            fields[5].setText(activeCustomer.getZipCode());
        }
    }

    public boolean saveCustomer() {
        if (activeCustomer == null) // Add new customer
            activeCustomer = new Customer(); // Without Id

        /*
         * Has something changed?
         */

        boolean equal = true;
        String tmp = fields[0].getText().trim();

        if (!tmp.equals(activeCustomer.getFamilyName())) {
            activeCustomer.setFamilyName(tmp);
            equal = false;
        }

        if (!(tmp = fields[1].getText().trim()).equals(activeCustomer.getStreet())) {
            activeCustomer.setStreet(tmp);
            equal = false;
        }

        if (!(tmp = fields[2].getText().trim()).equals(activeCustomer.getCity())) {
            activeCustomer.setCity(tmp);
            equal = false;
        }

        if (!(tmp = fields[3].getText().trim()).equals(activeCustomer.getFirstName())) {
            activeCustomer.setFirstName(tmp);
            equal = false;
        }

        if (!(tmp = fields[4].getText().trim()).equals(activeCustomer.getHouseNumber())) {
            activeCustomer.setHouseNumber(tmp);
            equal = false;
        }

        if (!(tmp = fields[5].getText().trim()).equals(activeCustomer.getZipCode())) {
            activeCustomer.setZipCode(tmp);
            equal = false;
        }

        if (equal) {
            msg = "Nothing changed!";
            return false;
        }

        /*
         * Exists this entry in the table twice?
         */
        if (!Main.enableDefects) {
            String simpleString = activeCustomer.toSimpleStringWithoutId();
            for (Customer c : customers)
                if (activeCustomer != c && c.toSimpleStringWithoutId().equals(simpleString)) {
                    msg = "Customer already exists with id " + c.getId() + " in database!";
                    return false;
                }
        }

        /*
         * Update tableView
         */

        if (!customers.contains(activeCustomer)) {
            activeCustomer.setId(++entryCounter);
            customers.add(activeCustomer);
        }
        jTable.getModel().refresh();
        jTable.updateSize();

        msg = "Customer was saved successfully.";
        return true; // !equal
    }

    public boolean checkFields() {

        header = "Error while checking text fields!";
        msg = "";

        msg += Check.name(fields[0].getText(), "Family name");
        msg += Check.street(fields[1].getText(), "Street name");
        msg += Check.name(fields[2].getText(), "City name");
        msg += Check.name(fields[3].getText(), "First name");
        msg += Check.houseNumber(fields[4].getText(), "House number");
        msg += Check.zipCode(fields[5].getText(), "ZIP code");

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
            jTabbedPane.setSelectedComponent(detailsTab);
            loadCustomer();
        };
    }

    public ActionListener getHandleModify() {
        return (e) -> {
            activeCustomer = jTable.getModel().getCustomer(jTable.getSelectedRow());
            if (activeCustomer == null) {
                MyDialog.error(null, "No line selected!");
            } else {
                jTabbedPane.setSelectedComponent(detailsTab);
                loadCustomer();
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
                activeCustomer = null; // Fix
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
                MyDialog.info(null, msg);
                activeCustomer = null;
                loadCustomer();
                jTable.getModel().refresh();
                jTabbedPane.setSelectedComponent(listTab);
            } else
                MyDialog.error(null, msg);
        } else
            MyDialog.error(header, msg);
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