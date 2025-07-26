package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;

public class AgregarAlumnoController {

    @FXML
    private TextField nombreCompleto;
    @FXML
    private TextField dni;
    @FXML
    private TextField telefono;
    @FXML
    private TextField direccion;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnSeleccionarSocio;
    @FXML
    private TextField txtSocioSeleccionado;

    private MenuAlumnoController menuAlumnoController;
    private Socio socioSeleccionado;

    public void setMenuAlumnoController(MenuAlumnoController controller) {
        this.menuAlumnoController = controller;
    }

    @FXML
    private void agregarAlumno() {
        if (socioSeleccionado == null) {
            System.out.println("Por favor, selecciona un socio asociado.");
            return;
        }

        double montoCuota = obtenerMontoCuota();
        if (montoCuota <= 0) {
            System.out.println("Error: No se pudo obtener el monto de las cuotas.");
            return;
        }

        Alumno nuevoAlumno = new Alumno(
            nombreCompleto.getText(),
            dni.getText(),
            telefono.getText(),
            direccion.getText(),
            socioSeleccionado.getId_Socio(),
            socioSeleccionado.getNombreCompleto()
        );

        String sqlAlumno = "INSERT INTO Alumnos (nombreCompleto, dni, telefono, direccion, socio_id, socio_nombre) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlCuotas = "INSERT INTO Cuotas (fechaCreacion, monto, estado, socio_id, socio_nombre, alumno_id, alumno_nombre, fechaVencimiento) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        int anioActual = LocalDate.now().getYear();
        int mesActual = LocalDate.now().getMonthValue();

        try (Connection conn = ConexionDB.getConnection()) {
            conn.setAutoCommit(false);

            // Insertar alumno
            try (PreparedStatement pstmtAlumno = conn.prepareStatement(sqlAlumno, Statement.RETURN_GENERATED_KEYS)) {
                pstmtAlumno.setString(1, nuevoAlumno.getNombreCompleto());
                pstmtAlumno.setString(2, nuevoAlumno.getDni());
                pstmtAlumno.setString(3, nuevoAlumno.getTelefono());
                pstmtAlumno.setString(4, nuevoAlumno.getDireccion());
                pstmtAlumno.setInt(5, nuevoAlumno.getSocioId());
                pstmtAlumno.setString(6, nuevoAlumno.getSocioNombre());
                pstmtAlumno.executeUpdate();

                try (ResultSet generatedKeys = pstmtAlumno.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idAlumno = generatedKeys.getInt(1);

                        // Insertar cuotas desde el mes actual hasta diciembre
                        try (PreparedStatement pstmtCuotas = conn.prepareStatement(sqlCuotas)) {
                            for (int mes = mesActual; mes <= 12; mes++) {
                                LocalDate fechaVencimiento = LocalDate.of(anioActual, mes, 15);
                                LocalDate fechaCreacion = YearMonth.of(anioActual, mes == 1 ? 12 : mes - 1).atDay(25);

                                pstmtCuotas.setDate(1, Date.valueOf(fechaCreacion));
                                pstmtCuotas.setDouble(2, montoCuota);
                                pstmtCuotas.setString(3, "pendiente");
                                pstmtCuotas.setInt(4, nuevoAlumno.getSocioId());
                                pstmtCuotas.setString(5, nuevoAlumno.getSocioNombre());
                                pstmtCuotas.setInt(6, idAlumno);  // ← Clave foránea correcta
                                pstmtCuotas.setString(7, nuevoAlumno.getNombreCompleto());
                                pstmtCuotas.setDate(8, Date.valueOf(fechaVencimiento));
                                pstmtCuotas.addBatch();
                            }
                            pstmtCuotas.executeBatch();
                        }
                    }
                }
            }

            conn.commit();
        } catch (SQLException e) {
            System.out.println("Error al agregar el alumno y generar las cuotas: " + e.getMessage());
        }

        menuAlumnoController.cargarAlumnosDesdeBD();
        cerrarVentana();
    }

    private double obtenerMontoCuota() {
        String sql = "SELECT monto FROM ConfiguracionCuotas ORDER BY fecha_actualizacion DESC LIMIT 1";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("monto");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el monto de la cuota: " + e.getMessage());
        }
        return -1;
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) nombreCompleto.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void abrirSeleccionarSocio() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/forms/SeleccionarSocio.fxml"));
            Parent root = loader.load();

            SeleccionarSocioController controller = loader.getController();
            controller.setAgregarAlumnoController(this);

            Stage stage = new Stage();
            stage.setTitle("Seleccionar Socio");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            System.out.println("Error al abrir el formulario de selección de socio: " + e.getMessage());
        }
    }

    public void setSocioSeleccionado(Socio socio) {
        this.socioSeleccionado = socio;
        txtSocioSeleccionado.setText(socio.getNombreCompleto());
    }

    public void eliminarAlumnoYCuotas(int idAlumno) {
        String sqlEliminarAlumno = "DELETE FROM Alumnos WHERE id_alumno = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlEliminarAlumno)) {

            pstmt.setInt(1, idAlumno);
            pstmt.executeUpdate();
            System.out.println("Alumno y cuotas asociadas eliminados correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al eliminar alumno: " + e.getMessage());
        }
    }


}
