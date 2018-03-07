package application;

import java.util.Map.Entry;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    private static final double HEIGHT = 700.0D;
    private static final double WEIDTH = 480.0D;

    public static boolean saveButtonEnabled = true;  // by true: saving is always possible
                                                     // by false: saving is only by correct values possible
    public static boolean hardNameChecking  = false; // by true: forbidden: "von Helen" allowed: "Von Helen"
                                                     // by false: forbidden: "von helen" allowed: "von Helen"

    public Main() {}

    public void start(Stage primaryStage) {
        setParameters();

        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/application/fxml/HeadScene.fxml"));

            VBox root = (VBox) loader.load();
            Scene scene = new Scene(root, HEIGHT, WEIDTH);
            scene.getStylesheets().add(Main.class.getResource("/application/css/headScene.css").toExternalForm());
            setUserAgentStylesheet(Application.STYLESHEET_MODENA);
            ((HeadSceneController) loader.getController()).draw(scene);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Customer Management");
            primaryStage.setResizable(false);
            setIcon(primaryStage);

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            primaryStage.close();
            Platform.exit();
        }
    }

    private void setParameters() {
        for (Entry<String, String> e : getParameters().getNamed().entrySet()) {
            switch (e.getKey().toLowerCase()) {
                case "hardnamechecking":
                    hardNameChecking = Boolean.parseBoolean(e.getValue().toLowerCase().trim());
                    break;

                case "savebuttonenabled":
                    saveButtonEnabled = Boolean.parseBoolean(e.getValue().toLowerCase().trim());
                    break;
            }

        }
    }

    public static void setIcon(Stage stage) {
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/application/img/CustomerManagerLogoTransparency.png")));
    }

    public static void main(String[] args) {
        launch(args);
    }
}