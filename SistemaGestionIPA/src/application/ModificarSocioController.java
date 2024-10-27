package application;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

	//Clase controladora que se encarga de el apartado Modificar Socio.
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

    private Socio socioSeleccionado; // Para almacenar el socio a modificar.
    private MenuSociosController menuSociosController; // Para poder refrescar la tabla en MenuSocios.

    // Este método será llamado desde el controlador de MenuSocios
    public void setSocio(Socio socio) {
        this.socioSeleccionado = socio;

        // Se comprueba que todos los campos de texto estén inicializados antes de usarlos.
        if (txtNombreCompleto != null && txtDni != null && txtTelefono != null && txtDireccion != null && txtEmail != null) {
            // Muestra los datos del socio seleccionado en los campos de texto.
            txtNombreCompleto.setText(socio.getNombreCompleto());
            txtDni.setText(socio.getDni());
            txtTelefono.setText(socio.getTelefono());
            txtDireccion.setText(socio.getDireccion());
            txtEmail.setText(socio.getEmail());
        } else {
            System.out.println("Error: Los campos de texto no están inicializados correctamente.");
        }
    }

    // Este método será llamado desde el controlador de MenuSocios para poder actualizar la tabla
    public void setMenuSociosController(MenuSociosController controller) {
        this.menuSociosController = controller;
    }

    // Actualizar los datos del socio con los valores modificados en los campos de texto.
    @FXML
    private void guardarCambios() {
        if (socioSeleccionado != null) {
            socioSeleccionado.setNombreCompleto(txtNombreCompleto.getText());
            socioSeleccionado.setDni(txtDni.getText());
            socioSeleccionado.setTelefono(txtTelefono.getText());
            socioSeleccionado.setDireccion(txtDireccion.getText());
            socioSeleccionado.setEmail(txtEmail.getText());

            // Refresca la tabla en MenuSociosController.
            if (menuSociosController != null) {
                menuSociosController.refrescarTabla();
            } else {
                System.out.println("Error: menuSociosController no está inicializado.");
            }

            // Cierra la ventana de ModificarSocio.
            Stage stage = (Stage) txtNombreCompleto.getScene().getWindow();
            stage.close();
        }
    }
    // Cierra la ventana sin realizar cambios.
    @FXML
    private void cerrarFormulario() {
        Stage stage = (Stage) txtNombreCompleto.getScene().getWindow();
        stage.close();
    }
}
