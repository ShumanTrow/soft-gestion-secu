package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

// Clase controladora de todo lo relacionado con el Menu Principal del programa.
public class MenuController {

    @FXML
    private Button btnSocios;
    @FXML
    private Button btnAlumnos;
    @FXML
    private Button btnCuotas; // Nuevo botón para el menú de Cuotas

    // Método que abre el Menu de Socios al tocar el botón de Socios.
    @FXML
    private void abrirSociosMenu(ActionEvent event) {
        try {
            // Cargar la nueva escena de Socios
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/forms/SociosMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnSocios.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.centerOnScreen();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para abrir el menú de alumnos al tocar el botón de Alumnos.
    @FXML
    private void abrirAlumnosMenu(ActionEvent event) {
        try {
            // Cargar la nueva escena de Alumnos
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/forms/AlumnosMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnAlumnos.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.centerOnScreen();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para abrir el menú de cuotas al tocar el botón de Cuotas.
    @FXML
    private void abrirCuotasMenu(ActionEvent event) {
        try {
            // Cargar la nueva escena de Cuotas
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/forms/CuotasMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnCuotas.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.centerOnScreen();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
