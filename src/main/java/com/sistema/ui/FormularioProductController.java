package com.sistema.ui;

import com.sistema.modelo.Producto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FormularioProductController {

    @FXML
    private TextField codigoNuevo, nombreNuevo, descripcionNuevo,
            preCompraNuevo, preVentaNuevo,
            stockActNuevo, stockMinNuevo;

    @FXML
    private ComboBox<Producto> comboProveedor;

    @FXML
    private Button btnCancelar, btnGuardar;

    @FXML
    private Label errorLabel, infoLabel;

    @FXML
    private void initialize(){
        infoLabel.setText("Si no encuantras el proveedor, tienes que \n"
                + "agregarlo en el panel correspondiente.");
        infoLabel.setVisible(true);
    }


    @FXML
    public void btnGuardarEvent(ActionEvent actionEvent) {
    }

    @FXML
    public void btnCancelarEvent(ActionEvent actionEvent){

    }


    private boolean productoGuardado = false;

    public void guardarProducto() {
        // Validaciones básicas
        if (nombreNuevo.getText().isEmpty() || codigoNuevo.getText().isEmpty() || descripcionNuevo.getText().isEmpty()
            || preCompraNuevo.getText().isEmpty() || preVentaNuevo.getText().isEmpty() || stockActNuevo.getText().isEmpty()
            || stockMinNuevo.getText().isEmpty()) {
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
