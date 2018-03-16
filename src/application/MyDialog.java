package application;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Dialog class
 * 
 * @author Sebastian Müller
 * @since 16.03.2018
 */

public class MyDialog {

    public synchronized static Alert info(String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information dialog");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initModality(Modality.APPLICATION_MODAL);
        setIcon(alert.getDialogPane());
        return alert;
    }

    public synchronized static Alert error(String header, String error) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error dialog");
        alert.setHeaderText(header);
        alert.setContentText(error);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.getDialogPane().setExpanded(true);
        setIcon(alert.getDialogPane());
        return alert;
    }

    private synchronized static void setIcon(DialogPane pane) {
        Main.setIcon((Stage) pane.getScene().getWindow());
    }
}