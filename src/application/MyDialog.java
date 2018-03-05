package application;

import application.Main;
import application.MyDialog;

import java.util.concurrent.Semaphore;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class MyDialog {
    private static Semaphore resultFinish = new Semaphore(0);
    private static Semaphore semaphore    = new Semaphore(1);

    public static boolean info(String header, String content) {
        try {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("InformationDialog");
                    alert.setHeaderText(header);
                    alert.setContentText(content);
                    setIcon(alert.getDialogPane());

                    alert.showAndWait();
                }
            };
            semaphore.acquire();

            // can it be run directly?
            if (Platform.isFxApplicationThread())
                runnable.run();
            else
                Platform.runLater(runnable);

            return true;
        } catch (InterruptedException e) {
            return false; // ungünstige Lösung
        } finally {
            semaphore.release();
        }
    }

    public static void error(String header, String error) {
        try {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("ErrorDialog");
                    alert.setHeaderText(header);
                    alert.setContentText(error);
                    setIcon(alert.getDialogPane());
                    // alert.initModality(Modality.APPLICATION_MODAL);
                    alert.getDialogPane().setExpanded(true);
                    alert.showAndWait();
                    resultFinish.release();
                }
            };
            semaphore.acquire();

            // can it be run directly?
            if (Platform.isFxApplicationThread())
                runnable.run();
            else
                Platform.runLater(runnable);

            try {
                resultFinish.acquire();
            } catch (InterruptedException e) {
                return; // ungünstige Lösung
            }
        } catch (InterruptedException e) {} finally {
            semaphore.release();
        }
    }

    private static void setIcon(DialogPane pane) {
        Main.setIcon((Stage) pane.getScene().getWindow());
    }

}