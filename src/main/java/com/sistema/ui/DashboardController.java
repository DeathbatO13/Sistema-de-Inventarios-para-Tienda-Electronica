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


/**
 * Controlador para la vista del panel principal (dashboard) de la aplicación.
 * Gestiona la tabla de productos con bajo stock, configura las columnas de la tabla,
 * muestra estadísticas de productos, proveedores y ventas del mes, y permite reordenar productos.
 */
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
     * Inicializa los componentes de la vista del panel principal.
     * Configura las columnas de la tabla de productos con bajo stock, carga los datos iniciales,
     * agrega una columna de acción con botones de reorden, y actualiza las etiquetas con el total
     * de productos, proveedores y ventas del mes en formato de moneda.
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
     * Carga los productos con bajo stock desde la base de datos y los muestra en la tabla.
     */
    private void cargarProductosBajoStock() {
        ProductoDAO dao = new ProductoDAO();
        List<Producto> lista = dao.stockBajo();

        ObservableList<Producto> datos = FXCollections.observableArrayList(lista);
        tablaExistencias.setItems(datos);
    }

    /**
     * Agrega una columna de acción a la tabla con un botón "Reordenar" para cada producto.
     * Al hacer clic, genera una alerta simulando el envío de un correo al proveedor para reordenar el producto.
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
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Producto Reordenado");
                    alert.setHeaderText(null);
                    alert.setContentText("Orden creada, correo enviado a proveedor: " + new ProveedorDAO()
                            .buscarPorId(producto.getIdProveedor()).getNombre());
                    alert.initOwner(tablaExistencias.getScene().getWindow());
                    alert.showAndWait();

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


