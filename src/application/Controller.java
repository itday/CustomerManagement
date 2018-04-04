package application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import javax.swing.JButton;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * Controller for FXML file.
 *
 * @author Sebastian Müller
 * @since 16.03.2018
 */

public class Controller {

    @FXML private TabPane tabPane;
    @FXML private Tab     tabList;
    @FXML private Tab     tabDetail;

    /*
     * tabList
     */

    @FXML private TableView<Customer>            tableView;
    private ObservableList<Customer>             data;
    @FXML private TableColumn<Customer, Integer> idTableColumn;
    @FXML private TableColumn<Customer, String>  houseNumberTableColumn;

    private int entryCounter = 0;

    /*
     * @FXML private JButton add;
     * @FXML private JButton modify;
     * @FXML private JButton delete;
     */

    /*
     * detailsTab
     */

    private Customer activeCustomer;

    @FXML private Pane       detailsPane;
    @FXML private BorderPane detailsBorderPane;

    @FXML private TextField familyNameField;
    @FXML private TextField streetField;
    @FXML private TextField cityField;
    @FXML private TextField firstNameField;
    @FXML private TextField houseNumberField;
    @FXML private TextField zipCodeField;

    /*
     * @FXML private JButton cancel;
     * @FXML private JButton reset;
     * @FXML private JButton save;
     */

    @FXML private Button saveDetails;

    // Message string for errors
    private String header;
    private String msg;

    /*
     * GUI actions
     */

    public void initialize() {
        data = tableView.getItems();

        /*
         * Swing
         * add.addActionListener(getHandleAdd());
         * modify.addActionListener(getHandleModify());
         * delete.addActionListener(getHandleDelete());
         * cancel.addActionListener(getHandleCancel());
         * reset.addActionListener(getHandleReset());
         * save.addActionListener(getHandleSave());
         * /
         */

        // Load data
        loadData();

        // tableView resizing (could be better)
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        idTableColumn.setMaxWidth(30.0);
        idTableColumn.setMinWidth(30.0);
        houseNumberTableColumn.setMaxWidth(110.0);
        houseNumberTableColumn.setMinWidth(110.0);

        // Enable saveDetails button
        if (Main.saveButtonEnabled) {
            saveDetails.setDisable(false);
        } else {
            ChangeListener<String> listener = (a, b, c) -> {
                saveDetails.setDisable(!checkFields());
            };
            familyNameField.textProperty().addListener(listener);
            streetField.textProperty().addListener(listener);
            cityField.textProperty().addListener(listener);
            firstNameField.textProperty().addListener(listener);
            houseNumberField.textProperty().addListener(listener);
            zipCodeField.textProperty().addListener(listener);
        }

    }

    public void draw(Scene scene) {
        detailsPane.setMaxHeight(scene.getHeight());
        detailsBorderPane.setMaxWidth(scene.getWidth());
    }

    /*
     * Customer actions
     */

    private void loadCustomer() {
        familyNameField.requestFocus();
        if (activeCustomer == null) {
            familyNameField.setText("");
            streetField.setText("");
            cityField.setText("");
            firstNameField.setText("");
            houseNumberField.setText("");
            zipCodeField.setText("");
        } else {
            familyNameField.setText(activeCustomer.getFamilyName());
            streetField.setText(activeCustomer.getStreet());
            cityField.setText(activeCustomer.getCity());
            firstNameField.setText(activeCustomer.getFirstName());
            houseNumberField.setText(activeCustomer.getHouseNumber());
            zipCodeField.setText(activeCustomer.getZipCode());
        }
    }

    public boolean saveCustomer() {
        if (activeCustomer == null) // Add new customer
            activeCustomer = new Customer(); // Without Id

        /*
         * Has something changed?
         */

        boolean equal = true;
        String tmp = familyNameField.getText().trim();

        if (!tmp.equals(activeCustomer.getFamilyName())) {
            activeCustomer.setFamilyName(tmp);
            equal = false;
        }

        if (!(tmp = streetField.getText().trim()).equals(activeCustomer.getStreet())) {
            activeCustomer.setStreet(tmp);
            equal = false;
        }

        if (!(tmp = cityField.getText().trim()).equals(activeCustomer.getCity())) {
            activeCustomer.setCity(tmp);
            equal = false;
        }

        if (!(tmp = firstNameField.getText().trim()).equals(activeCustomer.getFirstName())) {
            activeCustomer.setFirstName(tmp);
            equal = false;
        }

        if (!(tmp = houseNumberField.getText().trim()).equals(activeCustomer.getHouseNumber())) {
            activeCustomer.setHouseNumber(tmp);
            equal = false;
        }

        if (!(tmp = zipCodeField.getText().trim()).equals(activeCustomer.getZipCode())) {
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
            for (Customer c : data)
                if (activeCustomer != c && c.toSimpleStringWithoutId().equals(simpleString)) {
                    msg = "Customer already exists with id " + c.getId() + " in database!";
                    return false;
                }
        }

        /*
         * Update tableView
         */

        if (data.contains(activeCustomer))
            // fix update issue
            data.set(data.indexOf(activeCustomer), activeCustomer);
        else {
            activeCustomer.setId(++entryCounter);
            data.add(activeCustomer);
        }

        msg = "Customer was saved successfully.";
        return true; // !equal
    }

    public boolean checkFields() {

        header = "Error while checking text fields!";
        msg = "";

        msg += Check.name(familyNameField.getText(), "Family name");
        msg += Check.street(streetField.getText(), "Street name");
        msg += Check.name(cityField.getText(), "City name");
        msg += Check.name(firstNameField.getText(), "First name");
        msg += Check.houseNumber(houseNumberField.getText(), "House number");
        msg += Check.zipCode(zipCodeField.getText(), "ZIP code");

        return msg.isEmpty();
    }

    public void loadData() {
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
                                MyDialog.error("Error in customers input file: Malformed id value", "Line: " + line).show();
                                continue start;
                            }

                            if (id <= 0) {
                                MyDialog.error("Error in customers input file: Malformed id value", "Line: " + line).show();
                                continue start;
                            }

                            entryCounter = entryCounter < id ? id : entryCounter; // Update
                                                                                  // entryCounter
                            for (Customer c : data)
                                if (c.getId() == id) {
                                    MyDialog.error("Error in customers input file: Double id value", "Line: " + line).show();
                                    continue start;
                                }
                        }

                        activeCustomer = new Customer(id, cells[1].trim(), cells[2].trim(), cells[3].trim(), cells[4].trim(),
                                                      cells[5].trim(), cells[6].trim());
                        // At this point you could check the inputs and check
                        // for duplicates.
                        data.add(activeCustomer);
                    } else
                        MyDialog.error("Error in customers input file: Too few columns", "Line: " + line).show(); // continue:
                                                                                                                  // start
                } catch (Exception e) {
                    MyDialog.error("Error in customers input file", "Line: " + line).show();
                } finally {
                    activeCustomer = null;
                }
            }
            scanner.close(); // Scanner could theoretically remain open
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Sorts the data
        data.sort((o1, o2) -> o1.compareTo(o2));
    }

    /*
     * FXML handler for actions
     */

    public ActionListener getHandleAdd() {
        return (e) -> Platform.runLater(() -> handleAdd());
    }

    @FXML
    public void handleAdd() {
        activeCustomer = null;
        tabPane.getSelectionModel().select(tabDetail);
        loadCustomer();
    }

    public ActionListener getHandleModify() {
        return (e) -> Platform.runLater(() -> handleModify());
    }

    @FXML
    public void handleModify() {
        activeCustomer = (Customer) tableView.getSelectionModel().getSelectedItem();
        if (activeCustomer == null) {
            MyDialog.error(null, "No line selected!").showAndWait();
        } else {
            tabPane.getSelectionModel().select(tabDetail);
            loadCustomer();
        }
    }

    public ActionListener getHandleDelete() {
        return (e) -> Platform.runLater(() -> handleDelete());
    }

    @FXML
    public void handleDelete() {
        activeCustomer = (Customer) tableView.getSelectionModel().getSelectedItem();
        if (activeCustomer == null) {
            MyDialog.error(null, "No line selected!").showAndWait();
        } else {
            data.remove((Object) activeCustomer);
            activeCustomer = null; // Fix
            loadCustomer();
        }
    }

    public ActionListener getHandleCancel() {
        return (e) -> Platform.runLater(() -> handleCancel());
    }

    @FXML
    public void handleCancel() {
        tabPane.getSelectionModel().select(tabList);
        activeCustomer = null;
        loadCustomer();
    }

    public ActionListener getHandleReset() {
        return (e) -> Platform.runLater(() -> handleReset());
    }

    @FXML
    public void handleReset() {
        loadCustomer();
    }

    public ActionListener getHandleSave() {
        return (e) -> Platform.runLater(() -> handleSave());
    }

    @FXML
    public void handleSave() {
        if (checkFields()) {
            if (saveCustomer()) {
                MyDialog.info(null, msg).showAndWait();
                activeCustomer = null;
                loadCustomer();
                tabPane.getSelectionModel().select(tabList);
            } else
                MyDialog.error(null, msg).showAndWait();
        } else
            MyDialog.error(header, msg).showAndWait();
    }

    @FXML
    public void handleEnterSave(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER)
            handleSave();
    }
}