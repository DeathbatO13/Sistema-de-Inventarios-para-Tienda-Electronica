package com.sistema.ui;

import com.sistema.servicios.SistemaAutenticacion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private Button btnIniciarSesion, btnRegistrarse;

    @FXML
    private TextField campoCorreo;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    public void iniciarSesionAction(ActionEvent actionEvent) {
        String correo = campoCorreo.getText();
        String contrasena = passwordField.getText();

        SistemaAutenticacion auth = new SistemaAutenticacion();
        boolean exito = auth.iniciarSesion(correo, contrasena);

        if (exito) {
            System.out.println("Inicio de sesión correcto");
            errorLabel.setVisible(false);
            // Aquí puedes cargar la vista principal
        } else {
            System.out.println("Credenciales incorrectas");
            errorLabel.setVisible(true);
        }
    }

    @FXML
    public void registrarseAction(ActionEvent actionEvent) {
        System.out.println("Lanzar pantalla registro");
    }

}
