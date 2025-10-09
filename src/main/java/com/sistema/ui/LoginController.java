package com.sistema.ui;

import com.sistema.servicios.SistemaAutenticacion;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class LoginController {
    @FXML
    private Button btnIniciarSesion, btnRegistrarse;

    @FXML
    private TextField campoCorreo;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private int contadorInicio = 0;

    @FXML
    public void iniciarSesionAction(ActionEvent actionEvent) {
        String correo = campoCorreo.getText();
        String contrasena = passwordField.getText();
        SistemaAutenticacion auth = new SistemaAutenticacion();
        boolean exito = auth.iniciarSesion(correo, contrasena);

        if (exito) {
            System.out.println("Inicio de sesión correcto");
            errorLabel.setVisible(false);
            // Aquí se puede cargar la vista principal
        } else {
            contadorInicio++;
            errorLabel.setVisible(true);
            System.out.println("Credenciales incorrectas");

            if (contadorInicio < 5) {
                errorLabel.setText("Credenciales incorrectas. Intento " + contadorInicio + " de 5");
            } else {
                bloqueoIntentos();
            }
        }
    }

    @FXML
    public void registrarseAction(ActionEvent actionEvent) {
        System.out.println("Lanzar pantalla registro");
    }


    /**
     * Funcion para bloquear campos por limite de intentos
     */
    private void bloqueoIntentos() {
        campoCorreo.setEditable(false);
        passwordField.setEditable(false);
        btnIniciarSesion.setDisable(true);

        final int[] segundosRestantes = {60};
        errorLabel.setText("Límite alcanzado. Espera 60 segundos para reintentar.");

        Timeline temporizador = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    segundosRestantes[0]--;
                    errorLabel.setText("Bloqueado. Reintenta en " + segundosRestantes[0] + " segundos.");
                    if (segundosRestantes[0] <= 0) {
                        campoCorreo.setEditable(true);
                        passwordField.setEditable(true);
                        btnIniciarSesion.setDisable(false);
                        contadorInicio = 0;
                        errorLabel.setText("Puedes intentar iniciar sesión nuevamente.");
                    }
                })
        );
        temporizador.setCycleCount(60);
        temporizador.play();
    }

}
