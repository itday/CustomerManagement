package application.objects;

import java.util.Arrays;
import java.util.Vector;

/**
 * Data class for customers
 *
 * @author Sebastian Müller
 * @since 16.03.2018
 */

public class Customer implements Comparable<Customer>, Cloneable {

    private Vector<String> vector;

    public Customer() {
        vector = new Vector<>(7);
        vector.addAll(Arrays.asList("-1", "", "", "", "", "", "")); // Fix
    }

    public Customer(int id, String familyName, String firstName, String zipCode, String city, String street, String houseNumber) {
        this();
        vector.set(0, "" + id); // 0
        vector.set(1, familyName); // 1
        vector.set(2, firstName); // 2
        vector.set(3, zipCode); // 3
        vector.set(4, city); // 4
        vector.set(5, street); // 5
        vector.set(6, houseNumber); // 6
    }

    /**
     * Returns the data from this object.
     * 
     * @return Vector (length == 7) with data from this object
     */

    public Vector<String> getVector() {
        return vector;
    }

    /**
     * Sets the data.
     * 
     * @param customer Vector (length == 7) with data
     */

    public void setVektor(Vector<String> customer) {
        if (customer == null || customer.size() != 7)
            return;

        vector = customer;
    }

    public int getId() { // 0
        return Integer.parseInt(vector.get(0));
    }

    public void setId(int id) {
        vector.set(0, "" + id);
    }

    public String getFamilyName() { // 1
        return vector.get(1);
    }

    public void setFamilyName(String familyName) {
        vector.set(1, familyName);
    }

    public String getFirstName() { // 2
        return vector.get(2);
    }

    public void setFirstName(String firstName) {
        vector.set(2, firstName);
    }

    public String getZipCode() { // 3
        return vector.get(3);
    }

    public void setZipCode(String zipCode) {
        vector.set(3, zipCode);
    }

    public String getCity() { // 4
        return vector.get(4);
    }

    public void setCity(String city) {
        vector.set(4, city);
    }

    public String getStreet() { // 5
        return vector.get(5);
    }

    public void setStreet(String street) {
        vector.set(5, street);
    }

    public String getHouseNumber() { // 6
        return vector.get(6);
    }

    public void setHouseNumber(String houseNumber) {
        vector.set(6, houseNumber);
    }

    @Override
    public Customer clone() {
        return new Customer(getId(), getFamilyName(), getFirstName(), getZipCode(), getCity(), getStreet(), getHouseNumber());
    }

    @Override
    public int compareTo(Customer c) {
        if (c.getId() == getId())
            return 0;
        else if (c.getId() > getId())
            return -1;
        else
            return 1;
    }

    @Override
    public String toString() {
        return "Customer [id: " + getId() + ", familyName: " + getFamilyName() + ", firstName: " + getFirstName() + ", zipCode: "
            + getZipCode() + ", city: " + getCity() + ", street: " + getStreet() + ", houseNumber: " + getHouseNumber() + "]";
    }

    /**
     * toString for checks without variable id.
     * 
     * @return String for checking
     */
    public String toSimpleStringWithoutId() {
        return getFamilyName() + getFirstName() + getZipCode() + getCity() + getStreet() + getHouseNumber();
    }
}