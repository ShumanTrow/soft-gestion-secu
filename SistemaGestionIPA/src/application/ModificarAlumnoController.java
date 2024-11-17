package application;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;

public class ModificarAlumnoController {

    @FXML
    private TextField txtNombreCompleto;
    @FXML
    private TextField txtDni;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtDireccion;
    @FXML
    private ComboBox<Socio> comboSocio; // ComboBox para seleccionar el socio asociado

    private Alumno alumnoSeleccionado;
    private MenuAlumnoController menuAlumnoController;
    private ObservableList<Socio> listaSocios = FXCollections.observableArrayList();

    public void setAlumno(Alumno alumno) {
        this.alumnoSeleccionado = alumno;
        cargarSociosEnComboBox(); // Cargar los socios en el ComboBox

        if (txtNombreCompleto != null && txtDni != null && txtTelefono != null && txtDireccion != null) {
            txtNombreCompleto.setText(alumno.getNombreCompleto());
            txtDni.setText(alumno.getDni());
            txtTelefono.setText(alumno.getTelefono());
            txtDireccion.setText(alumno.getDireccion());

            // Seleccionar el socio asociado en el ComboBox
            for (Socio socio : listaSocios) {
                if (socio.getId_Socio() == alumno.getSocioId()) {
                    comboSocio.setValue(socio);
                    break;
                }
            }
        } else {
            System.out.println("Error: Los campos de texto no están inicializados correctamente.");
        }
    }

    public void setMenuAlumnoController(MenuAlumnoController controller) {
        this.menuAlumnoController = controller;
    }

    private void cargarSociosEnComboBox() {
        String sql = "SELECT * FROM Socios";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Socio socio = new Socio(
                    rs.getInt("id_Socio"),
                    rs.getString("nombreCompleto"),
                    rs.getString("dni"),
                    rs.getString("telefono"),
                    rs.getString("direccion"),
                    rs.getString("email")
                );
                listaSocios.add(socio);
            }
            comboSocio.setItems(listaSocios);
        } catch (SQLException e) {
            System.out.println("Error al cargar socios en el ComboBox: " + e.getMessage());
        }
    }

    @FXML
    private void guardarCambios() {
        if (alumnoSeleccionado != null) {
            alumnoSeleccionado.setNombreCompleto(txtNombreCompleto.getText());
            alumnoSeleccionado.setDni(txtDni.getText());
            alumnoSeleccionado.setTelefono(txtTelefono.getText());
            alumnoSeleccionado.setDireccion(txtDireccion.getText());

            Socio socioSeleccionado = comboSocio.getSelectionModel().getSelectedItem();
            if (socioSeleccionado != null) {
                alumnoSeleccionado.setSocioId(socioSeleccionado.getId_Socio());
            }

            String sql = "UPDATE Alumnos SET nombreCompleto = ?, telefono = ?, direccion = ?, curso = ?, socio_id = ? WHERE dni = ?";
            try (Connection conn = ConexionDB.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, alumnoSeleccionado.getNombreCompleto());
                pstmt.setString(2, alumnoSeleccionado.getTelefono());
                pstmt.setString(3, alumnoSeleccionado.getDireccion());
                pstmt.setInt(4, alumnoSeleccionado.getSocioId());
                pstmt.setString(5, alumnoSeleccionado.getDni());
                pstmt.executeUpdate();

                menuAlumnoController.cargarAlumnosDesdeBD();
            } catch (SQLException e) {
                System.out.println("Error al actualizar alumno: " + e.getMessage());
            }

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
