package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

	//Clase controladora de todo lo relacionado con el Menu Principal del programa.
public class MenuController {

    @FXML
    private Button btnSocios;

    //Método que abre el Menu de Socios al tocar el botón de Socios.
    @FXML
    private void abrirSociosMenu(ActionEvent event) {
        try {
            // Cargar la nueva escena
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/forms/SociosMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnSocios.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
