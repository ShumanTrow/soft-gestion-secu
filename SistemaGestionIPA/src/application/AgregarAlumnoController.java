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
    private TextField nombreCompleto;    // Campo para el nombre completo del alumno
    @FXML
    private TextField dni;               // Campo para el DNI del alumno
    @FXML
    private TextField telefono;          // Campo para el teléfono del alumno
    @FXML
    private TextField direccion;         // Campo para la dirección del alumno
    @FXML
    private Button btnAgregar;           // Botón para agregar el alumno
    @FXML
    private Button btnCancelar;          // Botón para cancelar la operación
    @FXML
    private Button btnSeleccionarSocio;  // Botón para seleccionar el socio asociado
    @FXML
    private TextField txtSocioSeleccionado; // Campo de texto para mostrar el nombre del socio seleccionado

    private MenuAlumnoController menuAlumnoController; // Controlador del menú de alumnos
    private Socio socioSeleccionado;                  // Socio seleccionado para el alumno

    public void setMenuAlumnoController(MenuAlumnoController controller) {
        this.menuAlumnoController = controller; // Establecer el controlador principal
    }

    @FXML
    private void agregarAlumno() {
        if (socioSeleccionado == null) {
            System.out.println("Por favor, selecciona un socio asociado.");
            return;
        }

        // Obtener el monto actual desde ConfiguracionCuotas
        double montoCuota = obtenerMontoCuota();
        if (montoCuota <= 0) {
            System.out.println("Error: No se pudo obtener el monto de las cuotas.");
            return;
        }

        // Creación del nuevo objeto Alumno
        Alumno nuevoAlumno = new Alumno(
            nombreCompleto.getText(),
            dni.getText(),
            telefono.getText(),
            direccion.getText(),
            socioSeleccionado.getId_Socio(),
            socioSeleccionado.getNombreCompleto()
        );

        String sqlAlumno = "INSERT INTO Alumnos (nombreCompleto, dni, telefono, direccion, socio_id, socio_nombre) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlCuotas = "INSERT INTO Cuotas (fechaCreacion, monto, estado, socio_id, socio_nombre, alumno_nombre, fechaVencimiento) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConnection()) {
            conn.setAutoCommit(false); // Inicia una transacción

            // Insertar el alumno
            try (PreparedStatement pstmtAlumno = conn.prepareStatement(sqlAlumno, Statement.RETURN_GENERATED_KEYS)) {
                pstmtAlumno.setString(1, nuevoAlumno.getNombreCompleto());
                pstmtAlumno.setString(2, nuevoAlumno.getDni());
                pstmtAlumno.setString(3, nuevoAlumno.getTelefono());
                pstmtAlumno.setString(4, nuevoAlumno.getDireccion());
                pstmtAlumno.setInt(5, nuevoAlumno.getSocioId());
                pstmtAlumno.setString(6, nuevoAlumno.getSocioNombre());
                pstmtAlumno.executeUpdate();

                // Obtener el ID generado del alumno
                try (ResultSet generatedKeys = pstmtAlumno.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idAlumno = generatedKeys.getInt(1);

                        // Insertar las cuotas (febrero a diciembre)
                        try (PreparedStatement pstmtCuotas = conn.prepareStatement(sqlCuotas)) {
                            for (int mes = 2; mes <= 12; mes++) {
                                LocalDate fechaVencimiento = LocalDate.of(2024, mes, 15); // Vencimiento el día 15 del mes actual
                                LocalDate fechaCreacion;

                                // Calcular fecha de creación como el 25 del mes anterior
                                if (mes == 2) {
                                    fechaCreacion = LocalDate.of(2024, 1, 25); // Enero del mismo año
                                } else {
                                    YearMonth mesAnterior = YearMonth.of(2024, mes - 1);
                                    fechaCreacion = mesAnterior.atDay(25);
                                }

                                pstmtCuotas.setDate(1, Date.valueOf(fechaCreacion));         // Fecha de creación
                                pstmtCuotas.setDouble(2, montoCuota);                       // Monto
                                pstmtCuotas.setString(3, "pendiente");                     // Estado
                                pstmtCuotas.setInt(4, nuevoAlumno.getSocioId());           // Socio ID
                                pstmtCuotas.setString(5, nuevoAlumno.getSocioNombre());    // Socio nombre
                                pstmtCuotas.setString(6, nuevoAlumno.getNombreCompleto()); // Alumno nombre
                                pstmtCuotas.setDate(7, Date.valueOf(fechaVencimiento));    // Fecha de vencimiento
                                pstmtCuotas.addBatch();                                    // Añadir al batch
                            }
                            pstmtCuotas.executeBatch(); // Ejecutar batch
                        }
                    }
                }
            }

            conn.commit(); // Confirmar la transacción
        } catch (SQLException e) {
            System.out.println("Error al agregar el alumno y generar las cuotas: " + e.getMessage());
        }

        // Actualizar la tabla en el controlador principal
        menuAlumnoController.cargarAlumnosDesdeBD();
        cerrarVentana();
    }

    /**
     * Método para obtener el monto actual de las cuotas desde ConfiguracionCuotas.
     * @return Monto de la cuota, o -1 si ocurre un error.
     */
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
        return -1; // En caso de error
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
        String sqlEliminarCuotas = "DELETE FROM Cuotas WHERE alumno_nombre = (SELECT nombreCompleto FROM Alumnos WHERE id_alumno = ?)";
        String sqlEliminarAlumno = "DELETE FROM Alumnos WHERE id_alumno = ?";

        try (Connection conn = ConexionDB.getConnection()) {
            conn.setAutoCommit(false); // Inicia una transacción

            // Eliminar cuotas asociadas
            try (PreparedStatement pstmtCuotas = conn.prepareStatement(sqlEliminarCuotas)) {
                pstmtCuotas.setInt(1, idAlumno);
                pstmtCuotas.executeUpdate();
            }

            // Eliminar alumno
            try (PreparedStatement pstmtAlumno = conn.prepareStatement(sqlEliminarAlumno)) {
                pstmtAlumno.setInt(1, idAlumno);
                pstmtAlumno.executeUpdate();
            }

            conn.commit(); // Confirmar la transacción
        } catch (SQLException e) {
            System.out.println("Error al eliminar el alumno y sus cuotas: " + e.getMessage());
        }
    }
}
