package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VerCuotasController {

    @FXML
    private TableView<Cuota> tableCuotas;
    @FXML
    private TableColumn<Cuota, Integer> colIdCuota;
    @FXML
    private TableColumn<Cuota, LocalDate> colFechaCreacion;
    @FXML
    private TableColumn<Cuota, LocalDate> colFechaVencimiento;
    @FXML
    private TableColumn<Cuota, String> colSocioAPagar;
    @FXML
    private TableColumn<Cuota, String> colEstado;
    @FXML
    private TableColumn<Cuota, Double> colMonto;
    @FXML
    private TableColumn<Cuota, Boolean> colPagado;
    @FXML
    private TableColumn<Cuota, String> colAlumnoAsociado;
    @FXML
    private Button btnVolverMenuPrincipal;
    @FXML
    private Button btnActualizarCuotas;
    @FXML
    private Button btnCambiarPrecio; // Nuevo botón
    @FXML
    private TextField txtBuscarSocio;

    private ObservableList<Cuota> listaCuotas = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colIdCuota.setCellValueFactory(new PropertyValueFactory<>("idCuota"));
        colFechaCreacion.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));
        colFechaVencimiento.setCellValueFactory(new PropertyValueFactory<>("fechaVencimiento"));
        colSocioAPagar.setCellValueFactory(new PropertyValueFactory<>("socioAPagar"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        colAlumnoAsociado.setCellValueFactory(new PropertyValueFactory<>("alumnoAsociado"));

        colPagado.setCellValueFactory(cellData -> cellData.getValue().pagadoProperty());
        colPagado.setCellFactory(CheckBoxTableCell.forTableColumn(colPagado));
        colPagado.setEditable(true);
        tableCuotas.setEditable(true);

        aplicarFormatoFecha();
        convertirEstadoAMayuscula();
        cargarCuotasDesdeBD();
        agregarListenerCambioPago();

        btnActualizarCuotas.setOnAction(event -> refrescarTabla());

        // Acción del nuevo botón para abrir la mini interfaz
        btnCambiarPrecio.setOnAction(event -> mostrarCambioPrecio());

        // Agregar listener al campo de búsqueda
        txtBuscarSocio.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarCuotas(newValue);
        });
    }

    private void cargarCuotasDesdeBD() {
        String sql = "SELECT id_cuota, fechaCreacion, fechaVencimiento, socio_nombre, estado, monto, alumno_nombre FROM Cuotas";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            listaCuotas.clear();
            while (rs.next()) {
                String estado = rs.getString("estado");
                Cuota cuota = new Cuota(
                        rs.getInt("id_cuota"),
                        rs.getDate("fechaCreacion").toLocalDate(),
                        rs.getDate("fechaVencimiento").toLocalDate(),
                        rs.getString("socio_nombre"),
                        estado,
                        rs.getDouble("monto"),
                        rs.getString("alumno_nombre")
                );
                listaCuotas.add(cuota);
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar las cuotas: " + e.getMessage());
        }
        tableCuotas.setItems(listaCuotas);
    }

    public void refrescarTabla() {
        cargarCuotasDesdeBD();
        tableCuotas.refresh();
    }

    private void aplicarFormatoFecha() {
        colFechaCreacion.setCellFactory(col -> new TableCell<>() {
            private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText((item != null && !empty) ? item.format(formatter) : null);
            }
        });

        colFechaVencimiento.setCellFactory(col -> new TableCell<>() {
            private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText((item != null && !empty) ? item.format(formatter) : null);
            }
        });
    }

    private void convertirEstadoAMayuscula() {
        colEstado.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText((item != null && !empty) ? item.substring(0, 1).toUpperCase() + item.substring(1).toLowerCase() : null);
            }
        });
    }

    private void agregarListenerCambioPago() {
        tableCuotas.getItems().forEach(cuota -> 
            cuota.pagadoProperty().addListener((observable, oldValue, newValue) -> {
                cuota.setEstado(newValue ? "pagada" : "pendiente");
                actualizarEstadoPago(cuota);
            })
        );
    }

    private void actualizarEstadoPago(Cuota cuota) {
        String sql = "UPDATE Cuotas SET estado = ? WHERE id_cuota = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cuota.getEstado());
            pstmt.setInt(2, cuota.getIdCuota());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar el estado de pago: " + e.getMessage());
        }
    }

    private void filtrarCuotas(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            tableCuotas.setItems(listaCuotas); 
        } else {
            ObservableList<Cuota> cuotasFiltradas = FXCollections.observableArrayList();
            for (Cuota cuota : listaCuotas) {
                if (cuota.getSocioAPagar().toLowerCase().contains(filtro.toLowerCase())) {
                    cuotasFiltradas.add(cuota);
                }
            }
            tableCuotas.setItems(cuotasFiltradas);
        }
    }

    @FXML
    private void mostrarCambioPrecio() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/forms/CambiarPrecio.fxml"));
            Parent root = loader.load();

            // Crear una nueva ventana para cambiar el precio
            Stage stage = new Stage();
            stage.setTitle("Cambiar Precio de Cuotas Pendientes");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Error al cargar la interfaz de cambio de precio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void volverMenuPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/forms/Menu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnVolverMenuPrincipal.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Error al cargar el menú principal: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
