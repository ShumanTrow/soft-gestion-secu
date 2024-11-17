package application;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.Calendar;

public class AgregarSocioController {

    @FXML
    private TextField nombreCompleto;
    @FXML
    private TextField dni;
    @FXML
    private TextField telefono;
    @FXML
    private TextField direccion;
    @FXML
    private TextField email;

    private MenuSociosController menuSociosController;

    public void setMenuSociosController(MenuSociosController controller) {
        this.menuSociosController = controller;
    }

    @FXML
    private void agregarSocio() {
        // Crear el objeto Socio
        Socio nuevoSocio = new Socio(
            nombreCompleto.getText(),
            dni.getText(),
            telefono.getText(),
            direccion.getText(),
            email.getText()
        );

        String sqlInsertSocio = "INSERT INTO Socios (nombreCompleto, dni, telefono, direccion, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlInsertSocio)) {

            // Usar el objeto para insertar los datos
            pstmt.setString(1, nuevoSocio.getNombreCompleto());
            pstmt.setString(2, nuevoSocio.getDni());
            pstmt.setString(3, nuevoSocio.getTelefono());
            pstmt.setString(4, nuevoSocio.getDireccion());
            pstmt.setString(5, nuevoSocio.getEmail());
            pstmt.executeUpdate();

            // Refrescar la lista en la tabla del controlador principal
            menuSociosController.cargarSociosDesdeBD();

            // Cerrar la ventana
            Stage stage = (Stage) nombreCompleto.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            System.out.println("Error al agregar socio: " + e.getMessage());
        }
    }


    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) nombreCompleto.getScene().getWindow();
        stage.close();
    }
}
