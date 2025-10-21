package com.sistema.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ProductoEditorController {

    @FXML
    private Label nombreLabel;

    @FXML
    private TextField codigoTF, nombreTF, descripcionTF, preCompraTF, preVentaTF, stockActualTF, stockMinTF;

    @FXML
    private ComboBox<String> proveedorCB;

    @FXML
    public void btnConfirmarAction(ActionEvent actionEvent) {
    }

    @FXML
    public void btnCancelarAction(ActionEvent actionEvent) {
    }
}
