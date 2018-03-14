package application;

import java.util.Arrays;
import java.util.Vector;

public class Customer implements Comparable<Customer> {

    private Vector<String> vector;

    public Customer() {
        vector = new Vector<>(7);
        vector.addAll(Arrays.asList("-1", "", "", "", "", "", "")); // Fix
    }

    public Customer(int id, String familyName, String firstName, String zipCode, String city, String street, String houseNumber) {
        this();
        vector.add(0, "" + id); // 0
        vector.add(1, familyName); // 1
        vector.add(2, firstName); // 2
        vector.add(3, zipCode); // 3
        vector.add(4, city); // 4
        vector.add(5, street); // 5
        vector.add(6, houseNumber); // 6
    }

    public Vector<String> getVector() {
        return vector;
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

    public String toSimpleStringWithoutId() {
        return getFamilyName() + getFirstName() + getZipCode() + getCity() + getStreet() + getHouseNumber();
    }
}