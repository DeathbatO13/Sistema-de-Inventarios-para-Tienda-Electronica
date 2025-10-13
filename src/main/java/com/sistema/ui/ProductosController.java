package com.sistema.ui;

import com.sistema.modelo.Producto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

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




    public void agregarProductoEvent(ActionEvent actionEvent) {

    }

    public void buscarNombreProducto(ActionEvent actionEvent) {
    }
}
