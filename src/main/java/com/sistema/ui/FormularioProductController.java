package com.sistema.ui;


import com.sistema.dao.ProveedorDAO;
import com.sistema.modelo.Proveedor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class FormularioProductController {

    @FXML
    private TextField codigoNuevo, nombreNuevo, descripcionNuevo,
            preCompraNuevo, preVentaNuevo,
            stockActNuevo, stockMinNuevo;

    @FXML
    private ComboBox<String> comboProveedor;

    @FXML
    private Button btnCancelar, btnGuardar;

    @FXML
    private Label errorLabel, infoLabel;

    /**
     * Funcion para inicializar la vista del formuario
     */
    @FXML
    private void initialize(){
        infoLabel.setText("Si no encuantras el proveedor, tienes que \n"
                + "agregarlo en el panel correspondiente.");
        infoLabel.setVisible(true);

        cargarProveedoresAccion();

    }


    @FXML
    public void btnGuardarEvent(ActionEvent actionEvent) {
    }

    @FXML
    public void btnCancelarEvent(ActionEvent actionEvent){

    }

    /**
     * Funcion para buscar lista de proveedores y cargarlos al combobox
     */
    @FXML
    public void cargarProveedoresAccion(){
        ProveedorDAO proveedor = new ProveedorDAO();
        List<Proveedor> listaPro = proveedor.listaProveedores();
        List<String> listaProveedores = new ArrayList<>();

        for(Proveedor pro: listaPro){
            String nombrePro = pro.getNombre();
            listaProveedores.add(nombrePro);
        }
        ObservableList<String> datos = FXCollections.observableArrayList(listaProveedores);
        comboProveedor.setItems(datos);
    }


    private boolean productoGuardado = false;

    public void guardarProducto() {
        // Validaciones básicas
        if (nombreNuevo.getText().isEmpty() || codigoNuevo.getText().isEmpty() || descripcionNuevo.getText().isEmpty()
            || preCompraNuevo.getText().isEmpty() || preVentaNuevo.getText().isEmpty() || stockActNuevo.getText().isEmpty()
            || stockMinNuevo.getText().isEmpty()) {
            errorLabel.setText("Faltan campos obligatorios.");
            return;
        }

        System.out.println("Producto agregado: " + nombreNuevo.getText());
        productoGuardado = true;

        // Cerrar ventana
        Stage stage = (Stage) nombreNuevo.getScene().getWindow();
        stage.close();
    }

    public boolean isProductoGuardado() {
        return productoGuardado;
    }


}
