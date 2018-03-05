package application;

import application.Customer;
import application.MyDialog;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class HeadSceneController {

    @FXML TabPane tabPane;
    @FXML Tab     tabList;
    @FXML Tab     tabDetail;

    /*
     * tabList
     */
    @FXML TableView<Customer>            tableView;
    private ObservableList<Customer>     data;
    @FXML TableColumn<Customer, Integer> idTableColumn;
    @FXML TableColumn<Customer, String>  houseNumberTableColumn;

    private int entryCounter = 0;

    /*
     * detailsTab
     */
    private Customer activeCustomer;

    @FXML Pane       detailsPane;
    @FXML BorderPane detailsBorderPane;

    @FXML TextField familyNameField;
    @FXML TextField streetField;
    @FXML TextField cityField;
    @FXML TextField firstNameField;
    @FXML TextField houseNumberField;
    @FXML TextField zipCodeField;

    @FXML Button saveDetails;

    public static final boolean saveButtonEnabledByDefault = true;
    public static final String  nameCheckPattern           = "^[A-ZÄÖÜ][a-züöä]{2,19}((\\-|\\s)[A-ZÄÖÜ][a-zäöüc]{2,19}){0,3}$";
    public static final String  houseNumberCheckPattern    = "^\\d{1,3}[a-z]?$";
    String                      msg;

    /*
     * GUI actions
     */

    public void initialize() {
        data = tableView.getItems();

        // examples
        data.add(new Customer(++entryCounter, "Musterman", "Max", "12345", "München", "Dachauer", "1"));
        data.add(new Customer(++entryCounter, "Musterman", "Max", "12345", "München", "Dachauer", "1b"));
        data.add(new Customer(++entryCounter, "Musterman", "Max", "12345", "München", "Dachauer", "12b"));
        data.add(new Customer(++entryCounter, "Musterman", "Max", "12345", "München", "Dachauer", "122b"));
        data.add(new Customer(++entryCounter, "Musterman", "Max", "12345", "München", "Dachauer", "1c"));
        data.add(new Customer(++entryCounter, "Musterman", "Max", "12345", "München", "Dachauer", "2"));
        data.add(new Customer(++entryCounter, "Musterman", "Max", "12345", "München", "Dachauer", "3"));
        data.add(new Customer(++entryCounter, "Musterman", "Max", "12345", "München", "Dachauer", "55"));

        // tableView resizing
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        idTableColumn.setMaxWidth(30.0);
        idTableColumn.setMinWidth(30.0);
        houseNumberTableColumn.setMaxWidth(100.0);
        houseNumberTableColumn.setMinWidth(100.0);

        // enable saveDetails button
        if (!saveButtonEnabledByDefault) {
            ChangeListener<String> listener = (a, b, c) -> {
                saveDetails.setDisable(!checkFields());
            };
            familyNameField.textProperty().addListener(listener);
            streetField.textProperty().addListener(listener);
            cityField.textProperty().addListener(listener);
            firstNameField.textProperty().addListener(listener);
            houseNumberField.textProperty().addListener(listener);
            zipCodeField.textProperty().addListener(listener);
        } else
            saveDetails.setDisable(false);
    }

    public void draw(Scene scene) {
        detailsPane.setMaxHeight(scene.getHeight());
        detailsBorderPane.setMaxWidth(scene.getWidth());
    }

    /*
     * Customer actions
     */

    public void loadCustomer() {
        if (activeCustomer == null) {
            familyNameField.requestFocus();
            familyNameField.setText("");
            streetField.setText("");
            cityField.setText("");
            firstNameField.setText("");
            houseNumberField.setText("");
            zipCodeField.setText("");
        } else {
            familyNameField.requestFocus();
            familyNameField.setText(activeCustomer.getFamilyName());
            streetField.setText(activeCustomer.getStreet());
            cityField.setText(activeCustomer.getCity());
            firstNameField.setText(activeCustomer.getFirstName());
            houseNumberField.setText(activeCustomer.getHouseNumber());
            zipCodeField.setText(activeCustomer.getZipCode());
        }
    }

    public boolean saveCustomer() {
        if (activeCustomer == null) {
            activeCustomer = new Customer(++entryCounter);
            data.add(activeCustomer);
        }

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

        // fix update issue
        data.set(data.indexOf(activeCustomer), activeCustomer);

        return !equal;
    }

    public boolean checkFields() {

        String tmp = familyNameField.getText().trim();
        if (tmp.length() < 3 || !nameCheck(tmp)) {
            msg = "Incorrect family name!";
            return false;
        }

        tmp = streetField.getText().trim();
        if (tmp.length() < 3 || !nameCheck(tmp)) {
            msg = "Incorrect street name!";
            return false;
        }

        tmp = cityField.getText().trim();
        if (tmp.length() < 3 || !nameCheck(tmp)) {
            msg = "Incorrect city name!";
            return false;
        }

        tmp = firstNameField.getText().trim();
        if (tmp.length() < 3 || !nameCheck(tmp)) {
            msg = "Incorrect first name!";
            return false;
        }

        tmp = houseNumberField.getText().trim();
        if (!houseNummberCheck(tmp)) {
            msg = "Incorrect house number!";
            return false;
        }

        tmp = zipCodeField.getText().trim();
        if (tmp.length() != 5 || !tmp.chars().allMatch(Character::isDigit)) {
            msg = "Incorrect zip code!";
            return false;
        }

        return true;
    }

    private boolean nameCheck(String tmp) {
        Matcher m = Pattern.compile(nameCheckPattern).matcher(tmp);
        return m.find();
    }

    private boolean houseNummberCheck(String tmp) {
        Matcher m = Pattern.compile(houseNumberCheckPattern).matcher(tmp);
        return m.find();
    }

    /*
     * FXML handler for actions
     */

    @FXML
    public void handleAdd() {
        activeCustomer = null;
        tabPane.getSelectionModel().select(tabDetail);
        loadCustomer();
    }

    @FXML
    public void handleModify() {
        activeCustomer = (Customer) tableView.getSelectionModel().getSelectedItem();
        if (activeCustomer == null) {
            MyDialog.error(null, "No line selected!");
        } else {
            tabPane.getSelectionModel().select(tabDetail);
            loadCustomer();
        }
    }

    @FXML
    public void handleDelete() {
        activeCustomer = (Customer) tableView.getSelectionModel().getSelectedItem();
        if (activeCustomer == null) {
            MyDialog.error(null, "No line selected!");
        } else {
            data.remove((Object) activeCustomer);
        }
    }

    @FXML
    public void handleCancle() {
        tabPane.getSelectionModel().select(tabList);
        activeCustomer = null;
        loadCustomer();
    }

    @FXML
    public void handleReset() {
        loadCustomer();
    }

    @FXML
    public void handleSave() {
        if (checkFields()) {
            if (saveCustomer()) {
                MyDialog.info(null, "Customer was saved successfully.");
            } else {
                MyDialog.error(null, "Nothing changed!");
            }
            activeCustomer = null;
            loadCustomer();
            tabPane.getSelectionModel().select(tabList);
        } else {
            MyDialog.error(null, msg);
        }
    }
}