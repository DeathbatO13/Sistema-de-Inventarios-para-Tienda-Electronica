package com.sistema.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

public class EditarVentaController {

    @FXML
    private Label productoLabel;

    @FXML
    private Spinner<Integer> cantidad;

    @FXML
    private TextField precioTF;

    @FXML
    private ComboBox<String> vendedor;


    public void btnGuardarAction(ActionEvent actionEvent) {
    }

    public void btnCancelarAction(ActionEvent actionEvent) {
    }
}
