package application;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ModificarSocioController {

    @FXML
    private TextField txtNombreCompleto;
    @FXML
    private TextField txtDni;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtEmail;

    private Socio socioSeleccionado;
    private MenuSociosController menuSociosController;

    public void setSocio(Socio socio) {
        this.socioSeleccionado = socio;
        txtNombreCompleto.setText(socio.getNombreCompleto());
        txtDni.setText(socio.getDni());
        txtTelefono.setText(socio.getTelefono());
        txtDireccion.setText(socio.getDireccion());
        txtEmail.setText(socio.getEmail());
    }

    public void setMenuSociosController(MenuSociosController controller) {
        this.menuSociosController = controller;
    }

    @FXML
    private void guardarCambios() {
        if (socioSeleccionado != null) {
            // Actualizamos los datos del socio con los valores del formulario
            socioSeleccionado.setNombreCompleto(txtNombreCompleto.getText());
            socioSeleccionado.setDni(txtDni.getText());  // Este sí puede cambiar si lo desea el admin
            socioSeleccionado.setTelefono(txtTelefono.getText());
            socioSeleccionado.setDireccion(txtDireccion.getText());
            socioSeleccionado.setEmail(txtEmail.getText());

            // Usamos id_Socio como el identificador para la actualización
            String sql = "UPDATE Socios SET nombreCompleto = ?, telefono = ?, direccion = ?, email = ?, dni = ? WHERE id_Socio = ?";
            try (Connection conn = ConexionDB.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, socioSeleccionado.getNombreCompleto());
                pstmt.setString(2, socioSeleccionado.getTelefono());
                pstmt.setString(3, socioSeleccionado.getDireccion());
                pstmt.setString(4, socioSeleccionado.getEmail());
                pstmt.setString(5, socioSeleccionado.getDni());  // Permitimos que el DNI sea actualizado
                pstmt.setInt(6, socioSeleccionado.getId_Socio());  // Usamos el id_Socio para la actualización
                pstmt.executeUpdate();

                // Recargamos los socios en la tabla después de la actualización
                menuSociosController.cargarSociosDesdeBD();
            } catch (SQLException e) {
                System.out.println("Error al actualizar socio: " + e.getMessage());
            }

            // Cerramos la ventana después de guardar los cambios
            Stage stage = (Stage) txtNombreCompleto.getScene().getWindow();
            stage.close();
        }
    }


    @FXML
    private void cerrarFormulario() {
        Stage stage = (Stage) txtNombreCompleto.getScene().getWindow();
        stage.close();
    }
}
