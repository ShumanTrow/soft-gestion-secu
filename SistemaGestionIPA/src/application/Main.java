package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage stage;

    //Inicia la aplicacion desde el form llamado Login.
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/application/forms/Login.fxml"));
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Sistema de Gestión");
        stage.show();
    }

    public static void setRoot(String fxml) throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource("/application/forms/" + fxml + ".fxml"));
        stage.getScene().setRoot(root);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
