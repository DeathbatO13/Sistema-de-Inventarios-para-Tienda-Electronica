package com.sistema.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FormularioProductController {

    @FXML
    private TextField codigoNuevo, nombreNuevo, descripcionNuevo,
            preCompraNuevo, preVentaNuevo,
            stockActNuevo, stockMinNuevo, proveedorNuevo;

    @FXML
    private Button btnCancelar, btnGuardar;


    @FXML
    public void btnGuardarEvent(ActionEvent actionEvent) {
    }

    @FXML
    public void btnCancelarEvent(ActionEvent actionEvent){

    }


    private boolean productoGuardado = false;

    public void guardarProducto() {
        // Validaciones básicas
        if (nombreNuevo.getText().isEmpty() || codigoNuevo.getText().isEmpty()) {
            System.out.println("Faltan campos obligatorios.");
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
