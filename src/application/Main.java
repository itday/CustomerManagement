package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends javafx.application.Application {
    private static final double HEIGHT = 700.0D;
    private static final double WEIDTH = 480.0D;

    public Main() {}

    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/application/fxml/headScene.fxml"));
            javafx.scene.layout.VBox root = (javafx.scene.layout.VBox) loader.load();
            Scene scene = new Scene(root, HEIGHT, WEIDTH);
            scene.getStylesheets().add(getClass().getResource("/application/css/headScene.css").toExternalForm());
            ((HeadSceneController) loader.getController()).draw(scene);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Customer Management");
            primaryStage.setResizable(false);
            setIcon(primaryStage);

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            primaryStage.close();
            javafx.application.Platform.exit();
        }
    }

    public static void setIcon(Stage stage) {
        stage.getIcons()
             .add(new javafx.scene.image.Image(Main.class.getResourceAsStream("/application/img/CustomerManagerLogoTransparency.png")));
    }

    public static void main(String[] args) {
        launch(args);
    }
}