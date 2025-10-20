package com.sistema.ui;

import com.sistema.servicios.SistemaAutenticacion;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

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
    private Hyperlink linkRecContra;

    private int contadorInicio = 0;

    /**
     * Funcion para iniciar sesion, activada por el boton en la vista del login, verifica
     * que el usuario exista y las credenciales sean correctas.
     * @param actionEvent evento del boton inicar sesion.
     */
    @FXML
    public void iniciarSesionAction(ActionEvent actionEvent) {
        String correo = campoCorreo.getText();
        String contrasena = passwordField.getText();
        SistemaAutenticacion auth = new SistemaAutenticacion();
        boolean estadoInicioSesion = auth.iniciarSesion(correo, contrasena);

        if (estadoInicioSesion) {
            System.out.println("Inicio de sesión correcto");
            errorLabel.setVisible(false);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PanelPrincipalVista.fxml"));
                Parent root = loader.load();
                // Crear una nueva ventana (Stage)
                Stage stage = new Stage();
                Image icono = new Image(getClass().getResource("/img/Icon.png").toExternalForm());
                stage.getIcons().add(icono);
                stage.setTitle("Panel Principal");
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.show();

                // Cerrar la ventana actual
                Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                currentStage.close();
            }
            catch (IOException e) {
                System.err.println("Error al lanzar pantalla de registro" + e.getMessage());
                e.printStackTrace();
            }

        } else {
            contadorInicio++;
            errorLabel.setVisible(true);
            linkRecContra.setVisible(true);
            System.out.println("Credenciales incorrectas");

            if (contadorInicio < 5) {
                errorLabel.setText("Credenciales incorrectas. Intento " + contadorInicio + " de 5");
            } else {
                bloqueoIntentos();
            }
        }
    }

    /**
     * Funcion para desplegar la vista de registro desde la ventana login
     * @param actionEvent evento del boton registrarse
     */
    @FXML
    public void registrarseAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RegistroVista.fxml"));
            Parent root = loader.load();
            // Crear una nueva ventana (Stage)
            Stage stage = new Stage();
            Image icono = new Image(getClass().getResource("/img/Icon.png").toExternalForm());
            stage.getIcons().add(icono);
            stage.setTitle("Registro de Usuario");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();

            // Cerrar la ventana actual (login)
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();
        }
        catch (IOException e) {
            System.err.println("Error al lanzar pantalla de registro" + e.getMessage());
            e.printStackTrace();
        }
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

    /**
     * Funcion para lanzar la ventana de recuperacion de contraseña
     * @param actionEvent event del link
     */
    public void recuperarContraAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RecuperacionPassword.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            Image icono = new Image(getClass().getResource("/img/Icon.png").toExternalForm());
            stage.getIcons().add(icono);
            stage.setTitle("Recuperar Contraseña");
            stage.setScene(new Scene(root));

            // --- Configurar como ventana modal ---
            stage.initModality(Modality.APPLICATION_MODAL);

            // Mostrar y esperar a que se cierre
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
