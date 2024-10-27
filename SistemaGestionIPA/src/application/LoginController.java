package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

	//Clase que controla lo relacionado al Login.
public class LoginController {

    @FXML
    private TextField txtDNI;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button btnIniciarSesion;

    // Simulación de un array de admins.
    private String[][] admins = {{"444", "123"}};

    @FXML
    private void iniciarSesion(ActionEvent event) {
        String dniIngresado = txtDNI.getText();
        String passwordIngresado = txtPassword.getText();

        // Comprueba si el DNI y contraseña son correctos.
        boolean loginExitoso = false;
        for (String[] usuario : admins) {
            if (usuario[0].equals(dniIngresado) && usuario[1].equals(passwordIngresado)) {
                loginExitoso = true;
                break;
            }
        }
        // Carga el menú si el login es exitoso
        if (loginExitoso) {
            
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/forms/Menu.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) btnIniciarSesion.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Muestra un mensaje de error si los datos son incorrectos.
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error de Autenticación");
            alerta.setHeaderText("Datos incorrectos");
            alerta.setContentText("El documento de identidad o la contraseña son incorrectos.");
            alerta.showAndWait();
        }
    }
}
