package application;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implements checks for text input fields.
 * 
 * @author Sebastian Müller
 * @since 14.03.2018
 */

public class Check {

    /*
     * Basic patterns
     */

    private static final String letters = "[a-zA-ZÜÖÄüöäß]{3,20}";
    private static final String word    = "[A-ZÄÖÜ][a-züöäß]{2,19}";

    private static String delimiter     = "(\\-|\\s)";
    private static String optionalPoint = "(\\.)?";

    static { // Do something for the defects
        if (Main.enableDefects)
            delimiter += "+";
    }

    /*
     * Advanced pattern
     */

    /**
     * Single,double,triple,quad words separated by a hyphen or a space are
     * allowed.<br>
     * 3 >= words.length <= 20<br>
     * Included symbols: a-zA-ZÜÖÄüöäß
     * 
     * @see {@link Main#enableDefects} -> delimiters
     */
    private static String nameCheckPattern = "^" + letters + "(" + delimiter + letters + "){0,3}$";

    /**
     * Single,double,triple,quad words separated by a hyphen or a space are
     * allowed.<br>
     * All words have to start with a upper case letter.<br>
     * 3 >= words.length <= 20<br>
     * Included symbols: a-zA-ZÜÖÄüöäß
     * 
     * @see {@link Main#enableDefects} -> delimiters
     */
    private static final String nameCheckPatternUpperCaseLetterWordBeginning = "^" + word + "(" + delimiter + word + "){0,3}$";

    /**
     * Single,double,triple,quad words separated by a hyphen or a space are
     * allowed.<br>
     * 3 >= words.length <= 20<br>
     * Included symbols: a-zA-ZÜÖÄüöäß.
     * 
     * @see {@link Main#enableDefects} -> delimiters
     */
    private static String streetCheckPattern = "^" + letters + optionalPoint + "(" + delimiter + letters + optionalPoint + "){0,3}$";

    /**
     * Single,double,triple,quad words separated by a hyphen or a space are
     * allowed.<br>
     * All words have to start with a upper case letter.<br>
     * 3 >= words.length <= 20<br>
     * Included symbols: a-zA-ZÜÖÄüöäß.
     * 
     * @see {@link Main#enableDefects} -> delimiters
     */
    private static final String streetCheckPatternUpperCaseLetterWordBeginning = "^" + word + optionalPoint + "(" + delimiter + word
        + optionalPoint + "){0,3}$";

    /**
     * Checks a house number.<br>
     * Valid: 1, 12, 123, 1a, 12a, 123a, ...<br>
     * Invalid: a, 1aa, 12aa, ...<br>
     * Pattern: digit{1-3}letter{0-1}
     */
    private static final String houseNumberCheckPattern = "^\\d{1,3}[a-z]?$";

    public static String name(String name, String prefix) {
        name = name.trim();

        if (name.length() == 0)
            if (!Main.enableDefects)
                return prefix + " is empty!\n";
            else
                return "";

        if (name.length() < 3)
            return prefix + " is to short!\n";

        if (name.length() > 39)
            return prefix + " is to long!\n";

        if (!nameRegexHelper(name))
            return prefix + " doesn't fit.\n";

        return "";
    }

    public static boolean name(String name) {
        return name(name, "XXX").isEmpty();
    }

    public static String street(String street, String prefix) {
        street = street.trim();

        if (street.length() == 0)
            if (!Main.enableDefects)
                return prefix + " is empty!\n";
            else
                return "";

        if (street.length() < 3)
            return prefix + " is to short!\n";

        if (street.length() > 39)
            return prefix + " is to long!\n";

        if (!streetRegexHelper(street))
            return prefix + " doesn't fit.\n";

        return "";
    }

    public static boolean street(String name) {
        return name(name, "XXX").isEmpty();
    }

    public static String houseNumber(String houseNumber, String prefix) {
        if (houseNumber.length() == 0)
            if (!Main.enableDefects)
                return prefix + " is empty!\n";
            else
                return "";

        if (houseNumber.length() < 1)
            return prefix + " is to short!\n";

        if (houseNumber.length() > 4)
            return prefix + " is to long!\n";

        if (!houseNummberRegexHelper(houseNumber))
            return prefix + " doesn't fit.\n";

        return "";
    }

    public static boolean houseNumber(String houseNumber) {
        return houseNumber(houseNumber, "XXX").isEmpty();
    }

    public static String zipCode(String zipCode, String prefix) {
        if (zipCode.length() == 0)
            if (!Main.enableDefects)
                return prefix + " is empty!\n";
            else
                return "";

        if (zipCode.length() < 5)
            return prefix + " is to short!\n";

        if (zipCode.length() > 5)
            return prefix + " is to long!\n";

        if (!zipCode.chars().allMatch(Character::isDigit))
            return prefix + " contains symbols other than digits.\n";

        return "";
    }

    public static boolean zipCode(String zipCode) {
        return zipCode(zipCode, "XXX").isEmpty();
    }

    private static boolean nameRegexHelper(String tmp) {
        if (Main.hardNameChecking) {
            return Pattern.compile(nameCheckPatternUpperCaseLetterWordBeginning).matcher(tmp).find();
        } else {
            Matcher m = Pattern.compile(nameCheckPattern).matcher(tmp);
            Matcher mm = Pattern.compile(word).matcher(tmp);
            return m.find() && mm.find();
        }
    }

    private static boolean streetRegexHelper(String tmp) {
        if (Main.hardNameChecking) {
            return Pattern.compile(streetCheckPatternUpperCaseLetterWordBeginning).matcher(tmp).find();
        } else {
            Matcher m = Pattern.compile(streetCheckPattern).matcher(tmp);
            Matcher mm = Pattern.compile(word).matcher(tmp);
            return m.find() && mm.find();
        }
    }

    private static boolean houseNummberRegexHelper(String tmp) {
        Matcher m = Pattern.compile(houseNumberCheckPattern).matcher(tmp);
        return m.find();
    }

}