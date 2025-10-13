package com.sistema.ui;

import com.sistema.dao.ProductoDAO;
import com.sistema.modelo.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

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
        // Actualizar numero de productos
        productosTotalesLabel.setText(String.valueOf(new ProductoDAO().cantidadProductos()));
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
                    System.out.println("Reordenar producto: " + producto.getNombre());
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


