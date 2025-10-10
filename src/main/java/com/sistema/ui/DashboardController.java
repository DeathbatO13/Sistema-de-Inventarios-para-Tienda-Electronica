package com.sistema.ui;

import com.sistema.dao.ProductoDAO;
import com.sistema.modelo.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableViewSkin;
import javafx.scene.control.skin.TableHeaderRow;

import java.util.List;

public class DashboardController {

    @FXML
    private Label productosTotalesLabel, proveedoresTotalesLabel, totalVentasLabel;

    @FXML
    private TableView<Producto> tablaExistencias;

    @FXML
    private TableColumn<Producto, String> codigoColumn;

    @FXML
    private TableColumn<Producto, Integer> nombreColumn;

    @FXML
    private TableColumn<Producto, String> unidadesColumn;

    @FXML
    private TableColumn<Producto, Void> accionColumn;

    /**
     * Funcion para darle estilos a los cabezales de la tabla
     */
    @FXML
    public void initialize() {
        // ====== Estilos de cabecera ======
        tablaExistencias.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            if (newSkin instanceof TableViewSkin<?>) {
                TableViewSkin<?> skin = (TableViewSkin<?>) newSkin;

                for (Node node : skin.getChildren()) {
                    if (node instanceof TableHeaderRow) {
                        TableHeaderRow header = (TableHeaderRow) node;
                        header.setStyle("-fx-background-color: transparent; -fx-font-family: 'Corbel'; -fx-font-size: 14px;");
                    }
                }
            }
        });

        // ====== Configurar columnas ======
        codigoColumn.setCellValueFactory(new PropertyValueFactory<>("codigoSku"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        unidadesColumn.setCellValueFactory(new PropertyValueFactory<>("stockActual"));

        // Cargar datos
        cargarProductosBajoStock();

        // Añadir columna de acción
        agregarColumnaAccion();
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
                btn.setStyle("-fx-background-color: #5882E8; -fx-text-fill: white; -fx-font-size: 13px; -fx-background-radius: 5;");
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


