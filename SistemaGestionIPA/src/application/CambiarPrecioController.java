package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class CambiarPrecioController {

    @FXML
    private TextField txtNuevoPrecio;
    @FXML
    private Button btnAsignarPrecio;

    @FXML
    private void initialize() {
        btnAsignarPrecio.setOnAction(event -> asignarNuevoPrecio());
    }

    @FXML
    private void asignarNuevoPrecio() {
        try {
            double nuevoPrecio = Double.parseDouble(txtNuevoPrecio.getText());
            actualizarPrecioCuotasPendientes(nuevoPrecio);
            actualizarPrecioConfiguracionCuotas(nuevoPrecio);

            // Cerrar la ventana actual
            Stage stage = (Stage) btnAsignarPrecio.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            System.out.println("Por favor, ingrese un precio válido.");
        }
    }

    private void actualizarPrecioCuotasPendientes(double nuevoPrecio) {
        String sql = "UPDATE Cuotas SET monto = ? WHERE estado = 'pendiente'";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, nuevoPrecio);
            pstmt.executeUpdate();
            System.out.println("Precio actualizado correctamente para las cuotas pendientes.");
        } catch (SQLException e) {
            System.out.println("Error al actualizar el precio de las cuotas: " + e.getMessage());
        }
    }

    private void actualizarPrecioConfiguracionCuotas(double nuevoPrecio) {
        String sql = "UPDATE ConfiguracionCuotas SET monto = ?, fecha_actualizacion = ? WHERE id_config = 1"; // Asumiendo que solo hay un registro de configuración
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, nuevoPrecio);
            pstmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            pstmt.executeUpdate();
            System.out.println("Precio actualizado correctamente en ConfiguracionCuotas.");
        } catch (SQLException e) {
            System.out.println("Error al actualizar el precio en ConfiguracionCuotas: " + e.getMessage());
        }
    }
}
