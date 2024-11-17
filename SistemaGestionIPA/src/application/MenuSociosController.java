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

public class MenuSociosController {

    @FXML
    private TableView<Socio> tablaSocios;
    @FXML
    private TableColumn<Socio, String> colNombre;
    @FXML
    private TableColumn<Socio, String> colIdSocio;
    @FXML
    private TableColumn<Socio, String> colDni;
    @FXML
    private TableColumn<Socio, String> colTelefono;
    @FXML
    private TableColumn<Socio, String> colDireccion;
    @FXML
    private TableColumn<Socio, String> colEmail;
    @FXML
    private Button btnEliminarSocio;
    @FXML
    private Button btnVolverMenuPrincipal;
    
    private ObservableList<Socio> listaSocios = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
    	colIdSocio.setCellValueFactory(new PropertyValueFactory<>("id_Socio"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tablaSocios.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Cargar los socios desde la base de datos
        cargarSociosDesdeBD();
    }

    public void cargarSociosDesdeBD() {
        listaSocios.clear();
        String sql = "SELECT id_Socio, nombreCompleto, dni, telefono, direccion, email FROM Socios"; // Incluye idSocio
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Socio socio = new Socio(
                    rs.getInt("id_Socio"),  // Obtener el ID del socio
                    rs.getString("nombreCompleto"),
                    rs.getString("dni"),
                    rs.getString("telefono"),
                    rs.getString("direccion"),
                    rs.getString("email")
                );
                listaSocios.add(socio);
            }
            tablaSocios.setItems(listaSocios);
        } catch (SQLException e) {
            System.out.println("Error al cargar socios: " + e.getMessage());
        }
    }


    @FXML
    private void abrirAgregarSocio() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/forms/SociosNuevo.fxml"));
        Parent root = loader.load();

        AgregarSocioController agregarController = loader.getController();
        agregarController.setMenuSociosController(this);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void abrirModificarSocio() throws IOException {
        Socio socioSeleccionado = tablaSocios.getSelectionModel().getSelectedItem();
        if (socioSeleccionado != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/forms/SociosModificar.fxml"));
            Parent root = loader.load();

            ModificarSocioController modificarController = loader.getController();
            modificarController.setSocio(socioSeleccionado);
            modificarController.setMenuSociosController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            System.out.println("Por favor, selecciona un socio de la tabla.");
        }
    }

    @FXML
    private void eliminarSocio() {
        Socio socioSeleccionado = tablaSocios.getSelectionModel().getSelectedItem();
        if (socioSeleccionado != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación de eliminación");
            alert.setHeaderText(null);
            alert.setContentText("¿Estás seguro de que deseas eliminar el socio seleccionado?");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                String sqlSocios = "DELETE FROM Socios WHERE id_Socio = ?";
                String sqlCuotas = "DELETE FROM Cuotas WHERE socio_id = ?"; // Elimina cuotas asociadas
                try (Connection conn = ConexionDB.getConnection();
                     PreparedStatement pstmtSocios = conn.prepareStatement(sqlSocios);
                     PreparedStatement pstmtCuotas = conn.prepareStatement(sqlCuotas)) {
                    // Eliminar cuotas primero
                    pstmtCuotas.setInt(1, socioSeleccionado.getId_Socio());
                    pstmtCuotas.executeUpdate();
                    // Eliminar socio
                    pstmtSocios.setInt(1, socioSeleccionado.getId_Socio());
                    pstmtSocios.executeUpdate();
                    cargarSociosDesdeBD(); // Refresca la tabla después de la eliminación
                } catch (SQLException e) {
                    System.out.println("Error al eliminar socio: " + e.getMessage());
                }
            }
        } else {
            System.out.println("Por favor, selecciona un socio de la tabla para eliminar.");
        }
    }




    public void refrescarTabla() {
        cargarSociosDesdeBD();
    }

    public void agregarSocio(Socio nuevoSocio) {
        String sql = "INSERT INTO Socios (nombreCompleto, dni, telefono, direccion, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nuevoSocio.getNombreCompleto());
            pstmt.setString(2, nuevoSocio.getDni());
            pstmt.setString(3, nuevoSocio.getTelefono());
            pstmt.setString(4, nuevoSocio.getDireccion());
            pstmt.setString(5, nuevoSocio.getEmail());
            pstmt.executeUpdate();
            cargarSociosDesdeBD();
        } catch (SQLException e) {
            System.out.println("Error al agregar socio: " + e.getMessage());
        }
    }

    public void actualizarSocio(Socio socioModificado) {
        String sql = "UPDATE Socios SET nombreCompleto = ?, telefono = ?, direccion = ?, email = ? WHERE dni = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, socioModificado.getNombreCompleto());
            pstmt.setString(2, socioModificado.getTelefono());
            pstmt.setString(3, socioModificado.getDireccion());
            pstmt.setString(4, socioModificado.getEmail());
            pstmt.setString(5, socioModificado.getDni());
            pstmt.executeUpdate();
            cargarSociosDesdeBD();
        } catch (SQLException e) {
            System.out.println("Error al actualizar socio: " + e.getMessage());
        }
    }
   

    public void volverMenuPrincipal() {
        try {
            // Cargar el FXML del Menu Principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/forms/Menu.fxml"));
            Parent root = loader.load();

            // Obtener la ventana actual (la ventana de Socios, Alumnos o Cuotas)
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
