package com.sistema.ui;

import com.sistema.modelo.Proveedor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ProveedoresController {

    @FXML
    private TextField buscarTextfield;

    @FXML
    private ListView<Proveedor> listaView;

    @FXML
    private Label nombreProvLabel,nombreContLabel,telefonoLabel, correoLabel;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab resumenTab, productosTab;

    @FXML
    private Button btnAgregar, btnEditar, btnEliminar;

    @FXML
    private TableView<Proveedor> tablaProd;

    @FXML
    private TableColumn<Proveedor, String> prodName,prodDescripcion, prodPrecio;

    @FXML
    private TableColumn<Proveedor, Integer> prodCantidad;


    @FXML
    public void buscarProvAction(ActionEvent actionEvent) {
    }

    @FXML
    public void btnEditarAction(ActionEvent actionEvent) {
    }

    @FXML
    public void btnElimnarAction(ActionEvent actionEvent) {
    }

    @FXML
    public void btnAgregarAction(ActionEvent actionEvent) {
    }
}
