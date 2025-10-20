package com.sistema.ui;

import com.sistema.dao.ProductoDAO;
import com.sistema.dao.ProveedorDAO;
import com.sistema.dao.VentasDAO;
import com.sistema.modelo.Producto;
import com.sistema.servicios.ServicioEmail;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DashboardController {

    @FXML
    private Label productosTotalesLabel, proveedoresTotalesLabel, totalVentasLabel;

    @FXML
    private TableView<Producto> tablaExistencias;

    @FXML
    private TableColumn<Producto, String> codigoColumn;

    @FXML
    private TableColumn<Producto, String> nombreColumn;

    @FXML
    private TableColumn<Producto, Integer> unidadesColumn;

    @FXML
    private TableColumn<Producto, Void> accionColumn;

    /**
     * Funcion para darle estilos a los cabezales de la tabla
     */
    @FXML
    public void initialize() {
        // ====== Configurar columnas ======
        codigoColumn.setCellValueFactory(new PropertyValueFactory<>("codigoSku"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        unidadesColumn.setCellValueFactory(new PropertyValueFactory<>("stockActual"));
        // Cargar datos
        cargarProductosBajoStock();
        // Añadir columna de acción
        agregarColumnaAccion();
        // Actualizar cantidades resumen
        productosTotalesLabel.setText(String.valueOf(new ProductoDAO().cantidadProductos()));
        proveedoresTotalesLabel.setText(String.valueOf(new ProveedorDAO().cantidadProveedores()));
        //Agregar formato de moneda para el total de ventas del mes
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        totalVentasLabel.setText(String.valueOf(formatoMoneda.format(new VentasDAO().totalVentasMes())));

    }

    /**
     * Carga los productos con bajo stock y los muestra en la tabla.
     */
    private void cargarProductosBajoStock() {
        ProductoDAO dao = new ProductoDAO();
        List<Producto> lista = dao.stockBajo();

        ObservableList<Producto> datos = FXCollections.observableArrayList(lista);
        tablaExistencias.setItems(datos);
    }

    /**
     * Agrega botón "Reordenar" en cada fila.
     */
    private void agregarColumnaAccion() {
        accionColumn.setCellFactory(param -> new TableCell<Producto, Void>() {

            private final Button btn = new Button("Reordenar");

            {
                btn.setStyle("-fx-background-color: #5882E8; -fx-text-fill: white; -fx-font-size: 14px; " +
                        "-fx-font-family: \"Corbel\", \"Segoe UI\", sans-serif; -fx-font-weight: bold; -fx-background-radius: 7;");

                btn.setOnAction(event -> {
                    Producto producto = getTableView().getItems().get(getIndex());
                    String email = new ProveedorDAO().buscarPorId(producto.getIdProveedor()).getEmail();
                    //Envio del correo comentado para no hacer pedidos falsos, dado que los correos en la
                    //db son de empresas reales.
                    /*******************
                    ServicioEmail.enviarCorreo(email,
                            "Reorden de producto",
                            "Envio este correo para reordenar 20 unidades de " + producto.getNombre());
                     *****************/
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ReordenarConfirm.fxml"));
                        Parent root = loader.load();

                        ReordenarController controller = loader.getController();
                        controller.nombreALabel(new ProveedorDAO().buscarPorId(producto.getIdProveedor()).getNombre());

                        Stage stage = new Stage();
                        Image icono = new Image(getClass().getResource("/img/Icon.png").toExternalForm());
                        stage.getIcons().add(icono);
                        stage.setTitle("Orden Realizada");
                        stage.setScene(new Scene(root));

                        // --- Configurar como ventana modal ---
                        stage.initModality(Modality.APPLICATION_MODAL);

                        // Mostrar y esperar a que se cierre
                        stage.showAndWait();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
    }

}


