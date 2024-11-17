package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MenuAlumnoController {

    @FXML
    private TableView<Alumno> tablaAlumnos;
    @FXML
    private TableColumn<Alumno, Integer> colID;
    @FXML
    private TableColumn<Alumno, String> colNombre;
    @FXML
    private TableColumn<Alumno, String> colDni;
    @FXML
    private TableColumn<Alumno, String> colTelefono;
    @FXML
    private TableColumn<Alumno, String> colDireccion;
    @FXML
    private TableColumn<Alumno, Integer> colSocioAsociado;
    @FXML
    private Button btnEliminarAlumno;
    @FXML
    private Button btnVolverMenuPrincipal;
    

    private ObservableList<Alumno> listaAlumnos = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colID.setCellValueFactory(new PropertyValueFactory<>("idAlumno"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colSocioAsociado.setCellValueFactory(new PropertyValueFactory<>("socioNombre"));
        
        tablaAlumnos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        cargarAlumnosDesdeBD();
    }

    public void cargarAlumnosDesdeBD() {
        listaAlumnos.clear();
        String sql = "SELECT a.*, s.nombreCompleto AS nombreSocioAsociado FROM Alumnos a LEFT JOIN Socios s ON a.socio_id = s.id_Socio";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Alumno alumno = new Alumno(
                    rs.getInt("id_alumno"),
                    rs.getString("nombreCompleto"),
                    rs.getString("dni"),
                    rs.getString("telefono"),
                    rs.getString("direccion"),
                    rs.getInt("socio_id"),
                    rs.getString("nombreSocioAsociado")
                );
                listaAlumnos.add(alumno);
            }
            tablaAlumnos.setItems(listaAlumnos);
        } catch (SQLException e) {
            System.out.println("Error al cargar alumnos: " + e.getMessage());
        }
    }

    @FXML
    private void abrirAgregarAlumno() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/forms/AlumnosNuevo.fxml"));
        Parent root = loader.load();

        AgregarAlumnoController agregarController = loader.getController();
        agregarController.setMenuAlumnoController(this);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void abrirModificarAlumno() throws IOException {
        Alumno alumnoSeleccionado = tablaAlumnos.getSelectionModel().getSelectedItem();
        if (alumnoSeleccionado != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/forms/AlumnosModificar.fxml"));
            Parent root = loader.load();

            ModificarAlumnoController modificarController = loader.getController();
            modificarController.setAlumno(alumnoSeleccionado);
            modificarController.setMenuAlumnoController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            System.out.println("Por favor, selecciona un alumno de la tabla.");
        }
    }

    @FXML
    private void eliminarAlumno() {
        Alumno alumnoSeleccionado = tablaAlumnos.getSelectionModel().getSelectedItem();
        if (alumnoSeleccionado != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación de eliminación");
            alert.setHeaderText(null);
            alert.setContentText("¿Estás seguro de que deseas eliminar el alumno seleccionado? Esto también eliminará todas sus cuotas asociadas.");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                String sql = "DELETE FROM Alumnos WHERE id_alumno = ?";
                try (Connection conn = ConexionDB.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, alumnoSeleccionado.getIdAlumno());
                    pstmt.executeUpdate();
                    cargarAlumnosDesdeBD();
                } catch (SQLException e) {
                    System.out.println("Error al eliminar alumno: " + e.getMessage());
                }
            }
        } else {
            System.out.println("Por favor, selecciona un alumno de la tabla para eliminar.");
        }
    }

    public void refrescarTabla() {
        cargarAlumnosDesdeBD();
    }

    public void agregarAlumno(Alumno nuevoAlumno) {
        String sql = "INSERT INTO Alumnos (nombreCompleto, dni, telefono, direccion, email, socio_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nuevoAlumno.getNombreCompleto());
            pstmt.setString(2, nuevoAlumno.getDni());
            pstmt.setString(3, nuevoAlumno.getTelefono());
            pstmt.setString(4, nuevoAlumno.getDireccion());
            pstmt.setInt(5, nuevoAlumno.getSocioId());
            pstmt.executeUpdate();
            cargarAlumnosDesdeBD();
        } catch (SQLException e) {
            System.out.println("Error al agregar alumno: " + e.getMessage());
        }
    }

    public void actualizarAlumno(Alumno alumnoModificado) {
        String sql = "UPDATE Alumnos SET nombreCompleto = ?, telefono = ?, direccion = ?, email = ?, socio_id = ? WHERE id_alumno = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, alumnoModificado.getNombreCompleto());
            pstmt.setString(2, alumnoModificado.getTelefono());
            pstmt.setString(3, alumnoModificado.getDireccion());
            pstmt.setInt(4, alumnoModificado.getSocioId());
            pstmt.setInt(5, alumnoModificado.getIdAlumno());
            pstmt.executeUpdate();
            cargarAlumnosDesdeBD();
        } catch (SQLException e) {
            System.out.println("Error al actualizar alumno: " + e.getMessage());
        }
    }
    
    public void volverMenuPrincipal() {
        try {
            // Cargar el FXML del Menu Principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/forms/Menu.fxml"));
            Parent root = loader.load();

            // Obtener la ventana actual (la ventana de Alumnos)
            Stage stage = (Stage) btnVolverMenuPrincipal.getScene().getWindow();

            // Establecer la nueva escena (Menu Principal)
            stage.setScene(new Scene(root));

            // Mostrar la ventana con el menú principal
            stage.show();
        } catch (IOException e) {
            System.out.println("Error al cargar el menú principal: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
