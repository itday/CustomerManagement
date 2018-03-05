package application;

import javafx.beans.property.SimpleStringProperty;

public class Customer {
    private javafx.beans.property.SimpleIntegerProperty id          = new javafx.beans.property.SimpleIntegerProperty();
    private SimpleStringProperty                        familyName  = new SimpleStringProperty("");
    private SimpleStringProperty                        firstName   = new SimpleStringProperty("");
    private SimpleStringProperty                        zipCode     = new SimpleStringProperty("");
    private SimpleStringProperty                        city        = new SimpleStringProperty("");
    private SimpleStringProperty                        street      = new SimpleStringProperty("");
    private SimpleStringProperty                        houseNumber = new SimpleStringProperty("");

    public Customer(int id) {
        this.id.set(id);
    }

    public Customer(int id, String familyName, String firstName, String zipCode, String city, String street, String houseNumber) {
        this.id.set(id);
        this.familyName.set(familyName);
        this.firstName.set(firstName);
        this.zipCode.set(zipCode);
        this.city.set(city);
        this.street.set(street);
        this.houseNumber.set(houseNumber);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getFamilyName() {
        return familyName.get();
    }

    public void setFamilyName(String familyName) {
        this.familyName.set(familyName);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getZipCode() {
        return zipCode.get();
    }

    public void setZipCode(String zipCode) {
        this.zipCode.set(zipCode);
    }

    public String getCity() {
        return city.get();
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getStreet() {
        return street.get();
    }

    public void setStreet(String street) {
        this.street.set(street);
    }

    public String getHouseNumber() {
        return houseNumber.get();
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber.set(houseNumber);
    }

    public String toString() {
        return "Customer [id=" + id + ", familyName=" + familyName + ", firstName=" + firstName + ", zipCode=" + zipCode + ", city=" + city
            + ", street=" + street + ", houseNumber=" + houseNumber + "]";
    }
}