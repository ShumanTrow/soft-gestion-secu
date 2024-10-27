package application;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

	//Clase controladora que gestiona todo lo relacionado con Agregar un Socio nuevo.
public class AgregarSocioController {

    @FXML
    private TextField nombreCompleto;
    
    @FXML
    private TextField dni;
    
    @FXML
    private TextField telefono;
    
    @FXML
    private TextField direccion;
    
    @FXML
    private TextField email;

    private MenuSociosController menuSociosController;

    public void setMenuSociosController(MenuSociosController controller) {
        this.menuSociosController = controller;
    }

    // Se crea un objeto socio con los datos ingresados.
    @FXML
    private void agregarSocio() {
        Socio nuevoSocio = new Socio(nombreCompleto.getText(), dni.getText(), telefono.getText(), direccion.getText(), email.getText());
        
        // Agrega el socio al array temporal en MenuSociosController.
        menuSociosController.agregarSocio(nuevoSocio);
        
        // Cierra la ventana.
        Stage stage = (Stage) nombreCompleto.getScene().getWindow();
        stage.close();
    }
    
    // Cerrar la ventana sin agregar el socio
    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) nombreCompleto.getScene().getWindow();
        stage.close();
    }
}
