package com.sistema.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RecuperacionController {

    @FXML
    private TextField correoTextField, codigoVerificacion;

    @FXML
    private PasswordField primeraContra, segundaContra;

    @FXML
    private Button btnEnviarCorreo, btnVerificarCodigo, btnConfirmarCambio;

    @FXML
    private Label estadoCodigo, estadoContra, cambioExistoso;

    @FXML
    public void btnEnviarCorreoAction(ActionEvent actionEvent) {
    }

    @FXML
    public void btnVerificarAction(ActionEvent actionEvent) {
    }

    @FXML
    public void btnConfirmarAction(ActionEvent actionEvent) {
    }
}
