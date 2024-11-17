package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SeleccionarSocioController {
    @FXML
    private TextField buscarSocio;
    @FXML
    private TableView<Socio> tablaSocios;
    @FXML
    private TableColumn<Socio, String> colNombreCompleto;
    @FXML
    private TableColumn<Socio, String> colIDSocio;
    @FXML
    private Button btnSeleccionar;

    private ObservableList<Socio> listaSocios = FXCollections.observableArrayList();
    private AgregarAlumnoController agregarAlumnoController;

    public void setAgregarAlumnoController(AgregarAlumnoController controller) {
        this.agregarAlumnoController = controller;
    }

    @FXML
    private void initialize() {
        colNombreCompleto.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        cargarSocios();
        buscarSocio.textProperty().addListener((observable, oldValue, newValue) -> filtrarSocios(newValue));
    }

    private void cargarSocios() {
        listaSocios.clear();
        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT id_Socio, nombreCompleto FROM Socios")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idSocio = resultSet.getInt("id_Socio");
                String nombreCompleto = resultSet.getString("nombreCompleto");
                // Usamos el constructor que incluye idSocio
                Socio socio = new Socio(idSocio, nombreCompleto, null, null, null, null);
                listaSocios.add(socio);
            }
            tablaSocios.setItems(listaSocios);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private void filtrarSocios(String filtro) {
        ObservableList<Socio> sociosFiltrados = FXCollections.observableArrayList();
        for (Socio socio : listaSocios) {
            if (socio.getNombreCompleto().toLowerCase().contains(filtro.toLowerCase())) {
                sociosFiltrados.add(socio);
            }
        }
        tablaSocios.setItems(sociosFiltrados);
    }

    @FXML
    private void seleccionarSocio() {
        Socio socioSeleccionado = tablaSocios.getSelectionModel().getSelectedItem();
        if (socioSeleccionado != null) {
            agregarAlumnoController.setSocioSeleccionado(socioSeleccionado);
            ((Stage) btnSeleccionar.getScene().getWindow()).close();
        }
    }
}
