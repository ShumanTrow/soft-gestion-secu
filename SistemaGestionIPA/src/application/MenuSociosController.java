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
import java.util.Optional;

	//Clase controladora que gestiona todo el Menu de Socios.
public class MenuSociosController {

    @FXML
    private TableView<Socio> tablaSocios;

    @FXML
    private TableColumn<Socio, String> colNombre;

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

    private ObservableList<Socio> listaSocios = FXCollections.observableArrayList();

    // Método que vincula las columnas con propiedades de la clase Socio.
    @FXML
    private void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tablaSocios.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        // Agrego algunos socios de ejemplo para que se vean en la tabla.
        Socio socio1 = new Socio("Juan Perez", "12345678", "123456789", "Calle 1", "juanperez@mail.com");
        Socio socio2 = new Socio("Maria Lopez", "87654321", "987654321", "Calle 2", "marialopez@mail.com");
        Socio socio3 = new Socio("Carlos Gómez", "45678912", "654987321", "Calle 3", "carlosgomez@mail.com");

        listaSocios.addAll(socio1, socio2, socio3);
        
        // Configura la tabla con los datos iniciales
        tablaSocios.setItems(listaSocios);
    }

    @FXML
    private void abrirAgregarSocio() throws IOException {
        // Carga el formulario de AgregarSocio.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/forms/SociosNuevo.fxml"));
        Parent root = loader.load();

        // Obtiene el controlador de AgregarSocio para pasar el controlador de MenuSocios.
        AgregarSocioController agregarController = loader.getController();
        agregarController.setMenuSociosController(this);

        // Muestra la ventana de Agregar Socio Nuevo.
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void abrirModificarSocio() throws IOException {
        // Verifica si hay un socio seleccionado.
        Socio socioSeleccionado = tablaSocios.getSelectionModel().getSelectedItem();
        if (socioSeleccionado != null) {
            // Carga el formulario de ModificarSocio.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/forms/SociosModificar.fxml"));
            Parent root = loader.load();
            	
            // Obtiene el controlador de ModificarSocio para pasar los datos del socio seleccionado.
            ModificarSocioController modificarController = loader.getController();
            modificarController.setSocio(socioSeleccionado);
            modificarController.setMenuSociosController(this);

            // Se muestra la ventana de Modificar Socio
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            System.out.println("Por favor, selecciona un socio de la tabla.");
        }
    }
    
    @FXML
    private void eliminarSocio() {
        // Obtiene el socio seleccionado en la tabla.
        Socio socioSeleccionado = tablaSocios.getSelectionModel().getSelectedItem();

        if (socioSeleccionado != null) {
            // Crea el cuadro de diálogo de confirmación.
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación de eliminación");
            alert.setHeaderText(null);
            alert.setContentText("¿Estás seguro de que deseas eliminar el socio seleccionado?");
            
            // Muestra el cuadro de diálogo y espera la respuesta del usuario.
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Elimina el socio de la lista y refresca la tabla.
                listaSocios.remove(socioSeleccionado);
                tablaSocios.refresh();
            }
        } else {
            System.out.println("Por favor, selecciona un socio de la tabla para eliminar.");
        }
    }

    //Mini método para refrescar la tabla.
    public void refrescarTabla() {
        tablaSocios.refresh();
    }
  
    // Agrega el nuevo socio al array, proximamente será a la BD por medio de un Insert.
    public void agregarSocio(Socio nuevoSocio) {
        listaSocios.add(nuevoSocio);

        // Refresca la tabla para mostrar el nuevo socio.
        tablaSocios.refresh();
    }

    public void actualizarSocio(Socio socioModificado) {
        // Refresca la tabla para reflejar los cambios.
        tablaSocios.refresh();
    }
}
