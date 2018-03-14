package application;

import javax.swing.JOptionPane;

/**
 * Dialogklasse
 * 
 * @author Sebastian Müller
 * @since 14.03.2018
 */

public class MyDialog {

    public MyDialog() {}

    public static void info(String header, String content) {
        JOptionPane.showMessageDialog(null, content, header, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void error(String header, String content) {
        JOptionPane.showMessageDialog(null, content, header, JOptionPane.ERROR_MESSAGE);
    }

}
