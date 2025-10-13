package com.sistema.ui;

import com.sistema.dao.ProductoDAO;
import com.sistema.modelo.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.util.List;

public class ProductosController {

    @FXML
    private TextField buscarProducto;

    @FXML
    private Button btnAgregarProducto;

    @FXML
    private TableView<Producto> tablaProductos;

    @FXML
    private TableColumn<Producto, String> codigoProducto;

    @FXML
    private TableColumn<Producto, String> nombreProducto;

    @FXML
    private TableColumn<Producto, String> precioProducto;

    @FXML
    private TableColumn<Producto, Integer> stockDisponible;

    @FXML
    private TableColumn<Producto, Void> accionesProducto;

    @FXML
    public void initialize(){
        codigoProducto.setCellValueFactory(new PropertyValueFactory<>("codigoSku"));
        nombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        precioProducto.setCellValueFactory(new PropertyValueFactory<>("precioVenta"));
        stockDisponible.setCellValueFactory(new PropertyValueFactory<>("stockActual"));

        cargarListaProductos();

        agregarColumnaAccion();
    }

    public void agregarProductoEvent(ActionEvent actionEvent) {

    }

    public void buscarNombreProducto(ActionEvent actionEvent) {
    }

    /**
     * Funcion para cargar los datos de los productos a la tabla
     */
    private void cargarListaProductos(){
        ProductoDAO dao = new ProductoDAO();
        List<Producto> lista = dao.listaProductos();

        ObservableList<Producto> datos = FXCollections.observableArrayList(lista);
        tablaProductos.setItems(datos);
    }

    private void agregarColumnaAccion() {
        accionesProducto.setCellFactory(param -> new TableCell<Producto, Void>() {

            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox contenedorBotones = new HBox(8); // separación entre botones

            {
                // Estilos
                btnEliminar.setStyle("-fx-background-color: #cf0808; -fx-text-fill: white; -fx-font-size: 13px; " +
                        "-fx-font-weight: bold; -fx-background-radius: 7;");
                btnEditar.setStyle("-fx-background-color: #5882E8; -fx-text-fill: white; -fx-font-size: 13px; " +
                        "-fx-font-weight: bold; -fx-background-radius: 7;");

                // Margen interno y alineación
                contenedorBotones.setAlignment(Pos.CENTER);
                contenedorBotones.getChildren().addAll(btnEditar, btnEliminar);

                // Acciones
                btnEditar.setOnAction(event -> {
                    Producto producto = getTableView().getItems().get(getIndex());
                    System.out.println("Editar producto: " + producto.getNombre());
                });

                btnEliminar.setOnAction(event -> {
                    Producto producto = getTableView().getItems().get(getIndex());
                    System.out.println("Eliminar producto: " + producto.getNombre());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(contenedorBotones);
                }
            }
        });
    }

}
