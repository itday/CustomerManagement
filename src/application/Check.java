package application;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Check {

    private static final String nameCheckPattern                                       = "^[a-zA-ZÜÖÄüöäß]{3,19}((\\-|\\s)[A-ZÄÖÜa-zäöüß]{3,19}){0,3}$";
    private static final String nameCheckPatternUpperCaseLetterWordBeginning           = "^[A-ZÄÖÜ][a-züöäß]{2,19}((\\-|\\s)[A-ZÄÖÜ][a-zäöüß]{2,19}){0,3}$";
    private static final String nameCheckPatternAtLeastOneUpperCaseLetterWordBeginning = "[A-ZÄÖÜ][a-züöäß]{2,19}";
    private static final String houseNumberCheckPattern                                = "^\\d{1,3}[a-z]?$";

    public static String name(String name, String prefix) {
        name = name.trim();

        if (name.length() == 0)
            return prefix + " is empty!\n";

        if (name.length() < 3)
            return prefix + " is to short!\n";

        if (name.length() > 39)
            return prefix + " is to long!\n";

        if (!nameRegexHelper(name))
            return prefix + " doesn't match.\n";

        return "";
    }

    public static boolean name(String name) {
        return name(name, "XXX").isEmpty();
    }

    public static String houseNumber(String houseNumber, String prefix) {
        if (houseNumber.length() == 0)
            return prefix + " is empty!\n";

        if (houseNumber.length() < 1)
            return prefix + " is to short!\n";

        if (houseNumber.length() > 4)
            return prefix + " is to long!\n";

        if (!houseNummberRegexHelper(houseNumber))
            return prefix + " doesn't match.\n";

        return "";
    }

    public static boolean houseNumber(String houseNumber) {
        return houseNumber(houseNumber, "XXX").isEmpty();
    }

    public static String zipCode(String zipCode, String prefix) {
        if (zipCode.length() == 0)
            return prefix + " is empty!\n";

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
            Matcher mm = Pattern.compile(nameCheckPatternAtLeastOneUpperCaseLetterWordBeginning).matcher(tmp);
            return m.find() && mm.find();
        }
    }

    private static boolean houseNummberRegexHelper(String tmp) {
        Matcher m = Pattern.compile(houseNumberCheckPattern).matcher(tmp);
        return m.find();
    }

}
